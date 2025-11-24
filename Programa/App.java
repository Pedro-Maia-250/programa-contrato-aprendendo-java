package Programa;

import Entidades.Contrato;
import Entidades.Parcela;
import Repositores.ContratoRepositor;
import Repositores.ParcelaRepositor;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args){
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Entre com os dados do contrato");
            System.out.print("Digite o numero do contrato: ");
            String numero = sc.next();
            System.out.print("Entre com o valor do contrato: ");
            Double valor = sc.nextDouble();
            System.out.print("Entre com a quantidade de parcelas: ");
            Integer qtd = sc.nextInt();
            
            Contrato ct = new Contrato(numero, LocalDate.now(), valor);
            
            new ContratoRepositor().addContrato(ct, qtd);
            List<Parcela> parcelas = new ParcelaRepositor().getParcelasBanco(ct);

            for (Parcela p : parcelas){
                System.out.println(p);
            }
            
        }
    }
}