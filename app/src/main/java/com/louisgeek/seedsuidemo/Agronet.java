package com.louisgeek.seedsuidemo;

import java.util.List;

/**
 * Created by louisgeek on 2016/5/10.
 */
public class Agronet {

   private String table_id;

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    private List<Period> periods;

    @Override
    public String toString() {
        return "Agronet{" +
                "table_id='" + table_id + '\'' +
                ", periods=" + periods +
                '}';
    }
}
