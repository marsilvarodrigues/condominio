import React, { useState } from 'react';
import {
    Button,
    Container, FormControl,
    Grid, InputLabel, MenuItem, Select,
    TextField
} from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";

export default function Formulario({ label }) {

    const [dataReserva, setDataReserva] = useState(dayjs());
    const [espacoComum, setEspacoComum] = useState('');

    const handleChange = (event) => {
        setEspacoComum(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        const credentials = {
            email: data.get('email'),
            password: data.get('password'),
        };
        console.log('Login attempt:', credentials);
    };

    return (
        <Container
            disableGutters
            maxWidth="100%"
            sx={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'flex-start',
                paddingBottom: 2,
            }}
        >
            {/* Reduzindo o espaçamento entre os campos */}
            <Grid container spacing={1} alignItems="flex-end" sx={{ paddingBottom: 2 }}>
                {/* Data da Reserva */}
                <Grid item xs={3}>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            label="Data da Reserva"
                            value={dataReserva}
                            onChange={(newValue) => setDataReserva(newValue)}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    fullWidth
                                    variant="outlined"
                                    margin="normal"
                                />
                            )}
                        />
                    </LocalizationProvider>
                </Grid>

                {/* Espaço Comum */}
                <Grid item xs={3}>
                    <FormControl variant="standard" sx={{ minWidth: 200 }}>
                        <InputLabel id="demo-simple-select-standard-label">Espaço Comum</InputLabel>
                        <Select
                            labelId="demo-simple-select-standard-label"
                            id="demo-simple-select-standard"
                            value={espacoComum}
                            onChange={handleChange}
                            label="Espaço Comum"
                        >
                            <MenuItem value="">
                                <em>Selecione um espaço</em>
                            </MenuItem>
                            <MenuItem value={1}>Churrasqueira 1</MenuItem>
                            <MenuItem value={2}>Churrasqueira 2</MenuItem>
                            <MenuItem value={3}>Churrasqueira 3</MenuItem>
                            <MenuItem value={4}>Salão de Festa Infantil</MenuItem>
                            <MenuItem value={5}>Salão de Festa Adulto</MenuItem>
                        </Select>
                    </FormControl>
                </Grid>

                {/* Bloco */}
                <Grid item xs={2}>
                    <TextField
                        label="Bloco"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                    />
                </Grid>

                {/* Apartamento */}
                <Grid item xs={2}>
                    <TextField
                        label="Apartamento"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                    />
                </Grid>
            </Grid>

            <Button
                variant="contained"
                color="primary"
                size="small"
                onClick={handleSubmit}
                sx={{ width: '150px', marginTop: 2 }}
            >
                {label.icon} {label.text}
            </Button>
        </Container>
    );
}
