package org.westos.crm.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.westos.crm.domain.Customer;
import org.westos.crm.domain.JDBCUtils;
import org.westos.crm.domain.PageBean;
import org.westos.crm.domain.User;
import org.westos.crm.utils.MD5Utils;
import org.westos.crm.utils.UUIDUtils;

import javax.servlet.http.Cookie;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Properties;


public class CustomerDao {

    //查询所有条目，并储存在分页数据中；
    public List<Customer> findAllCustomer(PageBean pageBean) throws Exception {
        String sql1 = "select * from customer limit ?,?";
        String sql2 = "select count(*) from customer";
        List<Customer> list = findUserLimt(pageBean.getCurrentPage(), pageBean.getPageCount(), sql1);
        //填充了总记录数
        int sum=findAllItem(sql2);
        pageBean.setTotalCount(sum);
        return list;
    }

    //查询所有符合条件的条目，并储存在分页数据中；
    public List<Customer> queryAllCustomer(String customername, String phonenumber,PageBean pageBean) throws Exception {
        String sql = "select count(*) from customer where (cname like ?) and (telephone like ?)";
        String sql2="select * from customer where (cname like ?) and (telephone like ?) ";
        //填充了总记录数
        int sum=queryAllItem(sql,customername,phonenumber);
        pageBean.setTotalCount(sum);
        List<Customer> list = queryUserLimt(pageBean.getCurrentPage(), pageBean.getPageCount(),sql2,customername,phonenumber);
        return  list;

    }

    private List<Customer> findUserLimt(int currentPage, int pageCount,String sql1) throws Exception {
        DataSource ds = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(ds);
        int index = (currentPage - 1) * pageCount;
        List<Customer> list = queryRunner.query(sql1, new BeanListHandler<Customer>(Customer.class), index, pageCount);
        return list;
    }

    private static int findAllItem(String sql2) throws Exception {
        DataSource ds = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(ds);
        Long sum = (Long) queryRunner.query(sql2, new ScalarHandler());
        return sum.intValue();
    }
    private static int queryAllItem(String sql,String customername, String phonenumber) throws Exception {
        DataSource ds = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(ds);
        Long sum = (Long) queryRunner.query(sql, new ScalarHandler(),'%'+customername+'%','%'+phonenumber+'%');
        return sum.intValue();
    }
    private List<Customer> queryUserLimt(int currentPage, int pageCount,String sql,String customername, String phonenumber) throws Exception {
        DataSource ds = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(ds);
        List<Customer> list = queryRunner.query(sql, new BeanListHandler<Customer>(Customer.class),'%'+customername+'%','%'+phonenumber+'%');
        return list;
    }

    public boolean addCustomer(Customer customer) throws Exception {
        //查询数据库
        DataSource dataSource = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(dataSource);
        int i = queryRunner.update("insert into customer values(?,?,?,?,?,?,?)", customer.getCid(), customer.getCname(), customer.getAge(), customer.getGender(), customer.getEmail(), customer.getTelephone(), customer.getDescription());
        return i>0;
    }

    public boolean delCustomer(String cid) throws Exception {
        DataSource dataSource = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(dataSource);
        int i = queryRunner.update("delete from customer where cid=?", cid);
        return i>0;
    }

    public boolean delAllCustomer(String[] item_checkboxes) throws Exception {
        DataSource dataSource = JDBCUtils.getDataSource();
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("delete from customer where cid=?");
        for (String cid : item_checkboxes) {
            preparedStatement.setString(1,cid);
            preparedStatement.addBatch();
        }
        int[] ints = preparedStatement.executeBatch();
        preparedStatement.clearBatch();
        return ints.length>0;
    }

    public Customer preparedupdateCustomerInfo(String cid) throws Exception{
        DataSource dataSource = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(dataSource);
        Customer customer = queryRunner.query("select * from customer where cid=? ", new BeanHandler<Customer>(Customer.class),cid);
        System.out.println(customer.getCname());
        return customer;
    }

    public boolean updateCustomerInfo(Customer upcustomer,String ccid) throws Exception {

        DataSource dataSource = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(dataSource);
        int i = queryRunner.update("update customer set cname=?,age=?,gender=?,email=?,telephone=?,description=? where cid=?", upcustomer.getCname(), upcustomer.getAge(), upcustomer.getGender(), upcustomer.getEmail(), upcustomer.getTelephone(), upcustomer.getDescription(),ccid);
        return i>0;
    }

}
