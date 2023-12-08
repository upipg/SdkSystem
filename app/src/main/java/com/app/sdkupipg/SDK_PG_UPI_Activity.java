package com.app.sdkupipg;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.app.sdkupipg.Models.PG_Collect_REQUEST;
import com.app.sdkupipg.Models.PG_Collect_RES;
import com.app.sdkupipg.Models.PG_Response_REQ;
import com.app.sdkupipg.Models.PG_Response_RES;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SDK_PG_UPI_Activity extends AppCompatActivity {
    String strtTid,strorderid;
    double latitude;
    double longitude;
    float pecentge = 18;
    private static final int REQUEST_CODE_UPI_PAYMENT = 0;


    String ipAddress, deviceId;

    private static final int PERMISSION_REQUEST_CODE = 1;
    TextView b_n,merchant_name,txtUserAmount;
    EditText edit_m;
    TextView go;
    private final Gson gson = new Gson();

    private UPG.SuccessHandler successHandler;
    private UPG.SuccessHandler failureHandler;
    private String FAILURE = "FAILURE";

    ProgressBar progress;
    String str_m_s,str_m_r_i_d,stramt,struid,str_t_i_d,strNotes,strCurrency,str_m_n,str_m_i_d,str_b_n,formattedDate;
    ImageView app_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pg_info);

        b_n = findViewById(R.id.brand_name);
        merchant_name = findViewById(R.id.merchant_name);
        txtUserAmount = findViewById(R.id.txtUserAmount);
        edit_m = findViewById(R.id.txtUserMobile);
        progress = findViewById(R.id.progress);


        go = findViewById(R.id.proceed);


        Intent i = getIntent();
        stramt = i.getExtras().getString("trAm");
        struid = i.getExtras().getString("trUpiId");
        str_t_i_d = i.getExtras().getString("trId");
        strNotes = i.getExtras().getString("trNotes");
        strCurrency = i.getExtras().getString("trcur");
        str_b_n = i.getExtras().getString("BRname");
        str_m_n = i.getExtras().getString("merchant_name");
        str_m_i_d = i.getExtras().getString("merchant_id");
        str_m_r_i_d = i.getExtras().getString("merchant_ref_id");
        str_m_s = i.getExtras().getString("merchant_secret");
        if (i.getExtras().getString("trOrId") != null) {
            strorderid = i.getExtras().getString("trOrId");
        }


        merchant_name.setText(str_m_n);
        txtUserAmount.setText("₹ "+stramt);
        b_n.setText("Brand Name : "+str_b_n);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                getLocation();
            } else {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        } else {
            getLocation();
        }

        ipAddress = getIPAddress();
        Date currentDate = new Date();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        formattedDate = dateFormatter.format(currentDate);




        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_m.getText().toString().length()!=10){
                    edit_m.setError("Enter Mobile Number");
                    edit_m.setFocusable(true);
                }else {
                    go.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    PGC();


                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                finish();
            }
        }
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                } else {
                    finish();
                }
            }
        }
    }


    private String getIPAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }


    private void PGC(){
        SharedPreferences prefs = SDK_PG_UPI_Activity.this.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        PG_Collect_REQUEST Collect_REQ= new PG_Collect_REQUEST();
        Collect_REQ.setIpaddress(ipAddress);
        Collect_REQ.setLatitude(String.valueOf(latitude));
        Collect_REQ.setLongitude(String.valueOf(longitude));
        Collect_REQ.setAppId(Build.FINGERPRINT);
        Collect_REQ.setMerchantId(str_m_i_d);
        Collect_REQ.setMerchantRefId(str_t_i_d);
        Collect_REQ.setMerchantSecret(str_m_s);
        Collect_REQ.setMerchantTransactionAmount(stramt);
        Collect_REQ.setMerchantUserName(str_m_n);
        Collect_REQ.setMobileNumber(edit_m.getText().toString());


        AI aint= AC.PB().create(AI.class);
        Call<PG_Collect_RES> apicall= aint.USER_DATA(Collect_REQ);
        apicall.enqueue(new Callback<PG_Collect_RES>() {
            @Override
            public void onResponse(Call<PG_Collect_RES> call, Response<PG_Collect_RES> response) {

                try {


                    if (response.body().getStatus().equals("00")) {

                        String data= response.body().getData().getUpiid();
                        if (response.body().getData().getPipe().equals("P1")) {

                            Intent i = new Intent(SDK_PG_UPI_Activity.this, UPG.class);
                            i.putExtra("trAm", stramt);
                            i.putExtra("trUpiId", data);
                            i.putExtra("trId", str_t_i_d);
                            i.putExtra("trNotes", strNotes);
                            i.putExtra("trcur", strCurrency);
                            i.putExtra("trOrId", strorderid);
                            i.putExtra("BRname", str_b_n);
                            i.putExtra("merchant_name", str_m_n);
                            i.putExtra("merchant_id", str_m_i_d);
                            i.putExtra("merchant_secret", str_m_s);
                            i.putExtra("merchnat_ref_id", str_t_i_d);
                            i.putExtra("mobile", edit_m.getText().toString());
                            i.putExtra("ip_address", ipAddress);
                            i.putExtra("date_time", formattedDate);
                            startActivity(i);
                            finish();

                        }

                        if (response.body().getData().getPipe().equals("P2")){

                            String originalString = "upi://pay?"+response.body().getData().getUpiIntent().toString();
                            Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                            upiPayIntent.setData(Uri.parse(originalString));
                            Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
                            if (null != chooser.resolveActivity(getPackageManager())) {
                                startActivityForResult(chooser, REQUEST_CODE_UPI_PAYMENT);
                            } else {
                                Toast.makeText(SDK_PG_UPI_Activity.this, "No UPI APP Found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {

                        go.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);

                    }

                }catch (Exception e){

                    go.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<PG_Collect_RES> call, Throwable t) {
                Toast.makeText(SDK_PG_UPI_Activity.this, "onFailure.....", Toast.LENGTH_SHORT).show();

                go.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final JSONObject responseData = new JSONObject();


        try {
            if (requestCode == REQUEST_CODE_UPI_PAYMENT) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String status = data.getStringExtra("Status");
                        String responseMessage = bundle.getString("response");

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


    private void Response(String MerchantTransactionStatus, String StrResponse) {

        PG_Response_REQ PG_REQ = new PG_Response_REQ();
        PG_REQ.setMerchantId(str_m_i_d);
        PG_REQ.setMerchantRefId(str_t_i_d);
        PG_REQ.setMerchantSecret(str_m_s);
        PG_REQ.setMerchantUserName(str_m_n);
        PG_REQ.setMerchantTransactionStatus(MerchantTransactionStatus);
        PG_REQ.setResponse(StrResponse);
        PG_REQ.setProvider("OTHER INTENT");

        AI aint = AC.PB().create(AI.class);
        Call<PG_Response_RES> apicall = aint.Transaction_response(PG_REQ);
        apicall.enqueue(new Callback<PG_Response_RES>() {
            @Override
            public void onResponse(Call<PG_Response_RES> call, Response<PG_Response_RES> response) {

                try {


                    if (response.body().getStatus().equals("00")) {


                        if (response.body().getResponse().getStatus().equals("Success") || response.body().getResponse().getStatus().equals("SUCCESS")) {

                            Intent i = new Intent(SDK_PG_UPI_Activity.this, CT.class);
                            i.putExtra("txnid", response.body().getResponse().getTxnId());
                            i.putExtra("ref_t_id", response.body().getResponse().getApprovalRefNo());
                            i.putExtra("flag", 1);
                            i.putExtra("trAm", stramt);
                            i.putExtra("merchant_name", str_m_n);
                            i.putExtra("BRname", str_b_n);
                            i.putExtra("mobile", str_m_n);
                            startActivity(i);
                            finish();
                        } else {

                            Intent i = new Intent(SDK_PG_UPI_Activity.this, CT.class);
                            i.putExtra("txnid", response.body().getResponse().getTxnId());
                            i.putExtra("flag", 2);
                            i.putExtra("ref_t_id", response.body().getResponse().getApprovalRefNo());
                            i.putExtra("trAm", stramt);
                            i.putExtra("merchant_name", str_m_n);
                            i.putExtra("BRname", str_b_n);
                            i.putExtra("mobile", str_m_n);
                            startActivity(i);
                            finish();
                        }


                    } else {


                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<PG_Response_RES> call, Throwable t) {
            }
        });
    }

}