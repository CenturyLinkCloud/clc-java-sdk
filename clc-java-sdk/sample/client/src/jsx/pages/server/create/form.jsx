
import reactMixin from 'react-mixin';
import GroupSelect from './group-select.jsx';
import TemplateSelect from './template-select.jsx';


export class Form extends React.Component {

    constructor (args) {
        super(args);

        this.state = this.initialState();

        _.bindAll(this, 'onSubmit', 'render', 'onCancel');
    }

    initialState(dataCenter) {
        return { type: 'STANDARD', dataCenter: this.props.dataCenter };
    }

    onSubmit(e) {
        e.preventDefault();

        this.props.onSubmit(this.state);
    }

    onCancel() {
        this.setState(this.state = this.initialState());
    }

    render () {
        return (
            <form onSubmit={this.onSubmit}>
                <div className="form-group">
                    <label htmlFor="nameField">Name</label>
                    <input type="text" className="form-control" id="nameField" placeholder="Server Name..."
                        valueLink={this.linkState('name')} />
                </div>

                <div className="form-group">
                    <label htmlFor="typeField">Type</label>
                    <select className="form-control" id="typeField" valueLink={this.linkState('type')}>
                        <option value="STANDARD">Standard</option>
                        <option value="HYPERSCALE">Hyperscale</option>
                    </select>
                </div>

                <GroupSelect model={this.state} />

                <div className="form-group">
                    <label htmlFor="cpuField">CPU Count</label>
                    <input type="text" className="form-control" id="cpuField" placeholder="CPU..."
                        valueLink={this.linkState('cpu')} />
                </div>

                <div className="form-group">
                    <label htmlFor="ramField">RAM</label>
                    <input type="text" className="form-control" id="ramField" placeholder="RAM (GB)..."
                        valueLink={this.linkState('ram')} />
                </div>

                <TemplateSelect model={this.state} />

                <div className="pull-right">
                    <button type="submit" className="btn btn-primary">Submit</button>
                    <button type="button" style={{marginLeft: 3}} className="btn btn-default" onClick={this.onCancel}>
                        Cancel
                    </button>
                </div>
            </form>
        );
    }

}

reactMixin(Form.prototype, React.addons.LinkedStateMixin);