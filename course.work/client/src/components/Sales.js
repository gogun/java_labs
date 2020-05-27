import React, {Component} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Paper from "@material-ui/core/Paper";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import Typography from "@material-ui/core/Typography";

class Sales extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: true,
            goods: [],
            left: []
        };
    }


    async componentDidMount() {
        const response_goods = await fetch('/api/goods/all');
        const body = await response_goods.json();

        const response_amounts = await fetch('/api/warehouse/left', {
            method: "POST",
            dataType: 'json',
            body: JSON.stringify(body),
            headers: {
                "Content-Type": "application/json"
            }
        });
        const list = await response_amounts.json();
        this.setState({goods: body, isLoading: false, left: list});
    }

    render() {
        const {goods, isLoading, left} = this.state;

        if (isLoading) {
            return <p>Загрузка...</p>;
        }

        return (

            <div className="App">
                <Typography variant="h3" id="tableTitle" component="div">
                    Таблица товаров
                </Typography>
                <TableContainer component={Paper}>
                    <Table aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Название товара</TableCell>
                                <TableCell align="right">Приоритет</TableCell>
                                <TableCell align="right">Количество на складе</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {goods.map((goods, index) => (
                                <TableRow key={goods.id}>
                                    <TableCell component="th" scope="row">
                                        {goods.name}
                                    </TableCell>
                                    <TableCell align="right">{this.state.goods[index].priority}</TableCell>
                                    <TableCell align="right">{left[index]}</TableCell>
                                </TableRow>
                            ))}

                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        );
    }
}

export default Sales;