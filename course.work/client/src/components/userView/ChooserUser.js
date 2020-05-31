import React, {Component} from 'react';
import GoodsUser from "./GoodsUser";
import SalesUser from "./SalesUser";
import Paper from "@material-ui/core/Paper";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import Cookies from "universal-cookie";
import Button from "@material-ui/core/Button";
import Redirect from "react-router-dom/es/Redirect";


const _ = require('lodash');

class ChooserUser extends Component {
    constructor(props) {
        super(props);
        this.cookies = new Cookies();

        this.state = {
            isRedirect: false,
            value: 0
        }
    }

    render() {
        if (_.isEmpty(this.cookies.getAll())
            // || this.cookies.get('role') !== "ROLE_ADMIN"
        ) {
            return <Redirect to='/'/>
        }

        // if (this.cookies.get('role') !== "ROLE_ADMIN") {
        //     return <Redirect to='/main'/>
        // }


        const switchViews = (view) => {
            switch (view) {
                case 0 :
                    return <GoodsUser token={this.cookies.get('token')}/>;
                case 1:
                    return <SalesUser token={this.cookies.get('token')}/>;
                default:
                    return null
            }
        };

        const handleChange = (event, newValue) => {
            this.setState({value: newValue})
        };

        const handleSignOut = () => {

            this.cookies.remove('token');
            this.cookies.remove('role');
            this.cookies.remove('remember');
            this.cookies.remove('user');
            this.setState({isRedirect : true})
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
                        <Tab label="Товары"/>
                        <Tab label="Заявки"/>
                        <Button onClick={handleSignOut}>
                            Выйти
                        </Button>
                    </Tabs>

                </Paper>
                {switchViews(value)}

            </div>
        )
    }
}

export default ChooserUser;