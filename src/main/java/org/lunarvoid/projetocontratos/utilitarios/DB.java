package org.lunarvoid.projetocontratos.utilitarios;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.lunarvoid.projetocontratos.exeçoes.DBexeption;

public class DB {

    private static Connection conn = null;

    public static Connection getConnection(){
        if(conn == null){
            try {

                conn = DriverManager.getConnection(loadProperties().getProperty("dburl"), loadProperties());
                
            } catch (SQLException e) {
                throw new DBexeption(e.getMessage() + " A conn não pode ser criada");
            }
        }
        return conn;
    }

       public static void closeConnection(){
        if(conn != null)
        try {
            conn.close();
        } catch (SQLException e) {
            throw new DBexeption(e.getMessage() + " A conn não pode ser fechada");
        }

    }

    private static Properties loadProperties(){
        try (FileInputStream fs = new FileInputStream("src/main/resources/db.properties")){
            Properties props = new Properties();
            props.load(fs);
            return props;

        }catch(IOException e){
            throw new DBexeption(e.getMessage() + " O properties está errado");
        }
    }

    private DB(){}

}
