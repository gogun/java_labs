import React, {Component} from 'react';
import Goods from "./Goods";


class Chooser extends Component {
    constructor(props) {
        super(props);
    }


    render() {
        const switchViews = (view) => {
            switch (view) {
                case 0 :
                    return <Goods/>
                default:
                    return null
            }
        };
        return (
            switchViews(this.props.view)
        )
    }
}

export default Chooser;