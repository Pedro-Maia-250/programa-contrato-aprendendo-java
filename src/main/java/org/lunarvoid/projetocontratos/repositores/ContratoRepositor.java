package org.lunarvoid.projetocontratos.repositores;

import org.lunarvoid.projetocontratos.utilitarios.Config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.URI;

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

            URL url = URI.create(API_URL + "/contratos/parcelas").toURL();
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

    public String getTamanhoTexto(String texto){

        System.out.println("cheguei aqui 2");

        if (texto.isEmpty()){
            throw new IllegalArgumentException("texto invalido"); 
        }

        try {
            
            URL url = URI.create(API_URL + "/string/t").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-API-KEY", API_KEY);
            conn.setDoOutput(true);

            System.out.println("cheguei aqui 3 - " + url.toString());

            String json = """
            {
                "texto": "%s"
            }
            """.formatted(texto);

            System.out.println("cheguei aqui 4 - " + API_KEY + " " + API_URL);

            try (OutputStream os = conn.getOutputStream()){
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            
            if(status != 200){
                throw new Exception("falha ao descobrir o tamanho do texto " + texto);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while((linha = reader.readLine()) != null){
                resposta.append(linha);
            }

            Gson gson = new Gson();

            JsonObject obj = gson.fromJson(resposta.toString(), JsonObject.class);

            Boolean error = obj.get("error").getAsBoolean();

            if(error){
                throw new Exception(obj.get("mensage").getAsString());
            }

            return obj.get("resultado").getAsString();

        } catch (Exception e) {
            throw new RuntimeException("falha ao testar texto" + e.getMessage());
        }
    }

}
