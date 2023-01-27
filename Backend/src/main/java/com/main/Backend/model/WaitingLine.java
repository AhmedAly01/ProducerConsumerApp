package com.main.Backend.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

public class WaitingLine extends Observable {
    private String id;
    private Queue<Integer> products = (Queue<Integer>) new LinkedList<Integer>();

    public WaitingLine(String id) {
        this.id = id;
    }


    public boolean isEmpty(){
        synchronized (products) {
            return products.isEmpty();
        }
    }

    public Integer getProduct() throws InterruptedException {
//        System.out.println(Thread.currentThread());
        synchronized (products) {
            if(products.isEmpty())
                return null;
//            System.out.println(Thread.currentThread() + "got product");
            return products.remove();
        }

    }

    public void addProduct(Integer product){
        synchronized (products) {
            this.products.add(product);
            setChanged();
            notifyObservers();
        }
    }


    public String getId(){
        return this.id;
    }
    public Integer getSize(){
        return products.size();
    }


    public void clearData(){
        this.products.clear();
    }
}
