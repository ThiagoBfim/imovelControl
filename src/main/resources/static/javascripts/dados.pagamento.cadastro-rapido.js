var ImovelControl = ImovelControl || {};

ImovelControl.AluguelCadastroRapido = (function () {

    var modal = $('#modalCadastroPagamento');
    var hrefOrigin = document.location.origin;

    function AluguelCadastroRapido() {
        this.botaoSalvar = $('.js-modal-pagamento');
        this.codigoAluguel = $('#codigoAluguelPagamento');

        var token = $("input[name='_crsf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }

    AluguelCadastroRapido.prototype.iniciar = function () {
        this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
    };

    function onBotaoSalvarClick(evento) {
        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON(hrefOrigin + '/pagamento/'
            + codigoAluguel, function (data) {
            meu_callback(data);
        });

        this.codigoAluguel.val(codigoAluguel);
    }

    function meu_callback(conteudo) {


        function changeBootstrapSwitch(codigo, valor, campoDiv) {
            if (valor == null) {
                $(campoDiv).hide();
            } else {
                $(campoDiv).show();
            }
            $(codigo).bootstrapSwitch('state', valor);
        }

        var inputCodigo = $('#codigoPagamento');
        var inputValor = $('#valorMensal');
        var checkMensalidade = $('.checkBoxPago');
        var mensalidade = $('#mensalidade');

        if (!("erro" in conteudo)) {
            inputCodigo.val(conteudo.codigo);
            var valor = conteudo.valor.toString();
            var valores = valor.split('.');
            if (valores[1] != null) {
                if (valores[1] < 10) {
                    valores[1] *= 10;
                }
                valor = valores[0] + ',' + valores[1];
                inputValor.val(valor);
            } else {
                inputValor.val(valor + ',00');
            }
            checkMensalidade.prop('checked', conteudo.pago);

            var mes = conteudo.dataMensal.monthValue;
            var ano = conteudo.dataMensal.year;

            mensalidade.text("Mensalidade: " +  "01/" + mes + "/" + ano );


            var dadoPagamento = $('.dadoPagamento');
            if (conteudo.estaAlugado) {
                dadoPagamento.show();
            } else {
                dadoPagamento.hide();
            }

            changeBootstrapSwitch('#inputAguaInclusa', conteudo.aguaInclusa, '#aguaInclusa');

            changeBootstrapSwitch('#inputLuzInclusa', conteudo.luzInclusa, '#luzInclusa');

            changeBootstrapSwitch('#inputInternetInclusa', conteudo.internetInclusa, '#internetInclusa');

            changeBootstrapSwitch('#inputIptuIncluso', conteudo.iptuIncluso, '#iptuIncluso');

            changeBootstrapSwitch('#inputPossuiCondominio', conteudo.possuiCondominio, '#possuiCondominio');

            modal.modal();
            modal.on('shown.bs.modal', function () {
                inputValor.focus();
            });
        } //end if.
        else {
            console.log('erro');
        }
    }

    return AluguelCadastroRapido;

}());

$(function () {
    var aluguelCadastroRapido = new ImovelControl.AluguelCadastroRapido();
    aluguelCadastroRapido.iniciar();

});