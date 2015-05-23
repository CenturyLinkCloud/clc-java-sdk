
class Sessions {

    constructor () {
        this.session = null;
        this.listeners = [];
    }

    current () {
        return this.session;
    }

    addListener (type, listener) {
        type === 'onSessionUpdated' && this.listeners.push(listener);
    }

    fire

    createNew (username, password) {
        this.session = new Session(username, password);


    }

    logout () {
        this.session = null;
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

export default new Sessions();

