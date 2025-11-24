package Repositores;

import Entidades.Contrato;
import Entidades.Picpay;
import Exeçoes.DBexeption;
import Seviços.SDPDP;
import Utilitarios.DB;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContratoRepositor {
    private static Connection conn;

    public ContratoRepositor(){
        conn = DB.getConnection();
    }

    public void addContrato(Contrato contrato, int qtdparcelas){

        if (contrato.getNumero().length() > 4){
            throw new IllegalArgumentException("O número do contrato não pode ter mais de 4 caracteres.");
        }
        if (qtdparcelas < 1){
            throw new IllegalArgumentException("O número de parcelas precisa ser igual ou superior a 1");
        }

        try (PreparedStatement stmt = conn.prepareStatement("insert into contratos(numero, valor, datac) values (?,?,?)")) {
        
        conn.setAutoCommit(false);

        stmt.setString(1, contrato.getNumero());
        stmt.setDouble(2, contrato.getTotal());
        stmt.setDate(3, Date.valueOf(contrato.getData()));

        stmt.executeUpdate();
        SDPDP.processarParcelas(qtdparcelas, contrato, new Picpay());

        ParcelaRepositor.addParcelas(conn, contrato);

        conn.commit();

        } catch (SQLException e) {
            throw new DBexeption(e.getMessage());
        } finally{
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new DBexeption(e.getMessage());
            }
        }
    }
}