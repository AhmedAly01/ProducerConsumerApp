package com.main.Backend.service;

import com.main.Backend.model.Machine;
import com.main.Backend.model.WaitingLine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Queue;

public class Simulation {
    private ArrayList<WaitingLine> waitingLines = new ArrayList<WaitingLine>();
    private ArrayList<Machine> machines = new ArrayList<Machine>();
    private ArrayList<Thread> threads = new ArrayList<Thread>();


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
        WaitingLine waitingLine = waitingLines.get(0);
        waitingLine.addProduct(0);
        waitingLine.addProduct(1);
        waitingLine.addProduct(2);
        waitingLine.addProduct(3);
        waitingLine.addProduct(4);
        waitingLine.addProduct(5);

    }

    public void startSim(){
        for(Thread thread: threads){
            thread.start();
        }

    }

    public void stopSim(){
        for(Thread thread: threads){
            thread.interrupt();
        }
    }


}
