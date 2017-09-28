package br.com.imovelcontrol.controller.converter;

import java.math.BigDecimal;
import java.text.NumberFormat;

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

    public static String inserirCepMascara(String cpf) {
        return cpf.replaceAll("(\\d{5})(\\d{3})", "$1-$2");
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

    public static String formatBigDecimal(BigDecimal valor) {
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }
        return NumberFormat.getCurrencyInstance().format(valor);
    }
}
