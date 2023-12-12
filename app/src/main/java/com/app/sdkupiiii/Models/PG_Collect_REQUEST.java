package com.app.sdkupiiii.Models;

import java.io.Serializable;

public class PG_Collect_REQUEST implements Serializable {
    private String ipaddress;

    private String merchantId;

    private String merchantRefId;

    private String merchantTransactionAmount;

    private String mobileNumber;

    private String appId;

    private String latitude;

    private String merchantUserName;

    private String merchantSecret;

    private String longitude;

    public String getIpaddress() {
        return this.ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantRefId() {
        return this.merchantRefId;
    }

    public void setMerchantRefId(String merchantRefId) {
        this.merchantRefId = merchantRefId;
    }

    public String getMerchantTransactionAmount() {
        return this.merchantTransactionAmount;
    }

    public void setMerchantTransactionAmount(String merchantTransactionAmount) {
        this.merchantTransactionAmount = merchantTransactionAmount;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMerchantUserName() {
        return this.merchantUserName;
    }

    public void setMerchantUserName(String merchantUserName) {
        this.merchantUserName = merchantUserName;
    }

    public String getMerchantSecret() {
        return this.merchantSecret;
    }

    public void setMerchantSecret(String merchantSecret) {
        this.merchantSecret = merchantSecret;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
