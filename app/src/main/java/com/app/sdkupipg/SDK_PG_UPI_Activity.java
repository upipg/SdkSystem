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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
    String strupiid,strtTid,strorderid,stramount;
    double latitude;
    double longitude;
    float pecentge = 18;

    String ipAddress, deviceId;

    private static final int PERMISSION_REQUEST_CODE = 1;
    TextView brand_name,merchant_name,txtUserAmount;
    EditText txtUserMobile;
    AppCompatButton proceed;
    String strmerchant_secret,strmerchnat_ref_id,strAmount,strUpiId,strTransactionId,strNotes,strCurrency,strmerchnat_name,strmerchnat_id,strbrandname,formattedDate;
    ImageView app_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pg_info);

        brand_name = findViewById(R.id.brand_name);
        merchant_name = findViewById(R.id.merchant_name);
        txtUserAmount = findViewById(R.id.txtUserAmount);
        txtUserMobile = findViewById(R.id.txtUserMobile);


        proceed = findViewById(R.id.proceed);


        Intent i = getIntent();
        strAmount = i.getExtras().getString("trAm");
        strUpiId = i.getExtras().getString("trUpiId");
        strTransactionId = i.getExtras().getString("trId");
        strNotes = i.getExtras().getString("trNotes");
        strCurrency = i.getExtras().getString("trcur");
        strbrandname = i.getExtras().getString("BRname");
        strmerchnat_name = i.getExtras().getString("merchant_name");
        strmerchnat_id = i.getExtras().getString("merchant_id");
        strmerchnat_ref_id = i.getExtras().getString("merchant_ref_id");
        strmerchant_secret = i.getExtras().getString("merchant_secret");
        if (i.getExtras().getString("trOrId") != null) {
            strorderid = i.getExtras().getString("trOrId");
        }


        merchant_name.setText(strmerchnat_name);
        txtUserAmount.setText("₹ "+strAmount);
        brand_name.setText("Brand Name : "+strbrandname);




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

        System.out.println("Current Date: " + formattedDate);




        System.out.println("ipAddress"+ipAddress+"        \n    "+Build.DEVICE+"\n"+Build.USER+"\n"+Build.SERIAL+"\n"+Build.FINGERPRINT);



        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUserMobile.getText().toString().length()!=10){
                    txtUserMobile.setError("Enter Mobile Number");
                    txtUserMobile.setFocusable(true);
                }else {

                    PG_Collect();


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
                Log.e("Permission", "Location permission denied");
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

                    Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                } else {
                    Log.e("Location", "Unable to retrieve location");
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
                    System.out.println(inetAddress+" IP Address");
                    // Check if it's not the loopback address and if it's an IPv4 address
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





    //call

    private void PG_Collect(){
        SharedPreferences prefs = SDK_PG_UPI_Activity.this.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        PG_Collect_REQUEST Collect_REQ= new PG_Collect_REQUEST();
        Collect_REQ.setIpaddress(ipAddress);
        Collect_REQ.setLatitude(String.valueOf(latitude));
        Collect_REQ.setLongitude(String.valueOf(longitude));
        Collect_REQ.setAppId(Build.FINGERPRINT);
        Collect_REQ.setMerchantId(strmerchnat_id);
        Collect_REQ.setMerchantRefId(strTransactionId);
        Collect_REQ.setMerchantSecret(strmerchant_secret);
        Collect_REQ.setMerchantTransactionAmount(strAmount);
        Collect_REQ.setMerchantUserName(strmerchnat_name);
        Collect_REQ.setMobileNumber(txtUserMobile.getText().toString());

      //set data req

        ApiInterface apiInterface= ApiClient.PG_BASE().create(ApiInterface.class);
        Call<PG_Collect_RES> apicall= apiInterface.USER_DATA(Collect_REQ);
        apicall.enqueue(new Callback<PG_Collect_RES>() {
            @Override
            public void onResponse(Call<PG_Collect_RES> call, Response<PG_Collect_RES> response) {

                try {


                    if (response.body().getStatus().equals("00")) {

                        Intent i = new Intent(SDK_PG_UPI_Activity.this,UpiPgActivity.class);
                        i.putExtra("trAm",strAmount);
                        i.putExtra("trUpiId",strUpiId);
                        i.putExtra("trId",strTransactionId);
                        i.putExtra("trNotes",strNotes);
                        i.putExtra("trcur",strCurrency);
                        i.putExtra("trOrId",strorderid);
                        i.putExtra("BRname",strbrandname);
                        i.putExtra("merchant_name",strmerchnat_name);
                        i.putExtra("merchant_id",strmerchnat_id);
                        i.putExtra("merchant_secret",strmerchant_secret);
                        i.putExtra("merchnat_ref_id",strTransactionId);
                        i.putExtra("mobile",txtUserMobile.getText().toString());
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