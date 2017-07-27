package br.com.imovelcontrol.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.event.ImovelSalvoEvent;
import br.com.imovelcontrol.service.exception.CepImovelJaCadastradoException;
import br.com.imovelcontrol.service.exception.NomeImovelJaCadastradoException;
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

    @Autowired Alugueis alugueis;

    @Transactional
    public Imovel salvar(Imovel imovel) {
        Optional<Imovel> imovelRetrieve = imoveis.findByCep(imovel.getEndereco().getCep(), imovel.getDonoImovel());

        if (imovelRetrieve.isPresent() && !imovelRetrieve.get().equals(imovel)) {
            throw new CepImovelJaCadastradoException("Já existe um imóvel cadastrado com este CEP");
        } else {
            imovelRetrieve = imoveis.findByNomeAndDonoImovel(imovel.getNome(), imovel.getDonoImovel());
            if (imovelRetrieve.isPresent() && !imovelRetrieve.get().equals(imovel)) {
                throw new NomeImovelJaCadastradoException("Já existe um imóvel cadastrado com este Nome");
            }
        }
        publisher.publishEvent(new ImovelSalvoEvent(imovel));
        return imoveis.save(imovel);
    }


    @Transactional
    public void excluir(Imovel imovel) {
        Optional<List<Aluguel>> aluguel;
        aluguel = alugueis.findByImovel_Codigo(imovel.getCodigo());
        if (aluguel.isPresent()){
            for (Aluguel item:aluguel.get()) {
                cadastroAluguelService.excluir(item);
            }
        }
        imoveis.delete(imovel);
    }
}
