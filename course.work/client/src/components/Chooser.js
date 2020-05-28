import React, {Component} from 'react';
import Goods from "./Goods";
import Sales from "./Sales";


class Chooser extends Component {
    render() {
        const switchViews = (view) => {
            switch (view) {
                case 0 :
                    return <Goods/>;
                case 1:
                    return <Sales/>
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