CREATE TABLE FORMA_PAGAMENTO (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    valor DECIMAL(10, 2) NOT NULL,
	aguaInclusa BOOLEAN,
	luzInclusa BOOLEAN,
	internetInclusa BOOLEAN,
	iptuIncluso BOOLEAN,
	possuiCondominio BOOLEAN,
    pago BOOLEAN
    
)
