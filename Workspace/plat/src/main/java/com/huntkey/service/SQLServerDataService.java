package com.huntkey.service;

import com.huntkey.vo.ORMDataBean;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * Created by liuwens on 2017/7/3.
 */
@Service
public class SQLServerDataService
{



    private Connection getConnection(String dbName)
    {
        Connection conn = null;

        //testdb02.hkgp.net
        String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url="jdbc:sqlserver://192.168.12.76:1433;DatabaseName=" + dbName;

        try
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,"hkdb","hkdb6688");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return conn;
    }


    /**
     *
     *  SELECT * FROM R8DB.DBO.V_ba_tree where tree_Code = 'tr01'
     * @param sqlValue
     * @return
     */
    public ORMDataBean query(String sqlValue)
    {
        ORMDataBean ormDataBean = new ORMDataBean();

        Connection connection = getConnection("R8DB");

        try
        {
            PreparedStatement pstmt = connection.prepareStatement(sqlValue);
            ResultSet rs = pstmt.executeQuery();

            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int columnNum = resultSetMetaData.getColumnCount();

            List rowList = new ArrayList();

            while(rs.next())
            {

                List columnList = new ArrayList();

                for (int columnIndex = 1; columnIndex < columnNum; columnIndex++)
                {
                    Map column = new HashMap();

                    // 获得指定列的列名
                    String columnName = resultSetMetaData.getColumnName(columnIndex);

                    // 获得指定列的列值
                    String columnValue = rs.getString(columnName);

                    column.put(columnName, columnValue);

                    columnList.add(column);
                }

                rowList.add(columnList);
            }

            ormDataBean.setRowList(rowList);

            rs.close();
            pstmt.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return ormDataBean;

    }

    /**
     * 将树形结构数据拆分为平行结构，每一层对应一列
     * @param hbaseTableName  HBase表名称
     * @param treeTypeValue   2 客户树; 3 仓库树; 4 产品分类树
     * @return
     */
    public ORMDataBean queryTreeDataToMapService(String hbaseTableName, String treeTypeValue, int level, String columnName)
    {
        Connection connection = getConnection("R8DB");

        ORMDataBean ormDataBean = new ORMDataBean();
        ormDataBean.setTableName(hbaseTableName);

        //查根节点的集合
        String rootSql = "select tret_level,tret_code,* from hportal.dbo.tba_tree_t_tmp where tret_enddate is null and  tret_tree_id =" + treeTypeValue + " and tret_parent_id ='0'";

        //查询树形结构层级的集合，父节点的code需要在循环层是替换
        String querySQL = "select tret_level,tret_code,* from hportal.dbo.tba_tree_t_tmp where tret_enddate is null and  tret_tree_id =" + treeTypeValue + " and TRET_PARENT_ID in (?)";

        //总层级
        String queryLevelIndexSQL = "select max(len(tret_level) - len(replace(tret_level, ',', ''))) max_value from hportal.dbo.tba_tree_t_tmp where tret_enddate is null and tret_tree_id =" + treeTypeValue;

        int levelIndex = 0;

        List<Map<String, Object>> resultList = new ArrayList<>();

        String columnKeyName = "tret_id";

        try
        {
            //先统计树形结构总的层级深度
            levelIndex = queryLevelIndex(connection, queryLevelIndexSQL);

            List<Map<String, Object>> rowList = new ArrayList<>();

            //根节点
            queryForTree(connection, rowList, rootSql, columnKeyName ,null, 0, columnName);
            resultList.addAll(rowList);

            List<Map<String, Object>> recursionResult = rowList;

            //按照最大层级计算循环次数，如果最后一层返回合并的结果集为null，则说明已经没有更深层
            for(int index = 1; index < levelIndex; index++)
            {
                int forLen = recursionResult.size()/1000;
                if(recursionResult.size()%1000 > 0)
                {
                    forLen += 1;
                }

                //多层循环中，每一层的所有结果集
                List<Map<String, Object>> levelsRowList = new ArrayList<>();

                for(int forIndex = 0; forIndex < forLen; forIndex++)
                {
                    StringBuffer conditionValue = new StringBuffer();

                    for(int i = 1000 * forIndex; i < recursionResult.size() && i < (1000 * (forIndex + 1)); i++)
                    {
                        Map<String, Object> queryMap = recursionResult.get(i);

                        //根据父节点最新的列名称获取缓存值, 父节点层级减一
                        String columnKeyValue = (String)queryMap.get(columnName + "_" + (index - 1));
                        if(columnKeyValue != null)
                        {
                            columnKeyValue = "'" + columnKeyValue.replaceAll(" ", "") + "',";

                            conditionValue.append(columnKeyValue);
                        }
                    }

                    String inValueStr = conditionValue.toString();

                    String inValue = inValueStr.substring(0, inValueStr.length() - 1);

                    //tret_tree_id 2 客户树; 3 仓库树; 4 产品分类树
                    String querySQLNew = querySQL.replace("?", inValue);

                    //每一层中，利用父节点code查询所有子节点列表
                    queryForTree(connection, levelsRowList, querySQLNew, columnKeyName, recursionResult, index, columnName);
                }

                //本层查询结果集作为下层查询的父节点条件
                recursionResult = levelsRowList;

                System.out.print("当前第" + index + "层，本层总记录数" + recursionResult.size());

                if(levelsRowList .size() == 0)
                {
                    //如果一层查询结束，合并的结果集没有任何值，则说明当前这层已经是叶子层，可以直接跳出循环
                    break;
                }

                resultList.addAll(levelsRowList);
            }

            ormDataBean.setRowList(resultList);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if(connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return ormDataBean;
    }


    private void queryForTree(Connection connection, List<Map<String, Object>> rowList, String querySQL, String columnKeyName, List<Map<String, Object>> recursionResult, int index, String columnName) throws SQLException
    {
        System.out.println(querySQL + ";");

        PreparedStatement pstmt = connection.prepareStatement(querySQL);
        ResultSet rs = pstmt.executeQuery();

        while(rs.next())
        {
            Map column = new HashMap();

            //主键
            column.put("pk_col", UUID.randomUUID().toString());

            //自己的主键code, 去除多余的占位符
            String colValue = rs.getString(columnKeyName) == null ? "" : rs.getString(columnKeyName);

            //自定义列名称
            column.put(columnName + "_" + index, colValue.replaceAll(" ", ""));

            if(recursionResult != null)
            {
                String parentTredIDColumnValue = rs.getString("tret_parent_id");

                //将父层级的列值合并到子层级中，最终形成梯形结构
                //mergeParentMapValue(columnKeyName, parentColumnMap, column);

                for (Map<String, Object> parentColumnMap : recursionResult)
                {
                    if(parentColumnMap != null)
                    {
                        //自己上一层的父节点值
                        String preParentCodeValue = (String)parentColumnMap.get(columnName + "_" + (index - 1));

                        //查找自己上级节点，并合并父节点的父节点的code，形成梯形结构
                        if(preParentCodeValue.equals(parentTredIDColumnValue))
                        {
                            mergeParentMapValue(columnName, parentColumnMap, column);
                        }
                    }
                }
            }

            rowList.add(column);
        }

        if(rs != null)
        {
            rs.close();
        }

        if(pstmt != null)
        {
            pstmt.close();
        }
    }


    public ORMDataBean recursionQueryDeptInfo(String hbaseTableName)
    {
        ORMDataBean ormDataBean = new ORMDataBean();
        ormDataBean.setTableName(hbaseTableName);

        List<Map<String, Object>> resultList = new ArrayList<>();

        String sql = "select * from hr90.dbo.thr_dept_mstr where dept_enable=1 and len(dept_level)=0";

        Connection connection = getConnection("R8DB");

        List<Map<String, Object>> rowList = new ArrayList<>();

        try
        {
            test(connection, rowList, sql, "dept_level" ,null, 0);

            resultList.addAll(rowList);

            List<Map<String, Object>> recursionResult = rowList;

            for(int index = 1; index < 9; index++)
            {
                recursionResult = recursion(connection, recursionResult, "dept_level",   index);
                resultList.addAll(recursionResult);
            }

            System.out.println("记录数:" + resultList.size());

            ormDataBean.setRowList(resultList);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if(connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return ormDataBean;

    }

    private List<Map<String, Object>> recursion(Connection connection, List<Map<String, Object>> queryList, String columnKeyName, int index) throws SQLException
    {
        List<Map<String, Object>> rowList = new ArrayList<>();

        //循环所有行
        for (Map<String, Object> queryMap : queryList)
        {
            //根据父节点最新的列名称获取缓存值, 父节点层级减一
            String columnKeyValue = (String)queryMap.get(columnKeyName + "_" + (index - 1));

            columnKeyValue = columnKeyValue.replaceAll(" ", "");

            String querySQL = "select * from hr90.dbo.thr_dept_mstr where dept_enable=1 and dept_level like '?' and dept_level <> '" + columnKeyValue + "'";

            String querySQLNew = querySQL.replace("?", columnKeyValue + "__");

            System.out.println(querySQLNew + ";");

            test(connection, rowList, querySQLNew, columnKeyName, queryMap, index);
        }

        return rowList;
    }

    private void test(Connection connection, List<Map<String, Object>> rowList, String querySQL, String columnKeyName, Map<String, Object> parentColumnMap, int index) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement(querySQL);
        ResultSet rs = pstmt.executeQuery();

        while(rs.next())
        {
            Map column = new HashMap();

            //主键
            column.put("pk_col", UUID.randomUUID().toString());

            String deptCodeColName = "dept_code";

            String deptCodeValue = rs.getString(deptCodeColName) == null ? "" : rs.getString(deptCodeColName);
            column.put(deptCodeColName + "_" + index, deptCodeValue.replaceAll(" ", ""));

            //自己的主键code, 去除多余的占位符
            String colValue = rs.getString(columnKeyName) == null ? "" : rs.getString(columnKeyName);
            column.put(columnKeyName + "_" + index, colValue.replaceAll(" ", ""));

            //别名
            column.put("dept_name" + "_" + index, rs.getString("dept_name") == null ? "" : rs.getString("dept_name"));

            mergeParentMapValue(deptCodeColName, parentColumnMap, column);

            rowList.add(column);
        }

        if(rs != null)
        {
            rs.close();
        }

        if(pstmt != null)
        {
            pstmt.close();
        }
    }

    private void mergeParentMapValue(String columnKeyName, Map<String, Object> parentColumnMap, Map column) {
        if(parentColumnMap != null)
        {
            for(Map.Entry<String, Object> entry : parentColumnMap.entrySet())
            {
                String parentColKey = entry.getKey();
                String parentColValue = (String)entry.getValue();

                //相关列才做处理
                if(parentColKey.indexOf(columnKeyName) > -1)
                {
                    //父节点的主键code
                    column.put(parentColKey, parentColValue);
                }
            }
        }
    }



    private int queryLevelIndex(Connection connection, String querySQL) throws SQLException
    {
        int result = 0;

        PreparedStatement pstmt = connection.prepareStatement(querySQL);
        ResultSet rs = pstmt.executeQuery();

        while(rs.next())
        {
            result = rs.getInt("max_value");
        }

        if(rs != null)
        {
            rs.close();
        }

        if(pstmt != null)
        {
            pstmt.close();
        }

        return result;
    }




    public void querySQLTable(String hbaseTableName, String treeTypeValue)
    {
        Connection connection = getConnection("R8DB");

        ORMDataBean ormDataBean = new ORMDataBean();
        ormDataBean.setTableName(hbaseTableName);


        String querySQL = "select tret_level,tret_code,* from hportal.dbo.tba_tree_t_tmp where tret_enddate is null and  tret_tree_id =" + treeTypeValue + " and TRET_PARENT_ID in (?)";





    }






    /*
            for (int columnIndex = 1; columnIndex < columnNum; columnIndex++)
            {
                // 获得指定列的列名
                String columnName = resultSetMetaData.getColumnName(columnIndex);

                // 获得指定列的列值
                String columnValue = rs.getString(columnName);

                column.put(columnName, columnValue);
            }
            */

/*
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
            {
                // 获得所有列的数目及实际列数
                int columnCount = resultSetMetaData.getColumnCount();
                // 获得指定列的列名
                String columnName = resultSetMetaData.getColumnName(i);
                // 获得指定列的列值
                int columnType = resultSetMetaData.getColumnType(i);
                // 获得指定列的数据类型名
                String columnTypeName = resultSetMetaData.getColumnTypeName(i);
                // 所在的Catalog名字
                String catalogName = resultSetMetaData.getCatalogName(i);
                // 对应数据类型的类
                String columnClassName = resultSetMetaData.getColumnClassName(i);
                // 在数据库中类型的最大字符个数
                int columnDisplaySize = resultSetMetaData.getColumnDisplaySize(i);
                // 默认的列的标题
                String columnLabel = resultSetMetaData.getColumnLabel(i);
                // 获得列的模式
                String schemaName = resultSetMetaData.getSchemaName(i);
                // 某列类型的精确度(类型的长度)
                int precision = resultSetMetaData.getPrecision(i);
                // 小数点后的位数
                int scale = resultSetMetaData.getScale(i);
                // 获取某列对应的表名
                String tableName = resultSetMetaData.getTableName(i);
                // 是否自动递增
                boolean isAutoInctement = resultSetMetaData.isAutoIncrement(i);
                // 在数据库中是否为货币型
                boolean isCurrency = resultSetMetaData.isCurrency(i);
                // 是否为空
                int isNullable = resultSetMetaData.isNullable(i);
                // 是否为只读
                boolean isReadOnly = resultSetMetaData.isReadOnly(i);
                // 能否出现在where中
                boolean isSearchable = resultSetMetaData.isSearchable(i);
            }
            */

}
