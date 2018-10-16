package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import entity.Address;
import entity.MD5;
import entity.User;
import entity.pass;
import service.Address_service;
import service.User_service;
import util.SearchInfo;
import util.jsonInfo;

@Controller
public class User_Controller {
	@Autowired
	User_service service;
	@Autowired
	Address_service service2;
	@RequestMapping("Userindex")
	public void index1(ModelMap m, String txt, SearchInfo info) {

		String where = "";
		if (txt == null)
			txt = "";
		if (txt != null && txt.length() > 0) {
			where = " where User.name like '%" + txt + "%' ";
		}
		Integer count = service.select1(info);
		info.setCount(count);
		info.pagemax();
		info.setWhere(where);
		m.put("search", info);
		m.put("list", service.select(info));
	}
	@RequestMapping("updatepassword")  //�޸�����
	protected @ResponseBody jsonInfo update1(pass pp,HttpSession session) { // ����ֵ��jsonInfo���ڵ�������ɵ�json�ַ���{"status":"����ֵ","text":"����ֵ"}
		if (pp.getPassword2().equals(pp.getPassword3())) {
			User User=(User)session.getAttribute("sdetail");
			if (User.getPassword().equals(MD5.MD5(pp.getPassword1()))) {
				service.update1(pp);
			    User op= service.getById(((User)session.getAttribute("sdetail")).getId());
				session.setAttribute("sdetail",op);
			}else {
				return  new jsonInfo(-1, "ԭ�������");
			}
		}else {
			return  new jsonInfo(-1, "�������벻һ��");
		}
		return new jsonInfo(1, "");
	}
	@RequestMapping("newone")
	public String newone(ModelMap m,User op) {
		service.insert(op);
		return "login";
	}
@RequestMapping("addressinsert")//������ַ
public String addressadd(Address add,HttpSession session) {
	User p = (User) session.getAttribute("sdetail");// ������ַ
	add.setId(p.getId());
	service2.Insert(add);
	return "redirect:addressselect";
}

@RequestMapping("addressadd")//������ַ
public String addressadd() {
	
	return "GrAddress/Ordersover";
}
@RequestMapping("addressselect")
public String addressselect(HttpSession session,ModelMap m) {
	User p = (User) session.getAttribute("sdetail");    //��ѯ��ǰ��¼�û������е�ַ 
	m.put("address",  service2.addressall(p.getId()));
	return "GrAddress/index";
}
@RequestMapping("addressdelete")
public String addressdelete(int id) {  //����ɾ����ַ
	service2.addressdelete(id);
	return "redirect:addressselect";
}



}
