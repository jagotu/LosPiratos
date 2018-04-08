package com.vztekoverflow.lospiratos.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AxialDirectionTest {

    @Test
    void directionFromDegreeTest() {
        List<AxialDirection> list = new ArrayList<>();
        list.add(AxialDirection.PointyDownLeft);
        list.add(AxialDirection.PointyDownRight);
        list.add(AxialDirection.PointyRight);
        list.add(AxialDirection.PointyDownRight);
        list.add(AxialDirection.PointyUpLeft);
        list.add(AxialDirection.PointyUpRight);
        list.add(AxialDirection.PointyLeft);

        int someBignumber = 27;
        for (AxialDirection d : list) {
            AxialDirection n = d;
            for (int i = 0; i < someBignumber; i++) {
                d = AxialDirection.directionFromDegree_Pointy(d.toDegrees());
                Assertions.assertEquals(d,n);
            }
        }

    }

}