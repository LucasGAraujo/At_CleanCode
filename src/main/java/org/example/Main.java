package org.example;

import org.example.domain.Entrega;
import org.example.domain.TipoFrete;
import org.example.exception.RegraException;
import org.example.service.EtiquetaService;
import org.example.service.FreteService;
import org.example.service.Interface.*;
import org.example.service.strategy.etiqueta.GeradorEtiquetaPadrao;
import org.example.service.strategy.etiqueta.GeradorEtiquetaResumida;
import org.example.service.strategy.frete.FreteEconomico;
import org.example.service.strategy.frete.FreteExpresso;
import org.example.service.strategy.frete.FreteFixoDez;
import org.example.service.strategy.frete.FretePadrao;
import org.example.service.strategy.promocao.PromocaoFreteGratisEco;
import org.example.service.strategy.promocao.PromocaoPorPeso;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("--- 1. CONFIGURANDO A SUÍTE DE TESTES ---");

        // 1.1: Criamos o mapa com todas as estratégias de frete disponíveis na aplicação.
        Map<TipoFrete, CalculadoraFrete> calculadorasDisponiveis = new EnumMap<>(TipoFrete.class);
        calculadorasDisponiveis.put(TipoFrete.PAD, new FretePadrao());
        calculadorasDisponiveis.put(TipoFrete.EXP, new FreteExpresso());
        calculadorasDisponiveis.put(TipoFrete.ECO, new FreteEconomico());
        calculadorasDisponiveis.put(TipoFrete.FIXO_DEZ, new FreteFixoDez());

        // 1.2: Ativamos as promoções que queremos testar. (Isso você já tinha)
        List<IRegraPromocional> promocoesAtivas = List.of(
                new PromocaoPorPeso(),
                new PromocaoFreteGratisEco()
        );

        // 1.3: Instanciamos os serviços injetando TODAS as dependências necessárias.
        IFreteService freteService = new FreteService(calculadorasDisponiveis, promocoesAtivas);
        IEtiquetaService etiquetaService = new EtiquetaService(freteService);
        IGeradorEtiqueta formatoPadrao = new GeradorEtiquetaPadrao();
        IGeradorEtiqueta formatoResumido = new GeradorEtiquetaResumida();

        System.out.println("Sistema configurado com " + calculadorasDisponiveis.size() + " tipos de frete e " + promocoesAtivas.size() + " promoções ativas.\n");

        // CENÁRIO 1: NENHUMA PROMOÇÃO APLICÁVEL
        System.out.println("--- CENÁRIO 1: Frete Padrão, sem promoções ---");
        Entrega cenario1 = new Entrega("Rua Comum, 50", 7.0, TipoFrete.PAD, "Cliente Básico");
        System.out.println("--> Testando com peso de 7.0kg (não ativa promoção de peso) e tipo PAD (não ativa frete grátis).");
        executarTeste(cenario1, etiquetaService, formatoPadrao, formatoResumido);

        // CENÁRIO 2: APENAS PROMOÇÃO POR PESO
        System.out.println("\n--- CENÁRIO 2: Frete Expresso, com promoção de peso ---");
        Entrega cenario2 = new Entrega("Avenida Larga, 2500", 12.5, TipoFrete.EXP, "Cliente Ouro");
        System.out.println("--> Testando com peso de 12.5kg (DEVE ativar promoção de peso) e tipo EXP.");
        executarTeste(cenario2, etiquetaService, formatoPadrao, formatoResumido);

        // CENÁRIO 3: APENAS PROMOÇÃO DE FRETE GRÁTIS (DEVE TER PRIORIDADE)
        System.out.println("\n--- CENÁRIO 3: Frete Econômico, com promoção de frete grátis ---");
        Entrega cenario3 = new Entrega("Viela da Sorte, 7", 1.9, TipoFrete.ECO, "Cliente Sortudo");
        System.out.println("--> Testando com peso de 1.9kg e tipo ECO (DEVE ativar frete grátis e ignorar outras lógicas).");
        executarTeste(cenario3, etiquetaService, formatoPadrao, formatoResumido);

        // CENÁRIO 4: FRETE ECONÔMICO, MAS SEM DIREITO A FRETE GRÁTIS
        System.out.println("\n--- CENÁRIO 4: Frete Econômico, sem frete grátis ---");
        Entrega cenario4 = new Entrega("Alameda dos Peixes, 33", 4.0, TipoFrete.ECO, "Cliente Econômico");
        System.out.println("--> Testando com tipo ECO mas peso de 4.0kg (NÃO deve ativar frete grátis).");
        executarTeste(cenario4, etiquetaService, formatoPadrao, formatoResumido);

        // CENÁRIO 5: TESTE DE EXCEÇÃO
        System.out.println("\n--- CENÁRIO 5: Teste de Validação (Exceção) ---");
        try {
            System.out.println("--> Tentando criar uma entrega com endereço vazio...");
            new Entrega("", 5.0, TipoFrete.PAD, "Cliente Fantasma");
        } catch (RegraException e) {
            System.out.println("SUCESSO: Exceção capturada como esperado!");
            System.out.println("Mensagem: " + e.getMessage());
        }
        System.out.println("\n--- CENÁRIO 6: Novo Frete Fixo de R$ 10,00 (Teste OCP) ---");
        Entrega cenario6 = new Entrega("Rua Nova, 123", 2.0, TipoFrete.FIXO_DEZ, "Cliente Teste");
        System.out.println("--> Testando um tipo de frete adicionado sem alterar o FreteService.");
        executarTeste(cenario6, etiquetaService, formatoPadrao, formatoResumido);

    }

    private static void executarTeste(Entrega entrega, IEtiquetaService etiquetaService, IGeradorEtiqueta formatoPadrao, IGeradorEtiqueta formatoResumido) {
        try {
            System.out.println("\n--- Gerando Etiqueta Padrão ---");
            String etiquetaPadrao = etiquetaService.gerarEtiqueta(entrega, formatoPadrao);
            System.out.println(etiquetaPadrao);

            System.out.println("\n--- Gerando Etiqueta Resumida ---");
            String etiquetaResumida = etiquetaService.gerarEtiqueta(entrega, formatoResumido);
            System.out.println(etiquetaResumida);
        } catch (Exception e) {
            System.err.println("ERRO INESPERADO NO TESTE: " + e.getMessage());
        } finally {
            System.out.println("-------------------------------------------------");
        }
    }
}