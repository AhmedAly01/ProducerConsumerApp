import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { CanvasComponent } from './canvas/canvas.component';
import { HttpClientModule } from "@angular/common/http";
import { SocketService } from './Service/socket/socket.service';

@NgModule({
  declarations: [
    AppComponent,
    CanvasComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [SocketService],
  bootstrap: [AppComponent]
})
export class AppModule { }
