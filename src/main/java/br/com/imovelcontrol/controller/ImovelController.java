package br.com.imovelcontrol.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.page.PageWrapper;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.CadastroImovelService;
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
@RequestMapping("/imovel")
public class ImovelController {

    @Autowired
    private Imoveis imoveis;

    @Autowired
    private CadastroImovelService cadastroImovelService;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    /**
     * Metodo para iniciar um novo imovel.
     *
     * @param imovel Imovel a ser instanciado.
     * @return Pagina para cadastro do imovel.
     */
    @RequestMapping("/novo")
    public ModelAndView novo(Imovel imovel) {
        ModelAndView mAndView = new ModelAndView("imovel/CadastroImovel");
        return mAndView;
    }

    /**
     * Metodo para receber a requisição de criar um novo imovel.
     *
     * @param imovel Imovel a ser criado.
     * @param result Resultado do cadastro, caso tenha erro, serão exibidos.
     * @return Pagina de sucesso.
     */
    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrar(@Valid Imovel imovel, BindingResult result) {

        ModelAndView mAndView = new ModelAndView("imovel/CadastroImovel");
        Usuario usuario = usuarioLogadoService.getUsuario();

        imovel.setDonoImovel(usuario);
        if (result.hasErrors()) {
            return novo(imovel);
        }

        try {
            cadastroImovelService.salvar(imovel);
        } catch (BusinessException e) {
            result.rejectValue(e.getField(), e.getMessage(), e.getMessage());
            return novo(imovel);
        }
        
        mAndView.addObject("imovel", imovel);
        mAndView.addObject("mensagem", "Imovel Salvo com sucesso!");
        return mAndView;
    }

    @GetMapping("/pesquisar")
    public ModelAndView pesquisar(Imovel imovel, BindingResult result, @PageableDefault(size = 5) Pageable pageable,
            HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("imovel/PesquisaImovel");
        Usuario usuario = usuarioLogadoService.getUsuario();

        PageWrapper<Imovel> pagina = new PageWrapper<>(imoveis.filtrar(imovel, usuario, pageable), httpServletRequest);
        modelAndView.addObject("pagina", pagina);
        return modelAndView;
    }

    @DeleteMapping("/{codigo}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable Long codigo) {
        Imovel imovel = imoveis.findOne(codigo);
        try {
            cadastroImovelService.excluirLogicamente(imovel);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{codigo}")
    public ModelAndView editar(@PathVariable Long codigo) {
        Imovel imovel = imoveis.findOne(codigo);
        Usuario usuario = usuarioLogadoService.getUsuario();
        if (!imovel.getDonoImovel().equals(usuario)) {
            return new ModelAndView("/403");
        }
        ModelAndView mAndView = new ModelAndView("imovel/CadastroImovel");
        mAndView.addObject(imovel);
        return mAndView;
    }

}