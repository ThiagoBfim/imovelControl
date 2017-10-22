/**
 * Created by marcosfellipec on 21/10/17.
 */
var ImovelControl = ImovelControl || {};

ImovelControl.BuscarCep = (function () {

    var locatario = document.getElementById('locatario');
    var codigo = $('td[id=codigoAluguelLocatario]');
    console.log(codigo.text());
    console.log(locatario);
    function BuscarAluguelLocatario() {
        this.labelFindLocatario =$('.labelLocatario');
    }

    BuscarAluguelLocatario.prototype.iniciar = function () {
        this.labelFindLocatario.on('load', onExcluirClicado.bind(this));
        if (codigo.text() == "1"){

        }
    };

    function onExcluirClicado () {
        locatario.innerHTML = 'Deu certo otários';
    }

    function meu_callback(conteudo) {
        if (!("erro" in conteudo)) {
            //Atualiza os campos com os valores.

            inputRua.val(conteudo.logradouro);
            inputBairro.val(conteudo.bairro);
            inputCidade.val(conteudo.localidade);
            inputUf.val(conteudo.uf);
            labelCepInvalido.hide();
        } //end if.
        else {
            //CEP não Encontrado.
            limpa_formulario_cep();
            labelCepInvalido.html("CEP não encontrado.");
            labelCepInvalido.show();
        }
    }

    return BuscarAluguelLocatario;
}());

$(function () {
    var BuscarAluguelLocatario = new ImovelControl.BuscarCep();
    BuscarAluguelLocatario.iniciar();
});