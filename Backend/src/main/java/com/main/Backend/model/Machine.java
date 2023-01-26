package com.main.Backend.model;

public class Machine implements Runnable{
    private String id;
    private WaitingLine source;
    private WaitingLine target;

    private Integer currProduct = null;

    public Machine(String id, WaitingLine source, WaitingLine target){
        this.source = source;
        this.target = target;
        this.id = id;
    }

    private void processProduct() throws InterruptedException {
        Integer currProd;

        currProd = source.getProduct();

        Thread.sleep(5000);
        System.out.println(id + " processed " + currProd);

        target.addProduct(currProd);


    }

    @Override
    public void run(){
        try {
            while (true) {
                processProduct();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
