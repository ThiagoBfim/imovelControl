var ImovelControl = ImovelControl || {};

ImovelControl.MascaraCpfCnpj = (function() {

	function MascaraCpfCnpj() {
		this.radioTipoPessoa = $('.js-raido-tipo-pessoa');
		this.labelCpfCnpj = $('.js-documento');
		this.inputCpfCnpj = $('.js-inputDocumento');
	}

	MascaraCpfCnpj.prototype.iniciar = function() {
		this.radioTipoPessoa.on('change', onTipoPessoaAlterado.bind(this));
		var tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];
		if (tipoPessoaSelecionada) {
			aplicarMascara.call(this, $(tipoPessoaSelecionada));
		}
	};

	function onTipoPessoaAlterado(evento) {
		var tipoPessoaSelecionada = $(evento.currentTarget);
		aplicarMascara.call(this, tipoPessoaSelecionada);
		this.inputCpfCnpj.val('');
	}

	function aplicarMascara(tipoPessoaSelecionada) {
		this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'));
		this.inputCpfCnpj.mask(tipoPessoaSelecionada.data('mascara'));
		this.inputCpfCnpj.removeAttr('disabled');
	}

	return MascaraCpfCnpj;
}());

$(function() {
	var mascaraCpfCnpj = new ImovelControl.MascaraCpfCnpj();
	mascaraCpfCnpj.iniciar();

});