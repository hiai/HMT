package cn.edu.scau.hometown.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;


import cn.edu.scau.hometown.R;



public class SecondHandMarketSearchActivity extends ActionBarActivity {
   private EditText et_search;
    private Button btn_search;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_hand_market_search);

        requestQueue = Volley.newRequestQueue(SecondHandMarketSearchActivity.this);

        et_search = (EditText) findViewById(R.id.et_search);
        btn_search = (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goodName = et_search.getText().toString();
                if(goodName.equals("")){
                    Toast.makeText(getApplicationContext(),"输入为空",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(SecondHandMarketSearchActivity.this, SecondHandMarketSearchResultActivity.class);
                    intent.putExtra("keyword", goodName);
                    startActivity(intent);
                }
            }
        });

    }


}
