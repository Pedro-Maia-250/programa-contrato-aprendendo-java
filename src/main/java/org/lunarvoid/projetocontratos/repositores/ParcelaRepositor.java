package org.lunarvoid.projetocontratos.repositores;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lunarvoid.projetocontratos.entidades.Contrato;
import org.lunarvoid.projetocontratos.entidades.Parcela;
import org.lunarvoid.projetocontratos.exeçoes.DBexeption;
import org.lunarvoid.projetocontratos.repositores.enumeradores.ParcelaEnumerador;
import org.lunarvoid.projetocontratos.utilitarios.DB;

public class ParcelaRepositor{
    private static Connection conn;

    public ParcelaRepositor(){
        conn = DB.getConnection();
    }

    static void addParcelas(Connection conn ,Contrato contrato){

        for(Parcela p: contrato.getParcelas()){
            try (PreparedStatement stmt = conn.prepareStatement("insert into parcelas(numero, valor, datap, statusp) values (?,?,?,?)")) {

            stmt.setString(1, contrato.getNumero());
            stmt.setDouble(2, p.getValorParcela());
            stmt.setDate(3, Date.valueOf(p.getMes()));
            stmt.setString(4, p.getStatus().name());

            stmt.executeUpdate();

            } catch (SQLException e) {
                throw new DBexeption(e.getMessage());
            }
        }
    }

    public List<Parcela> getParcelasBanco(Contrato contrato){
        List<Parcela> parcelas = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("select * from parcelas where numero = ?")) {
            stmt.setString(1, contrato.getNumero());
            ResultSet rest = stmt.executeQuery();

            while (rest.next()){
                Parcela p = new Parcela(
                    rest.getDate("datap").toLocalDate(), 
                    rest.getDouble("valor"),
                    ParcelaEnumerador.valueOf(rest.getString("statusp"))
                );
                parcelas.add(p);
            }
            
            return parcelas;

        } catch (SQLException e) {
            throw new DBexeption(e.getMessage());
        }
    }
}