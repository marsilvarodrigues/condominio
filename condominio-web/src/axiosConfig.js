import axios from "axios";

export function configureAxiosInterceptors(navigate) {
    axios.interceptors.response.use(
        (response) => response,
        (error) => {
            if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                console.error("Sessão expirada ou acesso negado. Redirecionando para o login...");
                navigate("/login");
            }
            return Promise.reject(error);
        }
    );
}
