
import reactMixin from 'react-mixin';
import { TemplateList } from './../../../model/template.jsx';


export default class TemplateSelect extends React.Component {

    constructor (args) {
        super(args);

        this.state = this.props.model;
        this.state.templates = new TemplateList([], { dataCenter: this.state.dataCenter });
        this.state.templates.fetch({ success: () => this.setState(this.state) });

        _.bindAll(this, 'render');
    }

    render () {
        return (
            <div className="form-group">
                <label htmlFor="templateField">Template</label>
                <select className="form-control" id="templateField" valueLink={this.linkState('template')}>
                    <option>&lt;Select&gt;</option>
                    {this.state.templates.map((i) =>
                        <option value={i.get('name')}>{i.get('description')}</option>
                    )}
                </select>
            </div>
        );
    }

}

reactMixin(TemplateSelect.prototype, React.addons.LinkedStateMixin);