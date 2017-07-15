var Brewer = Brewer || {};

Brewer.AluguelCadastroRapido = (function () {

    var modal = $('#modalCadastroPagamento');

    function AluguelCadastroRapido() {
        this.botaoSalvar = $('.js-modal-pagamento');
        this.codigoAluguel = $('#codigoAluguel');


        var token = $("input[name='_crsf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }

    AluguelCadastroRapido.prototype.iniciar = function () {
        this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
    }

    function onBotaoSalvarClick(evento) {
        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON('http://localhost:8080/ImovelControl/imovel/aluguel/pagamento/'
            + codigoAluguel, function (data) {
            meu_callback(data);
        });


        this.codigoAluguel.val(codigoAluguel);
    }

    function meu_callback(conteudo) {

        var inputAguaInclusa = $('#aguaInclusa');
        var luzInclusa = $('#luzInclusa');
        var internetInclusa = $('#internetInclusa');
        var iptuIncluso = $('#iptuIncluso');
        var possuiCondominio = $('#possuiCondominio');
        var inputCodigo = $('#codigoPagamento');

        if (!("erro" in conteudo)) {
            inputCodigo.val(conteudo.codigo);
            //Atualiza os campos com os valores.
            if (conteudo.aguaInclusa == true) {
                inputAguaInclusa.show();
            } else {
                inputAguaInclusa.hide();
            }
            if (conteudo.luzInclusa == true) {
                luzInclusa.show();
            } else {
                luzInclusa.hide();
            }
            if (conteudo.internetInclusa == true) {
                internetInclusa.show();
            } else {
                internetInclusa.hide();
            }
            if (conteudo.iptuIncluso == true) {
                iptuIncluso.show();
            } else {
                iptuIncluso.hide();
            }
            if (conteudo.possuiCondominio == true) {
                possuiCondominio.show();
            } else {
                possuiCondominio.hide();
            }

            modal.modal();
        } //end if.
        else {
            console.log('erro');
        }
    }

    return AluguelCadastroRapido;

}());

$(function () {
    var aluguelCadastroRapido = new Brewer.AluguelCadastroRapido();
    aluguelCadastroRapido.iniciar();

})