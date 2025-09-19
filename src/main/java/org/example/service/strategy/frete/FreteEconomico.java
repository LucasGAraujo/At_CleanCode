package org.example.service.strategy.frete;

import org.example.domain.Entrega;
import org.example.service.Interface.CalculadoraFrete;

public class FreteEconomico implements CalculadoraFrete {
    @Override
    public double calcula(Entrega entrega) {
        double custo = entrega.getPeso() * 1.1 - 5;
        if (custo < 0) {
            custo = 0;
        }

        System.out.printf(
                "[CALC] FreteEconomico: Calculando com base no peso %.2fkg. Custo final: R$ %.2f\n",
                entrega.getPeso(),
                custo
        );
        return custo;
    }
}