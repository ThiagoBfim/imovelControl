package br.com.imovelcontrol.repository;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.helper.imovel.ImoveisQuerys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Imoveis extends JpaRepository<Imovel, Long>, ImoveisQuerys {

    Optional<Imovel> findByEndereco_CepAndDonoImovelAndExcluido(String cep, Usuario usuario, Boolean excluido);

    Optional<Imovel> findByNomeAndDonoImovelAndExcluido(String nome, Usuario usuario, Boolean excluido);

    Optional<List<Imovel>> findByDonoImovel_CodigoAndExcluido(Long codigo, Boolean excluido);

    Optional<List<Imovel>> findByDonoImovelAndExcluido(Usuario usuario, Boolean excluido);

    List<Imovel>  findByDonoImovel_Codigo(Long codigo);
}
