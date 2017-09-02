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
    }

    function onExcluirClicado(evento) {
        event.preventDefault();
        var botaoClicado = $(evento.currentTarget);
        var url = botaoClicado.data('url');
        var objeto = botaoClicado.data('objeto');

        var verificador = botaoClicado.data('verificador');
        var usuarioExcluido = botaoClicado.data('usuarioexcluido');
        var sair = botaoClicado.data('sair');

        if (verificador != null && verificador != '') {
            var codigoEspecial = $('.codigoEspecial');
            if (verificador != codigoEspecial.val()) {
                swal('Erro!', "Código Verificador Incorreto!", 'error');
                return
            }
        }
        swal({
            title: 'Tem certeza?',
            text: 'Excluir "' + objeto
            + '"? Você não poderá recuperar depois.',
            showCancelButton: true,
            confirmButtonColor: '#DD6855',
            confirmButtonText: 'Sim, exclua agora!',
            closeOnConfirm: false
        }, onExcluirConfirmado.bind(this, url, usuarioExcluido, sair));

    }

    function onExcluirConfirmado(url, usuarioExcluido, sair) {
        $.ajax({
            url: url,
            method: 'DELETE',
            success: onExcluidoSucesso.bind(this, sair, usuarioExcluido),
            error: onErrorExcluir.bind(this)
        })
    }

    function onExcluidoSucesso(sair, usuarioExcluido) {
        swal({
                title: 'Pronto',
                text: 'Excluído com sucesso!',
                showCancelButton: false,
                confirmButtonText: 'OK'
            },
            onRemoveExcluido.bind(this, sair, usuarioExcluido));

    }

    function onRemoveExcluido(sair, usuarioExcluido) {
        if (usuarioExcluido != null && usuarioExcluido == true) {
            $.ajax({
                url: sair,
                method: 'GET'
            })
        }
        window.location = window.location.href;
    }

    function onErrorExcluir(e) {
        swal('Erro!', e.responseText, 'error');
    }

    return DialogoExcluir;

}());

$(function () {
    var dialogoExcluir = new Brewer.DialogoExcluir();
    dialogoExcluir.iniciar();

});
