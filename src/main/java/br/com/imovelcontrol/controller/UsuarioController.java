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


    /**
     * Método para redirecionar para a pagina de cadastro de um novo usuário.
     * <p>Utilizado apenas internamente Administradores.
     *
     * @param usuario Usuário que será carregado na tela.
     * @return Retorna a tela de cadastro de um novo usuario.
     */
    @RequestMapping("/novo")
    public ModelAndView novo(Usuario usuario) {
        ModelAndView modelAndView = new ModelAndView("usuario/CadastroUsuario");
        modelAndView.addObject("grupos", grupos.findAll());
        return modelAndView;
    }

    /**
     * Método Post para salvar um novo usuario.
     * <p>Utilizado apenas internamente por Administradores.
     *
     * @param usuario Usuario a ser salvo.
     * @param result  Result é o parametro responsavel por conter erros e por passar erros para a tela,
     *                facilitando a visão do cliente.
     * @return Retorna a pagina de sucesso, ou de exceção caso ocorra erro no cadastro.
     */
    @RequestMapping(value = {"/novo"}, method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Usuario usuario, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("usuario/CadastroUsuario");

        if (result.hasErrors()) {
            return novo(usuario);
        }
        try {
            cadastroUsuarioService.salvar(usuario);
        } catch (BusinessException e) {
            result.rejectValue(e.getField(), e.getMessage(), e.getMessage());
            return novo(usuario);
        }
        modelAndView.addObject("grupos", grupos.findAll());
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("mensagem", "Usuário Salvo com Sucessso!");
        return modelAndView;
    }


    /**
     * Método para redirecionar para a pagina de cadastro de um novo usuário.
     * <p>Utilizado apenas para cadastro de forma publica, sem estar logado.
     *
     * @return Retorna a tela de cadastro de um novo usuario.
     */
    @RequestMapping("/novoLogin")
    public ModelAndView novoLogin(Usuario usuario) {
        ModelAndView modelAndView = new ModelAndView("usuario/CadastroUsuarioLogin");
        modelAndView.addObject("grupos", grupos.findAll());
        return modelAndView;
    }

    /**
     * Método Post para salvar um novo usuario.
     * <p>Utilizado apenas para cadastro de forma publica, sem estar logado.
     *
     * @param usuario Usuario a ser salvo.
     * @param result  Result é o parametro responsavel por conter erros e por passar erros para a tela,
     *                facilitando a visão do cliente.
     * @return Retorna a pagina de sucesso, ou de exceção caso ocorra erro no cadastro.
     */
    @RequestMapping(value = {"/novoLogin"}, method = RequestMethod.POST)
    public ModelAndView salvarLogin(@Valid Usuario usuario, BindingResult result) {

        usuario.getGrupos().add(grupos.findOne(Grupo.PROPRIETARIO));
        usuario.setAtivo(Boolean.TRUE);

        if (result.hasErrors()) {
            return novoLogin(usuario);
        }
        usuario.setCodigoVerificadorTemp(usuario.getCodigoVerificador());
        if (salvarOuAlterarUsuario(usuario, result)) return novoLogin(usuario);
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("mensagem", "Usuário Salvo com Sucessso!");
        return modelAndView;
    }

    /**
     * Método para realizar a edição do usuario.
     *
     * @param usuario Usuario a ser salvo.
     * @param result  Result é o parametro responsavel por conter erros e por passar erros para a tela,
     *                facilitando a visão do cliente.
     * @return Retorna a pagina de sucesso, ou de exceção caso ocorra erro no cadastro.
     */
    @RequestMapping(value = {"/editar"}, method = RequestMethod.POST)
    public ModelAndView editar(@Valid Usuario usuario, BindingResult result) {

        usuario.setAtivo(Boolean.TRUE);

        if (result.hasErrors()) {
            return editar(usuario);
        }
        if (salvarOuAlterarUsuario(usuario, result)) return editar(usuario);
        ModelAndView modelAndView = new ModelAndView("usuario/EditarUsuario");
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("mensagem", "Usuário Salvo com Sucessso!");
        return modelAndView;
    }

    /**
     * Método responsavel por realizar a busca por todos os usuarios.
     *
     * @param usuario            filtro para buscar por usuarios especificos.
     * @param result             Result é o parametro responsavel por conter erros e por passar erros para a tela,
     *                           facilitando a visão do cliente.
     * @param pageable           Pageable é o parametro para realizar a paginação.
     * @param httpServletRequest O Resquest do http.
     * @return Retorna uma lista de usuarios de forma paginada.
     */
    @GetMapping(value = "/pesquisar")
    public ModelAndView pesquisar(Usuario usuario, BindingResult result, @PageableDefault(size = 5) Pageable pageable,
            HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("usuario/PesquisaUsuario");
        modelAndView.addObject("grupos", grupos.findAll());
        PageWrapper<Usuario> pagina = new PageWrapper<>(usuarios.filtrar(usuario, pageable), httpServletRequest);
        modelAndView.addObject("pagina", pagina);
        return modelAndView;
    }

    /**
     * Método para realizar a exclusão de um ou mais usuarios de forma mais rapida.
     *
     * @param codigos       Codigo dos usuarios que serão excluidos logicamente.
     * @param statusUsuario Status, se será ativado, ou inativado.
     */
    @PutMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarStatus(@RequestParam("codigos[]") Long[] codigos,
            @RequestParam("status") StatusUsuario statusUsuario) {
        cadastroUsuarioService.alterarStatus(codigos, statusUsuario);
    }

    /**
     * Logica para realizar a edição por meio de usuarios Administradores.
     *
     * @param codigo Código do usuario que seja alterado.
     * @return Retorna a view que foi realizada a alteração.
     */
    @GetMapping("/{codigo}")
    public ModelAndView editar(@PathVariable Long codigo) {
        Usuario usuario = usuarios.buscarComGrupos(codigo);
        if (!usuarioLogadoService.getUsuario().getGrupos().contains(new Grupo(Grupo.ADMIN))) {
            return new ModelAndView("/404");
        }
        ModelAndView modelAndView = novo(usuario);
        modelAndView.addObject(usuario);
        return modelAndView;
    }

    /**
     * Método que realiza a edição do usuario logado.
     *
     * @return retorna o caminho para a edição por meio do usuario.
     */
    @GetMapping("/redirectEditar")
    public ModelAndView editar(Usuario usuario) {
        if (usuario.getCodigo() == null) {
            usuario = usuarioLogadoService.getUsuario();
        }
        ModelAndView modelAndView = new ModelAndView("usuario/EditarUsuario");
        modelAndView.addObject(usuario);
        return modelAndView;
    }

    /**
     * Metodo para chamar a tela de alterar senha
     *
     * @param usuario Usuario que sera alterado.
     * @return Retorna a tela de alterar a senha.
     */
    @RequestMapping("/alterarsenha")
    public ModelAndView alterarSenha(Usuario usuario) {
        ModelAndView modelAndView = new ModelAndView("usuario/AlterarSenha");
        if (usuario.getCodigo() == null) {
            usuario = usuarioLogadoService.getUsuario();
        }
        modelAndView.addObject(usuario);
        return modelAndView;
    }

    /**
     * Metodo para alterar a senha do usuario.
     *
     * @param usuario Usuario que tera a senha alterada.
     * @param result  Result é o parametro responsavel por conter erros e por passar erros para a tela,
     *                facilitando a visão do cliente.
     * @return Retorna a pagina de sucesso, ou de exceção caso ocorra erro no cadastro.
     */
    @RequestMapping(value = {"/alterarsenha"}, method = RequestMethod.POST)
    public ModelAndView alterarSenha(Usuario usuario, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("usuario/AlterarSenha");
        Usuario usuarioRetrived = usuarios.buscarComGrupos(usuario.getCodigo());
        if (StringUtils.isEmpty(usuario.getSenha())) {
            result.rejectValue("senha", "Senha deve ter no máximo 30 caracteres e no mínimo 6", "Senha é Obrigatório");
            return alterarSenha(usuario);
        }
        if (StringUtils.isEmpty(usuario.getConfirmacaoSenha())) {
            result.rejectValue("confirmacaoSenha", "Confirmação da senha está em branco", "Confirmação da Senha é obrigatório");
            return alterarSenha(usuario);
        }
        if (!usuario.getSenha().equals(usuario.getConfirmacaoSenha())) {
            result.rejectValue("confirmacaoSenha", "Confirmação da senha está incorreta", "Confirmação da senha está incorreta");
            return alterarSenha(usuario);
        }
        usuarioRetrived.setSenha(usuario.getSenha());
        usuarioRetrived.setConfirmacaoSenha(usuario.getConfirmacaoSenha());
        usuarioRetrived.setCodigoVerificadorTemp(usuario.getCodigoVerificadorTemp());
        if (salvarOuAlterarUsuario(usuarioRetrived, result)) return alterarSenha(usuarioRetrived);
        modelAndView.addObject("mensagem", "Senha alterada com Sucessso!");
        modelAndView.addObject(usuarioRetrived);
        return modelAndView;
    }

    /**
     * Método para realizar a exclusão.
     *
     * @param codigo Código do usuario que será excluido.
     * @return Retorna codigo de sucesso(200) ou de erro(403)
     */
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

    /**
     * Método interno que realiza as validações para salvar ou alterar o usuario.
     *
     * @param usuario usuario a ser salvo.
     * @param result  Result para preencher com erro.
     * @return retorna true caso sucesso e false caso contrario.
     */
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
