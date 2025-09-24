import org.example.domain.Entrega;
import org.example.domain.TipoFrete;
import org.example.exception.RegraException;
import org.example.service.EtiquetaService;
import org.example.service.Interface.IEtiquetaService;
import org.example.service.Interface.IFreteService;
import org.example.service.strategy.etiqueta.GeradorEtiquetaPadrao;
import org.example.service.strategy.etiqueta.GeradorEtiquetaResumida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class EtiquetaServiceTest {

private static class FreteServiceStub implements IFreteService {
    private double custoParaRetornar;

    public void setCustoParaRetornar(double custo) {
        this.custoParaRetornar = custo;
    }

    @Override
    public double calcularFrete(Entrega entrega) {
        return this.custoParaRetornar;
    }
}

private IEtiquetaService etiquetaService;
private FreteServiceStub freteServiceStub;

@BeforeEach
void setUp() {
    this.freteServiceStub = new FreteServiceStub();
    this.etiquetaService = new EtiquetaService(freteServiceStub);
}

@Test
@DisplayName("1. Deve gerar etiqueta com formato Padrão corretamente")
void deveGerarEtiquetaComFormatoPadrao() {
    Entrega entrega = new Entrega("Rua Padrão, 1", 5.0, TipoFrete.PAD, "João");
    GeradorEtiquetaPadrao formatador = new GeradorEtiquetaPadrao();
    freteServiceStub.setCustoParaRetornar(50.0);
    String etiqueta = etiquetaService.gerarEtiqueta(entrega, formatador);
    assertThat(etiqueta.replace("\u00A0", " "))
            .contains("Custo do Frete: R$ 50,00");
}

@Test
@DisplayName("2. Deve gerar etiqueta com formato Compacto corretamente")
void deveGerarEtiquetaComFormatoCompacto() {
    Entrega entrega = new Entrega("Rua Compacta, 2", 2.0, TipoFrete.ECO, "Maria");
    GeradorEtiquetaResumida formatador = new GeradorEtiquetaResumida();

    freteServiceStub.setCustoParaRetornar(12.35);

    String etiqueta = etiquetaService.gerarEtiqueta(entrega, formatador);

    assertThat(etiqueta).isEqualTo("Resumo da Entrega -> Destinatário: Maria | Tipo: ECO | Custo: R$ 12,35");
}

@Test
@DisplayName("3. Deve lançar exceção ao tentar gerar etiqueta para uma entrega nula")
void deveLancarExcecaoAoGerarEtiquetaParaEntregaNula() {
    GeradorEtiquetaResumida formatador = new GeradorEtiquetaResumida();

    assertThatThrownBy(() -> {
        etiquetaService.gerarEtiqueta(null, formatador);
    })
            .isInstanceOf(RegraException.class)
            .hasMessage( "Dados da entrega e gerador não podem ser nulos.");
}
}
