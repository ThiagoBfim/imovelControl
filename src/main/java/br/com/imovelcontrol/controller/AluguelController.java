package br.com.imovelcontrol.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.page.PageWrapper;
import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.FormaPagamento;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.model.enuns.TipoForro;
import br.com.imovelcontrol.model.enuns.TipoImovel;
import br.com.imovelcontrol.model.enuns.TipoPiso;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.CadastroAluguelService;
import br.com.imovelcontrol.service.TemplateFormaPagamentoService;
import br.com.imovelcontrol.service.UsuarioLogadoService;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private TemplateFormaPagamentoService templateFormaPagamentoService;

    @Autowired
    private CadastroAluguelService cadastroAluguelService;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

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

        FormaPagamento formaPagamentoSession = templateFormaPagamentoService.salvar(aluguel.getFormaPagamento());
        aluguel.setFormaPagamento(formaPagamentoSession);
        try {
            cadastroAluguelService.salvar(aluguel);
        } catch (BusinessException e) {
            result.rejectValue(e.getField(), e.getMessage(), e.getMessage());
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
        Usuario usuario = usuarioLogadoService.getUsuario();
        Imovel imovelRetrived = imoveis.findOne(codigo);
        if (!imovelRetrived.getDonoImovel().equals(usuario)) {
            return new ModelAndView("/403");
        }
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

        Usuario usuario = usuarioLogadoService.getUsuario();
        if (!aluguel.getImovel().getDonoImovel().equals(usuario)) {
            return new ModelAndView("/403");
        }

        modelAndView.addObject(aluguel);
        return modelAndView;
    }

    @DeleteMapping("/{codigo}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable Long codigo) {
        Aluguel aluguel = alugueis.findOne(codigo);
        try {
            cadastroAluguelService.excluirLogicamente(aluguel);
        } catch (BusinessException e) {
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


}
