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
	 * ���캯��
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
	 * ����һ���û�   ע���û��������޸�
	 * @param username
	 * @param col
	 */
	public void addUserName(String username,DBCollection col ){
		String[] userStr=new String[1];
		userStr[0]=username;
		dbc.addDocument("username", userStr, col);
		for(int i=1001;i<2000;i++){
			dbc.addKeyandValueArray("username", userStr, "idpool", String.valueOf(i), col);//Ŀ���ǽ���һ��oid�⣬�û��ı���id�Ŵ��������ȡ�����ɾ��һ�����壬���id�ŷŻء�
		}
		
		
	}
	
	/**
	 * ɾ��һ���û�
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
	 * Ϊ�û�������Ϣ�����������룬���ӱ������ȡ�
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
	 * �����û��ı��壬���Լ������ı��壬 owl���� ���й����壬���б���id�ͱ�������
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
	 * �����ݿ��н�onto��oid ontoname��Ӧ���ĵ�ȫ���ó���
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
	 * ����һ�������id��name �õ���name��id
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
	 * ���û���ID�����棬ȡ��һ��oid�š���Ϊ���ı����id�ţ�ע����������һ���⼸�ֱ��壬������ҵı�����ǰ���1�������OWL���壬��ǰ���2������ǹ����壬���û���+3+id��
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
	 * ��ɾ���ı����id�ŷŻ�idpool
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
	 * ��ȡ�û�����Ϣ���û��������� email
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
