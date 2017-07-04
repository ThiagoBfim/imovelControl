var Brewer = Brewer || {};

Brewer.AluguelCadastroRapido = (function () {

    var modal = $('#modalCadastroPagamento');

    function AluguelCadastroRapido() {
        this.botaoSalvar = $('.js-modal-pagamento');
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

    function onBotaoSalvarClick(evento) {
        var botaoClicado = $(evento.currentTarget);
        var codigoAluguel = botaoClicado.data('codigo');
        $.getJSON('http://localhost:8080/ImovelControl/imovel/aluguel/pagamento/'
            + codigoAluguel, function (data) {
            meu_callback(data);
        });


        this.locatarioAluguel.val(codigoAluguel);
    }

    function meu_callback(conteudo) {

        var inputAguaInclusa = $('#aguaInclusa');

        if (!("erro" in conteudo)) {
            //Atualiza os campos com os valores.
            console.log(conteudo.aguaInclusa);
            if (conteudo.aguaInclusa == true) {
                inputAguaInclusa.hide();
            } else {
                inputAguaInclusa.hide();
            }
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