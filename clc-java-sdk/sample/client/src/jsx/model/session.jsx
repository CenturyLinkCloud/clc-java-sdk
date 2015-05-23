
export default sessions = new Sessions();

class Sessions {

    constructor () {
        this.session = null;
    }

    get current () {
        return this.session;
    }

    createNew(username, password) {
        this.session = new Session(username, password);
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

