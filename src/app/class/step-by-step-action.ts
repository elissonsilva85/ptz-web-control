import { RcpSession } from "./rcp-session";
import { StepByStepActionParam } from "./step-by-step-action-param";

export enum StepByStepActionStatus {
    PENDING = 0,
    RUNNING,
    DONE
  }

export class StepByStepAction {

    private _title: string;
    private _description: string;
    private _ptz: string;
    
    private _functionName: string;

    private _timeFromBegining: number = 0;
    private _executionTime: number = 0;
    private _waitingTime: number = 0;

    private _status: StepByStepActionStatus = StepByStepActionStatus.PENDING;

    public _params: StepByStepActionParam[];

    constructor(title: string = "", desc: string = "", funcName: string = "", execTime: number = 0, paramsType: StepByStepActionParam[] = []) {
        this._title = title;
        this._description = desc;
        this._functionName = funcName;
        this._executionTime = execTime;
        this._params = paramsType;
    }

    public getTitle(): string {
        return this._title;
    }

    public getDescription(): string {
        return this._description;
    }

    public setPTZ(ptz: string) {
        this._ptz = ptz;
    }

    public getStartTime(): number {
        return this._timeFromBegining;
    }

    public getDuration(): number {
        return this._waitingTime + this._executionTime;
    }

    public getWaitingTime(): number {
        return this._waitingTime;
    }

    public setWaitingTime(waitingTime: number) {
        this._waitingTime = waitingTime;
    }

    public getExecutionTime() {
        return this._executionTime;
    }
  
    public updateStartTime( timeFromBegining: number) {
        this._timeFromBegining = timeFromBegining;
    }

    public getParams(): StepByStepActionParam[] {
        return this._params;
    }      

    public getParamsToString(): string {
        return this._params
            .filter( p => (p.getType() != "label") )
            .reduce( (acc, curr) => {
                return (acc ? acc + " | " : "" ) + curr.getShortLabel() + ": " + curr.getValue();
            }, "");
    }

    public setParamValue(idx: number, value: string) {
        this._params[idx].setValue(value);
    }

    public getFunctionName(): string {
        return this._functionName;
    }

    public isPending(): boolean {
        return this._status == StepByStepActionStatus.PENDING;
    }

    public isRunning(): boolean {
        return this._status == StepByStepActionStatus.RUNNING;
    }

    public isDone(): boolean {
        return this._status == StepByStepActionStatus.DONE;
    }

    public setStatusPending() {
        this._status = StepByStepActionStatus.PENDING;
    }

    public setStatusDone() {
        this._status = StepByStepActionStatus.DONE;
    }

    public setStatusRunning() {
        this._status = StepByStepActionStatus.RUNNING;
    }

    run(rcp: RcpSession): Promise<any> {

        return new Promise<any>( (resolve, reject) => {

            let param = this._params
                .filter( p => (p.getType() != "label") )
                .map( p => p.getValue() );

            rcp[this._functionName].apply(rcp, param)
                .then( () => {
                    setTimeout( () => {
                        resolve(null);
                    }, this._waitingTime );                    
                })
                .catch( (err) => {
                    reject( ( typeof err == "string" ? err : JSON.stringify(err) ) );
                });    
        });
    }

    clone() {
        let obj = new StepByStepAction(
            this._title,
            this._description,
            this._functionName,
            this._executionTime,
            this._params
        );

        return obj;
    }

}