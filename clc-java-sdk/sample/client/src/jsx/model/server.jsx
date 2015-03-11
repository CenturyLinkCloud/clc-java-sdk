
let { Model, Collection } = Backbone;


export class Server extends Model {

    constructor (attributes = {}, options = {}) {
        this.url = () => `/datacenter/${this.get('dataCenter')}/server/${this.get('id') || ''}`;

        super(attributes, options);
    }

}

export class ServerList extends Collection {

    constructor (models = [], options = {}) {
        this.url = `/datacenter/${options.dataCenter}/server`;
        this.model = Server;

        super(models, options);
    }

}