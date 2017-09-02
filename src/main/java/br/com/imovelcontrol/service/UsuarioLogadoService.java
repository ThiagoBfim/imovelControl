package br.com.imovelcontrol.service;

import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by Usuario on 05/08/2017.
 */
@Service
public class UsuarioLogadoService {

    @Autowired
    private Usuarios usuarios;

    public Usuario getUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarios.buscarComGruposByLogin(auth.getName());
    }
}
