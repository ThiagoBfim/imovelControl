CREATE TABLE IMOVEL (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
	cep VARCHAR(20) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    estado varchar(30) NOT NULL,
    complemento varchar(150),
    foto VARCHAR(100),
    content_type VARCHAR(100)
)

