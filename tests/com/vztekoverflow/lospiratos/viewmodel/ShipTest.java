package com.vztekoverflow.lospiratos.viewmodel;


import com.vztekoverflow.lospiratos.model.ShipEnhancementStatus;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.CannonUpgrade;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.ChainShot;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.HullUpgrade;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics.Chained;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics.Rooted;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShipTest {

    @Test
    void mechanicsTest() {
        com.vztekoverflow.lospiratos.model.Ship model = new com.vztekoverflow.lospiratos.model.Ship();
        model.getActiveMechanics().add(com.vztekoverflow.lospiratos.model.ShipMechanics.chained);

        //disable warnings when creating ship wil null owner
        boolean status = Warnings.isEnabled();
        Warnings.setEnabled(false);
        Ship s = new Ship(null, model);
        Warnings.setEnabled(status);

        Assertions.assertEquals(1, s.getMechanics().size());
        Assertions.assertEquals(1, model.getActiveMechanics().size());
        Assertions.assertTrue(s.getMechanics().contains(new Chained()));

        model.getActiveMechanics().remove(com.vztekoverflow.lospiratos.model.ShipMechanics.chained);
        Assertions.assertEquals(0, s.getMechanics().size());
        Assertions.assertEquals(0, model.getActiveMechanics().size());

        ShipMechanics r = new Rooted();
        s.mechanicsProperty().add(r);
        Assertions.assertTrue(s.getMechanics().contains(r));
        Assertions.assertTrue(model.getActiveMechanics().contains(com.vztekoverflow.lospiratos.model.ShipMechanics.rooted));

        model.getActiveMechanics().add(com.vztekoverflow.lospiratos.model.ShipMechanics.chained);
        Assertions.assertTrue(s.getMechanics().contains(new Chained()));
        Assertions.assertTrue(model.getActiveMechanics().contains(com.vztekoverflow.lospiratos.model.ShipMechanics.chained));

        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                s.getMechanics().add(new Rooted())
        );

    }

    @Test
    void positionTest() {
        //boolean testPassed = true;
        Game g = new Game();
        Team t = g.createAndAddNewTeam("team", Color.AZURE);
        AxialCoordinate pos = new AxialCoordinate(-10, -10);
        Ship ship = t.createAndAddNewShip(Schooner.class, "s1", "captain", pos);
        Assertions.assertTrue(ship.getPosition().getCoordinate().equals(new AxialCoordinate(-10, -10)));

        ship.getPosition().setCoordinate(4, 5);
        Assertions.assertTrue(ship.getPosition().getCoordinate().equals(new AxialCoordinate(4, 5)));

        Assertions.assertFalse(ship.getPosition().getCoordinate().equals(new AxialCoordinate(5, 4)));
        Assertions.assertFalse(ship.getPosition().getCoordinate().equals(new AxialCoordinate(0, 0)));
        Assertions.assertFalse(ship.getPosition().getCoordinate().equals(new AxialCoordinate(10, 10)));

    }

    @Test
    void storageTest() {
        Game g = new Game();
        Team t = g.createAndAddNewTeam("team", Color.AZURE);
        AxialCoordinate pos = new AxialCoordinate(0, 0);
        Ship ship = t.createAndAddNewShip(Schooner.class, "s1", "captain", pos);

        int storageCapacity = 250; //for schooner

        ResourceStorage storage = ship.getStorage();
        Assertions.assertEquals(storageCapacity, storage.getCapacityMaximum());

        Assertions.assertEquals(0, storage.getCloth());
        Assertions.assertEquals(0, storage.getMetal());
        Assertions.assertEquals(0, storage.getMoney()   );
        Assertions.assertEquals(0, storage.getRum());
        Assertions.assertEquals(0, storage.getWood());
        Assertions.assertEquals(0, storage.getTobacco());

        storage.setRum(150);
        Assertions.assertEquals(storageCapacity - 150, storage.getCapacityLeft());

        //upgrade the ship:
        ship.setShipType(Frigate.class);
        storageCapacity = 800;

        Assertions.assertEquals(storageCapacity, storage.getCapacityMaximum());

        Assertions.assertEquals(0, storage.getCloth());
        Assertions.assertEquals(0, storage.getMetal());
        Assertions.assertEquals(0, storage.getMoney()   );
        Assertions.assertEquals(150, storage.getRum());
        Assertions.assertEquals(0, storage.getWood());
        Assertions.assertEquals(0, storage.getTobacco());

        storage.tryAddRum(150);
        Assertions.assertEquals(storageCapacity - 150*2, storage.getCapacityLeft());
        Assertions.assertEquals(150*2, storage.getRum());



        ship = t.createAndAddNewShip(Schooner.class, "s2", "captain", pos);
        storage = ship.getStorage();
        storageCapacity = 250;//for schooner
        boolean addedSucesfully = storage.tryAddWood(storageCapacity - 1);
        Assertions.assertTrue(addedSucesfully);

        Assertions.assertTrue(storage.canStoreMoreCloth(1));
        Assertions.assertTrue(storage.canStoreMoreWood(1));
        Assertions.assertTrue(storage.canStoreMoreMetal(1));
        Assertions.assertTrue(storage.canStoreMoreRum(1));
        Assertions.assertTrue(storage.canStoreMoreTobacco(1));

        addedSucesfully = storage.tryAddWood(1);
        Assertions.assertTrue(addedSucesfully);

        Assertions.assertFalse(storage.canStoreMoreCloth(1));
        Assertions.assertFalse(storage.canStoreMoreWood(1));
        Assertions.assertFalse(storage.canStoreMoreMetal(1));
        Assertions.assertFalse(storage.canStoreMoreRum(1));
        Assertions.assertFalse(storage.canStoreMoreTobacco(1));

    }

    @Test
    void namesTest() {
        boolean status = Warnings.isEnabled();
        boolean testPassed = true;

        Warnings.disable();

        Game g = new Game();
        AxialCoordinate pos = new AxialCoordinate(0, 0);
        Team t1 = g.createAndAddNewTeam("team", Color.AZURE);
        Ship s1 = t1.createAndAddNewShip(Galleon.class, "s1", "captain", pos);
        Assertions.assertNotEquals(null, s1);

        Ship s1_2 = t1.createAndAddNewShip(Schooner.class, "s1", "captain2", pos);
        Assertions.assertEquals(null, s1_2);

        Team t2 = g.createAndAddNewTeam("team2", Color.AZURE);
        Ship s1_3 = t2.createAndAddNewShip(Frigate.class, "s1", "whatever", pos);
        Assertions.assertEquals(null, s1_3);

        Ship s2 = t2.createAndAddNewShip(Frigate.class, "s2", "whatever", pos);
        Ship s3 = t2.createAndAddNewShip(Frigate.class, "s3", "whatever", pos);
        Ship s4 = t2.createAndAddNewShip(Frigate.class, "s4", "whatever", pos);
        Assertions.assertNotEquals(null, s2);
        Assertions.assertNotEquals(null, s3);
        Assertions.assertNotEquals(null, s4);

        Ship s5 = t1.createAndAddNewShip(Frigate.class, "s5", "whatever", pos);
        Ship s6 = t1.createAndAddNewShip(Frigate.class, "s6", "whatever", pos);
        Ship s7 = t1.createAndAddNewShip(Frigate.class, "s7", "whatever", pos);
        Assertions.assertNotEquals(null, s5);
        Assertions.assertNotEquals(null, s6);
        Assertions.assertNotEquals(null, s7);


        boolean debugStatus = Warnings.isDebug();
        Warnings.setDebug(false);

        Ship empty = t1.createAndAddNewShip(Frigate.class, "", "whatever", pos);
        Ship shipnull = t1.createAndAddNewShip(Frigate.class, null, "whatever", pos);
        Assertions.assertEquals(null, empty);
        Assertions.assertEquals(null, shipnull);

        Warnings.setDebug(debugStatus);
        Warnings.setEnabled(status);

    }

    @Test
    void ShipEnhancementTest() {

        AxialCoordinate pos = new AxialCoordinate(0, 0);
        com.vztekoverflow.lospiratos.model.Ship modelShip = new com.vztekoverflow.lospiratos.model.Ship();
        modelShip.typeProperty().set(ShipType.getPersistentName(Galleon.class));

        //disable warnings when creating ship wil null owner
        boolean status = Warnings.isEnabled();
        Warnings.setEnabled(false);
        Ship s = new Ship(null, modelShip);
        Warnings.setEnabled(status);

        //basic addition:
        s.addNewEnhancement(CannonUpgrade.class);
        Assertions.assertTrue(s.hasActiveEnhancement(CannonUpgrade.class));
        Assertions.assertFalse(s.hasActiveEnhancement(HullUpgrade.class));
        Assertions.assertTrue(modelShip.getEnhancements().containsKey(ShipEnhancement.getPersistentName(CannonUpgrade.class)));

        //destroy the ship:
        s.destroyShipAndEnhancements();
        Assertions.assertTrue(s.isDestroyed());
        Assertions.assertTrue(s.getEnhancement(CannonUpgrade.class).isDestroyed());
        Assertions.assertEquals(ShipEnhancementStatus.destroyed,modelShip.getEnhancements().get(ShipEnhancement.getPersistentName(CannonUpgrade.class)));
        Assertions.assertFalse(s.hasActiveEnhancement(CannonUpgrade.class));

        //repair the ship:
        s.repairShip();
        Assertions.assertFalse(s.isDestroyed());
        Assertions.assertTrue(s.getEnhancement(CannonUpgrade.class).isDestroyed());

        //repair cannon upgrade several times:
        s.getEnhancement(CannonUpgrade.class).setDestroyed(false);
        Assertions.assertTrue(s.hasActiveEnhancement(CannonUpgrade.class));
        Assertions.assertEquals(ShipEnhancementStatus.active,modelShip.getEnhancements().get(ShipEnhancement.getPersistentName(CannonUpgrade.class)));
        for (int i = 0; i < 999; i++) {
            s.getEnhancement(CannonUpgrade.class).setDestroyed(true);
            s.getEnhancement(CannonUpgrade.class).setDestroyed(false);
        }
        Assertions.assertTrue(s.hasActiveEnhancement(CannonUpgrade.class));
        Assertions.assertEquals(ShipEnhancementStatus.active,modelShip.getEnhancements().get(ShipEnhancement.getPersistentName(CannonUpgrade.class)));
        Assertions.assertFalse(modelShip.getEnhancements().size() > 2);
        Assertions.assertFalse(s.getEnhancements().values().size() > 2);

        // check cannon upgrade functionality:
        Game g = new Game();
        Team t = g.createAndAddNewTeam("team", Color.AZURE);
        Ship weakShip = t.createAndAddNewShip(Schooner.class, "weak ship", "c1", pos);
        Ship strongShip = t.createAndAddNewShip(Schooner.class, "strong ship", "c1", pos);
        strongShip.addNewEnhancement(CannonUpgrade.class);
        Assertions.assertEquals(8,weakShip.getCannonsCount());
        Assertions.assertEquals(10,strongShip.getCannonsCount());
        weakShip.addNewEnhancement(HullUpgrade.class);
        weakShip.addNewEnhancement(ChainShot.class);
        strongShip.addNewEnhancement(HullUpgrade.class);
        strongShip.addNewEnhancement(ChainShot.class);
        //Hull upgrade and chain shot should not change cannon upgrade result:
        Assertions.assertEquals(8,weakShip.getCannonsCount());
        Assertions.assertEquals(10,strongShip.getCannonsCount());

        // change shipType:
        weakShip.setShipType(Galleon.class);
        strongShip.setShipType(Galleon.class);
        Assertions.assertEquals(30,weakShip.getCannonsCount());
        Assertions.assertEquals(37,strongShip.getCannonsCount());

    }

}