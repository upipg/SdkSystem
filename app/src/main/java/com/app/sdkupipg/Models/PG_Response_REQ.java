package com.app.sdkupipg.Models;

import java.io.Serializable;

public class PG_Response_REQ implements Serializable {
  private String merchantTransactionStatus;

  private String merchantId;

  private String merchantRefId;

  private String response;
  private String provider;

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  private String merchantUserName;

  private String merchantSecret;

  public String getMerchantTransactionStatus() {
    return this.merchantTransactionStatus;
  }

  public void setMerchantTransactionStatus(String merchantTransactionStatus) {
    this.merchantTransactionStatus = merchantTransactionStatus;
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

  public String getResponse() {
    return this.response;
  }

  public void setResponse(String response) {
    this.response = response;
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
}
