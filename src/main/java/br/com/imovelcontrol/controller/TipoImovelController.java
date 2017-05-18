package br.com.imovelcontrol.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.page.PageWrapper;
import br.com.imovelcontrol.model.FormaPagamento;
import br.com.imovelcontrol.model.enuns.TipoImovel;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.model.tipoimovel.enuns.TipoForro;
import br.com.imovelcontrol.model.tipoimovel.enuns.TipoPiso;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.CadastroAluguelService;
import br.com.imovelcontrol.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/imovel/aluguel")
public class TipoImovelController {

    @Autowired
    private Alugueis alugueis;

    @Autowired
    private Imoveis imoveis;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private CadastroAluguelService cadastroAluguelService;

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
    public ModelAndView salvar(@Valid Aluguel aluguel, BindingResult result, Model model, RedirectAttributes attributes,
            @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("tipoImovel/PesquisarAluguel");
        setAllObjectsFromImovelToModelView(modelAndView);

        aluguel.setImovel(imoveis.findOne(codigoImovel));
        if (result.hasErrors()) {
            return novo(aluguel);
        }

        FormaPagamento formaPagamentoSession = formaPagamentoService.salvar(aluguel.getFormaPagamento());
        aluguel.setFormaPagamento(formaPagamentoSession);

        cadastroAluguelService.salvar(aluguel);
        aluguel = new Aluguel();
        setImovelAluguel(aluguel);

        modelAndView.addObject(aluguel);
        modelAndView.addObject("codigo", codigoImovel);
        attributes.addFlashAttribute("mensagem", "Aluguel Salvo com sucesso!");

        PageWrapper<Aluguel> pagina = new PageWrapper<>(alugueis.filtrarByImovel(codigoImovel, pageable),
                httpServletRequest);
        modelAndView.addObject("pagina", pagina);
        modelAndView.addObject("aluguel", aluguel);

        return modelAndView;
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
    public RedirectView excluir(@PathVariable("codigo") Long codigo) {
        Aluguel aluguel = alugueis.findOne(codigo);
        cadastroAluguelService.excluir(aluguel);
        RedirectView modelAndView = new RedirectView("/imovel/aluguel/" + codigoImovel, false);
        modelAndView.setStatusCode(HttpStatus.OK);
        return modelAndView;
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

}
