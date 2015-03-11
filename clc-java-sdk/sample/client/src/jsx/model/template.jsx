
let { Model, Collection } = Backbone;


export class Template extends Model {}

export class TemplateList extends Collection {

    constructor (models = [], options = {}) {
        this.url = `/datacenter/${options.dataCenter}/template`;
        this.model = Template;

        super(models, options);
    }

}