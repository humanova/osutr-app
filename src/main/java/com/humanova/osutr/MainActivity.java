package com.humanova.osutr;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ListView messageList;
    private ArrayList<String> messages;
    private ArrayAdapter<String> adapter;
    private int lineIndex = 0;
    private boolean init = false;

    private static String apiURL = "http://173.249.51.133:5000/osuTR-API/v1/get_messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageList = findViewById(R.id.message_list);

        messages = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, messages)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView ListItemShow = (TextView) v.findViewById(android.R.id.text1);

                v.setBackgroundColor(0xFF212121);
                ListItemShow.setTextColor(0xFFe5e5e5);
                TextView textView = ((TextView) v.findViewById(android.R.id.text1));

                textView.setMaxHeight(60); // Min Height
                return v;
            }
        };

        messageList.setAdapter(adapter);
        Button refreshButton = (Button) findViewById(R.id.refresh_btn);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChat();
                messageList.setSelection(messages.size());
            }
        });

        chatLoop();

    }

    public void chatLoop()
    {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateChat();
            }
        }, 0, 3000);
    }

    public void updateChat()
    {
        Request(lineIndex, this);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("messages");
                    lineIndex = response.getInt("lineIndex");
                    for (int i = 0; i < dataArray.length(); i++) {
                        adapter.add(dataArray.getString(i));
                    }
                    if (!init)
                    {
                        messageList.setSelection(messages.size());
                        init = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }


    private void Request(int currLineIndex, Context context){

        final String lineIndex = String.valueOf(currLineIndex);

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = null;
        try {
            params = new JSONObject().put("lineIndex", String.valueOf(currLineIndex));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                apiURL,
                params,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

        queue.add(myReq);

    }

}
