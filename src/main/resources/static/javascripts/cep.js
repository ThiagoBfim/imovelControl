var ImovelControl = ImovelControl || {};

ImovelControl.BuscarCep = (function () {

    var inputRua = $('input[id=rua]');
    var inputBairro = $('input[id=bairro]');
    var inputCidade = $('input[id=cidade]');
    var inputUf = $('input[id=uf]');
    var inputCep = $('input[id=cep]');
    var labelCepInvalido = $('label[id=cepInvalido]');

    function BuscarCep() {
    }

    BuscarCep.prototype.iniciar = function () {
        if (inputCep.val() == '') {
            disabledAll();
        }
        labelCepInvalido.hide();
        inputCep.blur(pesquisacep.bind(this));

    };


    function disabledAll() {
        inputRua.attr('disabled', 'disabled');
        inputBairro.attr('disabled', 'disabled');
        inputCidade.attr('disabled', 'disabled');
        inputUf.attr('disabled', 'disabled');
    }

    function limpa_formulario_cep() {
        inputRua.val('');
        inputBairro.val('');
        inputCidade.val('');
        inputUf.val('');
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

    function pesquisacep() {

        //Nova variável "cep" somente com dígitos.
        var cep = inputCep.val();

        //Verifica se campo cep possui valor informado.
        if (cep.length == 9) {
            //Preenche os campos com "..." enquanto consulta webservice.
            inputRua.val("...").removeAttr('disabled');
            inputBairro.val("...").removeAttr('disabled');
            inputCidade.val("...").removeAttr('disabled');
            inputUf.val("...").removeAttr('disabled');

            $.getJSON('//viacep.com.br/ws/' + cep + '/json/?&callback=?', function (data) {
                meu_callback(data);
            })

        } else {
            //cep sem valor, limpa formulário.
            labelCepInvalido.html('CEP informado é inválido.');
            labelCepInvalido.show();
            limpa_formulario_cep();
            disabledAll();
        }
    }

    return BuscarCep;
}());

$(function () {
    var buscaCep = new ImovelControl.BuscarCep();
    buscaCep.iniciar();
});