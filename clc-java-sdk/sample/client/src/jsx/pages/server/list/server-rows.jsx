
import reactMixin from 'react-mixin';


export default class ServerRows extends React.Component {

    constructor (args) {
        super(args);

        this.state = {
            groupId: this.props.groupId,
            servers: []
        }

        _.bindAll(this, 'render');
    }

    componentWillReceiveProps(args) {
        var groupId = this.state.groupId;
        this.state.servers = args && args.servers && args.servers.filter(function(s) {return s.get('groupId') === groupId})
    }

    render () {
        return (
            <ul>
            {this.state.servers.map(item =>
                <li key={item.get('id')}>{item.get('name')}</li>
            )}
            </ul>
        );
    }

}

reactMixin(ServerRows.prototype, React.addons.LinkedStateMixin);