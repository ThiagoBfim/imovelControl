package br.com.imovelcontrol.controller;

import java.util.Optional;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.converter.FormatUtil;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.CadastroLocatarioService;
import br.com.imovelcontrol.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by marcosfellipec on 18/05/17.
 */

@Controller
@RequestMapping("/locatario")
public class LocatarioController {

    @Autowired
    private CadastroLocatarioService cadastroLocatarioService;

    @Autowired
    private Locatarios locatarios;

    @RequestMapping("/novo")
    public ModelAndView novo(Locatario locatario) {
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");
        return mAndView;
    }

    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrar(@Valid Locatario locatario, BindingResult result) {
        ModelAndView mAndView = new ModelAndView("locatario/CadastroLocatario");

        Locatario locatarioRetrieve = locatario;
        if (locatario.getCodigo() != null) {
            locatarioRetrieve = cadastroLocatarioService.retrieveById(locatario.getCodigo());
            locatarioRetrieve.setNome(locatario.getNome());
            locatarioRetrieve.setCpf(locatario.getCpf());
            locatarioRetrieve.setTelefone(locatario.getTelefone());
        }
        cadastroLocatarioService.salvar(locatarioRetrieve);

        mAndView.addObject("mensagem", "Locatário Salvo com sucesso!");
        return mAndView;
    }

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

    @DeleteMapping("/{codigo}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable Long codigo){
        Locatario locatario = locatarios.findOne(codigo);

        try {
            cadastroLocatarioService.excluir(locatario);

        }catch (ImpossivelExcluirEntidadeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

}
