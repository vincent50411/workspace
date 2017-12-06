package com.huntkey.multDimesions.comparator;


import com.huntkey.multDimesions.dto.TableColumnInfoDTO;

import java.util.Comparator;

/**
 * 自定义比较器 sort升序操作 reverse是降序
 * Created by liuwens on 2017/8/10.
 */
public class KylinDimesionTableColumnComparator implements Comparator
{
    /**
     * sort升序操作
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Object o1, Object o2)
    {
        TableColumnInfoDTO compareStr1 = (TableColumnInfoDTO)o1;

        TableColumnInfoDTO compareStr2 = (TableColumnInfoDTO)o2;

        return compareStr1.getColumnName().compareTo(compareStr2.getColumnName());
    }
}
