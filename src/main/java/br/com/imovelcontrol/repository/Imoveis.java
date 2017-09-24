package br.com.imovelcontrol.repository;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.helper.imovel.ImoveisQuerys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Imoveis extends JpaRepository<Imovel, Long>, ImoveisQuerys {

    @Query(value = "select * from imovel  where cep = ?1 and codigo_usuario = ?2", nativeQuery = true)
    Optional<Imovel> findByCep(String cep, Usuario usuario);
    Optional<Imovel> findByNomeAndDonoImovel(String nome, Usuario usuario);
    Optional<List<Imovel>>findByDonoImovel_Codigo(Long codigo);
    Optional<List<Imovel>>findByDonoImovelAndExcluido(Usuario usuario, Boolean excluido);

}
