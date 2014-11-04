package com.onto.user.db;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;
import com.onto.mongo.control.Dbcontrol;

public class UserAccessControlBack {

	
	public ConRelAdmin cra;
	public Dbcontrol dbc;
	DB db;
	static DBCollection colUser;
	static DBCollection colAdmin;
	
	/**
	 * 构造函数
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	public UserAccessControlBack() throws UnknownHostException, MongoException{
		
		dbc=new Dbcontrol();
		db=dbc.creatDb("user");
		colUser=dbc.creatTable(db, "ColUser");
		colAdmin=dbc.creatTable(db, "ColAdmin");
		
	}
	
	
	/**
	 * 增加一个用户   注意用户名不能修改
	 * @param username
	 * @param col
	 */
	public void addUserName(String username,DBCollection col ){
		String[] userStr=new String[1];
		userStr[0]=username;
		dbc.addDocument("username", userStr, col);
		for(int i=1001;i<2000;i++){
			dbc.addKeyandValueArray("username", userStr, "idpool", String.valueOf(i), col);//目的是建立一个oid库，用户的本体id号从这个库来取，如果删除一个本体，则把id号放回。
		}
		
		
	}
	
	/**
	 * 删除一个用户
	 * @param username
	 * @param col
	 */
	public void delUserName(String username,DBCollection col ){
		String[] userStr=new String[1];
		userStr[0]=username;
		dbc.delDocument("username", userStr, col);
		
		
	}
	
	
	/**
	 *  username password  myontos owlontos shareontos  
	 * 为用户增加信息，如增加密码，增加本体名等。
	 * @param username
	 * @param newkeyStr
	 * @param newvalStr
	 * @param col
	 */
	public void addUserInfo(String username,String newkeyStr,String newvalStr,DBCollection col ){
		String[] userStr=new String[1];
		userStr[0]=username;
		
		dbc.addKeyandValueArray("username", userStr, newkeyStr, newvalStr, col);
		
		
	}
	
	/**
	 * 增加用户的本体，有自己构建的本体， owl本体 还有共享本体，都有本体id和本体名。
	 * @param username
	 * @param ontoKindStr
	 * @param ontoNameStr
	 * @param col
	 */
	public void addUserOntos(String username,String ontoKindStr,String ontoNameStr,DBCollection col ){
		String[] userStr=new String[1];
		userStr[0]=username;
		String newval1Str=this.getOid(username, colUser);
		dbc.addKeyandValueDocArray("username", userStr, ontoKindStr, newval1Str, ontoNameStr, col);
		
		
	}
	
	/**
	 * 从数据库中将onto的oid ontoname对应的文档全部拿出来
	 * @param username
	 * @param ontoKindStr
	 * @param col
	 */
	public ArrayList<BasicDBObject> getUserOntos(String username,String ontoKindStr,DBCollection col ){
		ArrayList<BasicDBObject>  redoc=new ArrayList<BasicDBObject>();
		String[] userStr=new String[1];
		userStr[0]=username;
		redoc=dbc.getKeyandValueDocArray("username", userStr, ontoKindStr, col);
		return redoc;
		
	}
	
	
	/**
	 * 给定一个本体的id或name 得到其name或id
	 * @param username
	 * @param ontoKindStr
	 * @param aimonto
	 * @param col
	 * @return
	 */
	public String getUserOntosOidorName(String username,String ontoKindStr,String oidorname,String valofOidorName,DBCollection col ){
		String[] userStr=new String[1];
		userStr[0]=username;
		
		String value=dbc.findKeyandValueDocArray("username", userStr, ontoKindStr, oidorname, valofOidorName, col);
		
		return value;
		
	}
	
	public void updateUserOntos(String username,String ontoKindStr,String ontoNameStr,DBCollection col ){
		String[] userStr=new String[1];
		userStr[0]=username;
		String newval1Str=this.getOid(username, colUser);
		dbc.addKeyandValueDocArray("username", userStr, ontoKindStr, newval1Str, ontoNameStr, col);
		
		
	}
	
	/*
	 * 从用户的ID号里面，取出一个oid号。作为他的本体的id号，注意这里区分一下这几种本体，如果是我的本体则前面加1，如果是OWL本体，则前面加2，如果是共享本体，则用户名+3+id号
	 * 
	 */
	public String getOid(String username,DBCollection col){
		String[] userStr=new String[1];
		userStr[0]=username;
		ArrayList<String> idlist;
		idlist=dbc.getValueArrayofKey("username", userStr, "idpool", col);
		String oid=idlist.get(0);
		dbc.delOneofKeyandValueArray("username", userStr, "idpool",oid,col);
		System.out.println("oid:"+oid);		
		return oid;
	}
	
	/**
	 * 将删掉的本体的id号放回idpool
	 * @param username
	 * @param oid
	 * @param col
	 */
	public void putOid(String username,String oid,DBCollection col){
		String[] userStr=new String[1];
		userStr[0]=username;
				
		dbc.addKeyandValueArray("username", userStr, "idpool", oid, col);
	}
	
	
	
	/**
	 * 获取用户的信息，用户名和密码 email
	 * @param username
	 * @param newkeyStr
	 * @param newvalStr
	 * @param col
	 * @return
	 */
	public ArrayList<String> getUserInfo(String username,String newkeyStr,String newvalStr,DBCollection col ){
		ArrayList<String> valueArray=new ArrayList<String>();
		String[] userStr=new String[1];
		userStr[0]=username;
		valueArray=dbc.getValueArrayofKey("username", userStr, newkeyStr, col);
		
		return valueArray;
		
	}
	
	
	/**
	 * 
	 * @param username
	 * @param updatekeyStr
	 * @param oldvalStr
	 * @param newvalStr
	 * @param col
	 */
	public void UpdateUserInfo(String username,String updatekeyStr,String oldvalStr,String newvalStr,DBCollection col ){
		String[] userStr=new String[1];
		userStr[0]=username;
		dbc.updateKeyandValue("username", userStr, updatekeyStr, oldvalStr, newvalStr, col);
	}
	
	
	
	
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub

		UserAccessControlBack uacb=new UserAccessControlBack();
//		uacb.addUserName("bbb", colUser);
//		uacb.addUserInfo("zlzhou", "password", "123456", colUser);
//		uacb.UpdateUserInfo("zlzhou", "password", "123456", "111", colUser);
//		uacb.getOid("bbb", colUser);
		uacb.putOid("bbb", "10001", colUser);
		System.out.println("okkkk!");
		
		
	}

}
