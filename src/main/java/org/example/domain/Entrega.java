package org.example.domain;

import org.example.exception.RegraException;

import java.util.Objects;

public final class Entrega {
    private final String endereco;
    private final double peso;
    private final TipoFrete tipoFrete;
    private final String destinatario;

    public Entrega(String endereco, double peso, TipoFrete tipoFrete, String destinatario) {
        if (endereco == null || endereco.isBlank()) {
            throw new RegraException("Endereço não pode ser nulo ou vazio.");
        }
        if (destinatario == null || destinatario.isBlank()) {
            throw new RegraException("Destinatário não pode ser nulo ou vazio.");
        }
        if (peso <= 0) {
            throw new RegraException("Peso deve ser maior que zero.");
        }
        Objects.requireNonNull(tipoFrete, "Tipo de frete é obrigatório.");
        this.endereco = endereco;
        this.peso = peso;
        this.tipoFrete = tipoFrete;
        this.destinatario = destinatario;
    }

    public String getEndereco() {
        return endereco;
    }

    public double getPeso() {
        return peso;
    }

    public TipoFrete getTipoFrete() {
        return tipoFrete;
    }

    public String getDestinatario() {
        return destinatario;
    }
}
