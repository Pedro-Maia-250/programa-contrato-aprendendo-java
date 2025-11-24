package Entidades;

import Repositores.Enumeradores.ParcelaEnumerador;
import java.time.LocalDate;

@SuppressWarnings("FieldMayBeFinal")

public class Parcela{
    private LocalDate mes;
    private Double valorParcela;
    private ParcelaEnumerador status;

    public Parcela(LocalDate mes, Double valor, ParcelaEnumerador status){
        this.mes = mes;
        this.valorParcela = valor;
        this.status = status;
    }

    public LocalDate getMes(){
        return mes;
    }

    public Double getValorParcela(){
        return valorParcela;
    }

    public ParcelaEnumerador getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return getMes() + " - " + getValorParcela() + " - " + getStatus();
    }
}