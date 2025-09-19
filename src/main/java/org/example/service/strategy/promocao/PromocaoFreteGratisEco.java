package org.example.service.strategy.promocao;

import org.example.domain.Entrega;
import org.example.domain.TipoFrete;
import org.example.service.Interface.IRegraPromocional;

public class PromocaoFreteGratisEco implements IRegraPromocional {

    @Override
    public Entrega aplicar(Entrega entrega) {
        return entrega;
    }

    public boolean isAplicavel(Entrega entrega) {
        return entrega.getTipoFrete() == TipoFrete.ECO && entrega.getPeso() < 2.0;
    }
}

