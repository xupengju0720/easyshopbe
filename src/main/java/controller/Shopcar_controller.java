package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.interceptors.SessionAssociationInterceptor;

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
import util.serialnumber;

@Controller
public class Shopcar_controller {
	
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
	@Autowired
	Shopcar_service service;
	@RequestMapping("ShoppingCarindex") //���ﳵ��Ʒ����
	public String  index(ModelMap m,Integer id,SearchInfo info) {
	m.put("carlist", service.getById(id));
	List<Shopcar> tt = (List<Shopcar>) service.getById(id);
	 ArrayList<Product> pp = (ArrayList<Product>) service6.select(info);
	m.put("productlist", pp);
	return "ShoppingCar";
	}
	
	@RequestMapping("ShoppingCardel") //ɾ�����ﳵ
	public @ResponseBody jsonInfo ShoppingCardel(Shopcar sho){
		service.delete(sho.getId());
		return new jsonInfo(1,"");
		
	}
	@RequestMapping("changecount") //�ı乺�ﳵ����
	public @ResponseBody jsonInfo changecount(Shopcar sho){
		service.update(sho);
		return new jsonInfo(1,"");
		
	}
	@RequestMapping("shoppingcaradd") //���빺�ﳵ
	public @ResponseBody jsonInfo ShoppingCaradd(Shopcar sho,HttpSession session){
		Shopcar ss = new Shopcar();
		User user =(User) session.getAttribute("sdetail");
		ss.setProduct_id(sho.getProduct_id());
		ss.setUser_id(user.getId());
		if (service.addcount(ss)==null) {
			sho.setUser_id(user.getId());
			service.insert(sho);
		}else {
			Integer pp = service.addcount(ss).getCount()+sho.getCount();
			ss.setCount(pp);
			service.update(ss);
		}
		return new jsonInfo(1,""); 
		
	}
	@RequestMapping("payorders")   //����ҳ δ֧����֧����ת  
	public String payorders(Ids i,int id,ModelMap m,HttpSession session) {
		User add = (User) session.getAttribute("sdetail");
		Integer k =add.getId();
		m.put("addlist",service5.addressall(k));
		m.put("orders_id", id);
		String  pids= service1.onlyids(id).toString().substring(1, service1.onlyids(id).toString().length()-1);
		i.setIds(pids);
		m.put("choicelist", service.selectproductid(i));  //������Ʒid��
		return "Addresschoice/index";  
	}
	@RequestMapping("selectCar")   //��session�л�ȡ��ǰ�û�id  ������е�ַ    �������ѡ��Ʒ
	public String selectCar(Ids ids,ModelMap m,HttpSession session) {
	     m.put("choicelist", service.select(ids));   //���ݹ��ﳵid��
		User add = (User) session.getAttribute("sdetail");
		Integer id =add.getId();
		m.put("addlist",service5.addressall(id));
		Double amount=0.0;
		Double nowamount=0.0;
		Orders_details ordd =new Orders_details();
		Orders_status ords = new Orders_status();
		Orders ord =new Orders();
		serialnumber pp = new serialnumber();
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
		ord.setUser_id(userid);
		ord.setAddress_id(0);
		ord.setAssessstatus(0);
		ord.setCode(pp.Getnum());
		ord.setDate(date);
		ord.setStatus(0);
		ord.setComments("��ע");
		ord.setAmount(amount);
		ord.setNowamount(nowamount);
		service1.insert(ord);        //���붩��

		int orders_id= service1.getMaxid(userid);//��ȡָ���û�����󶩵�id
		m.put("orders_id", orders_id);
		ords.setOrders_id(orders_id);
		ords.setDate(date);
		ords.setDest_status(0);
		ords.setInfo("������");
		ords.setNum(0);
	    ords.setAmount(amount);	
		ords.setComments("0");
		service2.insert(ords);     //���붩��״̬
		
		Product product = new Product();
		 for (int i = 0; i < aa.size(); i++) {
		Double damount=(Double)(aa.get(i).getCount()*aa.get(i).getProduct_price());
		Double dnowamount=(Double)(aa.get(i).getCount()*aa.get(i).getProduct_nowprice());
		ordd.setOrders_id(orders_id);
		ordd.setProduct_id(aa.get(i).getProduct_id());
		ordd.setCount(aa.get(i).getCount());
		ordd.setPrice(damount);
		ordd.setNowprice(dnowamount);
	    ordd.setComments("δ����");
	    service3.insert(ordd);     //���붩������
		}
		return "Addresschoice/index";
	}
	@RequestMapping("ShoppingCardelete")//���붩����ɾ�����ﳵ�ж�Ӧ����Ʒ
	public String ShoppingCardel(HttpSession session,Ids ids) {
		service.deleteids(ids);
		return "GR";
		
	}
	
}
