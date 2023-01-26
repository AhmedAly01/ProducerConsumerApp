import { Component, OnInit } from '@angular/core';
import Konva from "konva";
import Stage = Konva.Stage;
import Layer = Konva.Layer;
import {Machine} from "../model/machine/machine";
import {Queue} from "../model/queue/queue";

@Component({
  selector: 'app-canvas',
  templateUrl: './canvas.component.html',
  styleUrls: ['./canvas.component.css']
})
export class CanvasComponent implements OnInit {
  stage!: Stage;
  layer!: Layer;
  machine = new Machine();
  queue = new Queue();
  connection: Konva.Shape = new Konva.Shape();
  queueArray: Array<string> = [];
  machineArray: Array<string> = [];
  products: Array<number> = [];
  isConnect: boolean = false;
  source: any;
  sim: string = "Start Simulation";
  input: string = "Start Input";
  isSimOn: boolean = false;
  isInputOn: boolean = false;

  constructor() { }

  ngOnInit(): void {
    this.stage = new Stage({
      container: 'container',
      width: window.innerWidth,
      height: window.innerHeight - 100
    });
    this.layer = new Layer();
    this.stage.add(this.layer);
    this.mouseListeners();
  }


  addMachine() {
    let m = this.machine.getMachine();
    this.machineArray.push(m.getAttr('id'));
    this.layer.add(m);
    this.stage.draw();
  }

  addQueue() {
    let q = this.queue.getQueue();
    this.queueArray.push(q.getAttr('id'));
    this.layer.add(q);
    this.stage.draw();
  }

  mouseListeners() {
    this.stage.on("click", (event) =>{
      console.log(event.target);
      const pos: any = this.stage.getPointerPosition();
      if (!this.isConnect && (event.target.className == "Circle")) {
        this.connection = new Konva.Line({
          points: [event.target.getAttr('x'), event.target.getAttr('y')],
          stroke: 'black',
          strokeWidth: 2,
          lineJoin: 'round',
        });
        this.source = event.target
        this.isConnect = true;
      }
      else if (this.isConnect && (event.target.className == "Circle")){
        let newPoints = [this.connection.getAttr('points')[0], this.connection.getAttr('points')[1], event.target.getAttr('x'), event.target.getAttr('y')];
        this.connection.setAttr('points', newPoints);
        if (this.source.className != event.target.className) {
          this.machineArray[event.target.getAttr('id')] = this.machineArray[event.target.getAttr('id')].concat(" " + this.source.getAttr('id'));
        }
        this.isConnect = false;
      }
      else if (!this.isConnect && (event.target.className == "Rect")) {
        this.connection = new Konva.Line({
          points: [event.target.getAttr('x') + event.target.getAttr('width') / 2, event.target.getAttr('y') + event.target.getAttr('height') / 2],
          stroke: 'black',
          strokeWidth: 2,
          lineJoin: 'round',
        });
        this.source = event.target;
        this.isConnect = true;
      }
      else if (this.isConnect && (event.target.className == "Rect")){
        let newPoints = [this.connection.getAttr('points')[0], this.connection.getAttr('points')[1], event.target.getAttr('x') + event.target.getAttr('width') / 2, event.target.getAttr('y') + event.target.getAttr('height') / 2];
        this.connection.setAttr('points', newPoints);
        if (this.source.className != event.target.className) {
          this.machineArray[this.source.getAttr('id')] = this.machineArray[this.source.getAttr('id')].concat(" " + event.target.getAttr('id'));
        }
        this.isConnect = false;
      }
      this.layer.add(this.connection);
      console.log(this.machineArray);
    });
  }

  controlSim() {
    if (!this.isSimOn){
      this.sim = "Stop Simulation";
      this.isSimOn = true;
    }
    else {
      this.sim = "Start Simulation";
      this.isSimOn = false;
    }
  }

  controlInput() {
    if (!this.isInputOn){
      this.input = "Stop Input";
      this.isInputOn = true;
    }
    else {
      this.input = "Start Input";
      this.isInputOn = false;
    }
  }

  play() {
    
  }

}
