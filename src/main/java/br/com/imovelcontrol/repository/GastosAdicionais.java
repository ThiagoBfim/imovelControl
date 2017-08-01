package br.com.imovelcontrol.repository;

import br.com.imovelcontrol.model.GastoAdicional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Usuario on 30/07/2017.
 */
@Repository
public interface GastosAdicionais extends JpaRepository<GastoAdicional, Long> {
}
