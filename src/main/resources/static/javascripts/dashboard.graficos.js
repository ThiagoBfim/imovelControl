var ImovelControl = ImovelControl || {};

ImovelControl.GraficoVendaPorMes = (function () {

    function GraficoVendaPorMes() {
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.load('current', {'packages': ['bar']});
        //  google.charts.setOnLoadCallback(drawChart);
        this.btnGraficoPizza = $('#pizzaBtn');
        this.btnGraficoColuna = $('#colunaBtn');

    }

    GraficoVendaPorMes.prototype.iniciar = function () {
        this.btnGraficoPizza.on('click', drawPizzaChart.bind(this));
        this.btnGraficoColuna.on('click', drawColunaChart.bind(this));
    }


    function drawColunaChart() {
        $.ajax({
            url: 'totalPorMes',
            method: 'GET',
            success: onDadosRecebidosColuna.bind(this)
        });

    }

    function onDadosRecebidosColuna(vendaMes) {

        var meses = [];
        var valores = [];
        vendaMes.forEach(function (obj) {
            meses.push(obj.nome);
            valores.push(obj.cep);
        });
        var data = google.visualization.arrayToDataTable([
            ['Year', 'Sales', 'Expenses', 'Profit'],
            ['2014', 1000, 400, 200],
            ['2015', 1170, 460, 250],
            ['2016', 660, 1120, 300],
            ['2017', 1030, 540, 350]
        ]);

        var options = {
            chart: {
                title: 'Company Performance',
                subtitle: 'Sales, Expenses, and Profit: 2014-2017',
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

        var ganhos = [1, 2, 3, 4, 5];
        var imoveis = [];
        vendaMes.forEach(function (obj) {
            imoveis.push(obj.nome);
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

        var chart = new google.visualization.PieChart(document.getElementById('chart'));
        chart.draw(data, options);

    }


    return GraficoVendaPorMes;

}());


$(function () {
    var graficoVendaPorMes = new ImovelControl.GraficoVendaPorMes();
    graficoVendaPorMes.iniciar();


})