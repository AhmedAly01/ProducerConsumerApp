package com.main.Backend.controllers;


import com.main.Backend.service.Simulation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/")
@RestController
public class simController {
    private Simulation simulation = new Simulation();


    @PostMapping("/sim/start")
    public ResponseEntity<?> startSim(@RequestParam("queues") String[] queuesIds,
                                      @RequestParam("machines") String[] machineIds,
                                      @RequestParam("feedProducts") boolean feedProducts){
        simulation.buildGraph(queuesIds, machineIds, feedProducts);
        simulation.startSim();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/sim/stop")
    public ResponseEntity<?> stopSim(){
        simulation.stopSim();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sim/pause")
    public ResponseEntity<?> pauseSim(){
        simulation.pauseSim();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sim/resume")
    public ResponseEntity<?> resumeSim(){
        simulation.resumeSim();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sim/feed")
    public ResponseEntity<?> changeFeed(@RequestParam("feed") boolean feedProducts) throws InterruptedException {
        simulation.setFeedProducts(feedProducts);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
