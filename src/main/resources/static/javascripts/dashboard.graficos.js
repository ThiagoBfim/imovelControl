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

        /*var meses = [];
        var valores = [];
        vendaMes.forEach(function (obj) {
            meses.push(obj.nome);
            valores.push(obj.cep);
        });*/
        var data = google.visualization.arrayToDataTable([
            ['Year', 'Sales', 'Expenses', 'Profit'],
            ['2014', 1000, 400, 200],
            ['2015', 1170, 460, 250],
            ['2016', 660, 1120, 300],
             ['2017', 1030, 540, 350]//,
            // ['2018', 100, 1, 1]
        ]);
        // var data = new google.visualization.DataTable();
        // data.addColumn('string', 'Month'); // Implicit domain label col.
        // data.addColumn('number', 'Sales'); // Implicit series 1 data col.
        // //data.addColumn('number', 'Expenses');
        // data.addColumn({type:'number', role:'interval'});  // interval role col.
        // data.addColumn({type:'number', role:'interval'});  // interval role col.
        // data.addColumn({type:'string', role:'annotation'}); // annotation role col.
        // data.addColumn({type:'string', role:'annotationText'}); // annotationText col.
        // data.addColumn({type:'boolean',role:'certainty'}); // certainty col.
        // data.addRows([
        //     ['April',1000,  900, 1100,  'A','Stolen data', true],
        //     ['May',  1170, 1000, 1200,  'B','Coffee spill', true],
        //     ['June',  660,  550,  800,  'C','Wumpus attack', true],
        //     ['July', 1030, 300, null, null, null, false]
        // ]);

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