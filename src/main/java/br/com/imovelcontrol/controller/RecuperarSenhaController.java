package br.com.imovelcontrol.controller;

import br.com.imovelcontrol.email.JavaMail;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.service.CadastroUsuarioService;
import br.com.imovelcontrol.service.exception.EmailNaoEncontradoException;
import br.com.imovelcontrol.service.exception.LoginUsuarioNaoEncontradoException;
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

    @RequestMapping(method = RequestMethod.POST, value = "/pesquisa")
    public ModelAndView pesquisar(Usuario usuario, BindingResult result){
        ModelAndView modelAndView = new ModelAndView("RecuperarSenha");
        JavaMail javaMail = new JavaMail();
        try{
            usuario = cadastroUsuarioService.findByLogin(usuario.getLogin());
        }catch (LoginUsuarioNaoEncontradoException e){
            result.rejectValue("login", e.getMessage(),e.getMessage());
        }

        javaMail.setDestinarario("marcosfellipec@gmail.com");
        javaMail.setTitulo("Teste Java Mail");
        javaMail.setMensagem("minha tela de recuperar senha não está funcionando");
        javaMail.enviarEmail();
        modelAndView.addObject("mensagem", "Nova senha foi enviada ao seu " +
                "email cadastrado no sistema");
        return modelAndView;
    }
}
