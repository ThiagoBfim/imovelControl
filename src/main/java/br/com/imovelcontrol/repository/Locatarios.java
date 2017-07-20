package br.com.imovelcontrol.repository;

import java.util.Optional;

import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Locatarios extends JpaRepository<Locatario, Long> {

    Optional<Locatario> findByAluguel(Aluguel codAluguel);
    Optional<Locatario> findByCpf(String cpf);
    Optional<Locatario> findByTelefone(String telefone);
}
