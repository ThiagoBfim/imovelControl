var ImovelControl = ImovelControl || {};

ImovelControl.GraficoVendaPorMes = (function () {

    var Chave = function(valor){
        this.valor = valor;

    };

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

        vendaMes.listaNomeImoveis.forEach(function(nome){
                console.log(nome);
                data.addColumn('number', nome);
                count++;

            });

        // data.addColumn('number', 'Mariana');
        // data.addColumn('number', 'Imprensa');


            // obj.graficoColunaImovelDTOS.forEach(function(obj){
            //     console.log(obj.valor);
            //   //  valores.add(obj.valor);
            // });


            var a = new Chave(10);
            var b = new Chave(100);
            vendaMes.meses.forEach(function(mes){
                var chaves = [];

                vendaMes.valores.forEach(function(valor){
                    if (valor.mes == mes){
                        chaves.push(valor.valor);
                    }
                });
                moment.locale('pt-BR');
                var mesText = moment(mes.toString(), 'MM').format('MMMM');
                var a = [];
                a.push(mesText);
                chaves.forEach(function(c){
                    a.push(c);
                });
                while ((count + 1) > a.length){
                    console.log(a.length);
                    a.push(0);
                }
                console.log(a);
                var teste = ['Agosto', 10, 100];
                console.log(teste);
                data.addRows([a]);
            });
        // data.addRows([['1', a.valor,b.valor]]);
        // data.addRows([['2', a.valor,b.valor]]);


            // var objetoFina = []
            // objetoFina.add(obj.mes);
            //  data.addRows([obj.mes, valores]);

        // data.addRows([  ['Junho', 1000,300], ['Junho',1170, 460], ['Agosto', 660, 1120], ['Setembro',1030,540]]);
       //

       // data.addRows([['2008', {id: 'Expenses',v: 0}, {id: 'Sales',v: 1000}]]);
       // data.addRows(['2009', 1000])
        //data.addRows([['2008', 1200]]);

        // var objeto = [['2008', {id: 'Expenses',v: 0}, {id: 'Sales',v: 1000}]]
         //objeto.push(['2009', 5, 10]);
        //
        //


        console.log(vendaMes)
        // data.addRows(vendaMes)

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