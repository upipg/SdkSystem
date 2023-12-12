package com.app.sdkupiiii.Models;

import java.io.Serializable;

public class PG_Collect_RES implements Serializable {
    private String msg;

    private Data data;

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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data implements Serializable {
        private String brandName;

        private String Message;

        private String Id;

        private String upiid;

        private String tid;

        private String pipe;
        private String upiIntent;

        public String getPipe() {
            return pipe;
        }

        public void setPipe(String pipe) {
            this.pipe = pipe;
        }

        public String getUpiIntent() {
            return upiIntent;
        }

        public void setUpiIntent(String upiIntent) {
            this.upiIntent = upiIntent;
        }

        public String getBrandName() {
            return this.brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

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

        public String getUpiid() {
            return this.upiid;
        }

        public void setUpiid(String upiid) {
            this.upiid = upiid;
        }

        public String getTid() {
            return this.tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }
    }
}
