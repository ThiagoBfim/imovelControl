package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.event.ImovelSalvoEvent;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroImovelService {

    @Autowired
    private Imoveis imoveis;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CadastroAluguelService cadastroAluguelService;

    @Autowired
    private Alugueis alugueis;

    @Transactional
    public Imovel salvar(Imovel imovel) {
        Optional<Imovel> imovelRetrieve = imoveis.findByCep(imovel.getEndereco().getCep(), imovel.getDonoImovel());

        if (imovelRetrieve.isPresent() && !imovelRetrieve.get().equals(imovel)) {
            throw new BusinessException("Já existe um imóvel cadastrado com este CEP", "endereco");
        } else {
            imovelRetrieve = imoveis.findByNomeAndDonoImovel(imovel.getNome(), imovel.getDonoImovel());
            if (imovelRetrieve.isPresent() && !imovelRetrieve.get().equals(imovel)) {
                throw new BusinessException("Já existe um imóvel cadastrado com este Nome", "nome");
            }
        }
        publisher.publishEvent(new ImovelSalvoEvent(imovel));
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
        Optional<List<Imovel>> imovels = imoveis.findByDonoImovel_Codigo(codigo);

        if (!imovels.isPresent()) {
            throw new BusinessException("Imóvel não encontrado", "imovel");
        }

        return imovels.get();
    }

}
