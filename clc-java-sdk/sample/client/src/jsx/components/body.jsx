let { RouteHandler } = ReactRouter;
import reactMixin from 'react-mixin';
import sessions from './../model/session.jsx';


export class Body extends React.Component {

    constructor (props) {
        super(props);
        this.state = { loading: true };
        this.state.sessions = sessions;
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
                        <div id="navbar" className="collapse navbar-collapse">
                            <ul className="nav navbar-nav">
                                <li className="active"><a href="#">Home</a></li>
                                <li><a href="#about">About</a></li>
                            </ul>
                            <ul className="nav navbar-nav navbar-right">
                                <li><a href="#about">{ this.user ? 'Logout' : 'Login' }</a></li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <div className="container">
                    <RouteHandler />
                </div>
            </div>
        );
    }

}
