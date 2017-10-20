package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.FormasPagamentos;
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

    @Transactional
    public Aluguel salvar(Aluguel aluguel) {
        Optional<Aluguel> aluguelRetrived = alugueis.findByNomeAndImovel_Codigo(aluguel.getNome(),
                aluguel.getImovel().getCodigo());
        if (aluguelRetrived.isPresent() && !aluguelRetrived.get().equals(aluguel)
                && aluguelRetrived.get().getImovel().equals(aluguel.getImovel())) {
            throw new BusinessException("Nome já cadastrado", "nome");
        }
        if (aluguel.getCodigo() != null) {
            Aluguel aluguelSameId = alugueis.getOne(aluguel.getCodigo());
             /*Caso já possua um ID e esse ID não seja de outro imovel, então significa que alguem está
             tentanto burlar o sistema, nesse caso será retornando uma mensagem.*/
            if (!aluguel.getImovel().equals(aluguelSameId.getImovel())) {
                throw new BusinessException("Ocorreu um erro, aparentemente já existe um aluguel igual no nosso sistema. "
                        + "Favor entrar em contato com a equipe do ImovelControl", null);
            }
            aluguel.getFormaPagamento().setCodigo(alugueis.getOne(aluguel.getCodigo()).getFormaPagamento().getCodigo());
            formasPagamentos.save(aluguel.getFormaPagamento());

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
