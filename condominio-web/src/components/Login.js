import React from 'react';
import LoginForm from './LoginForm';
import Popup from './messages/Popup';
import useLogin from './useLogin'; // ajuste o caminho conforme necessário
import estilo from './estilo.css'
const Login = () => {
    const {
        username,
        password,
        handleUsernameChange,
        handlePasswordChange,
        handleSubmit,
        errorMessage,
        showErrorPopup,
        closeErrorPopup,
    } = useLogin();

    return (
        <div>
            <LoginForm
                username={username}
                password={password}
                onUsernameChange={handleUsernameChange}
                onPasswordChange={handlePasswordChange}
                onSubmit={handleSubmit}
            />
            {showErrorPopup && (
                <Popup message={errorMessage} onClose={closeErrorPopup} />
            )}
        </div>
    );
};

export default Login;
