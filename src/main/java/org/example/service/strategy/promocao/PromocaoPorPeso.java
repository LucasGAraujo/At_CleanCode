package org.example.service.strategy.promocao;

import org.example.domain.Entrega;
import org.example.service.Interface.IRegraPromocional;

public class PromocaoPorPeso implements IRegraPromocional {
    @Override
    public Entrega aplicar(Entrega entrega) {
        if (entrega.getPeso() > 10.0) {
            System.out.printf("[PROMO] PromocaoPorPeso: CONDIÇÃO ATENDIDA! Peso de %.2fkg será reduzido.\n", entrega.getPeso());
            return new Entrega(entrega.getEndereco(), entrega.getPeso() - 1.0, entrega.getTipoFrete(), entrega.getDestinatario());
        }
        return entrega;
    }
}