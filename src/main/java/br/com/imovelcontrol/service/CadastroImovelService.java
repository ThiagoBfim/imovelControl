package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroImovelService {

    private static final Logger logger = LoggerFactory.getLogger(CadastroImovelService.class);

    @Autowired
    private Imoveis imoveis;

    @Autowired
    private CadastroAluguelService cadastroAluguelService;

    @Transactional
    public Imovel salvar(Imovel imovel) {
        validarBeforeSave(imovel);
        return imoveis.save(imovel);
    }

    @Transactional
    public void excluirLogicamente(Imovel imovel) {
        List<Aluguel> aluguel;
        aluguel = cadastroAluguelService.findByImovel(imovel.getCodigo());

        for (Aluguel item : aluguel) {
            cadastroAluguelService.excluirLogicamente(item);
        }
        imovel.setExcluido(Boolean.TRUE);
        imoveis.save(imovel);
    }

    @Transactional
    public List<Imovel> findByDonoImovel(Long codigo) {
        Optional<List<Imovel>> imovels = imoveis.findByDonoImovel_CodigoAndExcluido(codigo, Boolean.FALSE);

        if (!imovels.isPresent()) {
            throw new BusinessException("Imóvel não encontrado", "imovel");
        }

        return imovels.get();
    }

    @Transactional
    public void reativar(Imovel imovel) {
        validarBeforeSave(imovel);
        imovel.getAluguelList().forEach(a -> {
            a.setExcluido(Boolean.FALSE);
            cadastroAluguelService.salvar(a);
        });
        imoveis.save(imovel);
    }

    private void validarBeforeSave(Imovel imovel) {
        Optional<Imovel> imovelRetrieve = imoveis.findByNomeAndDonoImovelAndExcluido(imovel.getNome().trim(), imovel.getDonoImovel(),
                Boolean.FALSE);
        if (imovelRetrieve.isPresent() && !imovelRetrieve.get().equals(imovel)) {
            throw new BusinessException("Já existe um imóvel cadastrado com este nome", "nome");
        }

        /*Caso já possua um ID e esse ID não seja do usuario logado, então significa que alguem está
        tentanto burlar o sistema, nesse caso será retornando uma mensagem.*/
        if (imovel.getCodigo() != null
                && !imovel.getDonoImovel().equals(imoveis.getOne(imovel.getCodigo()).getDonoImovel())) {
            BusinessException exception = new BusinessException("Ocorreu um erro, aparentemente já existe um imovel igual no nosso sistema. "
                    + "Favor entrar em contato com a equipe do ImovelControl", null);
            logger.error("Tentativa de burlar o sistema", exception);
            throw exception;
        }
    }
}
