import Konva from "konva";

export class Machine {
  count = -1;
  x;
  y;
  radius;
  fill;
  draggable;

  constructor() {
    this.x = 100;
    this.y = 100;
    this.radius = 50;
    this.fill = 'gray';
    this.draggable = true;
  }

  getMachine(){
    this.count++;
    return new Konva.Circle({
      id: this.count.toString(),
      x: this.x,
      y: this.y,
      radius: this.radius,
      fill: this.fill,
      draggable: this.draggable
    });
  }
}
