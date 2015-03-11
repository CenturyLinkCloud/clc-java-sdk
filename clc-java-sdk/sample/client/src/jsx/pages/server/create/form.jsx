
import reactMixin from 'react-mixin';


export class Form extends React.Component {

    constructor (args) {
        super(args);

        this.state = Form.initialState();

        _.bindAll(this, 'onSubmit', 'render', 'onCancel');
    }

    static initialState() {
        return { type: 'standard' };
    }

    onSubmit(e) {
        e.preventDefault();

        this.props.onSubmit(this.state);
    }

    onCancel() {
        this.setState(this.state = Form.initialState());
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
                    <select className="form-control" id="typeField" placeholder="Instance Type..."
                        valueLink={this.linkState('type')}>
                        <option value="standard">Standard</option>
                        <option value="hyperscale">Hyperscale</option>
                    </select>
                </div>

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