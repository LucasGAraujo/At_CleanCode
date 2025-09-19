package org.example.service;
import org.example.domain.Entrega;
import org.example.exception.RegraException;
import org.example.service.Interface.IEtiquetaService;
import org.example.service.Interface.IFreteService;
import org.example.service.Interface.IGeradorEtiqueta;

import java.text.NumberFormat;
import java.util.Locale;

public class EtiquetaService implements IEtiquetaService {
    private final IFreteService freteService;

    public EtiquetaService(IFreteService freteService) {
        this.freteService = freteService;
    }

    @Override
    public String gerarEtiqueta(Entrega entrega, IGeradorEtiqueta gerador) {
        if (entrega == null || gerador == null) {
            throw new RegraException("Dados da entrega e gerador não podem ser nulos.");
        }
        String custoFormatado = calcularEFormatarCusto(entrega);
        return gerador.gerar(entrega, custoFormatado);
    }

    private String calcularEFormatarCusto(Entrega entrega) {
        double custo = freteService.calcularFrete(entrega);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(brasil);
        return formatoMoeda.format(custo);
    }
}