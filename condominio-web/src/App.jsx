import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginForm from './modules/login/login.jsx';
import Usuarios from "./modules/usuarios/usuarios.jsx";
import Visitantes from "./modules/visitantes/visitantes.jsx";
import Reserva from "./modules/reservas/reservas.jsx";
import Moradores from "./modules/moradores/moradores.jsx";

const ProtectedRoute = ({ children }) => {
    const token = sessionStorage.getItem('auth_token');
    if (!token) {
        return <Navigate to="/auth" replace />;
    }
    return children;
};


const isAuthenticated = () => {
    const token = sessionStorage.getItem("auth_token");
    return (token != null && token !== '');
}

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={
                    <ProtectedRoute>
                        <Reserva />
                    </ProtectedRoute>} />
                <Route path="/auth" element={<LoginForm />} />
                <Route path="/usuarios" element={
                    <ProtectedRoute>
                        <Usuarios />
                    </ProtectedRoute>
                    } />
                <Route path="/visitantes" element={
                    <ProtectedRoute>
                        <Visitantes />
                    </ProtectedRoute>
                } />
                <Route path="/reservas" element={
                    <ProtectedRoute>
                        <Reserva />
                    </ProtectedRoute>
                    } />

                <Route path="/moradores" element={
                    <ProtectedRoute>
                        <Moradores />
                    </ProtectedRoute>
                } />

            </Routes>
        </Router>
    );
}

export default App;
