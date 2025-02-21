package it.epicode.be_w7_exam_gestione_eventi.prenotazioni;

import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUser;
import it.epicode.be_w7_exam_gestione_eventi.eventi.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
	List<Prenotazione> findByUtente(AppUser utente);
	boolean existsByUtenteAndEvento(AppUser utente, Evento evento);  // Metodo per evitare prenotazioni duplicate
}
