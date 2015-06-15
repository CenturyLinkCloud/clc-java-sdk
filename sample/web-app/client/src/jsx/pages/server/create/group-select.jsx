
import reactMixin from 'react-mixin';
import { GroupList } from './../../../model/group.jsx';


export default class GroupSelect extends React.Component {

    constructor (args) {
        super(args);

        this.form = this.props.form;
        this.state = this.props.form.state;
        this.state.groups = new GroupList([], { dataCenter: this.state.dataCenter });
        this.state.groups.fetch({ success: () => this.setState(this.state) });

        _.bindAll(this, 'render', 'validate');
    }

    setState (args) {
        this.props.form.state.group = args.group;
        super.setState(args);
        setTimeout(() => this.validate());
    }

    render () {
        return (
            <div className={this.form.classesFor('group')}>
                <label htmlFor="groupField">Group</label>
                <select className="form-control" id="groupField" valueLink={this.linkState('group')}
                        onBlur={this.validate()}>
                    <option value="">&lt;Select&gt;</option>
                    {this.state.groups.map((i) =>
                        <option value={i.get('id')}>{i.get('name')}</option>
                    )}
                </select>
                {this.form.getValidationMessages('group').map(this.form.renderHelpText)}
            </div>
        );
    }

    validate () {
        return this.form.handleValidation('group');
    }

}

reactMixin(GroupSelect.prototype, React.addons.LinkedStateMixin);