
import reactMixin from 'react-mixin';
import { TemplateList } from './../../../model/template.jsx';


export default class TemplateSelect extends React.Component {

    constructor (args) {
        super(args);

        this.form = this.props.form;
        this.state = this.props.form.state;
        this.state.templates = new TemplateList([], { dataCenter: this.state.dataCenter });
        this.state.templates.fetch({ success: () => this.setState(this.state) });

        _.bindAll(this, 'render');
    }

    setState (args) {
        this.props.form.state.template = args && args.template;
        super.setState(args);
    }

    render () {
        return (
            <div className={this.form.classesFor('template')}>
                <label htmlFor="templateField">Template</label>
                <select className="form-control" id="templateField" valueLink={this.linkState('template')}
                        onBlur={this.form.handleValidation('template')}>
                    <option>&lt;Select&gt;</option>
                    {this.state.templates.map((i) =>
                        <option value={i.get('name')}>{i.get('description')}</option>
                    )}
                </select>
                {this.form.getValidationMessages('template').map(this.form.renderHelpText)}
            </div>
        );
    }

}

reactMixin(TemplateSelect.prototype, React.addons.LinkedStateMixin);