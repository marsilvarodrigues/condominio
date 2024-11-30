import axios from "axios";
import {formatarData, removeEmptyParams} from "../../libraries/utils.js";
import {fetchUsuarios} from "../usuarios/usuarioService.js";

export const fetchMoradores = async () => {
    try {

        const token = sessionStorage.getItem("auth_token")
        return await axios.get('http://localhost:5173/api/moradores', {
            withCredentials: true,
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
        .then((response) => {
            const moradores = response.data.map((morador) => ({
                ...morador,
                dataNascimento: formatarData(morador.dataNascimento) // Formata a data
            }));
            return moradores;


        }).catch((error) => {
            console.log(error)
        });
    } catch (error) {
        console.error("Erro ao buscar os moradores:", error);
    }
};

export const getMorador = async (id) => {
    const token = sessionStorage.getItem("auth_token");
    return await axios.get(`http://localhost:5173/api/moradores/${id}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }).then((response) => {
        const morador = response.data;
        morador.dataNascimento = formatarData(morador.dataNascimento);
        return morador;
    })
        .catch((error) => {
            console.log(error)
        });
}


export const pesquisarMorador = async(morador) => {
    const token = sessionStorage.getItem("auth_token");
    const params = removeEmptyParams(morador);

    return await axios.get(`http://localhost:5173/api/moradores`, {
        params: params,
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }).then((response) => {
        if( response && response.data ) {
            return response.data.map((morador) => ({
                ...morador,
                dataNascimento: formatarData(morador.dataNascimento) // Formata a data
            }));
        }else {
            console.error(response);
        }
    }).catch((error) => {
        console.error(error);
    });


}

export const salvarMorador = async(morador) => {
    const token = sessionStorage.getItem("auth_token");

    if( morador.moradorId ) {
        const response = await axios.put(`http://localhost:5173/api/moradores/${morador.moradorId}`, morador,{
            headers: {
                Authorization: `Bearer ${token}`,
            }
        }).catch((error) => {
                console.log(error)
            });

        console.log(response);
    } else {
        const response = await axios.post(`http://localhost:5173/api/moradores`, morador,{
            headers: {
                Authorization: `Bearer ${token}`,
            }
        }).catch((error) => {
                console.log(error)
            });
        console.log(response);
    }

}

export const excluirMorador = async(id) => {
    const token = sessionStorage.getItem("auth_token");
    await axios.delete(`http://localhost:5173/api/moradores/${id}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }).catch((error) => console.log(error));

    return pesquisarMorador({});
}
