package com.onto.query;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;
import com.onto.mongo.control.Dbcontrol;

public class Query {
	
	public int nodenum=0;
	public ConRelAdmin cra;
	public Dbcontrol dbc;
	 DB db;
	static DBCollection colConData;
	static DBCollection colConTerm;
	DBCollection colRelData;
	DBCollection colRelTerm;
	DBCollection colId;
	
	public Query(){
		
		cra=new ConRelAdmin();
		dbc=new Dbcontrol();
		try {
			db=cra.creatOntoDB("data30");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		colConData=cra.creatTable(db, "ConData");
		colConTerm=cra.creatTable(db, "ConTerm");
		colRelData=cra.creatTable(db, "RelData");
		colRelTerm=cra.creatTable(db, "RelTerm");
		colId=cra.creatTable(db, "idPool");
		
	}
	
	/**
	 * 查询一个术语所对应的所有概念
	 * @param term
	 * @param colConTerm
	 */
	public void queryConofOneTerm(String term,DBCollection colConTerm){
		BasicDBObject doc1=new BasicDBObject();
		BasicDBObject doc2=new BasicDBObject();
		long begintime,endtime,costTime;
		doc1.put("term", term);
		begintime=System.nanoTime();
		doc2=(BasicDBObject) colConTerm.findOne(doc1);
		endtime=System.nanoTime();
		costTime = (endtime - begintime)/1000;
		System.out.println(doc2);
		System.out.println("cost time :"+costTime);
		
	}
	
	/**
	 * 查询一个概念的所有属性ID
	 * @param con
	 * @param colConData
	 */
	public void queryAllRelofOneCon(String con,DBCollection colConData){
		BasicDBObject doc1=new BasicDBObject();
		BasicDBObject doc2=new BasicDBObject();
		long begintime,endtime,costTime;
		doc1.put("cid", con);
		begintime=System.nanoTime();
		doc2=(BasicDBObject) colConData.findOne(doc1);
		endtime=System.nanoTime();
		costTime = (endtime - begintime)/1000;
		System.out.println(doc2);
		System.out.println("cost time :"+costTime);
		
	}
	
	/**
	 * 查询一个概念的某一个属性
	 * @param con
	 * @param pro
	 * @param colConData
	 */
	public void queryOneRelofOneCon(String con,String pro,DBCollection colConData){
		BasicDBObject doc1=new BasicDBObject();
		BasicDBObject doc2=new BasicDBObject();
		BasicDBObject doc3=new BasicDBObject();
		long begintime,endtime,costTime;
		doc1.put("cid", con);
		doc3.put(pro, 1);
		begintime=System.nanoTime();
		doc2=(BasicDBObject) colConData.findOne(doc1,doc3);
		endtime=System.nanoTime();
		costTime = (endtime - begintime)/1000;
		System.out.println(doc2);
		System.out.println("cost time :"+costTime);
		
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public String  Iteration(String con,String pro, DBCollection colConData){
		String reval="nothing";
		
		BasicDBObject doc1=new BasicDBObject();
		BasicDBObject doc2=new BasicDBObject();
		BasicDBObject doc3=new BasicDBObject();
		ArrayList<String> valstring=new ArrayList<String>();
		doc1.put("cid", con);
		doc3.put(pro, 1);
		doc2=(BasicDBObject) colConData.findOne(doc1,doc3);
		if(doc2.containsKey(pro)) {
		Object[] val=doc2.values().toArray();
		valstring=(ArrayList<String>) val[1];
		for (int i=0;i<valstring.size();i++){
			reval=valstring.get(i);
			
			this.Iteration(reval, pro, colConData);
			nodenum++;
			System.out.println(reval+"  "+nodenum+"|");
		}
		
		
		}
		
		return reval;
	}
		
	/**
	 * 计算查询一个节点的所有父节点所用的时间
	 * @param con
	 * @param pro
	 * @param colConData
	 */
	public void queryAllFatherofOneCon(String con,String pro,DBCollection colConData){
		
		BasicDBObject doc1=new BasicDBObject();
		BasicDBObject doc2=new BasicDBObject();
		BasicDBObject doc3=new BasicDBObject();
		BasicDBObject doc4=new BasicDBObject();
		ArrayList<String> valstring=new ArrayList<String>();
		long begintime,endtime,costTime;
		doc1.put("cid", con);
		doc3.put(pro, 1);
		begintime=System.nanoTime();
		doc2=(BasicDBObject) colConData.findOne(doc1,doc3);
		Object[] val=doc2.values().toArray();
		valstring=(ArrayList<String>) val[1];
		for (int i=0;i<valstring.size();i++){
			
			
		}
		
		endtime=System.nanoTime();
		costTime = (endtime - begintime)/1000;
		System.out.println(val[1].toString());
		System.out.println("cost time :"+costTime);
		
		
		
	}
		

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Query qy=new Query();
//		ArrayList<String> idlist=new ArrayList<String>();
//			
//		long begintime = System.nanoTime();
//		idlist=qy.cra.getTermId("car", "cid", colConTerm);//列出一个术语的所有概念
//		long endtime = System.nanoTime();
//		long costTime = (endtime - begintime)/1000;
//		System.out.println(idlist+":"+costTime);
////		
//		String[] str=new String[1];
//		str[0]="1000007846";
//	//	colConData.createIndex(new BasicDBObject("cid",1));
////		BasicDBObject doc=new BasicDBObject();
////		doc.put("cid", str[0]);
////	//	doc.put("2000000002", 1);
////		//BasicDBObject aimdoc=new BasicDBObject();
////		//aimdoc.put(aimKey, 1);
//		BasicDBObject redoc=new BasicDBObject();
//		BasicDBObject valdoc=new BasicDBObject();
//		valdoc.put("2000000001", 1);
//		
//		begintime=System.nanoTime();
//		//idlist=qy.cra.getConceptofRel("1000001740", "2000000002", colConData);//列出一个概念的指定关系
//		
////		qy.dbc.findDocument("cid",str, colConData);//列出所有的属性及值
////		redoc=(BasicDBObject)colConData.findOne(doc);
//		idlist=qy.cra.getTermId("cat", "cid", colConTerm);
//		//String[] str=new String[1];
//		for (int i=0;i<idlist.size();i++){
//		//str[0]=idlist.get(i);	
//		BasicDBObject doc=new BasicDBObject();
//		doc.put("cid", idlist.get(i));
//		redoc=(BasicDBObject)colConData.findOne(doc);
////		System.out.println("num: "+i);
//		}
//		endtime=System.nanoTime();
//		costTime = (endtime - begintime)/1000;
//		System.out.println(redoc+":"+costTime);
//		System.out.println(costTime);
//		qy.queryConofOneTerm("zebra", colConTerm);
//		qy.queryAllRelofOneCon("1000007846", colConData);
//		qy.queryAllFatherofOneCon("1000007846", "2000000001", colConData);
		long begintime = System.nanoTime();
		qy.Iteration("1000039545", "2000000001", colConData);
//		qy.queryAllRelofOneCon("1000007846", colConData);
//		qy.queryAllRelofOneCon("1000039545", colConData);
//		qy.queryAllRelofOneCon("1000478647", colConData);
		long endtime = System.nanoTime();
		long costTime = (endtime - begintime)/1000;
		System.out.println("time: "+costTime);
		
	}

}
