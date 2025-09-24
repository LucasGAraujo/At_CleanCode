package org.example.service.strategy.frete;

import org.example.domain.Entrega;
import org.example.service.Interface.CalculadoraFrete;

public class FreteFixoDez implements CalculadoraFrete {
    @Override
    public double calcula(Entrega entrega) {
        double custo = 10.0; // Lógica de negócio: custo fixo de R$ 10,00

        System.out.printf(
                "[CALC] FreteFixoDez: Aplicando custo fixo. Custo final: R$ %.2f\n",
                custo
        );
        return custo;
    }
}

