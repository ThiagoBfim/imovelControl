package br.com.imovelcontrol.repository;

import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.repository.helper.locatario.LocatarioQuerys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Locatarios extends JpaRepository<Locatario, Long> {

}
