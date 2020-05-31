import React, {Component} from 'react';
import MaterialTable from 'material-table';
import Alert from '@material-ui/lab/Alert';
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from '@material-ui/icons/Close';
import Collapse from "@material-ui/core/Collapse";
import AlertTitle from "@material-ui/lab/AlertTitle";
import EditComponentUser from "./EditComponentUser";
import Cookies from "universal-cookie";
import Redirect from "react-router-dom/es/Redirect";


const _ = require('lodash');

const editComponent = EditComponentUser;

class GoodsUser extends Component {
    constructor(props) {
        super(props);
        this.cookie = new Cookies();
        this.token = this.props.token;
        this.state = {
            showNameAlert: false,
            showAddAlert : false,
            isRedirect: false,
            isLoading: true,
            data: [
                {id: null, name: null, priority: null, left: null, uuid: null}
            ],
            columns: [
                {title: 'Название товара', field: 'name', editComponent},
                {title: 'Приоритет', field: 'priority', editComponent},
                {title: 'Остаток на складе', field: 'left', editComponent},
            ],
        }
    }

    makeResponse = (body, data) => {
        let obj = {
            goods: {},
            count: 0
        };
        obj.count = data.left;
        obj.goods = body;
        if (data.uuid != null) {
            obj.id = data.uuid
        }

        return obj;
    };

    async componentDidMount() {
        let response_goods = await fetch('/api/goods/all', {
            headers : {
                "Authorization": "Bearer " + this.token
            }
        });

        if (!response_goods.ok) {
            if (this.cookie.get('remember') === "true") {

                let body = this.cookie.get('user');

                const request = await fetch("/api/auth/signin", {
                    method: "POST",
                    body: JSON.stringify(body),
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
                const response = await request.json();
                this.cookie.set('token', response.token, {path: '/'});
                this.token = response.token;
                response_goods = await fetch('/api/goods/all', {
                    headers : {
                        "Authorization": "Bearer " + this.token
                    }
                });
            } else {
                this.cookie.remove('token');
                this.cookie.remove('role');
                this.cookie.remove('remember');
                this.cookie.remove('user');
                this.setState({isLoading:false})
                return;
            }
        }

        const body = await response_goods.json();

        const response_amounts = await fetch('/api/warehouse/left', {
            method: "POST",
            dataType: 'json',
            body: JSON.stringify(body),
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + this.token
            }
        });
        const list = await response_amounts.json();
        body.map((good, index) => {
            good.left = list[index].amount;
            good.uuid = list[index].uuid;
        });
        this.setState({data: body, isLoading: false});
    }

    validateUniqueName = (prop) => {
        let found = false;
        this.state.data.filter((row) => {
            if (row.uuid !== prop.uuid && row.name === prop.name) {
                console.log(row);
                console.log(prop);
                found = true;
            }
        });
        return !found;
    };

    validateAdd = (prop) => {
        return prop.name !== undefined && prop.priority !== undefined && prop.left !== undefined;
    };

    render() {
        if (this.state.isLoading) {
            return <p>Загрузка...</p>;
        }
        if (_.isEmpty(this.cookie.getAll())) {
            return <Redirect to='/'/>
        }

        return (
            <div>
                <Collapse in={this.state.showNameAlert}>
                    <Alert
                        severity="warning"
                        action={
                            <IconButton
                                aria-label="close"
                                color="inherit"
                                size="small"

                                onClick={() => {
                                    this.setState({showNameAlert: false});
                                }}
                            >

                                <CloseIcon fontSize="inherit"/>
                            </IconButton>
                        }
                    >
                        <AlertTitle>Предупреждение</AlertTitle>
                        Товар с таким именем уже существует, выберите другое
                    </Alert>
                </Collapse>
                <Collapse in={this.state.showAddAlert}>
                    <Alert
                        severity="warning"
                        action={
                            <IconButton
                                aria-label="close"
                                color="inherit"
                                size="small"

                                onClick={() => {
                                    this.setState({showAddAlert: false});
                                }}
                            >

                                <CloseIcon fontSize="inherit"/>
                            </IconButton>
                        }
                    >
                        <AlertTitle>Предупреждение</AlertTitle>
                        Введите корректные данные
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
                                deleteText: 'Вы уверены, что хотите удалить эту строку?',
                                cancelTooltip: 'Отменить',
                                saveTooltip: 'Подтвердить'
                            },
                            addTooltip: 'Добавить',
                            deleteTooltip: 'Удалить',
                            editTooltip: 'Редактировать'
                        }
                    }}
                    title="Таблица товаров"
                    columns={this.state.columns}
                    data={this.state.data}
                />
            </div>
        )
    }
}

export default GoodsUser;