package net.ddns.hyena.papaya.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class ServerStatus extends Thread {

    private boolean success;
    private String host;

    public ServerStatus(String host){
        this.host = host;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection)new URL(host).openConnection();
            conn.setRequestProperty("User-Agent","Android");
            conn.setConnectTimeout(1000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode == 200) success = true;
            else success = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        if(conn != null){
            conn.disconnect();
        }
    }

    public boolean isSuccess(){
        return success;
    }

}
