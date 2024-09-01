CREATE TABLE apartamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bloco_id BIGINT NOT NULL,
    guid VARCHAR(36) NOT NULL UNIQUE,
    numero VARCHAR(255) NOT NULL,
    andar INT NOT NULL,
    CONSTRAINT fk_bloco FOREIGN KEY (bloco_id)
        REFERENCES blocos(id)
);