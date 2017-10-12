var ImovelControl = ImovelControl || {};

ImovelControl.LocatarioCadastroRapido = (function () {

    var modal = $('#modalCadastroLocatario');

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
        this.botaoSalvar.on('click',onBotaoSalvarClick.bind(this));
    };

    function onBotaoExcluirClick() {
        event.preventDefault();
        var inputCodigo = $('#codigoLocatario');
        console.log(inputCodigo.val());
        $.ajax({
            url: document.location.origin + '/ImovelControl/locatario/' + inputCodigo.val(),
            method: 'GET',
            success: onExcluidoSucesso.bind(this),
            error: onErrorExcluir.bind(this)
        });

    }

    function onExcluidoSucesso() {
        modal.hide();
        swal({
                title: 'Pronto',
                text: 'Excluído com sucesso!',
                showCancelButton: false,
                confirmButtonText: 'OK'
            },
            onRemoveExcluido.bind(this));

    }

    function onSalvarSucesso(){
        console.log("salvo");
        modal.hide();
        swal({
            title: 'Pronto',
            text: 'Salvo com Sucesso',
            showCancelButton: false,
            confirmButtonTexto: 'OK'
        }, onRemoveSalvo.bind(this));
    }

    function onRemoveExcluido() {
        window.location = window.location.href;
    }

    function onRemoveSalvo(){
        window.location = window.location.href;
    }

    function onErrorExcluir(e) {
        var codigo = $('#codigoLocatario');
        if (codigo.val() == ''){
            swal('Erro!', 'Não há locatário para excluir', 'error');
        }else{
            swal('Erro!', e.responseText, 'error');
        }
    }

    function onErrorSalvar(e){
        console.log(e);
        if(e.status == 200){
            onSalvarSucesso();
        } else {
            swal('Erro!',e.responseText, 'error');
        }
    }

    function onBotaoAdicionarClick(evento) {
        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON('http://localhost:8080/ImovelControl/locatario/' + codigoAluguel, function (data) {
            meu_callback(data);
        });


        this.locatarioAluguel.val(codigoAluguel);
    }
    function onBotaoSalvarClick(evento) {
        var botaoClicado = $(evento.currentTarget);
        var nome = $('#nomeLocatario').val();
        var telefone = $('#telefone').val();
        var cpf = $('#cpf').val();
        var codigo = $('#codigoLocatario').val();
        var cpf = $('#cpf').val();
        var aluguel = $('#codigoAluguel').val();

        cpf = cpf.replace(/\D/g, '');

        telefone = telefone.replace(/\D/g, '');

        var locatario = {"codigo": codigo,"nome": nome, "telefone": telefone, "cpf": cpf, "aluguel":{"codigo":aluguel }};
        $.ajax({
            type: "POST",
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            url:  document.location.origin + '/ImovelControl/locatario/novo/',
            data: JSON.stringify(locatario),
            success: onSalvarSucesso.bind(this),
            error:onErrorSalvar.bind(this)
        });
    }
    //     $.ajax({
    //         url: 'http://localhost:8080/ImovelControl/locatario/novo?nome=teste',
    //         method: 'POST',
    //         success: onSalvarSucesso.bind(this),
    //
    //         error: onErrorSalvar.bind(this)
    //
    //     });
    // }

    function meu_callback(conteudo) {

        var inputNome = $('#nomeLocatario');
        var inputCpf = $('#cpf');
        var inputTelefone = $('#telefone');
        var inputCodigo = $('#codigoLocatario');

        if (!("erro" in conteudo)) {
            //Atualiza os campos com os valores.
            inputCodigo.val(conteudo.codigo);
            inputNome.val(conteudo.nome);
            inputCpf.val(conteudo.cpf);
            inputTelefone.val(conteudo.telefone);
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