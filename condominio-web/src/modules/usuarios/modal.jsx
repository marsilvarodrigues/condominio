import React from 'react';
import { Modal, Box, Typography, Button } from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';

import Formulario from "./formulario.jsx";
import CloseIcon from "@mui/icons-material/Close";

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    display: 'flex',
    p: 4,
};

export default function UsuarioModal({ open, onClose, usuario, onSubmit })  {
    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={modalStyle}>
                <Button sx={{
                    position: 'absolute',
                    top: 8,
                    right: 8,
                }}
                        onClick={onClose}
                ><CloseIcon /></Button>
                <Formulario label={{icon: <SaveIcon sx={{mr: 1}}/>, text: "Salvar"}} showPasswordField={usuario == null} usuario={usuario} onSubmit={onSubmit}/>


            </Box>
        </Modal>
    )
};