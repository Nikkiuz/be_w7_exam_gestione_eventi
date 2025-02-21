package it.epicode.be_w7_exam_gestione_eventi.eventi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long> {
	Optional<Evento> findByTitolo(String titolo);
	boolean existsByTitolo(String titolo);
}
