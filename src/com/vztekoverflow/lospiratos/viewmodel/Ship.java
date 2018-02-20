package com.vztekoverflow.lospiratos.viewmodel;


import com.vztekoverflow.lospiratos.model.ShipEnhancementStatus;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Port;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEntity;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.*;

import java.util.*;


public class Ship implements MovableFigure, DamageableFigure, OnNextRoundStartedListener {

    //region initializers

    /**
     * Sets properties' values to default.
     * Should be called only after the object has been created.
     */
    public void initialize() {
        currentHP.set(getMaxHP());
        this.destroyed.set(false);
    }

    /**
     * Creates a new ship object with values as defined in the @shipModel.
     *
     * @param owner is not bound to any property in model. It must correspond to the ownerTeam as represented in model. If you set a team that is not an ownerTeam according to the model, behaviour is not defined.
     */
    public Ship(Team owner, com.vztekoverflow.lospiratos.model.Ship shipModel) {
        this.ownerTeam = owner;
        this.shipModel = shipModel;
        bindToModel();
        //this is also bindingToModel, but has to be directly in constructor (because storage is marked final)
        storage = new ResourceStorage(
                shipModel.carriesClothUnitsProperty(),
                shipModel.carriesMetalUnitsProperty(),
                shipModel.carriesRumUnitsProperty(),
                shipModel.carriesTobaccoUnitsProperty(),
                shipModel.carriesWoodUnitsProperty(),
                shipModel.carriesMoneyProperty(),
                maxCargo
        );
        storage.addListener(__ -> weight.invalidate());
        position = new Position(shipModel.positionProperty(), shipModel.orientationDegProperty());
    }
    //endregion
    //region model management

    /**
     * should be called only from constructor
     */
    private void bindToModel() {
        name.bindBidirectional(shipModel.nameProperty());
        captainName.bindBidirectional(shipModel.captainProperty());

        shipModel.typeProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) trySettingType(newValue);
        });
        boolean typeSet = trySettingType(shipModel.getType());
        if (!typeSet) {
            Warnings.makeWarning(toString() + ".ctor()", "Fallbacking to Brig.");
            shipType.set(new Brig()); //to make sure that some type is always set
        }


        shipModel.enhancementsProperty().addListener((MapChangeListener<String, ShipEnhancementStatus>) c -> {
            if (c.wasAdded()) {
                tryAddingEnhancement(c.getKey(), c.getValueAdded());
            } else if (c.wasRemoved()) {
                removeEnhancementFromCollection(c.getKey());
            } else {
                Warnings.panic("shipModel.enhancementsProperty listener of " + toString(), "unreachable code?!");
            }
        });
        for (Map.Entry<String, ShipEnhancementStatus> entry : shipModel.enhancementsProperty().get().entrySet()) {
            tryAddingEnhancement(entry.getKey(), entry.getValue());
        }
        destroyed.bindBidirectional(shipModel.destroyedProperty());
        currentHP.bindBidirectional(shipModel.HPProperty());
        XP.bindBidirectional(shipModel.XPProperty());
        for (com.vztekoverflow.lospiratos.model.ShipMechanics m : shipModel.getActiveMechanics()) {
            mechanics.add(ShipMechanics.getInstanceFromDescription(m));
        }
        shipModel.activeMechanicsProperty().addListener((SetChangeListener<com.vztekoverflow.lospiratos.model.ShipMechanics>) c -> {
            if (c.wasAdded()) {
                ShipMechanics m = ShipMechanics.getInstanceFromDescription(c.getElementAdded());
                if (!mechanics.contains(m)) //this check could be omitted because it is inherent set behaviour
                    mechanics.add(m);
            }
            if (c.wasRemoved()) {
                mechanics.removeIf(p -> p.getModelDescription() == c.getElementRemoved());
            }

        });

    }

    private boolean trySettingType(String type) {
        if (type == null || type.isEmpty()) {
            Warnings.makeWarning(toString(), "Invalid ship type description (null or empty).");
            return false;
        }
        ShipType newType = ShipType.createInstanceFromPersistentName(type);
        if (newType == null) return false;
        Ship.this.shipType.set(newType);
        applyToEntities(ShipEntity::onShipTypeJustChanged);
        onEntityInvalidated();
        return true;
    }

    private boolean tryAddingEnhancement(String name, ShipEnhancementStatus status) {
        ShipEnhancement e = EnhancementsCatalog.createInstanceFromPersistentName(name);
        if (e == null) return false;
        if (!e.isAcquirableBy(getShipType())) return false;
        e.onAddedToShip(this);
        if (status == ShipEnhancementStatus.destroyed) {
            e.setDestroyed(true);
        }
        if (status == ShipEnhancementStatus.empty) {
            Warnings.makeDebugWarning(toString(), "Attempt to try empty enhancement: " + name);
            return false;
        }
        e.destroyedProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) updateEnhancement(name, newValue);
        });
        enhancements.put(e.getClass(), e);
        onEntityInvalidated();
        return true;
    }

    private void updateEnhancement(String enhName, boolean isDestroyed) {
        if (shipModel.enhancementsProperty().containsKey(enhName)) {
            shipModel.enhancementsProperty().remove(enhName);
        } else
            Warnings.makeDebugWarning(toString() + ".destroyShipAndEnhancements()", "Enhancement exist as entity but it is not in the model: " + enhName);
        ShipEnhancementStatus status = ShipEnhancementStatus.active;
        if (isDestroyed) status = ShipEnhancementStatus.destroyed;
        shipModel.enhancementsProperty().put(enhName, status);
        onEntityInvalidated();
    }

    private void removeEnhancementFromCollection(String name) {
        ShipEnhancement anotherInstance = EnhancementsCatalog.createInstanceFromPersistentName(name);
        if (anotherInstance == null) return;
        if (enhancements.containsKey(anotherInstance.getClass())) {
            enhancements.remove(anotherInstance.getClass());
        }
        onEntityInvalidated();
    }

    //endregion
    //region general ship properties

    private ObjectProperty<ShipType> shipType = new SimpleObjectProperty<>();

    public ShipType getShipType() {
        return shipType.get();
    }

    public <T extends ShipType> void setShipType(Class<T> shipType) {
        shipModel.typeProperty().set(ShipType.getPersistentName(shipType));
        //callback on shipModel's typeProperty changed will do the job
    }

    public ReadOnlyObjectProperty<ShipType> shipTypeProperty() {
        return shipType;
    }

    private final Team ownerTeam;

    public final Team getTeam() {
        return ownerTeam;
    }

    private com.vztekoverflow.lospiratos.model.Ship shipModel;

    public IntegerProperty currentHPProperty() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP.set(currentHP);
    }

    private IntegerProperty currentHP = new SimpleIntegerProperty();

    public int getCurrentHP() {
        return currentHP.getValue();
    }

    private BooleanProperty destroyed = new SimpleBooleanProperty(true);

    public boolean isDestroyed() {
        return destroyed.getValue();
    }

    public ReadOnlyBooleanProperty destroyedProperty() {
        return destroyed;
    }


    private StringProperty name = new SimpleStringProperty();

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty captainNameProperty() {
        return captainName;
    }

    private StringProperty captainName = new SimpleStringProperty();

    public String getCaptainName() {
        return captainName.getValue();
    }

    public void setCaptainName(String captainName) {
        this.captainName.set(captainName);
    }

    private final ResourceStorage storage;

    public final ResourceStorage getStorage() {
        return storage;
    }

    private final Position position;

    public final Position getPosition() {
        return position;
    }

    private IntegerProperty XP = new SimpleIntegerProperty();

    public int getXP() {
        return XP.get();
    }

    public IntegerProperty XPProperty() {
        return XP;
    }

    public void incrementXP() {
        this.XP.set(this.XP.get() + 1);
    }

    //endregion
    //region stats

    private final List<Binding> entityInvalidatedListeners = new ArrayList<>();

    private void onEntityInvalidated() {
        for (Binding binding : entityInvalidatedListeners) {
            binding.invalidate();
        }
        maxHP.invalidate();
        cannonsCount.invalidate();
        speed.invalidate();
        maxCargo.invalidate();
        garrisonSize.invalidate();
        weight.invalidate();
    }

    private IntegerBinding maxHP = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int val = 0;
            for (ShipEntity e : getAllEntities()) {
                val += e.getBonusMaxHP();
            }
            return val;
        }
    };
    private IntegerBinding cannonsCount = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int val = 0;
            for (ShipEntity e : getAllEntities()) {
                val += e.getBonusCannonsCount();
            }
            return val;
        }
    };
    private IntegerBinding speed = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int val = 0;
            for (ShipEntity e : getAllEntities()) {
                val += e.getBonusSpeed();
            }
            return val;
        }
    };
    private IntegerBinding maxCargo = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int val = 0;
            for (ShipEntity e : getAllEntities()) {
                val += e.getBonusCargoSpace();
            }
            return val;
        }
    };
    private IntegerBinding garrisonSize = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int val = 0;
            for (ShipEntity e : getAllEntities()) {
                val += e.getBonusGarrison();
            }
            return val;
        }
    };

    private IntegerBinding weight = Bindings.createIntegerBinding(() ->
                getShipType().getWeight() + getStorage().scalarProduct(ResourceReadOnly.ONE)
            , shipType //also depends on storage, see constructor
    );

    public int getWeight() {
        return weight.get();
    }

    public IntegerBinding weightProperty() {
        return weight;
    }

    public int getMaxHP() {
        return maxHP.get();
    }

    public IntegerBinding maxHPProperty() {
        return maxHP;
    }

    public int getCannonsCount() {
        return cannonsCount.get();
    }

    public IntegerBinding cannonsCountProperty() {
        return cannonsCount;
    }

    public int getSpeed() {
        return speed.get();
    }

    public IntegerBinding speedProperty() {
        return speed;
    }

    public int getGarrisonSize() {
        return garrisonSize.get();
    }

    public IntegerBinding garrisonSizeProperty() {
        return garrisonSize;
    }

    /**
     * The ships takes damage, reducing its HP by {@code value}.
     * If HP goes below 0, the ship will be destroyed.
     *
     * @returns value indicating whether the ship has been destroyed.
     */
    @Override
    public DamageSufferedResponse takeDamage(int value) {
        if (isDestroyed()) return DamageSufferedResponse.alreadyDestroyed;
        currentHP.set(currentHP.get() - value);
        if (currentHP.get() <= 0) {
            return DamageSufferedResponse.hasJustBeenDestroyed;
        } else
            return DamageSufferedResponse.stillAlive;
    }

    //endregion
    //region entities
    //region enhancements

    private MapProperty<Class<? extends ShipEnhancement>, ShipEnhancement> enhancements = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public ObservableMap<Class<? extends ShipEnhancement>, ShipEnhancement> getEnhancements() {
        return enhancements;
    }


    public <Enhancement extends ShipEnhancement> void addNewEnhancement(Class<Enhancement> enhancement) {
        shipModel.enhancementsProperty().put(EnhancementsCatalog.getPersistentName(enhancement), ShipEnhancementStatus.active);
    }

    /**
     * @return null if Ship does not contain any enhancement of given type
     */
    public <SpecificEnh extends ShipEnhancement> SpecificEnh getEnhancement(Class<SpecificEnh> enhancement) {
        if (!enhancements.containsKey(enhancement)) return null;
        return (SpecificEnh) enhancements.get(enhancement);
    }

    public <Enhancement extends ShipEnhancement> ShipEnhancementStatus getEnhancementStatus(Class<Enhancement> enhancement) {
        if (enhancements.containsKey(enhancement)) {
            if ((enhancements.get(enhancement).isDestroyed())) {
                return ShipEnhancementStatus.destroyed;
            } else
                return ShipEnhancementStatus.active;
        } else
            return ShipEnhancementStatus.empty;
    }

    public <Enhancement extends ShipEnhancement> void removeEnhancement(Class<Enhancement> enhancement) {
        if (enhancements.containsKey(enhancement)) {
            enhancements.remove(enhancement);
            onEntityInvalidated();
        }
    }

    public <Enhancement extends ShipEnhancement> boolean hasActiveEnhancement(Class<Enhancement> enhancement) {
        return enhancements.containsKey(enhancement) && (!enhancements.get(enhancement).isDestroyed());
    }

    public <Enhancement extends ShipEnhancement> boolean hasEnhancement(Class<Enhancement> enhancement) {
        return enhancements.containsKey(enhancement);
    }

    public <Enhancement extends ShipEnhancement> ObjectBinding<ShipEnhancementStatus> enhancementStatusProperty(Class<Enhancement> enhancement) {
        ObjectBinding<ShipEnhancementStatus> b = new ObjectBinding<ShipEnhancementStatus>() {
            @Override
            protected ShipEnhancementStatus computeValue() {
                return getEnhancementStatus(enhancement);
            }
        };
        entityInvalidatedListeners.add(b);
        return b;
    }

    //endregion
    //region mechanics:

    private SetProperty<ShipMechanics> mechanics = new SimpleSetProperty<ShipMechanics>(FXCollections.observableSet()) {
        @Override
        public boolean add(ShipMechanics element) {
            shipModel.activeMechanicsProperty().add(element.getModelDescription());
            boolean result = super.add(element);
            element.onAddedToShip(Ship.this);
            return result;
        }

        @Override
        public boolean addAll(Collection<? extends ShipMechanics> elements) {
            boolean result = false;
            for (ShipMechanics m : elements)
                result = result || add(m);
            return result;
        }
    };
    private ReadOnlySetWrapper<ShipMechanics> mechanicsReadOnly = new ReadOnlySetWrapper<>(FXCollections.unmodifiableObservableSet(mechanics.get()));

    /**
     * returns mechanics as read only set (elements cannot be added to it)
     */
    public ReadOnlySetWrapper<ShipMechanics> getMechanics() {
        return mechanicsReadOnly;
    }

    /**
     * To add new mechanics, add them to this property.
     */
    public ReadOnlySetProperty<ShipMechanics> mechanicsProperty() {
        return mechanics;
    }

    //endregion
    private void applyToEntities(java.util.function.Consumer<ShipEntity> action) {
        for (ShipEntity e : getAllEntities()) {
            action.accept(e);
        }
    }

    private Iterable<ShipEntity> getAllEntities() {
        return () -> new Iterator<ShipEntity>() {
            Iterator<ShipEnhancement> enh = enhancements.values().iterator();
            Iterator<ShipMechanics> mech = mechanics.iterator();
            int state = 0;
            boolean hasNext = true;
            //states:
            //  0 .. next() not called yet, ie shipType to be returned
            //  1 .. iterating over enhancements
            //  2 .. iterating over mechanics
            //  3 .. iteration has ended, no more elements available

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public ShipEntity next() {
                ShipEntity result;
                switch (state) {
                    case 0: {
                        stateAdvance();
                        return shipType.get();
                    }
                    case 1: {
                        result = enh.next();
                        stateAdvance();
                        return result;
                    }
                    case 2: {
                        result = mech.next();
                        stateAdvance();
                        return result;
                    }
                    case 3:
                        throw new NoSuchElementException();
                    default:
                        throw new NoSuchElementException();
                }
            }

            private void stateAdvance() {
                switch (state) {
                    case 0:
                        state = 1;
                        //yes, fall through to case 1:
                    case 1:
                        if (enh.hasNext()) break;
                        state = 2;
                        //yes, fall through to case 2:
                    case 2:
                        if (mech.hasNext()) break;
                        state = 3;
                        //yes, fall through to case 3:
                    case 3:
                    default:
                        hasNext = false;
                }
            }
        };
    }

    //endregion
    //region  actions

    private final ListProperty<Action> plannedActions = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ObservableList<Action> getPlannedActions() {
        return plannedActions.get();
    }

    public ListProperty<? extends PlannableAction> plannedActionsProperty() {
        return plannedActions;
    }

    public void unplanActions(int howManyToLeave) {
        plannedActions.remove(howManyToLeave, plannedActions.size());
    }

    public void planAction(PlannableAction action) {
        Action a;
        try {
            a = (Action) action.asPerformableAction();
        } catch (ClassCastException e) {
            //this should never happen in our game, as Action is the only class that implements PerformableAction
            throw new UnsupportedOperationException();
        }
        a.setRelatedShip(this);
        if (!a.getPlannable()) {
            Warnings.makeWarning(toString() + ".planAction()", "attempt to plan action that is not plannable: " + a.getClass().getSimpleName());
            return;
        }
        plannedActions.add(a);
    }
    //endregion
    //region public functions


    @Override
    public String toString() {
        String name = this.name.get();
        if (name == null || name.equals("")) name = "<empty>";
        return "Ship \"" + name + "\"";
    }

    /**
     * marks the ship and all its enhancements (e.g. upgrades) as destroyed
     */
    public void destroyShipAndEnhancements() {
        destroyed.setValue(true);
        currentHP.set(0);
        getStorage().setAll(ResourceReadOnly.ZERO);
        translocateToNearestPort();

        //destroy enhancements:
        //do this in 2 steps, because enhancements collection will be changed, thus invalidated, thus couldnt be iterated through
        List<ShipEnhancement> enhCopy = new ArrayList<>(enhancements.values());
        for (ShipEnhancement e : enhCopy) {
            e.setDestroyed(true);
            //  how this works:
            //  1) set enh as destroyed
            //     > property changed > callback to ship's "updateEnh"
            //       > remove old value from ship model
            //         > map changed > callback to ship's "removeEnh"
            //       > add new value to ship model
            //         > map changed > callback to ship's "addEnh"
            //  2) this Enhancement instance disappears, the collection contains a new one
        }
    }

    private void translocateToNearestPort() {
        getPosition().setCoordinate(getTeam().getGame().getBoard().getNearestTile(getCoordinate(), Port.class).getLocation());
    }

    /**
     * marks the ship (BUT NOT its enhancements) as not destroyed
     */
    public void repairShip() {
        destroyed.set(false);
        currentHP.set(getMaxHP());
    }

    /**
     * @return Resource that corresponds to how many Resource had to be paid for obtaining the ship and all its enhancements
     */
    public ResourceReadOnly computeInitialCost(boolean includeDamagedEnhancements) {
        Resource result = new Resource();
        for (ShipEnhancement e : enhancements.values()) {
            if (e.isDestroyed() && !includeDamagedEnhancements)
                continue;
            result.add(e.getCostUniversal());
        }
        result.add(getShipType().getBuyingCost());
        return result;
    }

    /**
     * is called by Game whenever the game proceeds to a next round
     */
    @Override
    public void onNextRoundStarted(int roundNo) {
        plannedActions.clear();
        for (ShipEntity e : getAllEntities()) {
            e.onNextRoundStarted(roundNo);
        }
    }
    //endregion

}