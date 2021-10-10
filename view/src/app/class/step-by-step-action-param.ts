export class StepByStepActionParam {

    private _label: string;
    private _shortLabel: string;
    private _type: string;
    private _options: any[];
    public _value: string = "";
        
    constructor(label: string = "", shortLabel: string = "", type: string = "", options: any[] = []) {
        this._label = label;
        this._shortLabel = (shortLabel ? shortLabel : label);
        this._type = type;
        this._options = options;

        if(type == "slider")
            this._value = "0";
    }

    public getValue(): string {
        return this._value;
    }

    public setValue(value: string) {
        this._value = value;
    }

    public getLabel(): string {
        return this._label;
    }

    public getShortLabel(): string {
        return this._shortLabel;
    }

    public getType(): string {
        return this._type;
    }
    public getOptions(): any[] {
        return this._options;
    }

    public clone(): StepByStepActionParam {
        let obj = new StepByStepActionParam(
            this._label,
            this._shortLabel,
            this._type,
            this._options
        );

        obj.setValue(this.getValue());

        return obj;
    }
}