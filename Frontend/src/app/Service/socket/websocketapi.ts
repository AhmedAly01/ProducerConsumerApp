import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { SocketService} from "./socket.service";
import { Injectable } from '@angular/core';

@Injectable({providedIn:'root'})
export class WebSocketAPI {
  webSocketEndPoint: string = 'http://localhost:8080/sba-websocket';
  topic: string = "/topic/message";
  stompClient: any;

  constructor(private websocketShare: SocketService){

  }
  _connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function (frame: any) {
      _this.stompClient.subscribe(_this.topic, function (sdkEvent: any) {
        _this.onMessageReceived(sdkEvent);
      });
      //_this.stompClient.reconnect_delay = 2000;
    }, this.errorCallBack);
  };

  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error: any) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this._connect();
    }, 5000);
  }

  onMessageReceived(message: any) {
    this.websocketShare.onNewValueReceive(message.body);
  }
}
