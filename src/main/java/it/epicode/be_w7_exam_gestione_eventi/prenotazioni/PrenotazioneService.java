package it.epicode.be_w7_exam_gestione_eventi.prenotazioni;

import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUser;
import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUserRepository;
import it.epicode.be_w7_exam_gestione_eventi.eventi.Evento;
import it.epicode.be_w7_exam_gestione_eventi.eventi.EventoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrenotazioneService {

	private final PrenotazioneRepository prenotazioneRepository;
	private final EventoRepository eventoRepository;
	private final AppUserRepository appUserRepository;

	public Prenotazione prenotaEvento(PrenotazioneRequest prenotazioneRequest) {
		AppUser utente = appUserRepository.findById(prenotazioneRequest.getUtenteId())
			.orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
		Evento evento = eventoRepository.findById(prenotazioneRequest.getEventoId())
			.orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

		// Verifica se l'utente ha già prenotato l'evento
		if (prenotazioneRepository.existsByUtenteAndEvento(utente, evento)) {
			throw new IllegalStateException("Hai già prenotato questo evento.");
		}

		// Verifica se ci sono posti disponibili
		if (evento.getPostiDisponibili() <= 0) {
			throw new IllegalStateException("Non ci sono posti disponibili per questo evento. Posti rimanenti: " + evento.getPostiDisponibili());
		}

		// Decrementa i posti disponibili
		evento.setPostiDisponibili(evento.getPostiDisponibili() - 1);
		eventoRepository.save(evento);

		// Crea e salva la prenotazione
		Prenotazione prenotazione = new Prenotazione();
		prenotazione.setUtente(utente);
		prenotazione.setEvento(evento);

		return prenotazioneRepository.save(prenotazione);
	}

	public List<PrenotazioneResponse> getUserBookings(String username) {
		AppUser utente = appUserRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

		// Recupera le prenotazioni dell'utente e mappa i dettagli
		return prenotazioneRepository.findByUtente(utente).stream()
			.map(prenotazione -> new PrenotazioneResponse(
				prenotazione.getId(),
				prenotazione.getUtente().getUsername(),
				prenotazione.getEvento().getTitolo(),
				prenotazione.getEvento().getData(),
				prenotazione.getEvento().getLuogo()))
			.collect(Collectors.toList());
	}

	public void cancellaPrenotazione(Long id, String username) {
		Prenotazione prenotazione = prenotazioneRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));

		// Verifica che l'utente possa cancellare solo la propria prenotazione
		if (!prenotazione.getUtente().getUsername().equals(username)) {
			throw new AccessDeniedException("Non hai il permesso di cancellare questa prenotazione");
		}

		// Ripristina i posti disponibili
		Evento evento = prenotazione.getEvento();
		evento.setPostiDisponibili(evento.getPostiDisponibili() + 1);
		eventoRepository.save(evento);

		// Elimina la prenotazione
		prenotazioneRepository.delete(prenotazione);
	}
}
