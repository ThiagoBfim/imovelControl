package br.com.imovelcontrol.controller;

import java.util.Optional;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.converter.FormatUtil;
import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.CadastroLocatarioService;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    private CadastroLocatarioService cadastroLocatarioService;

    @Autowired
    private Locatarios locatarios;

    @Autowired
    private Alugueis alugueis;

    @RequestMapping("/novo")
    public ModelAndView novo(Locatario locatario) {
        return new ModelAndView();
    }

    //    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Locatario locatario, BindingResult result) {
        Locatario locatarioRetrieve = locatario;
        if (locatario.getCodigo() != null) {
            locatarioRetrieve = cadastroLocatarioService.retrieveById(locatario.getCodigo());
            locatarioRetrieve.setNome(locatario.getNome());
            locatarioRetrieve.setCpf(locatario.getCpf());
            locatarioRetrieve.setTelefone(locatario.getTelefone());
        }

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }
        try {
            cadastroLocatarioService.salvar(locatarioRetrieve);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
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

    @GetMapping("/{codigo}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable Long codigo) {
        Locatario locatario = locatarios.findOne(codigo);

        try {
            cadastroLocatarioService.excluirLogicamente(locatario);

        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
