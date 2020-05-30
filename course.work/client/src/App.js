import React, {Component} from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Paper from '@material-ui/core/Paper';
import Chooser from "./components/Chooser";



class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            value : 1
        }
    }


    render() {

        const handleChange = (event, newValue) => {
            this.setState({value:newValue})
        };
        const {value} = this.state;

        return (
            <div>
                <Paper square>
                    <Tabs
                        value={value}
                        indicatorColor="primary"
                        textColor="primary"
                        onChange={handleChange}
                        aria-label="disabled tabs example"
                    >
                        <Tab label="Товары" />
                        <Tab label="Заявки" />
                    </Tabs>
                </Paper>
                <Chooser view = {value}/>

            </div>
        );
    }
}

export default App;
