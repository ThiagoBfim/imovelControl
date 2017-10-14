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

        // var meses = [];
        // var valores = [];
        // vendaMes.forEach(function (obj) {
        //     meses.push(obj.nome);
        //     valores.push(obj.cep);
        // });

        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Meses');
        data.addColumn('number', 'Mariana');
        data.addColumn('number', 'Imprensa');
        data.addRows([  ['Junho', 1000 , 300], ['Julho',1170, 460], ['Agosto', 660, 1120], ['Setembro',1030,540]
        ]);

       // data.addRows([['2008', {id: 'Expenses',v: 0}, {id: 'Sales',v: 1000}]]);
       // data.addRows(['2009', 1000])
        //data.addRows([['2008', 1200]]);

        // var objeto = [['2008', {id: 'Expenses',v: 0}, {id: 'Sales',v: 1000}]]
        // objeto.push(['2009', 5, 10]);
        //
        //
        // data.addRows(objeto)

        var options = {
            chart: {
                title: 'Ganhos Mensais',
                subtitle: 'Ganhos mensais de todos os imóveis',
            }
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
        var formatter = new google.visualization.NumberFormat({ prefix: 'R$: ',decimalSymbol: ',', groupingSymbol: '.' });
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