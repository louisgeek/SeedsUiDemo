package com.louisgeek.seedsuidemo;

import java.util.List;
import java.util.Map;

/**
 * Created by louisgeek on 2016/5/10.
 */
public class Period {

   private String period_name;
    private String period_value;

    private List<Map<String, Object>> columsMapList;

    public List<Map<String, Object>> getColumsMapList() {
        return columsMapList;
    }

    public void setColumsMap(List<Map<String, Object>> columsMapList) {
        this.columsMapList = columsMapList;
    }

    public String getPeriod_name() {
        return period_name;
    }

    public void setPeriod_name(String period_name) {
        this.period_name = period_name;
    }

    public String getPeriod_value() {
        return period_value;
    }

    public void setPeriod_value(String period_value) {
        this.period_value = period_value;
    }

    @Override
    public String toString() {
        return "Period{" +
                "period_name='" + period_name + '\'' +
                ", period_value='" + period_value + '\'' +
                ", columsMapList=" + columsMapList +
                '}';
    }
}
