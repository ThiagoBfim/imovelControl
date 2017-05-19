var Bomfim = Bomfim || {};

Bomfim.ChangeTipoImovel = (function () {

    function ChangeTipoImovel() {
        this.radioTipoImovel = $('.js-radio-tipo-imovel');
    }

    ChangeTipoImovel.prototype.iniciar = function () {
        this.radioTipoImovel.on('change', onTipoImovelChange.bind(this));
        var tipoPessoaSelecionada = this.radioTipoImovel.filter(':checked')[0];
        if (tipoPessoaSelecionada) {
            changeTipoImovel(tipoPessoaSelecionada.value);
        }
    }

    function onTipoImovelChange(evento) {
        var tipoPessoaSelecionada = $(evento.currentTarget);
        changeTipoImovel(tipoPessoaSelecionada.val());
    }

    function changeTipoImovel(tipoPessoaSelecionada) {
        var inputQuarto = $('.js-quarto');
        var inputBanheiro = $('.js-banheiro');
        var inputVaga = $('.js-vaga');
        var inputSuite = $('.js-suites');
        var inputForro = $('.js-forro');
        var inputPiso = $('.js-piso');
        switch (tipoPessoaSelecionada) {
            case 'CASA' : {
                inputQuarto.show();
                inputBanheiro.show();
                inputVaga.show();
                inputSuite.show();
                inputForro.show();
                inputPiso.show();
                break;
            }
            case 'APARTAMENTO' : {
                inputQuarto.show();
                inputBanheiro.show();
                inputVaga.show();
                inputSuite.show();
                inputForro.hide();
                inputPiso.hide();

                inputForro.val('');
                inputPiso.val('');
                break;
            }
            case 'KIT' : {
                inputQuarto.show();
                inputBanheiro.show();
                inputForro.show();
                inputPiso.show();
                inputSuite.hide();
                inputVaga.hide();

                inputSuite.val('');
                inputVaga.val('');
                break;
            }
            case 'LOJA' : {
                inputBanheiro.show();
                inputForro.show();
                inputPiso.show();
                inputQuarto.hide();
                inputSuite.hide();
                inputVaga.hide();

                inputQuarto.val('');
                inputSuite.val('');
                inputVaga.val('');
                break;
            }
            case 'SALAO' : {
                inputBanheiro.show();
                inputForro.show();
                inputPiso.show();
                inputQuarto.hide();
                inputSuite.hide();
                inputVaga.hide();

                inputQuarto.val('');
                inputSuite.val('');
                inputVaga.val('');
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