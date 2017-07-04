ALTER TABLE informacao_pagamento
ADD codigo_aluguel BIGINT(20) NOT NULL DEFAULT 1;

ALTER TABLE informacao_pagamento
ADD CONSTRAINT fk_aluguel_informacao_pagamento   FOREIGN KEY (codigo_aluguel) REFERENCES aluguel(codigo);

