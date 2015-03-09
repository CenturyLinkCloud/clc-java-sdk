let Router = ReactRouter;
let { Route, DefaultRoute } = Router;
import { DashboardPage } from './pages/dashboard.jsx';
import { CreateServerPage } from './pages/create-server.jsx';
import { Body } from './components/body.jsx';


class App {

    get routes () {
        return (
            <Route handler={Body} path="/">
                <DefaultRoute handler={DashboardPage} />
                <Route name="dashboard" path="/dashboard" handler={DashboardPage} />
                <Route name="createServer" path="/:dataCenter/server/new" handler={CreateServerPage} />
            </Route>
        );
    }

    run() {
        Router.run(this.routes, App.renderApp);
    }

    static renderApp (Handler) {
        React.render(<Handler />, document.getElementById('content'));
    }

}

new App().run();


