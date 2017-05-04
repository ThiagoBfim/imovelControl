var Bomfim = Bomfim || {};

Bomfim.ChangeTipoImovel = (function() {

	function ChangeTipoImovel() {
		this.radioTipoPessoa = $('.js-radio-tipo-imovel');
		this.campoTipoImovel = $('.js-campoTipoImovel');
		this.inputCpfCnpj = $('.js-inputDocumento');
	}

	ChangeTipoImovel.prototype.iniciar = function() {
		this.radioTipoPessoa.on('change', onTipoPessoaAlterado.bind(this));
		var tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];
		if (tipoPessoaSelecionada) {
			aplicarMascara.call(this, $(tipoPessoaSelecionada));
		}
	}

	function onTipoPessoaAlterado(evento) {
		var tipoPessoaSelecionada = $(evento.currentTarget);
		aplicarMascara.call(this, tipoPessoaSelecionada);
		this.inputCpfCnpj.val('');
	}

	function aplicarMascara(tipoPessoaSelecionada) {
		this.campoTipoImovel.show();
	}

	return ChangeTipoImovel;
}());

$(function() {
	var changeTipoImovel = new Bomfim.ChangeTipoImovel();
	changeTipoImovel.iniciar();

});