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
  queueArray: Array<number> = [];
  machineArray: Array<Array<number>> = [];

  constructor() { }

  ngOnInit(): void {
    this.stage = new Stage({
      container: 'container',
      width: window.innerWidth,
      height: window.innerHeight - 100
    });
    this.layer = new Layer();
    this.stage.add(this.layer);
  }


  addMachine() {
    let m = this.machine.getMachine();
    console.log(m);
    this.layer.add(m);
    this.stage.draw();
  }

  addQueue() {
    let q = this.queue.getQueue();
    console.log(q);
    this.layer.add(q);
    this.stage.draw();
  }
}
