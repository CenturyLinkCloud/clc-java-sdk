
import reactMixin from 'react-mixin';


export class Form extends React.Component {

    constructor (args) {
        super(args);

        this.state = {};

        _.bindAll(this, 'onSubmit', 'render');
    }

    onSubmit(e) {
        e.preventDefault();

        this.props.onSubmit(this.state);
    }

    render () {
        return (
            <form onSubmit={this.onSubmit}>
                <div className="form-group">
                    <label for="nameField">Name</label>
                    <input type="text" className="form-control" id="nameField" placeholder="Server Name..."
                        valueLink={this.linkState('name')} />
                </div>

                <button type="submit" className="btn btn-default">Submit</button>
            </form>
        );
    }

}

reactMixin(Form.prototype, React.addons.LinkedStateMixin);