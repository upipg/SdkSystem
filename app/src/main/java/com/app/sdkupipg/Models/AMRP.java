package com.app.sdkupipg.Models;

import java.io.Serializable;
import java.util.List;

public class AMRP implements Serializable {
  private String msg;

  private List<Data> data;

  private String status;

  public String getMsg() {
    return this.msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public List<Data> getData() {
    return this.data;
  }

  public void setData(List<Data> data) {
    this.data = data;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public static class Data implements Serializable {
    private String app_name;

    private String app_icon;

    private String app_alias;

    private String app_status;

    private String provider;

    private String indate;

    private String fk_id;

    private String pk_app_id;

    public String getApp_name() {
      return this.app_name;
    }

    public void setApp_name(String app_name) {
      this.app_name = app_name;
    }

    public String getApp_icon() {
      return this.app_icon;
    }

    public void setApp_icon(String app_icon) {
      this.app_icon = app_icon;
    }

    public String getApp_alias() {
      return this.app_alias;
    }

    public void setApp_alias(String app_alias) {
      this.app_alias = app_alias;
    }

    public String getApp_status() {
      return this.app_status;
    }

    public void setApp_status(String app_status) {
      this.app_status = app_status;
    }

    public String getProvider() {
      return this.provider;
    }

    public void setProvider(String provider) {
      this.provider = provider;
    }

    public String getIndate() {
      return this.indate;
    }

    public void setIndate(String indate) {
      this.indate = indate;
    }

    public String getFk_id() {
      return this.fk_id;
    }

    public void setFk_id(String fk_id) {
      this.fk_id = fk_id;
    }

    public String getPk_app_id() {
      return this.pk_app_id;
    }

    public void setPk_app_id(String pk_app_id) {
      this.pk_app_id = pk_app_id;
    }
  }
}
