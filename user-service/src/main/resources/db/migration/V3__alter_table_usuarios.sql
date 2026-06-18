-- Passo 1: Criar a sequência (caso ainda não exista)
CREATE SEQUENCE IF NOT EXISTS usuarios_id_seq;

-- Passo 2: Configurar a coluna para usar a sequência
ALTER TABLE usuarios
    ALTER COLUMN id SET DEFAULT nextval('usuarios_id_seq');

-- Passo 3: Garantir que a coluna seja NOT NULL
ALTER TABLE usuarios
    ALTER COLUMN id SET NOT NULL;

-- Passo 4 (opcional, mas recomendado): Garantir que a sequência esteja vinculada corretamente
ALTER SEQUENCE usuarios_id_seq OWNED BY usuarios.id;
