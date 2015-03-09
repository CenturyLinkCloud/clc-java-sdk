
var A = require('./pages/hello.jsx');

React.render(
    <h1>{new A().value}</h1>,
    document.getElementById('helloContainer')
);