CREATE TABLE perfils_usuarios (
    usuario_id UUID NOT NULL,
    perfil_id UUID NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    CONSTRAINT fk_usuarios
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id),
    CONSTRAINT fk_perfils
        FOREIGN KEY (perfil_id)
        REFERENCES perfils(id)
);