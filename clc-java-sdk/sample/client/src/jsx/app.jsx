let Router = ReactRouter;
let { Route, DefaultRoute } = Router;
import { DashboardPage } from './pages/dashboard.jsx';
import { LoginPage } from './pages/login.jsx';
import { CreateServerPage } from './pages/server/create/create-server.jsx';
import { ServerListPage } from './pages/server/list/server-list.jsx';
import { Body } from './components/body.jsx';


class App {

    get routes () {
        return (
            <Route handler={Body} path="/">
                <DefaultRoute handler={DashboardPage} />
                <Route name="dashboard" path="/dashboard" handler={DashboardPage} />
                <Route name="createServerInDC" path="/:dataCenter/server/new" handler={CreateServerPage} />
                <Route name="serverList" path="/:dataCenter/server/" handler={ServerListPage} />
                <Route name="login" path="/login" handler={LoginPage} />
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


