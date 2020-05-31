import React, {Component} from 'react';
import Goods from "./Goods";
import Sales from "./Sales";
import LoginTab from "./Login";
import Paper from "@material-ui/core/Paper";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";


class Chooser extends Component {
    constructor(props) {
        super(props);
        this.state = {
            value : 0
        }
    }

    render() {
        const switchViews = (view) => {
            switch (view) {
                case 0 :
                    return <Goods/>;
                case 1:
                    return <Sales/>;
                default:
                    return null
            }
        };

        const handleChange = (event, newValue) => {
            this.setState({value:newValue})
        };

        const {value} = this.state;

        return (
            // switchViews(this.props.view)
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
                {switchViews(value)}

            </div>
        )
    }
}

export default Chooser;