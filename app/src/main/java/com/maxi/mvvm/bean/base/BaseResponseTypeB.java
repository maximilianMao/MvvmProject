package com.maxi.mvvm.bean.base;

/**
 * Created by maxi on 2021/9/3.
 */
public class BaseResponseTypeB<T> extends BaseResponse {

    private Datas<T> datas;

    public Datas<T> getDatas() {
        return datas;
    }

    public void setDatas(Datas<T> datas) {
        this.datas = datas;
    }

    public static class Datas<T> {
        private int total;
        private T list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public T getList() {
            return list;
        }

        public void setList(T list) {
            this.list = list;
        }
    }
}