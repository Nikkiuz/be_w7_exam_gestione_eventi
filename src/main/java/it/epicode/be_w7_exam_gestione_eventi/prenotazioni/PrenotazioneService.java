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

		if (prenotazioneRepository.existsByUtenteAndEvento(utente, evento)) {
			throw new IllegalStateException("Hai già prenotato questo evento.");
		}

		if (evento.getPostiDisponibili() <= 0) {
			throw new IllegalStateException("Non ci sono posti disponibili per questo evento. Posti rimanenti: " + evento.getPostiDisponibili());
		}

		evento.setPostiDisponibili(evento.getPostiDisponibili() - 1);
		eventoRepository.save(evento);

		Prenotazione prenotazione = new Prenotazione();
		prenotazione.setUtente(utente);
		prenotazione.setEvento(evento);

		return prenotazioneRepository.save(prenotazione);
	}

	public List<PrenotazioneResponse> getPrenotazioniUtente(AppUser user) {

		return prenotazioneRepository.findByUtente(user).stream()
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

		if (!prenotazione.getUtente().getUsername().equals(username)) {
			throw new AccessDeniedException("Non hai il permesso di cancellare questa prenotazione");
		}

		Evento evento = prenotazione.getEvento();
		evento.setPostiDisponibili(evento.getPostiDisponibili() + 1);
		eventoRepository.save(evento);

		prenotazioneRepository.delete(prenotazione);
	}


}
