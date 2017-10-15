var ImovelControl = ImovelControl || {};

ImovelControl.GraficoVendaPorMes = (function () {

    function GraficoVendaPorMes() {
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.load('current', {packages: ['bar']});
        this.btnGraficoPizza = $('#pizzaBtn');
        this.btnGraficoColuna = $('#colunaBtn');
    }

    GraficoVendaPorMes.prototype.iniciar = function () {
        this.btnGraficoPizza.on('click', drawPizzaChart.bind(this));
        this.btnGraficoColuna.on('click', drawColunaChart.bind(this));
    };


    function drawColunaChart() {
        $.ajax({
            url: 'totalPorMesColuna',
            method: 'GET',
            success: onDadosRecebidosColuna.bind(this)
        });

    }

    function onDadosRecebidosColuna(vendaMes) {
        var count = 0;
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Meses');

        vendaMes.listaNomeImoveis.forEach(function (nome) {
            data.addColumn('number', nome);
            count++;

        });

        vendaMes.meses.forEach(function (mes) {
            var chaves = [];

            vendaMes.valores.forEach(function (valor) {
                if (valor.mes == mes) {
                    chaves.push(valor.valor);
                }
            });
            moment.locale('pt-BR');
            var mesText = moment(mes.toString(), 'MM').format('MMMM');
            var a = [];
            a.push(mesText);
            chaves.forEach(function (c) {
                a.push(c);
            });
            while ((count + 1) > a.length) {
                a.push(0);
            }
            data.addRows([a]);
        });


        var options = {
            chart: {
                title: 'Ganhos Mensais',
                subtitle: 'Ganhos mensais de todos os imóveis'

            },
            vAxis: {format: ''}
        };

        var chart = new google.charts.Bar(document.getElementById('chart'));
        chart.draw(data, google.charts.Bar.convertOptions(options));
    }

    function drawPizzaChart() {
        $.ajax({
            url: 'totalPorMes',
            method: 'GET',
            success: onDadosRecebidosPizza.bind(this)
        });
    }

    function onDadosRecebidosPizza(vendaMes) {

        var ganhos = [];
        var imoveis = [];
        vendaMes.forEach(function (obj) {
            imoveis.push(obj.nomeImovel);
        });

        vendaMes.forEach(function (obj) {
            ganhos.push(obj.rendimento)

        });

        data = new google.visualization.DataTable();
        data.addColumn('string', 'Imovel');
        data.addColumn('number', 'Ganhos');

        for (var i = 0; i < imoveis.length; i++) {
            data.addRows([[imoveis[i], ganhos[i]]]);
        }

        var options = {
            title: 'Ganho Geral do Imóvel',
            titleTextStyle: {
                fontSize: 20
            },
            is3D: true,
            titlePosition: 'center',
        };
        var formatter = new google.visualization.NumberFormat({
            prefix: 'R$: ',
            decimalSymbol: ',',
            groupingSymbol: '.'
        });
        formatter.format(data, 1);
        var chart = new google.visualization.PieChart(document.getElementById('chart'));
        chart.draw(data, options);

    }

    return GraficoVendaPorMes;

}());


$(function () {
    var graficoVendaPorMes = new ImovelControl.GraficoVendaPorMes();
    graficoVendaPorMes.iniciar();


});