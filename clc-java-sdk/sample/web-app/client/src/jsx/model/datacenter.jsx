
let { Model, Collection } = Backbone;


export class DataCenter extends Model { }

export class DataCenterList extends Collection {

    constructor (args) {
        this.url = '/datacenter';
        this.model = DataCenter;

        super(args);
    }

}