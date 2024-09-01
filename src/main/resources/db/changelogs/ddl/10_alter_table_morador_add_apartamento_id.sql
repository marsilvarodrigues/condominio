ALTER TABLE morador
    ADD COLUMN apartamento_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_morador_apartamento
        FOREIGN KEY (apartamento_id) REFERENCES apartamentos(id);