package br.com.imovelcontrol.controller;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.page.PageWrapper;
import br.com.imovelcontrol.model.FormaPagamento;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.enuns.TipoImovel;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.model.tipoimovel.enuns.TipoForro;
import br.com.imovelcontrol.model.tipoimovel.enuns.TipoPiso;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.CadastroAluguelService;
import br.com.imovelcontrol.service.FormaPagamentoService;
import br.com.imovelcontrol.service.InformacaoPagamentoService;
import br.com.imovelcontrol.service.exception.ImpossivelExcluirEntidadeException;
import br.com.imovelcontrol.service.exception.NomeAluguelJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/imovel/aluguel")
public class AluguelController {

    @Autowired
    private Alugueis alugueis;

    @Autowired
    private Imoveis imoveis;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private CadastroAluguelService cadastroAluguelService;

    @Autowired
    private InformacaoPagamentoService informacaoPagamentoService;

    private Long codigoImovel;

    private Aluguel aluguel;

    @RequestMapping("/novo")
    public ModelAndView novo(Aluguel aluguel) {
        ModelAndView modelAndView = new ModelAndView("tipoImovel/CadastroAluguel");
        if (aluguel.getImovel() == null) {
            return new ModelAndView("redirect:/imovel/aluguel/" + codigoImovel);
        }
        modelAndView.addObject(aluguel);
        setAllObjectsFromImovelToModelView(modelAndView);
        return modelAndView;
    }

    // ver alguma maneira de não mudar a url.
    @RequestMapping(value = {"/novo"}, method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Aluguel aluguel, BindingResult result) {
        aluguel.setImovel(imoveis.findOne(codigoImovel));
        if (result.hasErrors()) {
            return novo(aluguel);
        }

        FormaPagamento formaPagamentoSession = formaPagamentoService.salvar(aluguel.getFormaPagamento());
        aluguel.setFormaPagamento(formaPagamentoSession);
        try {
            cadastroAluguelService.salvar(aluguel);
        } catch (NomeAluguelJaCadastradoException e) {
            result.rejectValue("nome", e.getMessage(), e.getMessage());
            return novo(aluguel);
        }
        aluguel = new Aluguel();
        setImovelAluguel(aluguel);

        return new ModelAndView("redirect:/imovel/aluguel/" + codigoImovel);

    }

    @GetMapping("/{codigo}")
    public ModelAndView pesquisar(@PathVariable("codigo") Long codigo, @PageableDefault(size = 5) Pageable pageable,
            HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("tipoImovel/PesquisarAluguel");
        setAllObjectsFromImovelToModelView(modelAndView);
        codigoImovel = codigo;
        aluguel = new Aluguel();
        setImovelAluguel(aluguel);
        PageWrapper<Aluguel> pagina = new PageWrapper<>(alugueis.filtrarByImovel(codigo, pageable), httpServletRequest);
        modelAndView.addObject("pagina", pagina);
        modelAndView.addObject("aluguel", aluguel);
        Locatario locatario = new Locatario();
        modelAndView.addObject("locatario", locatario);
        InformacaoPagamento informacaoPagamento = new InformacaoPagamento();
        modelAndView.addObject("informacaoPagamento", informacaoPagamento);
        return modelAndView;
    }

    @GetMapping("/editar/{codigo}")
    public ModelAndView editar(@PathVariable("codigo") Long codigo) {
        ModelAndView modelAndView = new ModelAndView("tipoImovel/CadastroAluguel");
        setAllObjectsFromImovelToModelView(modelAndView);
        Aluguel aluguel = alugueis.findOne(codigo);
        modelAndView.addObject(aluguel);
        return modelAndView;
    }

    @DeleteMapping("/{codigo}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable Long codigo) {
        Aluguel aluguel = alugueis.findOne(codigo);
        try {
            cadastroAluguelService.excluir(aluguel);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    private void setImovelAluguel(Aluguel aluguel) {
        if (aluguel.getImovel() == null) {
            aluguel.setImovel(imoveis.findOne(codigoImovel));
        }
    }

    private void setAllObjectsFromImovelToModelView(ModelAndView modelAndView) {
        modelAndView.addObject("tiposImoveis", TipoImovel.values());
        modelAndView.addObject("tiposPiso", TipoPiso.values());
        modelAndView.addObject("tiposForro", TipoForro.values());
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/pagamento/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    InformacaoPagamento getInformacaoPagamentoInJSON(@PathVariable String codigo) {


        Optional<InformacaoPagamento> informacaoPagamentoOptional = informacaoPagamentoService.retrieveByAluguel(codigo);
        InformacaoPagamento informacaoPagamento = new InformacaoPagamento();
        if (informacaoPagamentoOptional.isPresent()) {
            informacaoPagamento = informacaoPagamentoOptional.get();
            informacaoPagamento.setAguaInclusa(informacaoPagamentoOptional.get().getAluguel().getFormaPagamento().getAguaInclusa());
            informacaoPagamento.setInternetInclusa(informacaoPagamentoOptional.get().getAluguel().getFormaPagamento().getInternetInclusa());
            informacaoPagamento.setAluguel(new Aluguel());
        } else {
            informacaoPagamento.setAluguel(new Aluguel());
            informacaoPagamento.setInternetInclusa(Boolean.TRUE);
            informacaoPagamento.setAguaInclusa(Boolean.FALSE);
            informacaoPagamento.setLuzInclusa(false);
            informacaoPagamento.setPago(false);
            informacaoPagamento.setIptuIncluso(false);
            informacaoPagamento.setPossuiCondominio(false);
        }
        return informacaoPagamento;

    }

}
