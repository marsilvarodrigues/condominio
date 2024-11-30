import React, {useState, useEffect} from 'react';
import BaseLayout from '../template/baseUI.jsx';
import { DataGrid } from '@mui/x-data-grid';
import {Breadcrumbs, Button, Container, IconButton, Link, Typography} from '@mui/material';
import Formulario from './formulario.jsx';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import HomeIcon from '@mui/icons-material/Home';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import * as PropTypes from "prop-types";
import UsuarioModal from "./modal.jsx";
import axios from "axios";
import {
    fetchUsuarios,
    getUsuario,
    excluirUsuario,
    pesquisarUsuario,
    salvarUsuario
} from "./usuarioService.js";

import {useNavigate} from "react-router-dom";



HomeIcon.propTypes = {
    sx: PropTypes.shape({mr: PropTypes.number}),
    fontSize: PropTypes.string
};


function Usuarios() {

    const [open, setOpen] = useState(false);
    const [usuarios, setUsuarios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [usuarioSelecionado, setUsuarioSelecionado] = useState(null);
    const navigate = useNavigate();

    const handleOpen = () => {
        setUsuarioSelecionado(null);
        setOpen(true);
    }
    const handleClose = () => setOpen(false);

    useEffect(() => {
        // Função para buscar os usuários
        const interceptor = axios.interceptors.response.use((response) => response,
            (error) => {
                    if (error.response && (error.response.status == 401 || error.response.status == 403)) {
                        sessionStorage.removeItem("auth_token");
                        navigate('/');
                    }
                }
        );

        const getUsuarios = async () => {
            try {
                setLoading(true);
                const usuarios = await fetchUsuarios();
                setUsuarios(usuarios);
            } catch (error) {
                console.error("Erro ao buscar os usuários:", error);
            } finally {
                setLoading(false); // Finaliza o estado de carregamento
            }
        }

        getUsuarios();

        return () => {
            axios.interceptors.response.eject(interceptor);
        };
    }, [navigate]);

    const handleEditar = async (usuario) => {

        setUsuarioSelecionado(await getUsuario(usuario.usuarioId));

        setOpen(true); // Abre o modal
    };

    const handlePesquisa = async (usuario) => {
        try {
            setLoading(true);
            setUsuarios(await pesquisarUsuario(usuario));
        }catch (error){
            console.error("erro ao buscar usuarios: ", error);
        }finally{
            setLoading(false);
        }
    }

    const handleNewUsuario = async(usuario) => {
        try {
            salvarUsuario(usuario);
            setOpen(false);
        }catch(error){
            console.error("erro ao salvar usuarios :", error);
        }finally{
            setLoading(true);
            const usuarios = await fetchUsuarios();
            setUsuarios(usuarios);
            setLoading(false);
        }
    }

    const handleExcluir = async (usuario) => {
        if (window.confirm(`Tem certeza que deseja excluir o usuário ${usuario.username}?`)) {
            try {
                const usuarios = await excluirUsuario(usuario.usuarioId);
                setUsuarios(usuarios);
                alert("Usuário excluído com sucesso!");
            } catch (error) {
                console.error("Erro ao excluir o usuário:", error);
                alert("Erro ao excluir o usuário.");
            }
        }
    };

    const columns = [
        { field: 'id', headerName: 'ID', width: 10 },
        { field: 'username', headerName: 'Nome de Usuário', width: 300, sortable: true },
        { field: 'perfis', headerName: 'Perfis', width: 400, sortable: true },
        { field: 'dataCriacao', headerName: 'Data da criação', width: 150, sortable: true },
        {   field: 'acoes',
            headerName: '',
            sortable: false,
            width: 150,
            renderCell: (params) => (
                <div style={{display: 'flex', gap: '10px'}}>
                    <IconButton
                        color="primary"
                        onClick={() => handleEditar(params.row)}
                    >
                        <EditIcon/>
                    </IconButton>
                    <IconButton
                        color="error"
                        onClick={() => handleExcluir(params.row)}
                    >
                        <DeleteIcon/>
                    </IconButton>
                </div>
            ),
        }
    ];

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
                        Usuários
                    </Typography>
                </Breadcrumbs>

                <Formulario label={{ icon: <SearchIcon sx={{ mr: 1 }} />, text: "Pesquisar" }} showPasswordField={false} onSubmit={handlePesquisa}/>
                <div style={{ marginTop: 0 }}> {/* Contêiner para o DataGrid */}
                    <DataGrid
                        columns={columns}
                        rows={usuarios}
                        pageSize={5}
                        rowsPerPageOptions={[5]}
                        loading={loading}
                        getRowId={(row) => row.usuarioId}
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
            <UsuarioModal open={open} onClose={handleClose} usuario={usuarioSelecionado}
                          onSubmit={handleNewUsuario}/>
        </BaseLayout>
    );
}

export default Usuarios;
