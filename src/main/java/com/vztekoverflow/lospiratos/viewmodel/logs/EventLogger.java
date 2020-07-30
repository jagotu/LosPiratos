package com.vztekoverflow.lospiratos.viewmodel.logs;

import java.util.Map;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.Attack;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventLogger {
    public void logAttack(Ship sender, Attack cause, Ship target, int damageCaused) {
        events.add(new DamageDone(sender, cause, target, damageCaused));
    }

    public void logAttackingEmptyTile(Ship sender, Attack cause, AxialCoordinate targetPosition) {
        events.add(new MessageLoggedEvent(null, LogFormatter.hezkyČesky().format(sender) + "naprázdno útočí akcí " + cause.getČeskéJméno() + " na prázdné políčko " + targetPosition));
    }

    public void logFrontalAssaultOnTargetFacingMe(Ship sender, Ship target){
        events.add(new MessageLoggedEvent(LogFormatter.hezkyČesky().format(sender), " útouk klounem proti soupeři natočenému přídí nezpůsobil žádnou škodu. Nepřítel: " + LogFormatter.hezkyČesky().format(target)));
    }

    public void logResourceGain(Ship subject, ResourceReadOnly amount, Action cause) {
        events.add(new ResourceGained(subject, amount, cause));
    }

    public void logResourceGain(Ship subject, ResourceReadOnly amount) {
        events.add(new ResourceGained(subject, amount, null));
    }

    public void logShipHasDied(Ship deadShip, Iterable<Ship> attackers) {
        events.add(new ShipDied(deadShip, attackers));
    }

    public void logCollisionSolved(AxialCoordinate collisionPosition, Map<Ship, AxialCoordinate> newPositions, int iteration) {
        events.add(new CollisionSolved(collisionPosition, newPositions, iteration));
    }

    public void logRoundHasEnded(int roundNo) {
        events.add(new LoggedEvent() {
            @Override
            public String getTextualDescription(LogFormatter f) {
                return "konec " + roundNo + ". kola";
            }
        });
    }

    public void logActionFailed(Action a, Ship owner, String reason) {
        events.add(new ActionFailed(a, owner, reason));
    }

    public void logMessage(String sender, String message) {
        events.add(new MessageLoggedEvent(sender, message));
    }

    public void logActionNotPerformed(Action sender, String reason) {
        events.add(new MessageLoggedEvent("akce " + sender.getČeskéJméno() + " na lodi " + sender.getRelatedShip().getName() + " se neprovedla", reason));
        //todo udelat systemovejsi reseni, s vlastnim LoggedEvent objektem, formaterem atd
    }

    public void logShipTranslocated(Ship ship, AxialCoordinate target){
        events.add(new MessageLoggedEvent(null,LogFormatter.hezkyČesky().format(ship) + " byla teleportována na " + target));
    }

    public void clearLoggedEvents() {
        events.clear();
    }

    private final ListProperty<LoggedEvent> events = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ReadOnlyListProperty<LoggedEvent> unmodifiableEvents = new SimpleListProperty<>(FXCollections.unmodifiableObservableList(events));

    /**
     * resulting list is unmodifiable
     */
    public ObservableList<LoggedEvent> getLoggedEvents() {
        return unmodifiableEvents.get();
    }

    /**
     * resulting list is unmodifiable
     */
    public ReadOnlyListProperty<LoggedEvent> loggedEventsProperty() {
        return unmodifiableEvents;
    }
}
