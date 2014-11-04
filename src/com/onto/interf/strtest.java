package com.onto.interf;

import java.lang.String;
//import java.lang.Character;
import java.lang.System;
import java.net.UnknownHostException;

import mongodbcontrol.Dbcontrol;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


public class strtest {
	
	// public static void findOne(DB db) {
	        //DB db = MongoTest.connect();
	    //    DBCollection collection = db.getCollection("c1");
	    //    DBObject doc = collection.findOne();
	   //     System.out.println(doc);
	   // }
	
	
	public static void main(String[] args) throws UnknownHostException, MongoException  {
		
		Interf inTerf=new Dbcontrol();
		
		inTerf.creatDb("dbtest");
		Dbcontrol Dc=new Dbcontrol();
		//System.out.println(Dc.creatDb("my"));
		DB db=Dc.openDb("aaa");
		
    /**
       Mongo实例代表了一个数据库连接池
     * Mongo mg = new Mongo("localhost");
       Mongo mg = new Mongo("localhost", 27017);
     */
    //Mongo mg = new Mongo();
    

    // 获取名为“dbtest”的数据库对象
    //DB db = mg.getDB("dt");
    // 查询该库中所有的集合 (相当于表)
    //for (String name : db.getCollectionNames()) {
     //   System.out.println("Collection Name: " + name);
    //}
    
    DBCollection collection = db.getCollection("c1");
    BasicDBObject doc = new BasicDBObject();
    doc.put("name", "jason");
    doc.put("sex", "women");
    doc.put("count", 1);
 
  //  BasicDBObject info = new BasicDBObject();
  //  info.put("x", 123);
   // info.put("y", 234);
 
  //  doc.put("info", info);
 
    collection.insert(doc);// 数据库持久化到磁盘上
    System.out.println("insert ok");
    
   // Dc.delDb("aaa");
    
    System.out.println("delete ok");
    
   // findOne(db);
   
		
	}
		
	

}
