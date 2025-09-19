package org.example.service.strategy.etiqueta;

import org.example.domain.Entrega;
import org.example.service.Interface.IGeradorEtiqueta;

public class GeradorEtiquetaResumida implements IGeradorEtiqueta {
    @Override
    public String gerar(Entrega entrega, String custoFormatado) {
        return String.format(
                "Resumo da Entrega -> Destinatário: %s | Tipo: %s | Custo: %s",
                entrega.getDestinatario(),
                entrega.getTipoFrete(),
                custoFormatado
        );
    }
}