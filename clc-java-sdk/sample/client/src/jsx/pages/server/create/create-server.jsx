
let Router = ReactRouter;
import { Form } from "./form.jsx";

export class CreateServerPage extends React.Component {

    constructor (args) {
        super(args);

        _.bindAll(this, 'render', 'submitForm');
    }

    params() {
        return this._reactInternalInstance._context.getCurrentParams();
    }

    submitForm(model) {
        console.log('Submitting Form...');
        console.dir(model);
    }

    render () {
        return (
            <div className="panel panel-default">
                <div className="panel-heading">Create Server</div>

                <div className="panel-body">
                    <div className="row">
                        <div className="col-md-6 col-md-offset-3">
                            <Form onSubmit={this.submitForm} />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

}

