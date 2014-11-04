package com.onto.user.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.onto.front.action.TreeModel;
import com.onto.mongo.control.Dbcontrol;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class UserFront extends ActionSupport {

	private static String username;
	private String password;
	private String email;
	
	private String ontoname;
	private String ontomodel;
	
	
	private String id;//树
	private String text;
	private List<TreeModel> userOntoTreeData;//向 前台传递概念树数据参数
	private String ontoKindStr;
	
	
	DB db;
	public Dbcontrol dbc;
	UserAccessControlBack uacb;
	static DBCollection colUser;
	static DBCollection colAdmin;
	
	public UserFront() throws UnknownHostException, MongoException{
		uacb=new UserAccessControlBack();
		ontoKindStr="myonto";
		dbc=new Dbcontrol();
		db=dbc.creatDb("user");
		colUser=dbc.creatTable(db, "ColUser");
		colAdmin=dbc.creatTable(db, "ColAdmin");
		
	}
	
	/**
	 * 增加一个用户
	 * @return
	 * @throws Exception
	 */
	public String adduser() throws Exception{
		PrintWriter out=ServletActionContext.getResponse().getWriter();
		uacb=new UserAccessControlBack();
		String[] userStr=new String[1];
		userStr[0]=username;
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		System.out.println("email:" + email);
		if(dbc.findDocument("username",userStr, colUser).count() ==0){
		uacb.addUserName(username, colUser);
		uacb.addUserInfo(username, "password", password, colUser);
		uacb.addUserInfo(username, "email", email, colUser);
		out.write("{success:true}");
		out.close();		
		return SUCCESS;}
		else return "failure";
	}
	
	
	/**
	 * 用户登录action 检验输入的用户名和密码是否匹配，如果匹配则返回success
	 * @return
	 * @throws Exception
	 */
	public String  userlogin() throws Exception{
		PrintWriter out=ServletActionContext.getResponse().getWriter();
		String[] userStr=new String[1];
		userStr[0]=username;
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		ArrayList<String> pwd=new ArrayList<String>();
		pwd=dbc.getValueArrayofKey("username", userStr, "password", colUser);
		if (pwd.get(0).equals(password)){
			System.out.println("ok!!!!");
			out.write("{success:true}");
			out.close();
			return SUCCESS;
		//	return "success";
		}else {
			System.out.println("no!!!!");
			out.write("{failure:true}");
			out.close();
			return "failure";
		
		}
		
		
		
		
	}
	
	/**
	 * 将某个用户的数据组织成前台需要的数据
	 * @param username
	 * @param ontoKindStr
	 * @param col
	 * @return
	 */
	public List<TreeModel> covertDbtoFront(String username,String ontoKindStr,DBCollection  col){
		List<TreeModel> list=new ArrayList<TreeModel>();
		
		ArrayList<BasicDBObject>  redoc=new ArrayList<BasicDBObject>();
		
		redoc=uacb.getUserOntos(username, ontoKindStr, col);
		for(int i=0;i<redoc.size();i++){
			TreeModel tm=new TreeModel();
			tm.setId(redoc.get(i).getString("oid"));
			tm.setText(redoc.get(i).getString("ontoname"));
			tm.setLeaf(true);
			
			list.add(tm);
		}
		
		
		System.out.println("onto:"+list);
		return list;
		
	}
	
	
	
public String conMyontoTreeDataForFront() {
		
		//System.out.println("ok1"+text);
		/*System.out.println(text);
		System.out.println(id);
		System.out.println(newConTerm);*/
		System.out.println(ontoKindStr);
		System.out.println(username);
		userOntoTreeData=new ArrayList<TreeModel>();
		//System.out.println("浏览概念传来的参数:   "+id);
		try {
//			userOntoTreeData=this.covertDbtoFront(username, ontoKindStr, colUser);
			userOntoTreeData=this.covertDbtoFront(username, ontoKindStr, colUser);
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	    this.setUserOntoTreeData(userOntoTreeData);
	    return SUCCESS;
	    
	 
	    /*String menuString=new String();
	    try {
			menuString=JSONUtil.serialize(menus);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ok2"+menuString);
		*/
		
		
	}
	


 public String createOnto(){
	 
	 System.out.println("ontoname:  "+ontoname);
	 System.out.println("ontomodel:  "+ontomodel);
	 
	 return SUCCESS; 
 }
	
	public String getOntoKindStr() {
		return ontoKindStr;
	}

	public void setOntoKindStr(String ontoKindStr) {
		this.ontoKindStr = ontoKindStr;
	}

	

	public List<TreeModel> getUserOntoTreeData() {
		return userOntoTreeData;
	}

	public void setUserOntoTreeData(List<TreeModel> userOntoTreeData) {
		this.userOntoTreeData = userOntoTreeData;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}




	public String getOntoname() {
		return ontoname;
	}

	public void setOntoname(String ontoname) {
		this.ontoname = ontoname;
	}

	public String getOntomodel() {
		return ontomodel;
	}

	public void setOntomodel(String ontomodel) {
		this.ontomodel = ontomodel;
	}

	public void setUsername(String username) {
		this.username = username;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub

		UserFront uf=new UserFront();
		try {
	//		uf.userlogin();
	//		uf.adduser();
			uf.conMyontoTreeDataForFront();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
