package br.com.imovelcontrol.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.GastoAdicional;
import br.com.imovelcontrol.model.InformacaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Thiago on 30/07/2017.
 */
@Repository
public interface GastosAdicionais extends JpaRepository<GastoAdicional, Long> {

    Optional<List<GastoAdicional>> findByInformacaoPagamento(InformacaoPagamento informacaoPagamento);
    Optional<List<GastoAdicional>> findByInformacaoPagamento_Codigo(Long codigo);
    Optional<List<GastoAdicional>> findByInformacaoPagamentoAndDataMensal(InformacaoPagamento informacaoPagamento, LocalDate dataMensal);

}
