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
import br.gov.am.tce.auditor.domain.Contrato;
import br.gov.am.tce.auditor.domain.Medicao;

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

        String argumentStr = null;
        if(municipio != null) {
            argumentStr += municipio;
            if(jurisdicionado != null) {
                argumentStr += "&" + jurisdicionado;
            }
        } else {
            if(jurisdicionado != null) {
                argumentStr += jurisdicionado;
            }
        }

        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_BEMPUBLICO)
                    .appendQueryParameter("arguments", argumentStr)
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

    public BemPublico fetchBemPublico(String bemPublico_str) {
        BemPublico bemPublico = null;
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_BEMPUBLICO)
                    .appendQueryParameter("arguments", bemPublico_str)
                    .build().toString();
            // String jsonStringBemPublico = getUrlString(url);
            String jsonStringBemPublico = "{\"bempublico\":{\"id\":\"EE11491\",\"area\":\"250\",\"latitude\":\"121345\",\"longitude\":\"458734\",\"tipo\":\"edificacao\",\"nome\":\"escola estadual nossa senhora das gracas\",\"jurisdicionado\":\"SEDUC\",\"endereco\":\"rua 1 numero 35 manaus amazonas\",\"contratos\":[{\"contrato\":{\"id\":\"012\",\"numero\":\"25/2011\",\"prazo\":\"45\",\"dataInicio\":\"31/08/2011\",\"bemPublico\":\"PS875\",\"contratado\":\"Carrane Engenharia\"}}]}}";
            JSONObject jsonBody = new JSONObject(jsonStringBemPublico);
            bemPublico =  parseBemPublico(jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return bemPublico;
    }

    private void parseBensPublicos(List<BemPublico> bensPublicosList, JSONObject jsonBody)
            throws IOException, JSONException {

            JSONArray bensPublicosJSONArray = jsonBody.getJSONArray("benspublicos");
            for(int i = 0; i < bensPublicosJSONArray.length(); i++) {
                bensPublicosList.add(parseBemPublico(bensPublicosJSONArray.getJSONObject(i)));
            }
    }

    private BemPublico parseBemPublico(JSONObject jsonBody)
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
                bemPublico.getContratos().add(parseContrato(contratosJsonArray.getJSONObject(i)));
            }
        }
        return bemPublico;
    }


    /* ************************** CONTRATOS ********************************************************/
    public List<Contrato> fetchContratos(String municipio, String jurisdicionado, String exercicio) {
        List<Contrato> contratoList = new ArrayList<>();

        String argumentStr = null;
        if(municipio != null) {
            argumentStr += municipio;
            if(jurisdicionado != null) argumentStr += "&" + jurisdicionado;
            if(exercicio != null) argumentStr += "&" + exercicio;
        } else {
            if(jurisdicionado != null) {
                argumentStr += jurisdicionado;
                if(exercicio != null) argumentStr += "&" + exercicio;
            }
            argumentStr += exercicio;
        }

        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_CONTRATO)
                    .appendQueryParameter("arguments", argumentStr)
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

    public Contrato fetchContrato(String contractId) {
        Contrato contrato = null;
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_CONTRATO)
                    .appendQueryParameter("arguments", contractId)
                    .build().toString();
            // String jsonStringContrato = getUrlString(url);
            String jsonStringContrato = "{\"contrato\":{\"id\":\"456\",\"numero\":\"13/2007\",\"prazo\":\"180\",\"dataInicio\":\"21/05/2007\",\"bemPublico\":\"EE11491\",\"contratado\":\"Oderbreach Engenharia\",\"medicao\":[{\"id\":\"12\",\"numero\":\"1234\",\"dataInicio\":\"12/08/2009\",\"dataFim\":\"14/12/2009\",\"contratoId\":\"123\"}]}}";
            JSONObject jsonBody = new JSONObject(jsonStringContrato);
            contrato = parseContrato(jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return contrato;
    }

    private void parseContratos(List<Contrato> contratoList, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray contratoJSONArray = jsonBody.getJSONArray("contratos");
        for(int i = 0; i < contratoJSONArray.length(); i++) {
            contratoList.add(parseContrato(contratoJSONArray.getJSONObject(i)));
        }
    }

    private Contrato parseContrato(JSONObject jsonBody)
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
        return contrato;
    }

    /* ************************* MEDICAO *********************************/
    public Medicao fetchMedicao(String medicao_str) {
        Medicao medicao = null;
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_MEDICAO)
                    .appendQueryParameter("arguments", medicao_str)
                    .build().toString();
            // String jsonStringMedicao = getUrlString(url);
            String jsonStringMedicao = "{\"medicao\":{\"id\":\"abc\",\"numero\":\"1\",\"dataInicio\":\"12/06/2009\",\"dataFim\":\"14/12/2009\",\"contratoId\":\"456\"}}";
            JSONObject jsonBody = new JSONObject(jsonStringMedicao);
            medicao = parseMedicao(jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return medicao;
    }

    private Medicao parseMedicao (JSONObject jsonBody)
        throws IOException, JSONException {
        JSONObject medicaoObject = jsonBody.getJSONObject("medicao");
        Medicao medicao = new Medicao();
        medicao.setId(medicaoObject.getString("id"));
        medicao.setNumero(medicaoObject.getString("numero"));
        medicao.setDataInicio(medicaoObject.getString("dataInicio"));
        medicao.setDataFim(medicaoObject.getString("dataFim"));
        medicao.setContratoId(medicaoObject.getString("contratoId"));

        return medicao;
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
