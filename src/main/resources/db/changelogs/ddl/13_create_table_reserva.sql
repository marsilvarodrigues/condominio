CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid CHAR(36) NOT NULL,
    espaco_comum_id BIGINT NOT NULL,
    morador_id BIGINT NOT NULL,
    data_reserva DATE NOT NULL,
    status_reserva INT DEFAULT 0,
    FOREIGN KEY (espaco_comum_id) REFERENCES espacos_comuns(id),
    FOREIGN KEY (morador_id) REFERENCES morador(id),
    UNIQUE KEY uqx_guid_reserva (guid)
);