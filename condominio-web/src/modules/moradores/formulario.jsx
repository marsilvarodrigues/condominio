import React, {useState} from 'react';
import {
    Button,
    Container,
    FormControl,
    FormGroup,
    Grid, InputLabel, MenuItem,
    Select,
    TextField
} from "@mui/material";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";

export default function Formulario({ label , showPasswordField}) {

    const [perfil, setPerfil] = React.useState('');
    const [dataNascimento, setDataNascimento] = useState(dayjs());

    const handleChange = (event) => {
        setPerfil(event.target.value);
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
                disableGutters // Remove o padding padrão do Container
                maxWidth="100%"
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'flex-start', // Alinha no topo
                    paddingBottom: 2, // Garante que não haja padding no topo
                }}>
                <Grid container spacing={3}>
                    <Grid item width="45%">
                        <TextField label="Nome" variant="outlined" fullWidth margin="normal" />
                    </Grid>
                    <Grid item xs={2} marginTop={2}>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DatePicker
                                label="Data de nascimento"
                                value={dataNascimento}
                                onChange={(newValue) => setDataNascimento(newValue)}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        fullWidth
                                        variant="outlined"
                                        margin="none"
                                    />
                                )}
                            />
                        </LocalizationProvider>
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
                    {showPasswordField && ( <Grid item width="45%" margin="10">
                        <TextField label="Senha" type="password" variant="outlined" fullWidth margin="normal" />
                    </Grid>)}
                </Grid>


                <Button
                    variant="contained"
                    color="primary"
                    size="small"
                    onClick={handleSubmit}
                    sx={{ align: 'left', padding: '6px 12px', fontSize: '0.8rem', width: '150px', display: 'column' }} // Define largura e centraliza
                >
                    {label.icon} {/* Renderiza o ícone */}
                    {label.text} {/* Renderiza o texto */}
                </Button>
            </Container>

    );
}
