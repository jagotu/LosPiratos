package com.vztekoverflow.lospiratos;


import com.vztekoverflow.lospiratos.model.Game;
import com.vztekoverflow.lospiratos.model.GameSerializer;
import com.vztekoverflow.lospiratos.util.AxialDirection;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.CannonUpgrade;

import java.util.ArrayList;
import java.util.List;

public class TondovaTestClass {
    public static void main(String[] args) {

        System.out.println("" + CannonUpgrade.class.toString());
    }

    private static void DeSerializeTest() {
        String outputFilePath = "C:\\Users\\Tonda\\Desktop\\game-out.json";
        String inputFilePath = "C:\\Users\\Tonda\\Desktop\\game-in.json";

        Game g = Game.CreateNewMockGame();
        GameSerializer.SaveGameToFile(outputFilePath, g, false);

        g = GameSerializer.LoadGameFromFile(inputFilePath);

        int a = 0;
    }







    private static void AxialDirectionTest(){

        List<AxialDirection> list = new ArrayList<>();
        list.add(AxialDirection.PointyDownLeft);
        list.add(AxialDirection.PointyDownRight);
        list.add(AxialDirection.PointyRight);
        list.add(AxialDirection.PointyDownRight);
        list.add(AxialDirection.PointyUpLeft);
        list.add(AxialDirection.PointyUpRight);
        list.add(AxialDirection.PointyLeft);

        for(AxialDirection d : list){
            AxialDirection n = d;
            for (int i = 0; i < 27; i++) {
                d = AxialDirection.DirectionFromDegree_Pointy(d.toDegrees());
            }
            if(!d.equals(n)){
                System.out.println("Test failed!");
                System.out.println("d" + d);
                System.out.println("n" + n);
            }
        }
        System.out.println("allright");

    }


}
