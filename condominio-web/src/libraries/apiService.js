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
