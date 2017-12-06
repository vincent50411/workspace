package com.huntkey.multDimesions.dto;

import java.util.List;

/**
 * kylin cube查询结果 行的信息
 * Created by liuwens on 2017/8/11.
 */
public class HiveQueryRowResultDTO
{

    private List<HiveQuerColumnResultDTO> columns;

    public List<HiveQuerColumnResultDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<HiveQuerColumnResultDTO> columns) {
        this.columns=columns;
    }
}
