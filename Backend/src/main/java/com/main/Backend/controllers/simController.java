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
    public ResponseEntity<?> startSim(@RequestParam("queues") String[] queuesIds, @RequestParam("machines") String[] machineIds){
        simulation.buildGraph(queuesIds, machineIds);
        simulation.startSim();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/sim/stop")
    public ResponseEntity<?> stopSim(){
        simulation.stopSim();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
