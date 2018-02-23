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
            getResource().addMoney(-amountGiven);
            p.getRelatedShip().getStorage().addMoney(amountGiven);
        }
        //cloth:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getCloth()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getCloth();
            int amountGiven = Math.min(amountWanted, getResource().getCloth());
            getResource().addCloth(-amountGiven);
            p.getRelatedShip().getStorage().addCloth(amountGiven);
        }
        //metal:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getMetal()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getMetal();
            int amountGiven = Math.min(amountWanted, getResource().getMetal());
            getResource().addMetal(-amountGiven);
            p.getRelatedShip().getStorage().addMetal(amountGiven);
        }
        //rum:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getRum()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getRum();
            int amountGiven = Math.min(amountWanted, getResource().getRum());
            getResource().addRum(-amountGiven);
            p.getRelatedShip().getStorage().addRum(amountGiven);
        }
        //tobaco:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getTobacco()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getTobacco();
            int amountGiven = Math.min(amountWanted, getResource().getTobacco());
            getResource().addTobacco(-amountGiven);
            p.getRelatedShip().getStorage().addTobacco(amountGiven);
        }
        //wood:
        sortedPlunders.sort(Comparator.comparingInt(p -> p.getCommodities().getWood()));
        for(Plunder p : sortedPlunders){
            int amountWanted = p.getCommodities().getWood();
            int amountGiven = Math.min(amountWanted, getResource().getWood());
            getResource().addWood(-amountGiven);
            p.getRelatedShip().getStorage().addWood(amountGiven);
        }

    }
}
