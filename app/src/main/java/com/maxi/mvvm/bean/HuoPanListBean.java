package com.maxi.mvvm.bean;

import java.util.List;

/**
 * Created by maxi on 2021/9/14.
 */
public class HuoPanListBean {

    private int id;
    private int status;
    private String statusStr;
    private String goodsName;
    private String departureArea;
    private String arrivalArea;
    private long expirationDate;
    private int waitConfirm;
    private int processing;
    private int completed;
    private int isDelete;
    private List<ShipTypeJsonBean> shipTypeJson;
    private int expirationDateRange;
    private String goodsLaveTonnage;
    private String singlePrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDepartureArea() {
        return departureArea;
    }

    public void setDepartureArea(String departureArea) {
        this.departureArea = departureArea;
    }

    public String getArrivalArea() {
        return arrivalArea;
    }

    public void setArrivalArea(String arrivalArea) {
        this.arrivalArea = arrivalArea;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getWaitConfirm() {
        return waitConfirm;
    }

    public void setWaitConfirm(int waitConfirm) {
        this.waitConfirm = waitConfirm;
    }

    public int getProcessing() {
        return processing;
    }

    public void setProcessing(int processing) {
        this.processing = processing;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public List<ShipTypeJsonBean> getShipTypeJson() {
        return shipTypeJson;
    }

    public void setShipTypeJson(List<ShipTypeJsonBean> shipTypeJson) {
        this.shipTypeJson = shipTypeJson;
    }

    public int getExpirationDateRange() {
        return expirationDateRange;
    }

    public void setExpirationDateRange(int expirationDateRange) {
        this.expirationDateRange = expirationDateRange;
    }

    public String getGoodsLaveTonnage() {
        return goodsLaveTonnage;
    }

    public void setGoodsLaveTonnage(String goodsLaveTonnage) {
        this.goodsLaveTonnage = goodsLaveTonnage;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }

    public static class ShipTypeJsonBean {
        private String name;
        private int groupNo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGroupNo() {
            return groupNo;
        }

        public void setGroupNo(int groupNo) {
            this.groupNo = groupNo;
        }
    }
}