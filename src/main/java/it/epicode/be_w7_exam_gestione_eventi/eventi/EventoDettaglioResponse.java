package it.epicode.be_w7_exam_gestione_eventi.eventi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDettaglioResponse {
	private Long id;
	private String titolo;
	private String descrizione;
	private String data;
	private String luogo;
	private int postiDisponibili;
	private String organizzatore;
}