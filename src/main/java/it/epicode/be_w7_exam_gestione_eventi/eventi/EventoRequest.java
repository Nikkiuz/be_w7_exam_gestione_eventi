package it.epicode.be_w7_exam_gestione_eventi.eventi;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequest {
	@NotNull
	private String titolo;

	@NotNull
	private String descrizione;

	@NotNull
	private LocalDate data;

	@NotNull
	private String luogo;

	@Min(1)
	private int postiDisponibili;
}
