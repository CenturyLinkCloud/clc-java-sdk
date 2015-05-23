let { RouteHandler, Navigation } = ReactRouter;
import reactMixin from 'react-mixin';
import sessions from './../model/session.jsx';


export class Body extends React.Component {

    constructor (props) {
        super(props);
        _.bindAll(this, 'render', 'whenUserLoggedIn', 'getContext', 'transitionToRoute');

        this.state = { loading: true, curSession: sessions.current() };
        sessions.addListener('onSessionUpdated', () => this.updateSession());

        if (this.state.curSession === null) {
            setTimeout(() => this.transitionToRoute('login'));
        }
    }

    updateSession () {
        this.state.curSession = sessions.current();
        this.setState(this.state);
    }

    whenUserLoggedIn (func) {
        return this.state.curSession && func();
    }

    getContext() {
        return this._reactInternalInstance._context;
    }

    transitionToRoute (to, params, query) {
        this.getContext().transitionTo(to, params, query);
    }

    render () {
        return (
            <div>
                <nav className="navbar navbar-inverse navbar-fixed-top">
                    <div className="container">
                        <div className="navbar-header">
                            <button type="button" className="navbar-toggle collapsed" data-toggle="collapse"
                                data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                                <span className="sr-only">Toggle navigation</span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                            </button>
                            <a className="navbar-brand" href="#">SDK Sample</a>
                        </div>

                        {this.whenUserLoggedIn(() =>
                            <div id="navbar" className="collapse navbar-collapse">
                                <ul className="nav navbar-nav">
                                    <li className="active"><a href="#/dashboard">Dashboard</a></li>
                                </ul>

                                <ul className="nav navbar-nav navbar-right">
                                    <li><a href="#/login">Logout</a></li>
                                </ul>
                            </div>
                        )}
                    </div>
                </nav>

                <div className="container">
                    <RouteHandler />
                </div>
            </div>
        );
    }

}

reactMixin(Body.prototype, Navigation);
