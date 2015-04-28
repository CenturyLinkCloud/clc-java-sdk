
import reactMixin from 'react-mixin';
let { Router, Navigation } = ReactRouter;
import { Group, GroupList } from './../../../model/group.jsx';
import { Server, ServerList } from './../../../model/server.jsx';
import ServerRows from './server-rows.jsx';


export class ServerListPage extends React.Component {

    constructor (args) {
        super(args);

        _.bindAll(this, 'render', 'getContext', 'transitionToRoute');
    }

    componentWillMount() {
        this.state = {};

        var dataCenter = this.params().dataCenter;

        this.state.groups = new GroupList([], { dataCenter: dataCenter });
        this.state.groups.fetch({ success: () => this.setState(this.state) });

        this.state.servers = new ServerList([], {dataCenter: dataCenter });
        this.state.servers.fetch({ success: () => this.setState(this.state) });
    }

    getInitialState() {
        return {groups: [], servers:[]};
    }

    getContext() {
        return this._reactInternalInstance._context;
    }

    params() {
        return this.getContext().getCurrentParams();
    }

    transitionToRoute (to, params, query) {
        this.getContext().transitionTo(to, params, query);
    }

    render () {
        return (
            <div className="starter-template">
                <h2>Servers in Datacenter {this.params().dataCenter}</h2>
                <table className="table">
                    <thead>
                        <tr>
                            <th>Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.groups.map(item =>
                            <tr>
                                <td key={item.get('id')}>{item.get('name')}
                                    <ServerRows groupId={item.get('id')} servers={this.state.servers}/>
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        );
    }

}

reactMixin(ServerListPage.prototype, Navigation);

