import reactMixin from 'react-mixin';
import ValidationMixin from 'react-validation-mixin';
import Joi from 'joi';
import sessions from './../model/session.jsx';
let { Navigation } = ReactRouter;

export class LoginPage extends React.Component {

    constructor (args) {
        super(args);

        sessions.logout();
        this.state = { username: null, password: null };
        _.bindAll(this, 'render', 'classesFor', 'onSubmit', 'getContext', 'transitionToRoute');
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

    onSubmit () {
        this.validate((err) => !err && this.createSession());
    }

    createSession () {
        sessions.createNew(this.state.username, this.state.password);
        this.transitionToRoute('dashboard');
    }

    getContext() {
        return this._reactInternalInstance._context;
    }

    transitionToRoute (to, params, query) {
        this.getContext().transitionTo(to, params, query);
    }

    render () {
        return (
            <div className="col-md-6 col-md-offset-3">
                <div className="panel panel-default">
                    <div className="panel-heading">Login</div>

                    <div className="panel-body">
                        <form className="form-signin" onSubmit={this.onSubmit}>
                            <div className="col-md-6 col-md-offset-3">
                                <div className={this.classesFor('username')}>
                                    <label htmlFor="usernameField">Username</label>
                                    <input type="text" className="form-control" id="usernameField" placeholder="Username..."
                                        valueLink={this.linkState('username')} onBlur={this.handleValidation('username')} />
                                    {this.getValidationMessages('username').map(this.renderHelpText)}
                                </div>

                                <div className={this.classesFor('password')}>
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
