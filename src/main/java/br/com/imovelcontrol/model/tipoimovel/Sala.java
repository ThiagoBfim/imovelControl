package br.com.imovelcontrol.model.tipoimovel;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "SL")
public class Sala extends Aluguel {

	
}
