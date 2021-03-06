var ImovelControl = ImovelControl || {};

ImovelControl.UploadFoto = (function () {

    function UploadFoto() {
        this.inputNomeFoto = $('input[name=foto]');
        this.inputContentType = $('input[name=contentType]');
        this.htmlFotoImovelTemplate = $('#foto-imovel').html();
        this.template = Handlebars.compile(this.htmlFotoImovelTemplate);
        this.containerFotoImovel = $('.js-container-foto-imovel');
        this.uploadDrop = $('#upload-drop');

        this.inputUrlFoto = $('input[name=urlFoto]');
        this.novaFoto = $('input[name=novaFoto]');
        this.imgLoading = $('.js-img-loading');

    }

    UploadFoto.prototype.iniciar = function () {
        var settings = {
            type: 'json',
            filelimit: 1,
            allow: '*.(jpg|jpeg|png)',
            action: this.containerFotoImovel.data('url-fotos'),
            complete: onUploadCompleto.bind(this),
            beforeSend: adicionarCsrfToken,
            loadstart: onLoadStart.bind(this)
        };
        UIkit.uploadSelect($('#upload-select'), settings);
        UIkit.uploadDrop(this.uploadDrop, settings);

        if (this.inputNomeFoto.val()) {
            renderizarFoto.call(this, {
                nome: this.inputNomeFoto.val(),
                contentType: this.inputContentType.val(),
                url: this.inputUrlFoto.val()
            });
        }

    };

    function onLoadStart() {
        this.imgLoading.removeClass('hidden');
    }

    function onUploadCompleto(resposta) {
        this.novaFoto.val('true');
        this.inputUrlFoto.val(resposta.url);
        this.imgLoading.addClass('hidden');
        renderizarFoto.call(this, resposta);
    }

    function renderizarFoto(resposta) {
        this.inputNomeFoto.val(resposta.nome);
        this.inputContentType.val(resposta.contentType);

        this.uploadDrop.addClass('hidden');
        var htmlFotoImovel = this.template({
            url: resposta.url
        });
        this.containerFotoImovel.append(htmlFotoImovel);

        $('.js-remove-foto').on('click', onRemoverFoto.bind(this));
    }


    function onRemoverFoto() {
        $('.js-foto-imovel').remove();
        this.uploadDrop.removeClass('hidden');
        this.inputNomeFoto.val('');
        this.inputContentType.val('');
        this.novaFoto.val('false');
    }

    function adicionarCsrfToken(xhr) {
        var token = $('input[name=_csrf]').val();
        var header = $('input[name=_crfs_header]').val();
        xhr.setRequestHeader(header, token);
    }

    return UploadFoto;

}());

$(function () {
    var uploadFoto = new ImovelControl.UploadFoto();
    uploadFoto.iniciar();
});