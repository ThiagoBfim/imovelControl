var Brewer = Brewer || {};

Brewer.DialogoExcluir = (function () {

    function DialogoExcluir() {
        this.exclusaoBtn = $('.js-exclusao-btn');

        var token = $("input[name='_crsf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }

    DialogoExcluir.prototype.iniciar = function () {
        this.exclusaoBtn.on('click', onExcluirClicado.bind(this));

        if (window.location.search.indexOf('excluido') > -1) {
            swal({
                    title: 'Pronto',
                    text: 'Excluído com seuceso!',
                    showCancelButton: false,
                    confirmButtonText: 'OK'
                },
                onRemoveExcluido.bind(this));

        }
    }
    function onRemoveExcluido() {
        var urlAtual = window.location.href;
        window.location = urlAtual.replace('?excluido', '');
    }

    function onExcluirClicado(evento) {
        event.preventDefault();
        var botaoClicado = $(evento.currentTarget);
        var url = botaoClicado.data('url');
        var objeto = botaoClicado.data('objeto');

        swal({
            title: 'Tem certeza?',
            text: 'Excluir "' + objeto
            + '"? Você não poderá recuperar depois.',
            showCancelButton: true,
            confirmButtonColor: '#DD6855',
            confirmButtonText: 'Sim, exclua agora!',
            closeOnConfirm: false
        }, onExcluirConfirmado.bind(this, url));
    }

    function onExcluirConfirmado(url) {
        $.ajax({
            url: url,
            method: 'DELETE',
            success: onExcluidoSucesso.bind(this),
            error: onErrorExcluir.bind(this)
        })
    }

    function onExcluidoSucesso() {
        var urlAtual = window.location.href;
        var separador = urlAtual.indexOf('?') > -1 ? '&' : '?';
        var novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : urlAtual
            + separador + 'excluido';

        window.location = novaUrl;
    }

    function onErrorExcluir(e) {
        swal('Oops!', e.responseText, 'error');
    }

    return DialogoExcluir;

}());

$(function () {
    var dialogoExcluir = new Brewer.DialogoExcluir();
    dialogoExcluir.iniciar();

});
