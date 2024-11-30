import React from 'react';
import { Modal, Box, Button, TextField } from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import Formulario from "./formulario.jsx";

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 650,
    height: 300,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

export default function VisitanteModal({ open, handleClose })  {
    return (
        <Modal open={open} onClose={handleClose}>
            <Box sx={modalStyle}>
                <Formulario label={{icon: <SaveIcon sx={{mr: 1}}/>, text: "Salvar"}} showPasswordField={true}/>
            </Box>
        </Modal>
    )
};