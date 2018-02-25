package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.Plunder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public interface Plunderable {

    default void addResource(ResourceReadOnly amount) {
        getResource().add(amount);
    }

    default void setResource(ResourceReadOnly amount) {
        getResource().setAll(amount);
    }

    /**
     * @return final object representing the resource hold by this board. The returned value is always the same.
     */
    Resource getResource();

    default void getPlunderedBy(Set<Plunder> plunderAttempts){
        List<Plunder> sortedPlunders = new ArrayList<>(plunderAttempts);


        //5 times the same code snippet, differing only in resource selector
        //money:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getMoney()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getMoney();
            int amountGiven = Math.min(amountWanted, getResource().getMoney());
            p.getRelatedShip().getStorage().addMoney(amountGiven);
            getResource().addMoney(-amountGiven);
        }
        //metal:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getMetal()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getMetal();
            int amountGiven = Math.min(amountWanted, getResource().getMetal());
            if (p.getRelatedShip().getStorage().tryAddMetal(amountGiven))
                getResource().addMetal(-amountGiven);
        }
        //wood:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getWood()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getWood();
            int amountGiven = Math.min(amountWanted, getResource().getWood());
            if( p.getRelatedShip().getStorage().tryAddWood(amountGiven))
                getResource().addWood(-amountGiven);
        }
        //cloth:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getCloth()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getCloth();
            int amountGiven = Math.min(amountWanted, getResource().getCloth());
            if( p.getRelatedShip().getStorage().tryAddCloth(amountGiven))
                getResource().addCloth(-amountGiven);
        }
        //rum:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getRum()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getRum();
            int amountGiven = Math.min(amountWanted, getResource().getRum());
            if(p.getRelatedShip().getStorage().tryAddRum(amountGiven))
                getResource().addRum(-amountGiven);
        }
        //tobaco:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getTobacco()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getTobacco();
            int amountGiven = Math.min(amountWanted, getResource().getTobacco());
            if( p.getRelatedShip().getStorage().tryAddTobacco(amountGiven))
                getResource().addTobacco(-amountGiven);
        }
    }
}
