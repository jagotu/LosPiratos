package com.vztekoverflow.lospiratos.viewmodel;


import com.vztekoverflow.lospiratos.model.ShipEnhancementStatus;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEntity;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;


import java.util.*;


public class Ship implements MovableFigure {

    //initializers:

    /*
     * Sets properties' values to default.
     * Should be called only after the object has been created.
     */
    public void initialize(){
        currentHP.set(getMaxHP());
        this.destroyed.set(false);
    }

    /*
     * Creates a new ship object with values as defined in the @shipModel.
     * @param ownerTeam is not bound to any property in model. It must correspond to the ownerTeam as represented in model. If you set a team that is not an ownerTeam according to the model, behaviour is not defined.
     */
    public Ship(Team owner, com.vztekoverflow.lospiratos.model.Ship shipModel) {
        this.ownerTeam = owner;
        this.shipModel = shipModel;
        bindToModel();
    }

    private void bindToModel() {
        name.bindBidirectional(shipModel.nameProperty());
        captainName.bindBidirectional(shipModel.captainProperty());

        shipModel.typeProperty().addListener((observable, oldValue, newValue) -> trySettingType(newValue));
        boolean typeSet = trySettingType(shipModel.getType());
        if (!typeSet) {
            Warnings.makeWarning(toString() + ".ctor()", "Fallbacking to Brig.");
            shipType = new Brig(); //to make sure that some type is always set
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
        storage = new ResourceStorage(
                shipModel.carriesClothUnitsProperty(),
                shipModel.carriesMetalUnitsProperty(),
                shipModel.carriesRumUnitsProperty(),
                shipModel.carriesTobaccoUnitsProperty(),
                shipModel.carriesWoodUnitsProperty(),
                shipModel.carriesMoneyProperty(),
                maxCargo
        );
        positionProperty().bindBidirectional(shipModel.positionProperty());
    }

    private boolean trySettingType(String type) {
        if (type == null || type.isEmpty()) {
            Warnings.makeWarning(toString(), "Invalid ship type description (null or empty).");
            return false;
        }
        ShipType newType = ShipType.createInstanceFromPersistentName(type);
        if (newType == null) return false;
        Ship.this.shipType = newType;
        applyToEntities(ShipEntity::onShipTypeJustChanged);
        onEntityInvalidated();
        return true;
    }

    private boolean tryAddingEnhancement(String name, ShipEnhancementStatus status) {
        ShipEnhancement e = ShipEnhancement.createInstanceFromPersistentName(name);
        if (e == null) return false;
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
        ShipEnhancement anotherInstance = ShipEnhancement.createInstanceFromPersistentName(name);
        if (anotherInstance == null) return;
        if (enhancements.containsKey(anotherInstance.getClass())) {
            enhancements.remove(anotherInstance.getClass());
        }
        onEntityInvalidated();
    }

    //general ship properties:

    private ShipType shipType;

    public ShipType getShipType() {
        return shipType;
    }

    public <T extends ShipType> void setShipType(Class<T> shipType) {
        shipModel.typeProperty().set(ShipType.getPersistentName(shipType));
        //callback on shipModel's typeProperty changed will do the job
    }

    private Team ownerTeam;

    public Team getTeam() {
        return ownerTeam;
    }

    private com.vztekoverflow.lospiratos.model.Ship shipModel;

    private IntegerProperty currentHP = new SimpleIntegerProperty();

    public void addToCurrentHP(int value) {
        currentHP.set(currentHP.get() + value);
    }

    public int getCurrentHP() {
        return currentHP.getValue();
    }

    private BooleanProperty destroyed = new SimpleBooleanProperty(true);

    public boolean isDestroyed() {
        return destroyed.getValue();
    }


    private StringProperty name = new SimpleStringProperty();

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    private StringProperty captainName = new SimpleStringProperty();
    public String getCaptainName() {
        return captainName.getValue();
    }
    public void setCaptainName(String captainName) {
        this.captainName.set(captainName);
    }

    private ResourceStorage storage;

    public ResourceStorage getStorage() {
        return storage;
    }

    private ObjectProperty<AxialCoordinate> position = new SimpleObjectProperty<>();

    @Override
    public AxialCoordinate getPosition() {
        return position.get();
    }

    public ObjectProperty<AxialCoordinate> positionProperty() {
        return position;
    }

    public void setPosition(AxialCoordinate position) {
        this.position.set(position);
    }
    public void setPosition(int Q, int R) {
        setPosition(new AxialCoordinate(Q, R));
    }
    //stats:

    private void onEntityInvalidated() {
        maxHP.invalidate();
        cannonsNr.invalidate();
        speed.invalidate();
        maxCargo.invalidate();
        garrisonNr.invalidate();
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
    private IntegerBinding cannonsNr = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int val = 0;
            for (ShipEntity e : getAllEntities()) {
                val += e.getBonusCannonsNr();
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
    private IntegerBinding garrisonNr = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int val = 0;
            for (ShipEntity e : getAllEntities()) {
                val += e.getBonusGarrison();
            }
            return val;
        }
    };

    public int getMaxHP() {
        return maxHP.get();
    }

    public IntegerBinding maxHPProperty() {
        return maxHP;
    }

    public int getCannonsNr() {
        return cannonsNr.get();
    }

    public IntegerBinding cannonsNrProperty() {
        return cannonsNr;
    }

    public int getSpeed() {
        return speed.get();
    }

    public IntegerBinding speedProperty() {
        return speed;
    }

    public int getMaxCargo() {
        return maxCargo.get();
    }

    public IntegerBinding maxCargoProperty() {
        return maxCargo;
    }

    public int getGarrisonNr() {
        return garrisonNr.get();
    }

    public IntegerBinding garrisonNrProperty() {
        return garrisonNr;
    }



    // enhancements:

    private MapProperty<Class<? extends ShipEnhancement>, ShipEnhancement> enhancements = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public ObservableMap<Class<? extends ShipEnhancement>, ShipEnhancement> getEnhancements() {
        return enhancements;
    }


    public <Enhancement extends ShipEnhancement> void addNewEnhancement(Class<Enhancement> enhancement) {
        shipModel.enhancementsProperty().put(ShipEnhancement.getPersistentName(enhancement), ShipEnhancementStatus.active);

    }

    /*
     * @returns null if Ship does not contain any enhancement of given type
     */
    public <SpecificEnh extends ShipEnhancement> SpecificEnh getEnhancement(Class<SpecificEnh> enhancement) {
        if (!enhancements.containsKey(enhancement)) return null;
        return (SpecificEnh) enhancements.get(enhancement);
    }

    public <Enhancement extends ShipEnhancement> boolean hasActiveEnhancement(Class<Enhancement> enhancement) {
        return enhancements.containsKey(enhancement) && (!enhancements.get(enhancement).isDestroyed());
    }

    //mechanics:

    //todo mechanics are to be implemented:
    private ListProperty<ShipMechanics> mechanics = new SimpleListProperty<>(FXCollections.observableArrayList());

    private void applyToEntities(java.util.function.Consumer<ShipEntity> action){
        for (ShipEntity e : getAllEntities()){
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
                    case 0:{
                        stateAdvance();
                        return shipType;
                    }
                    case 1:{
                        result = enh.next();
                        stateAdvance();
                        return result;
                    }
                    case 2:{
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
            private void stateAdvance(){
                switch (state) {
                    case 0:
                        state = 1;
                        //yes, fall through to case 1:
                    case 1:
                        if(enh.hasNext()) break;
                        state = 2;
                        //yes, fall through to case 2:
                    case 2:
                        if(mech.hasNext()) break;
                        state = 3;
                        //yes, fall through to case 3:
                    case 3:
                    default:
                        hasNext = false;
                }
            }
        };
    }

    //public functions:


    @Override
    public String toString() {
        String name = this.name.get();
        if (name == null || name.equals("")) name = "<empty>";
        return "Ship \"" + name + "\"";
    }

    /*
     * marks the ship and all its enhancements (e.g. upgrades) as destroyed
     */
    public void destroyShipAndEnhancements() {
        destroyed.setValue(true);

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

    /*
     * marks the ship (BUT NOT its enhancements) as not destroyed
     */
    public void repairShip() {
        destroyed.set(false);
    }

    /*
     * @returns Resource that corresponds to how many Resource had to be paid for obtaining the ship and all its enhancements
     */
    public ResourceImmutable computeInitialCost(boolean includeDamagedEnhancements){
        Resource result = new Resource();
        for(ShipEnhancement e: enhancements.values()){
            if(e.isDestroyed() && !includeDamagedEnhancements) continue;
            try{
                result.add((Resource) (e.getClass().getMethod("getCost").invoke(null)));
            }catch (Exception ex){
                Warnings.makeWarning(toString()+".computeInitialCost()", ex.getMessage());
            }
        }
        try{
            result.add((Resource) (shipType.getClass().getMethod("getCost").invoke(null)));
        }catch (Exception ex){
            Warnings.makeWarning(toString()+".computeInitialCost()", ex.getMessage());
        }
        return result;
    }

}