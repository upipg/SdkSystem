package com.app.sdkupiiii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class CT extends AppCompatActivity {
    int flag;
    GifImageView correct,wrong;
    TextView txn_id,amount,merchant_name,brand_name,mobile,response,date,desc;
    String Transaction_id,strAmount,strmerchnat_name,strbrandname,strdate,strmobile;
    TextView done;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_transcation);

        correct= findViewById(R.id.correct);
        wrong= findViewById(R.id.wrong);
        date= findViewById(R.id.date);

        amount= findViewById(R.id.amount);
        txn_id= findViewById(R.id.txn_id);
        response= findViewById(R.id.response);
        merchant_name= findViewById(R.id.merchant_name);
        brand_name= findViewById(R.id.brand_name);
        mobile= findViewById(R.id.mobile);
        done= findViewById(R.id.done);
        desc= findViewById(R.id.desc);


        // Get the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        strdate = sdf.format(new Date());


        Intent i = getIntent();
        flag= i.getExtras().getInt("flag");
        if (i.getExtras().getString("txnid").isEmpty()||i.getExtras().getString("txnid").equals(null)) {
            Transaction_id = i.getExtras().getString("ref_t_id");
        }else {
            Transaction_id = i.getExtras().getString("txnid");
        }
        strAmount= i.getExtras().getString("trAm");
        strmerchnat_name= i.getExtras().getString("merchant_name");
        strbrandname= i.getExtras().getString("BRname");
        strmobile= i.getExtras().getString("mobile");

        if (flag==1){
            response.setText("Payment Success");
            correct.setVisibility(View.VISIBLE);
            wrong.setVisibility(View.GONE);
        }else{
            response.setText("Payment Failed");
            correct.setVisibility(View.GONE);
            wrong.setVisibility(View.VISIBLE);
        }

        mobile.setText(strmobile);
        brand_name.setText(strbrandname);
        merchant_name.setText(strmerchnat_name);
        amount.setText("₹ "+strAmount);

        date.setText(strdate);
        desc.setText("Thanks for Choosing "+strbrandname);

        txn_id.setText(Transaction_id);

        done.setOnClickListener(v -> {
            finish();
        });

    }


}
