package org.example.service.strategy.etiqueta;

import org.example.domain.Entrega;
import org.example.service.Interface.IGeradorEtiqueta;

public class GeradorEtiquetaPadrao implements IGeradorEtiqueta {
    @Override
    public String gerar(Entrega entrega, String custoFormatado) {
        return String.format(
                """
                ****************************************
                ** ETIQUETA DE ENVIO           **
                ****************************************
                Destinatário: %s
                Endereço: %s
                ----------------------------------------
                Peso: %.2f kg
                Tipo de Frete: %s
                Custo do Frete: %s
                ****************************************
                """,
                entrega.getDestinatario(),
                entrega.getEndereco(),
                entrega.getPeso(),
                entrega.getTipoFrete(),
                custoFormatado
        );
    }
}