import React, {Component} from 'react';
import SignIn from "./components/Login";


class App extends Component {
    constructor(props) {
        super(props);
    }


    render() {

        return (
            <div>

                <SignIn/>

            </div>
        );
    }
}

export default App;
