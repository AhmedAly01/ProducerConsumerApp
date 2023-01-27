package com.main.Backend.SnapShot;

import com.main.Backend.model.Product;

public class Memento {
    Product state;
    public Memento(Product state){
        this.state = state;
    }
    public Product getState(){
        return state;
    }
}
