package net.ddns.hyena.papaya;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static net.ddns.hyena.papaya.common.USER_INFO;

public class RequestHttpURLConnection {

    Context context;

    // HttpURLConnection 참조 변수
    HttpURLConnection conn = null;

    public RequestHttpURLConnection(Context context) {
        this.context = context;
    }

    public String request(String _url, String method, Map<String, String> _params) {

        try {
            URL url = new URL(_url);
            conn = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정.
            conn.setRequestMethod(method);

            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            conn.setRequestProperty("Cookie", "loginId=" + USER_INFO.getUserId());
            conn.setRequestProperty("Session", USER_INFO.getSessionId());
            conn.setRequestProperty("AP-LANG", USER_INFO.getLang());

            conn.setDoInput(true);

            // [2-2]. parameter -> JSON 변환.
            if(!method.equals("GET")) {
                JSONObject jsonObject = new JSONObject(_params);

                OutputStream outStream = conn.getOutputStream();
                outStream.write(jsonObject.toString().getBytes());
                outStream.flush();
                outStream.close();
            }

            // [2-3]. 연결 확인.
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return "";

            // [2-4]. 결과 리턴.
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            String page = "";
            while((line = reader.readLine()) != null) {
                page += line;
            }
            return page;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }

        return null;
    }

}
