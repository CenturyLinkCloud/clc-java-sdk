
import reactMixin from 'react-mixin';
let { Router, Navigation } = ReactRouter;
import { Group, GroupList } from './../../model/group.jsx';


export class GroupListPage extends React.Component {

    constructor (args) {
        super(args);

        _.bindAll(this, 'render', 'getContext', 'transitionToRoute');
    }

    componentWillMount() {
        this.state = {};

        this.state.groups = new GroupList([], { dataCenter: this.params().dataCenter });
        this.state.groups.fetch({ success: () => this.setState(this.state) });
    }

    getInitialState() {
        return {groups: []};
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
            <h2>Groups in Datacenter {this.params().dataCenter}</h2>
                <table className="table">
                    <thead>
                        <tr>
                            <th>Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.groups.map(item =>
                        <tr>
                            <td>{item.get('name')}</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        );
    }

}

reactMixin(GroupListPage.prototype, Navigation);

