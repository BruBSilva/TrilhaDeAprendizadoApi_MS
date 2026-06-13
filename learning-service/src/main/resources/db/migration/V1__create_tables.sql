CREATE TABLE progressos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    trilha_id BIGINT NOT NULL,
    data_inicio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_conclusao TIMESTAMP,
    xp_ganho INTEGER NOT NULL,
    percentual DOUBLE PRECISION NOT NULL,
    modulo_atual_id BIGINT NOT NULL,
    status_progresso VARCHAR(100) NOT NULL
);

CREATE TABLE usuario_conquistas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    conquista_id BIGINT NOT NULL,

    conquista_nome VARCHAR(150) NOT NULL,
    conquista_descricao TEXT NOT NULL,
    conquista_tipo VARCHAR(100) NOT NULL,
    conquista_modulo VARCHAR(150),
    conquista_trilha VARCHAR(150),
    conquista_xp_ganho INTEGER NOT NULL,

    data_conquista TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
