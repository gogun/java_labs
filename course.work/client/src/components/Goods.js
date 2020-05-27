import React, {Component, useState} from 'react';
import MaterialTable from 'material-table';
import TextField from "@material-ui/core/TextField";

const validate = {
    fullName: s => (s.length > 3 ? "" : "Name not long enough"),
    email: s => (s.length > 3 ? "" : "Not valid email")
};

const editComponent = ({ onChange, value, ...rest }) => {
    const [currentValue, setValue] = useState(value);
    const [error, setError] = useState("");
    const change = e => {
        const newValue = e.target.value;
        setValue(newValue);
        const errorMessage = validate[rest.columnDef.field](newValue);
        setError(errorMessage);
        if (!errorMessage) {
            onChange(newValue);
        }
    };
    return (
        <TextField
            {...rest}
            error={error}
            helperText={error}
            value={currentValue}
            onChange={change}
        />
    );
};

class Goods extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: true,
            columns: [
                { title: 'Название товара', field:'name' },
                { title: 'Приоритет', field: 'priority' },
                {title: 'Остаток на складе', field: 'left'},
            ],

            data: [
                { id: null, name: null, priority: null, left: null, uuid: null }
            ],
        }
    }

    makeResponse = (body, data) => {
        let obj = {
            goods: {},
            count:0
        };
        obj.count = data.left;
        obj.goods = body;
        if (data.uuid != null) {
            obj.id = data.uuid
        }

        return obj;
    };

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
        body.map((good, index) => {
            good.left = list[index].amount;
            good.uuid = list[index].uuid;
        });
        this.setState({data: body, isLoading: false});
    }

    render() {

        if (this.state.isLoading) {
            return <p>Загрузка...</p>;
        }

        return (
            <div>
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
                                deleteText: 'Вы уверены, что хотите удалить эту строку?',
                                cancelTooltip: 'Отменить',
                                saveTooltip: 'Подтвердить'
                            },
                            addTooltip: 'Добавить',
                            deleteTooltip: 'Удалить',
                            editTooltip: 'Редактировать'
                        }
                    }}
                    title="Таблица Товаров"
                    columns={this.state.columns}
                    data={this.state.data}
                    editable={{
                        onRowAdd: (newData) =>
                            new Promise((resolve) => {
                                setTimeout(async () => {
                                    resolve();

                                    const response_good = await fetch('/api/goods/add', {
                                        method: "POST",
                                        dataType: 'json',
                                        body: JSON.stringify(newData),
                                        headers: {
                                            "Content-Type": "application/json"
                                        }
                                    });
                                    let body = await response_good.json();

                                    if (newData.left != null) {
                                        await fetch('/api/warehouse/add', {
                                            method: "POST",
                                            dataType: 'json',
                                            body: JSON.stringify(this.makeResponse(body, newData)),
                                            headers: {
                                                "Content-Type": "application/json"
                                            }
                                        });
                                    }
                                    this.setState((prevState) => {
                                        const data = [...prevState.data];
                                        data.push(newData);

                                        return { ...prevState, data };
                                    });
                                    // this.componentDidMount();
                                }, 600);

                            }),
                        onRowUpdate: (newData, oldData) =>
                            new Promise((resolve) => {
                                setTimeout(async () => {
                                    resolve();
                                    if (oldData) {

                                        const response_good = await fetch('/api/goods/update', {
                                            method: "PUT",
                                            dataType: 'json',
                                            body: JSON.stringify(newData),
                                            headers: {
                                                "Content-Type": "application/json"
                                            }
                                        });
                                        let body = await response_good.json();

                                        if (newData.left != null) {
                                            await fetch('/api/warehouse/update', {
                                                method: "PUT",
                                                dataType: 'json',
                                                body: JSON.stringify(this.makeResponse(body, newData)),
                                                headers: {
                                                    "Content-Type": "application/json"
                                                }
                                            });
                                        }


                                        this.setState((prevState) => {
                                            const data = [...prevState.data];
                                            data[data.indexOf(oldData)] = newData;
                                            return { ...prevState, data };
                                        });
                                    }
                                }, 600);
                            }),
                        onRowDelete: (oldData) =>
                            new Promise((resolve) => {
                                setTimeout(async () => {
                                    resolve();

                                    await fetch('api/goods/delete/' + oldData.id, {
                                        method: "DELETE"
                                    });

                                    this.setState((prevState) => {
                                        const data = [...prevState.data];
                                        data.splice(data.indexOf(oldData), 1)

                                        return { ...prevState, data };
                                    });

                                }, 600);

                            }),
                    }}
                />
            </div>
        )
    }
}

export default Goods;