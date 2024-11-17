import axios from "axios";
import {formatarData, removeEmptyParams} from "../../libraries/utils.js";

export const fetchUsuarios = async () => {
    try {

        const token = sessionStorage.getItem("auth_token")
        return await axios.get('http://localhost:5173/api/usuarios', {
            withCredentials: true,
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
            .then((response) => {
                const usuariosFormatados = response.data.map((usuario) => ({
                    ...usuario,
                    dataCriacao: formatarData(usuario.dataCriacao), // Formata a data
                    perfis: [usuario.perfis.map((perfil) => perfil.nome)]
                }));
                return usuariosFormatados;
            });
    } catch (error) {
        console.error("Erro ao buscar os usuários:", error);
    }
};

export const getUsuario = async (id) => {
    const token = sessionStorage.getItem("auth_token");
    return await axios.get(`http://localhost:5173/api/usuarios/${id}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }).then((response) => {
        const usuario = response.data;
        usuario.dataCriacao = formatarData(usuario.dataCriacao);
        usuario.perfis = usuario.perfis.map((perfil) => ({
            id: perfil.id,
            nome: perfil.nome,
        }));
        return usuario;
    })
        .catch((error) => {
            console.log(error)
        });
}

export const excluirUsuario = async(id) => {
    const token = sessionStorage.getItem("auth_token");
    await axios.delete(`http://localhost:5173/api/usuarios/${id}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }).then((response) => {
        return fetchUsuarios();
    })
    .catch((error) => {
            console.log(error)
        });
}

export const pesquisarUsuario = async(usuario) => {
    const token = sessionStorage.getItem("auth_token");
    const params = removeEmptyParams(usuario);

    const response = await axios.get(`http://localhost:5173/api/usuarios`, {
        params: params,
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    console.log(response.status)

    if( response && response.data ) {
        const usuariosFormatados = response.data.map((usuario) => ({
            ...usuario,
            dataCriacao: formatarData(usuario.dataCriacao), // Formata a data
            perfis: [usuario.perfis.map((perfil) => perfil.nome)],
        }));
        return usuariosFormatados;
    }else {
        console.error(response);
    }
}

export const salvarUsuario = async(usuario) => {
    const token = sessionStorage.getItem("auth_token");

    if( usuario.usuarioId ) {
        const response = await axios.put(`http://localhost:5173/api/usuarios/${usuario.usuarioId}`, usuario,{
            headers: {
                Authorization: `Bearer ${token}`,
            }
        }).catch((error) => {
                console.log(error)
            });

        console.log(response);
    } else {
        const response = await axios.post(`http://localhost:5173/api/usuarios`, usuario,{
            headers: {
                Authorization: `Bearer ${token}`,
            }
        }).catch((error) => {
                console.log(error)
            });
        console.log(response);
    }

}