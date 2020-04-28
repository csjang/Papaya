package net.ddns.hyena.papaya;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static net.ddns.hyena.papaya.common.SERVER;

public class ResultActivity extends AppCompatActivity {

    ImageView backButton;

    Button resultButton;
    TextView resultTv;

    Button nextButton;
    TextView nextKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        backButton = findViewById(R.id.backBtn);
        resultButton = findViewById(R.id.resultBtn);
        resultTv = findViewById(R.id.resultTv);
        nextButton = findViewById(R.id.nextBtn);
        nextKey = findViewById(R.id.nextKey);

        // set Scrolling
        resultTv.setMovementMethod(new ScrollingMovementMethod());
        // nextButton Disable
        nextButton.setVisibility(View.INVISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 초기화
                resultTv.setText(null);
                sendUrl("");
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 초기화
                sendUrl(nextKey.getText().toString());
            }
        });


    }

    private void sendUrl(String next) {
        // URL 설정.
        String url = SERVER + "/back/ba/symbol/lookupsymbolinfo?all=NO" + "&nextKey=" + next;
        String result = "";

        // AsyncTask를 통해 HttpUTLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, "GET", null, ResultActivity.this);
        try {
            result = networkTask.execute().get();

            if(result == null)
                return;

            String strResult = resultTv.getText().toString() + result;
            resultTv.setText(strResult);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // 결과처리(next 처리)
        try {
            JSONObject json = new JSONObject(result);
            String strNextKey = "";
            boolean continued = false;

            if(json.has("nextKey")) {
                strNextKey = json.getString("nextKey");
                continued = json.getBoolean("continued");
            }

            // nextButton 셋팅
            if(continued)
                nextButton.setVisibility(View.VISIBLE);
            else
                nextButton.setVisibility(View.INVISIBLE);

            // nextKey 셋팅
            nextKey.setText(strNextKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
