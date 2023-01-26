package com.main.Backend.service;

import com.main.Backend.model.Machine;
import com.main.Backend.model.WaitingLine;

import java.util.ArrayList;
import java.util.Random;

public class Simulation implements Runnable{
    private ArrayList<WaitingLine> waitingLines = new ArrayList<WaitingLine>();
    private ArrayList<Machine> machines = new ArrayList<Machine>();
    private ArrayList<Thread> threads = new ArrayList<Thread>();
    private boolean feedProducts = false;
    private Thread simThread = new Thread(this, "sim thread");

    public void buildGraph(String[] queueIds, String[] machineIds, boolean feedProducts){
        this.feedProducts = feedProducts;
        for (String queueId : queueIds) {
            waitingLines.add(new WaitingLine(queueId));
        }

        for(String machineId : machineIds){
            String[] machineVals = machineId.split(" ");
            int min = 500;
            int max = 3000;
            threads.add(new Thread(new Machine(machineVals[0],
                    waitingLines.get(Integer.parseInt(machineVals[1])),
                    waitingLines.get(Integer.parseInt(machineVals[2])), (long) Math.floor(Math.random() *(max - min + 1) + min)), "Machine " + machineVals[0]));
        }
    }


    public void setFeedProducts(boolean feedProducts) throws InterruptedException {
        this.feedProducts = feedProducts;
        if (feedProducts) {
            simThread.resume();
        } else {
            simThread.suspend();
        }
        System.out.println(simThread.getState());
    }

    public void startSim(){
        for(Thread thread: threads){
            thread.start();
        }
        if(simThread.getState().equals(Thread.State.NEW))
            simThread.start();
        else
            simThread.resume();
    }


    public void stopSim(){
        for(Thread thread: threads){
            thread.interrupt();
        }
        for(WaitingLine waitingLine: waitingLines){
            System.out.println(waitingLine.getSize());
        }

        threads.clear();
        machines.clear();
        waitingLines.clear();

        simThread.suspend();
    }


    public void pauseSim(){
        for(Thread thread: threads){
            thread.suspend();
        }
        for(WaitingLine waitingLine: waitingLines){
            System.out.println(waitingLine.getSize());
        }

        simThread.suspend();
    }


    public void resumeSim(){
        for(Thread thread: threads){
            thread.resume();
        }
        for(WaitingLine waitingLine: waitingLines){
            System.out.println(waitingLine.getSize());
        }
        if(feedProducts)
            simThread.resume();
    }

    public void feedProducts() throws InterruptedException {
        WaitingLine waitingLine = waitingLines.get(0);
        Random rand = new Random();
        int min = 500;
        int max = 2000;
        while(feedProducts){
            waitingLine.addProduct(rand.nextInt(1000));
            Thread.sleep((long) Math.floor(Math.random() *(max - min + 1) + min));
        }
    }


    @Override
    public void run() {
        try {
            feedProducts();
        } catch (InterruptedException e) {
        }
    }
}
