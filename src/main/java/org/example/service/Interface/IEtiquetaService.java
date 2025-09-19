package org.example.service.Interface;
import org.example.domain.Entrega;

public interface IEtiquetaService {
    String gerarEtiqueta(Entrega entrega, IGeradorEtiqueta gerador);
}
