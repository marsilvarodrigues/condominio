import React, { useState } from 'react';
import {
    Button,
    Container,
    Dialog, DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    TextField,
    Typography
} from "@mui/material";
import { createTheme } from '@mui/material/styles';
import BaseLayout from '../template/baseUI.jsx';
import axios from 'axios';
import CloseIcon from '@mui/icons-material/Close';

// Crie um tema personalizado (opcional)
const theme = createTheme();


function LoginForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [open, setOpen] = useState(false);

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post(
                'http://localhost:8080/api/auth',
                new URLSearchParams({
                    username,
                    password,
                }),
                {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                }
            );

            const token = response.data.token; // Assumindo que o token JWT vem no campo 'token' da resposta
            sessionStorage.setItem('auth_token', token);
            window.location.href = '/';
        } catch (error) {
            console.log(error);
            setError('Falha na autenticação. Verifique suas credenciais.');
            setOpen(true);
        }

    };

    const handleClose = () => {
        setOpen(false); // Fecha o Dialog
    };

    return (
        <BaseLayout>
            <Container maxWidth="xs">
                <TextField label="Username" variant="outlined" fullWidth margin="normal" value={username}
                           onChange={(e) => setUsername(e.target.value)}/>
                <TextField label="Senha" type="password" variant="outlined" fullWidth margin="normal" value={password}
                           onChange={(e) => setPassword(e.target.value)}  />
                <Button variant="contained" color="primary" fullWidth onClick={handleLogin}>
                    Entrar
                </Button>
                <Dialog open={open} onClose={handleClose}>
                    <DialogTitle>Erro de Autenticação</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            {error}
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleClose} color="primary" startIcon={<CloseIcon />}>
                            Fechar
                        </Button>
                    </DialogActions>
                </Dialog>
            </Container>
        </BaseLayout>
    );
}

export default LoginForm;
