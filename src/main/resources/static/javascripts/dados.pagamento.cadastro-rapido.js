var ImovelControl = ImovelControl || {};
ImovelControl.AluguelCadastroRapido = (function () {

    var modal = $('#modalCadastroPagamento');
    var hrefOrigin = document.location.origin;
    var multaRow = $('.multaRow');

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

    function onSelectChange() {
        var codigoInformacaoPagamento = this.informacoesPagamentoVencidas.val();
        if (codigoInformacaoPagamento != '') {
            $.getJSON(hrefOrigin + '/pagamento/'
                + this.informacoesPagamentoVencidas.val(), function (data) {
                meu_callback_select(data);
            });
        }
    }

    function onModalPagamentoClick(evento) {
        multaRow.hide();
        this.informacoesPagamentoVencidas.empty();
        this.informacoesPagamentoVencidas.append($("<option>").attr('value', "").text("Mensalidades Vencidas"));
        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON(hrefOrigin + '/pagamento/aluguel/'
            + codigoAluguel, function (data) {
            meu_callback_open_modal(data);
        });

        this.codigoAluguel.val(codigoAluguel);
    }


    function meu_callback_select(conteudo) {
        var vencido = $('#vencido');
        var multa = $('#multa');
        vencido.val(conteudo.atrasado);
        createMaskForMoney(conteudo.multa, multa);
        if (conteudo.atrasado) {
            multaRow.show();
        } else {
            multaRow.hide();
        }

        populateInformacaoPagamento(conteudo);

    }


    function meu_callback_open_modal(informacaoPagamentoModal) {
        var informacoesPagamentoVencidas = $('#informacoesPagamentoVencidas');


        informacaoPagamentoModal.informacaoPagamentoList.forEach(function (e) {
            var mes = e.dataMensal.monthValue;
            var ano = e.dataMensal.year;
            if (mes < 10) {
                mes = "0" + mes;
            }
            var mensalidade =  mes + "/" + ano;
            if(e.locatario.nome != null) {
                mensalidade += " - " + e.locatario.nome;
            }
            informacoesPagamentoVencidas.append($("<option>").attr('value', e.codigo).text(mensalidade));
        });

        /*Se so tiver um elemento significa que so tem a mensalidade atual, então não preciso exibir esse select*/
        if (informacaoPagamentoModal.informacaoPagamentoList.length <= 1) {
            console.log(informacaoPagamentoModal.informacaoPagamentoList.length);
            $('.selectContainer').hide();
        } else {
            $('.selectContainer').show();
        }

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

    function createMaskForMoney(conteudo, inputValor) {
        var valor = conteudo.toString();
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
        inputValor.focus();
    }

    function populateInformacaoPagamento(conteudo) {
        var inputCodigo = $('#codigoPagamento');
        var inputValor = $('#valorMensal');
        var checkMensalidade = $('.checkBoxPago');
        var mensalidade = $('#mensalidade');

        if (!("erro" in conteudo)) {
            inputCodigo.val(conteudo.codigo);
            createMaskForMoney(conteudo.valor, inputValor);
            checkMensalidade.prop('checked', conteudo.pago);

            var mes = conteudo.dataMensal.monthValue;
            var ano = conteudo.dataMensal.year;

            if (mes < 10) {
                mes = "0" + mes;
            }

            mensalidade.text("Mensalidade: " + mes + "/" + ano);


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