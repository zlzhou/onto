package com.onto.mongo.control;

/**********************************************************
* ��MongoDb���ݿ���в���
* @author ������
* @version 1.01��2013��12��18��
* @since ��
**********************************************************/


import java.awt.List;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.bson.BSONObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.onto.interf.Interf;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

	public  class Dbcontrol implements Interf {

	/**
	 * 
	 * @param str
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	@SuppressWarnings("deprecation")
	public DB creatDb(String str) throws UnknownHostException, MongoException {
		
		Mongo mg = new Mongo();
	    
		// ��ȡ��Ϊ����str�����ݿ����
	    DB db = mg.getDB(str);
		
	    return db;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	@SuppressWarnings("deprecation")
	public  DB openDb(String str) throws UnknownHostException, MongoException {
		
		Mongo mg = new Mongo();
	   
	    // ��ȡ��Ϊ����str�����ݿ����
	    DB db = mg.getDB(str);
		
	    return db;
	}
	
	
	/**
	 * 
	 * @param str
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	@SuppressWarnings("deprecation")
	public void delDb(String str) throws UnknownHostException, MongoException {
		
		Mongo mg = new Mongo();
	    
	    // ��ȡ��Ϊ����str�����ݿ����
	    DB db = mg.getDB(str);
		
	    db.dropDatabase();
	}
	
	
	/**
	 * 
	 * @param db
	 * @param tableStr
	 * @return
	 */
	public DBCollection creatTable(DB db,String tableStr){
		
		DBCollection collection=db.getCollection(tableStr);
		
		//BasicDBObject doc = new BasicDBObject();
	    //doc.put("name", "jason");
	    //doc.put("sex", "women");
	    //doc.put("count", 1);
	    //collection.insert(doc);
		return collection;
				
	}
	
	
	/**
	 * 
	 * @param db
	 * @param tableStr
	 * @return
	 */
	public DBCollection openTable(DB db,String tableStr){
		
		DBCollection collection=db.getCollection(tableStr);
		
		//BasicDBObject doc = new BasicDBObject();
	    //doc.put("name", "jason");
	    //doc.put("sex", "women");
	    //doc.put("count", 1);
	    //collection.insert(doc);
		return collection;
				
	}
	
	/**
	 * 
	 * @param db
	 * @param tableStr
	 */
	public void delTable(DB db,String tableStr){
		
		DBCollection collection=db.getCollection(tableStr);
		
		collection.drop();
		
		//BasicDBObject doc = new BasicDBObject();
	    //doc.put("name", "jason");
	    //doc.put("sex", "women");
	    //doc.put("count", 1);
	    //collection.insert(doc);
		
				
	}
	
	/**
	 * 
	 * @param doc
	 * @param col
	 * @return
	 */
	public BasicDBObject addDocument(BasicDBObject doc,DBCollection col){
		
		col.insert(doc);
		
		return doc;
	}
	
	/**
	 * 
	 * @param keyStr
	 * @param valStr
	 * @param col
	 * @return
	 */
	public BasicDBObject addDocument(String keyStr,String[] valStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		col.insert(doc);
		
		return doc;
	}
	
	/**
	 * 
	 * @param keyStr
	 * @param valStr
	 * @param col
	 * @return
	 */
	public BasicDBObject delDocument(String keyStr,String[] valStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		col.remove(doc);
		
		return doc;
	}
	
	public BasicDBObject delDocument(String keyStr,String valStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		col.remove(doc);
		
		return doc;
	}
	
	/**
	 * 
	 * @param doc
	 * @param col
	 * @return
	 */
	public BasicDBObject delDocument(BasicDBObject doc,DBCollection col){
		
		col.remove(doc);
		
		return doc;
	}
	
	/**
	 * 
	 * @param doc
	 * @param col
	 * @return
	 */
	
	public DBCursor findDocument(BasicDBObject doc,DBCollection col){
		
		//BasicDBObject doc=new BasicDBObject();
		//doc.put(keyStr, valStr);
		DBCursor cur=col.find(doc);
		//col.findOne(doc);
		
		return cur;
	}
	
	/**
	 * 
	 * @param doc
	 * @param col
	 * @return
	 */
	
	public DBObject findODocument(BasicDBObject doc,DBCollection col){
		
		//BasicDBObject doc=new BasicDBObject();
		//doc.put(keyStr, valStr);
		DBObject cur=col.findOne(doc);
		//col.findOne(doc);
		
		return cur;
	}
	
	
	/**
	 * ����һ��ָ��
	 * @param keyStr
	 * @param valStr
	 * @param col
	 * @return
	 */
	
	public DBCursor findDocument(String keyStr,String[] valStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		DBCursor cur=col.find(doc);
		//col.findOne(doc);
		
		return cur;
	}
	
	/**
	 * ����һ��ָ��
	 * @param keyStr
	 * @param valStr
	 * @param col
	 * @return
	 */
	
	public DBObject findOneDocument(String keyStr,String[] valStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		DBObject cur=col.findOne(doc);
		//col.findOne(doc);
		
		return cur;
	}
	
	
	
	/**
	 * �ҵ�����ĳ�����ĵ�
	 * @param keyStr
	 * @param col
	 * @return
	 */
	
	public DBCursor findDocumentWithKey(String keyStr,DBCollection col){
		
		BasicDBObject doc1=new BasicDBObject();
		doc1.put("$exists", true);
		BasicDBObject doc2=new BasicDBObject();
		doc2.put(keyStr, doc1);
		DBCursor cur=col.find(doc2);
		//col.findOne(doc);
		
		return cur;
	}
	
	
	/**
	 * ���ɵļ��ͼ�ֵ�滻Ϊ�µļ��ͼ�ֵ
	 * @param keyStr
	 * @param valStr
	 * @param newkeyStr
	 * @param newvalStr
	 * @param col
	 * @return
	 */
	public DBObject replaceKeyandValue(String keyStr,String[] valStr,String newkeyStr,String newvalStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(newkeyStr, newvalStr);
		DBObject redoc=new BasicDBObject();
		redoc=col.findAndModify(doc, newdoc);
		return redoc;
		
	}
	
	/**
	 * ��������е�һ��Ԫ��
	 * @param keyStr
	 * @param valStr
	 * @param updatekeyStr
	 * @param oldvalStr
	 * @param newvalStr
	 * @param col
	 */
	public void updateKeyandValue(String keyStr,String[] valStr,String updatekeyStr,String oldvalStr,String newvalStr,DBCollection col){
		
		this.delOneofKeyandValueArray(keyStr, valStr, updatekeyStr, oldvalStr, col);
		this.addKeyandValueArray(keyStr, valStr, updatekeyStr, newvalStr, col);
		
				
	}
	
	/**
	 * �����û�������
	 * �����ĵ�������иü�����ֵ������¼�ֵ�����û�иü�����ֵ������
	 * @param keyStr
	 * @param valStr
	 * @param newkeyStr
	 * @param newvalStr
	 * @param col
	 * @return
	 */
public DBObject updateKeyandValue(String keyStr,String[] valStr,String newkeyStr,String newvalStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(newkeyStr, newvalStr);
		DBObject updatedoc=new BasicDBObject();
		updatedoc.put("$set", newdoc);
		col.update(doc, updatedoc);
		return updatedoc;
		
	}
	
	
	
	/**
	 * ���¼���Ҳ����˵�����ֶεķ�����
	 * @param keyStr
	 * @param valStr
	 * @param keyold
	 * @param keynew
	 * @param col
	 * @return
	 */
	public DBObject updateKey(String keyStr,String[] valStr,String keyold,String keynew,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(keyold, keynew);
		DBObject updatedoc=new BasicDBObject();
		updatedoc.put("$rename", newdoc);
		col.update(doc, updatedoc);
		return updatedoc;
		
	}
	
	
	
	/**
	 * ɾ��ָ���ļ�
	 * @param keyStr
	 * @param valStr
	 * @param newkeyStr
	 * @param newvalStr
	 * @param col
	 * @return
	 */
	public DBObject delKeyandValue(String keyStr,String[] valStr,String newkeyStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(newkeyStr, "");
		DBObject updatedoc=new BasicDBObject();
		updatedoc.put("$unset", newdoc);
		col.update(doc, updatedoc);
		return updatedoc;
		
	}
	
	
	/**��Լ�ֵΪ�����
	 * ���¼������һ������ֵ���ظ������
	 * @param keyStr
	 * @param valStr
	 * @param newkeyStr
	 * @param newvalStr
	 * @param col
	 * @return
	 */
	public DBObject addKeyandValueArray(String keyStr,String[] valStr,String newkeyStr,String newvalStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(newkeyStr, newvalStr);
		DBObject updatedoc=new BasicDBObject();
		updatedoc.put("$addToSet", newdoc);
		col.update(doc, updatedoc);
		return updatedoc;
		
	}
	
public DBObject addKeyandValueArray(String keyStr,String[] valStr,String newkeyStr,float val,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(newkeyStr, val);
		DBObject updatedoc=new BasicDBObject();
		updatedoc.put("$addToSet", newdoc);
		col.update(doc, updatedoc);
		return updatedoc;
		
	}
	
	
	/**
	 * �������������û��������������Ϊÿ�����嶼��ID�ź�Name ������Ҫ�����ĵ����ϣ��� "myonto":[{"oid":10001,"name":"onto1"},{}]
	 * @param keyStr
	 * @param valStr
	 * @param newkeyStr
	 * @param newval1Str
	 * @param newval2Str
	 * @param col
	 * @return
	 */
public DBObject addKeyandValueDocArray(String keyStr,String[] valStr,String newkeyStr,String newval1Str,String newval2Str,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc1=new BasicDBObject();
		newdoc1.put("oid", newval1Str);
		newdoc1.put("ontoname", newval2Str);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(newkeyStr, newdoc1);
		DBObject updatedoc=new BasicDBObject();
		updatedoc.put("$addToSet", newdoc);
		col.update(doc, updatedoc);
		return updatedoc;
		
	}


/**
 * ��user�û��л�ȡ��������ĵ�����"myonto":[{"oid":10001,"name":"onto1"},{}]�е�{"oid":10001,"name":"onto1"}
 * @param keyStr
 * @param valStr
 * @param aimKeyStr
 * @param col
 * @return
 */
@SuppressWarnings("unchecked")
public ArrayList<BasicDBObject> getKeyandValueDocArray(String keyStr,String[] valStr,String aimKeyStr,DBCollection col){
	
	BasicDBObject doc=new BasicDBObject();
	doc.put(keyStr, valStr);
	BasicDBObject aimdoc=new BasicDBObject();
	aimdoc.put(aimKeyStr, 1);
	ArrayList<BasicDBObject>  redoc=new ArrayList<BasicDBObject>();
	BasicDBObject fdoc=(BasicDBObject) col.findOne(doc,aimdoc);
	Object[] keyvalue=fdoc.values().toArray();
	redoc=(ArrayList<BasicDBObject>) keyvalue[1];
    
	//System.out.println(redoc.get(0).getString("oid"));
	
	
	return redoc;
	
}

public String findKeyandValueDocArray(String keyStr,String[] valStr,String aimKeyStr,String oidorname,String valofOidorName,DBCollection col){
	String value=null;
	ArrayList<BasicDBObject>  redoc=new ArrayList<BasicDBObject>();
	redoc=this.getKeyandValueDocArray(keyStr, valStr, aimKeyStr, col);
	if(oidorname=="oid"){
		
		for(int i=0;i<redoc.size();i++){
			
			if(redoc.get(i).getString(oidorname).equals(valofOidorName)) 
				
				value=redoc.get(i).getString("ontoname");
			
		}
		
	}
	if(oidorname=="ontoname"){
		
		for(int i=0;i<redoc.size();i++){
			
			if(redoc.get(i).getString(oidorname).equals(valofOidorName)) value=redoc.get(i).getString("oid");
			
		}
		
	}
	
	return value;
}

	
	/**
	 * �ǻ�ȡ��¼�е�ĳһ�����ļ�ֵ
	 * @param keyStr
	 * @param valStr
	 * @param aimKey
	 * @param col
	 * @return
	 */
	public DBObject getKeyandValue(String keyStr,String[] valStr,String aimKey,DBCollection col){
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject aimdoc=new BasicDBObject();
		aimdoc.put(aimKey, 1);
		DBObject redoc=new BasicDBObject();
		redoc=col.findOne(doc,aimdoc);
		return redoc;
		
	}
	
	
	
	
	/**
	 * ��ȡ������
	 * @param keyStr
	 * @param valStr
	 * @param col
	 * @return
	 */
	public Object[] getKeySet(String keyStr,String[] valStr,DBCollection col){
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		//BasicDBObject aimdoc=new BasicDBObject();
		//aimdoc.put(aimKey, 1);
		BasicDBObject redoc=new BasicDBObject();
		redoc=(BasicDBObject)col.findOne(doc);
	    Object[] keyArray=redoc.keySet().toArray();
	    
	    return keyArray;
		
	}
	/**
	 * �õ�һ�����ļ�ֵ�ļ���
	 * @param keyStr
	 * @param valStr
	 * @param col
	 * @return
	 */
	public Collection<Object> getValueSet(String keyStr,String[] valStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		//BasicDBObject aimdoc=new BasicDBObject();
		//aimdoc.put(aimKey, 1);
		BasicDBObject redoc=new BasicDBObject();
		redoc=(BasicDBObject)col.findOne(doc);
		//redoc.values();
		 //Set<String> str=redoc.keySet();
		return redoc.values();
	}
	
	/**
	 * �ú���������һ��key,��õ���Ӧ�ڸ�key�ļ�ֵ����
	 * 
	 * @param keyStr
	 * @param valStr
	 * @param col
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getValueArrayofKey(String keyStr,String[] valStr,String key,DBCollection col){
		ArrayList<String> valueArray=new ArrayList<String>();
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		//BasicDBObject aimdoc=new BasicDBObject();
		//aimdoc.put(aimKey, 1);
		BasicDBObject redoc=new BasicDBObject();
		redoc=(BasicDBObject)col.findOne(doc);
		
		if (redoc==null){
			
			return valueArray;
		}
		
		else {
	    Object[] keys=redoc.keySet().toArray();
	    int i;
	    for(i=0;i<keys.length;i++){
	    	if (keys[i].equals(key)) break;
	    }
	    Object[] keyvalue=redoc.values().toArray();
	    if(i==keys.length) valueArray.clear();
	    else valueArray = (ArrayList<String>)keyvalue[i];
		return valueArray;
		}
	}
	/**
	 * 
	 * @param keyStr
	 * @param valStr
	 * @param key
	 * @param col
	 * @return
	 */
	public ArrayList<String> getValueArrayofKeyforTermID(String keyStr,String valStr,String key,DBCollection col){
		ArrayList<String> valueArray=new ArrayList<String>();
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		//BasicDBObject aimdoc=new BasicDBObject();
		//aimdoc.put(aimKey, 1);
		BasicDBObject redoc=new BasicDBObject();
		redoc=(BasicDBObject)col.findOne(doc);
		
		if (redoc==null){
			
			return valueArray;
		}
		
		else {
	    Object[] keys=redoc.keySet().toArray();
	    int i;
	    for(i=0;i<keys.length;i++){
	    	if (keys[i].equals(key)) break;
	    }
	    Object[] keyvalue=redoc.values().toArray();
	    valueArray = (ArrayList<String>)keyvalue[i];
		return valueArray;
		}
	}
	
	
	/**
	 * �ú���������һ������ֵ������һ�����ڸ�ֵ��key�ļ��ϣ�һ��ֵ�п��ܶ�Ӧ���key
	 * ��Ե������ A��B�����ж����ϵ�����ܼ��Ǻ��ѣ�����ͬ�£�����������ϵ��
	 * @param keyStr
	 * @param valStr
	 * @param col
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getKeyArrayofValue(String keyStr,String[] valStr,String val,DBCollection col){
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		//BasicDBObject aimdoc=new BasicDBObject();
		//aimdoc.put(aimKey, 1);
		BasicDBObject redoc=new BasicDBObject();
		redoc=(BasicDBObject)col.findOne(doc);
		int[] num=new int[10];//����10��ȡֵ��ʾһ���������һ������Ĺ�ϵ��಻����10����ϵ��
		int m=0,j=0,i=0;  
	    Object[] keyvalue=redoc.values().toArray();
	    for(i=1;i<keyvalue.length;i++){
	    	
	    ArrayList<String> list = (ArrayList<String>)keyvalue[i];
	    
	    for(j=0;j<list.size();j++){
	    	if(list.get(j).equals(val))break;
	    }
	    if(j<list.size()) {num[m]=i;m=m+1;}	    	
	    }	
	    
	    
	    Object[] key=redoc.keySet().toArray();
	    ArrayList<String> keyArray=new ArrayList();
	    for(int k=0;k<m;k++)
	    	keyArray.add(key[num[k]].toString());
	    
		return keyArray;
	}
	
	/**
	 * ���ַ�������ʽ���ظ������ļ�ֵ
	 * @param keyStr
	 * @param valStr
	 * @param aimKey
	 * @param col
	 * @return
	 */
	public String getKeysValue(String keyStr,String[] valStr,String aimKey,DBCollection col){
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		//BasicDBObject aimdoc=new BasicDBObject();
		//aimdoc.put(aimKey, 1);
		BasicDBObject redoc=new BasicDBObject();
		redoc=(BasicDBObject)col.findOne(doc);
	    String str=redoc.getString(aimKey);
		return str;
		
	}
	
	
	/**
	 * ��double����ʽ���ظ������ļ�ֵ
	 * @param keyStr
	 * @param valStr
	 * @param aimKey
	 * @param col
	 * @return
	 */
	public double getKeysDoubleValue(String keyStr,String[] valStr,String aimKey,DBCollection col){
		BasicDBObject doc=new BasicDBObject();
		double[] str=new double[1];
		doc.put(keyStr, valStr);
		//BasicDBObject aimdoc=new BasicDBObject();
		//aimdoc.put(aimKey, 1);
		BasicDBObject redoc=new BasicDBObject();
		redoc=(BasicDBObject)col.findOne(doc);
		str=redoc.g.getDouble(aimKey);
		return str[0];
		
	}
	
	
	/**
	 * ɾ��ָ���ļ���Ӧ�ļ�ֵ�����е�ָ����ֵ,������ָü�ֵΪ�գ���ֱ��ɾ�ü�
	 * @param keyStr
	 * @param valStr
	 * @param newkeyStr
	 * @param newvalStr
	 * @param col
	 * @return
	 */
	public DBObject delOneofKeyandValueArray(String keyStr,String[] valStr,String delkeyStr,String delvalStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(delkeyStr, delvalStr);
		DBObject updatedoc=new BasicDBObject();
		updatedoc.put("$pull", newdoc);
		col.update(doc, updatedoc);
		//���ɾ�����ָü�ֵΪ�գ���ֱ��ɾ�ü�
		ArrayList<String> vallist=getValueArrayofKey(keyStr,valStr,delkeyStr,col);
		if(vallist.isEmpty()) 
			delKeyandValue(keyStr,valStr,delkeyStr,col);
		
		return updatedoc;
		
	}
	
	/**
	 * ���������������id�ŵ�ɾ����ר�ű�д�ĺ�����Ŀ����ɾ����������Ӧ��id���飬�����������û��id���ˣ���ֱ��ɾ����������
	 * @param keyStr
	 * @param valStr
	 * @param delkeyStr
	 * @param delvalStr
	 * @param col
	 * @return
	 */
public DBObject delOneofKeyandValueArrayandDocument(String keyStr,String[] valStr,String delkeyStr,String delvalStr,DBCollection col){
		
		BasicDBObject doc=new BasicDBObject();
		doc.put(keyStr, valStr);
		BasicDBObject newdoc=new BasicDBObject();
		newdoc.put(delkeyStr, delvalStr);
		DBObject updatedoc=new BasicDBObject();
		updatedoc.put("$pull", newdoc);
		col.update(doc, updatedoc);
		//���ɾ�����ָü�ֵΪ�գ���ֱ��ɾ�ü�
		ArrayList<String> vallist=getValueArrayofKey(keyStr,valStr,delkeyStr,col);
		if(vallist.isEmpty()) delDocument(keyStr,valStr,col);
		
		return updatedoc;
		
	}
	
	
	
	
	
@SuppressWarnings("unchecked")
public static void main(String[] args) throws UnknownHostException, MongoException  {
		
		//Dbcontrol Dc=new Dbcontrol();
		//System.out.println(Dc.creatDb("my"));
	//Mongo mg = new Mongo();
    

    // ��ȡ��Ϊ����str�����ݿ����
    //DB db = mg.getDB("admin");
	Dbcontrol Dbc = new Dbcontrol();
	DB db=Dbc.creatDb("user");
	//Dbc.delDb("qqq");
	DBCollection conCol=Dbc.creatTable(db,"ConData");
	DBCollection relCol=Dbc.creatTable(db,"RelData");
	DBCollection colUser=Dbc.creatTable(db,"ColUser");
	BasicDBObject doc=new BasicDBObject();
	String[] ss=new String[1];
	ss[0]="a";
	
	///conCol.findAndModify(query, update)
    /*Mongo mg = new Mongo();
    DB db = mg.getDB("admin");
	DBObject cmdObj = new BasicDBObject();
    cmdObj.put("copydb","1");
    cmdObj.put("fromhost","127.0.0.1:27017");
    cmdObj.put("fromdb","ontotest");
    cmdObj.put("todb","kkk");
    System.out.println(db.command(cmdObj));
	*/
	
	
	//System.out.println(Dbc.addDocument("b",ss, col));
	//Dbc.delTable(db, "ctest");
	//doc=Dbc.addDocument("a", "1",col);
	//String s;
	//s=doc.getString("a");
	//System.out.println(s);
	//Dbc.addDocument("b", "2", col);
	//Dbc.delDocument("cid",ss,conCol);
	//Dbc.delDocument("rid",ss,relCol);
//	Dbc.findDocument("cid",ss , conCol);
	//JSONObject js;
	//js= new JSONObject();
	//JSONArray ja=new JSONArray();
	//Dbc.addKeyandValueDocArray("username", ss, "myonto", "10002", "onto2", colUser);
	
    //System.out.println(js);
	
	
	//System.out.println(Dbc.delKeyandValue("b","2","b",col));
	//System.out.println(Dbc.updateKeyandValue("b", "2","c","21", col));
	
	//System.out.println(Dbc.getKeysValue("b", "2","e", col));
	
	//System.out.println(Dbc.addKeyandValueArray("b", ss,"d","23", col));
	//System.out.println(Dbc.getKeyandValue("b", "2","c", col));
	//System.out.println(Dbc.getKeySet("cid", ss, conCol)[0].toString());
	//Dbc.delOneofKeyandValueArray("b", ss, "d", "23", col);
	//Object ob[] = Dbc.getValueSet("b", ss, conCol).toArray();
	//System.out.println(ob);
	//Object ob[] = Dbc.getKeySet("b", ss, col).toArray();
	//System.out.println(Dbc.getValueArrayofKey("cid", ss, "syn", conCol));
	//System.out.println(Dbc.updateKey("cid", ss, "sss","syn", col));
	//System.out.println(Dbc.getValueSet("b", ss, col).toArray().length);
	//ja=JSONArray.fromObject(ob[1]);
	//System.out.println(ja.toArray().length);
	//for (int i=0;i<Dbc.getValueSet("b", ss, col).toArray().length;i++){System.out.println(ob[i]);}
	
	
	
	//System.out.println(ja.indexOf(0).equals("2"));
	//System.out.println(Dbc.delOneofKeyandValueArray("cid", ss, "gloss", "gloss", conCol));
	/*ArrayList<String> list = (ArrayList<String>)ob[2];
	System.out.println(list.get(0));
	
	Object a=new Object();
	a=Dbc.getValueSet("b", "2", col).toArray()[1];
	System.out.println(a);*/
	
	//DBCursor cur=Dbc.findDocumentWithKey("2003", col);
	//while (cur.hasNext()){
	//	System.out.println(cur.next().get("cid"));
	//	System.out.println(cur.size());
	//}
	//Dbc.getKeyandValueDocArray("username", ss, "myonto", colUser);
	//System.out.println(Dbc.findKeyandValueDocArray("username", ss, "myonto", "ontoname", "onto2", colUser));
	System.out.println("ok");
	  
    //System.out.println(col);
    
    
}	
		
	
	
}
