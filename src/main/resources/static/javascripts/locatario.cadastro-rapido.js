var ImovelControl = ImovelControl || {};

ImovelControl.LocatarioCadastroRapido = (function () {

    var modal = $('#modalCadastroLocatario');

    function LocatarioCadastroRapido() {
        this.botaoSalvar = $('.js-modal-locatario');
        this.locatarioAluguel = $('#codigoAluguel');

    }

    LocatarioCadastroRapido.prototype.iniciar = function () {
        this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
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