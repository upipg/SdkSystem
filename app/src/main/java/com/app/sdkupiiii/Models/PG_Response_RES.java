package com.app.sdkupiiii.Models;

import java.io.Serializable;

public class PG_Response_RES  implements Serializable {
  private String msg;

  private Data data;

  private Response response;

  private String status;

  public String getMsg() {
    return this.msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Data getData() {
    return this.data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public Response getResponse() {
    return this.response;
  }

  public void setResponse(Response response) {
    this.response = response;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public static class Data implements Serializable {
    private String Message;

    private String Id;

    public String getMessage() {
      return this.Message;
    }

    public void setMessage(String Message) {
      this.Message = Message;
    }

    public String getId() {
      return this.Id;
    }

    public void setId(String Id) {
      this.Id = Id;
    }
  }

  public static class Response implements Serializable {
    private String ApprovalRefNo;

    private String Status;

    private String txnRef;

    private String txnId;

    private String responseCode;

    public String getApprovalRefNo() {
      return this.ApprovalRefNo;
    }

    public void setApprovalRefNo(String ApprovalRefNo) {
      this.ApprovalRefNo = ApprovalRefNo;
    }

    public String getStatus() {
      return this.Status;
    }

    public void setStatus(String Status) {
      this.Status = Status;
    }

    public String getTxnRef() {
      return this.txnRef;
    }

    public void setTxnRef(String txnRef) {
      this.txnRef = txnRef;
    }

    public String getTxnId() {
      return this.txnId;
    }

    public void setTxnId(String txnId) {
      this.txnId = txnId;
    }

    public String getResponseCode() {
      return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
      this.responseCode = responseCode;
    }
  }
}