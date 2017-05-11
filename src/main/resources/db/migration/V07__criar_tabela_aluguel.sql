CREATE TABLE ALUGUEL (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	complemento VARCHAR(200),
	nome VARCHAR(200),
    codigo_imovel BIGINT(20) NOT NULL,
    codigo_forma_pagamento BIGINT(20) NOT NULL,
    quantidade_comodos INTEGER,
    tamanhoArea INTEGER,
    tipo_forro varchar(30),
    tipo_piso varchar(30) ,
    tipo_imovel varchar(50) NOT NULL,
    quantidade_banheiros int,
    quantidade_quartos int,
    vagas_garagem int,
    suites int,
    tipo CHAR(2),
    FOREIGN KEY (codigo_imovel) REFERENCES IMOVEL(codigo),
    FOREIGN KEY (codigo_forma_pagamento) REFERENCES TEMPLATE_FORMA_PAGAMENTO(codigo)
    
)