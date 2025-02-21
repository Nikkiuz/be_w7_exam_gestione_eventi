package it.epicode.be_w7_exam_gestione_eventi.prenotazioni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneResponse {

	private Long id;
	private String username;
	private String titoloEvento;
	private LocalDate dataEvento;
	private String luogoEvento;

}
