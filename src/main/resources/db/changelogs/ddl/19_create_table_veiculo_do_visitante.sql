CREATE TABLE veiculo_do_visitante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid CHAR(36),
    modelo VARCHAR(100),
    cor VARCHAR(100),
    placa VARCHAR(10),
    visitante_id BIGINT NOT NULL,
    UNIQUE KEY uqx_guid_veiculo_do_visitante (guid),
    CONSTRAINT fk_carro_visitante FOREIGN KEY (visitante_id) REFERENCES visitantes(id)
);
