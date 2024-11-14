import React, {useEffect, useState} from 'react';
import {AppBar, Toolbar, IconButton, Menu, MenuItem, Typography, Box, Button} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { useNavigate } from 'react-router-dom';


const BaseLayout = ({ children }) => {
    const navigate = useNavigate();
    const [anchorEl, setAnchorEl] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        // Verifica se o token está armazenado ao montar o componente
        const token = sessionStorage.getItem("auth_token");
        setIsAuthenticated(!!token);
    }, []);

    const handleMenuClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    const handleLogout = () => {
        sessionStorage.removeItem("auth_token");
        setIsAuthenticated(false);
        navigate('/auth'); // Redireciona para a tela de login

    };

    const handleNavigate = (module) => {
        navigate(module);
    };

    return (
        <Box sx={{flexGrow: 1}}>
            <AppBar position="static">
                <Toolbar>
                    {isAuthenticated && (
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        sx={{ mr: 2 }}
                        onClick={handleMenuClick}>
                        <MenuIcon />
                    </IconButton>
                    )}
                    <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                        Sistema de Gestão de Condominio
                    </Typography>
                    {isAuthenticated && (
                        <Menu
                            anchorEl={anchorEl}
                            open={Boolean(anchorEl)}
                            onClose={handleMenuClose}>
                            <MenuItem onClick={() => handleNavigate('/usuarios')}>Administração</MenuItem>
                            <MenuItem onClick={() => handleNavigate('/visitantes')}>Visitantes</MenuItem>
                            <MenuItem onClick={() => handleNavigate('/moradores')}>Moradores</MenuItem>
                            <MenuItem onClick={() => handleNavigate('/')}>Reserva</MenuItem>
                        </Menu>
                    )}
                    {isAuthenticated && (
                        <Button color="inherit" onClick={handleLogout}>
                            Logout
                        </Button>
                    )}
                </Toolbar>
            </AppBar>
            <main style={{
                padding: 20,
                height: 'calc(100vh - 64px)',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center'
            }}>
                {children}
            </main>
        </Box>

    )
        ;
};

export default BaseLayout;
