import reactMixin from 'react-mixin';
import ValidationMixin from 'react-validation-mixin';
import Joi from 'joi';

export class LoginPage extends React.Component {

    constructor (args) {
        super(args);

        this.state = { username: null, password: null };
        _.bindAll(this, 'render', 'classesFor');
    }

    get validatorTypes () {
        return {
            username: Joi.string().required().label('Username'),
            password: Joi.string().required().label('Password')
        };
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

    render () {
        return (
            <div className="col-md-6 col-md-offset-3">
                <div className="panel panel-default">
                    <div className="panel-heading">Login</div>

                    <div className="panel-body">
                        <form className="form-signin">
                            <div className="col-md-6 col-md-offset-3">
                                <div className={this.classesFor('name')}>
                                    <label htmlFor="usernameField">Username</label>
                                    <input type="text" className="form-control" id="usernameField" placeholder="Username..."
                                        valueLink={this.linkState('username')} onBlur={this.handleValidation('username')} />
                                    {this.getValidationMessages('username').map(this.renderHelpText)}
                                </div>

                                <div className={this.classesFor('name')}>
                                    <label htmlFor="passwordField">Password</label>
                                    <input type="password" className="form-control" id="passwordField" placeholder="Password..."
                                        valueLink={this.linkState('password')} onBlur={this.handleValidation('password')} />
                                    {this.getValidationMessages('password').map(this.renderHelpText)}
                                </div>

                                <button className="btn btn-lg btn-primary btn-block" type="submit">
                                    Login
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    }

}

reactMixin(LoginPage.prototype, React.addons.LinkedStateMixin);
reactMixin(LoginPage.prototype, ValidationMixin);
