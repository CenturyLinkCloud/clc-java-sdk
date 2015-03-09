
var Router = ReactRouter;
var { Route, RouteHandler, DefaultRoute } = Router;
import { DashboardPage } from './pages/dashboard.jsx';


class App extends React.Component {

    constructor (props) {
        super(props);
        this.state = { loading: true };
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
                            <a className="navbar-brand" href="#">Project name</a>
                        </div>
                        <div id="navbar" className="collapse navbar-collapse">
                            <ul className="nav navbar-nav">
                                <li className="active"><a href="#">Home</a></li>
                                <li><a href="#about">About</a></li>
                                <li><a href="#contact">Contact</a></li>
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


var routes = (
    <Route handler={App} path="/">
        <Route name="dashboard" path="dashboard" handler={DashboardPage} />
        <DefaultRoute handler={DashboardPage}/>
    </Route>
);

Router.run(routes, Router.HistoryLocation, function (Handler) {
    React.render(<Handler />, document.getElementById('content'));
});