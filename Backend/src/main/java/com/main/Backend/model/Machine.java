package com.main.Backend.model;

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

        Thread.sleep(processingTime);
        System.out.println(id + " processed " + currProd);

        target.addProduct(currProd);
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
