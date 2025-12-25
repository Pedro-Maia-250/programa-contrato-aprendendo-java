package org.lunarvoid.projetocontratos.utilitarios;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties props = new Properties();

    static {
        try (InputStream input =
                Config.class
                      .getClassLoader()
                      .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException(
                    "application.properties não encontrado no classpath"
                );
            }

            props.load(input);

        } catch (Exception e) {
            throw new RuntimeException(
                "Erro ao carregar application.properties: " + e.getMessage(),
                e
            );
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
