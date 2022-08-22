import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PtzAbstractSession } from '../class/ptz-abstract-session';
import { PtzDahuaSession } from '../class/ptz-dahua-session';
import { StepByStepAction } from '../class/step-by-step-action';
import { StepByStepActionParam } from '../class/step-by-step-action-param';

export class StepByStepPtzAction {
  name: string = "";
  actions : StepByStepAction[] = [];
}

export class StepByStepPtz {
  ptz: string = "";
  currentTimelineActions : StepByStepAction[] = [];
  currentTimelineName: string = "";
  currentExecutionTime: number = 0;
  isTimelineRunning: boolean = false;
  timelineActions: StepByStepPtzAction[] = [];

  constructor(p: string = "") {
    this.ptz = p;
  }
}

@Injectable({
  providedIn: 'root'
})
export class StepByStepService {

  public timelineData: StepByStepPtz[] = [];
  public availableActions : StepByStepAction[] = [];

  constructor(private snackBar: MatSnackBar) {
  }

  private _convertToStepByStepAction(array: any[]): StepByStepAction[] {
    return array.map( a => {
      a._params = a._params.map( p => Object.assign(new StepByStepActionParam(), p) )
      return Object.assign(new StepByStepAction(), a);
    });
  }

  private _convertToStepByStepPtz(array: StepByStepPtz[]): StepByStepPtz[] {

    return array.map( ptz => {
      ptz.currentTimelineActions = this._convertToStepByStepAction(ptz.currentTimelineActions);
      ptz.timelineActions = ptz.timelineActions.map( (a: StepByStepPtzAction) => {
        a.actions = this._convertToStepByStepAction(a.actions);
        return Object.assign(new StepByStepPtzAction(), a);
      });
      return Object.assign(new StepByStepPtz(), ptz);
    });
  }

  private _loadAvailableActions() {

    if(this.availableActions.length > 0) return;

    this.availableActions.push(new StepByStepAction(
      "Velocidade do Zoom",
      "Defina um valor de 0 a 100 para a velocidade do zoom",
      "setZoomSpeed",
      500,
      [
        new StepByStepActionParam( "Velocidade", null, "slider", [0, 100, 1] )
      ]));

    this.availableActions.push(new StepByStepAction(
      "Zoom In",
      "Defina um tempo de duração para a ação de Zoom In",
      "startZoomIn",
      100,
      [
        new StepByStepActionParam( "Defina apenas o tempo de espera", null, "label", null )
      ]));

    this.availableActions.push(new StepByStepAction(
      "Zoom Out",
      "Defina um tempo de duração para a ação de Zoom Out",
      "startZoomOut",
      100,
      [
        new StepByStepActionParam( "Defina apenas o tempo de espera", null, "label", null )
      ]));

    this.availableActions.push(new StepByStepAction(
      "Focus In",
      "Defina um tempo de duração para a ação de Focus In",
      "startFocusIn",
      100,
      [
        new StepByStepActionParam( "Defina apenas o tempo de espera", null, "label", null )
      ]));

    this.availableActions.push(new StepByStepAction(
      "Focus Out",
      "Defina um tempo de duração para a ação de Focus Out",
      "startFocusOut",
      100,
      [
        new StepByStepActionParam( "Defina apenas o tempo de espera", null, "label", null )
      ]));

    this.availableActions.push(new StepByStepAction(
      "Joystick",
      "Defina a direção, velocidade e duração para o movimento da camera",
      "startJoystick",
      100,
      [
        new StepByStepActionParam( "Direção", "Dir", "select", [ "LeftDown", "LeftUp", "RightDown", "RightUp" ] ),
        new StepByStepActionParam( "Velocidade Up/Down", "U-D", "slider", [ 0, 8, 1 ] ),
        new StepByStepActionParam( "Velocidade Left/Right", "L-R", "slider", [ 0, 8, 1 ] ),
      ]));

    this.availableActions.push(new StepByStepAction(
      "Posição Específica",
      "Defina um valor de 0 a 3600 para a posição horizontal, 0 a 900 para vertical e 0 a 128 para zoom",
      "specificPosition",
      200,
      [
        new StepByStepActionParam( "Horizontal", "Horiz", "slider", [ 0, 3600, 1 ] ),
        new StepByStepActionParam( "Vertical", "Vert", "slider", [ 0, 900, 1 ] ),
        new StepByStepActionParam( "Zoom", null, "slider", [ 1, 128, 1 ] )
      ]));

    this.availableActions.push(new StepByStepAction(
      "Carregar Preset",
      "Defina o numero do preset que deve ser carregado",
      "loadPreset",
      200,
      [
        new StepByStepActionParam( "Preset", null, "select", [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12] ),
      ]));

    this.availableActions.push(new StepByStepAction(
      "Salvar Preset",
      "Defina o numero onde será salvo o preset",
      "savePreset",
      200,
      [
        new StepByStepActionParam( "ID", null, "select", [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12] ),
        new StepByStepActionParam( "Nome", null, "input", null )
      ]));

    this.availableActions.push(new StepByStepAction(
      "Interrompe",
      "Interrompe ultimo comando executado",
      "stopLastCall",
      200,
      [
        new StepByStepActionParam( "Sem parametros de configuração", null, "label", null )
      ]));
  }

  public initialize(ptz: string) {
    this._loadAvailableActions();

    if(this.timelineData.length == 0)
    {
      let tempNames = JSON.parse(localStorage.getItem('PTZTimelineData'));
      if(!tempNames) tempNames = [];
      this.timelineData = this._convertToStepByStepPtz(tempNames);
    }

    if(!this.timelineData.find( el => el.ptz == ptz ))
      this.timelineData.push(new StepByStepPtz(ptz));
  }

  public getTimelineNames(ptz: string): string[] {

    let element = this.timelineData.find( el => el.ptz == ptz );
    return element.timelineActions.map( a => a.name );

  }

  public getTimelineCurrentAction(ptz: string): StepByStepAction[] {

    let element = this.timelineData.find( el => el.ptz == ptz );
    return element.currentTimelineActions;

  }

  public loadTimelineByNameIntoCurrentAction(ptz: string, name: string): boolean {

    let element = this.timelineData.find( el => el.ptz == ptz );
    let actionIdx = element.timelineActions.findIndex( el => el.name == name )
    if(actionIdx == -1) return false;

    element.currentTimelineActions = element.timelineActions[actionIdx].actions;
    return true;
  }

  public isTimelineRunning(ptz: string): boolean {

    let element = this.timelineData.find( el => el.ptz == ptz );
    return element.isTimelineRunning;

  }

  public getCurrentExecutionTime(ptz: string) {

    let element = this.timelineData.find( el => el.ptz == ptz );
    return element.currentExecutionTime;

  }

  public saveTimelineAllData() {
    localStorage.setItem('PTZTimelineData', JSON.stringify(this.timelineData));
  }

  public saveTimelineByName(ptz: string, name: string) {

    let element = this.timelineData.find( el => el.ptz == ptz );
    element.currentTimelineName = name;

    let actionIdx = element.timelineActions.findIndex( el => el.name == name )
    if(actionIdx == -1)
      element.timelineActions.push({
        name: name,
        actions: element.currentTimelineActions.map( a => a.clone() )
      })
    else
      element.timelineActions[actionIdx].actions =
        element.currentTimelineActions.map( a => a.clone() );

    // Store the names and the actions
    this.saveTimelineAllData();
  }

  public removeTimeline(ptz: string, name: string) {

    let element = this.timelineData.find( el => el.ptz == ptz );
    element.currentTimelineName = "";

    let actionIdx = element.timelineActions.findIndex( el => el.name == name )
    if(actionIdx > -1) element.timelineActions.splice(actionIdx, 1);

    // Store the names and the actions
    this.saveTimelineAllData();
  }

  public clearTimeline(ptz: string) {

    let element = this.timelineData.find( el => el.ptz == ptz );
    element.currentTimelineActions = [];

  }

  public updateActionTimes(ptz: string) {

    let timeFromBegining = 0;
    let element = this.timelineData.find( el => el.ptz == ptz );

    element.currentTimelineActions.forEach( (a: StepByStepAction) => {
      a.updateStartTime(timeFromBegining);
      timeFromBegining += a.getDuration();
    });

  }

  public testItem(ptz: string, index: number, session: PtzDahuaSession) {

    let element = this.timelineData.find( el => el.ptz == ptz );

    element.currentTimelineActions[index]
      .run(session)
      .then( () => {
        session.stopLastCall();
        this.snackBar.open(`Ação executada com sucesso`, "Fechar", {
          duration: 3000,
        });
      })
      .catch( (err) => {
        this.snackBar.open(`Erro: ${err}`, "Fechar", {
          duration: 3000,
        });
      });

  }

  public runTimelineByName(ptz: string, name: string, session: PtzAbstractSession) {

    let element = this.timelineData.find( el => el.ptz == ptz );

    let actionIdx = element.timelineActions.findIndex( el => el.name == name )
    if(actionIdx == -1) return;

    element.currentTimelineActions = element.timelineActions[actionIdx].actions;

    this.runTimeline(ptz, session);
  }

  public runTimeline(ptz: string, session: PtzAbstractSession) {

    let element = this.timelineData.find( el => el.ptz == ptz );

    if(element.isTimelineRunning) {
      element.isTimelineRunning = false;
      session.stopLastCall();
      return;
    }

    let timeoutStep = 100;
    let currentStatus = "RUNNING";
    let startTime = new Date();

    var process = () => {
      console.log("process", element.isTimelineRunning);
      if(!element.isTimelineRunning) {
        return;
      }

      element.currentExecutionTime = (new Date()).getTime() - startTime.getTime();

      let pendingList = element.currentTimelineActions.filter( a => a.isPending() );
      let executionList = element.currentTimelineActions.filter( a => (a.isPending() && a.getStartTime() <= element.currentExecutionTime) );

      console.log("pendingList", pendingList);
      console.log("executionList", executionList);

      if( pendingList.length == 0 && executionList.length == 0 ) {
        currentStatus = "DONE";
        element.isTimelineRunning = false;
        element.currentExecutionTime = 0;
        element.currentTimelineActions.forEach( a => a.setStatusPending() );
        return;
      }

      if(executionList.length == 0) {
        console.log("setTimeout (1)", element.currentExecutionTime);
        setTimeout(_ => process(), timeoutStep);
        return;
      }

      // Run the actions sequentially
      // https://stackoverflow.com/questions/43082934/how-to-execute-promises-sequentially-passing-the-parameters-from-an-array
      if( executionList.length > 0 )
        executionList.reduce<Promise<any>>(
          (p, a): Promise<any> => {
            // -----------------------------------------------------
            return p.then(_ => {
              return new Promise<any>( (resolve, reject) => {
                if( currentStatus == "DONE" )
                  resolve(null);

                a.setStatusRunning();
                a.run(session)
                  .then(_ => a.setStatusDone())
                  .then(_ => resolve(null))
                  .catch(err => reject(err));
              });
            });
          },
          Promise.resolve()
        ).then(_ => {
          console.log("setTimeout (2)", element.currentExecutionTime);
          setTimeout(_ => process(), timeoutStep);
        })
        .catch((err) => {
            // -----------------------------------------------------
            this.snackBar.open(`Erro: ${err}`, "Fechar", {
              duration: 3000,
            });
            currentStatus == "DONE";
            //
        });
    }

    element.isTimelineRunning = true;
    element.currentTimelineActions.forEach( a => a.setStatusPending() );
    process();

  }

  public stopTimeline(ptz: string, session: PtzAbstractSession) {

    let element = this.timelineData.find( el => el.ptz == ptz );
    if(element.isTimelineRunning) {
      element.isTimelineRunning = false;
      session.stopLastCall();
      return;
    }

  }

}
