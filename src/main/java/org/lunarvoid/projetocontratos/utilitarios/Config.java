package org.lunarvoid.projetocontratos.utilitarios;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Properties props = new Properties();

    static {
        try (InputStream input = new FileInputStream("src\\main\\resourses\\application.properties")) {

            props.load(input);

        } catch (Exception e) {
            throw new RuntimeException(
                "Erro ao carregar application.properties. " +
                "Verifique se o arquivo está na raiz do projeto. " +
                "Detalhe: " + e.getMessage()
            );
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
