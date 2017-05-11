package br.com.imovelcontrol.repository;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.repository.helper.imovel.ImoveisQuerys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Imoveis extends JpaRepository<Imovel, Long>, ImoveisQuerys {

}
