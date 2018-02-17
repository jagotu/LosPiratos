package com.vztekoverflow.lospiratos.evaluator;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
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
            if (a instanceof Attack) ((Attack) a).addOnDamageDoneListener(onDamageDoneListener);
        });
        evaluateVolleys();
        evaluate(Maneuver.class);
        evaluate(MortarShot.class);
        evaluate(Transaction.class);
        evaluatePlundering();
        performOnAllActions(a -> {
            if (a instanceof Attack) ((Attack) a).removeOnDamageDoneListener(onDamageDoneListener);
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

    private void evaluateVolleys() {
        for (Ship s : g.getAllShips().values()) {
            Position oldPosition = s.getPosition().createCopy();
            for (PerformableAction a : s.getPlannedActions()) {
                if (a instanceof Maneuver || a instanceof CannonsAbstractVolley || a instanceof FrontalAssault) {
                    a.performOnShip();
                }
            }
            s.getPosition().setFrom(oldPosition);
        }
    }

    private void evaluate(Class<? extends Action> action) {
        performOnAllActions(a -> {
            if (action.isAssignableFrom(a.getClass())) {
                a.performOnShip();
            }
        });
    }

    private void evaluatePlundering(){
        //predtim si musim do Plunder action ulozit listenery "tedka pludruju tady"
        //projdu listenery, sestavim si mapu <plunderable, set<ship>>
        //pro kazdou pluderable polozku provedu rozdeleni koristi (haha, na to uz mam funkci z deleni)
    }

    private void damageCollisionAttendees(){
        for(Ship s: collisionAttendees){
            DamageSufferedResponse response = s.takeDamage(FrontalAssault.FrontalAssaultBasicSelfDamage);
            if(response.equals(DamageSufferedResponse.hasJustBeenDestroyed)){
                s.destroyShipAndEnhancements();
                g.getLogger().logShipHasDied(s, null);
                //todo vyrobit vrak nebo tak neco
            }
        }
    }

    private void destroyDamagedShips() {
        for (Ship s : killedShips) {
            //dividePlunderedTreasure(s, attackers.get(s)); //currently not supported by the game rules
            s.destroyShipAndEnhancements();
            g.getLogger().logShipHasDied(s, attackers.get(s));
            for (Ship a : attackers.get(s)){
                a.incrementXP();
            }
        }
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
        Ship heaviest = ships.stream().max(Comparator.comparingInt(this::shipWeight)).get();
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

    private int shipWeight(Ship s) {
        return s.getStorage().getMoney();
        //todo this is mock implementation only
    }

    private void performOnAllActions(Consumer<Action> c) {
        for (Ship s : g.getAllShips().values()) {
            for (Action a : s.getPlannedActions()) {
                c.accept(a);
            }
        }
    }
}
