ALTER TABLE visitantes
   ADD COLUMN autorizado_por_id BIGINT NOT NULL,
   ADD COLUMN registrado_por_id BIGINT NOT NULL,
   ADD CONSTRAINT fk_visita_registrado_por_id
      FOREIGN KEY (registrado_por_id) REFERENCES usuarios(id),
   ADD CONSTRAINT fk_visita_autorizado_por_id
      FOREIGN KEY (autorizado_por_id) REFERENCES morador(id)
