package com.onto.mongo.control;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 是对整个本体工程做处理的类
 * @author Administrator
 *
 */

public class OntoAdmin {
	
	public static ConRelAdmin cra=new ConRelAdmin();	
	public static Dbcontrol dbCon=new Dbcontrol();
	
	/**
	 * 用来建本体数据库的方法，从模板数据库中copy内容到一个新建数据库。
	 * @param projPath
	 * @param newDB 新建本体数据库
	 * @param modelDB 模板数据库，包括：OWL模板(分三种)，其它领域模板。系统中有存储模板的功能。
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	
	@SuppressWarnings("deprecation")
	public DB creatProjectDB(String projPath,String newDB,String modelDB) throws UnknownHostException, MongoException{
		
		//generate a file including project name,path,databasename
		
		Mongo mg = new Mongo();
	    DB db = mg.getDB("admin");
		DBObject cmdObj = new BasicDBObject();
	    cmdObj.put("copydb","1");
	    cmdObj.put("fromhost","127.0.0.1:27017");
	    cmdObj.put("fromdb",modelDB);
	    cmdObj.put("todb",newDB);
	    System.out.println(db.command(cmdObj));
	    
		DB newOntoDB=mg.getDB(newDB);
		return newOntoDB;
			
	}
	
	/**
	 *  这是一个直接添加子节点的操作。
	 * @param superConID
	 * @param subConID
	 * @param syn
	 * @param colCon
	 * @param colRel
	 */
	
	@SuppressWarnings("unchecked")
	public void addSubConcept(String superConID,String subConID,String syn, DBCollection colCon,DBCollection colRel){
		cra.addConcept(subConID,syn,colCon);
		cra.addConceptRel(subConID, "2002", superConID, colCon);//“2002”是“是一”关系的ID号，以后固定下来。
		
		
		//检查父节点中是否有可以继承的关系或属性，如果有，则将该属性和值添加取子节点中
		
		ArrayList<String> rellist=new ArrayList();
		ArrayList<String> inhlist=new ArrayList();
		rellist=cra.getPartRelsofConcept(superConID, colCon);
		for(int i=0;i<rellist.size();i++){
			if(cra.getPropofRel(rellist.get(i), "prop",colRel).contains(cra.propofRel.get(4)))
				inhlist.add(rellist.get(i));
		}//得到父节点具有继承性的关系列表
		for(int j=0;j<inhlist.size();j++){
			ArrayList<String> vallist=cra.getConceptofRel(superConID, inhlist.get(j), colCon);
			cra.addConceptRel(subConID, inhlist.get(j), vallist, colCon);
			//cra.addConceptValueofRel(subConID, rel, valnew, col)
		}
				
		
	}
	
	
	
	
	
	
	
		
	
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub
		OntoAdmin onto=new OntoAdmin();
		
		//DB db=onto.creatProjectDB("","qqq","ontotest");
		DB db=cra.creatOntoDB("qqq");
		DBCollection colConData=cra.creatTable(db, "ConData");
		DBCollection colRelData=cra.creatTable(db, "RelData");
		//onto.addSubConcept("1000", "1007","adfad", colConData,colRelData);//待验证
		
		
		System.out.println("ok");

	}
	

}
