package net.ddns.hyena.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import net.ddns.hyena.papaya.util.AES256Util;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static net.ddns.hyena.papaya.common.SERVER;
import static net.ddns.hyena.papaya.common.USER_INFO;

public class LoginActivity extends AppCompatActivity {

    ImageView backButton;
    Button loginButton;
    EditText et_userId;
    EditText et_passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginButton = findViewById(R.id.loginBtn);
        et_userId = findViewById(R.id.et_userId);
        et_passwd = findViewById(R.id.et_passwd);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = et_userId.getText().toString();
                String passwd = et_passwd.getText().toString();
                String encPasswd = "";

                try {
                    encPasswd = AES256Util.getSHA256(passwd);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                if("".equals(userId) || "".equals(passwd)) {
                    return;
                }
                // LoginInfo Setting
                USER_INFO.setUserId(userId);
                USER_INFO.setPassword(encPasswd);
                USER_INFO.setLang("ko");

                if(connectionUrl(USER_INFO)) {
                    Intent intent = new Intent(LoginActivity.this, ResultActivity.class);
                    startActivity(intent);
                }
                else {
                    //et_userId.setText("");
                    et_passwd.setText("");
                    et_passwd.setFocusable(true);
                }
            }
        });
    }

    private boolean connectionUrl(UserInfo user) {
        // URL Parameter
        Map<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("password", user.getPassword());
        params.put("lang", user.getLang());

        // URL 설정.
        String url = SERVER + "/admin/xa/session/login";
        String result = "";

        // AsyncTask를 통해 HttpUTLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, "POST", params, LoginActivity.this);
        try {
            result = networkTask.execute().get();

            if(result.isEmpty())
                return false;

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

}
