import React, { useState } from 'react';
import {
    Button,
    Container,
    Grid,
    TextField
} from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";
import UsuarioModal from "../usuarios/modal.jsx";
import VisitanteModal from "./modal.jsx";

export default function Formulario({ label, showVeiculoField }) {

    const [dataVisita, setDataVisita] = useState(dayjs());
    const [modelo, setModelo] = useState('');
    const [cor, setCor] = useState('');
    const [placa, setPlaca] = useState('');
    const [fabricante, setFabricante] = useState('');

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
            <Grid container spacing={2} alignItems="flex-end" sx={{ paddingBottom: 2 }}>
                {/* Primeira linha */}
                <Grid item xs={3}>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            label="Data da Visita"
                            value={dataVisita}
                            onChange={(newValue) => setDataVisita(newValue)}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    fullWidth
                                    variant="outlined"
                                    margin="normal"
                                    marginTop={10}
                                />
                            )}
                        />
                    </LocalizationProvider>
                </Grid>
                <Grid item xs={5}>
                    <TextField
                        label="Visitante"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                    />
                </Grid>
                <Grid item xs={2}>
                    <TextField
                        label="Bloco"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                    />
                </Grid>
                <Grid item xs={2}>
                    <TextField
                        label="Apartamento"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                    />
                </Grid>

                {showVeiculoField && (
                    <Grid container spacing={2} sx={{ paddingTop: 2, marginLeft: 0.2}}>
                    <Grid item xs={2}>
                        <TextField
                            label="Modelo"
                            variant="outlined"
                            fullWidth
                            value={modelo}
                            onChange={(e) => setModelo(e.target.value)}
                            required
                        />
                    </Grid>
                    <Grid item xs={1.5}>
                        <TextField
                            label="Cor"
                            variant="outlined"
                            fullWidth
                            value={cor}
                            onChange={(e) => setCor(e.target.value)}
                            required
                        />
                    </Grid>
                    <Grid item xs={1.5}>
                        <TextField
                            label="Placa"
                            variant="outlined"
                            fullWidth
                            value={placa}
                            onChange={(e) => setPlaca(e.target.value)}
                            required
                        />
                    </Grid>
                    <Grid item xs={3}>
                        <TextField
                            label="Fabricante"
                            variant="outlined"
                            fullWidth
                            value={fabricante}
                            onChange={(e) => setFabricante(e.target.value)}
                            required
                        />
                    </Grid>
                </Grid>
                    )}
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
