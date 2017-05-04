var Brewer = Brewer || {};

Brewer.AluguelCadastroRapido = (function() {

	function AluguelCadastroRapido() {
		this.modal = $('#modalCadastroRapidoAluguel');
		this.botaoSalvar = this.modal.find('.js-modal-cadastro-salvar-button');
		this.containerMensagemErro = $('.js-messagem-cadastro-rapido');
		// this.inputComplemento = $('#complemento');
		this.inputAluguel = $('#aluguel');
		this.form = this.modal.find('form');
		this.url = this.form.attr('action');
	}

	AluguelCadastroRapido.prototype.iniciar = function() {
		this.form.on('submit', function(event) {
			event.preventDefault();
		});

		this.modal.on('hide.bs.modal', onModalClose.bind(this));
		this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
	}

	function onModalClose() {
		this.containerMensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}

	function onBotaoSalvarClick() {
		var aluguel = this.inputAluguel.val();
		$.ajax({
			url : this.url,
			method : 'POST',
			contentType : 'application/json',
			data : {
				aluguel : aluguel
			},
			error : onErroSalvandoaluguel.bind(this),
			success : onAluguelSalvo.bind(this)
		});
	}

	function onErroSalvandoaluguel(obj) {
		console.log("erro");
		var msgError = obj.responseText;
		this.containerMensagemErro.removeClass('hidden');
		this.containerMensagemErro.html('<span>' + msgError + '</span>');
		this.form.find('.form-group').addClass('has-error');
	}

	function onAluguelSalvo(aluguel) {
		console.log("salvo");
		var comboAluguel = $('#aluguel');
		comboAluguel.append('<option value=' + aluguel.codigo + '>'
				+ aluguel.complemento + '</option>');

		comboAluguel.val(aluguel.codigo);
		this.modal.modal('hide');

	}

	return AluguelCadastroRapido;

}());

$(function() {
	var aluguelCadastroRapido = new Brewer.AluguelCadastroRapido();
	aluguelCadastroRapido.iniciar();

})