/*Desativar um modo de segurança do mysql que não permite UPDATE sem WHERE*/
SET SQL_SAFE_UPDATES = 0;

ALTER TABLE INFORMACAO_PAGAMENTO ADD  dataPagamento DATE;

ALTER TABLE INFORMACAO_PAGAMENTO ADD  multa BIGINT(20) NOT NULL DEFAULT 0;

ALTER TABLE INFORMACAO_PAGAMENTO ADD  atrasado BOOLEAN NOT NULL DEFAULT FALSE ;


Update INFORMACAO_PAGAMENTO info set dataPagamento = dataMensal;

