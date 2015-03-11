
import reactMixin from 'react-mixin';
import { GroupList } from './../../../model/group.jsx';


export default class GroupSelect extends React.Component {

    constructor (args) {
        super(args);

        this.state = this.props.model;
        this.state.groups = new GroupList([], { dataCenter: this.state.dataCenter });
        this.state.groups.fetch({ success: () => this.setState(this.state) });

        _.bindAll(this, 'render');
    }

    setState (args) {
        this.props.model.group = args && args.group;
        super.setState(args);
    }

    render () {
        return (
            <div className="form-group">
                <label htmlFor="groupField">Group</label>
                <select className="form-control" id="groupField" valueLink={this.linkState('group')}>
                    <option>&lt;Select&gt;</option>
                    {this.state.groups.map((i) =>
                        <option value={i.get('id')}>{i.get('name')}</option>
                    )}
                </select>
            </div>
        );
    }

}

reactMixin(GroupSelect.prototype, React.addons.LinkedStateMixin);