package br.com.imovelcontrol.controller.converter;

/**
 * Created by Usuario on 23/06/2017.
 */
public class FormatUtil {

    public static String removerMascara(String str) {
        return str.replaceAll("\\D", "").replaceAll(" ", "");
    }

    public static String inserirCpfMascara(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String inserirTelefoneMascara(String telefone) {
        if (telefone.length() == 11) {
            telefone = '(' + telefone.substring(0, 2) + ")" + ' ' + telefone.substring(2, 7) + "-"
                    + telefone.substring(7, telefone.length());
        } else {
            telefone = '(' + telefone.substring(0, 2) + ")" + ' ' + telefone.substring(2, 6) + "-"
                    + telefone.substring(6, telefone.length());
        }

        return telefone;
    }
}
