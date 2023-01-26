package com.main.Backend.service;

import com.main.Backend.model.Machine;
import com.main.Backend.model.WaitingLine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class Simulation implements Runnable{
    private ArrayList<WaitingLine> waitingLines = new ArrayList<WaitingLine>();
    private ArrayList<Machine> machines = new ArrayList<Machine>();
    private ArrayList<Thread> threads = new ArrayList<Thread>();
    private boolean autoFeed = true;

    public void buildGraph(String[] queueIds, String[] machineIds){
        for (String queueId : queueIds) {
            waitingLines.add(new WaitingLine(queueId));
        }

        for(String machineId : machineIds){
            String[] machineVals = machineId.split(" ");
            threads.add(new Thread(new Machine(machineVals[0],
                    waitingLines.get(Integer.parseInt(machineVals[1])),
                    waitingLines.get(Integer.parseInt(machineVals[2]))), "Machine " + machineVals[0]));
        }
    }

    public void startSim(){
        for(Thread thread: threads){
            thread.start();
        }
        Thread simThread = new Thread(this, "sim thread");
    }


    public void stopSim(){
        for(Thread thread: threads){
            thread.interrupt();
        }
    }

    public void feedProducts() throws InterruptedException {
        WaitingLine waitingLine = waitingLines.get(0);
        Random rand = new Random();
        while(autoFeed){
            waitingLine.addProduct(rand.nextInt());
            Thread.sleep((long) Math.floor(Math.random() *(2000 - 100 + 1) + 100));
        }
    }


    @Override
    public void run() {

    }
}
