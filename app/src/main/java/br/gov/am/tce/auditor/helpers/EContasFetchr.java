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

import br.gov.am.tce.auditor.domain.Commonweal;
import br.gov.am.tce.auditor.domain.Contract;
import br.gov.am.tce.auditor.domain.Metering;

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

    public List<Contract> fetchContracts(String year, String county, String owner) {
        List<Contract> contractList = new ArrayList<>();

        String argumentStr = null;
        if(year != null) {
            argumentStr += year;
            if(county != null) argumentStr += "&" + county;
            if(owner != null) argumentStr += "&" + owner;
        } else {
            if(county != null) {
                argumentStr += county;
                if(owner != null) argumentStr += "&" + owner;
            }
            argumentStr += owner;
        }

        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_CONTRACTS)
                    .appendQueryParameter("arguments", argumentStr)
                    .build().toString();
            String jsonStringContracts = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonStringContracts);
            parseContracts(contractList, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "error");
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
        return null;
    }

    public void fetchCommonweal(String commonweal) {
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_BEMPUBLICO)
                    .appendQueryParameter("arguments", commonweal)
                    .build().toString();
            String jsonStringCommonweal = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonStringCommonweal);
            parseCommonweal(jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.e(TAG, jse.getMessage());
        }
    }

    public void fetchMetering(String metering) {
        try {
            String url = ENDPOINT.buildUpon()
                    .appendQueryParameter("method", FETCH_MEDICAO)
                    .appendQueryParameter("arguments", metering)
                    .build().toString();
            String jsonStringMetering = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonStringMetering);
            parseMetering(jsonBody);
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
            contract.setChave("chave");
            contract.setStatus("status");

            contractList.add(contract);
        }
    }

    private Commonweal parseCommonweal(JSONObject jsonBody)
        throws IOException, JSONException {

        Commonweal commonweal = new Commonweal();
        commonweal.setChave("chave");
        commonweal.setNome("nome");

        return commonweal;
    }

    private Metering parseMetering (JSONObject jsonBody)
        throws IOException, JSONException {

        Metering metering = new Metering();
        metering.setId("id");
        metering.setName("nome");

        return metering;
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
