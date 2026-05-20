CREATE TABLE progressos_trilha_modulos (
                                           progressos_id BIGINT NOT NULL,
                                           trilha_modulos BIGINT NOT NULL,
                                           CONSTRAINT fk_progressos_trilha_modulos_progressos FOREIGN KEY (progressos_id) REFERENCES progressos(id) ON DELETE CASCADE
);