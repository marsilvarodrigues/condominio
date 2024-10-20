const AUTH_URL = 'http://localhost:8080/api/auth';

const authService = {
    login: async (username, password) => {
        const params = new URLSearchParams();
        params.append('username', username);
        params.append('password', password);

        try {
            const response = await fetch(AUTH_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                credentials: "include",
                mode: 'cors',
                body: params.toString(),
                referrerPolicy: 'no-referrer-when-downgrade',
            });

            if (!response.ok) {
                throw new Error('Falha na autenticação');
            }

            const data = await response.json(); // Aguarde a resposta JSON
            const token = data.token; // Obtenha o token da resposta

            // Armazenar token no localStorage ou em outro lugar seguro
            localStorage.setItem('jwt_token', token);

            return token;
        } catch (error) {
            console.error('Erro ao autenticar:', error);
            throw error;
        }
    },

    // Função para renovar o token
    refreshToken: async () => {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
            window.location.href = '/'; // Ajuste o caminho conforme necessário
        }

        try {
            const response = await fetch(`${AUTH_URL}/refresh`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                window.location.href = '/'; // Ajuste o caminho conforme necessário

            }

            const data = await response.json();
            const newToken = data.token;

            // Atualizar o token no localStorage
            localStorage.setItem('jwt_token', newToken);

            return newToken;
        } catch (error) {
            console.error('Erro ao renovar o token:', error);
            window.location.href = '/'; // Ajuste o caminho conforme necessário
        }
    },

    // Função para fazer requisições autenticadas
    fetchWithAuth: async (url, options = {}) => {
        let token = localStorage.getItem('jwt_token');

        if (!token) {
            throw new Error('Usuário não autenticado');
        }

        try {
            const response = await fetch(url, {
                ...options,
                headers: {
                    ...options.headers,
                    'Authorization': `Bearer ${token}`,
                },
            });

            if (response.status === 401) {
                // Token expirou, tente renovar
                token = await authService.refreshToken();
                return fetch(url, {
                    ...options,
                    headers: {
                        ...options.headers,
                        'Authorization': `Bearer ${token}`,
                    },
                });
            }

            return response;
        } catch (error) {
            console.error('Erro ao fazer requisição:', error);
            throw error;
        }
    },
};

export default authService;
