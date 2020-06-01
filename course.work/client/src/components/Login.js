import React, {useState} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Alert from "@material-ui/lab/Alert";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";
import AlertTitle from "@material-ui/lab/AlertTitle";
import Collapse from "@material-ui/core/Collapse";
import Cookies from 'universal-cookie';
import Redirect from "react-router-dom/es/Redirect";

const _ = require('lodash');

const useStyles = makeStyles((theme) => ({

    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(1),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
}));

export default function SignIn() {

    const cookies = new Cookies();

    const classes = useStyles();

    const [username, setUsername] = useState("");
    const [pass, setPass] = useState("");
    const [isAdmin, setAdmin] = useState(false);
    const [isUser, setUser] = useState(false);
    const [isAlert, setAlert] = useState(false);

    let isRemember = false;

    const handleSubmit = async (e) => {
        e.preventDefault();

        const body = {
            "username": username,
            "password": pass
        };

        const request = await fetch("/api/auth/signin", {
            method: "POST",
            body: JSON.stringify(body),
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (!request.ok) {
            setAlert(true)
        } else {
            const response = await request.json();

            cookies.set('role', response.role, {path: '/'});
            cookies.set('token', response.token, {path: '/'});
            cookies.set('remember', false, {path: '/'});
            cookies.set('user', body, {path: '/'});
            console.log(response)
            if (isRemember) {
                cookies.set('remember', true, {path: '/'});
            }
            if (response.role === "ROLE_ADMIN") {
                setAdmin(true)
            } else {
                setUser(true)
            }

        }

    };

    if (isAdmin) {
        return <Redirect to='/admin'/>
    }

    if (isUser) {
        return <Redirect to='/main'/>
    }

    const handleChangeUsername = (e) => {
        setUsername(e.target.value)
    };

    const handleChangePass = (e) => {
        setPass(e.target.value)
    };

    const handleRemember = (e) => {
        isRemember = !!e.target.checked;
    };

    return (

        <div>
            <Collapse in={isAlert}>
                <Alert
                    severity="warning"
                    action={
                        <IconButton
                            aria-label="close"
                            color="inherit"
                            size="small"

                            onClick={() => {
                                setAlert(false);
                            }}
                        >

                            <CloseIcon fontSize="inherit"/>
                        </IconButton>
                    }
                >
                    <AlertTitle>Предупреждение</AlertTitle>
                    Неправильный логин или пароль
                </Alert>
            </Collapse>

            <Container component="main" maxWidth="xs">
                <CssBaseline/>
                <div className={classes.paper}>
                    <Avatar className={classes.avatar}>
                        <LockOutlinedIcon/>
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        Вход
                    </Typography>
                    <form className={classes.form} noValidate onSubmit={handleSubmit}>
                        <TextField
                            variant="outlined"
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            value={username}
                            onChange={handleChangeUsername}
                            label="Логин"
                            name="email"
                            autoComplete="email"
                            autoFocus
                        />
                        <TextField
                            variant="outlined"
                            margin="normal"
                            required
                            fullWidth
                            value={pass}
                            name="password"
                            label="Пароль"
                            onChange={handleChangePass}
                            type="password"
                            id="password"
                            autoComplete="current-password"
                        />
                        <FormControlLabel
                            control={<Checkbox onChange={handleRemember} value="remember" color="primary"/>}
                            label="Запомнить меня"
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            className={classes.submit}
                        >
                            Войти
                        </Button>
                    </form>
                </div>
            </Container>
            {(!_.isEmpty(cookies.getAll()) && cookies.get('role') === "ROLE_USER")
                ? <Redirect to='/main'/> : null}
            {(!_.isEmpty(cookies.getAll()) && cookies.get('role') === "ROLE_ADMIN")
                ? <Redirect to='/admin'/> : null}
        </div>
    );
}