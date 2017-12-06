package com.huntkey.controller;

import com.huntkey.service.HBaseDataService;
import com.huntkey.service.RadomDataService;
import com.huntkey.service.SQLServerDataService;
import com.huntkey.util.DateUtils;
import com.huntkey.vo.ORMDataBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by liuwens on 2017/6/29.
 */
@RestController
@RequestMapping("/hbase")
public class HBaseClientController
{

    @Autowired
    private HBaseDataService hBaseDataService;

    @Autowired
    private SQLServerDataService sqlServerDataService;

    @Autowired
    private RadomDataService radomDataService;

    public static final String PK_COLUMN_NAME = "pk_col";

    private Map<String, ORMDataBean> beanMap = new HashMap<>();

    @RequestMapping("/date")
    public String randomDateBetweenMinAndMax()
    {
        String timestampColumnValue = DateUtils.getDateStrFromSegment("2010-01-01 00:00:00", "2017-12-31 00:00:00", "yyyy-MM-dd HH:mm:ss");

        return timestampColumnValue;
    }

    /**
     * 创建表
     */
    @RequestMapping("/createTable/{tableName}")
    public void createTable(@PathVariable("tableName")String tableNameValue) throws IOException
    {

    }

    @RequestMapping("/dept")
    public void queryTableData() throws IOException
    {
        ORMDataBean prodOrmDataBean = sqlServerDataService.queryTreeDataToMapService("t_hk_prod", "4", 1, "prod_tret_id");
        hBaseDataService.createTestDataToHBaseService(prodOrmDataBean);
    }

    @RequestMapping("/dimesionTable")
    public void insertDimesionTabelData()
    {
        //客户维度数据
        if(!beanMap.containsKey("t_hk_custom"))
        {
            ORMDataBean customOrmDataBean = sqlServerDataService.queryTreeDataToMapService("t_hk_custom", "2", 1, "custom_tret_id");
            hBaseDataService.createTestDataToHBaseService(customOrmDataBean);
            beanMap.put("t_hk_custom", customOrmDataBean);
        }

        //产品维度数据，一层结构
        if(!beanMap.containsKey("t_hk_prod"))
        {
            ORMDataBean prodOrmDataBean = sqlServerDataService.queryTreeDataToMapService("t_hk_prod", "4", 1, "prod_tret_id");
            hBaseDataService.createTestDataToHBaseService(prodOrmDataBean);
            beanMap.put("t_hk_prod", prodOrmDataBean);
        }

        //仓库维度数据
        if(!beanMap.containsKey("t_hk_store"))
        {
            ORMDataBean storeOrmDataBean = sqlServerDataService.queryTreeDataToMapService("t_hk_store", "3", 9, "store_tret_id");
            hBaseDataService.createTestDataToHBaseService(storeOrmDataBean);
            beanMap.put("t_hk_store", storeOrmDataBean);
        }

        //岗位测试数据
        if(!beanMap.containsKey("t_hk_gw"))
        {
            ORMDataBean gwOrmDataBean = radomDataService.createRadomDataService("t_hk_gw", PK_COLUMN_NAME, "岗位", 300);
            hBaseDataService.createTestDataToHBaseService(gwOrmDataBean);
            beanMap.put("t_hk_gw", gwOrmDataBean);
        }

        //职员测试数据
        if(!beanMap.containsKey("t_hk_employ"))
        {
            ORMDataBean employOrmDataBean = radomDataService.createRadomDataService("t_hk_employ", PK_COLUMN_NAME, "职员", 380);
            hBaseDataService.createTestDataToHBaseService(employOrmDataBean);
            beanMap.put("t_hk_employ", employOrmDataBean);
        }

        //部门维度表
        if(!beanMap.containsKey("t_hk_dept"))
        {
            ORMDataBean departmentOrmDataBean = sqlServerDataService.recursionQueryDeptInfo("t_hk_dept");
            hBaseDataService.createTestDataToHBaseService(departmentOrmDataBean);
            beanMap.put("t_hk_dept", departmentOrmDataBean);
        }
    }

    @RequestMapping("/factTable")
    public void insertTestData()
    {
        try
        {
            //客户维度数据，一层结构
            ORMDataBean customOrmDataBean = hBaseDataService.queryTable("t_hk_custom", "cf", new String[]{PK_COLUMN_NAME});

            //产品维度数据，一层结构
            ORMDataBean prodOrmDataBean = hBaseDataService.queryTable("t_hk_prod", "cf", new String[]{PK_COLUMN_NAME});

            //仓库维度数据
            ORMDataBean storeOrmDataBean = hBaseDataService.queryTable("t_hk_store", "cf", new String[]{PK_COLUMN_NAME});

            //岗位测试数据
            ORMDataBean gwOrmDataBean = hBaseDataService.queryTable("t_hk_gw", "cf", new String[]{PK_COLUMN_NAME});

            //职员测试数据
            ORMDataBean employOrmDataBean = hBaseDataService.queryTable("t_hk_employ", "cf", new String[]{PK_COLUMN_NAME});

            //部门维度表
            ORMDataBean departmentOrmDataBean = hBaseDataService.queryTable("t_hk_dept", "cf", new String[]{PK_COLUMN_NAME});

            //生成实时表
            List factTableRowList = new ArrayList();

            for(int index = 0; index < 10; index++)
            {
                //维度表行主键
                String pk_column_value = radomDataService.radomDimesionDataService(departmentOrmDataBean, PK_COLUMN_NAME);

                Map column = new HashMap();

                // ********************* 部门列 *********************
                String fkDeptColumnName = "fk_dept";

                // 部门列值
                String fkDeptColumnValue = pk_column_value;

                column.put(fkDeptColumnName, fkDeptColumnValue);

                // ********************* 仓库列 *********************
                String fkStoreColumnName = "fk_store";

                // 仓库列值
                String fkStoreColumnValue = radomDataService.radomDimesionDataService(storeOrmDataBean, PK_COLUMN_NAME);

                column.put(fkStoreColumnName, fkStoreColumnValue);

                // ********************* 客户列 *********************
                String fkCustomColumnName = "fk_custom";

                // 客户列值
                String fkCustomColumnValue = radomDataService.radomDimesionDataService(customOrmDataBean, PK_COLUMN_NAME);

                column.put(fkCustomColumnName, fkCustomColumnValue);

                // ********************* 产品列 *********************
                String fkProdColumnName = "fk_prod";

                // 产品列值
                String fkProdColumnValue = radomDataService.radomDimesionDataService(prodOrmDataBean, PK_COLUMN_NAME);

                column.put(fkProdColumnName, fkProdColumnValue);

                // ********************* 岗位列 *********************
                String fkGWColumnName = "fk_gw";

                // 岗位列值
                String fkGWColumnValue = radomDataService.radomDimesionDataService(gwOrmDataBean, PK_COLUMN_NAME);

                column.put(fkGWColumnName, fkGWColumnValue);

                // *********************  职员列 *********************
                String fkEmployColumnName = "fk_employ";

                // 职员列值
                String fkEmployColumnValue = radomDataService.radomDimesionDataService(employOrmDataBean, PK_COLUMN_NAME);

                column.put(fkEmployColumnName, fkEmployColumnValue);

                // *********************  度量值，销售额 *********************
                String dalesColumnName = "sales_value";
                String salesValue = String.valueOf(new DecimalFormat("#.00").format(1000 + Math.random() * 10000));

                column.put(dalesColumnName, salesValue);

                //时间戳
                String timestampColumnName = "sale_time";
                String timestampColumnValue = DateUtils.getDateStrFromSegment("2012-01-01", "2014-01-01", "yyyy-MM-dd");//DateUtils.getDate(new Date(), "yyyy-MM-dd HH:mm:ss");

                column.put(timestampColumnName, timestampColumnValue);


                factTableRowList.add(column);
            }

            ORMDataBean factTableOrmDataBean = new ORMDataBean();

            factTableOrmDataBean.setTableName("t_hk_sales");
            factTableOrmDataBean.setRowList(factTableRowList);

            //生成hbase表数据
            hBaseDataService.createTestDataToHBaseService(factTableOrmDataBean, true);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    /**
     * 插入数据
     */
    @RequestMapping("/insert/{tableName}")
    public void insert(@PathVariable("tableName")String tableNameValue)
    {
        List<HashMap> sqlList = new ArrayList<HashMap>();

        HashMap m1 = new HashMap<String, String>();
        m1.put("V_ba_tree", "SELECT * FROM R8DB.DBO.V_ba_tree where tree_Code = 'tr01'");
        sqlList.add(m1);

        //仓库
        HashMap m2 = new HashMap<String, String>();
        m2.put("loc_mstr", "select * from r6erp.dbo.loc_mstr where loc_virtual ='0' and loc_hkdb = 'HK'");
        sqlList.add(m2);

        for (int sqlIndex = 0; sqlIndex < sqlList.size(); sqlIndex++)
        {
            HashMap<String, String> sqlMap = sqlList.get(sqlIndex);

            for(Map.Entry<String, String> entry : sqlMap.entrySet())
            {
                String tableName = entry.getKey();
                String sqlValue = entry.getValue();

                ORMDataBean ormDataBean = sqlServerDataService.query(sqlValue);

                ormDataBean.setTableName(tableName);

                try
                {
                    //先检查表
                    hBaseDataService.createTable(tableName);

                    //先清空表数据
                    hBaseDataService.truncateTable(tableName);

                    hBaseDataService.insert(ormDataBean);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping("/clear/{tableName}")
    public void cleanTable(@PathVariable("tableName")String tableNameValue)
    {
        try
        {
            //先清空表数据
            hBaseDataService.truncateTable(tableNameValue);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    /**
     * 仅供测试使用
     */
    @RequestMapping("/dimesionTableTest")
    public void insertDimesionTabelDataTest()
    {
        //客户维度数据
        if (!beanMap.containsKey("sceo_dimesion_depttree")) {
            ORMDataBean customOrmDataBean=sqlServerDataService.queryTreeDataToMapService("sceo_dimesion_depttree", "2", 1, "custom_tret_id");
            hBaseDataService.createTestDataToHBaseService(customOrmDataBean);
            beanMap.put("sceo_dimesion_depttree", customOrmDataBean);
        }
    }

    /**
     * 仅供测试使用
     */
    @RequestMapping("/factTableTest")
    public void insertTestDataTest()
    {
        try
        {
            //客户维度数据，一层结构
            ORMDataBean customOrmDataBean = hBaseDataService.queryTable("sceo_dimesion_depttree", "cf", new String[]{PK_COLUMN_NAME});

            //生成实时表
            List factTableRowList = new ArrayList();

            for(int index = 0; index < 10; index++)
            {
                Map column = new HashMap();

                // ********************* 客户列 *********************
                String fkCustomColumnName = "fk_custom";

                // 客户列值
                String fkCustomColumnValue = radomDataService.radomDimesionDataService(customOrmDataBean, PK_COLUMN_NAME);

                column.put(fkCustomColumnName, fkCustomColumnValue);

                // *********************  周期 cycle time *********************
                String cycleTimeEmployColumnName = "cycle_time";

                //
                String cycleTimeEmployColumnValue = "月";

                column.put(cycleTimeEmployColumnName, cycleTimeEmployColumnValue);

                // *********************  财年 fiscal year *********************
                String fiscalYearEmployColumnName = "fiscal_year";

                //
                String fiscalYearEmployColumnValue = "2017";

                column.put(fiscalYearEmployColumnName, fiscalYearEmployColumnValue);

                // *********************  度量值，销售额 *********************
                String dalesColumnName = "sales_value";
                String salesValue = String.valueOf(new DecimalFormat("#.00").format(1000 + Math.random() * 10000));

                column.put(dalesColumnName, salesValue);

                //时间戳
                String timestampColumnName = "sale_time";
                String timestampColumnValue = DateUtils.getDateStrFromSegment("2012-01-01", "2014-01-01", "yyyy-MM-dd");//DateUtils.getDate(new Date(), "yyyy-MM-dd HH:mm:ss");

                column.put(timestampColumnName, timestampColumnValue);


                factTableRowList.add(column);
            }

            ORMDataBean factTableOrmDataBean = new ORMDataBean();

            factTableOrmDataBean.setTableName("sceo_fact_sales");
            factTableOrmDataBean.setRowList(factTableRowList);

            //生成hbase表数据
            hBaseDataService.createTestDataToHBaseService(factTableOrmDataBean, true);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @RequestMapping("/queryTest")
    public void query()
    {
        hBaseDataService.queryRowByFilter();



    }




}
