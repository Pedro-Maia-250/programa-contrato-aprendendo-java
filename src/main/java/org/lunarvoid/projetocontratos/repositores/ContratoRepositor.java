package org.lunarvoid.projetocontratos.repositores;

import org.lunarvoid.projetocontratos.utilitarios.Config;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.lunarvoid.projetocontratos.entidades.Contrato;
import org.lunarvoid.projetocontratos.entidades.Parcela;
import org.lunarvoid.projetocontratos.entidades.Picpay;
import org.lunarvoid.projetocontratos.exeçoes.DBexeption;
import org.lunarvoid.projetocontratos.seviços.SDPDP;

public class ContratoRepositor {

    private static final String API_URL = Config.get("api.base.url");
    private static final String API_KEY = Config.get("api.key");


    public ContratoRepositor() {}

    public void addContrato(Contrato contrato, int qtdparcelas) {

    if (contrato.getNumero().length() != 4) {
        throw new IllegalArgumentException("O número do contrato precisa contar 4 digitos");
    }
    if (qtdparcelas < 1) {
        throw new IllegalArgumentException("O número de parcelas precisa ser igual ou superior a 1");
    }

    try {

        SDPDP.processarParcelas(qtdparcelas, contrato, new Picpay());

        URL url = java.net.URI.create(API_URL + "/contratos/parcelas").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("X-API-KEY", API_KEY);
        conn.setDoOutput(true);

        StringBuilder parcelasJson = new StringBuilder();

        parcelasJson.append("["); 

        for (int i = 0; i < contrato.getParcelas().size(); i++) {
            Parcela p = contrato.getParcelas().get(i);

            parcelasJson.append("{");
            parcelasJson.append("\"valor\": ").append(p.getValorParcela()).append(",");
            parcelasJson.append("\"datap\": \"").append(p.getMes().toString()).append("\",");
            parcelasJson.append("\"statusp\": \"").append(p.getStatus()).append("\"");
            parcelasJson.append("}");

            if (i < contrato.getParcelas().size() - 1) {
                parcelasJson.append(",");
            }
        }

        parcelasJson.append("]"); 

        String json = """
        {
            "contrato": {
                "numero": "%s",
                "valorc": %s,
                "datac": "%s"
            },
            "parcelas": %s
        }
        """.formatted(
            contrato.getNumero(),
            contrato.getTotal(),
            contrato.getData().toString(),
            parcelasJson.toString()
        );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new DBexeption("Erro ao enviar contrato para API. Status: " + status);
        }

    } catch (Exception e) {
        throw new DBexeption(e.getMessage());
    }
}

}
