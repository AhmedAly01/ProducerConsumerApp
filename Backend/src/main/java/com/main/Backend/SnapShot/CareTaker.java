package com.main.Backend.SnapShot;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {

    private static ArrayList<Memento> mementos =new ArrayList<Memento>();

    public static void add(Memento state){
        mementos.add(state);
    }
    public static ArrayList<Memento> get(){
        return mementos;
    }


}
