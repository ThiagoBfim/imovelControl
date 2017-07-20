package br.com.imovelcontrol.repository;

import br.com.imovelcontrol.model.Endereco;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.repository.helper.imovel.ImoveisQuerys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Imoveis extends JpaRepository<Imovel, Long>, ImoveisQuerys {

    @Query(value="select * from imovel  where cep = ?1", nativeQuery = true)
    Optional<List<Imovel>> findByCep(String cep);

    Optional<Imovel>findByNome(String nome);

    @Query(value = "select * from imovel where codigo_usuario = ?1", nativeQuery = true)
    Optional<List<Imovel>>findByUsuario(@Param("codigo_usuario") long codigo_usuario );

}
