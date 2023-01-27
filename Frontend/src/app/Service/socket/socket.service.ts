import { AppComponent } from './../../app.component';
import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { environment} from "../../../environments/environment";
import { catchError, tap, switchAll } from 'rxjs/operators';
import { EMPTY, Subject } from 'rxjs';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

export const WS_ENDPOINT = environment;

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
