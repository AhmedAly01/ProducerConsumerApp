package com.main.Backend.service;

import com.main.Backend.SnapShot.CareTaker;
import com.main.Backend.SnapShot.Memento;
import com.main.Backend.model.Machine;
import com.main.Backend.model.Product;
import com.main.Backend.model.WaitingLine;

import java.util.ArrayList;
import java.util.Random;

public class Simulation implements Runnable{
    private ArrayList<WaitingLine> waitingLines = new ArrayList<WaitingLine>();
    private ArrayList<Machine> machines = new ArrayList<Machine>();
    private ArrayList<Thread> threads = new ArrayList<Thread>();
    private boolean feedProducts = false;
    private Thread simThread;
    private ArrayList<Product> products = new ArrayList<Product>();

    private boolean replaying = false;

    public void buildGraph(String[] queueIds, String[] machineIds, boolean feedProducts){
        waitingLines.clear();
        machines.clear();
        threads.clear();
        products.clear();
        CareTaker.clearSnaps();

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
        replaying = false;
        for(Thread thread: threads){
            thread.start();
        }
        simThread = new Thread(this, "sim thread");
        simThread.start();
    }



    public void stopSim(){
        replaying = false;
        for(Thread thread: threads){
            thread.interrupt();
        }
        for(WaitingLine waitingLine: waitingLines){
            System.out.println(waitingLine.getSize());
        }

        threads.clear();
        machines.clear();
        waitingLines.clear();

        simThread.interrupt();
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
        if(!replaying){
            Random rand = new Random();
            int min = 500;
            int max = 2000;
            while (feedProducts) {
                int prodNum = rand.nextInt(1000);
                long delayTime = (long) Math.floor(Math.random() * (max - min + 1) + min);
                Product product = new Product(prodNum, delayTime);
                products.add(product);
                Memento memento = new Memento(product);
                CareTaker.add(memento);
                waitingLine.addProduct(prodNum);
                Thread.sleep(delayTime);
            }
        }else{
            for(Product product: products){
                waitingLine.addProduct(product.getProdNumber());
                Thread.sleep(product.getDelayTime());
            }
        }
    }

    public void replay(){
        simThread.suspend();

        for(Thread thread: threads){
            thread.interrupt();
        }
        threads.clear();

        for(WaitingLine waitingLine: waitingLines){
            waitingLine.clearData();
        }

        for(Machine machine: machines){
            machine.clearMachine();
            Thread thread = new Thread(machine, "Machine " + machine.getId());
            threads.add(thread);
            thread.start();
        }

        products.clear();
        ArrayList<Memento> mementos = CareTaker.get();
        for(Memento memento: mementos){
            products.add(memento.getState());
        }
        replaying = true;
        simThread.resume();
    }


    @Override
    public void run() {
        try {
            feedProducts();
        } catch (InterruptedException e) {
        }
    }
}
