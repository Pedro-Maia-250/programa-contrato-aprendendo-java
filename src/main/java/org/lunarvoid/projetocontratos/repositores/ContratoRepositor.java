package org.lunarvoid.projetocontratos.repositores;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.lunarvoid.projetocontratos.entidades.Contrato;
import org.lunarvoid.projetocontratos.entidades.Picpay;
import org.lunarvoid.projetocontratos.exeçoes.DBexeption;
import org.lunarvoid.projetocontratos.seviços.SDPDP;

public class ContratoRepositor {

    private static final String API_URL = "http://177.36.245.165/api";

    public ContratoRepositor() {}

    public void addContrato(Contrato contrato, int qtdparcelas) {

        if (contrato.getNumero().length() > 4) {
            throw new IllegalArgumentException("O número do contrato não pode ter mais de 4 caracteres.");
        }
        if (qtdparcelas < 1) {
            throw new IllegalArgumentException("O número de parcelas precisa ser igual ou superior a 1");
        }

        try {
            // 1️⃣ GERA AS PARCELAS NO JAVA
            SDPDP.processarParcelas(qtdparcelas, contrato, new Picpay());

            // 2️⃣ MANDA O CONTRATO PARA A API
            enviarContratoParaAPI(contrato);

            // 3️⃣ MANDA AS PARCELAS PARA A API
            ParcelaRepositor.enviarParcelasParaAPI(contrato);

        } catch (Exception e) {
            throw new DBexeption(e.getMessage());
        }
    }

    private void enviarContratoParaAPI(Contrato contrato) throws Exception {
        URL url = java.net.URI.create(API_URL + "/contratos").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = """
            {
                "numero": "%s",
                "valor": %s,
                "datac": "%s"
            }
            """.formatted(
                    contrato.getNumero(),
                    contrato.getTotal(),
                    contrato.getData().toString()
            );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new DBexeption("Erro ao enviar contrato para API. Status: " + status);
        }
    }
}
