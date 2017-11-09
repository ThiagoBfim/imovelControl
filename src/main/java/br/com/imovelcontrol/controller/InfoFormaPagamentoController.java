package br.com.imovelcontrol.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import br.com.imovelcontrol.dto.InformacaoPagamentoModalDTO;
import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.service.InformacaoPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Thiago on 30/07/2017.
 */
@Controller
@RequestMapping("/pagamento")
public class InfoFormaPagamentoController {

    @Autowired
    private Alugueis alugueis;

    @Autowired
    private InformacaoPagamentoService informacaoPagamentoService;


    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrar(@Valid InformacaoPagamento informacaoPagamento, BindingResult result) {

        informacaoPagamentoService.salvar(informacaoPagamento);
        return new ModelAndView("redirect:/imovel/aluguel/" + alugueis.findOne(informacaoPagamento.getAluguel()
                .getCodigo()).getImovel().getCodigo());
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/aluguel/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    InformacaoPagamentoModalDTO getInformacaoPagamentoInJsonByCodAluguel(@PathVariable String codigo) {


        Aluguel aluguel = alugueis.findOneWithLocatariosByCodigo(Long.valueOf(codigo));


        Optional<InformacaoPagamento> informacaoPagamentoOptional = informacaoPagamentoService.retrieveByAluguelAndData(codigo);
        InformacaoPagamento informacaoPagamento = new InformacaoPagamento();
        if (informacaoPagamentoOptional.isPresent()) {
            informacaoPagamento = informacaoPagamentoOptional.get();
        } else {

            informacaoPagamento.setValor(aluguel.getFormaPagamento().getValor());
            if (aluguel.getFormaPagamento().getAguaInclusa()) {
                informacaoPagamento.setAguaInclusa(false);
            }
            if (aluguel.getFormaPagamento().getInternetInclusa()) {
                informacaoPagamento.setInternetInclusa(false);
            }
            if (aluguel.getFormaPagamento().getIptuIncluso()) {
                informacaoPagamento.setIptuIncluso(false);
            }
            if (aluguel.getFormaPagamento().getLuzInclusa()) {
                informacaoPagamento.setLuzInclusa(false);
            }
            if (aluguel.getFormaPagamento().getPossuiCondominio()) {
                informacaoPagamento.setPossuiCondominio(false);
            }
            informacaoPagamento.setAluguel(aluguel);
            informacaoPagamento = informacaoPagamentoService.salvar(informacaoPagamento);

        }
        informacaoPagamento.setAluguel(new Aluguel());
        informacaoPagamento.setEstaAlugado(aluguel.isAlugado());

        InformacaoPagamentoModalDTO informacaoPagamentoModalDTO = new InformacaoPagamentoModalDTO();
        informacaoPagamentoModalDTO.setInformacaoPagamento(informacaoPagamento);

        Aluguel aluguelWithCodigo = new Aluguel();
        aluguelWithCodigo.setCodigo(Long.valueOf(codigo));
        List<InformacaoPagamento> alugueisRetrived = informacaoPagamentoService
                .retrieveInformacaoPagamentoVencidoByAluguel(Long.valueOf(codigo));

        if (!alugueisRetrived.isEmpty()) {
            alugueisRetrived.forEach(a -> {
                a.setAluguel(aluguelWithCodigo);
                a.setAtrasado(Boolean.TRUE);
            });
            if (!alugueisRetrived.contains(informacaoPagamento)) {
                alugueisRetrived.add(informacaoPagamento);
            }
            informacaoPagamentoModalDTO.setInformacaoPagamentoList(alugueisRetrived);
        }

        return informacaoPagamentoModalDTO;

    }


    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    InformacaoPagamento getInformacaoPagamentoInJsonByCodPagamento(@PathVariable String codigo) {


        InformacaoPagamento informacaoPagamento = informacaoPagamentoService.retrieveById(codigo);
        Aluguel aluguelWithCodigo = new Aluguel();
        aluguelWithCodigo.setCodigo(informacaoPagamento.getAluguel().getCodigo());
        Aluguel aluguel = alugueis.findOneWithLocatariosByCodigo(informacaoPagamento.getAluguel().getCodigo());
        informacaoPagamento.setAluguel(aluguelWithCodigo);
        informacaoPagamento.setEstaAlugado(aluguel.isAlugado());
        if (LocalDate.now().getMonthValue() > informacaoPagamento.getDataMensal().getMonthValue()) {
            informacaoPagamento.setAtrasado(Boolean.TRUE);
        }
        return informacaoPagamento;
    }
}
