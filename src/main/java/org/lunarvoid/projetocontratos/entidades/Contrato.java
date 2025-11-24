package org.lunarvoid.projetocontratos.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")

public class Contrato{

    private String numero;
    private LocalDate data;
    private Double total;

    List<Parcela> parcelas = new ArrayList<>();

    public Contrato(String numero, LocalDate data, Double total){
        this.numero = numero;
        this.data = data;
        this.total = total;
    }

    public String getNumero(){
        return numero;
    }

    public LocalDate getData(){
        return data;
    }

    public Double getTotal(){
        return total;
    }

    public List<Parcela> getParcelas(){
        return parcelas;
    }

    public void addParcela(Parcela e){
        parcelas.add(e);
    }

}