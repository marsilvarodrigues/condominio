CREATE TABLE morador (
    id BIGINT NOT NULL PRIMARY KEY,
    nome_do_morador VARCHAR(255) NOT NULL,
    data_de_nascimento DATE NOT NULL,
    email VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    FOREIGN KEY (id) REFERENCES usuarios(id),
    UNIQUE KEY ux_email_morador (email),
    UNIQUE KEY ux_cpf_morador (cpf)

);