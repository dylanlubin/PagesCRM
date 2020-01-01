package org.westos.crm.web;

import org.westos.crm.domain.Customer;
import org.westos.crm.domain.PageBean;
import org.westos.crm.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FuzzyQueryServlet",value = "/queryall")
public class FuzzyQueryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            String customername = request.getParameter("customername");
            String phonenumber = request.getParameter("phonenumber");

            int pageNUM=1;
            String currentPage = request.getParameter("currentPage");
            if (currentPage!=null){
                pageNUM=Integer.parseInt(currentPage);
            }else {
                pageNUM=1;
            }
            PageBean pageBean = new PageBean();
            pageBean.setCurrentPage(pageNUM);

            List<Customer> list =new CustomerService().queryCustomer(customername,phonenumber,pageBean);
            if(list.size()>0){
                //调到content.jsp
                request.setAttribute("customername",customername);
                request.setAttribute("phonenumber",phonenumber);
                pageBean.setPageData(list);
                request.setAttribute("pagesBean",pageBean);
                request.getRequestDispatcher("/likecontent.jsp").forward(request, response);
            }else{
                request.setAttribute("msg", "没有查询到任何客户");
                request.getRequestDispatcher("/likecontent.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
