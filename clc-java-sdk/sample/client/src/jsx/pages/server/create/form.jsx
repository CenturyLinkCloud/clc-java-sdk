
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
            name: Joi.string().required().max(6).label('Name')
//        lastName: Joi.string().allow(null).label('Last Name'),
//        email: Joi.string().email().label('Email Address'),
//        username:  Joi.string().alphanum().min(3).max(30).required().label('Username'),
//        password: Joi.string().regex(/[a-zA-Z0-9]{3,30}/).label('Password'),
//        verifyPassword: Joi.any().valid(Joi.ref('password')).required().label('Password Confirmation')
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

                <GroupSelect model={this.state} />

                <div className="form-group">
                    <label htmlFor="cpuField">CPU Count</label>
                    <input type="text" className="form-control" id="cpuField" placeholder="CPU..."
                        valueLink={this.linkState('cpu')} />
                </div>

                <div className="form-group">
                    <label htmlFor="ramField">RAM</label>
                    <input type="text" className="form-control" id="ramField" placeholder="RAM (GB)..."
                        valueLink={this.linkState('ram')} />
                </div>

                <TemplateSelect model={this.state} />

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