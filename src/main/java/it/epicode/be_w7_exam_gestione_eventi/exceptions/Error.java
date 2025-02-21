package it.epicode.be_w7_exam_gestione_eventi.exceptions;

import lombok.Data;

@Data
public class Error {
	private String message;
	private String details;
	private String status;
}
