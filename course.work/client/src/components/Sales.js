import React, {Component} from "react";
import MaterialTable from "material-table";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import InputLabel from "@material-ui/core/InputLabel";
import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";

class Sales extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: true,
            data: [{id: null, goods: null, count: null, timestamp: null, amount: null}],
            goods: [{id : null, name: null, priority: null, left: null}],
            goodToShow: 0,
            goodId: "",
            columns: [
                {
                    title: 'Название товара', field: 'goods.name', editComponent: () => {

                        const handleChange = (e) => {
                            let good = this.state.goods.find((good) => {
                                return good.id === Number(e.target.value)
                            });
                            this.setState({goodToShow : good, goodId: e.target.value})
                        };

                        return (
                            <div>
                                <FormControl style={{minWidth: 120}}>
                                    <InputLabel id="demo-simple-select-label">Товар</InputLabel>
                                    <Select
                                        labelId="demo-simple-select-label"
                                        id="demo-simple-select"
                                        value={this.state.goodId}
                                        onChange={handleChange}
                                    >
                                        {this.state.goods.map(good =>
                                            <MenuItem value={good.id}>
                                                {good.name}
                                            </MenuItem>
                                        )}
                                    </Select>
                                </FormControl>
                            </div>
                        )
                    }
                },
                {title: 'Колчивество', field: 'count'},
                {title: 'Приоритет товара', field: 'goods.priority', editable: 'never'},
                {title: 'Дата составления заявки', field: 'timestamp', editable: 'never'},
                {title: 'Остаток на складе', field: 'amount', editable: 'onAdd', editComponent: props => {

                    return (
                        <TextField
                            disabled
                            value={this.state.goodToShow.left}
                        >

                        </TextField>
                    )
                    }},
            ],
        }
    }


    async componentDidMount() {

        const response_goods = await fetch('/api/goods/all');
        const body_goods = await response_goods.json();

        const response_sales = await fetch('/api/sales/all');
        const sales = await response_sales.json();
        // const goods = sales.map(sale => {
        //     return sale.goods
        // });
        const response_amounts = await fetch('/api/warehouse/left', {
            method: "POST",
            dataType: 'json',
            body: JSON.stringify(body_goods),
            headers: {
                "Content-Type": "application/json"
            }
        });
        const left = await response_amounts.json();

        sales.map((sale) => {
            body_goods.find((good, index) => {
               if (good.id === sale.goods.id) {
                   sale.amount = left[index].amount;
                   sale.timestamp = new Date(sale.timestamp).toLocaleDateString("ru-RU")
               }
            });
        });
        body_goods.map((good, index) => {
            good.left = left[index].amount
        });
        this.setState({data: sales, isLoading: false, goods: body_goods});
    }

    render() {

        if (this.state.isLoading) {
            return <p>Загрузка...</p>;
        }

        return (
            <div>
                <MaterialTable
                    title="Таблица заявок"
                    columns={this.state.columns}
                    data={this.state.data}
                    editable={{
                        onRowAdd: (newData) =>
                            new Promise((resolve) => {
                                setTimeout(async () => {
                                    resolve();

                                    // {
                                    //     "count" : 5,
                                    //     "timestamp" : "2020-05-01",
                                    //     "goods" : {
                                    //     "id": 2,
                                    //         "name": "товар_2",
                                    //         "priority": 2.0
                                    // }
                                    // }

                                    const body = {
                                        count: Number(newData.count),
                                        timestamp: new Date(),
                                        goods : this.state.goodToShow,
                                    };

                                    const response_sale = await fetch('/api/sales/add', {
                                        method: "POST",
                                        dataType: 'json',
                                        body: JSON.stringify(body),
                                        headers: {
                                            "Content-Type": "application/json"
                                        }
                                    });

                                    this.setState((prevState) => {
                                        const data = [...prevState.data];

                                        let row = {
                                            goods: this.state.goods.find(elem => {
                                                return elem.id === Number(this.state.goodId)
                                            }),
                                            count: Number(newData.count),
                                            timestamp: new Date().toLocaleDateString("ru-RU"),
                                            amount: this.state.goodToShow.left
                                        };
                                        data.push(row);

                                        return {...prevState, data};
                                    });
                                }, 600);

                            }),
                        onRowDelete: (oldData) =>
                            new Promise((resolve) => {
                                setTimeout(() => {
                                    resolve();

                                    this.setState((prevState) => {
                                        const data = [...prevState.data];
                                        data.splice(data.indexOf(oldData), 1)

                                        return {...prevState, data};
                                    });

                                }, 600);

                            }),
                    }}
                />
            </div>
        );
    }
}

export default Sales;