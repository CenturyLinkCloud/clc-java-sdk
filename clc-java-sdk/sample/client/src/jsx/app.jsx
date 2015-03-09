
let Router = ReactRouter;
let { Route, DefaultRoute } = Router;
import { DashboardPage } from './pages/dashboard.jsx';
import { Body } from './components/body.jsx';

const routes = (
    <Route handler={Body} path="/">
        <Route name="dashboard" path="/dashboard" handler={DashboardPage} />
    </Route>
);

Router.run(routes, function (Handler) {
    React.render(<Handler />, document.getElementById('content'));
});