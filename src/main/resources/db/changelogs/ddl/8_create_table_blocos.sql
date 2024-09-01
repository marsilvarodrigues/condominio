CREATE TABLE blocos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(36) NOT NULL UNIQUE,
    nome_do_bloco VARCHAR(255) NOT NULL,
    numero_do_bloco VARCHAR(255) NOT NULL,
    quantidade_apartamento_bloco BIGINT NOT NULL,
    condominio_id BIGINT NOT NULL,
    CONSTRAINT fk_condominio FOREIGN KEY (condominio_id)
        REFERENCES condominio(id)
);