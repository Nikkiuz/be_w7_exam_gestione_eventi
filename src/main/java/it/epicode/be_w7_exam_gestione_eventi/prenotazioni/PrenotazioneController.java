package it.epicode.be_w7_exam_gestione_eventi.prenotazioni;

import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUser;
import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {

	private final PrenotazioneService prenotazioneService;
	private final AppUserRepository appUserRepository;

	@PostMapping
	public ResponseEntity<Prenotazione> prenotazioni(@RequestBody PrenotazioneRequest prenotazioneRequest) {
		Prenotazione prenotazione = prenotazioneService.prenotaEvento(prenotazioneRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(prenotazione);
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<PrenotazioneResponse>> getPrenotazioniUtente(Authentication authentication) {
		String username = authentication.getName();

		AppUser user = appUserRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

		List<PrenotazioneResponse> prenotazioni = prenotazioneService.getPrenotazioniUtente(user);

		return ResponseEntity.ok(prenotazioni);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {
		String username = authentication.getName();
		prenotazioneService.cancellaPrenotazione(id, username);
		return ResponseEntity.noContent().build();
	}

}
