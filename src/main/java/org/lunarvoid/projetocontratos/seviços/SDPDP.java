package org.lunarvoid.projetocontratos.seviços;

import org.lunarvoid.projetocontratos.entidades.Contrato;
import org.lunarvoid.projetocontratos.entidades.Parcela;
import org.lunarvoid.projetocontratos.interfaces.InterfacePagamento;
import org.lunarvoid.projetocontratos.repositores.enumeradores.ParcelaEnumerador;

public class SDPDP {

    public static void processarParcelas(Integer nPT, Contrato cT ,InterfacePagamento intf){
        Double valorParcelasSimples = cT.getTotal() / nPT;

        for (int i = 1; i <= nPT; i++){
            Parcela parcela = new Parcela(cT.getData().plusMonths(i), intf.calcularParcelas(valorParcelasSimples, i), ParcelaEnumerador.A_PAGAR);
            cT.addParcela(parcela);
        }
    }
}