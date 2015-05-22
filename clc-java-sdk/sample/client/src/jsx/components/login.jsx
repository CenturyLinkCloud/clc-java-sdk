


export class LoginPage extends React.Component {

    constructor() {

    }

    onStateChanged() {
        this.setState(this.state);
    }

    render () {
        return (
            <div className="panel panel-default">
                <div className="panel-heading">Login</div>

                <div className="panel-body">
                    <div className="row">
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
                        </div>
                    </div>
                </div>
            </div>
        );
    }

}
