package br.com.bomfim.model.tipoimovel;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "SL")
public class Sala extends Aluguel {

	
}
