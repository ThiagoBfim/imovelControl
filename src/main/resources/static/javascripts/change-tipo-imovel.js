var Bomfim = Bomfim || {};

Bomfim.ChangeTipoImovel = (function () {

    function ChangeTipoImovel() {
        this.radioTipoImovel = $('.js-radio-tipo-imovel');
        this.inputQuarto = $('.js-quarto');
        this.inputBanheiro = $('.js-banheiro');
        this.inputVaga = $('.js-vaga');
        this.inputSuite = $('.js-suites');
        this.inputForro = $('.js-forro');
        this.inputPiso = $('.js-piso');
    }

    ChangeTipoImovel.prototype.iniciar = function () {
        this.radioTipoImovel.on('change', onTipoImovelChange.bind(this));
    }

    function onTipoImovelChange(evento) {
        var tipoPessoaSelecionada = $(evento.currentTarget);
        switch (tipoPessoaSelecionada.val()) {
            case 'CASA' : {
                this.inputQuarto.show();
                this.inputBanheiro.show();
                this.inputVaga.show();
                this.inputSuite.show();
                this.inputForro.show();
                this.inputPiso.show();
                break;
            }
            case 'APARTAMENTO' : {
                this.inputQuarto.show();
                this.inputBanheiro.show();
                this.inputVaga.show();
                this.inputSuite.show();
                this.inputForro.hide();
                this.inputPiso.hide();
                break;
            }
            case 'KIT' : {
                this.inputQuarto.show();
                this.inputBanheiro.show();
                this.inputForro.show();
                this.inputPiso.show();
                this.inputSuite.hide();
                this.inputVaga.hide();
                break;
            }
            case 'LOJA' : {
                this.inputBanheiro.show();
                this.inputForro.show();
                this.inputPiso.show();
                this.inputQuarto.hide();
                this.inputSuite.hide();
                this.inputVaga.hide();
                break;
            }
            case 'SALAO' : {
                this.inputBanheiro.show();
                this.inputForro.show();
                this.inputPiso.show();
                this.inputQuarto.hide();
                this.inputSuite.hide();
                this.inputVaga.hide();
                break;
            }
        }
    }

    return ChangeTipoImovel;
}());

$(function () {
    var changeTipoImovel = new Bomfim.ChangeTipoImovel();
    changeTipoImovel.iniciar();

});