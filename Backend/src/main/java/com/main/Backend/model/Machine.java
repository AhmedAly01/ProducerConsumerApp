package com.main.Backend.model;

import com.main.Backend.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Observable;
import java.util.Observer;

public class Machine implements Runnable, Observer {
    private String id;
    private WaitingLine source;
    private WaitingLine target;

    private Integer currProduct = null;
    private Long processingTime;

    private Thread machineThread;

    public Machine(String id, WaitingLine source, WaitingLine target, Long processingTime){
        this.source = source;
        this.target = target;
        this.id = id;
        this.processingTime = processingTime;
    }

    private void processProduct() throws InterruptedException {
        Integer currProd;

        currProd = source.getProduct();

        if(currProd == null){
            machineThread.suspend();
            currProd = source.getProduct();
        }

        WebSocketService.notifyFrontend(this.id + " " + source.getId() + " " +
                source.getSize() + " " + target.getId() + " " + target.getSize() + " " + currProd);

        Thread.sleep(processingTime);

        WebSocketService.notifyFrontend(this.id + " " + source.getId() + " " +
                source.getSize() + " " + target.getId() + " " + target.getSize() + " " + "-1");

        target.addProduct(currProd);
    }

    public void clearMachine(){
        this.currProduct = null;
    }

    public String getId(){
        return id;
    }

    @Override
    public void run(){
        try {
            this.machineThread = Thread.currentThread();
            while (true) {
                processProduct();
            }
        } catch (InterruptedException e) { }
    }

    @Override
    public void update(Observable o, Object arg) {
        machineThread.resume();
    }
}
