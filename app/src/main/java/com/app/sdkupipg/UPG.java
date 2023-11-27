package com.app.sdkupipg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.app.sdkupipg.Models.PG_Response_REQ;
import com.app.sdkupipg.Models.PG_Response_RES;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UPG extends AppCompatActivity {
    String oid,strmo;
    private final Gson gson = new Gson();
    private SuccessHandler successHandler;
    private SuccessHandler failureHandler;
    private String FAILURE = "FAILURE";

    private static final int REQUEST_CODE_PHONEPE = 1002;
    private static final int REQUEST_CODE_PAYTM = 1003;
    private static final int REQUEST_CODE_GOOGLE_PAY = 1001;
    private static final int REQUEST_CODE_UPI_PAYMENT = 0;
    LinearLayout PGLayout;
    TextView ip_add,m_n,brand_name,time;
    LinearLayout creditCardLayout,debitCardLayout,netbanking,phonepe,gpay,paytm,otherupi;
    String strm_s,strm_r_id,strAmount,strUpiId,strTransactionId,strNotes,strCurrency,str_m_n,strmerchnat_id,strbrandname,formattedDate;
    ImageView app_logo;
    String which_upi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creditCardLayout=findViewById(R.id.creditCardLayout);
        debitCardLayout=findViewById(R.id.debitCardLayout);
        netbanking=findViewById(R.id.netbanking);
        phonepe=findViewById(R.id.phonepe);
        gpay=findViewById(R.id.gpay);
        PGLayout=findViewById(R.id.PGLayout);
        paytm=findViewById(R.id.paytm);
        otherupi=findViewById(R.id.otherupi);
        app_logo=findViewById(R.id.app_logo);
        ip_add = findViewById(R.id.ipaddress);
        time = findViewById(R.id.time);
        brand_name = findViewById(R.id.brand_name);
        m_n = findViewById(R.id.merchant_name);

        Intent i = getIntent();
        strAmount = i.getExtras().getString("trAm");
        strUpiId = i.getExtras().getString("trUpiId");
        strTransactionId = i.getExtras().getString("trId");
        strNotes = i.getExtras().getString("trNotes");
        strCurrency = i.getExtras().getString("trcur");
        strbrandname = i.getExtras().getString("BRname");
        str_m_n = i.getExtras().getString("merchant_name");
        strmerchnat_id = i.getExtras().getString("merchant_id");
        strmo = i.getExtras().getString("mobile");

        if (i.getExtras().getString("merchant_ref_id") != null) {
            strm_r_id = i.getExtras().getString("merchant_ref_id");
        }
        if (i.getExtras().getString("merchant_secret") != null) {
            strm_s = i.getExtras().getString("merchant_secret");
        }
        if (i.getExtras().getString("trOrId") != null) {
            oid = i.getExtras().getString("trOrId");
        }



        ip_add.setText("IP Address : "+i.getExtras().getString("ip_address"));

        time.setText("Accesss Time : "+ i.getExtras().getString("date_time"));
        brand_name.setText("Brand Name : "+ i.getExtras().getString("BRname"));
        m_n.setText(i.getExtras().getString("merchant_name"));


        phonepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_upi= "PHONEPE";
                payUsingPhonepeUpi(strbrandname, strUpiId, strNotes, strAmount,strCurrency);

            }
        });

        gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_upi= "GPAY";
                payUsingUpi(strbrandname, strUpiId, strNotes, strAmount,strCurrency);

            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_upi= "PAYTM";
                // Create an Intent with the ACTION_VIEW action and the Paytm URI
                Uri paytmUri = Uri.parse("upi://pay")
                        .buildUpon()
                        .appendQueryParameter("pa", strUpiId.trim()) // Payee VPA (Virtual Payment Address)
                        .appendQueryParameter("pn", strbrandname.trim()) // Payee name
                        .appendQueryParameter("mc", "000000") // Merchant code (optional)
                        .appendQueryParameter("tr", oid) // Transaction ID
                        .appendQueryParameter("tn", strNotes.trim()) // Transaction note
                        .appendQueryParameter("am", strAmount.trim()) // Transaction amount
                        .appendQueryParameter("mode", "04".trim()) // Payment mode (optional)
                        .appendQueryParameter("tid", strTransactionId) // Terminal ID (optional)
                        .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(paytmUri);
                intent.setPackage("net.one97.paytm");
                // Check if the user has the Paytm app installed
                try {
                    startActivityForResult(intent, REQUEST_CODE_PAYTM);
                } catch (Exception e) {

                    Toast.makeText(UPG.this, "PayTm app is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        otherupi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_upi= "OTHER UPI";
                payUsingOtherUpi(strbrandname, strUpiId, strNotes, strAmount,strCurrency);

            }
        });

    }

    void payUsingUpi(String name, String upiId, String note, String amount,String strCurrency) {
        paytm.setFocusable(false);
        gpay.setFocusable(false);
        phonepe.setFocusable(false);
        otherupi.setFocusable(false);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", strCurrency)
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        upiPayIntent.setPackage("com.google.android.apps.nbu.paisa.user");
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, REQUEST_CODE_GOOGLE_PAY);
        } else {
            Toast.makeText(this, "Google Pay app is not installed", Toast.LENGTH_SHORT).show();
        }


    }

    void payUsingPhonepeUpi(String name, String upiId, String note, String amount,String strCurrency) {
        paytm.setFocusable(false);
        gpay.setFocusable(false);
        phonepe.setFocusable(false);
        otherupi.setFocusable(false);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", strCurrency)
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        upiPayIntent.setPackage("com.phonepe.app");

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, REQUEST_CODE_PHONEPE);
        } else {
            Toast.makeText(this, "No UPI app Found", Toast.LENGTH_SHORT).show();
        }
    }

    void payUsingOtherUpi(String name, String upiId, String note, String amount,String strCurrency) {
        paytm.setFocusable(false);
        gpay.setFocusable(false);
        phonepe.setFocusable(false);
        otherupi.setFocusable(false);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", strCurrency)
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, REQUEST_CODE_UPI_PAYMENT);
        } else {
            Toast.makeText(this, "No UPI app Found", Toast.LENGTH_SHORT).show();
        }
    }
    //charges


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final JSONObject responseData = new JSONObject();

        try {
            if (requestCode == REQUEST_CODE_GOOGLE_PAY) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String status = data.getStringExtra("Status").trim();
                        String responseMessage = bundle.getString("response");

                        responseData.put("status", status);
                        responseData.put("message", responseMessage);

                        TransactionResult transactionResult = new TransactionResult(status, responseMessage);

                        if ("SUCCESS".equals(status)) {
                            collectData(transactionResult);

                        } else {
                            collectData(transactionResult);

                        }
                    }
                }
            } else {
                responseData.put("message", "Request Code Mismatch");
                responseData.put("status", FAILURE);
                if (failureHandler != null) {
                    failureHandler.invoke(gson.toJson(responseData));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == REQUEST_CODE_PHONEPE) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String status = data.getStringExtra("Status").trim();
                        String responseMessage = bundle.getString("response");

                        responseData.put("status", status);
                        responseData.put("message", responseMessage);

                        TransactionResult transactionResult = new TransactionResult(status, responseMessage);

                        if ("SUCCESS".equals(status)) {
                            collectData(transactionResult);

                        } else {
                            collectData(transactionResult);

                        }
                    }
                }
            } else {
                responseData.put("message", "Request Code Mismatch");
                responseData.put("status", FAILURE);
                if (failureHandler != null) {
                    failureHandler.invoke(gson.toJson(responseData));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == REQUEST_CODE_PAYTM) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String status = data.getStringExtra("Status").trim();
                        String responseMessage = bundle.getString("response");

                        responseData.put("status", status);
                        responseData.put("message", responseMessage);

                        TransactionResult transactionResult = new TransactionResult(status, responseMessage);

                        if ("SUCCESS".equals(status)) {
                            collectData(transactionResult);

                        } else {
                            collectData(transactionResult);

                        }
                    }
                }
            } else {
                responseData.put("message", "Request Code Mismatch");
                responseData.put("status", FAILURE);
                if (failureHandler != null) {
                    failureHandler.invoke(gson.toJson(responseData));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == REQUEST_CODE_UPI_PAYMENT) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String status = data.getStringExtra("Status");
                        String responseMessage = bundle.getString("response");

                        // Check if status is not null before calling trim()
                        if (status != null) {
                            status = status.trim();
                        }

                        responseData.put("status", status);
                        responseData.put("message", responseMessage);

                        TransactionResult transactionResult = new TransactionResult(status, responseMessage);

                        if ("SUCCESS".equals(status)) {
                            collectData(transactionResult);
                        } else {
                            collectData(transactionResult);
                        }
                    }
                }
            } else {
                responseData.put("message", "Request Code Mismatch");
                responseData.put("status", FAILURE);
                if (failureHandler != null) {
                    failureHandler.invoke(gson.toJson(responseData));


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void collectData(TransactionResult transactionResult) {
        // Add your data collection logic here

        String responseString = "txnId=&responseCode=00&ApprovalRefNo=332530&Status=SUCCESS&txnRef=";
        String[] keyValuePairs = responseString.split("&");

        Map<String, String> responseMap = new HashMap<>();

        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            if (entry.length == 2) {
                responseMap.put(entry[0], entry[1]);
            }
        }

        String responseCode = responseMap.get("responseCode");
        String approvalRefNo = responseMap.get("ApprovalRefNo");
        String status1 = responseMap.get("Status");
        String txnId = responseMap.get("txnId");
        String txnRef = responseMap.get("txnRef");


        if (transactionResult.getStatus() == null || transactionResult.getStatus().isEmpty()) {
            Response(status1, transactionResult.getMessage());
        } else {
            Response(transactionResult.getStatus(), transactionResult.getMessage());
        }

    }

    public interface SuccessHandler {
        void invoke(String jsonData);
    }

    private static class TransactionResult {
        private String status;
        private String message;

        public TransactionResult(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }


    private void Response(String MerchantTransactionStatus,String StrResponse){

        PG_Response_REQ PG_REQ= new PG_Response_REQ();
        PG_REQ.setMerchantId(strmerchnat_id);
        PG_REQ.setMerchantRefId(strTransactionId);
        PG_REQ.setMerchantSecret(strm_s);
        PG_REQ.setMerchantUserName(str_m_n);
        PG_REQ.setMerchantTransactionStatus(MerchantTransactionStatus);
        PG_REQ.setResponse(StrResponse);
        PG_REQ.setProvider(which_upi);
        //set data req

        AI aint= AC.PB().create(AI.class);
        Call<PG_Response_RES> apicall= aint.Transaction_response(PG_REQ);
        apicall.enqueue(new Callback<PG_Response_RES>() {
            @Override
            public void onResponse(Call<PG_Response_RES> call, Response<PG_Response_RES> response) {

                try {


                    if (response.body().getStatus().equals("00")) {


                            if (response.body().getResponse().getStatus().equals("Success")||response.body().getResponse().getStatus().equals("SUCCESS")){

                                Intent i = new Intent(UPG.this, CT.class);
                                i.putExtra("txnid", response.body().getResponse().getTxnId());
                                i.putExtra("ref_t_id", response.body().getResponse().getApprovalRefNo());
                                i.putExtra("flag", 1);
                                i.putExtra("trAm", strAmount);
                                i.putExtra("merchant_name", str_m_n);
                                i.putExtra("BRname", strbrandname);
                                i.putExtra("mobile", strmo);
                                startActivity(i);
                                finish();
                            }else{

                                Intent i = new Intent(UPG.this, CT.class);
                                i.putExtra("txnid", response.body().getResponse().getTxnId());
                                i.putExtra("flag", 2);
                                i.putExtra("ref_t_id", response.body().getResponse().getApprovalRefNo());
                                i.putExtra("trAm", strAmount);
                                i.putExtra("merchant_name", str_m_n);
                                i.putExtra("BRname", strbrandname);
                                i.putExtra("mobile", strmo);
                                startActivity(i);
                                finish();
                            }


                    } else {


                    }

                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<PG_Response_RES> call, Throwable t) {
               // Toast.makeText(UpiPgActivity.this, "onFailure.....", Toast.LENGTH_SHORT).show();
            }
        });
    }

}