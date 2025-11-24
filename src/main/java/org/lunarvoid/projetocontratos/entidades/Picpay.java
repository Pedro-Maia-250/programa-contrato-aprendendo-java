package org.lunarvoid.projetocontratos.entidades;

import org.lunarvoid.projetocontratos.interfaces.InterfacePagamento;

public class Picpay implements InterfacePagamento {
    @Override
    public Double calcularParcelas(Double amount, Integer nParcela){
        Double n1 = amount * 0.01;
        Double total1 = amount + (n1 * nParcela);
        Double n2 = total1 * 0.02;
        return total1 + n2;
    }
}
