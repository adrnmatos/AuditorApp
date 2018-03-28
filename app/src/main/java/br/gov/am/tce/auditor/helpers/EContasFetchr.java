package br.gov.am.tce.auditor.helpers;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.domain.BemPublico;
import br.gov.am.tce.auditor.domain.Contract;
import br.gov.am.tce.auditor.domain.Medicao;

/**
 * Created by Adriano on 12/03/2018.
 */

public class EContasFetchr {
    private static final String TAG = "EContasFetchr";

    private static final String API_KEY = "XXXXX";
    private static final String FETCH_CONTRACTS = "econtas.contratos";
    private static final String FETCH_BEMPUBLICO = "econtas.bempublico";
    private static final String FETCH_MEDICAO = "econtas.medicao";
    private static final Uri ENDPOINT = Uri
            .parse("https://econtas.tce.am.gov.br/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .build();

    public List<Contract> fetchContracts(String exercicio, String municipio, String jurisdicionado) {
        List<Contract> contractList = new ArrayList<>();

        String argumentStr = null;
        if(exercicio != null) {
            argumentStr += exercicio;
            if(municipio != null) argumentStr += "&" + municipio;
            if(jurisdicionado != null) argumentStr += "&" + jurisdicionado;
        } else {
            if(municipio != null) {
                argumentStr += municipio;
                if(jurisdicionado != null) argumentStr += "&" + jurisdicionado;
            }
            argumentStr += jurisdicionado;
        }

        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_CONTRACTS)
                    .appendQueryParameter("arguments", argumentStr)
                    .build().toString();
//            String jsonStringContracts = getUrlString(url);
            String jsonStringContracts = "{\"contratos\":{\"contrato\":[{\"id\":\"123\",\"numero\":\"456/2018\",\"prazo\":\"360\",\"dataInicio\":\"05/01/2018\",\"bemPublico\":\"PNT321\",\"contratado\":\"OAS Engenharia\"},{\"id\":\"456\",\"numero\":\"13/2007\",\"prazo\":\"180\",\"dataInicio\":\"21/05/2007\",\"bemPublico\":\"EE11491\",\"contratado\":\"Oderbreach Engenharia\",\"medicao\":[{\"id\":\"12\",\"name\":\"adrf\"}]},{\"id\":\"789\",\"numero\":\"458/2009\",\"prazo\":\"270\",\"dataInicio\":\"12/09/2009\",\"bemPublico\":\"HH42\",\"contratado\":\"Mendes Junior Engenharia\"},{\"id\":\"012\",\"numero\":\"25/2011\",\"prazo\":\"45\",\"dataInicio\":\"31/08/2011\",\"bemPublico\":\"PS875\",\"contratado\":\"Carrane Engenharia\"}]}}";
            JSONObject jsonBody = new JSONObject(jsonStringContracts);
            parseContracts(contractList, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "error");
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return contractList;
    }

    public BemPublico fetchBemPublico(String bemPublico_str) {
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_BEMPUBLICO)
                    .appendQueryParameter("arguments", bemPublico_str)
                    .build().toString();
            String jsonStringBemPublico = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonStringBemPublico);
            return parseBemPublico(jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        } finally {
            return null;
        }
    }

    public void fetchMedicao(String medicao) {
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_MEDICAO)
                    .appendQueryParameter("arguments", medicao)
                    .build().toString();
            String jsonStringMedicao = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonStringMedicao);
            parseMedicao(jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
    }

    private void parseContracts(List<Contract> contractList, JSONObject jsonBody)
        throws IOException, JSONException {

        JSONObject contractJsonObject = jsonBody.getJSONObject("contratos");
        JSONArray contractJsonArray = contractJsonObject.getJSONArray("contrato");

        for(int i = 0; i < contractJsonArray.length(); i++) {
            JSONObject contractObject = contractJsonArray.getJSONObject(i);
            Contract contract = new Contract();
            contract.setId(contractObject.getString("id"));
            contract.setNumero(contractObject.getString("numero"));
            contract.setPrazo(contractObject.getString("prazo"));
            contract.setDataInicio(contractObject.getString("dataInicio"));
            contract.setBemPublico(contractObject.getString("bemPublico"));
            contract.setContratado(contractObject.getString("contratado"));
            if(contractObject.has("medicao")) {
                JSONArray medicaoJsonArray = contractObject.getJSONArray("medicao");
                for(int j = 0; j < medicaoJsonArray.length(); j++) {
                    JSONObject medicaoObject = medicaoJsonArray.getJSONObject(j);
                    Medicao medicao = new Medicao();
                    medicao.setId(medicaoObject.getString("id"));
                    medicao.setName(medicaoObject.getString("name"));
                    contract.getMedicaoLista().add(medicao);
                }
            }
            contractList.add(contract);
        }
    }

    private BemPublico parseBemPublico(JSONObject jsonBody)
        throws IOException, JSONException {

        BemPublico bemPublico = new BemPublico();
        bemPublico.setId(jsonBody.getString("id"));
        bemPublico.setArea(jsonBody.getString("area"));
        bemPublico.setLatitude(jsonBody.getString("latitude"));
        bemPublico.setLongitude(jsonBody.getString("longitude"));
        bemPublico.setTipo(jsonBody.getString("tipo"));
        bemPublico.setNome(jsonBody.getString("nome"));
        bemPublico.setJurisdicionado(jsonBody.getString("jurisdicionado"));
        bemPublico.setEndereco(jsonBody.getString("endereco"));
        if(jsonBody.has("contrato")) {
            JSONArray contratoJsonArray = jsonBody.getJSONArray("contrato");
            for(int i = 0; i < contratoJsonArray.length(); i++) {
                JSONObject contratoObject = contratoJsonArray.getJSONObject(i);
                Contract contrato = new Contract();
            }
        }
        return bemPublico;
    }

    private Medicao parseMedicao (JSONObject jsonBody)
        throws IOException, JSONException {

        Medicao medicao = new Medicao();
        medicao.setId("id");
        medicao.setName("nome");

        return medicao;
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

}
