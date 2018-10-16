package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import entity.Product;
import service.Product_service;
import service.Type_service;
import util.SearchInfo;
import util.jsonInfo;

@Controller
public class Product_contoller {
	@Autowired
	Product_service service;
	@Autowired
	Type_service sservice;
	@RequestMapping("index")
	public String  index(ModelMap m,SearchInfo info) {
		m.put("plist",service.select(info));
		m.put("list",sservice.select(info));
		return "index";
	}
	
	@RequestMapping("insert")
	protected @ResponseBody jsonInfo insert(Product p) {
		service.insert(p);
		return new jsonInfo(1, "");
	}
	@RequestMapping("add")
	protected String add(ModelMap m) {
		m.put("status",Product.statuss);
		return "Product/edit";
	}
	@RequestMapping("update")
	protected @ResponseBody jsonInfo update(Product p) { // ����ֵ��jsonInfo���ڵ�������ɵ�json�ַ���{"status":"����ֵ","text":"����ֵ"}
		service.update(p);
		return new jsonInfo(1, "");
	}

	@RequestMapping("edit")
	protected String edit(ModelMap m, int id) {
		m.put("info", service.getById(id));
		return add(m);
	}
	
	@RequestMapping("delete")
	protected String delete(int id) {
		service.delete(id);
		return "redirect:index";
	}
	@RequestMapping("index1")  //����
	public String  index1(ModelMap m,SearchInfo info,String txt,int id) {
		m.put("plist",service.getById(id));
		return "XQ";
	}
	
	
	@RequestMapping("Product")//����
	public String  Product(ModelMap m,SearchInfo info,String txt) {
     	String where = "";
		if (txt == null)
			txt = "";
		if (txt != null && txt.length() > 0) {
			where = " where product.fullname like '%" + txt + "%' ";
		}
		info.setWhere(where);
		m.put("pplist",service.select(info));
		return "MZ";
	}
	@RequestMapping("producttype")//��Ʒ���Ͳ�ѯֻ�����һ��
	public String  producttype(int id,ModelMap m) {
	    m.put("pplist",sservice.producttype(id));
		return "MZ";
	}
	@RequestMapping("producttype1")//��Ʒ���Ͳ�ѯ  �����ڶ���
	public String  producttype1(int id,ModelMap m) {
		 m.put("pplist",sservice.producttype1(id));
		return "MZ";
	}
}
