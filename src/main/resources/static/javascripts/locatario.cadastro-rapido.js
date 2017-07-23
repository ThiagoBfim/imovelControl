var ImovelControl = ImovelControl || {};

ImovelControl.LocatarioCadastroRapido = (function () {

    var modal = $('#modalCadastroLocatario');

    function LocatarioCadastroRapido() {
        this.botaoSalvar = $('.js-modal-locatario');
        this.locatarioAluguel = $('#codigoAluguel');
        this.botaoExcluirClick = $('.js-modal-excluir');

        var token = $("input[name='_crsf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

    }

    LocatarioCadastroRapido.prototype.iniciar = function () {
        this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
        this.botaoExcluirClick.on('click', onBotaoExcluirClick.bind(this));
    }

    function onBotaoExcluirClick() {
        event.preventDefault();
        var inputCodigo = $('#codigoLocatario');
        console.log(inputCodigo.val());
        $.ajax({
            url: 'http://localhost:8080/ImovelControl/locatario/' + inputCodigo.val(),
            method: 'GET',
            success: onExcluidoSucesso.bind(this),
            error: function () {
                onErrorExcluir.bind(this)
            }
        });

    }

    function onExcluidoSucesso() {
        swal({
                title: 'Pronto',
                text: 'Excluído com seuceso!',
                showCancelButton: false,
                confirmButtonText: 'OK'
            },
            onRemoveExcluido.bind(this));

    }

    function onRemoveExcluido() {
        window.location = window.location.href;
    }

    function onErrorExcluir(e) {
        swal('Oops!', e.responseText, 'error');
    }

    function onBotaoSalvarClick(evento) {
        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON('http://localhost:8080/ImovelControl/locatario/' + codigoAluguel, function (data) {
            meu_callback(data);
        });


        this.locatarioAluguel.val(codigoAluguel);
    }

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

})