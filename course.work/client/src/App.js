import React, {Component} from 'react';
import SignIn from "./components/Login";
import { BrowserRouter as Router, Route, Switch, PrivateRoute } from 'react-router-dom';
import Chooser from "./components/Chooser";

class App extends Component {
    constructor(props) {
        super(props);
    }


    render() {

        return (
            <div>
                <Router>
                    <Switch>
                        <Route path='/' exact component={SignIn}/>
                        <Route path='/main' exact component={Chooser}/>
                    </Switch>
                </Router>

            </div>
        );
    }
}

export default App;
