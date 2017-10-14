package br.com.imovelcontrol.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.GastoAdicional;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.FormasPagamentos;
import br.com.imovelcontrol.repository.GastosAdicionais;
import br.com.imovelcontrol.repository.InformacaoPagamentos;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroAluguelService {

    @Autowired
    private Alugueis alugueis;

    @Autowired
    private FormasPagamentos formasPagamentos;

    @Autowired
    private CadastroLocatarioService cadastroLocatarioService;

    @Autowired
    private GastosAdicionais gastosAdicionais;

    @Autowired
    InformacaoPagamentos informacaoPagamentos;

    @Transactional
    public Aluguel salvar(Aluguel aluguel) {
        Optional<Aluguel> usuarioRetrived = alugueis.findByNomeAndImovel_Codigo(aluguel.getNome(),
                aluguel.getImovel().getCodigo());
        if (usuarioRetrived.isPresent() && !usuarioRetrived.get().equals(aluguel)
                && usuarioRetrived.get().getImovel().equals(aluguel.getImovel())) {
            throw new BusinessException("Nome já cadastrado", "nome");
        }
        return alugueis.save(aluguel);
    }

    @Transactional
    public void excluirLogicamente(Aluguel aluguel) {

        cadastroLocatarioService.deleteByAluguel(aluguel);
        aluguel.setExcluido(Boolean.TRUE);
        alugueis.save(aluguel);
    }

    @Transactional
    public List<Aluguel> findByImovel(Long codigo) {
        Optional<List<Aluguel>> aluguels = alugueis.findByImovel_Codigo(codigo);

        if (!aluguels.isPresent()) {
            throw new BusinessException("Aluguéis não encontrados", "aluguel");
        }

        return aluguels.get();
    }




}
