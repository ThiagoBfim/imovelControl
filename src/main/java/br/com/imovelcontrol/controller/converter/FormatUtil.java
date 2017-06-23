package br.com.imovelcontrol.controller.converter;

/**
 * Created by Usuario on 23/06/2017.
 */
public class FormatUtil {

    public static String removerMascara(String str){
        return str.replaceAll("\\D", "");
    }
}
