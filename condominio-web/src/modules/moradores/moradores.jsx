import React, {useState} from 'react';
import BaseLayout from '../template/baseUI.jsx';
import { DataGrid } from '@mui/x-data-grid';
import {Breadcrumbs, Button, Container, Link, Typography} from '@mui/material';
import Formulario from './formulario.jsx';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import HomeIcon from '@mui/icons-material/Home';
import * as PropTypes from "prop-types";
import UsuarioModal from "./modal.jsx";
import MoradorModal from "./modal.jsx";

const columns = [
    { field: 'moradorId', headerName: 'ID', width: 10 },
    { field: 'nome', headerName: 'Nome', width: 200, sortable: true },
    { field: 'cpf', headerName: 'CPF', width: 150, sortable: true },
    { field: 'dataNascimento', headerName: 'Data de Nascimento', width: 150, sortable: true },
    { field: 'apartamento', headerName: 'Apartamento', width: 150, sortable: true },
];


HomeIcon.propTypes = {
    sx: PropTypes.shape({mr: PropTypes.number}),
    fontSize: PropTypes.string
};

function Moradores() {

    const [open, setOpen] = useState(false);

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    return (
        <BaseLayout>
            <Container
                maxWidth="100%"
                disableGutters // Remove o padding padrão do Container
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'flex-start', // Alinha no topo
                    minHeight: '90vh', // Garante que o container tenha a altura da tela
                    paddingTop: 0, // Garante que não haja padding no topo
                }}
            >

                <Breadcrumbs aria-label="breadcrumb">

                    <Link
                        underline="hover"
                        sx={{ display: 'flex', alignItems: 'center' }}
                        color="inherit"
                        href="/administracao"
                    >
                        <HomeIcon sx={{ mr: 0.5 }} fontSize="inherit" />
                        Administração
                    </Link>
                    <Typography
                        sx={{ color: 'text.primary', display: 'flex', alignItems: 'center' }}
                    >
                        Moradores
                    </Typography>
                </Breadcrumbs>

                <Formulario label={{ icon: <SearchIcon sx={{ mr: 1 }} />, text: "Pesquisar" }} showPasswordField={false}/>
                <div style={{ marginTop: 0 }}> {/* Contêiner para o DataGrid */}
                    <DataGrid
                        columns={columns}
                        pageSize={5}
                        rowsPerPageOptions={[5]}
                        checkboxSelection
                        sx={{
                            marginTop: 0, // Remove espaço acima do DataGrid
                            '& .MuiDataGrid-root': {
                                marginTop: 0, // Se necessário, remover espaço no próprio DataGrid
                            },
                        }}
                    />
                </div>
                <Button
                    variant="contained"
                    color="primary"
                    size="small"
                    onClick={handleOpen}
                    sx={{ align: 'left',
                        padding: '6px 12px',
                        fontSize: '0.8rem',
                        width: '150px',
                        display: 'column',
                        marginTop: 2 }} // Define largura e centraliza
                >
                    <AddIcon />Adicionar

                </Button>
            </Container>
            <MoradorModal open={open} onClose={handleClose} />
        </BaseLayout>
    );
}

export default Moradores;
