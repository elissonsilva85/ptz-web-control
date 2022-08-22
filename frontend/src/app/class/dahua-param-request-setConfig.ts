export class DahuaParamRequestSetConfig {

    name: string = "";
    table: any[] = [];
    options: string[] = [];

    public constructor(_name: string, _table: any[]) {
        this.name = _name;
        this.table = _table;
    }

}
