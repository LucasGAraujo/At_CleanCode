package org.example.service.Interface;

import org.example.domain.Entrega;

public interface IGeradorEtiqueta {
    String gerar(Entrega entrega, String custoFormatado);
}
