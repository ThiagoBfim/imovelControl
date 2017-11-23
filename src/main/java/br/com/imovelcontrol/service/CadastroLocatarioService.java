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
import org.springframework.util.StringUtils;

/**
 * Created by marcosfellipec on 18/05/17.
 */

@Service
public class CadastroLocatarioService {

    @Autowired
    private Locatarios locatarios;

    @Transactional
    public Locatario salvar(Locatario locatario) {
        LocalDate dataInicioLocacao = null;
        if (locatario.getCodigo() != null) {
            Optional<Locatario> locatarioRetrived = locatarios
                    .findByAluguelAndExcluido(locatario.getAluguel(), Boolean.FALSE);
            if (locatarioRetrived.isPresent()) {
                locatario.setCodigo(locatarioRetrived.get().getCodigo());

                dataInicioLocacao = locatarioRetrived.get().getDataInicio();
            }
        }
        if (!StringUtils.isEmpty(locatario.getDataInicioJson())) {
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formater.withLocale(new Locale("pt", "BR"));
            LocalDate date = LocalDate.parse(locatario.getDataInicioJson(), formater);
            LocalDate dataAtual = LocalDate.now();
            /*Caso não tenha nenhuma data cadastrada, então a data precisa ser menor que a atual.*/
            if (dataInicioLocacao == null && date.isAfter(dataAtual)) {
                throw new BusinessException("Data de locação inválida! Não é aceita data maior que a data atual.", "data");
            }
            /*Caso já tenha uma data cadastrada, essa data precisa estar no mesmo mes do cadastro.*/
            if (dataInicioLocacao != null
                    && dataInicioLocacao.getMonthValue() != date.getMonthValue()) {
                throw new BusinessException("Data de locação inválida!", "data");
            }
            /*Caso exista algum locatario que possua uma data que faz conflito com a data escolhida.
            * Ou se o mes escolhido for menor que o mes atual.*/
            List<Locatario> locatarioList = locatarios
                    .findByDataFimGreaterThanAndAluguel(date, locatario.getAluguel());
            if (!CollectionUtils.isEmpty(locatarioList) ||
                    (date.getMonthValue() < dataAtual.getMonthValue() && dataInicioLocacao == null)) {
                throw new BusinessException("Data de locação inválida, favor colocar um data mais recente.", "data");
            }

            List<Locatario> locatarioListInicio = locatarios
                    .findByDataInicioGreaterThanEqualAndAluguel(date, locatario.getAluguel());
            if (!CollectionUtils.isEmpty(locatarioListInicio) && dataInicioLocacao == null) {
                throw new BusinessException("Data de locação inválida. Um locatario já foi incluido nesse dia.", "data");
            }

            locatario.setDataInicio(date);
        } else {
            throw new BusinessException("Data de locação inválida, favor colocar um data mais recente.", "data");
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











