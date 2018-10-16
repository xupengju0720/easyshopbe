package dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import entity.Orders_details;
import entity.Orders_status;

@Repository
public interface Orders_details_dao {

@Select("select *from orders_details ")
public List<Orders_details>  select();
@Update("update orders_details set price=#{price}, nowprice=#{nowprice} ,orders_id=#{orders_id},product_id=#{product_id},comments=#{comments},count={#count} where id=#{id}")
public void update(Orders_details ops);
@Select("select*from orders where id=#{id}")
public Orders_details getById(int id);

@Update("update orders set status=#{dest_status} where id=#{orders_id}")
public void update1(Orders_status ops);
@Insert("insert into oredrs_details(orders_id,product_id,count,price,nowprice,comments) values(#{orders_id},#{product_id},#{count},#{price},#{nowprice},#{comments})")
public void insert(Orders_details ops);
@Update("update oredrs_details set comments='�û���ɾ������' where orders_id=#{id}")
public void delete(int id);
@Update("update oredrs_details set comments='�Ѹ���' where orders_id=#{id}")
public void commentupdate(int id);

}
