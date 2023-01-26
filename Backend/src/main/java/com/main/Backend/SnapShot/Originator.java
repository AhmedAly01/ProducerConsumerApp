package com.main.Backend.SnapShot;

public class Originator {
    private Object state;

    public void setState(Object state){
        this.state=state;
    }

    public Object getState(){
        return state;
    }

    public Memento saveStateToMemento(){
        return new Memento(state);
    }

    public void getStateFromMemento(Memento memento){
        state = memento.getState();
    }
}
