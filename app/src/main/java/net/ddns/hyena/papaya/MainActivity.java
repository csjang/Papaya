package net.ddns.hyena.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.ddns.hyena.papaya.util.NetworkStatus;
import net.ddns.hyena.papaya.util.ServerStatus;
import static net.ddns.hyena.papaya.common.SERVER_INFORMATION;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginBtn);
        textView = findViewById((R.id.textNotice));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE){
                    textView.setText("Connected by LTE(3G)");
                }else if (status == NetworkStatus.TYPE_WIFI){
                    textView.setText("Connected by WIFI...");
                }else {
                    textView.setText("The network is not connected...");
                    return;
                }

                if(!isOnline()) {
                    textView.setText("Server connection failed....");
                    return;
                }

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public static boolean isOnline() {
        ServerStatus status = new ServerStatus(SERVER_INFORMATION);
        status.start();

        try{
            status.join();
            return status.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}