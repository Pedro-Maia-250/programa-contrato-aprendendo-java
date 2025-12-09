package org.lunarvoid.projetocontratos.repositores;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.lunarvoid.projetocontratos.entidades.Contrato;
import org.lunarvoid.projetocontratos.entidades.Parcela;
import org.lunarvoid.projetocontratos.exeçoes.DBexeption;
import org.lunarvoid.projetocontratos.repositores.enumeradores.ParcelaEnumerador;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class ParcelaRepositor {

    private static final String API_URL = "http://177.36.245.165/api";

    public ParcelaRepositor() {}

    static void enviarParcelasParaAPI(Contrato contrato) {
        for (Parcela p : contrato.getParcelas()) {
            try {
                enviarParcela(contrato, p);
            } catch (Exception e) {
                throw new DBexeption("Erro ao enviar parcela: " + e.getMessage());
            }
        }
    }

    private static void enviarParcela(Contrato contrato, Parcela p) throws Exception {

        URL url = java.net.URI.create(API_URL + "/parcelas").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = """
            {
                "numero": "%s",
                "valor": %s,
                "datap": "%s",
                "statusp": "%s"
            }
            """.formatted(
                    contrato.getNumero(),
                    p.getValorParcela(),
                    p.getMes().toString(),
                    p.getStatus().name()
            );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new DBexeption("Erro ao enviar parcela para API. Status: " + status);
        }
    }

    public List<Parcela> getParcelasBanco(Contrato contrato) {

        try {
            URL url = java.net.URI.create(API_URL + "/parcelas/" + contrato.getNumero()).toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            if (status != 200) {
                throw new DBexeption("Erro ao buscar parcelas. Status: " + status);
            }

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            return parseParcelasJson(json.toString());

        } catch (Exception e) {
            throw new DBexeption(e.getMessage());
        }
    }

    private List<Parcela> parseParcelasJson(String json) {
        Gson gson = new Gson();

        // transforma o JSON inteiro em um array do tipo JsonObject
        JsonArray array = gson.fromJson(json, JsonArray.class);

        List<Parcela> lista = new ArrayList<>();

        for (JsonElement elem : array) {
            JsonObject obj = elem.getAsJsonObject();

            String data = obj.get("datap").getAsString();
            double valor = obj.get("valor").getAsDouble();
            String status = obj.get("statusp").getAsString();

            Parcela p = new Parcela(
                java.time.LocalDate.parse(data),
                valor,
                ParcelaEnumerador.valueOf(status)
            );

            lista.add(p);
        }

        return lista;
    }

}
