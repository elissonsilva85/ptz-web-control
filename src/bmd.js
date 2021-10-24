const edge = require('edge-js');
const path = require('path');

let ip = "127.0.0.1";
let url = "http://localhost/";

let defaultLumaParameters = {
    onAir: 0,
    //
    inputFill: 3,
    inputKey: 3,
    //
    masked: 0,
    maskTop: 0,
    maskBottom: 0,
    maskRight: 0,
    maskLeft: 0,
    //
    preMultiplied: 0,
    preMultipliedClip: 0.101,
    preMultipliedGain: 1,
    preMultipliedInvertKey: 0,
    //
    fly: 1,
    flySizeX: 1,
    flySizeY: 1,
    flyPositionX: 0,
    flyPositionY: 13.9
}

var __SimpleSwitcher = edge.func({
    source: function () {/*
        using System.Threading.Tasks;
    
        public class Startup
        {
            public async Task<object> Invoke(dynamic input)
            {
                string method = (string)input.method;
                System.Console.WriteLine("method: " + method);

                typeof(Helper).GetMethod(method).Invoke(null, new [] {input.param});

                return true;
            }
        }
    
        static class Helper
        {
            private static SimpleSwitcher.RunCommands run = new SimpleSwitcher.RunCommands();

            public static void CarregarImagemTemaCulto(dynamic param)
            {
                run.CarregarImagemTemaCulto(param.ip, param.imageIndex);
            }

            public static void ExecutarAberturaCulto(dynamic param)
            {
                run.ExecutarAberturaCulto(param.ip, param.tempoFrames);
            }

            public static void ExecutarEncerramentoCulto(dynamic param)
            {
                run.ExecutarEncerramentoCulto(param.ip, param.indexImagemEncerramento, param.inputEncerramento, param.tempoTransicaoFinal, param.tempoEsperaAntesTerminar);
            }

            public static void AtivarDownstreamKey(dynamic param)
            {
                run.AtivarDownstreamKey(param.ip, param.dskIndex, param.indexImagem);
            }

            public static void DesativarDownstreamKey(dynamic param)
            {
                run.DesativarDownstreamKey(param.ip, param.dskIndex);
            }

            public static void DefinirSaidaAuxiliar(dynamic param)
            {
                run.DefinirSaidaAuxiliar(param.ip, param.tipoInput, param.inputIndex);
            }

            public static void AtivarLegendaCoral(dynamic param)
            {
                run.AtivarLegendaCoral(param.ip, param.lumaParameters);
            }

            public static void DesativarLegendaCoral(dynamic param)
            {
                run.DesativarLegendaCoral(param.ip);
            }

            public static void AtivarUpstream1(dynamic param)
            {
                run.AtivarUpstream1(param.ip, param.lumaParameters);
            }

            public static void DesativarUpstream1(dynamic param)
            {
                run.DesativarUpstream1(param.ip);
            }

            public static void DefinirPreview(dynamic param)
            {
                run.DefinirPreview(param.ip, param.tipoInput, param.inputIndex);
            }

            public static void DefinirProgram(dynamic param)
            {
                run.DefinirProgram(param.ip, param.tipoInput, param.inputIndex);
            }

            public static void PerformAutoTransition(dynamic param)
            {
                run.PerformAutoTransition(param.ip);
            }

            public static void PerformCut(dynamic param)
            {
                run.PerformCut(param.ip);
            }

            public static void ListarSwitcherInputs(dynamic param)
            {
                run.ListarSwitcherInputs(param.ip);
            }

            public static void SwitcherStatus(dynamic param)
            {
                run.SwitcherStatus(param.ip);
            }

        }
    */},
    references: [ path.join( __dirname, '../assets/SimpleSwitcher.dll') ]
});

let RunnableFunctions = {
    carregarImagemCultoEnsino: (recall) => {
        __SimpleSwitcher({
            method: "CarregarImagemTemaCulto", 
            param: {ip: ip, imageIndex: 3}
        }, recall);
    },

    carregarImagemEBD: (recall) => {
        __SimpleSwitcher({
            method: "CarregarImagemTemaCulto", 
            param: {ip: ip, imageIndex: 5}
        }, recall);
    },

    carregarImagemQuartaLibertacao: (recall) => {
        __SimpleSwitcher({
            method: "CarregarImagemTemaCulto", 
            param: {ip: ip, imageIndex: 6}
        }, recall);
    },

    carregarImagemSantaCeia: (recall) => {
        __SimpleSwitcher({
            method: "CarregarImagemTemaCulto", 
            param: {ip: ip, imageIndex: 1}
        }, recall);
    },

    carregarImagemDomingo: (recall) => {
        __SimpleSwitcher({
            method: "CarregarImagemTemaCulto", 
            param: {ip: ip, imageIndex: 7}
        }, recall);
    },

    carregarImagemBatismo: (recall) => {
        __SimpleSwitcher({
            method: "CarregarImagemTemaCulto", 
            param: {ip: ip, imageIndex: 9}
        }, recall);
    },
   
    carregarImagemGenerica: (recall) => {
        __SimpleSwitcher({
            method: "CarregarImagemTemaCulto", 
            param: {ip: ip, imageIndex: 8}
        }, recall);
    },

    carregarImagemAbertura: (recall) => {
        let now = new Date();
        switch(now.getDay())
        {
            case 0:
                // Domingo
		        console.log("Dom");
                if(now.getHours() <= 9)
                    RunnableFunctions.carregarImagemEBD(recall);
                else if (now.getHours() <= 15)
                    RunnableFunctions.carregarImagemBatismo(recall);
                else
                    RunnableFunctions.carregarImagemDomingo(recall);
                break;
            case 1:
                // Segunda
		        console.log("Seg");
		        RunnableFunctions.carregarImagemGenerica(recall);
                break;
            case 2:
                // Terça
                console.log("Ter");
                RunnableFunctions.carregarImagemGenerica(recall);
                break;
            case 3:
                // Quarta
		        console.log("Qua");
                RunnableFunctions.carregarImagemQuartaLibertacao(recall);
                break;
            case 4:
                // Quinta
                console.log("Qui");
                RunnableFunctions.carregarImagemGenerica(recall);
                break;
            case 5:
                // Sexta
		        console.log("Sex");
                RunnableFunctions.carregarImagemCultoEnsino(recall);
                break;
            case 6:
                // Sabado
		        console.log("Sab");
                RunnableFunctions.carregarImagemSantaCeia(recall);
                break;
        }     
    },

    iniciaCulto: (recall) => {
        __SimpleSwitcher({
            method: "ExecutarAberturaCulto", 
            param: {ip: ip, tempoFrames: 60}
        }, recall);
    },

    encerraCulto: (recall) => {
        __SimpleSwitcher({
            method: "ExecutarEncerramentoCulto", 
            param: {
                ip: ip,
                indexImagemEncerramento: 4, 
                inputEncerramento: 6, 
                tempoTransicaoFinal: 60, 
                tempoEsperaAntesTerminar: 8000
            }
        }, recall);
    },

    exibirOferta: (recall) => {
        __SimpleSwitcher({
            method: "AtivarDownstreamKey", 
            param: {ip: ip, dskIndex: 0, indexImagem: 11}
        }, recall);
    },

    ocultarOferta: (recall) => {
        __SimpleSwitcher({
            method: "DesativarDownstreamKey", 
            param: {ip: ip, dskIndex: 0}
        }, recall);
    },

    ativarLegendaCoral: (recall) => {
        __SimpleSwitcher({
            method: "AtivarLegendaCoral", 
            param: { 
                ip: ip,
                lumaParameters: defaultLumaParameters
            }
        }, recall);
    },

    desativarLegendaCoral: (recall) => {
        __SimpleSwitcher({
            method: "DesativarLegendaCoral", 
            param: { ip: ip }
        }, recall);
    },

    performAutoTransition: (recall) => {
        __SimpleSwitcher({
            method: "PerformAutoTransition", 
            param: { ip: ip }
        }, recall);
    },

    performCut: (recall) => {
        __SimpleSwitcher({
            method: "PerformCut", 
            param: { ip: ip }
        }, recall);
    },

    listarSwitcherInputs: (recall) => {
        __SimpleSwitcher({
            method: "ListarSwitcherInputs", 
            param:  { ip: ip }
        }, recall);
    },

    status: (recall) => {
        __SimpleSwitcher({
            method: "SwitcherStatus", 
            param: { ip: ip }
        }, recall);
    },

}

let errorHandling = function(error, req, res) {
    console.log("erro", error);
    //
    //res.redirect(301, url + req.path);
    //
}

module.exports = (app, config) => {
    ip = config.blackmagicDesignIP;
    url = config.urlBase;

    app.get('/bmd', function(req, res) {
        res.send('Blackmagic Design is Working');
    });

    app.get('/bmd/run/:func', function(req, res) {
        if(RunnableFunctions[req.params.func])
        {
            RunnableFunctions[req.params.func](
                function (error, result) {
                    if(error) {
                        errorHandling(error, req, res);
                    } else {
                        console.log("sucesso");
                        res.send(`OK - /bmd/run/${req.params.func}`);
                    }
                });
        }
        else
        {
            res.status(500).send("Nao localizado");
        }
    });

    app.get('/bmd/run/downstream/:dsk/:image', function(req, res) {
        __SimpleSwitcher({
            method: "AtivarDownstreamKey", 
            param: {
                ip: ip,
                dskIndex: parseInt(req.params.dsk),
                indexImagem: parseInt(req.params.image)
            }
        }, function (error, result) {
            if(error) {
                errorHandling(error, req, res);
            } else {
                console.log("sucesso");
                res.send(`OK - /bmd/run/downstream/${req.params.dsk}/${req.params.image}`);
            }
        });
    });

    app.get('/bmd/run/downstream/:dsk/offAir', function(req, res) {
        __SimpleSwitcher({
            method: "DesativarDownstreamKey", 
            param: {
                ip: ip,
                dskIndex: parseInt(req.params.dsk)
            }
        }, function (error, result) {
            if(error) {
                errorHandling(error, req, res);
            } else {
                console.log("sucesso");
                res.send(`OK - /bmd/run/downstream/${req.params.dsk}/offAir`);
            }
        });
    });

    app.get('/bmd/run/upstream/:input', function(req, res) {
        __SimpleSwitcher({
            method: "AtivarUpstream1", 
            param: { 
                ip: ip,
                lumaParameters: {
                    onAir: 1,
                    //
                    inputFill: parseInt(req.params.input),
                    inputKey: parseInt(req.params.input),
                    //
                    masked: 0,
                    maskTop: 0,
                    maskBottom: 0,
                    maskRight: 0,
                    maskLeft: 0,
                    //
                    preMultiplied: 0,
                    preMultipliedClip: 0,
                    preMultipliedGain: 1,
                    preMultipliedInvertKey: 0,
                    //
                    fly: 1,
                    flySizeX: 0.65,
                    flySizeY: 0.65,
                    flyPositionX: 5.3,
                    flyPositionY: 0.65
                }
            }
        }, function (error, result) {
            if(error) {
                errorHandling(error, req, res);
            } else {
                console.log("sucesso");
                res.send(`OK - /bmd/run/upstream/${req.params.input}`);
            }
        });
    });

    app.get('/bmd/run/upstream/offAir', function(req, res) {
        __SimpleSwitcher({
            method: "DesativarUpstream1", 
            param: { 
                ip: ip
            }
        }, function (error, result) {
            if(error) {
                errorHandling(error, req, res);
            } else {
                console.log("sucesso");
                res.send(`OK - /bmd/run/upstream/offAir`);
            }
        });
    });

    app.get('/bmd/run/aux/:tipo/:input', function(req, res) {
        __SimpleSwitcher({
            method: "DefinirSaidaAuxiliar", 
            param: {
                ip: ip,
                tipoInput: req.params.tipo,
                inputIndex: parseInt(req.params.input)
            }
        }, function (error, result) {
            if(error) {
                errorHandling(error, req, res);
            } else {
                console.log("sucesso");
                res.send(`OK - /bmd/run/aux/${req.params.tipo}/${req.params.input}`);
            }
        });
    });

    app.get('/bmd/run/pgm/:tipo/:input', function(req, res) {
        __SimpleSwitcher({
            method: "DefinirProgram", 
            param: {
                ip: ip,
                tipoInput: req.params.tipo,
                inputIndex: parseInt(req.params.input)
            }
        }, function (error, result) {
            if(error) {
                errorHandling(error, req, res);
            } else {
                console.log("sucesso");
                res.send(`OK - /bmd/run/pgm/${req.params.tipo}/${req.params.input}`);
            }
        });
    });

    app.get('/bmd/run/prev/:tipo/:input', function(req, res) {
        __SimpleSwitcher({
            method: "DefinirPreview", 
            param: {
                ip: ip,
                tipoInput: req.params.tipo,
                inputIndex: parseInt(req.params.input)
            }
        }, function (error, result) {
            if(error) {
                errorHandling(error, req, res);
            } else {
                console.log("sucesso");
                res.send(`OK - /bmd/run/prev/${req.params.tipo}/${req.params.input}`);
            }
        });
    });

}
