package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.jdbc.core.ParameterMapper;

public class serialnumber {
	
	  public static String getStringDate() {
		     Date currentTime = new Date();
		     SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		     String dateString = formatter.format(currentTime);
		     return dateString;
		  }
	
	  public static String Getnum(){
		  String t = getStringDate();
		  int x=(int)(Math.random()*900)+100;
		  String serial = t + x;
		  return serial;
	  }
}