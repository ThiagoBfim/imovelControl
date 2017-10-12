package br.com.imovelcontrol.controller;

import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.service.CadastroUsuarioService;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by marcosfellipec on 20/07/17.
 */

@Controller
@RequestMapping("/esqueciminhasenha")
public class RecuperarSenhaController {

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @GetMapping
    public ModelAndView recuperarSenha(Usuario usuario) {
        return new ModelAndView("RecuperarSenha");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pesquisa")
    public ModelAndView pesquisar(Usuario usuario, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("RecuperarSenha");

        try {
            usuario = cadastroUsuarioService.findByLogin(usuario.getLogin());
            cadastroUsuarioService.enviarNovaSenha(usuario);
            modelAndView.addObject("mensagem", "Nova senha foi enviada ao seu E-mail cadastrado no sistema.");

        } catch (BusinessException e) {
            result.rejectValue(e.getField(), e.getMessage(), e.getMessage());
            return recuperarSenha(usuario);
        }

        return modelAndView;
    }
}
