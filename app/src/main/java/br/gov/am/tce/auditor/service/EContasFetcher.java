package br.gov.am.tce.auditor.service;

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

import br.gov.am.tce.auditor.model.BemPublico;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Medicao;

/**
 * Created by Adriano on 12/03/2018.
 */

public class EContasFetcher {
    private static final String TAG = "EContasFetcher";

    private static final String API_KEY = "key";
    private static final String FETCH_BEM_PUBLICO = "eContas.bemPublico";
    private static final String FETCH_CONTRATO = "eContas.contrato";
    private static final String FETCH_MEDICAO = "eContas.medicao";
    private static final Uri ENDPOINT = Uri
            .parse("https://econtas.tce.am.gov.br/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .build();


    /* ********************** BEM PUBLICO *********************************************/
    // fetch all BP given city, owner, type and class of public work
    public List<ContextObject> fetchBemPublicoCollection(String municipio, String jurisdicionado/*, String tipo, String class */) {
        List<ContextObject> bemPublicoList = new ArrayList<>();
        try {
/*
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_BEM_PUBLICO)
                    .appendQueryParameter("municipio", municipio)
                    .appendQueryParameter("jurisdicionado", jurisdicionado)
                    .build().toString();
            String jsonStringBemPublicoCollection = getUrlString(url);
*/
            String jsonStringBemPublicoCollection = "{\"benspublicos\":[{\"bempublico\":{\"id\":\"EE11491\",\"area\":\"250\",\"latitude\":\"121345\",\"longitude\":\"458734\",\"tipo\":\"edificacao\",\"nome\":\"escola estadual nossa senhora das gracas\",\"jurisdicionado\":\"SEDUC\",\"endereco\":\"rua 1 numero 35 manaus amazonas\"}},{\"bempublico\":{\"id\":\"PS875\",\"area\":\"1200\",\"latitude\":\"5896\",\"longitude\":\"8426\",\"tipo\":\"ponte\",\"nome\":\"escola estadual santa ana\",\"jurisdicionado\":\"SEDUC\",\"endereco\":\"rua 20 parque dez manaus/AM\"}}]}";
            JSONObject jsonBody = new JSONObject(jsonStringBemPublicoCollection);
            parseBemPublicoCollection(bemPublicoList, jsonBody);
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return bemPublicoList;
    }

    public List<ContextObject> fetchBemPublico(String bpID) {
        List<ContextObject> bemPublicoList = new ArrayList<>();
        try {
/*
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_BEM_PUBLICO)
                    .appendQueryParameter("bpID", bpID)
                    .build().toString();
            String jsonStringBemPublico = getUrlString(url);
*/
            String jsonStringBemPublico = "{\"benspublicos\":[{\"bempublico\":{\"id\":\"EE11491\",\"area\":\"250\",\"latitude\":\"121345\",\"longitude\":\"458734\",\"tipo\":\"edificacao\",\"nome\":\"escola estadual nossa senhora das gracas\",\"jurisdicionado\":\"SEDUC\",\"endereco\":\"rua 1 numero 35 manaus amazonas\"}}]}";
            JSONObject jsonBody = new JSONObject(jsonStringBemPublico);
            parseBemPublicoCollection(bemPublicoList, jsonBody);
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return bemPublicoList;
    }

    private void parseBemPublicoCollection(List<ContextObject> bemPublicoList, JSONObject jsonBody)
            throws JSONException {

            JSONArray bemPublicoJSONArray = jsonBody.getJSONArray("benspublicos");
            for(int i = 0; i < bemPublicoJSONArray.length(); i++) {
                parseBemPublico(bemPublicoList, bemPublicoJSONArray.getJSONObject(i));
            }
    }

    private void parseBemPublico(List<ContextObject> bemPublicoList, JSONObject jsonBody)
            throws JSONException {

        JSONObject bemPublicoJsonObject = jsonBody.getJSONObject("bempublico");
        BemPublico bemPublico = new BemPublico();
        bemPublico.setId(bemPublicoJsonObject.getString("id"));
        bemPublico.setArea(bemPublicoJsonObject.getString("area"));
        bemPublico.setLatitude(bemPublicoJsonObject.getString("latitude"));
        bemPublico.setLongitude(bemPublicoJsonObject.getString("longitude"));
        bemPublico.setTipo(bemPublicoJsonObject.getString("tipo"));
        bemPublico.setNome(bemPublicoJsonObject.getString("nome"));
        bemPublico.setJurisdicionado(bemPublicoJsonObject.getString("jurisdicionado"));
        bemPublico.setEndereco(bemPublicoJsonObject.getString("endereco"));

        bemPublicoList.add(bemPublico);
    }


    /* ************************** CONTRATOS ********************************************************/
    // fetch all CT given a BP.
    public List<ContextObject> fetchContratoCollection(String bemPublico) {
        List<ContextObject> contratoList = new ArrayList<>();

        try {
/*
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_CONTRATO)
                    .appendQueryParameter("bemPublico", bemPublico)
                    .build().toString();
            String jsonStringContratoCollection = getUrlString(url);
*/
            String jsonStringContratoCollection = "{\"contratos\":[{\"contrato\":{\"id\":\"123\",\"numero\":\"456/2018\",\"prazo\":\"360\",\"dataInicio\":\"05/01/2018\",\"bemPublico\":\"PNT321\",\"contratado\":\"OAS Engenharia\"}},{\"contrato\":{\"id\":\"456\",\"numero\":\"13/2007\",\"prazo\":\"180\",\"dataInicio\":\"21/05/2007\",\"bemPublico\":\"EE11491\",\"contratado\":\"Oderbreach Engenharia\"}},{\"contrato\":{\"id\":\"789\",\"numero\":\"1311/2009\",\"prazo\":\"270\",\"dataInicio\":\"12/09/2009\",\"bemPublico\":\"HH42\",\"contratado\":\"Mendes Junior Engenharia\"}},{\"contrato\":{\"id\":\"012\",\"numero\":\"25/2011\",\"prazo\":\"45\",\"dataInicio\":\"31/08/2011\",\"bemPublico\":\"PS875\",\"contratado\":\"Carrane Engenharia\"}}]}";
            JSONObject jsonBody = new JSONObject(jsonStringContratoCollection);
            parseContratoCollection(contratoList, jsonBody);
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return contratoList;
    }

    // fetch a certain CT given BP and CT
    public List<ContextObject> fetchContrato(String bpID, String ctID) {
        List<ContextObject> contratoList = new ArrayList<>();
        try {
/*
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_CONTRATO)
                    .appendQueryParameter("bpID", bpID)
                    .appendQueryParameter("ctID", ctID)
                    .build().toString();
            String jsonStringContrato = getUrlString(url);
*/
            String jsonStringContrato = "{\"contratos\":[{\"contrato\":{\"id\":\"123\",\"numero\":\"456/2018\",\"prazo\":\"360\",\"dataInicio\":\"05/01/2018\",\"bemPublico\":\"PNT321\",\"contratado\":\"OAS Engenharia\"}}]}";
            JSONObject jsonBody = new JSONObject(jsonStringContrato);
            parseContratoCollection(contratoList, jsonBody);
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return contratoList;
    }

    private void parseContratoCollection(List<ContextObject> contratoList, JSONObject jsonBody)
            throws JSONException {

        JSONArray contratoJSONArray = jsonBody.getJSONArray("contratos");
        for(int i = 0; i < contratoJSONArray.length(); i++) {
            parseContrato(contratoList, contratoJSONArray.getJSONObject(i));
        }
    }

    private void parseContrato(List<ContextObject> contratoList, JSONObject jsonBody)
            throws JSONException {
        JSONObject contratoJsonObject = jsonBody.getJSONObject("contrato");
        Contrato contrato = new Contrato();
        contrato.setId(contratoJsonObject.getString("id"));
        contrato.setNumero(contratoJsonObject.getString("numero"));
        contrato.setPrazo(contratoJsonObject.getString("prazo"));
        contrato.setDataInicio(contratoJsonObject.getString("dataInicio"));
        contrato.setBemPublico(contratoJsonObject.getString("bemPublico"));
        contrato.setContratado(contratoJsonObject.getString("contratado"));

        contratoList.add(contrato);
    }

    /* ************************* MEDICAO *********************************/
    public List<ContextObject> fetchMedicao(String bpID, String ctID) {
        List<ContextObject> medicaoList = new ArrayList<>();
        try {
/*
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_MEDICAO)
                    .appendQueryParameter("bpID", bpID)
                    .appendQueryParameter("ctID", ctID)
                    .build().toString();
            String jsonStringMedicao = getUrlString(url);
*/
            String jsonStringMedicao = "{\"medicoes\":[{\"medicao\":{\"id\":\"abc\",\"numero\":\"1\",\"dataInicio\":\"12/06/2009\",\"dataFim\":\"14/12/2009\",\"contratoId\":\"456\"}},{\"medicao\":{\"id\":\"def\",\"numero\":\"2\",\"dataInicio\":\"15/12/2009\",\"dataFim\":\"21/02/2010\",\"contratoId\":\"456\"}}]}";
            JSONObject jsonBody = new JSONObject(jsonStringMedicao);
            parseMedicaoCollection(medicaoList, jsonBody);
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return medicaoList;
    }

    private void parseMedicaoCollection(List<ContextObject> medicaoList, JSONObject jsonBody)
            throws JSONException {

        JSONArray contratoJSONArray = jsonBody.getJSONArray("medicoes");
        for(int i = 0; i < contratoJSONArray.length(); i++) {
            parseMedicao(medicaoList, contratoJSONArray.getJSONObject(i));
        }
    }

    private void parseMedicao (List<ContextObject> medicaoList, JSONObject jsonBody)
        throws JSONException {

        JSONObject medicaoObject = jsonBody.getJSONObject("medicao");
        Medicao medicao = new Medicao();
        medicao.setId(medicaoObject.getString("id"));
        medicao.setNumero(medicaoObject.getString("numero"));
        medicao.setDataInicio(medicaoObject.getString("dataInicio"));
        medicao.setDataFim(medicaoObject.getString("dataFim"));
        medicao.setContratoId(medicaoObject.getString("contratoId"));
        medicaoList.add(medicao);
    }


    /* ********************** GERAL *******************************/
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
