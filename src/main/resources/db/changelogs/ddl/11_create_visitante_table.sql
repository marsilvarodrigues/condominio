CREATE TABLE visitantes (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    guid CHAR(36) NOT NULL,
    nome_do_visitante VARCHAR(255) NOT NULL,
    apartamento_id BIGINT NOT NULL,
    data_visita TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    UNIQUE KEY ux_guid_visitante (guid),
    FOREIGN KEY (apartamento_id) REFERENCES apartamentos(id)

);