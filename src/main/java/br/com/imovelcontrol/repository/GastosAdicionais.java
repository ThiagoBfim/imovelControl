package br.com.imovelcontrol.repository;

import br.com.imovelcontrol.model.GastoAdicional;
import br.com.imovelcontrol.model.InformacaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Usuario on 30/07/2017.
 */
@Repository
public interface GastosAdicionais extends JpaRepository<GastoAdicional, Long> {

    Optional<List<GastoAdicional>> findByInformacaoPagamento(InformacaoPagamento informacaoPagamento);

}
