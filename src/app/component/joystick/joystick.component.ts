import { Component, OnInit, Input, HostListener } from '@angular/core';
import { JoystickEvent } from 'ngx-joystick';
import { JoystickManagerOptions } from 'nipplejs';
import { Subject } from 'rxjs';
import { RcpService } from '../../service/rcp.service';
import { JoystickQueueData } from '../../class/joystick-queue-data';

export enum JoystickDirection {
  LeftDown = "LeftDown",
  LeftUp = "LeftUp",
  RightDown = "RightDown",
  RightUp = "RightUp"
}

@Component({
  selector: 'app-joystick',
  templateUrl: './joystick.component.html',
  styleUrls: ['./joystick.component.css']
})
export class JoystickComponent implements OnInit {

  @Input() ptz: string;

  showX: number;
  showY: number;

  joysctickOptions: JoystickManagerOptions = {
    mode: 'dynamic',
    color: 'blue',
    multitouch: true
  };

  joystickOn: boolean = false;
  joystickQueue = new Subject<JoystickQueueData>();
  joystickData = {
    speedLimit: {
      "1": [5,10],
      "2": [10,15],
      "3": [15,20],
      "4": [20,25],
      "5": [25,30],
      "6": [30,35],
      "7": [35,40],
      "8": [40,50]
    },
    speed: { "LeftDown": [0,0], "LeftUp": [0,0], "RightDown": [0,0], "RightUp": [0,0] },
    lastSpeed: { "LeftDown": [0,0], "LeftUp": [0,0], "RightDown": [0,0], "RightUp": [0,0] }
  }

  zoomDirection = 0;
  zoomStopTimeout = null;

  constructor(public rcp: RcpService) { }

  ngOnInit(): void {
    this.processJoystickQueue();
  }

  processJoystickQueue() {
    this.joystickQueue.subscribe( async (msg: JoystickQueueData) => {
      if(msg.startStop == "start") await this.rcp.getSession(this.ptz).startJoystick(msg.direction, msg.speed1, msg.speed2);
      if(msg.startStop == "stop")  await this.rcp.getSession(this.ptz).stopJoystick(msg.direction, msg.speed1, msg.speed2);
    })
  }

  getSpeed(pos: number): number {
    for(var speed in this.joystickData.speedLimit) {
      let limits = this.joystickData.speedLimit[speed];
      if(Math.abs(pos) > limits[0] && Math.abs(pos) <= limits[1])
        return parseInt(speed);
    }
    return 0;
  }

  stopAll() {
    for(var direction in this.joystickData.speed) {
      if(this.joystickData.speed[direction].reduce( (acc, cur) => { return acc + cur; }, 0) > 0) {
        this.joystickQueue.next({
          startStop: "stop",
          direction: direction,
          speed1: this.joystickData.speed[direction][0],
          speed2: this.joystickData.speed[direction][1],
        });
      }
      this.joystickData.speed[direction] = [0,0];
      this.joystickData.lastSpeed[direction] = [0,0];
    }
  }

  checkSpeedChange() {
    for(var direction in this.joystickData.speed) {
      let last   = this.joystickData.lastSpeed[direction].reduce( (acc, cur) => { return acc + cur; }, 0);
      let actual = this.joystickData.speed[direction].reduce( (acc, cur) => { return acc + cur; }, 0);
      if(last != actual) {
        this.joystickData.lastSpeed[direction] = this.joystickData.speed[direction];
        if(actual > 0)
          this.joystickQueue.next({
            startStop: "start",
            direction: direction,
            speed1: this.joystickData.speed[direction][0],
            speed2: this.joystickData.speed[direction][1]
          });
      }
    }
  }

  onStart() {
    this.joystickOn = true;
  }

  onEnd() {
    this.joystickOn = false;
    this.stopAll();
  }

  onMoveDynamic(event: JoystickEvent) {
    let xPos = this.showX = event.data.instance.frontPosition.x;
    let yPos = this.showY =  event.data.instance.frontPosition.y;

    let xSpeed = this.getSpeed(xPos);
    let ySpeed = this.getSpeed(yPos);

    if( (ySpeed + xSpeed) == 0) {
      this.stopAll();
      return;
    }

    let direction: JoystickDirection = null;
    if(xPos <= 0 && yPos <= 0) direction = JoystickDirection.LeftUp;
    if(xPos <= 0 && yPos >  0) direction = JoystickDirection.LeftDown;
    if(xPos >  0 && yPos <= 0) direction = JoystickDirection.RightUp;
    if(xPos >  0 && yPos >  0) direction = JoystickDirection.RightDown;

    this. moveByXandYSpeed(direction, xSpeed, ySpeed);
  }

  moveByXandYSpeed(direction: JoystickDirection, xSpeed: number, ySpeed: number) {

    this.joystickData.speed["LeftUp"] = [ 0, 0 ];
    this.joystickData.speed["LeftDown"] = [ 0, 0 ];
    this.joystickData.speed["RightUp"] = [ 0, 0 ];
    this.joystickData.speed["RightDown"] = [ 0, 0 ];

    this.joystickData.speed[direction] = [ ySpeed, xSpeed ];

    this.checkSpeedChange();

  }

  stopZoom(direction: number) {
    if(this.zoomStopTimeout)
      clearTimeout(this.zoomStopTimeout);
    if(direction == 1)
      this.rcp.getSession(this.ptz).stopZoomIn();
    if(direction == -1)
      this.rcp.getSession(this.ptz).stopZoomOut();
  }

  startZoom(direction: number) {
    if(direction == 1)
      this.rcp.getSession(this.ptz).startZoomIn();
    if(direction == -1)
      this.rcp.getSession(this.ptz).startZoomOut();
}

  @HostListener('mousewheel', ['$event'])
  scroll(event) {
    if(this.joystickOn) {
      event.preventDefault();
      let wheelDelta = Math.max(-1, Math.min(1, (event.wheelDelta || -event.detail)));
      console.log("Entered mouse wheel", wheelDelta);

      if(this.zoomDirection != wheelDelta)
      {
        if(this.zoomDirection != 0)
          this.stopZoom(this.zoomDirection);
        if(wheelDelta != 0)
          this.startZoom(wheelDelta);
        this.zoomDirection = wheelDelta;
      }

      if(wheelDelta != 0) {
        clearTimeout(this.zoomStopTimeout);
        this.zoomStopTimeout = setTimeout( () => {
          this.stopZoom(wheelDelta);
          this.zoomDirection = 0;
        }, 500);
      }

      console.log(wheelDelta);
    }
  }
}
