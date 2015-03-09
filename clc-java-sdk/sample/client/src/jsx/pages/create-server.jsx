import reactMixin from 'react-mixin';
let Router = ReactRouter;

export class CreateServerPage extends React.Component {

    params() {
        return this._reactInternalInstance._context.getCurrentParams();
    }

    render () {
        return (
            <h1>{this.params().dataCenter}</h1>
        );
    }

}

