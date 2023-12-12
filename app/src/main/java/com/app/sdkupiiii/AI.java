package com.app.sdkupiiii;


import com.app.sdkupiiii.Models.AMR;
import com.app.sdkupiiii.Models.AMRP;
import com.app.sdkupiiii.Models.PG_Collect_REQUEST;
import com.app.sdkupiiii.Models.PG_Collect_RES;
import com.app.sdkupiiii.Models.PG_Response_REQ;
import com.app.sdkupiiii.Models.PG_Response_RES;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AI {

    @POST("initiateTransactionSDK.php")
    Call<PG_Collect_RES> USER_DATA(@Body PG_Collect_REQUEST mpin_req);


    @POST("getClientUpiMethods.php")
    Call<AMRP> N_F_S(@Body AMR req);


    @POST("updateTransaction.php")
    Call<PG_Response_RES> Transaction_response(@Body PG_Response_REQ req);


}
