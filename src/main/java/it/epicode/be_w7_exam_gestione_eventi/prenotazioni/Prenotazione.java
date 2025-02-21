package it.epicode.be_w7_exam_gestione_eventi.prenotazioni;

import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUser;
import it.epicode.be_w7_exam_gestione_eventi.eventi.Evento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Prenotazione {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private AppUser utente;

	@ManyToOne
	private Evento evento;
}
