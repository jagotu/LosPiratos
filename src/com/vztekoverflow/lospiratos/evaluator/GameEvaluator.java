package com.vztekoverflow.lospiratos.evaluator;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.RandomStatic;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.*;
import com.vztekoverflow.lospiratos.viewmodel.actions.*;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.CannonsAbstractVolley;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.FrontalAssault;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.MortarShot;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Port;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.COLLISION_DAMAGE;

abstract public class GameEvaluator {

    abstract public void evaluateRound(int roundNo);

    public static GameEvaluator createInstance(Game g) {

        return new StandardGameEvaluator(g);
    }

    protected GameEvaluator(Game g) {
        this.g = g;
    }

    protected Game g;
}

class StandardGameEvaluator extends GameEvaluator {

    public StandardGameEvaluator(Game g) {
        super(g);
    }

    @Override
    public void evaluateRound(int roundNo) {

        reinitialize();
        performOnAllActions(a -> {
            if (a instanceof Attack) ((Attack) a).addListener(onDamageDoneListener);
            if (a instanceof Plunder) ((Plunder) a).addListener(onPlunderRequestedListener);
        });
        evaluateVolleys();
        evaluate(Maneuver.class);
        //destroyDamagedShips(); //patri to i sem? ma se zabijet i po manevrech?
        evaluate(MortarShot.class);
        evaluate(Transaction.class);
        evaluatePlundering();
        performOnAllActions(a -> {
            if (a instanceof Attack) ((Attack) a).removeListener(onDamageDoneListener);
            if (a instanceof Plunder) ((Plunder) a).removeListener(onPlunderRequestedListener);
        });
        destroyDamagedShips();
        solveCollisions(0);
        damageCollisionAttendees();
    }

    private void reinitialize() {
        collisionAttendees = new HashSet<>();
        collisionSolverManueverIndex = new HashMap<>();
        killedShips = new HashSet<>();
        attackers = new HashMap<>();
        plunderers = new HashMap<>();
    }

    /**
     * Ships that were part of a collision and had to be shifted to solve a collision.
     */
    private Set<Ship> collisionAttendees = new HashSet<>();
    private Map<Ship,Integer> collisionSolverManueverIndex = new HashMap<>();
    private Set<Ship> killedShips = new HashSet<>();
    /**
     * Key represents target, Value (List) represents all its attackers from this round
     */
    private Map<Ship, Set<Ship>> attackers = new HashMap<>();
    private Map<Plunderable, Set<Plunder>> plunderers = new HashMap<>();

    private OnDamageDoneListener onDamageDoneListener = (Attack sender, Ship target, int damageAmount, DamageSufferedResponse response) -> {

        Ship attacker = sender.getRelatedShip();
        if (attacker == null) throw new IllegalArgumentException();
        if (response.equals(DamageSufferedResponse.hasJustBeenDestroyed)) {
            killedShips.add(target);
        }
        if (!attackers.containsKey(target)) {
            attackers.put(target, new HashSet<>());
        }
        attackers.get(target).add(attacker);

        g.getLogger().logAttack(attacker, sender, target, damageAmount);
    };

    private OnPlunderRequestedListener onPlunderRequestedListener = (Plunderable target, Plunder sender) -> {
        plunderers.putIfAbsent(target, new HashSet<>());
        plunderers.get(target).add(sender);
    };

    private void evaluateVolleys() {
        for (Ship s : g.getAllShips().values()) {
            Position oldPosition = s.getPosition().createCopy();
            for (PerformableAction a : s.getPlannedActions()) {
                if (a instanceof Maneuver || a instanceof CannonsAbstractVolley || a instanceof FrontalAssault) {
                   try {
                       a.performOnShip();
                   }catch (Exception e){
                       Warnings.exceptionCaught(toString()+".evaluateVolleys()",e);
                   }
                }
            }
            s.getPosition().setFrom(oldPosition);
        }
    }

    private void evaluate(Class<? extends Action> action) {
        performOnAllActions(a -> {
            if (action.isAssignableFrom(a.getClass())) {
                try {
                    a.performOnShip();
                }catch (Exception e){
                    Warnings.exceptionCaught(toString()+".evaluate(" + action + ")",e);
                }
            }
        });
    }

    private void evaluatePlundering(){
        evaluate(Plunder.class);
        for(Map.Entry<Plunderable, Set<Plunder>> e : plunderers.entrySet()){
            try {
                e.getKey().getPlunderedBy(e.getValue());
            }catch (Exception ex){
                Warnings.exceptionCaught(toString()+".evaluatePlundering()",ex);
            }

        }
    }

    private void damageCollisionAttendees(){
        for(Ship s: collisionAttendees){
            DamageSufferedResponse response = s.takeDamage(COLLISION_DAMAGE);
            if(response.equals(DamageSufferedResponse.hasJustBeenDestroyed)){
                destroyShip(s,null);
            }
        }
    }

    private void destroyDamagedShips() {
        for (Ship s : killedShips) {
            destroyShip(s, attackers.get(s));
            for (Ship a : attackers.get(s)){
                a.incrementXP();
            }
        }
        killedShips.clear();
        attackers.clear();
    }

    private void destroyShip(Ship s, Iterable<Ship> attackers){
        //dividePlunderedTreasure(s, attackers); //currently not supported by the game rules
        g.createAndAddNewShipwreck(s.getPosition().getCoordinate(), s.getStorage());
        s.destroyShipAndEnhancements();
        g.getLogger().logShipHasDied(s, attackers);
    }

    private void dividePlunderedTreasure(Ship from, Set<Ship> to) {
        ResourceReadOnly amount = from.getStorage().times(1 / (double) to.size());
        for (Ship receiver : to) {
            receiver.getStorage().add(amount);
            g.getLogger().logResourceGain(receiver, amount);
        }
    }

    private boolean solveCollisions(int iteration) {
        Map<AxialCoordinate, Set<Ship>> nonemptyTiles = new HashMap<>();
        for (Ship s : g.getAllShips().values()) {
            AxialCoordinate pos = s.getPosition().getCoordinate();
            nonemptyTiles.putIfAbsent(pos, new HashSet<>());
            nonemptyTiles.get(pos).add(s);
        }
        Map<AxialCoordinate, Set<Ship>> collisionTiles = new HashMap<>();
        for (Map.Entry<AxialCoordinate, Set<Ship>> e : nonemptyTiles.entrySet()) {
            if (e.getValue().size() > 1 && ! (g.getBoard().getTiles().get(e.getKey()) instanceof Port)) {
                collisionTiles.put(e.getKey(), e.getValue());
            }
        }
        if (collisionTiles.size() == 0) {
            return true;
        }
        if (iteration >= 10) return false;

        for (Map.Entry<AxialCoordinate, Set<Ship>> e : collisionTiles.entrySet()) {
            solveOneCollision(e.getKey(), e.getValue(), iteration);
        }

        boolean success = solveCollisions(iteration+1);
        if (!success && iteration == 0)
            rollback(collisionTiles);
        return success;

    }

    private void rollback(Map<AxialCoordinate, Set<Ship>> originalPositions) {
        g.getLogger().logMessage("Vyhodnocení kola", "Nepodařilo se vyřešit kolize. Rollback."); //Unable to solve collisions. Rollback.
        for(Map.Entry<AxialCoordinate, Set<Ship>> e : originalPositions.entrySet()){
            AxialCoordinate c = e.getKey();
            for(Ship s : e.getValue()){
                s.getPosition().setCoordinate(c);
            }
        }
    }

    private void solveOneCollision(AxialCoordinate collisionPosition, Set<Ship> ships, int iteration) {
        if (ships.size() == 0) {
            Warnings.makeStrongWarning(toString() + ".solveOneCollistion()", "Set<Ship> ships is empty");
            return;
        }
        Map<Ship, AxialCoordinate> newPositions = new HashMap<>();

        //following should never be null, thanks to check on the beginning of the function
        Ship heaviest = ships.stream().max(this::compareShips).get();
        for (Ship s : ships) {
            if (s.equals(heaviest))
                continue;
            //undo maneuvers from last to first (if possible (i.e. i > 0)):
            List<Maneuver> maneuvers = s.getPlannedActions().stream().filter(a -> a instanceof Maneuver).map(a -> (Maneuver) a).collect(Collectors.toList());
            //1st skip operations that were already undone by previous iterations
            collisionSolverManueverIndex.putIfAbsent(s,maneuvers.size()-1);
            int i = collisionSolverManueverIndex.get(s);
            //2nd, undo one move and all preceding rotations
            if(i <0){
                Warnings.makeWarning("collision solver",s + " cannot move backwards anymore, but there is still pending collision.");
            }
            while(i >= 0){
                maneuvers.get(i).undo();
                if(maneuvers.get(i) instanceof MoveForward)
                    break;
                i--;
            }
            collisionSolverManueverIndex.replace(s,i);

            collisionAttendees.add(s);
            newPositions.put(s, s.getPosition().getCoordinate());
        }

        g.getLogger().logCollisionSolved(collisionPosition, newPositions, iteration);
    }

    private int compareShips(Ship a, Ship b){
        int weight_a = a.getWeight();
        int weight_b = b.getWeight();

        //if the figure has stayed od this position in the last round, it should win the contest
        if(a.getPlannedActions().stream().noneMatch(p-> p instanceof MoveForward))
            weight_a = Integer.MAX_VALUE;
        if(b.getPlannedActions().stream().noneMatch(p-> p instanceof MoveForward))
            weight_b = Integer.MAX_VALUE;

        if(weight_a == weight_b){
            if(weight_a == Integer.MAX_VALUE)
                Warnings.makeWarning(toString()+".compareShips()","both ships seem to have stayed on the tile "+ a.getCoordinate() +" in the last round");
            return RandomStatic.instance.nextInt(3) -1; // {-1, 0, 1}
        }else return weight_a - weight_b;

    }

    private void performOnAllActions(Consumer<Action> c) {
        for (Ship s : g.getAllShips().values()) {
            for (Action a : s.getPlannedActions()) {
                c.accept(a);
            }
        }
    }
}
