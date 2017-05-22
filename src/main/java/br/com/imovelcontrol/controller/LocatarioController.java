package br.com.imovelcontrol.controller;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.repository.Locatarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by marcosfellipec on 18/05/17.
 */

@Controller
@RequestMapping("/locatario")
public class LocatarioController {

    @Autowired
    private Locatarios locatarios;

    @RequestMapping("/novo")
    public ModelAndView novo(Imovel imovel) {
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");
        return mAndView;
    }



}
