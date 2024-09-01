CREATE TABLE perfis (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    guid CHAR(36) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    UNIQUE KEY uqx_guid_perfil (guid)
);