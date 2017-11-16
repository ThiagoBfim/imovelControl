package br.com.imovelcontrol.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Created by marcosfellipec on 18/05/17.
 */

@Service
public class CadastroLocatarioService {

    @Autowired
    private Locatarios locatarios;

    @Transactional
    public Locatario salvar(Locatario locatario) {
        if (locatario.getCodigo() != null) {
            Optional<Locatario> locatarioRetrived = locatarios
                    .findByAluguelAndExcluido(locatario.getAluguel(), Boolean.FALSE);
            if (locatarioRetrived.isPresent()) {
                locatario.setCodigo(locatarioRetrived.get().getCodigo());
            }
        }
        if (locatario.getDataInicioJson() != null) {
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formater.withLocale(new Locale("pt", "BR"));
            LocalDate date = LocalDate.parse(locatario.getDataInicioJson(), formater);
            locatario.setDataInicio(date);
            LocalDate dataAtual = LocalDate.now();
            if(date.isAfter(dataAtual)){
                throw new BusinessException("Datá de locação inválida! Não é aceita data maior que a data atual.", "data");
            }
            List<Locatario> locatarioList = locatarios
                    .findByDataFimGreaterThanAndAluguel(locatario.getDataInicio(), locatario.getAluguel());
            if (!CollectionUtils.isEmpty(locatarioList) || date.getMonthValue() < dataAtual.getMonthValue()) {
                throw new BusinessException("Datá de locação inválida, favor colocar um data mais recente.", "data");
            }
        } else {
            throw new BusinessException("Datá de locação inválida, favor colocar um data mais recente.", "data");
        }
        return locatarios.save(locatario);
    }

    @Transactional
    public void excluirLogicamente(Locatario locatario) {
        locatario.setExcluido(Boolean.TRUE);
        locatario.setDataFim(LocalDate.now());
        locatarios.save(locatario);
    }

    @Transactional
    public Optional<Locatario> retrieveByAluguel(String codigo) {
        Aluguel aluguel = new Aluguel();
        aluguel.setCodigo(Long.parseLong(codigo));

        return locatarios.findByAluguelAndExcluido(aluguel, Boolean.FALSE);
    }

    @Transactional
    public Locatario retrieveById(Long codigo) {
        return locatarios.findOne(codigo);
    }

    @Transactional
    public void deleteByAluguel(Aluguel aluguel) {

        Optional<Locatario> locatario = locatarios.findByAluguelAndExcluido(aluguel, Boolean.FALSE);
        if (!locatario.isPresent()) {
            return;
        }
        throw new BusinessException("Não é possível deletar, pois existe um locatário vinculado "
                + "ao aluguel : " + aluguel.getNome() + "!", "locatário");

    }

}











