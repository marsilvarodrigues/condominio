CREATE TABLE condominio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(36) NOT NULL UNIQUE,
    nome_do_condominio VARCHAR(255) NOT NULL,
    quantidade_de_blocos BIGINT NOT NULL
);