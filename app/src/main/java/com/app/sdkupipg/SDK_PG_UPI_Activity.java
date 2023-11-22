package com.app.sdkupipg;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.app.sdkupipg.Models.PG_Collect_REQUEST;
import com.app.sdkupipg.Models.PG_Collect_RES;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SDK_PG_UPI_Activity extends AppCompatActivity {
    String strtTid,strorderid;
    double latitude;
    double longitude;
    float pecentge = 18;

    String ipAddress, deviceId;

    private static final int PERMISSION_REQUEST_CODE = 1;
    TextView b_n,merchant_name,txtUserAmount;
    EditText edit_m;
    TextView go;
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




        // Check for location permissions at runtime (if not granted)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                getLocation();
            } else {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        } else {
            getLocation();
        }

        // Get IP address
         ipAddress = getIPAddress();
        Date currentDate = new Date();

        // Define the desired date format
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        // Format the current date as a string in the desired format
         formattedDate = dateFormatter.format(currentDate);

         


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_m.getText().toString().length()!=10){
                    edit_m.setError("Enter Mobile Number");
                    edit_m.setFocusable(true);
                }else {

                    PGC();


                }
            }
        });


    }

    // Check location permission at runtime (for Android 6.0 and above)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Request location permission callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                // Handle permission denied
finish();
            }
        }
    }

    // Get current location
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

    // Get device IP address

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

      //set data req

        AI aint= AC.PB().create(AI.class);
        Call<PG_Collect_RES> apicall= aint.USER_DATA(Collect_REQ);
        apicall.enqueue(new Callback<PG_Collect_RES>() {
            @Override
            public void onResponse(Call<PG_Collect_RES> call, Response<PG_Collect_RES> response) {

                try {


                    if (response.body().getStatus().equals("00")) {

                        Intent i = new Intent(SDK_PG_UPI_Activity.this, UPG.class);
                        i.putExtra("trAm",stramt);
                        i.putExtra("trUpiId",struid);
                        i.putExtra("trId",str_t_i_d);
                        i.putExtra("trNotes",strNotes);
                        i.putExtra("trcur",strCurrency);
                        i.putExtra("trOrId",strorderid);
                        i.putExtra("BRname",str_b_n);
                        i.putExtra("merchant_name",str_m_n);
                        i.putExtra("merchant_id",str_m_i_d);
                        i.putExtra("merchant_secret",str_m_s);
                        i.putExtra("merchnat_ref_id",str_t_i_d);
                        i.putExtra("mobile",edit_m.getText().toString());
                        i.putExtra("ip_address",ipAddress);
                        i.putExtra("date_time",formattedDate);
                        startActivity(i);
                        finish();


                    } else {


                    }

                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<PG_Collect_RES> call, Throwable t) {
                Toast.makeText(SDK_PG_UPI_Activity.this, "onFailure.....", Toast.LENGTH_SHORT).show();
                //   Toast.makeText(M_pinLoginCheck.this, "Failed to make the API call: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Log.e("API_CALL_FAILURE", "Failed to make the API call: " + t.getMessage());
            }
        });
    }




}