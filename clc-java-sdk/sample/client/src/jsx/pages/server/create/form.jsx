
import reactMixin from 'react-mixin';


export class Form extends React.Component {

    constructor (args) {
        super(args);

        this.state = {};

        _.bindAll(this, 'onSubmit', 'render', 'onCancel');
    }

    onSubmit(e) {
        e.preventDefault();

        this.props.onSubmit(this.state);
    }

    onCancel() {
        this.setState(this.state = { });
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
                    <label htmlFor="cpuField">CPU Count</label>
                    <input type="text" className="form-control" id="cpuField" placeholder="CPU..."
                        valueLink={this.linkState('cpu')} />
                </div>

                <div className="form-group">
                    <label htmlFor="ramField">RAM</label>
                    <input type="text" className="form-control" id="ramField" placeholder="RAM (GB)..."
                        valueLink={this.linkState('ram')} />
                </div>

                <button type="submit" className="btn btn-primary">Submit</button>
                <button type="button" style={{marginLeft: 3}} className="btn btn-default" onClick={this.onCancel}>
                    Cancel
                </button>
            </form>
        );
    }

}

reactMixin(Form.prototype, React.addons.LinkedStateMixin);