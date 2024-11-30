import React, {useEffect, useState} from 'react';
import BaseLayout from '../template/baseUI.jsx';
import { DataGrid } from '@mui/x-data-grid';
import {Breadcrumbs, Button, Container, IconButton, Link, Typography} from '@mui/material';
import Formulario from './formulario.jsx';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import HomeIcon from '@mui/icons-material/Home';
import * as PropTypes from "prop-types";
import MoradorModal from "./modal.jsx";
import axios from "axios";
import {fetchMoradores, getMorador, salvarMorador, pesquisarMorador, excluirMorador} from "./moradorService.js";
import {useNavigate} from "react-router-dom";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import {excluirUsuario} from "../usuarios/usuarioService.js";



HomeIcon.propTypes = {
    sx: PropTypes.shape({mr: PropTypes.number}),
    fontSize: PropTypes.string
};

function Moradores() {

    const [open, setOpen] = useState(false);
    const [moradores, setMoradores] = useState([]);
    const [morador, setMorador] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);


    const handleNewMorador = async (morador) => {
        try {
            salvarMorador(morador);
            setOpen(false);
        }catch(error){
            console.error("erro ao salvar usuarios :", error);
        }finally{
            setLoading(true);

            const moradores = await fetchMoradores();
            const rows = moradores.map((row) => ({
                id: row.id,
                moradorId: row.guid,
                nome: row.nome,
                email: row.email,
                cpf: row.cpf,
                dataNascimento: row.dataNascimento,
                apartamento: row.apartamento.apartamento,
                bloco: row.apartamento.bloco
            }));

            setMoradores(rows);
            setLoading(false);
        }
    }

    const handlePesquisa = async (morador) => {
        try {
            setLoading(true);

            const moradores = await pesquisarMorador(morador);
            let rows = [];
            if( moradores ) {
                rows = moradores.map((row) => ({
                    id: row.id,
                    moradorId: row.guid,
                    nome: row.nome,
                    email: row.email,
                    cpf: row.cpf,
                    dataNascimento: row.dataNascimento,
                    apartamento: row.apartamento.apartamento,
                    bloco: row.apartamento.bloco
                }));
            }

            setMoradores(rows);

        }catch (error){
            console.error("erro ao buscar usuarios: ", error);
        }finally{
            setLoading(false);
        }
    }

    const handleExcluir = async (morador) => {
        if (window.confirm(`Tem certeza que deseja excluir o morador ${morador.nome}?`)) {
            try {
                const moradores = await excluirMorador(morador.moradorId);
                let rows = [];
                if( moradores ) {
                    rows = moradores.map((row) => ({
                        id: row.id,
                        moradorId: row.guid,
                        nome: row.nome,
                        email: row.email,
                        cpf: row.cpf,
                        dataNascimento: row.dataNascimento,
                        apartamento: row.apartamento.apartamento,
                        bloco: row.apartamento.bloco
                    }));
                }

                setMoradores(rows);
                alert("Morador excluído com sucesso!");
            } catch (error) {
                console.error("Erro ao excluir o morador:", error);
                alert("Erro ao excluir o morador.");
            }
        }
    };

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

        const getMoradores = async () => {
            try {
                setLoading(true);
                const moradores = await fetchMoradores();

                const rows = moradores.map((row) => ({
                   id: row.id,
                   moradorId: row.guid,
                   nome: row.nome,
                   email: row.email,
                   cpf: row.cpf,
                   dataNascimento: row.dataNascimento,
                   apartamento: row.apartamento.apartamento,
                   bloco: row.apartamento.bloco
                }));

                setMoradores(rows);
            } catch (error) {
                console.error("Erro ao buscar os usuários:", error);
            } finally {
                setLoading(false); // Finaliza o estado de carregamento
            }
        }

        getMoradores();

        return () => {
            axios.interceptors.response.eject(interceptor);
        };


    }, [navigate]);

    const handleEditar = async (morador) => {

        const moradorSelecionado =await getMorador(morador.moradorId);
        setMorador(moradorSelecionado);

        setOpen(true); // Abre o modal
    };

    const columns = [
        { field: 'id', headerName: 'ID', width: 10 },
        { field: 'nome', headerName: 'Nome', width: 200, sortable: true },
        { field: 'cpf', headerName: 'CPF', width: 150, sortable: true },
        { field: 'email', headerName: 'E-mail', width: 150, sortable: true},
        { field: 'dataNascimento', headerName: 'Data de Nascimento', width: 150, sortable: true },
        { field: 'bloco', headerName: 'Bloco', width: 150, sortable: true },
        { field: 'apartamento', headerName: 'Apartamento', width: 150, sortable: true },
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
                        Moradores
                    </Typography>
                </Breadcrumbs>

                <Formulario label={{ icon: <SearchIcon sx={{ mr: 1 }} />, text: "Pesquisar" }} showPasswordField={false} onSubmit={handlePesquisa}/>
                <div style={{ marginTop: 0 }}> {/* Contêiner para o DataGrid */}
                    <DataGrid
                        columns={columns}
                        rows={moradores}
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
            <MoradorModal open={open} onClose={handleClose} onSubmit={handleNewMorador} morador={morador}/>
        </BaseLayout>
    );
}

export default Moradores;
