package com.main.Backend.SnapShot;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {

    private List<Memento> mementos =new ArrayList<Memento>();

    public void add(Memento state){
        mementos.add(state);
    }
    public Memento get(int index){
        return mementos.get(index);
    }


}
