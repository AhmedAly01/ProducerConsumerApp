import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class SimService {

  constructor(private http: HttpClient) { }

  startSim(queueArray: Array<string>, machineArray: Array<string>, feedProducts: boolean) {
    let params = new FormData();
    for(let element of queueArray ) {
      params.append("queues", element);
    }
    for (let element of machineArray) {
      params.append("machines", element);
    }
    params.append("feedProducts", "true");

    return this.http.post<any>("http://localhost:8080/sim/start", params);
  }

  stopSim() {
    return this.http.post<any>("http://localhost:8080/sim/stop", '');
  }

  pauseSim(){
    return this.http.post<any>("http://localhost:8080/sim/pause", '');

  }

  resumeSim(){
    return this.http.post<any>("http://localhost:8080/sim/resume", '');
  }

  changeFeed(feedProducts: boolean) {
    let params = new FormData();
    params.append("feed", String(feedProducts));
    return this.http.post<any>("http://localhost:8080/sim/feed", params);
  }

}
