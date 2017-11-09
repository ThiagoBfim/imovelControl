package br.com.imovelcontrol.repository.helper.informacaopagamento;

import java.util.List;

import br.com.imovelcontrol.model.InformacaoPagamento;

/**
 * Created by Usuario on 08/11/2017.
 */
public interface InformacoesPagamentosQuerys {

    List<InformacaoPagamento> retrieveInformacaoPagamentoVencidoByAluguel(Long codigoAluguel);

}
