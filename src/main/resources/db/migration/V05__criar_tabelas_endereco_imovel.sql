CREATE TABLE IMOVEL (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  cep CHAR(9) NOT NULL,
  cidade VARCHAR(100) NOT NULL,
  bairro VARCHAR(100) NOT NULL,
  rua VARCHAR(100) NOT NULL,
  uf CHAR(2) NOT NULL,
  complemento varchar(150),
  foto VARCHAR(100),
  content_type VARCHAR(100)
)

