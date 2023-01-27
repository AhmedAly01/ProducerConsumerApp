package com.main.Backend.model;

import com.main.Backend.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;

public class Machine implements Runnable{
    private String id;
    private WaitingLine source;
    private WaitingLine target;

    private Integer currProduct = null;
    private Long processingTime;

    public Machine(String id, WaitingLine source, WaitingLine target, Long processingTime){
        this.source = source;
        this.target = target;
        this.id = id;
        this.processingTime = processingTime;
    }

    private void processProduct() throws InterruptedException {
        Integer currProd;
        currProd = source.getProduct();

        System.out.println(this.id + " " + source.getId() + " " +
                source.getSize() + " " + target.getId() + " " + target.getSize() + " " + currProd);
        WebSocketService.notifyFrontend(this.id + " " + source.getId() + " " +
                source.getSize() + " " + target.getId() + " " + target.getSize() + " " + currProd);

        Thread.sleep(processingTime);
        System.out.println(id + " processed " + currProd);

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
            while (true) {
                processProduct();
            }
        } catch (InterruptedException e) { }
    }
}
