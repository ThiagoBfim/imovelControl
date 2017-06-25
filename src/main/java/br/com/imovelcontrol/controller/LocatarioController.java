package br.com.imovelcontrol.controller;

import javax.jws.WebParam;
import javax.validation.Valid;

import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.CadastroAluguelService;
import br.com.imovelcontrol.service.CadastroLocatarioService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Created by marcosfellipec on 18/05/17.
 */

@Controller
@RequestMapping("/locatario")
public class LocatarioController {

    @Autowired
    private CadastroAluguelService cadastroAluguelService;
    @Autowired
    private CadastroLocatarioService cadastroLocatarioService;

    @Autowired
    private Alugueis alugueis;
    @RequestMapping("/novo")
    public ModelAndView novo(Locatario locatario) {
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");
        return mAndView;
    }

    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrar(@Valid Locatario locatario, BindingResult result){
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");
        Aluguel aluguel = new Aluguel();
        aluguel.setCodigo(1l);
        locatario.setAluguel(aluguel);
        if (result.hasErrors()) {
            return novo(locatario);
        }
        try {
            cadastroLocatarioService.salvar(locatario);
        }catch (Exception e){
            result.rejectValue("nome", e.getMessage(), e.getMessage());
            return novo(locatario);
        }
        mAndView.addObject("locatario", locatario);
        mAndView.addObject("mensagem", "Imovel Salvo com sucesso!");
        return mAndView;
    }
}