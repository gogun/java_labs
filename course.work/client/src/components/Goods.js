import React, {Component} from 'react';
import MaterialTable from 'material-table';

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
                { id: null, name: "tovar_3", priority: 2, left: 0 }
            ],
        }
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
        body.map((good, index) => {
            good.left = list[index]
        });
        this.setState({data: body, isLoading: false});
    }

    render() {
        if (this.state.isLoading) {
            return <p>Загрузка...</p>;
        }
        return (
            <div>
                {console.log(this.state.data)}
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

                                    const response = await fetch('/api/goods/add', {
                                        method: "POST",
                                        dataType: 'json',
                                        body: JSON.stringify(newData),
                                        headers: {
                                            "Content-Type": "application/json"
                                        }
                                    });
                                    const body = await response.json();
                                    this.setState((prevState) => {
                                        const data = [...prevState.data];
                                        data.push(body);

                                        return { ...prevState, data };
                                    });
                                    this.componentDidMount();
                                }, 600);

                            }),
                        onRowUpdate: (newData, oldData) =>
                            new Promise((resolve) => {
                                setTimeout(() => {
                                    resolve();
                                    if (oldData) {
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