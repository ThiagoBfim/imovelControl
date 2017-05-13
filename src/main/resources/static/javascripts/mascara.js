var Brewer = Brewer || {};

Brewer.Mascara = (function () {

    function Mascara() {

        this.inputCpfCnpj = $('.js-cep');
    }

    Mascara.prototype.iniciar = function () {

        this.inputCpfCnpj.mask("99999-999");
    }

    return Mascara;
}());

$(function () {
    var mascara = new Brewer.Mascara();
    mascara.iniciar();

});