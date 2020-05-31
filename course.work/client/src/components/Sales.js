import React, {Component} from "react";
import MaterialTable from "material-table";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import InputLabel from "@material-ui/core/InputLabel";
import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";
import Alert from "@material-ui/lab/Alert";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";
import AlertTitle from "@material-ui/lab/AlertTitle";
import Collapse from "@material-ui/core/Collapse";
import {Done} from "@material-ui/icons";

const validate = {
    count: (s, g) => {
        if (g === undefined) {
            return "Выберите товар"
        } else {
            if (!isNaN(parseInt(s)) && parseInt(s) <= g && parseInt(s) > 0) {
                return ""
            } else {
                return "Выбрано некорректное количество"
            }
        }

    },
};

class Sales extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isLoading: true,
            data: [{id: null, goods: null, count: null, timestamp: null, amount: null}],
            goods: [{id: null, name: null, priority: null, left: null, uuid: null}],
            goodToShow: 0,
            showAcceptingAlert: false,
            goodId: "",
            count: "",
            errorInCount: "",
            columns: [
                {
                    title: 'Название товара', field: 'goods.name', editComponent: () => {

                        const handleChange = (e) => {
                            let good = this.state.goods.find((good) => {
                                return good.id === Number(e.target.value)
                            });
                            this.setState({goodToShow: good, goodId: e.target.value})
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
                {
                    title: 'Количество', field: 'count', editComponent: props => {
                        const handleChange = (e) => {
                            let newValue = e.target.value;
                            this.setState({count: e.target.value});
                            const errorMessage = validate[props.columnDef.field](newValue
                                , this.state.goodToShow.left);
                            this.setState({errorInCount: errorMessage})
                        };
                        return (
                            <TextField
                                placeholder="Количество"
                                error={this.state.errorInCount}
                                onChange={handleChange}
                                helperText={this.state.errorInCount}
                                value={this.state.count}
                            >
                            </TextField>
                        )
                    }
                },
                {title: 'Приоритет товара', field: 'goods.priority', editable: 'never'},
                {title: 'Дата составления заявки', field: 'timestamp', editable: 'never'},
                {
                    title: 'Остаток на складе', field: 'amount', editable: 'onAdd', editComponent: props => {

                        return (
                            <TextField
                                disabled
                                value={this.state.goodToShow.left}
                            />
                        )
                    }
                },
            ],
        }
    }


    async componentDidMount() {

        const response_goods = await fetch('/api/goods/all', {
            headers:{
                "Authorization": "Bearer " + this.props.token
            }
        });
        const body_goods = await response_goods.json();

        const response_sales = await fetch('/api/sales/all', {
            headers:{
                "Authorization": "Bearer " + this.props.token
            }
        });
        const sales = await response_sales.json();

        const response_amounts = await fetch('/api/warehouse/left', {
            method: "POST",
            dataType: 'json',
            body: JSON.stringify(body_goods),
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + this.props.token
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
            good.left = left[index].amount;
            good.uuid = left[index].uuid;
        });
        sales.sort((a, b) => b.goods.priority - a.goods.priority);
        this.setState({data: sales, isLoading: false, goods: body_goods});
    }

    validateAccepting = (row, data) => {
        return row.count <= data.left;
    };

    render() {

        if (this.state.isLoading) {
            return <p>Загрузка...</p>;
        }

        return (
            <div>
                <Collapse in={this.state.showAcceptingAlert}>
                    <Alert
                        severity="warning"
                        action={
                            <IconButton
                                aria-label="close"
                                color="inherit"
                                size="small"

                                onClick={() => {
                                    this.setState({showAcceptingAlert: false});
                                }}
                            >

                                <CloseIcon fontSize="inherit"/>
                            </IconButton>
                        }
                    >
                        <AlertTitle>Предупреждение</AlertTitle>
                        Товара не хватает на складе
                    </Alert>
                </Collapse>
                <MaterialTable
                    localization={{
                        pagination: {
                            labelDisplayedRows: '{from}-{to} из {count}',
                            labelRowsSelect: 'строк'
                        },
                        toolbar: {
                            nRowsSelected: '{0} строк выбрано',
                            searchTooltip: 'Поиск',
                            searchPlaceholder: 'Поиск'
                        },
                        header: {
                            actions: 'Действия'
                        },
                        body: {
                            emptyDataSourceMessage: 'Нету записей',
                            filterRow: {
                                filterTooltip: 'Фильтр'
                            },
                            editRow: {
                                deleteText: 'Вы уверены, что хотите подтвердить заказ?',
                                cancelTooltip: 'Отменить',
                                saveTooltip: 'Подтвердить'
                            },
                            addTooltip: 'Добавить',
                            deleteTooltip: 'Подтвердить',
                            editTooltip: 'Редактировать'
                        }
                    }}
                    title="Таблица заявок"
                    icons={{
                        Delete: props => <Done {...props} />
                    }}
                    columns={this.state.columns}
                    data={this.state.data}
                    editable={{
                        onRowAdd: (newData) =>
                            new Promise((resolve, reject) => {
                                setTimeout(async () => {
                                    if (validate['count'](this.state.count, this.state.goodToShow.left)
                                        !== "") {
                                        reject();
                                        return;
                                    }
                                    resolve();

                                    const body = {
                                        count: this.state.count,
                                        timestamp: new Date(),
                                        goods: this.state.goodToShow,
                                    };

                                    await fetch('/api/sales/add', {
                                        method: "POST",
                                        dataType: 'json',
                                        body: JSON.stringify(body),
                                        headers: {
                                            "Content-Type": "application/json",
                                            "Authorization": "Bearer " + this.props.token
                                        }
                                    });

                                    this.setState((prevState) => {
                                        const data = [...prevState.data];

                                        let row = {
                                            goods: this.state.goods.find(elem => {
                                                return elem.id === Number(this.state.goodId)
                                            }),
                                            count: this.state.count,
                                            timestamp: new Date().toLocaleDateString("ru-RU"),
                                            amount: this.state.goodToShow.left
                                        };
                                        data.push(row);

                                        return {...prevState, data};
                                    });
                                    this.componentDidMount();
                                }, 600);

                            }),
                        onRowDelete: (oldData) =>
                            new Promise((resolve, reject) => {
                                setTimeout(async () => {
                                    const row = this.state.data[this.state.data.indexOf(oldData)];
                                    const dataLeft = this.state.goods.find((good) => {
                                        return good.id === row.goods.id
                                    });
                                    if (!this.validateAccepting(row, dataLeft)) {
                                        this.setState({showAcceptingAlert: true});
                                        reject();
                                        return;
                                    }

                                    resolve();

                                    await fetch('/api/sales/delete/' + row.id, {
                                        method: "DELETE",
                                        headers: {
                                            "Authorization": "Bearer " + this.props.token
                                        }
                                    });

                                    await fetch('/api/warehouse/update/' +
                                        dataLeft.uuid + '/?count='
                                        + row.count, {
                                        method: "PUT",
                                        headers: {
                                            "Authorization": "Bearer " + this.props.token
                                        }
                                    });

                                    this.setState((prevState) => {
                                        const data = [...prevState.data];
                                        data.splice(data.indexOf(oldData), 1)

                                        return {...prevState, data};
                                    });
                                    this.componentDidMount();
                                }, 600);

                            }),
                    }}
                />
            </div>
        );
    }
}

export default Sales;