CREATE TABLE telefones (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    guid CHAR(36) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    morador_id BIGINT NOT NULL,
    UNIQUE KEY ux_guid_telefone (guid),
    FOREIGN KEY (morador_id) REFERENCES morador(id)
);