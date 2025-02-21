package it.epicode.be_w7_exam_gestione_eventi.eventi;

import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUser;
import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

	private final EventoRepository eventoRepository;
	private final AppUserRepository appUserRepository;


	public Evento createEvent(EventoRequest eventoRequest, String organizerUsername) {
		AppUser organizer = findUserByUsername(organizerUsername);

		Evento evento = new Evento();
		evento.setTitolo(eventoRequest.getTitolo());
		evento.setDescrizione(eventoRequest.getDescrizione());
		evento.setData(eventoRequest.getData());
		evento.setLuogo(eventoRequest.getLuogo());
		evento.setPostiDisponibili(eventoRequest.getPostiDisponibili());
		evento.setOrganizzatore(organizer);

		return eventoRepository.save(evento);
	}

	public Evento updateEvent(Long id, EventoRequest eventoRequest, String organizerUsername) {
		Evento evento = eventoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

		if (!evento.getOrganizzatore().getUsername().equals(organizerUsername)) {
			throw new AccessDeniedException("Non hai il permesso di modificare questo evento");
		}

		evento.setTitolo(eventoRequest.getTitolo());
		evento.setDescrizione(eventoRequest.getDescrizione());
		evento.setData(eventoRequest.getData());
		evento.setLuogo(eventoRequest.getLuogo());
		evento.setPostiDisponibili(eventoRequest.getPostiDisponibili());

		return eventoRepository.save(evento);
	}

	public void deleteEvent(Long id, String organizerUsername) {
		Evento evento = eventoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

		if (!evento.getOrganizzatore().getUsername().equals(organizerUsername)) {
			throw new AccessDeniedException("Non hai il permesso di eliminare questo evento");
		}

		eventoRepository.delete(evento);
	}

	public List<EventoResponse> getAllEvents() {
		return eventoRepository.findAll().stream()
			.map(evento -> new EventoResponse(
				evento.getId(),
				evento.getTitolo(),
				evento.getDescrizione(),
				evento.getData(),
				evento.getLuogo(),
				evento.getPostiDisponibili()))
			.collect(Collectors.toList());
	}

	private AppUser findUserByUsername(String username) {
		return appUserRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
	}

	public List<EventoResponse> searchEvents(String titolo, String luogo) {
		return eventoRepository.findAll().stream()
			.filter(evento -> (titolo == null || evento.getTitolo().contains(titolo)) &&
				(luogo == null || evento.getLuogo().contains(luogo)))
			.map(evento -> new EventoResponse(
				evento.getId(),
				evento.getTitolo(),
				evento.getDescrizione(),
				evento.getData(),
				evento.getLuogo(),
				evento.getPostiDisponibili()))
			.collect(Collectors.toList());
	}
}
