import React, { useEffect, useState } from 'react';
import {
    Button,
    Container,
    FormControl,
    Grid,
    InputLabel,
    MenuItem,
    Select,
    TextField
} from "@mui/material";

import { fetchPerfis } from "../../libraries/apiService.js";

export default function Formulario({ label, showPasswordField, usuario, onSubmit }) {
    const [perfisDisponiveis, setPerfisDisponiveis] = useState([]); // Perfis disponíveis para seleção
    const [perfisSelecionados, setPerfisSelecionados] = useState([]); // Perfis selecionados
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [usuarioId, setUsuarioId] = useState('');

    const getPerfis = async () => {
        try {
            const data = await fetchPerfis();
            setPerfisDisponiveis(data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleChange = (event) => {
        setPerfisSelecionados(event.target.value); // Atualiza a lista de perfis selecionados
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log({
            usuarioId: usuarioId,
            username: username,
            perfis: perfisSelecionados,
            password: password
        });
        onSubmit({
            usuarioId: usuarioId,
            username: username,
            perfis: perfisSelecionados,
            password: password
        });
    };

    useEffect(() => {
        getPerfis();

        // Preenche os campos do formulário com os valores do usuário selecionado (se houver)
        if (usuario) {
            console.log(usuario);
            setUsername(usuario.username || '');
            const perfisSelecionadosMapeados = usuario.perfis?.map((perfil) =>
                perfisDisponiveis.find((p) => p.id === perfil.id)?.nome
            ).filter(Boolean);
            setPerfisSelecionados(perfisSelecionadosMapeados || []);
            setUsuarioId(usuario.usuarioId || '');
        } else {
            // Reseta os campos para valores padrão ao criar um novo usuário
            setUsername('');
            setPerfisSelecionados([]);
        }
    }, [usuario]);

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
            <Grid container spacing={3}>
                <Grid item width="45%" margin="10">
                    <TextField
                        label="Username"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </Grid>
                {showPasswordField && (
                    <Grid item width="45%" margin="10">
                        <TextField
                            label="Senha"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            type="password"
                            variant="outlined"
                            fullWidth
                            margin="normal"
                        />
                    </Grid>
                )}
            </Grid>
            <Grid container spacing={3}>
                <Grid item width="45%" margin="10">
                    <FormControl variant="standard" sx={{ m: 1, minWidth: 120 }}>
                        <InputLabel id="demo-simple-select-standard-label">Perfil</InputLabel>
                        <Select
                            labelId="demo-simple-select-standard-label"
                            id="demo-simple-select-standard"
                            value={perfisSelecionados}
                            onChange={handleChange}
                            label="Perfil"
                            multiple
                        >
                            {Array.isArray(perfisDisponiveis) &&
                                perfisDisponiveis.map((p) => (
                                    <MenuItem key={p.id} value={p.nome}>
                                        {p.nome}
                                    </MenuItem>
                                ))}
                        </Select>
                    </FormControl>
                </Grid>
            </Grid>
            <Button
                variant="contained"
                color="primary"
                size="small"
                onClick={handleSubmit}
                sx={{
                    align: 'left',
                    padding: '6px 12px',
                    fontSize: '0.8rem',
                    width: '150px',
                    display: 'column'
                }}
            >
                {label.icon} {label.text}
            </Button>
        </Container>
    );
}
