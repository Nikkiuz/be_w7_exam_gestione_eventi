package it.epicode.be_w7_exam_gestione_eventi.prenotazioni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneRequest {
	private Long utenteId;
	private Long eventoId;
}