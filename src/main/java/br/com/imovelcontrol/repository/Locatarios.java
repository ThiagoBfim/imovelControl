package br.com.imovelcontrol.repository;

import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Locatarios extends JpaRepository<Locatario, Long> {

    Optional<Locatario> findByAluguel(Aluguel codAluguel);
    Optional<Locatario> findByCpfAndExcluido(String cpf, Boolean aFalse);
    Optional<Locatario> findByTelefoneAndExcluido(String telefone, Boolean aFalse);
    Optional<Locatario> findByAluguelAndExcluido(Aluguel aluguel, Boolean aFalse);
}
