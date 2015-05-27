
class Sessions {

    constructor () {
        this.session = null;
        this.listeners = [];
    }

    current () {
        return this.session;
    }

    isEmpty () {
        return this.session != null;
    }

    addListener (type, listener) {
        type === 'onSessionUpdated' && this.listeners.push(listener);
    }

    fireSessionChanges () {
        this.listeners.forEach(l => l(this.session));
    }

    createNew (username, password) {
        this.session = new Session(username, password);
        this.fireSessionChanges();
    }

    logout () {
        this.session = null;
        this.fireSessionChanges();
    }
}

class Session {

    constructor (username = null, password = null) {
        this.username = username;
        this.password = password;
    }

    isEmpty () {
        return !(this.username && this.password);
    }

}

let sessions = new Sessions();
export default sessions;


(function setAuthHeaders() {
    let sync = Backbone.sync;
    Backbone.sync = function (method, model, options) {
        options.beforeSend = function (xhr) {
            let curSession = sessions.current();

            if (curSession != null) {
                xhr.setRequestHeader('X-Clc-Username', curSession.username);
                xhr.setRequestHeader('X-Clc-Password', curSession.password);
            }
        };

      // Update other options here.

      sync(method, model, options);
    };
})();
