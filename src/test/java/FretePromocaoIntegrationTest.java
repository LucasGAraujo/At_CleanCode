import org.example.domain.Entrega;
import org.example.domain.TipoFrete;
import org.example.service.FreteService;
import org.example.service.Interface.IFreteService;
import org.example.service.Interface.IRegraPromocional;
import org.example.service.strategy.promocao.PromocaoFreteGratisEco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FretePromocaoIntegrationTest {

    private IFreteService freteService;

    @BeforeEach
    void setUp() {
        IRegraPromocional promocaoFreteGratis = new PromocaoFreteGratisEco();
        freteService = new FreteService(List.of(promocaoFreteGratis));
    }

    @Test
    @DisplayName("Frete Expresso: Deve ignorar a promoção de frete grátis e calcular o valor normal")
    void deveCalcularFreteExpressoNormalmenteQuandoPromocaoGratisNaoSeAplica() {
        Entrega entregaOriginal = new Entrega("Rua Expresso, 123", 5.0, TipoFrete.EXP, "Cliente Expresso");
        double custoEsperado = 17.5;
        double custoFinal = freteService.calcularFrete(entregaOriginal);
        assertThat(custoFinal).isEqualTo(custoEsperado);
    }

    @Test
    @DisplayName("Frete Padrão: Deve ignorar a promoção de frete grátis e calcular o valor normal")
    void deveCalcularFretePadraoNormalmenteQuandoPromocaoGratisNaoSeAplica() {
        Entrega entregaOriginal = new Entrega("Av. Padrão, 456", 8.0, TipoFrete.PAD, "Cliente Padrão");
        double custoEsperado = 9.6;
        double custoFinal = freteService.calcularFrete(entregaOriginal);
        assertThat(custoFinal).isEqualTo(custoEsperado);
    }

    @Test
    @DisplayName("Frete Econômico: Deve calcular valor normal para item acima do peso da promoção")
    void deveCalcularFreteEconomicoNormalmenteParaItensAcimaDoPesoDaPromocao() {
        Entrega entregaOriginal = new Entrega("Travessa Eco, 789", 3.0, TipoFrete.ECO, "Cliente Eco Pesado");
        double custoEsperado = new org.example.service.strategy.frete.FreteEconomico().calcula(entregaOriginal);
        double custoFinal = freteService.calcularFrete(entregaOriginal);
        assertThat(custoFinal).isEqualTo(custoEsperado);
    }

    @Test
    @DisplayName("Frete Econômico: Deve ter custo ZERO para item leve com promoção de frete grátis")
    void deveZerarCustoDoFreteEconomicoParaItensLevesComPromocaoGratis() {
        Entrega entregaOriginal = new Entrega("Viela Leve, 101", 1.5, TipoFrete.ECO, "Cliente Eco Leve");
        double custoFinal = freteService.calcularFrete(entregaOriginal);
        assertThat(custoFinal).isZero();
    }
}