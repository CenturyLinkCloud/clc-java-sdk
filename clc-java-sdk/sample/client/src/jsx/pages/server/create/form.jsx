
import reactMixin from 'react-mixin';
import ValidationMixin from 'react-validation-mixin';
import Joi from 'joi';
import GroupSelect from './group-select.jsx';
import TemplateSelect from './template-select.jsx';


export class Form extends React.Component {

    constructor (args) {
        super(args);

        this.state = this.initialState();
        this.validatorTypes = this.getValidatorTypes();

        _.bindAll(this, 'onSubmit', 'render', 'onCancel');
    }

    getValidatorTypes () {
        return {
            name: Joi.string().required().max(6).label('Name'),
            group: Joi.string().min(1).required().label("Group"),
            cpu: Joi.number().required().min(1).max(1000).label('CPU'),
            ram: Joi.number().required().min(1).max(1000).label("RAM"),
            template: Joi.string().min(1).required().label("Template")
        };
    }

    initialState() {
        return { type: 'STANDARD', dataCenter: this.props.dataCenter };
    }

    onSubmit(e) {
        e.preventDefault();

        this.validate((e) => !e && this.props.onSubmit(this.state));
    }

    onCancel() {
        this.setState(this.state = this.initialState());
    }

    render () {
        return (
            <form onSubmit={this.onSubmit}>
                <div className={this.classesFor('name')}>
                    <label htmlFor="nameField">Name</label>
                    <input type="text" className="form-control" id="nameField" placeholder="Server Name..."
                        valueLink={this.linkState('name')} onBlur={this.handleValidation('name')}  />
                    {this.getValidationMessages('name').map(this.renderHelpText)}
                </div>

                <div className="form-group">
                    <label htmlFor="typeField">Type</label>
                    <select className="form-control" id="typeField" valueLink={this.linkState('type')}>
                        <option value="STANDARD">Standard</option>
                        <option value="HYPERSCALE">Hyperscale</option>
                    </select>
                </div>

                <GroupSelect form={this} />

                <div className={this.classesFor('cpu')}>
                    <label htmlFor="cpuField">CPU Count</label>
                    <input type="text" className="form-control" id="cpuField" placeholder="CPU..."
                           valueLink={this.linkState('cpu')} onBlur={this.handleValidation('cpu')} />
                    {this.getValidationMessages('cpu').map(this.renderHelpText)}
                </div>

                <div className={this.classesFor('ram')}>
                    <label htmlFor="ramField">RAM</label>
                    <input type="text" className="form-control" id="ramField" placeholder="RAM (GB)..."
                        valueLink={this.linkState('ram')} onBlur={this.handleValidation('ram')} />
                    {this.getValidationMessages('ram').map(this.renderHelpText)}
                </div>

                <TemplateSelect form={this} />

                <div className="pull-right">
                    <button type="submit" className="btn btn-primary">Submit</button>
                    <button type="button" style={{marginLeft: 3}} className="btn btn-default" onClick={this.onCancel}>
                        Cancel
                    </button>
                </div>
            </form>
        );
    }

    renderHelpText (message) {
        return (
            <span className="help-block">{message}</span>
        );
    }

    classesFor (field) {
        return React.addons.classSet({
            'form-group': true,
            'has-error': !this.isValid(field)
        });
    }

}

reactMixin(Form.prototype, React.addons.LinkedStateMixin);
reactMixin(Form.prototype, ValidationMixin);