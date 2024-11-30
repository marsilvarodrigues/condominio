import React, { useEffect, useState } from "react";
import {
    Container,
    FormControl,
    Grid,
    InputLabel,
    MenuItem,
    Select,
    TextField,
    Button,
    Autocomplete,
} from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";
import { fetchApartamentos, fetchBlocos } from "../../libraries/apiService.js";
import {salvarMorador} from "./moradorService.js";

export default function Formulario({ label, showPasswordField, morador, onSubmit }) {
    const [dataNascimento, setDataNascimento] = useState(null);
    const [email, setEmail] = useState("");
    const [apartamento, setApartamento] = useState("");
    const [apartamentos, setApartamentos] = useState([]);
    const [nome, setNome] = useState("");
    const [cpf, setCPF] = useState("");
    const [bloco, setBloco] = useState(""); // Bloco inicializado como string vazia
    const [password, setPassword] = useState("");
    const [moradorId, setMoradorId] = useState("");
    const [apartamentoId, setApartamentoId] = useState("");
    const [blocos, setBlocos] = useState([]);

    const atualizaApartamentoAutoComplete = async (bloco, apartamento) => {
        if( bloco && apartamento ) {
            try {
                const apartamentosData = await fetchApartamentos(bloco, apartamento);
                setApartamentos(apartamentosData);
            } catch (error) {
                console.error("Erro ao buscar apartamentos:", error);
            }
        }else if( bloco ){
            try {
                const apartamentosData = await fetchApartamentos(bloco, null);
                setApartamentos(apartamentosData);
            } catch (error) {
                console.error("Erro ao buscar apartamentos:", error);
            }
        }
    };

    // Busca blocos apenas uma vez
    useEffect(() => {
        const fetchBlocosData = async () => {
            try {
                const data = await fetchBlocos();
                console.log("Blocos carregados:", data); // Debug
                setBlocos(data || []);
            } catch (error) {
                console.error("Erro ao buscar blocos:", error);
            }
        };

        fetchBlocosData();


        if (morador) {

            setBloco(morador.apartamento.blocoId); // Certifique-se de que é o `id`

            atualizaApartamentoAutoComplete(morador.apartamento.blocoId, morador.apartamento.apartamento);

            setDataNascimento(dayjs(morador.dataNascimento, "DD-MM-YYYY"));
            setNome(morador.nome);
            setCPF(morador.cpf);
            setEmail(morador.email);
            setApartamento(morador.apartamento.apartamento);
            setMoradorId(morador.guid || "");
            setApartamentoId(morador.apartamento?.apartamentoId || "");


        }

    }, [morador]);

    const handleSubmit = (event) => {
        event.preventDefault();
        const morador = {
            moradorId: moradorId,
            nome: nome,
            cpf: cpf,
            email: email,
            dataNascimento: dataNascimento?.toISOString(),
            apartamento: apartamentoId,
            bloco: bloco
        };
        onSubmit(morador);
    };

    return (
        <Container
            disableGutters
            maxWidth="100%"
            sx={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "flex-start",
                paddingBottom: 2,
            }}
        >
            <Grid container spacing={3}>
                {/* Primeira linha: Nome, CPF, Data de Nascimento */}
                <Grid item xs={4}>
                    <TextField
                        label="Nome"
                        variant="outlined"
                        fullWidth
                        value={nome}
                        onChange={(event) => setNome(event.target.value)}
                    />
                </Grid>
                <Grid item xs={4}>
                    <TextField
                        label="CPF"
                        variant="outlined"
                        fullWidth
                        value={cpf}
                        onChange={(event) => setCPF(event.target.value)}
                    />
                </Grid>
                <Grid item xs={4}>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            label="Data de nascimento"
                            value={dataNascimento}
                            onChange={(newValue) => setDataNascimento(newValue)}
                            renderInput={(params) => <TextField {...params} fullWidth />}
                        />
                    </LocalizationProvider>
                </Grid>

                {/* Segunda linha: Bloco e Apartamento */}
                <Grid item xs={6}>
                    <FormControl fullWidth>
                        <InputLabel id="bloco-select-label">Bloco</InputLabel>
                        <Select
                            labelId="bloco-select-label"
                            value={bloco} // Certifique-se de que o valor é o `id` do bloco
                            onChange={(event) => {
                                const selectedBloco = event.target.value;
                                console.log("Bloco selecionado:", selectedBloco); // Debug
                                setBloco(selectedBloco);
                                setApartamentos([]); // Reseta os apartamentos ao mudar o bloco
                            }}
                        >
                            <MenuItem key="" value="" >
                                Selecione o Bloco
                            </MenuItem>
                            {blocos.map((blocoItem) => (
                                <MenuItem key={blocoItem.blocoId} value={blocoItem.blocoId}>
                                    {blocoItem.nome}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item xs={6}>
                    <Autocomplete
                        freeSolo
                        options={apartamentos}
                        getOptionLabel={(option) => option.
                            apartamento || ""}
                        onInputChange={async (event, newInputValue) => {
                            if( newInputValue === "" ){
                                setApartamentos([]);
                                setApartamentoId("")
                                setApartamento("");
                                await atualizaApartamentoAutoComplete(bloco, newInputValue);
                            } else {
                                setApartamentoId("")
                                setApartamento("");
                                await atualizaApartamentoAutoComplete(bloco, newInputValue);
                            }
                        }}
                        onChange={(event, newValue) => {
                            if (newValue && typeof newValue === "object") {
                                // Quando um apartamento for selecionado
                                setApartamentoId(newValue.apartamentoId || "");
                                setApartamento(newValue.apartamento); // Atualiza o apartamento selecionado
                            } else {
                                // Quando o valor for limpo
                                setApartamento(""); // Limpa o valor do apartamento
                                setApartamentoId(""); // Limpa o ID do apartamento
                            }
                        }}
                        value={
                            apartamentos.find((ap) => ap.apartamento === apartamento) || null
                        }
                        renderInput={(params) => (
                            <TextField {...params} label="Apartamento" variant="outlined" fullWidth />
                        )}
                    />
                </Grid>

                {/* Terceira linha: Email */}
                <Grid item xs={12}>
                    <TextField
                        label="E-mail"
                        variant="outlined"
                        fullWidth
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                    />
                </Grid>

                {/* Quarta linha: Senha */}
                {showPasswordField && (
                    <Grid item xs={6}>
                        <TextField
                            label="Senha"
                            type="password"
                            variant="outlined"
                            fullWidth
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                        />
                    </Grid>
                )}
            </Grid>

            {/* Botão */}
            <Button
                variant="contained"
                color="primary"
                size="small"
                onClick={handleSubmit}
                sx={{
                    marginTop: 2,
                    padding: "6px 12px",
                    fontSize: "0.8rem",
                    width: "150px",
                }}
            >
                {label.icon}
                {label.text}
            </Button>
        </Container>
    );
}
