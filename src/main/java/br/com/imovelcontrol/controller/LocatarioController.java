package br.com.imovelcontrol.controller;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.CadastroLocatarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by marcosfellipec on 18/05/17.
 */

@Controller
@RequestMapping("/locatario")
public class LocatarioController {

    @Autowired
    private Locatarios locatarios;

    @Autowired
    private CadastroLocatarioService cadastroLocatarioService;

    @RequestMapping("/novo")
    public ModelAndView novo(Imovel imovel) {
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");
        return mAndView;
    }

    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrar(@Valid Locatario locatario, BindingResult result) {
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        cadastroLocatarioService.salvar(locatario);
        mAndView.addObject("locatario", locatario);
        return mAndView;
    }


}
