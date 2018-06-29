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
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Medicao;

/**
 * Created by Adriano on 12/03/2018.
 */

public class EContasFetchr {
    private static final String TAG = "EContasFetchr";

    private static final String API_KEY = "XXXXX";
    private static final String FETCH_BEMPUBLICO = "econtas.bempublico";
    private static final String FETCH_CONTRATO = "econtas.contrato";
    private static final String FETCH_MEDICAO = "econtas.medicao";
    private static final Uri ENDPOINT = Uri
            .parse("https://econtas.tce.am.gov.br/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .build();


    /* ********************** BEM PUBLICO *********************************************/
    public List<BemPublico> fetchBensPublicos(String municipio, String jurisdicionado) {
        List<BemPublico> bemPublicoList = new ArrayList<>();

        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_BEMPUBLICO)
                    .appendQueryParameter("municipio", municipio)
                    .appendQueryParameter("jurisdicionado", jurisdicionado)
                    .build().toString();
            // String jsonStringBensPublicos = getUrlString(url);
            String jsonStringBensPublicos = "{\"benspublicos\":[{\"bempublico\":{\"id\":\"EE11491\",\"area\":\"250\",\"latitude\":\"121345\",\"longitude\":\"458734\",\"tipo\":\"edificacao\",\"nome\":\"escola estadual nossa senhora das gracas\",\"jurisdicionado\":\"SEDUC\",\"endereco\":\"rua 1 numero 35 manaus amazonas\",\"contratos\":[{\"contrato\":{\"id\":\"012\",\"numero\":\"25/2011\",\"prazo\":\"45\",\"dataInicio\":\"31/08/2011\",\"bemPublico\":\"PS875\",\"contratado\":\"Carrane Engenharia\"}}]}}]}";
            JSONObject jsonBody = new JSONObject(jsonStringBensPublicos);
            parseBensPublicos(bemPublicoList, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "error");
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return bemPublicoList;
    }

    public List<BemPublico> fetchBemPublico(String bpID) {
        List<BemPublico> bemPublicoList = new ArrayList<>();
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_BEMPUBLICO)
                    .appendQueryParameter("bpID", bpID)
                    .build().toString();
            // String jsonStringBemPublico = getUrlString(url);
            String jsonStringBemPublico = "{\"bempublico\":{\"id\":\"EE11491\",\"area\":\"250\",\"latitude\":\"121345\",\"longitude\":\"458734\",\"tipo\":\"edificacao\",\"nome\":\"escola estadual nossa senhora das gracas\",\"jurisdicionado\":\"SEDUC\",\"endereco\":\"rua 1 numero 35 manaus amazonas\",\"contratos\":[{\"contrato\":{\"id\":\"012\",\"numero\":\"25/2011\",\"prazo\":\"45\",\"dataInicio\":\"31/08/2011\",\"bemPublico\":\"PS875\",\"contratado\":\"Carrane Engenharia\"}}]}}";
            JSONObject jsonBody = new JSONObject(jsonStringBemPublico);
            parseBemPublico(bemPublicoList, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return bemPublicoList;
    }

    private void parseBensPublicos(List<BemPublico> bensPublicosList, JSONObject jsonBody)
            throws IOException, JSONException {

            JSONArray bensPublicosJSONArray = jsonBody.getJSONArray("benspublicos");
            for(int i = 0; i < bensPublicosJSONArray.length(); i++) {
                parseBemPublico(bensPublicosList, bensPublicosJSONArray.getJSONObject(i));
            }
    }

    private void parseBemPublico(List<BemPublico> bensPublicosList, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONObject bempublicoJsonObject = jsonBody.getJSONObject("bempublico");
        BemPublico bemPublico = new BemPublico();
        bemPublico.setId(bempublicoJsonObject.getString("id"));
        bemPublico.setArea(bempublicoJsonObject.getString("area"));
        bemPublico.setLatitude(bempublicoJsonObject.getString("latitude"));
        bemPublico.setLongitude(bempublicoJsonObject.getString("longitude"));
        bemPublico.setTipo(bempublicoJsonObject.getString("tipo"));
        bemPublico.setNome(bempublicoJsonObject.getString("nome"));
        bemPublico.setJurisdicionado(bempublicoJsonObject.getString("jurisdicionado"));
        bemPublico.setEndereco(bempublicoJsonObject.getString("endereco"));
        if(bempublicoJsonObject.has("contratos")) {
            JSONArray contratosJsonArray = bempublicoJsonObject.getJSONArray("contratos");
            for(int i = 0; i < contratosJsonArray.length(); i++) {
                List<Contrato> contratoList = new ArrayList<>();
                parseContrato(contratoList, contratosJsonArray.getJSONObject(i));
                bemPublico.setContratos(contratoList);
            }
        }
        bensPublicosList.add(bemPublico);
    }


    /* ************************** CONTRATOS ********************************************************/
    public List<Contrato> fetchContratos(String municipio, String jurisdicionado, String exercicio) {
        List<Contrato> contratoList = new ArrayList<>();

        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_CONTRATO)
                    .appendQueryParameter("municipio", municipio)
                    .appendQueryParameter("jurisdicionado", jurisdicionado)
                    .appendQueryParameter("exercicio", exercicio)
                    .build().toString();
            // String jsonStringContratos = getUrlString(url);
            String jsonStringContratos = "{\"contratos\":[{\"contrato\":{\"id\":\"123\",\"numero\":\"456/2018\",\"prazo\":\"360\",\"dataInicio\":\"05/01/2018\",\"bemPublico\":\"PNT321\",\"contratado\":\"OAS Engenharia\"}},{\"contrato\":{\"id\":\"456\",\"numero\":\"13/2007\",\"prazo\":\"180\",\"dataInicio\":\"21/05/2007\",\"bemPublico\":\"EE11491\",\"contratado\":\"Oderbreach Engenharia\",\"medicoes\":[{\"id\":\"12\",\"numero\":\"1234\",\"dataInicio\":\"12/08/2009\",\"dataFim\":\"14/12/2009\",\"contratoId\":\"123\"}]}},{\"contrato\":{\"id\":\"789\",\"numero\":\"458/2009\",\"prazo\":\"270\",\"dataInicio\":\"12/09/2009\",\"bemPublico\":\"HH42\",\"contratado\":\"Mendes Junior Engenharia\"}},{\"contrato\":{\"id\":\"012\",\"numero\":\"25/2011\",\"prazo\":\"45\",\"dataInicio\":\"31/08/2011\",\"bemPublico\":\"PS875\",\"contratado\":\"Carrane Engenharia\"}}]}";
            JSONObject jsonBody = new JSONObject(jsonStringContratos);
            parseContratos(contratoList, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "error");
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return contratoList;
    }

    public List<Contrato> fetchContrato(String bpID, String ctID) {
        List<Contrato> contratoList = new ArrayList<>();
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_CONTRATO)
                    .appendQueryParameter("bpID", bpID)
                    .appendQueryParameter("ctID", ctID)
                    .build().toString();
            // String jsonStringContrato = getUrlString(url);
            String jsonStringContrato = "{\"contrato\":{\"id\":\"456\",\"numero\":\"13/2007\",\"prazo\":\"180\",\"dataInicio\":\"21/05/2007\",\"bemPublico\":\"EE11491\",\"contratado\":\"Oderbreach Engenharia\",\"medicoes\":[{\"id\":\"12\",\"numero\":\"1234\",\"dataInicio\":\"12/08/2009\",\"dataFim\":\"14/12/2009\",\"contratoId\":\"123\"}]}}";
            JSONObject jsonBody = new JSONObject(jsonStringContrato);
            parseContrato(contratoList, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return contratoList;
    }

    private void parseContratos(List<Contrato> contratoList, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray contratoJSONArray = jsonBody.getJSONArray("contratos");
        for(int i = 0; i < contratoJSONArray.length(); i++) {
            parseContrato(contratoList, contratoJSONArray.getJSONObject(i));
        }
    }

    private void parseContrato(List<Contrato> contratoList, JSONObject jsonBody)
            throws IOException, JSONException {
        JSONObject contratoJsonObject = jsonBody.getJSONObject("contrato");
        Contrato contrato = new Contrato();
        contrato.setId(contratoJsonObject.getString("id"));
        contrato.setNumero(contratoJsonObject.getString("numero"));
        contrato.setPrazo(contratoJsonObject.getString("prazo"));
        contrato.setDataInicio(contratoJsonObject.getString("dataInicio"));
        contrato.setBemPublico(contratoJsonObject.getString("bemPublico"));
        contrato.setContratado(contratoJsonObject.getString("contratado"));
        if(contratoJsonObject.has("medicoes")) {
            JSONArray medicaoJsonArray = contratoJsonObject.getJSONArray("medicoes");
            for(int i = 0; i < medicaoJsonArray.length(); i++) {
                JSONObject medicaoObject = medicaoJsonArray.getJSONObject(i);
                Medicao medicao = new Medicao();
                medicao.setId(medicaoObject.getString("id"));
                medicao.setNumero(medicaoObject.getString("numero"));
                medicao.setDataInicio(medicaoObject.getString("dataInicio"));
                medicao.setDataFim(medicaoObject.getString("dataFim"));
                medicao.setContratoId(medicaoObject.getString("contratoId"));
                contrato.getMedicaoLista().add(medicao);
            }
        }
        contratoList.add(contrato);
    }

    /* ************************* MEDICAO *********************************/
    public List<Medicao> fetchMedicao(String bpID, String ctID, String mdID) {
        List<Medicao> medicaoList = new ArrayList<>();
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_MEDICAO)
                    .appendQueryParameter("bpID", bpID)
                    .appendQueryParameter("ctID", ctID)
                    .appendQueryParameter("mdID", mdID)
                    .build().toString();
            // String jsonStringMedicao = getUrlString(url);
            String jsonStringMedicao = "{\"medicao\":{\"id\":\"abc\",\"numero\":\"1\",\"dataInicio\":\"12/06/2009\",\"dataFim\":\"14/12/2009\",\"contratoId\":\"456\"}}";
            JSONObject jsonBody = new JSONObject(jsonStringMedicao);
            parseMedicao(medicaoList, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return medicaoList;
    }

    private void parseMedicao (List<Medicao> medicaoList, JSONObject jsonBody)
        throws IOException, JSONException {
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
