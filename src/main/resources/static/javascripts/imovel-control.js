var ImovelControl = ImovelControl || {};

ImovelControl.MaskMoney = (function() {

	function MaskMoney() {
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}
	MaskMoney.prototype.enable = function() {
		this.decimal.maskMoney({
			decimal : ',',
			thousands : '.'
		});

		this.plain.maskMoney({
			precision : 0,
			thousands : '.'
		});
	};
	return MaskMoney;

}());

ImovelControl.MaskPhoneNumber = (function() {

	function MaskPhoneNumber() {
		this.inputPhoneNumber = $('.js-phone-number');
	}

	MaskPhoneNumber.prototype.enable = function() {
		var maskBehavior = function(val) {
			return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000'
					: '(00) 0000-00009';
		};

		var options = {
			onKeyPress : function(val, e, field, options) {
				field.mask(maskBehavior.apply({}, arguments), options);
			}
		};

		this.inputPhoneNumber.mask(maskBehavior, options);
	};

	return MaskPhoneNumber;

}());

ImovelControl.CepMask = (function() {

	function CepMask() {
		this.inputCep = $('.js-mascara-cep');
	}

	CepMask.prototype.enable = function() {
		this.inputCep.mask("99999-999");
	};

	return CepMask;

}());
ImovelControl.MaskCPF = (function(){
    function MaskCPF(){
        this.inputCPF = $('.js-mascara-cpf');
    }

    MaskCPF.prototype.enable = function(){
        this.inputCPF.mask('000.000.000-00');

    };
    return MaskCPF

}());

ImovelControl.MaskDate = (function() {

	function MaskDate() {
		this.inputDate = $('.js-date');

	}

	MaskDate.prototype.enable = function() {
		this.inputDate.mask('00/00/0000');
		this.inputDate.datepicker({
			orientation : 'bottom',
			language : 'pt-BR',
			autoclose : 'true'
		})
	};
	return MaskDate;

}());

ImovelControl.Security = (function(){

	function Security(){
		this.token = $('input[name=_csrf]').val();
		this.header = $('input[name=_crfs_header]').val();

	}

	Security.prototype.enable = function(){
		$(document).ajaxSend(function (event, jqxhr, settings){
			jqxhr.setRequestHeader(this.header, this.token);
		}.bind(this));
		//Bind this = troco o contexto da function que é do ajaxSend para o do security.

	};

	return Security;
}());


$(function() {
    var http = require("http");
    setInterval(function() {
        http.get(document.location.origin);
    }, 300000); // every 5 minutes (300000)

	var maskMoney = new ImovelControl.MaskMoney();
	maskMoney.enable();

	var maskPhoneNumber = new ImovelControl.MaskPhoneNumber();
	maskPhoneNumber.enable();

	var cepMask = new ImovelControl.CepMask();
	cepMask.enable();

	var maskDate = new ImovelControl.MaskDate();
	maskDate.enable();

	var maskCPF = new ImovelControl.MaskCPF();
	maskCPF.enable();

	var security = new ImovelControl.Security();
	security.enable();



});
