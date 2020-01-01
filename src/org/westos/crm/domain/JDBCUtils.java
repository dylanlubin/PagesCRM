package org.westos.crm.domain;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: ShenMouMou
 * @CreateTime: 2019-09-14 15:36
 * @Description:西部开源教育科技有限公司
 */
public class JDBCUtils {

    public static DataSource getDataSource() throws Exception {
        InputStream in = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
        Properties properties = new Properties();
        properties.load(in);
        DataSource ds = new DruidDataSourceFactory().createDataSource(properties);
        return ds;
    }
}
