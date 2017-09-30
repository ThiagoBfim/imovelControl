package br.com.imovelcontrol.controller;

import java.util.Optional;
import javax.validation.Valid;

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

    @RequestMapping("/novo")
    public ModelAndView novo(InformacaoPagamento informacaoPagamento) {
        return new ModelAndView();
    }

    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrar(@Valid InformacaoPagamento informacaoPagamento, BindingResult result) {

        informacaoPagamentoService.salvar(informacaoPagamento);

        return new ModelAndView("redirect:/imovel/aluguel/" + alugueis.findOne(informacaoPagamento.getAluguel()
                .getCodigo()).getImovel().getCodigo());
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    InformacaoPagamento getInformacaoPagamentoInJSON(@PathVariable String codigo) {


        Aluguel aluguel = alugueis.findOne(Long.parseLong(codigo));


        Optional<InformacaoPagamento> informacaoPagamentoOptional = informacaoPagamentoService.retrieveByAluguelAndData(codigo);
        InformacaoPagamento informacaoPagamento = new InformacaoPagamento();
        if (informacaoPagamentoOptional.isPresent()) {
            informacaoPagamento = informacaoPagamentoOptional.get();
        } else {

            //TODO Utilizar Reflection
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
        return informacaoPagamento;

    }
}
