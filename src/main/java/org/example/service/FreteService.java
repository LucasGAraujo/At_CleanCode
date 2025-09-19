package org.example.service;

import org.example.domain.Entrega;
import org.example.domain.TipoFrete;
import org.example.exception.RegraException;
import org.example.service.Interface.IFreteService;
import org.example.service.Interface.CalculadoraFrete;
import org.example.service.Interface.IRegraPromocional;
import org.example.service.strategy.frete.FreteEconomico;
import org.example.service.strategy.frete.FreteExpresso;
import org.example.service.strategy.frete.FretePadrao;
import org.example.service.strategy.promocao.PromocaoFreteGratisEco;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FreteService implements IFreteService {

    private static final Map<TipoFrete, CalculadoraFrete> CALCULADORAS = new EnumMap<>(TipoFrete.class);
    private final List<IRegraPromocional> regrasPromocionais;

    static {
        CALCULADORAS.put(TipoFrete.EXP, new FreteExpresso());
        CALCULADORAS.put(TipoFrete.PAD, new FretePadrao());
        CALCULADORAS.put(TipoFrete.ECO, new FreteEconomico());
    }
    public FreteService(List<IRegraPromocional> regrasPromocionais) {
        this.regrasPromocionais = regrasPromocionais;
    }
    @Override
    public double calcularFrete(Entrega entrega) {

        if (entrega == null) {
            throw new RegraException("Dados da entrega não podem ser nulos para calcular o frete.");
        }
        for (IRegraPromocional regra : regrasPromocionais) {
            if (regra instanceof PromocaoFreteGratisEco promocaoFreteGratis) {
                if (promocaoFreteGratis.isAplicavel(entrega)) {
                    return 0.0;
                }
            }
        }
        Entrega entregaPromocional = aplicarPromocoes(entrega);
        TipoFrete tipo = entregaPromocional.getTipoFrete();
        CalculadoraFrete calculadora = CALCULADORAS.get(tipo);
        if (calculadora == null) {
            throw new RegraException("Tipo de frete não suportado: " + tipo);
        }
        return calculadora.calcula(entregaPromocional);
    }
    private Entrega aplicarPromocoes(Entrega entregaOriginal) {
        Entrega entregaModificada = entregaOriginal;
        for (IRegraPromocional regra : regrasPromocionais) {
            if (!(regra instanceof PromocaoFreteGratisEco)) {
                entregaModificada = regra.aplicar(entregaModificada);
            }
        }
        return entregaModificada;
    }
}