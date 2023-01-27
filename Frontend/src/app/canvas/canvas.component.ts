import { SocketService } from '../Service/socket/socket.service';
import { Component, OnInit } from '@angular/core';
import Konva from "konva";
import Stage = Konva.Stage;
import Layer = Konva.Layer;
import {Machine} from "../model/machine/machine";
import {Queue} from "../model/queue/queue";
import {SimService} from "../Service/sim/sim.service";
import {WebSocketAPI} from "../Service/socket/websocketapi";

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
  prodInQueue: Array<any> = [];
  products: Array<number> = [];
  isConnect: boolean = false;
  source: any;
  sim: string = "Start Simulation";
  input: string = "Start Input";
  pauseResume: string = "Pause";
  isSimOn: boolean = false;
  isInputOn: boolean = false;
  isPaused: boolean = false;
  Data: any;
  dataArr: Array<any> = [];

  constructor(private simService: SimService, private socketService: SocketService, private socketAPI: WebSocketAPI) { }

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
    let group = new Konva.Group({
      draggable: true
    });
    let text = new Konva.Text({
      x: m.getAttr('x') - 10,
      y: m.getAttr('y') - 7,
      text: "M" + m.getAttr('id'),
      fontSize: 16
    })
    group.add(m);
    group.add(text);
    this.layer.add(group);
    this.stage.draw();
  }

  addQueue() {
    let q = this.queue.getQueue();
    if (this.queueArray.length == 0) {
      q.setAttr('fill', 'white');
      q.setAttr('stroke', "black");
      q.setAttr('strokeWidth', 3);
    }
    this.queueArray.push(q.getAttr('id'));
    let group = new Konva.Group({
      draggable: true
    });
    let text = new Konva.Text({
      x: q.getAttr('x') - 5 + q.getAttr('width') / 2,
      y: q.getAttr('y') - 5 + q.getAttr('height') / 2,
      text: "0",
      fontSize: 16
    })
    this.prodInQueue.push(text);
    group.add(q);
    group.add(text);
    this.layer.add(group);
    this.stage.draw();
  }

  mouseListeners() {
    this.stage.on("click", (event) =>{
      const pos: any = this.stage.getPointerPosition();
      if (!this.isConnect && (event.target.className == "Circle")) {
        this.connection = new Konva.Line({
          points: [event.target.getParent().getAttr('x') + 140, event.target.getParent().getAttr('y') + 100],
          stroke: 'black',
          strokeWidth: 5,
          lineJoin: 'round',
          lineCap: 'round',
        });
        this.source = event.target
        this.isConnect = true;
      }
      else if (this.isConnect && (event.target.className == "Circle")){
        if (this.source.className !== event.target.className) {
          let newPoints = [this.connection.getAttr('points')[0], this.connection.getAttr('points')[1], event.target.getParent().getAttr('x') + 60, event.target.getParent().getAttr('y') + 100];
          this.connection.setAttr('points', newPoints);
          this.machineArray[event.target.getAttr('id')] = this.machineArray[event.target.getAttr('id')].concat(" " + this.source.getAttr('id'));
        }
        this.isConnect = false;
      }
      else if (!this.isConnect && (event.target.className == "Rect")) {
        this.connection = new Konva.Line({
          points: [event.target.getParent().getAttr('x') + 140 + event.target.getAttr('width') / 2, event.target.getParent().getAttr('y') + 100 + event.target.getAttr('height') / 2],
          stroke: 'black',
          strokeWidth: 5,
          lineJoin: 'round',
          lineCap: "round",
        });
        this.source = event.target;
        this.isConnect = true;
      }
      else if (this.isConnect && (event.target.className == "Rect")){
        if (this.source.className != event.target.className) {
          let newPoints = [this.connection.getAttr('points')[0], this.connection.getAttr('points')[1], event.target.getParent().getAttr('x') + 60 + event.target.getAttr('width') / 2, event.target.getParent().getAttr('y') + 100 + event.target.getAttr('height') / 2];
          this.connection.setAttr('points', newPoints);
          this.machineArray[this.source.getAttr('id')] = this.machineArray[this.source.getAttr('id')].concat(" " + event.target.getAttr('id'));
        }
        this.isConnect = false;
      }
      this.layer.add(this.connection);
    });
  }

  controlSim() {
    if (!this.isSimOn){
      this.sim = "Stop Simulation";
      this.isSimOn = true;
      this.input = "Stop Input";
      this.isInputOn = true;
      document.getElementById("pause-btn")!.style.display = "inline-block";
      document.getElementById("input-btn")!.style.display = "inline-block";
      document.getElementById("replay-btn")!.style.display = "inline-block";
      document.getElementById("machine-btn")!.style.display = "none";
      document.getElementById("queue-btn")!.style.display = "none";
      this.simService.startSim(this.queueArray, this.machineArray, true).subscribe();
      this.socketAPI._connect();
      this.onNewValueReceive();
    }
    else {
      this.sim = "Start Simulation";
      this.isSimOn = false;
      document.getElementById("pause-btn")!.style.display = "none";
      document.getElementById("input-btn")!.style.display = "none";
      document.getElementById("replay-btn")!.style.display = "none";
      document.getElementById("machine-btn")!.style.display = "inline-block";
      document.getElementById("queue-btn")!.style.display = "inline-block";
      this.simService.stopSim().subscribe();
    }
  }

  controlInput() {
    if (!this.isInputOn){
      this.input = "Stop Input";
      this.isInputOn = true;
      this.simService.changeFeed(true).subscribe();
    }
    else {
      this.input = "Start Input";
      this.isInputOn = false;
      this.simService.changeFeed(false).subscribe();
    }
  }

  onNewValueReceive() {
    this.socketService.getNewValue().subscribe(resp => {
      this.Data = resp;
      this.dataArr = this.Data.split(" ");
      if (this.dataArr[0] != '-1') {
        let shapes = this.stage.find('#' + this.dataArr[0]);
        for (let machine of shapes) {
          if (machine.className == "Circle") {
            if (this.dataArr[5] != '-1') {
              machine.setAttr('fill', "#" + Math.floor(this.dataArr[5] * 16777.215).toString(16));
            } else {
              machine.setAttr('fill', 'gray');
            }
          }
        }
        let text2 = this.prodInQueue[this.dataArr[3]];
        text2.setAttr('text', this.dataArr[4]);
      }
      let text1 = this.prodInQueue[this.dataArr[1]];
      text1.setAttr('text', this.dataArr[2]);
    });
  }

  pauseResumeSim() {
    if (!this.isPaused){
      this.pauseResume = "Resume";
      this.isPaused = true;
      this.simService.pauseSim().subscribe();
    }
    else {
      this.pauseResume = "Pause";
      this.isPaused = false;
      this.simService.resumeSim().subscribe();
    }
  }

  replay() {
    let shapes = this.stage.find("Circle");
    for (let shape of shapes) {
      shape.setAttr('fill', 'gray');
    }
    for (let prod of this.prodInQueue) {
      prod.setAttr('text', '0');
    }
    console.log(this.prodInQueue);
    this.simService.replay().subscribe();
  }
}
