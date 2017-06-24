var Brewer = Brewer || {};

Brewer.AluguelCadastroRapido = (function () {

    var modal = $('#modalCadastroLocatario');

    function AluguelCadastroRapido() {
        this.botaoSalvar = $('.js-modal-locatario');
        // this.inputComplemento = $('#complemento');
        this.codigoAluguel = this.botaoSalvar.data('codigo');
        this.locatarioAluguel = $('#codigoAluguel');


        var token = $("input[name='_crsf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }

    AluguelCadastroRapido.prototype.iniciar = function () {
        this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
    }

    function onBotaoSalvarClick() {
        console.log(this.codigoAluguel);
        $.getJSON('http://localhost:8080/ImovelControl/locatario/' + this.codigoAluguel, function (data) {
            meu_callback(data);
        });


        this.locatarioAluguel.val(this.codigoAluguel);
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

    return AluguelCadastroRapido;

}());

$(function () {
    var aluguelCadastroRapido = new Brewer.AluguelCadastroRapido();
    aluguelCadastroRapido.iniciar();

})