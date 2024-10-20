import React from 'react';
import useLogin from './useLogin'; // ajuste o caminho conforme necessário
import Popup from './messages/Popup'; // ajuste o caminho conforme necessário

const LoginForm = () => {
    const {
        username,
        password,
        handleUsernameChange,
        handlePasswordChange,
        handleSubmit,
        showErrorPopup,
        closeErrorPopup,
        errorMessage,
    } = useLogin(); // Utiliza o hook useLogin

    return (
        <div style={styles.container}>
            <h2 style={styles.title}>Login</h2>
            <form onSubmit={handleSubmit} style={styles.form}>
                <div style={styles.inputContainer}>
                    <label htmlFor="username" style={styles.label}>Nome de Usuário:</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={handleUsernameChange}
                        style={styles.input}
                        required
                    />
                </div>
                <div style={styles.inputContainer}>
                    <label htmlFor="password" style={styles.label}>Senha:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={handlePasswordChange}
                        style={styles.input}
                        required
                    />
                </div>
                <button type="submit" style={styles.button}>Entrar</button>
            </form>
            {showErrorPopup && (
                <Popup message={errorMessage} onClose={closeErrorPopup} />
            )}
        </div>
    );
};

const styles = {
    container: {
        width: '300px',
        margin: '100px auto',
        padding: '20px',
        borderRadius: '8px',
        backgroundColor: '#f4f4f4',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    },
    title: {
        textAlign: 'center',
        marginBottom: '20px',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
    },
    inputContainer: {
        marginBottom: '15px',
    },
    label: {
        marginBottom: '5px',
        fontSize: '14px',
    },
    input: {
        padding: '10px',
        fontSize: '16px',
        width: '100%',
        boxSizing: 'border-box',
    },
    button: {
        padding: '10px',
        fontSize: '16px',
        backgroundColor: '#007BFF',
        color: '#fff',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
    },
};

export default LoginForm;