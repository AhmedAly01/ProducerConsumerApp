package com.main.Backend.SnapShot;

public class Memento {
    Object state;
    public Memento(Object state){
        this.state = state;
    }
    public Object getState(){
        return state;
    }
}
