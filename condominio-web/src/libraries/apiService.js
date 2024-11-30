import axios from "axios";

export const fetchPerfis = async () => {
    try {
        const token = sessionStorage.getItem("auth_token"); // Obtém o token de autenticação
        const response = await axios.get("http://localhost:5173/api/perfis", {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        // Retorna os dados da resposta
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar perfis:", error);
        throw error; // Lança o erro para ser tratado no componente chamador
    }
};

export const fetchBlocos = async () => {
    try {
        const token = sessionStorage.getItem("auth_token"); // Obtém o token de autenticação
        const response = await axios.get("http://localhost:5173/api/blocos", {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        // Retorna os dados da resposta
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar blocos:", error);
        throw error; // Lança o erro para ser tratado no componente chamador
    }
}

export const fetchApartamentos = async (bloco, numero) => {

    try {

        const url = numero ? `http://localhost:5173/api/apartamentos/${bloco}/${numero}` :`http://localhost:5173/api/apartamentos/${bloco}`

        const token = sessionStorage.getItem("auth_token"); // Obtém o token de autenticação
        return await axios.get(url, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }).catch((error) => console.error(error))
        .then((response) => {
            console.log(response.data);
            return response.data
        });

    } catch (error) {
        console.error("Erro ao buscar apartamentos:", error);
        throw error; // Lança o erro para ser tratado no componente chamador
    }
}
