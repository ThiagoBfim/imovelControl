var ImovelControl = ImovelControl || {};

ImovelControl.DialogoExcluir = (function () {

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
    };

    function onExcluirClicado(evento) {
        event.preventDefault();
        var botaoClicado = $(evento.currentTarget);
        var url = botaoClicado.data('url');
        var objeto = botaoClicado.data('objeto');
        var exclusao = botaoClicado.data('exclusao');

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
        var texto = 'Excluir ' + objeto + ' ? ';
        var method = 'DELETE';
        var textoButton = 'Sim, exclua agora!';
        var colorButton = '#DD6855';
        var msgSucesso = 'Excluído com sucesso!';
        if (exclusao == null) {
            texto += ' Você não poderá recuperar depois.';
        } else if (exclusao == 'reativar') {
            texto = 'Reativar ' + objeto + ' ? ';
            method = 'GET';
            textoButton = " Sim, recuperar agora!";
            colorButton = '#22d31f';
            msgSucesso = 'Reativado com sucesso!';
        }

        swal({
            title: 'Tem certeza?',
            text: texto,
            showCancelButton: true,
            confirmButtonColor: colorButton,
            confirmButtonText: textoButton,
            closeOnConfirm: false
        }, onExcluirConfirmado.bind(this, url, usuarioExcluido, sair, method, msgSucesso));

    }

    function onExcluirConfirmado(url, usuarioExcluido, sair, method, msgSucesso) {
        $.ajax({
            url: url,
            method: method,
            success: onExcluidoSucesso.bind(this, sair, usuarioExcluido, msgSucesso),
            error: onErrorExcluir.bind(this)
        })
    }

    function onExcluidoSucesso(sair, usuarioExcluido, msgSucesso) {
        if (usuarioExcluido != null && usuarioExcluido == true) {
            $.ajax({
                url: sair,
                method: 'GET'
            });
            window.location = window.location.href;

        } else {
            swal({
                    title: 'Pronto',
                    text: msgSucesso,
                    showCancelButton: false,
                    confirmButtonText: 'OK'
                },
                onRemoveExcluido.bind(this));
        }
    }

    function onRemoveExcluido() {
        window.location = window.location.href;
    }

    function onErrorExcluir(e) {
        swal('Erro!', e.responseText, 'error');
    }

    return DialogoExcluir;

}());

$(function () {
    var dialogoExcluir = new ImovelControl.DialogoExcluir();
    dialogoExcluir.iniciar();

});
