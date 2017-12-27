package com.vztekoverflow.lospiratos;

import com.vztekoverflow.lospiratos.model.GameSerializer;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.CannonUpgrade;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.HullUpgrade;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import javafx.scene.paint.Color;

public class GameManagementExample {
    public static void main(String[] args) {

        // Try first running the program with Store, then with Load
        // You can even manually change the JSON file before loading it.

        //The first function will produce some game warnings.
        //    For load-store testing, those are not relevant.

        ViewModelStoreTest();
        //ViewModelLoadTest();
    }
    private static void ViewModelLoadTest(){
        Game g = Game.LoadFromModel(GameSerializer.LoadGameFromFile("game.json"));

        System.out.println("g.getTeams().size() = " + g.getTeams().size());

        Team pirraten = g.findTeamByName("Pirrrrráten mit nem Schwert");
        System.out.println("pirraten.getColor() = " + pirraten.getColor());
        System.out.println("pirraten.getMoney() = " + pirraten.getMoney());
        System.out.println("pirraten.getOwnedCloth() = " + pirraten.getOwnedCloth());

        Ship perla = pirraten.getShips().get(0);

        System.out.println("perla.getCurrentHP() = " + perla.getCurrentHP());
        System.out.println("perla.getMaxHP() = " + perla.getMaxHP());
        System.out.println("perla.hasActiveEnhancement(CannonUpgrade.class) = " + perla.hasActiveEnhancement(CannonUpgrade.class));
        System.out.println("perla.hasActiveEnhancement(HullUpgrade.class) = " + perla.hasActiveEnhancement(HullUpgrade.class));

        Team losBratros = g.findTeamByName("Los Bratros în trikos");

        System.out.println("losBratros.getOwnedRum() = " + losBratros.getOwnedRum());

    }

    private static void ViewModelStoreTest(){
        Game g = new Game();

        Team losBratros = g.createAndAddNewTeam("Los Bratros în trikos", Color.BLANCHEDALMOND);
        Team pirraten = g.createAndAddNewTeam("Pirrrrráten mit nem Schwert", Color.ANTIQUEWHITE);

        pirraten.addMoney(100);
        pirraten.addOwnedCloth(5);
        Ship perla = pirraten.createAndAddNewShip(Frigate.class, "Růžová perla", "Jack Daniels");
        perla.addNewEnhancement(CannonUpgrade.class);
        perla.addToCurrentHP(-10);
        perla.getMaxHP();

        losBratros.addOwnedRum(255);

        GameSerializer.SaveGameToFile("game.json", g.getGameModel(), false);

    }
}
