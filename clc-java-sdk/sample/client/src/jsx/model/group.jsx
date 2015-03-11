
let { Model, Collection } = Backbone;


export class Group extends Model { }

export class GroupList extends Collection {

    constructor (models = [], options = {}) {
        this.url = `/datacenter/${options.dataCenter}/group`;
        this.model = Group;

        super(models, options);
    }

}