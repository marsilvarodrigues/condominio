import React, { useState } from 'react';
import BaseLayout from '../template/baseUI.jsx';
import {
    Box,
    Typography,
    IconButton,
    Grid,
    Paper,
    Tooltip,
    Button,
    Breadcrumbs,
    Link, Container,
} from '@mui/material';
import { ArrowBack, ArrowForward, Event } from '@mui/icons-material';
import { format, addMonths, startOfMonth, endOfMonth, startOfWeek, endOfWeek, addDays, isSameDay, isBefore } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import HomeIcon from "@mui/icons-material/Home";
import Formulario from "./formulario.jsx";
import SearchIcon from "@mui/icons-material/Search";

const daysOfWeek = ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'];

const reservations = [
    { date: new Date(2024, 9, 28), space: 'Salão de Festa', responsible: 'João Silva' },
    { date: new Date(2024, 9, 30), space: 'Churrasqueira 1', responsible: 'Maria Souza' },
    { date: new Date(2024, 10, 1), space: 'Salão de Festa Infantil', responsible: 'Pedro Santos' },
    { date: new Date(2024, 10, 3), space: 'Churrasqueira 3', responsible: 'Ana Oliveira' },
];

const Reserva = () => {
    const [currentMonth, setCurrentMonth] = useState(new Date());

    const handleNextMonth = () => {
        setCurrentMonth((prev) => addMonths(prev, 1));
    };

    const handlePrevMonth = () => {
        setCurrentMonth((prev) => addMonths(prev, -1));
    };

    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart, { locale: ptBR });
    const endDate = endOfWeek(monthEnd, { locale: ptBR });

    const generateCalendarDays = () => {
        const days = [];
        let day = startDate;

        while (day <= endDate) {
            days.push(day);
            day = addDays(day, 1);
        }

        return days;
    };

    const days = generateCalendarDays();

    const getReservationForDay = (day) => {
        return reservations.find(reservation => isSameDay(day, reservation.date));
    };

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
                    paddingTop: 15, // Garante que não haja padding no topo
                }}
            >

                {/* Breadcrumb */}
                <Box marginBottom={2}>
                    <Breadcrumbs aria-label="breadcrumb">
                        <Link
                            underline="hover"
                            sx={{ display: 'flex', alignItems: 'center' }}
                            color="inherit"
                            href="/reservas"
                        >
                            <HomeIcon sx={{ mr: 0.5 }} fontSize="inherit" />
                            Reservas
                        </Link>
                    </Breadcrumbs>
                </Box>

                {/* Formulário de Pesquisa */}
                <Box marginBottom={4}>
                    <Formulario label={{ icon: <SearchIcon sx={{ mr: 1 }} />, text: "Pesquisar" }} />
                </Box>

                {/* Calendário */}
                <Box>
                    <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                        <IconButton onClick={handlePrevMonth}>
                            <ArrowBack />
                        </IconButton>
                        <Typography variant="h5">
                            {format(currentMonth, 'MMMM yyyy', { locale: ptBR })}
                        </Typography>
                        <IconButton onClick={handleNextMonth}>
                            <ArrowForward />
                        </IconButton>
                    </Box>

                    {/* Cabeçalho do calendário com dias da semana */}
                    <Grid container>
                        {daysOfWeek.map((day, index) => (
                            <Grid item xs={1.71} key={index}>
                                <Typography variant="subtitle1" align="center">
                                    {day}
                                </Typography>
                            </Grid>
                        ))}
                    </Grid>

                    {/* Corpo do calendário com os dias do mês */}
                    <Grid container>
                        {days.map((day, index) => {
                            const reservation = getReservationForDay(day);
                            const isTodayOrFuture = !isBefore(day, new Date());

                            return (
                                <Grid
                                    item
                                    xs={1.71}
                                    key={index}
                                    sx={{
                                        border: '1px solid #e0e0e0',
                                        minHeight: 80,
                                        backgroundColor: reservation
                                            ? isTodayOrFuture
                                                ? 'rgba(255, 0, 0, 0.2)'
                                                : '#d3d3d3'
                                            : 'white',
                                        display: 'flex',
                                        flexDirection: 'column',
                                        alignItems: 'center',
                                        padding: 1,
                                    }}
                                >
                                    <Typography variant="body2" color={format(day, 'MM') === format(currentMonth, 'MM') ? 'textPrimary' : 'textSecondary'}>
                                        {format(day, 'd')}
                                    </Typography>

                                    {reservation && (
                                        <Tooltip title={`${reservation.space} - ${reservation.responsible}`}>
                                            <Paper
                                                elevation={1}
                                                sx={{
                                                    marginTop: 1,
                                                    padding: '4px',
                                                    backgroundColor: isTodayOrFuture ? 'red' : '#9e9e9e',
                                                    color: 'white',
                                                    display: 'flex',
                                                    alignItems: 'center',
                                                    gap: '4px',
                                                }}
                                            >
                                                <Event fontSize="small" />
                                                <Typography variant="caption">
                                                    {reservation.space}
                                                </Typography>
                                            </Paper>
                                        </Tooltip>
                                    )}
                                </Grid>
                            );
                        })}
                    </Grid>
                </Box>

                {/* Botão de Adicionar Evento */}
                <Box display="flex" justifyContent="left" mt={2}>
                    <Button
                        variant="contained"
                        color="primary"
                        size="small"
                        sx={{ width: '180px', fontSize: '0.8rem' }}
                    >
                        + Adicionar Evento
                    </Button>
                </Box>
            </Container>
        </BaseLayout>
    );
};

export default Reserva;
