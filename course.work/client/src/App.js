import React, {Component} from 'react';
import SignIn from "./components/Login";
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Chooser from "./components/adminView/Chooser";
import ChooserUser from "./components/userView/ChooserUser";

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
                        <Route path='/admin' exact component={Chooser}/>
                        <Route path='/main' exact component={ChooserUser}/>
                    </Switch>
                </Router>

            </div>
        );
    }
}

export default App;
