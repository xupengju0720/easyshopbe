package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.aspectj.weaver.reflect.DeferredResolvedPointcutDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.jdbc.core.ParameterMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import entity.Address;
import entity.Orders;
import entity.Orders_details;
import entity.Orders_status;
import entity.Product;
import entity.Shopcar;
import entity.User;
import service.Address_service;
import service.Orders_details_service;
import service.Orders_service;
import service.Orders_status_service;
import service.Product_service;
import service.Shopcar_service;
import util.Ids;
import util.SearchInfo;
import util.jsonInfo;
import util.serialnumber;;
@Controller
public class Orders_controller {
@Autowired
Orders_service service1;
@Autowired
Orders_status_service service2;
@Autowired
Orders_details_service service3;
@Autowired
Shopcar_service service4;
@Autowired
Address_service service5;
@Autowired
Product_service service6;
serialnumber pp = new serialnumber();
@RequestMapping("fukuan") //���ﳵ����
public String fukuan(Address add,HttpSession session,Integer addressid,Ids ids,int ordersid) {
	Double amount=0.0;
	Double nowamount=0.0;
	Orders_details ordd =new Orders_details();
	Orders_status ords = new Orders_status();
	Orders ord =new Orders();
	
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     String date = formatter.format(new Date());
     User user =(User) session.getAttribute("sdetail");//��ȡ�û���Ϣ
    Integer userid =user.getId();			//��ȡ�û�id
	List<Shopcar> aa = service4.select1(ids);  //������ﳵ  ����Ʒ�۸� 
    ArrayList<Integer> reids =new ArrayList<Integer>();
	 for (int i = 0; i < aa.size(); i++) {
		 amount+=(Double)(aa.get(i).getCount()*aa.get(i).getProduct_price());
		 nowamount+=(Double)(aa.get(i).getCount()*aa.get(i).getProduct_nowprice());
		 reids.add(aa.get(i).getId());
	}
	ord.setAddress_id(addressid);
	ord.setDate(date);
	ord.setStatus(1);
	service1.update(ord);        //�޸Ķ���

	ords.setOrders_id(ordersid);
	ords.setDate(date);
	ords.setDest_status(1);
	ords.setInfo("�Ѹ���");
	ords.setNum(0);
    ords.setAmount(amount);	
	ords.setComments("0");
	service2.insert(ords);     //���붩��״̬
	
	ordd.setComments("�Ѹ���");  //�޸Ķ�������
    service3.commentupdate(ordersid); 
	Product product = new Product();
	 for (int i = 0; i < aa.size(); i++) {
    Integer  sproductid = aa.get(i).getProduct_id();
    Integer  scount = service6.getById(sproductid).getSalecount()+aa.get(i).getCount(); 
    product.setId(sproductid);
    product.setSalecount(scount);
    service6.updatecount(product);   //��Ӧ��Ʒ  ��������
	}
	 String  pids = reids.toString().substring(1,reids.toString().length()-1);
return "redirect:ShoppingCardelete?ids="+pids;
}
@RequestMapping("Ordersselect")
public  String  Ordersselect(ModelMap m,HttpSession session) {
	User user = (User) session.getAttribute("sdetail");
	int id =user.getId();
	m.put("orderslistdetail",service1.select(id));
	m.put("orderslistsimple", service1.selectpp(id));
	return "Orderview/index";
}
@RequestMapping("deleteorders")   //�Ķ���״̬Ϊ10  �ͻ�������  
public @ResponseBody jsonInfo deleteorders(int id) {
	
    	service1.delete(id);//�Ķ���״̬Ϊ6
    	Orders_status ords=new Orders_status();
    	ords.setOrders_id(id);
    	ords.setDate(pp.Getnum());
    	ords.setDest_status(6);
    	ords.setInfo("�˶����ѱ��û�ɾ��");
    	ords.setNum(0);
        ords.setAmount(0.0);	
    	ords.setComments("0");
    	service2.insert(ords);
    	service3.delete(id);//�Ķ��������� ��עΪ  �ͻ�ȡ������
	return new jsonInfo(1,"");
}
@RequestMapping("buyproduct")     // ��������ж���״̬Ϊ0δ����  ������Ʒ  �Ķ���״̬Ϊ1  �����޸Ķ���״̬��  ʱ���״̬
public @ResponseBody jsonInfo buyproduct(int id) {
	service1.buyproduct(id);
	Orders_status odes = new Orders_status();
	 odes.setDate( serialnumber.getStringDate());
	 odes.setDest_status(1);
return new jsonInfo(1,"");
}
@RequestMapping("takegoods")     //ȷ���ջ�  �Ķ���״̬Ϊ3���ջ� ������������״̬��  ʱ���״̬
public @ResponseBody jsonInfo takegoods(int id) {
	 service1.takegoods(id);     
	 Orders_status odes = new Orders_status();
	 SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
	String date =d1.format(new Date());
	 odes.setDate(date);
	 odes.setDest_status(3);
	 odes.setOrders_id(id);
	 odes.setNum(0);
	 odes.setInfo("�Ѿ��ջ�");
	 odes.setAmount(0.0);
	 odes.setComments("0");
	 service2.insert(odes);
	return new jsonInfo(1,"");
}
@RequestMapping("payattion")     //�ͻ����ѷ���   �Ķ�����ע
public @ResponseBody jsonInfo payattion(int id) {
	service1.payattion(id);
	return new jsonInfo(1,"");
}
@RequestMapping("orderscancel")     //����ȡ������  �Ķ�����״̬Ϊ4  �����ڶ�����������
public String  orderscancel(int id,ModelMap m) {
	 SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
		String date =d1.format(new Date());
	m.put("orders_id", id);
	m.put("dest_status", 4);
	m.put("date",date );
	m.put("info", "�˻�����");
	m.put("nowamount",service1.getById(id).getNowamount());
	return "Orderview/edit2";
}
@RequestMapping("insertstatus")
protected @ResponseBody jsonInfo insertstatus(Orders_status ords) { 
	service1.update1(ords);
	service2.insert(ords);
	return new jsonInfo(1,"");
}
}
