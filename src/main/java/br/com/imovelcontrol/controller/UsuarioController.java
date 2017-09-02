package br.com.imovelcontrol.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.page.PageWrapper;
import br.com.imovelcontrol.model.Grupo;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.model.enuns.StatusUsuario;
import br.com.imovelcontrol.repository.Grupos;
import br.com.imovelcontrol.repository.Usuarios;
import br.com.imovelcontrol.service.CadastroUsuarioService;
import br.com.imovelcontrol.service.UsuarioLogadoService;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    private Grupos grupos;

    @Autowired
    private Usuarios usuarios;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @RequestMapping("/novo")
    public ModelAndView novo(Usuario usuario) {
        ModelAndView modelAndView = new ModelAndView("usuario/CadastroUsuario");
        modelAndView.addObject("grupos", grupos.findAll());
        return modelAndView;
    }

    @RequestMapping(value = {"/novo"}, method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Usuario usuario, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("usuario/CadastroUsuario");

        if (result.hasErrors()) {
            return novo(usuario);
        }
        if (salvarOuAlterarUsuario(usuario, result)) return novo(usuario);
        modelAndView.addObject("grupos", grupos.findAll());
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("mensagem", "Usuário Salvo com Sucessso!");
        return modelAndView;
    }

    @RequestMapping("/novoLogin")
    public ModelAndView novoLogin(Usuario usuario) {
        ModelAndView modelAndView = new ModelAndView("usuario/CadastroUsuarioLogin");
        modelAndView.addObject("grupos", grupos.findAll());
        return modelAndView;
    }

    @RequestMapping(value = {"/novoLogin"}, method = RequestMethod.POST)
    public ModelAndView salvarLogin(@Valid Usuario usuario, BindingResult result) {

        usuario.getGrupos().add(grupos.findOne(Grupo.PROPRIETARIO));
        usuario.setAtivo(Boolean.TRUE);

        if (result.hasErrors()) {
            return novoLogin(usuario);
        }
        if (salvarOuAlterarUsuario(usuario, result)) return novoLogin(usuario);
        ModelAndView modelAndView = new ModelAndView("usuario/login");
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("mensagem", "Usuário Salvo com Sucessso!");
        return modelAndView;
    }

    @GetMapping(value = "/pesquisar")
    public ModelAndView pesquisar(Usuario usuario, BindingResult result, @PageableDefault(size = 5) Pageable pageable,
            HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("usuario/PesquisaUsuario");
        modelAndView.addObject("grupos", grupos.findAll());
        PageWrapper<Usuario> pagina = new PageWrapper<>(usuarios.filtrar(usuario, pageable), httpServletRequest);
        modelAndView.addObject("pagina", pagina);
        return modelAndView;
    }

    @PutMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarStatus(@RequestParam("codigos[]") Long[] codigos,
            @RequestParam("status") StatusUsuario statusUsuario) {
        cadastroUsuarioService.alterarStatus(codigos, statusUsuario);
    }

    @GetMapping("/{codigo}")
    public ModelAndView editar(@PathVariable Long codigo) {
        if (verificarUsuarioLogado(codigo)) return new ModelAndView("/403");
        Usuario usuario = usuarios.buscarComGrupos(codigo);
        ModelAndView modelAndView = novo(usuario);
        modelAndView.addObject(usuario);

        return modelAndView;
    }

    private boolean verificarUsuarioLogado(Long codigo) {
        if (!codigo.equals(usuarioLogadoService.getUsuario().getCodigo())) {
            /*Se o codigo for diferente, e ele não for Admin, então, ele não podera entrar*/
            Usuario usuarioLogado = usuarios.buscarComGrupos(usuarioLogadoService.getUsuario().getCodigo());
            if (!usuarioLogado.getGrupos().contains(new Grupo(Grupo.ADMIN))) {
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("/{codigo}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable Long codigo) {
        Usuario usuario = usuarios.findOne(codigo);
        try {
            cadastroUsuarioService.excluir(usuario);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping("/alterarsenha")
    public ModelAndView alterarSenha(Usuario usuario) {
        return new ModelAndView("usuario/AlterarSenha");
    }

    @GetMapping("/alterarsenha/{codigo}")
    public ModelAndView alterarSenha(@PathVariable Long codigo) {
        if (verificarUsuarioLogado(codigo)) return new ModelAndView("/403");
        Usuario usuario = usuarios.buscarComGrupos(codigo);
        ModelAndView modelAndView = alterarSenha(usuario);
        modelAndView.addObject(usuario);
        return modelAndView;
    }

    @RequestMapping(value = {"/alterarsenha"}, method = RequestMethod.POST)
    public ModelAndView alterarSenha(Usuario usuario, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("usuario/AlterarSenha");
        Usuario usuarioRetrived = usuarios.buscarComGrupos(usuario.getCodigo());
        if (StringUtils.isEmpty(usuario.getSenha())) {
            result.rejectValue("senha", "Senha deve ter no máximo 30 caracteres e no mínimo 6", "Senha Incorreta");
            return alterarSenha(usuarioRetrived);
        } else {
            usuarioRetrived.setSenha(usuario.getSenha());
            usuarioRetrived.setConfirmacaoSenha(usuario.getConfirmacaoSenha());
            if (salvarOuAlterarUsuario(usuarioRetrived, result)) return alterarSenha(usuario);
            modelAndView.addObject("mensagem", "Senha alterada com Sucessso!");
            return modelAndView;

        }

    }

    private boolean salvarOuAlterarUsuario(Usuario usuario, BindingResult result) {
        try {
            if (usuario.getSenha() != null
                    && (usuario.getSenha().length() > 30 || usuario.getSenha().length() < 6)) {
                result.rejectValue("senha", "Senha deve ter no máximo 30 caracteres e no mínimo 6",
                        "Senha deve ter no máximo 30 caracteres e no mínimo 6");
                return true;
            }
            if (StringUtils.isEmpty(usuario.getCodigoVerificadorTemp())
                    || !usuario.getCodigoVerificadorTemp().equals(usuario.getCodigoVerificador())) {
                result.rejectValue("codigoVerificadorTemp", "Código Verificador Incorreto", "Código Verificador Incorreto");
                return true;
            }
            cadastroUsuarioService.salvar(usuario);
        } catch (BusinessException e) {
            result.rejectValue(e.getField(), e.getMessage(), e.getMessage());
            return true;
        }
        return false;
    }

}
