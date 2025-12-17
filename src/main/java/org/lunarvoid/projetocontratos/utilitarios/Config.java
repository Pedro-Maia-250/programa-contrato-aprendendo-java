package org.lunarvoid.projetocontratos.utilitarios;

import java.io.InputStream;
import java.util.Properties;
public class Config {

    private static Properties props = new Properties();

    static {
        try (InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("Arquivo application.properties não encontrado");
            }

            props.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar configurações: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }


}
