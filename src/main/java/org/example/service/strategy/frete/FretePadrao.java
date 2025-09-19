package org.example.service.strategy.frete;

import org.example.domain.Entrega;
import org.example.service.Interface.CalculadoraFrete;

public class FretePadrao implements CalculadoraFrete {
    @Override
    public double calcula(Entrega entrega) {

        return entrega.getPeso() * 1.2;
    }
}
