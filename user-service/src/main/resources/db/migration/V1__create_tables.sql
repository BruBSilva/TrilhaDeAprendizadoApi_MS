CREATE TYPE role AS ENUM ('ADMIN', 'ALUNO');

CREATE TABLE usuarios (
                          id BIGINT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          senha_hash VARCHAR(40) NOT NULL,
                          role role NOT NULL
);

CREATE TABLE alunos (
                        id BIGINT PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE,
                        xp_total INTEGER NOT NULL,
                        nivel INTEGER NOT NULL,
                        data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE administradores (
                        id BIGINT PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE
);
