package com.main.Backend.SnapShot;

import com.main.Backend.model.Product;

public class Originator {
    private Product state;

    public void setState(Product state){
        this.state=state;
    }

    public Product getState(){
        return state;
    }

    public Memento saveStateToMemento(){
        return new Memento(state);
    }

    public void getStateFromMemento(Memento memento){
        state = memento.getState();
    }
}
