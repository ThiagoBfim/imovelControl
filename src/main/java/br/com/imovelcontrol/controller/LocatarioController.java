package br.com.imovelcontrol.controller;

import java.util.Optional;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.converter.FormatUtil;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.service.CadastroLocatarioService;
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
 * Created by marcosfellipec on 18/05/17.
 */

@Controller
@RequestMapping("/locatario")
public class LocatarioController {

    @Autowired
<<<<<<< HEAD
    private CadastroAluguelService cadastroAluguelService;
    @Autowired
=======
>>>>>>> origin/master
    private CadastroLocatarioService cadastroLocatarioService;


    @RequestMapping("/novo")
    public ModelAndView novo(Locatario locatario) {
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");
        return mAndView;
    }

    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrar(@Valid Locatario locatario, BindingResult result) {
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");
<<<<<<< HEAD
        Aluguel aluguel = new Aluguel();
        aluguel.setCodigo(1l);
        locatario.setAluguel(aluguel);
        if (result.hasErrors()) {
            return novo(locatario);
=======

        Locatario locatarioRetrieve = locatario;
        if (locatario.getCodigo() != null) {
            locatarioRetrieve = cadastroLocatarioService.retrieveById(locatario.getCodigo());
            locatarioRetrieve.setNome(locatario.getNome());
            locatarioRetrieve.setCpf(locatario.getCpf());
            locatarioRetrieve.setTelefone(locatario.getTelefone());
>>>>>>> origin/master
        }
        cadastroLocatarioService.salvar(locatarioRetrieve);

        mAndView.addObject("mensagem", "Imovel Salvo com sucesso!");
        return mAndView;
    }
<<<<<<< HEAD
}
=======

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    Locatario getLocatarioInJSON(@PathVariable String codigo) {


        Optional<Locatario> locatarioOptional = cadastroLocatarioService.retrieveByAluguel(codigo);
        Locatario locatario = new Locatario();
        if (locatarioOptional.isPresent()) {
            locatario = locatarioOptional.get();
            locatario.setAluguel(new Aluguel());
            locatario.setCpf(FormatUtil.inserirCpfMascara(locatario.getCpf()));
            locatario.setTelefone(FormatUtil.inserirTelefoneMascara(locatario.getTelefone()));
        }
        return locatario;

    }
}
>>>>>>> origin/master
