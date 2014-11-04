package com.onto.calculate;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;

public class ConBodyReduce {

	
	public ConRelAdmin cra;
	DB db;
	DBCollection colConData;
	DBCollection colConTerm;
	DBCollection colConWeight;
	DBCollection colRelData;
	DBCollection colRelTerm;
	DBCollection colId;

	public ConBodyReduce() throws UnknownHostException, MongoException  {
		
		cra=new ConRelAdmin();
		db=cra.creatOntoDB("ontopeople");
		colConData=cra.creatTable(db, "ConData");
		colConTerm=cra.creatTable(db, "ConTerm");
		colConWeight=cra.creatTable(db, "ConWeight");
		colRelData=cra.creatTable(db, "RelData");
		colRelTerm=cra.creatTable(db, "RelTerm");
		colId=cra.creatTable(db, "idPool");
		
	}	
	
	
	public void writeConWeightCol(){
		
		DBCursor cur=colConData.find();
     	DBObject dbrow;
     	//int termNum=0;
     	while (cur.hasNext()){
     		//tm.setId(String.valueOf(termNum));
     		dbrow=cur.next();
     		@SuppressWarnings("unchecked")
			ArrayList<String>  istr=(ArrayList<String>) dbrow.get("cid");
     		
     		WeightCal(istr.get(0),colConData,colConWeight);
     		     		
     		System.out.println(istr.get(0));
//     		
     	}
		
		
		
	}
	
	/**
	 * ��ÿһ����������Ӧ��Ȩֵ����ʵ���ǹ�ϵ��Ȩֵ��Ϊ���Ժ���ⷽ�㣬�������ֵ������һ�����
	 * @param conID
	 * @param col
	 */
	public void WeightCal(String conID,DBCollection col,DBCollection colw){
		cra.addConWeightDoc(conID, colw);
		float totalSubCods=0;
		ArrayList<String> rellist=cra.getPartRelsofConcept(conID, col);
		if(!rellist.isEmpty()){
		for(int i=0;i<rellist.size();i++){
			ArrayList<String> conlist=cra.getConceptofRel(conID, rellist.get(i), col);
			totalSubCods=totalSubCods+conlist.size();
		}
		
		cra.addConWeightVal(conID, "totalCons", totalSubCods, colw);
		
		for(int i=0;i<rellist.size();i++){
			ArrayList<String> conlist=cra.getConceptofRel(conID, rellist.get(i), col);
			float weight=conlist.size()/totalSubCods;
			for(int j=0;j<conlist.size();j++){
				
				cra.addConWeightVal(conID, conlist.get(j), weight, colw);
				
			}
			
			
			
		}
		
	}
	}
	
	
	public void doConBodyReduce(String conID,DBCollection colWeight){
		BasicDBObject doc=new BasicDBObject();
		String tempId="temp";
		String[] temp=new String[1];
		temp[0]=tempId;
		String aimId="aim";
		String[] aim=new String[1];
		aim[0]=aimId;
		doc=(BasicDBObject) cra.getConDocument(conID, colWeight);
		doc.remove("_id");
		doc.removeField("cid");
		doc.put("cid", temp);
		colWeight.insert(doc);
	
		
		ArrayList<String> conlist=cra.getAllRelsofConcept(tempId, colWeight);//����õ����Ǹ��������ֱ����صĽڵ㼯��
		conlist.remove("totalCons");//ȥ��ͳ�ƽڵ�����ļ���
		conlist.remove("cid");
		if(!conlist.isEmpty()){
			cra.addConWeightDoc(aimId, colWeight);
		for(int i=0;i<conlist.size();i++){
			ArrayList<String> subconlist=cra.getAllRelsofConcept(conlist.get(i), colWeight);//�õ����漯����ÿ���ڵ��ֱ������ӽڵ�ļ��ϡ�
			subconlist.remove("totalCons");
			subconlist.remove(conID);
			subconlist.remove("cid");
			if(!subconlist.isEmpty()){
			for (int j=0;j<subconlist.size();j++){
				
				if(conlist.contains(subconlist.get(j))) {
					float singlew=cra.getConWeightVal(tempId, subconlist.get(j), colWeight);
					float w1=cra.getConWeightVal(tempId,conlist.get(i), colWeight);
					float w2=cra.getConWeightVal(conlist.get(i), subconlist.get(j), colWeight);
					float w=singlew+(w1*w2/(w1+w2));
					cra.addConWeightVal(aimId, subconlist.get(j), w, colWeight);
					
				}
				else{
					
					float w1=cra.getConWeightVal(tempId,conlist.get(i), colWeight);
					float w2=cra.getConWeightVal(conlist.get(i), subconlist.get(j), colWeight);
					float w=(w1*w2/(w1+w2));
					cra.addConWeightVal(aimId, subconlist.get(j), w, colWeight);
					
				}
				
			}
			
		}
		}
		
	}
		
		cra.delConDocument("cid", tempId, colWeight);
		
	}
	
	
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub
		
		ConBodyReduce cbr=new ConBodyReduce();
		
	//	cbr.WeightCal("1000000004", cbr.colConData,cbr.colConWeight);
	//    cbr.writeConWeightCol();
		cbr.doConBodyReduce("1000000004", cbr.colConWeight);
		System.out.println("OK!!!");
		
		

	}

}
