package org.lunarvoid.projetocontratos.interfaces;

public interface InterfacePagamento {
    public Double calcularParcelas(Double amount, Integer nParcela);
}