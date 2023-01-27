import { AppComponent } from './../../app.component';
import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class SocketService {
  socket = new SockJS('http://localhost:8080/spa-websocket');
  stompClient = Stomp.over(this.socket);

  subscribe(topic: string, callback: any): void{
    const connected: boolean = this.stompClient.coonected;
    if(connected){
      this.subscribeToTopic(topic, callback);
      return;
    }

    this.stompClient.connect({}, (): any =>{
      this.subscribeToTopic(topic, callback)
    });
  }

  private subscribeToTopic(topic: string, callback: any): any{
    this.stompClient.subscribe(topic, (): any => {
      callback();
    });
  }
}
