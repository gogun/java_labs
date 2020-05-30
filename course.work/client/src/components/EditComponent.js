import React, {useState} from "react";
import TextField from "@material-ui/core/TextField";

const validate = {

    name: s => ((s.length > 0 ) ? "" : "Введите название товара"),
    priority: s => ((!isNaN(parseInt(s)) && parseInt(s) > 0) ? "" : "Некорректный приоритет"),
    left: s => ((!isNaN(parseInt(s)) && parseInt(s) > 0) ? "" : "Некорректное число")
};

const EditComponent = ({ onChange, value, ...rest }) => {
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

export default EditComponent;