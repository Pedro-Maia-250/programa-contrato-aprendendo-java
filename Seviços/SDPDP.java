package Seviços;

import Entidades.Contrato;
import Entidades.Parcela;
import Interface.InterfacePagamento;
import Repositores.Enumeradores.ParcelaEnumerador;

public class SDPDP {

    public static void processarParcelas(Integer nPT, Contrato cT ,InterfacePagamento intf){
        Double valorParcelasSimples = cT.getTotal() / nPT;

        for (int i = 1; i <= nPT; i++){
            Parcela parcela = new Parcela(cT.getData().plusMonths(i), intf.calcularParcelas(valorParcelasSimples, i), ParcelaEnumerador.A_PAGAR);
            cT.addParcela(parcela);
        }
    }
}