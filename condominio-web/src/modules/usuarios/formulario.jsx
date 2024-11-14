import React from 'react';
import {
    Button,
    Container,
    FormControl,
    FormGroup,
    Grid, InputLabel, MenuItem,
    Select,
    TextField
} from "@mui/material";

export default function Formulario({ label , showPasswordField}) {

    const [perfil, setPerfil] = React.useState('');

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
                    <Grid item width="45%" margin="10">
                        <TextField label="Username" variant="outlined" fullWidth margin="normal" />
                    </Grid>
                    {showPasswordField && ( <Grid item width="45%" margin="10">
                        <TextField label="Senha" type="password" variant="outlined" fullWidth margin="normal" />
                    </Grid>)}
                </Grid>
                <Grid container spacing={3}>
                    <Grid item width="45%" margin="10">
                    <FormControl variant="standard" sx={{ m: 1, minWidth: 120 }}>
                        <InputLabel id="demo-simple-select-standard-label">Perfil</InputLabel>
                        <Select
                            labelId="demo-simple-select-standard-label"
                            id="demo-simple-select-standard"
                            value={perfil}
                            onChange={handleChange}
                            label="Perfil"
                        >
                            <MenuItem value="">
                                <em></em>
                            </MenuItem>
                            <MenuItem value={10}>Porteiro</MenuItem>
                            <MenuItem value={20}>Administrador</MenuItem>

                        </Select>
                    </FormControl>
                    </Grid>
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
