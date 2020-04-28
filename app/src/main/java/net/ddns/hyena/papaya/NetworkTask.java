package net.ddns.hyena.papaya;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static net.ddns.hyena.papaya.common.USER_INFO;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private String method;
    private Map<String, String> values;
    private Context context;

    public NetworkTask(String url, String method, Map<String, String> values, Context context) {
        this.url = url;
        this.method = method;
        this.values = values;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Log.i("CSJANG: onPreExecute", "Start...");
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";

        try {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection(context);
            result = requestHttpURLConnection.request(url, method, values);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(result == null)
            return;

        // 결과처리(sessionId만 처리)
        Map<String, String> myMap = new HashMap<>();
        try {
            JSONObject json = new JSONObject(result);

            if(json.has("sessionId")) {
                Iterator i = json.keys();
                while (i.hasNext()) {
                    String k = i.next().toString();
                    myMap.put(k, json.getString(k));
                }
                // LoginInfo Setting
                USER_INFO.setSessionId(myMap.get("sessionId"));
                USER_INFO.setTempPass(Boolean.parseBoolean(myMap.get("tempPass")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

