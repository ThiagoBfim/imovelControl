var ImovelControl = ImovelControl || {};
ImovelControl.AluguelCadastroRapido = (function () {

    var modal = $('#modalCadastroPagamento');
    var hrefOrigin = document.location.origin;

    function AluguelCadastroRapido() {
        this.modalPagamento = $('.js-modal-pagamento');
        this.codigoAluguel = $('#codigoAluguelPagamento');

        this.informacoesPagamentoVencidas = $('#informacoesPagamentoVencidas');

        var token = $("input[name='_crsf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }

    AluguelCadastroRapido.prototype.iniciar = function () {
        this.modalPagamento.on('click', onModalPagamentoClick.bind(this));
        this.informacoesPagamentoVencidas.on('change', onSelectChange.bind(this));
    };

    function  onSelectChange(evento) {

        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON(hrefOrigin + '/pagamento/'
            + this.informacoesPagamentoVencidas.val(), function (data) {
            meu_callback_select(data);
        });
    }

    function onModalPagamentoClick(evento) {
        this.informacoesPagamentoVencidas.empty();
        this.informacoesPagamentoVencidas.append($("<option>").attr('value',"").text("Mensalidades Vencidas"));
        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON(hrefOrigin + '/pagamento/aluguel/'
            + codigoAluguel, function (data) {
            meu_callback_open_modal(data);
        });

        this.codigoAluguel.val(codigoAluguel);
    }


    function meu_callback_select(conteudo){

        populateInformacaoPagamento(conteudo);

    }


    function meu_callback_open_modal(informacaoPagamentoModal) {

        var informacoesPagamentoVencidas = $('#informacoesPagamentoVencidas');


        informacaoPagamentoModal.informacaoPagamentoList.forEach(function(e) {
            var mes = e.dataMensal.monthValue;
            var ano = e.dataMensal.year;
            if(mes < 10){
                mes = "0" + mes;
            }
            var mensalidade = "01/"+ mes + "/" + ano;
            informacoesPagamentoVencidas.append($("<option>").attr('value', e.codigo ).text(mensalidade));
        });

        var conteudo = informacaoPagamentoModal.informacaoPagamento;

        populateInformacaoPagamento(conteudo);
    }

    function changeBootstrapSwitch(codigo, valor, campoDiv) {
        if (valor == null) {
            $(campoDiv).hide();
        } else {
            $(campoDiv).show();
        }
        $(codigo).bootstrapSwitch('state', valor);
    }

    function populateInformacaoPagamento(conteudo) {
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

            if(mes < 10){
                mes = "0" + mes;
            }

            mensalidade.text("Mensalidade: " + "01/" + mes + "/" + ano);


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