var ImovelControl = ImovelControl || {};

ImovelControl.LocatarioCadastroRapido = (function () {

    var modal = $('#modalCadastroLocatario');
    var hrefOrigin = document.location.origin;

    function LocatarioCadastroRapido() {
        this.botaoAdicionar = $('.js-modal-locatario');
        this.locatarioAluguel = $('#codigoAluguel');
        this.botaoExcluirClick = $('.js-modal-excluir');
        this.botaoSalvar = $('.js-modal-salvar');

        var token = $("input[name='_crsf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

    }

    LocatarioCadastroRapido.prototype.iniciar = function () {
        this.botaoAdicionar.on('click', onBotaoAdicionarClick.bind(this));
        this.botaoExcluirClick.on('click', onBotaoExcluirClick.bind(this));
        this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
    };

    function onBotaoExcluirClick() {
        event.preventDefault();
        var inputCodigo = $('#codigoLocatario');
        $.ajax({
            url: hrefOrigin + '/locatario/' + inputCodigo.val(),
            method: 'GET',
            success: eventTargetPage.bind(this),
            error: onErrorExcluir.bind(this)
        });

    }

    function eventTargetPage() {
        window.location = window.location.href;
    }

    function onErrorExcluir(e) {
        var codigo = $('#codigoLocatario');
        if (codigo.val() == '') {
            swal('Erro!', 'Não há locatário para excluir', 'error');
        } else {
            swal('Erro!', e.responseText, 'error');
        }
    }

    function onErrorSalvar(e) {
        if (e.status == 200) {
            eventTargetPage();
        } else {
            swal('Erro!', e.responseText, 'error');
        }
    }

    function onBotaoAdicionarClick(evento) {
        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON(hrefOrigin + '/locatario/' + codigoAluguel, function (data) {
            meu_callback(data);
        });


        this.locatarioAluguel.val(codigoAluguel);
    }

    function onBotaoSalvarClick() {
        var nome = $('#nomeLocatario').val();
        var telefone = $('#telefone').val();
        var cpf = $('#cpf').val();
        var codigo = $('#codigoLocatario').val();
        var cpf = $('#cpf').val();
        var aluguel = $('#codigoAluguel').val();

        cpf = cpf.replace(/\D/g, '');

        telefone = telefone.replace(/\D/g, '');

        var locatario = {
            "codigo": codigo,
            "nome": nome,
            "telefone": telefone,
            "cpf": cpf,
            "aluguel": {"codigo": aluguel}
        };
        $.ajax({
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            url: hrefOrigin + '/locatario/novo/',
            data: JSON.stringify(locatario),
            success: eventTargetPage.bind(this),
            error: onErrorSalvar.bind(this)
        });
    }

    function meu_callback(conteudo) {

        var inputNome = $('#nomeLocatario');
        var inputCpf = $('#cpf');
        var inputTelefone = $('#telefone');
        var inputCodigo = $('#codigoLocatario');
        var dataEntrada = $('#entradaMes');

        if (!("erro" in conteudo)) {
            //Atualiza os campos com os valores.
            inputCodigo.val(conteudo.codigo);
            inputNome.val(conteudo.nome);
            inputCpf.val(conteudo.cpf);
            inputTelefone.val(conteudo.telefone);

            if (conteudo.dataInicio != null) {

                var dia = conteudo.dataInicio.dayOfMonth;
                var mes = conteudo.dataInicio.monthValue;
                var ano = conteudo.dataInicio.year;
                if (mes < 10) {
                    mes = "0" + mes;
                }
                if (dia < 10) {
                    dia = "0" + dia;
                }
                var data = dia + "/" + mes + "/" + ano;
            dataEntrada.text("Data da locação: " + data);
                dataEntrada.show();
            } else {
                dataEntrada.hide();
            }

            modal.modal();
        } //end if.
        else {
            console.log('erro');
        }
    }

    return LocatarioCadastroRapido;

}());

$(function () {
    var locatarioCadastroRapido = new ImovelControl.LocatarioCadastroRapido();
    locatarioCadastroRapido.iniciar();

});