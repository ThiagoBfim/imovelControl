ALTER TABLE IMOVEL ADD  excluido BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE ALUGUEL ADD excluido BOOLEAN NOT NULL  DEFAULT FALSE;

ALTER TABLE locatario ADD excluido BOOLEAN NOT NULL  DEFAULT FALSE;
