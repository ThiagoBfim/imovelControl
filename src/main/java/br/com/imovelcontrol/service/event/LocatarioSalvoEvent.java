package br.com.imovelcontrol.service.event;

import br.com.imovelcontrol.model.Locatario;

/**
 * Created by marcosfellipec on 15/06/17.
 */
public class LocatarioSalvoEvent {
    private Locatario locatario;

    public LocatarioSalvoEvent(Locatario locatario){
        this.locatario  = locatario;
    }

}
