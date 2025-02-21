package it.epicode.be_w7_exam_gestione_eventi.prenotazioni;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {

	private final PrenotazioneService prenotazioneService;

	@PostMapping
	public ResponseEntity<Prenotazione> prenotazioni(@RequestBody PrenotazioneRequest prenotazioneRequest) {
		Prenotazione prenotazione = prenotazioneService.prenotaEvento(prenotazioneRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(prenotazione);
	}

	@GetMapping
	public ResponseEntity<List<PrenotazioneResponse>> getUserBookings(Authentication authentication) {
		String username = authentication.getName();
		List<PrenotazioneResponse> prenotazioni = prenotazioneService.getUserBookings(username);
		return ResponseEntity.ok(prenotazioni);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {
		String username = authentication.getName();
		prenotazioneService.cancellaPrenotazione(id, username);
		return ResponseEntity.noContent().build();
	}
}
