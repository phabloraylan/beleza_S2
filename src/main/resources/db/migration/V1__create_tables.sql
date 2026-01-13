CREATE TABLE tb_usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP
);

CREATE TABLE tb_estabelecimentos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    descricao TEXT,
    horario_funcionamento VARCHAR(255),
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP
);

CREATE TABLE estabelecimento_fotos (
    estabelecimento_id BIGINT NOT NULL,
    foto_url VARCHAR(255),
    FOREIGN KEY (estabelecimento_id) REFERENCES tb_estabelecimentos(id)
);

CREATE TABLE estabelecimento_servicos (
    estabelecimento_id BIGINT NOT NULL,
    servico VARCHAR(255),
    FOREIGN KEY (estabelecimento_id) REFERENCES tb_estabelecimentos(id)
);

CREATE TABLE tb_profissionais (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    especialidades VARCHAR(255),
    horarios_disponiveis VARCHAR(255),
    tarifa DOUBLE PRECISION,
    estabelecimento_id BIGINT,
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP,
    FOREIGN KEY (estabelecimento_id) REFERENCES tb_estabelecimentos(id)
);

CREATE TABLE tb_agendamentos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    estabelecimento_id BIGINT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(50),
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id),
    FOREIGN KEY (profissional_id) REFERENCES tb_profissionais(id),
    FOREIGN KEY (estabelecimento_id) REFERENCES tb_estabelecimentos(id)
);

CREATE TABLE tb_avaliacoes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    estabelecimento_id BIGINT,
    profissional_id BIGINT,
    nota INTEGER NOT NULL,
    comentario TEXT,
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id),
    FOREIGN KEY (estabelecimento_id) REFERENCES tb_estabelecimentos(id),
    FOREIGN KEY (profissional_id) REFERENCES tb_profissionais(id)
);
