package it.epicode.be_w7_exam_gestione_eventi.eventi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/eventi")
@RequiredArgsConstructor
public class EventoController {

	private final EventoService eventoService;

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ORGANIZER')")
	public ResponseEntity<Evento> createEvent(@RequestBody EventoRequest eventoRequest, Authentication authentication) {
		String organizerUsername = authentication.getName();
		Evento createdEvent = eventoService.createEvent(eventoRequest, organizerUsername);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Evento> updateEvent(@PathVariable Long id, @RequestBody EventoRequest eventoRequest, Authentication authentication) {
		String organizerUsername = authentication.getName();
		Evento updatedEvent = eventoService.updateEvent(id, eventoRequest, organizerUsername);
		return ResponseEntity.ok(updatedEvent);
	}


	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ORGANIZER')")
	public ResponseEntity<Void> deleteEvent(@PathVariable Long id, Authentication authentication) {
		String organizerUsername = authentication.getName();
		eventoService.deleteEvent(id, organizerUsername);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<EventoResponse>> getAllEvents() {
		return ResponseEntity.ok(eventoService.getAllEvents());
	}

	@GetMapping("/search")
	public ResponseEntity<List<EventoResponse>> searchEvents(@RequestParam(required = false) String titolo,
	                                                         @RequestParam(required = false) String luogo) {
		return ResponseEntity.ok(eventoService.searchEvents(titolo, luogo));
	}
}
