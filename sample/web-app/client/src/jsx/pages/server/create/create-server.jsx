
import reactMixin from 'react-mixin';
let { Router, Navigation } = ReactRouter;
import { Form } from "./form.jsx";
import { Server } from './../../../model/server.jsx';


export class CreateServerPage extends React.Component {

    constructor (args) {
        super(args);

        _.bindAll(this, 'render', 'submitForm', 'getContext', 'transitionToRoute');
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

    submitForm(model) {
        new Server(model).save();
        this.transitionToRoute('dashboard');
    }

    render () {
        return (
            <div className="panel panel-default">
                <div className="panel-heading">Create Server</div>

                <div className="panel-body">
                    <div className="row">
                        <div className="col-md-6 col-md-offset-3">
                            <Form onSubmit={this.submitForm} dataCenter={this.params().dataCenter} />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

}

reactMixin(CreateServerPage.prototype, Navigation);

