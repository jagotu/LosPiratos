package com.vztekoverflow.lospiratos.util;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;

public class SimpleObservable implements Observable {
    @Override
    public void addListener(InvalidationListener listener) {
        list.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        list.remove(listener);
    }

    public void fireListenerChange(){
        for(InvalidationListener l : list){
            l.invalidated(null);
        }
    }

    private ArrayList<InvalidationListener> list = new ArrayList<>();
}
