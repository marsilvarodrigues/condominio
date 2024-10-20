import { useState } from 'react';
import authService from './authentication/authService'; // ajuste o caminho conforme necessário

// Hook customizado para gerenciar a lógica do login
const useLogin = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState(''); // Estado para controlar a mensagem de erro
    const [showErrorPopup, setShowErrorPopup] = useState(false); // Estado para controlar o pop-up de erro

    // Manipuladores para mudanças nos inputs
    const handleUsernameChange = (e) => setUsername(e.target.value);
    const handlePasswordChange = (e) => setPassword(e.target.value);

    // Lógica de submissão
    const handleSubmit = async (e) => {
        e.preventDefault(); // Previne o comportamento padrão do formulário
        try {
            const token = await authService.login(username, password);
            console.log('Login bem-sucedido!', token);
            // Redirecionar ou fazer algo após o login bem-sucedido
        } catch (error) {
            // Mostrar o pop-up de erro
            setErrorMessage('Não foi possível efetuar a autenticação. Verifique suas credenciais.');
            setShowErrorPopup(true);
        }
    };

    const closeErrorPopup = () => {
        setShowErrorPopup(false);
        setErrorMessage('');
    };

    // Retorna os dados e funções necessárias para o componente
    return {
        username,
        password,
        handleUsernameChange,
        handlePasswordChange,
        handleSubmit,
        showErrorPopup,
        closeErrorPopup,
        errorMessage,
    };
};

export default useLogin;
