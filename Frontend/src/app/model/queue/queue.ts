import Konva from "konva";

export class Queue {
  count = -1;
  x;
  y;
  w;
  h;
  fill;
  draggable;

  constructor() {
    this.x = 100;
    this.y = 100;
    this.w = 120;
    this.h = 50;
    this.fill = 'gray';
    this.draggable = false;
  }

  getQueue(){
    this.count++;
    return new Konva.Rect({
      id: this.count.toString(),
      x: this.x,
      y: this.y,
      width: this.w,
      height: this.h,
      fill: this.fill,
      draggable: this.draggable
    });
  }
}
