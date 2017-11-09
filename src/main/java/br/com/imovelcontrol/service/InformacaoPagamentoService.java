package br.com.imovelcontrol.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.GastoAdicional;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.repository.GastosAdicionais;
import br.com.imovelcontrol.repository.InformacoesPagamentos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformacaoPagamentoService {

    @Autowired
    private InformacoesPagamentos informacaoPagamentos;

    @Autowired
    private GastosAdicionais gastosAdicionais;

    @Autowired
    private GastoAdicionalService gastosAdicionaisService;

    @Transactional
    public InformacaoPagamento salvar(InformacaoPagamento informacaoPagamento) {
        GastoAdicional gastoAdicional = informacaoPagamento.getGastoAdicional();
        if (informacaoPagamento.getCodigo() == null) {
            informacaoPagamento.setDataMensal(LocalDate.now());
            informacaoPagamento.setPago(Boolean.FALSE);
        } else {
            /*Logica para garantir que o sistema continue com os dados de forma integra, ou seja,
            * não permitir que o usuario burle o sistema com JavaScript. */
            InformacaoPagamento informacaoPagamentoRetrived = informacaoPagamentos.findOne(informacaoPagamento.getCodigo());
            caseTrueChangeValue(informacaoPagamento, informacaoPagamentoRetrived);
            informacaoPagamento.setDataMensal(informacaoPagamentoRetrived.getDataMensal());

        }
        InformacaoPagamento informacaoPagamentoRetrived = informacaoPagamentos.save(informacaoPagamento);
        if (gastoAdicional != null && gastoAdicional.getValorGasto() != null) {
            gastoAdicional.setInformacaoPagamento(informacaoPagamentoRetrived);
            gastoAdicional.setDataMensal(LocalDate.now());
            gastosAdicionais.save(gastoAdicional);
        }

        return informacaoPagamentoRetrived;
    }

    /**
     * Logica para manter as informacoes de pagamento igual o template de forma de pagamento gerado mensalmente.
     *
     * @param informacaoPagamento         Informação de pagamento provinda da tela.
     * @param informacaoPagamentoRetrived Informação de pagamento no banco de dados.
     */
    private void caseTrueChangeValue(InformacaoPagamento informacaoPagamento,
            InformacaoPagamento informacaoPagamentoRetrived) {

        if (informacaoPagamentoRetrived.getAguaInclusa() == null) {
            informacaoPagamento.setAguaInclusa(null);
        }
        if (informacaoPagamentoRetrived.getInternetInclusa() == null) {
            informacaoPagamento.setInternetInclusa(null);
        }
        if (informacaoPagamentoRetrived.getIptuIncluso() == null) {
            informacaoPagamento.setIptuIncluso(null);
        }
        if (informacaoPagamentoRetrived.getLuzInclusa() == null) {
            informacaoPagamento.setLuzInclusa(null);
        }
        if (informacaoPagamentoRetrived.getPossuiCondominio() == null) {
            informacaoPagamento.setPossuiCondominio(null);
        }
    }

    @Transactional
    public Optional<InformacaoPagamento> retrieveByAluguelAndData(String codigo) {
        Aluguel aluguel = new Aluguel();

        aluguel.setCodigo(Long.parseLong(codigo));
        Optional<List<InformacaoPagamento>> informacaoPagamento = informacaoPagamentos.findByAluguel(aluguel);

        return informacaoPagamento.get().stream().filter(pag -> pag.getDataMensal().getMonth()
                .equals(LocalDate.now().getMonth())).findAny();
    }

    @Transactional
    public Optional<List<InformacaoPagamento>> retrieveInforcamacoesPagamentoByAluguel(String codigo) {
        Aluguel aluguel = new Aluguel();

        aluguel.setCodigo(Long.parseLong(codigo));
        return informacaoPagamentos.findByAluguel(aluguel);

    }

    @Transactional
    public Optional<List<InformacaoPagamento>> findByAluguel(Aluguel aluguel) {
        return informacaoPagamentos.findByAluguel(aluguel);
    }

    @Transactional
    public Optional<List<InformacaoPagamento>> findByAluguelToGraficoBarrra(Aluguel aluguel) {
        Optional<List<InformacaoPagamento>> retorno = Optional.of(new ArrayList<>());
        List<InformacaoPagamento> informacaoPagamentoList = informacaoPagamentos.findByAluguel(aluguel).get();

        informacaoPagamentoList.forEach((InformacaoPagamento info) -> {
            Optional<List<GastoAdicional>> gastoAdicionalList = gastosAdicionaisService.findByInformaçãoPagamento(info);
            InformacaoPagamento informacaoPagamento = info;
            for (GastoAdicional gasto : gastoAdicionalList.get()) {
                informacaoPagamento.setValor(informacaoPagamento.getValor().add(gasto.getValorGasto()));
            }
            retorno.get().add(informacaoPagamento);
        });
        return retorno;
    }

    @Transactional
    public InformacaoPagamento retrieveById(String codigo) {
        return informacaoPagamentos.findOne(Long.parseLong(codigo));
    }

    public List<InformacaoPagamento> retrieveInformacaoPagamentoVencidoByAluguel(Long codigoAluguel) {
        return informacaoPagamentos.retrieveInformacaoPagamentoVencidoByAluguel(codigoAluguel);
    }
}
