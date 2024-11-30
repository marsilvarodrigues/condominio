import React from 'react';
import { Modal, Box, Button, TextField } from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import Formulario from "./formulario.jsx";
import CloseIcon from "@mui/icons-material/Close";

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '70%',
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

export default function MoradorModal({ open, onClose, onSubmit, morador })  {
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

                <Formulario label={{icon: <SaveIcon sx={{mr: 1}}/>, text: "Salvar"}} showPasswordField={morador == null} morador={morador} onSubmit={onSubmit}/>
            </Box>
        </Modal>
    )
};