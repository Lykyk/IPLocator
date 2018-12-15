package com.example.lykyk.iplocator;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.guide.GetExample;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                new Thread(networkTask).start();
            }
        });

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
            tv = findViewById(R.id.tv);
            tv.setText(val);
        }
    };


    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            GetExample example = new GetExample();
            String response = "????";
            try {
                response = example.run("http://www.ip-api.com/json");
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jsonObject = JSONObject.parseObject(response);
            Message msg = new Message();
            Bundle data = new Bundle();

            String showText = jsonObject.getString("query") +
                    "\n" +
                    jsonObject.getString("city");

            data.putString("value", showText);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
}
