
import reactMixin from 'react-mixin';


export class Form extends React.Component {

    constructor (args) {
        super(args);

        this.state = {};
    }

    render () {
        return (
            <form onSubmit={this.props.onSubmit}>
                <div className="form-group">
                    <label for="nameField">Name</label>
                    <input type="email" className="form-control" id="nameField" placeholder="Server Name..."
                        valueLink={this.linkState('name')} />
                </div>
            </form>
        );
    }

}

reactMixin(Form.prototype, React.addons.LinkedStateMixin);