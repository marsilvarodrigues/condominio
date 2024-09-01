CREATE TABLE espacos_comuns (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(36) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    capacidade_maxima INT NOT NULL,
    condominio_id BIGINT NOT NULL,
    UNIQUE KEY ux_guid_espaco_comum (guid),
    FOREIGN KEY (condominio_id) REFERENCES condominio(id)
);
