ALTER TABLE locatario
ADD codigo_aluguel BIGINT(20) NOT NULL DEFAULT 1;

ALTER TABLE locatario
ADD CONSTRAINT fk_alguel_locatario   FOREIGN KEY (codigo_aluguel) REFERENCES aluguel(codigo);

