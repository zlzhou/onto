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
 * �Ƕ��������幤�����������
 * @author Administrator
 *
 */

public class OntoAdmin {
	
	public static ConRelAdmin cra=new ConRelAdmin();	
	public static Dbcontrol dbCon=new Dbcontrol();
	
	/**
	 * �������������ݿ�ķ�������ģ�����ݿ���copy���ݵ�һ���½����ݿ⡣
	 * @param projPath
	 * @param newDB �½��������ݿ�
	 * @param modelDB ģ�����ݿ⣬������OWLģ��(������)����������ģ�塣ϵͳ���д洢ģ��Ĺ��ܡ�
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
	 *  ����һ��ֱ������ӽڵ�Ĳ�����
	 * @param superConID
	 * @param subConID
	 * @param syn
	 * @param colCon
	 * @param colRel
	 */
	
	@SuppressWarnings("unchecked")
	public void addSubConcept(String superConID,String subConID,String syn, DBCollection colCon,DBCollection colRel){
		cra.addConcept(subConID,syn,colCon);
		cra.addConceptRel(subConID, "2002", superConID, colCon);//��2002���ǡ���һ����ϵ��ID�ţ��Ժ�̶�������
		
		
		//��鸸�ڵ����Ƿ��п��Լ̳еĹ�ϵ�����ԣ�����У��򽫸����Ժ�ֵ���ȡ�ӽڵ���
		
		ArrayList<String> rellist=new ArrayList();
		ArrayList<String> inhlist=new ArrayList();
		rellist=cra.getPartRelsofConcept(superConID, colCon);
		for(int i=0;i<rellist.size();i++){
			if(cra.getPropofRel(rellist.get(i), "prop",colRel).contains(cra.propofRel.get(4)))
				inhlist.add(rellist.get(i));
		}//�õ����ڵ���м̳��ԵĹ�ϵ�б�
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
		//onto.addSubConcept("1000", "1007","adfad", colConData,colRelData);//����֤
		
		
		System.out.println("ok");

	}
	

}
