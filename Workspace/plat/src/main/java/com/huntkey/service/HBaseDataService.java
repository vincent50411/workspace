package com.huntkey.service;

import com.huntkey.util.DateUtils;
import com.huntkey.vo.ORMDataBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by liuwens on 2017/7/3.
 */
@Service
public class HBaseDataService {

    // 与HBase数据库的连接对象
    private static Connection connection;

    // 数据库元数据操作对象
    private static HBaseAdmin admin;

    static
    {
        // 取得一个数据库连接的配置参数对象
        Configuration conf = HBaseConfiguration.create();

        // 设置连接参数：HBase数据库所在的主机IP vm-centeros03.hkgp.net,vm-centeros02.hkgp.net, vm-centeros01.hkgp.net
        conf.set("hbase.zookeeper.quorum", "vm-linux-162.hkhf.hkgp.net,vm-linux-161.hkhf.hkgp.net,vm-linux-163.hkhf.hkgp.net");
        // 设置连接参数：HBase数据库使用的端口
        conf.set("hbase.zookeeper.property.clientPort", "2181");

        conf.set("zookeeper.znode.parent", "/hbase-unsecure");

        // 取得一个数据库元数据操作对象
        try
        {
            // 取得一个数据库连接对象
            connection = ConnectionFactory.createConnection(conf);

            admin = (HBaseAdmin) connection.getAdmin();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String createDateDimesionTable()
    {
        String timestampColumnValue = DateUtils.getDateStrFromSegment("2012-01-01", "2016-01-01", "yyyy-MM-dd");

        String yearStr = DateUtils.formatDate(DateUtils.parseDate(timestampColumnValue), "yyyy") ;

        String monthStr = DateUtils.formatDate(DateUtils.parseDate(timestampColumnValue), "MM");


        //所在年
        String yearBegStr = yearStr + "-01-01";

        //所在季度开始日期
        String qtrBegStr = yearStr + "-" + DateUtils.getQrtByMonth(timestampColumnValue) + "-01";

        //所在月份
        String monthBegStr = yearStr + "-" + monthStr + "-01";


        return yearBegStr + "; " + qtrBegStr + "; " + monthBegStr + ";day:" + timestampColumnValue;
    }




    public HBaseAdmin getHBaseAdmin()
    {
        return admin;
    }

    /**
     * 创建表
     */
    public void createTable(String tableNameValue) throws IOException{

        System.out.println("--------------- 检查表 " + tableNameValue + "-----------------");

        // 数据表表名
        String tableNameString = tableNameValue;

        // 新建一个数据表表名对象
        TableName tableName = TableName.valueOf(tableNameString);

        // 如果需要新建的表已经存在
        if(admin.tableExists(tableName))
        {
            System.out.println(tableNameString + "表已经存在！");
        }
        // 如果需要新建的表不存在
        else
        {
            System.out.println("--------------- 创建表 -----------------");

            System.out.println("新建表:" + tableNameString);

            // 数据表描述对象
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);

            // 列族描述对象
            HColumnDescriptor family= new HColumnDescriptor("cf");

            // 在数据表中新建一个列族
            hTableDescriptor.addFamily(family);

            // 新建数据表
            admin.createTable(hTableDescriptor);
        }

        System.out.println("---------------创建表 END-----------------");
    }

    /**
     * 插入数据
     */
    public void insert(ORMDataBean ormDataBean) throws IOException
    {

        // 取得一个数据表对象
        Table table = connection.getTable(TableName.valueOf(ormDataBean.getTableName()));

        // 需要插入数据库的数据集合
        List<Put> putList = new ArrayList<>();

        // 生成数据集合
        List rowList = ormDataBean.getRowList();

        System.out.println("--------------- 待插入" + rowList.size() + "条数据 START -----------------");

        for(int rowIndex = 0;  rowList != null && rowIndex < rowList.size(); rowIndex++)
        {
            String rowKey = UUID.randomUUID().toString();
            Put put = new Put(Bytes.toBytes(rowKey));

            //行数据
            Map<String, Object> column = (Map<String, Object>)rowList.get(rowIndex);

            for(Map.Entry<String, Object> entry : column.entrySet())
            {
                String columnName = entry.getKey();

                if(entry.getValue() instanceof Date)
                {
                    Date timeValue = (Date)entry.getValue();

                    //columnValue 不能为null，否则会出现空指针异常
                    put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes(columnName), Bytes.toBytes(DateUtils.getDate(timeValue, "yyyy-MM-dd")));
                }
                else
                {
                    String columnValue = (String) entry.getValue();

                    //columnValue 不能为null，否则会出现空指针异常
                    put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes(columnName), Bytes.toBytes(columnValue == null ? "" : columnValue));
                }
            }

            putList.add(put);
        }

        // 将数据集合插入到数据库
        table.put(putList);

        System.out.println("---------------插入数据 END-----------------");
    }


    /**
     * 创建测试数据，先检查表（不存在，创建表）；清空数据；插入数据
     * @param ormDataBean
     */
    public void createTestDataToHBaseService(ORMDataBean ormDataBean)
    {
        createTestDataToHBaseService(ormDataBean, true);
    }

    /**
     * 创建测试数据，先检查表（不存在，创建表）；是否清空数据；插入数据
     * @param ormDataBean
     * @param isTruncate
     */
    public void createTestDataToHBaseService(ORMDataBean ormDataBean, boolean isTruncate)
    {
        try
        {
            //先检查表
            createTable(ormDataBean.getTableName());

            //先清空表数据
            if(isTruncate)
            {
                truncateTable(ormDataBean.getTableName());
            }

            insert(ormDataBean);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    /**
     * 查询整表数据, 该接口只对特定列族进行查询
     */
    public ORMDataBean queryTable(String tableName, String columnFamilyName, String[] columnList) throws IOException
    {
        ORMDataBean ormDataBean = new ORMDataBean();
        ormDataBean.setTableName(tableName);

        System.out.println("---------------查询" + tableName + "整表数据 START-----------------");

        ResultScanner scanner = null;

        List<Map<String, Object>> resultList = new ArrayList<>();

        try
        {
            // 取得数据表对象
            Table table = connection.getTable(TableName.valueOf(tableName));

            Scan scan = new Scan();

            //对列结果进行选择性的筛选
            if(columnList != null && columnList.length > 0)
            {
                for (int index = 0; index < columnList.length; index++)
                {
                    String columnName = columnList[index];

                    scan.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(columnName));
                }
            }

            // 取得表中所有数据
            scanner = table.getScanner(scan);


            int rowIndex = 0;
            // 循环输出表中的数据
            for(Result result:scanner)
            {
                Map<String, Object> rowMap = new HashMap<>();

                //得到单元格集合
                List<Cell> cs = result.listCells();
                for(Cell cell:cs)
                {
                    //取行健
                    String rowKey = Bytes.toString(CellUtil.cloneRow(cell));
                    //取到时间戳
                    long timestamp = cell.getTimestamp();
                    //取到族列
                    String family = Bytes.toString(CellUtil.cloneFamily(cell));
                    //取到修饰名:列的名称
                    String qualifier  = Bytes.toString(CellUtil.cloneQualifier(cell));
                    //取到值
                    String value = Bytes.toString(CellUtil.cloneValue(cell));

                    if(columnList != null && columnList.length > 0)
                    {
                        for(int index = 0; index < columnList.length; index++)
                        {
                            String columnName = columnList[index];

                            //取需要的列
                            if(columnName.equals(qualifier))
                            {
                                rowMap.put(qualifier, value);
                            }
                        }
                    }
                    else
                    {
                        //如果columnList为空，则表示不对结果集列进行筛选，全部返回所有列
                        rowMap.put(qualifier, value);
                    }

                }

                resultList.add(rowMap);

                rowIndex++;
            }

            ormDataBean.setRowList(resultList);

            //closing the scanner
            scanner.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            scanner.close();
        }

        System.out.println("---------------查询整表数据 END, 总记录数 " + resultList.size() + "-----------------" );

        return ormDataBean;
    }


    /**
     * 按行键查询表数据
     */
    public void queryTableByRowKey() throws IOException{

        System.out.println("---------------按行键查询表数据 START-----------------");

        // 取得数据表对象
        Table table = connection.getTable(TableName.valueOf("member_test"));

        // 新建一个查询对象作为查询条件
        Get get = new Get("l_0005".getBytes());

        // 按行键查询数据
        Result result = table.get(get);

        byte[] row = result.getRow();
        System.out.println("row key is:" + new String(row));

        List<Cell> listCells = result.listCells();
        for (Cell cell : listCells) {

            byte[] familyArray = cell.getFamilyArray();
            byte[] qualifierArray = cell.getQualifierArray();
            byte[] valueArray = cell.getValueArray();

            System.out.println("row value is:" + new String(familyArray) + new String(qualifierArray)
                    + new String(valueArray));
        }

        System.out.println("---------------按行键查询表数据 END-----------------");

    }


    public void queryRowByFilter()
    {
        // 取得数据表对象
        try
        {
            Table table = connection.getTable(TableName.valueOf("t_hk_sales"));

            Scan scan = new Scan();

            // Scanning the required columns cf:sales_value
            //scan.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("sale_time"));
            //scan.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("sales_value"));

            Filter rf = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("f7f35312-7e20-4afa-913b-895a372f5d56"))); // OK 筛选出匹配的所有的行
            Filter vf = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("2fda8fe0-0af6-42c6-8032-2d2a10a528ec")); // OK 筛选某个（值的条件满足的）特定的单元格

            SingleColumnValueFilter cf = new SingleColumnValueFilter(Bytes.toBytes("cf"), Bytes.toBytes("fk_custom"), CompareFilter.CompareOp.EQUAL, new SubstringComparator("6da21ecd-1f2b-4d49-8840-af218c5b4a86"));
            cf.setFilterIfMissing(true);
            cf.setLatestVersionOnly(true); // OK

            SingleColumnValueFilter cf2 = new SingleColumnValueFilter(Bytes.toBytes("cf"), Bytes.toBytes("fk_custom"), CompareFilter.CompareOp.EQUAL, new SubstringComparator("9ac0f36e-1838-453c-be50-1c9eea7bf7c3"));
            cf2.setFilterIfMissing(true);
            cf2.setLatestVersionOnly(true); // OK

            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            filterList.addFilter(cf);
            filterList.addFilter(cf2);

            scan.setFilter(filterList);

            // Getting the scan result
            ResultScanner scanner = table.getScanner(scan);

            for(Result result  = scanner.next(); result != null; result = scanner.next())
            {
                StringBuffer resultBuf = new StringBuffer();

                List<Cell> listCells = result.listCells();
                for (Cell cell : listCells)
                {
                    //取行健
                    String rowKey = Bytes.toString(CellUtil.cloneRow(cell));

                    //取到族列
                    String family = Bytes.toString(CellUtil.cloneFamily(cell));
                    //取到修饰名:列的名称
                    String qualifier  = Bytes.toString(CellUtil.cloneQualifier(cell));
                    //取到值
                    String value = Bytes.toString(CellUtil.cloneValue(cell));


                    resultBuf.append(family + ":" + qualifier + "=" + value + "; ");
                }

                System.out.println(resultBuf.toString());

            }


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }




    /**
     * 清空表
     */
    public void truncateTable(String tableNameValue) throws IOException{

        System.out.println("---------------清空表 START-----------------");

        // 取得目标数据表的表名对象
        TableName tableName = TableName.valueOf(tableNameValue);

        admin.disableTable(tableName);

        // 清空指定表的数据
        admin.truncateTable(tableName, true);

        System.out.println("---------------清空表 End-----------------");
    }



}
