package it.epicode.be_w7_exam_gestione_eventi.eventi;

import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Evento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titolo;
	private String descrizione;
	private LocalDate data;
	private String luogo;
	private int postiDisponibili;

	@ManyToOne
	private AppUser organizzatore;
}