/**
 * 
 */
package com.onto.mongo.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.bson.BSONObject;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.onto.reason.PrologCKI;
import com.onto.reason.PrologFile;

/**�������Ҫ�����ǶԸ���͹�ϵ������ɾ�Ĳ�
 * @author Administrator������
 * 
 * 
 *2014��1��7��
 */
@SuppressWarnings("unchecked")
public class ConRelAdmin {
	
	public static Dbcontrol dbCon=new Dbcontrol(); 
	public static ArrayList<String> noIDrelofCon=new ArrayList<String>();
	public ArrayList<String> noIDrelofRel=new ArrayList<String>();
	public ArrayList<String> propofRel=new ArrayList<String>();
	
	
	public ConRelAdmin(){
		//������û��ID�ŵĹ�ϵ�������˳���ǲ��ܱ�ġ�
		noIDrelofCon.add("cid");//0
		noIDrelofCon.add("csyn");//1
		noIDrelofCon.add("gloss");//2
		noIDrelofCon.add("esyn");//3
		
		//��ϵ��û��ID�ŵĹ�ϵ�������˳���ǲ��ܱ��
		noIDrelofRel.add("rid");//0
		noIDrelofRel.add("dom");//1
		noIDrelofRel.add("ran");//2
		noIDrelofRel.add("csyn");//3
		noIDrelofRel.add("esyn");//4
		noIDrelofRel.add("gloss");//5
		noIDrelofRel.add("prop");//6
		noIDrelofRel.add("rule");//7
		
		//��ϵ������  rfl sym tra inh fun invfun
		propofRel.add("rfl");//1�Է���
		propofRel.add("sym");//2�Գ���
		propofRel.add("tra");//3������
		propofRel.add("inh");//4�̳���
		propofRel.add("fun");//5������
		propofRel.add("invfun");//6��������
		
	
		
		
	}
	
	
	
	/**
	 * 1��ͷ���Ǹ���ID��2��ͷ���ǹ�ϵID
	 * ���ڸ�������2λ��0������ָ�ĳ����������1������ָ�ľ������Ҳ����ʵ����
	 * ���ڹ�ϵ�� ���ڹ�ϵ�ţ������2λ��0����ʾ�˹�ϵ�����ԣ������1�����ʾ�ǹ�ϵ��
	 * 
	 * ���ڹ�ϵ����������Ķ��ǵ��ţ�˫�ſ�����ȡ��������Զ���1������������Ϊ���ϵ��ID�š�
	 * 
	 * �˴��ڲ���id��ʱ�����⣬������mongodb
	 * 
	 * @param col
	 */
	
	public void generateID(DBCollection col){
		int conmaxnum=10000;
		int insmaxnum=100000;
		int attmaxnum=5000;
		int relmaxnum=5000;
		String cid="conid";
		String[] cidStr1=new String[1];
		cidStr1[0]=cid;
//		dbCon.addDocument("idtype", cidStr1, col); 
//		for (int i=1;i<conmaxnum;i++){
//			
//			dbCon.addKeyandValueArray("idtype", cidStr1, "idpool", String.valueOf(1000000000+i), col);
//			
//		}
//		
//		System.out.println("conid ok");
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		cid="insid";
//		String[] cidStr2=new String[1];
//		cidStr2[0]=cid;
//		dbCon.addDocument("idtype", cidStr2, col); 
//		for (int i=1;i<insmaxnum;i++){
//			
//			dbCon.addKeyandValueArray("idtype", cidStr2, "idpool", String.valueOf(1100000000+i), col);
//			
//		}
//		System.out.println("insid ok");
//
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		cid="attid";
		String[] cidStr3=new String[1];
		cidStr3[0]=cid;
		dbCon.addDocument("idtype", cidStr3, col); 
		for (int i=21;i<attmaxnum;i=i+2){
			
			dbCon.addKeyandValueArray("idtype", cidStr3, "idpool", String.valueOf(2000000000+i), col);
	
		}
		System.out.println("attid ok");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cid="relid";
		String[] cidStr4=new String[1];
		cidStr4[0]=cid;
		dbCon.addDocument("idtype", cidStr4, col); 
		for (int i=21;i<relmaxnum;i=i+2){
			
			dbCon.addKeyandValueArray("idtype", cidStr4, "idpool", String.valueOf(2100000000+i), col);
			
		}
		System.out.println("relid ok");
		
			
		
	}
	
	
	/**
	 *�����Ĺ����Ǵ�ID���л�ȡid�ţ�һ�������֣��ֱ��ǳ�����ʵ�������ԣ���ϵ
	 *ȡ������id���о���һ����
	 * @param typestr���ֱ��Ӧ�������ͣ�conid insid attid relid
	 * @param col
	 * @return
	 */
	public String getID(String typestr,DBCollection col){
		String[] typeStr=new String[1];
		typeStr[0]=typestr;
		ArrayList<String> idlist;
		idlist=dbCon.getValueArrayofKey("idtype", typeStr, "idpool", col);
		String idstr=idlist.get(0);
		dbCon.delOneofKeyandValueArray("idtype", typeStr, "idpool",idstr,col);
		return idstr;
		
	}
	
	
	/**
	 * ��ɾ������ID�ŷŻ�idpool �������Զ������������һ�����͵�id
	 * ���ڹ�ϵ���Ե�ID�ţ�ֻ�ŵ��ţ���Ϊ˫�������ϵ��
	 * @param idstr
	 * @param col
	 */
	public void putID(String idstr,DBCollection col){
	
		String cid="conid";
		String[] cidStr1=new String[1];
		cidStr1[0]=cid;
				
		cid="insid";
		String[] cidStr2=new String[1];
		cidStr2[0]=cid;
				
		cid="attid";
		String[] cidStr3=new String[1];
		cidStr3[0]=cid;
		
		
		cid="relid";
		String[] cidStr4=new String[1];
		cidStr4[0]=cid;
		
		
		int id=Integer.valueOf(idstr);
		if (id<1100000000) dbCon.addKeyandValueArray("idtype", cidStr1, "idpool", idstr, col);
		else if((id>=1100000000)&&(id<2000000000))
			dbCon.addKeyandValueArray("idtype", cidStr2, "idpool", idstr, col);
		else if((id>=2000000000)&&(id<2100000000)&&(id%2!=0))
			dbCon.addKeyandValueArray("idtype", cidStr3, "idpool", idstr, col);
		else if((id>=2100000000)&&(id%2!=0))
			dbCon.addKeyandValueArray("idtype", cidStr4, "idpool", idstr, col);
	}
	
	
	/**
	 * �������������û�к�������ͬ���������У��򷵻������б����û�У��򷵻�sizeΪ0��list
	 * @param termstr
	 * @param col
	 * @return
	 */
	public ArrayList<String> getDupConTermID(String termstr,DBCollection col){
		
		String[] cidStr=new String[1];
		cidStr[0]=termstr;
		ArrayList<String> vallist=dbCon.getValueArrayofKey("term", cidStr, "cid", col);
		return vallist;
			
	}
	
	
	/**
	 * �������������û�к�������ͬ���������У��򷵻������б����û�У��򷵻�sizeΪ0��list
	 * @param termstr
	 * @param col
	 * @return
	 */
	public ArrayList<String> getDupRelTermID(String termstr,DBCollection col){
		
		String[] cidStr=new String[1];
		cidStr[0]=termstr;
		ArrayList<String> vallist=dbCon.getValueArrayofKey("term", cidStr, "rid", col);
		return vallist;
			
	}
	
	
	
	
	/**
	 * ����һ������termstr,����������û��������������һ��������������ţ������ݱ�������һ������
	 * �����������и�����տ�һ���Ƿ��������Ӧ�ĸ������У������������û�����½���newconΪ�棩��
	 * ��������Ӧ������������һ������ţ����ݱ�������һ���������conorins������ʵ���������Ӹ���
	 * @param conorins conid or insid
	 * @param termstr
	 * @param newcon
	 * @param termcol
	 * @param datacol
	 * @param idcol
	 */
	public String addConTerm(String conorins,String termstr,Boolean newcon,DBCollection termcol,DBCollection datacol,DBCollection idcol){
		String[] Str=new String[1];
		Str[0]=termstr;
		String termid=new String();
		ArrayList<String> idlist=new ArrayList<String>();
		idlist=this.getDupConTermID(termstr, termcol);
		if (idlist.size()==0){
			String id=this.getID(conorins, idcol);
			String[] idStr=new String[1];
			idStr[0]=id;
			termid=id;
			this.addTermDocument(termstr, termcol);
			this.addConTermID(termstr, id, termcol);
			this.addConcept(id, termstr, datacol);
			dbCon.addKeyandValueArray(noIDrelofCon.get(0), idStr, noIDrelofCon.get(1), termstr, datacol);
			}else {
			if(newcon) {
				String id=this.getID(conorins, idcol);
				String[] idStr=new String[1];
				idStr[0]=id;
				termid=id;
				this.addConTermID(termstr, id, termcol);
				this.addConcept(id, termstr, datacol);
				dbCon.addKeyandValueArray(noIDrelofCon.get(0), idStr, noIDrelofCon.get(1), termstr, datacol);
				
			}else termid=idlist.get(0);//�������е�ID
		}
			
	return termid;
		
	}
	
	
	
	
	/**
	 * ����һ�����򵽹�ϵ�б��У�����ѹ��򿴳�һ����ϵ������û�����ϵ�������ڹ�ϵ��ĺ��������һ��rule һ����һ����Ԫ���ַ������飬��һ���ǹ���ͷ���ڶ����ǹ����塣
	 * @param attorrel
	 * @param termstr
	 * @param newrel
	 * @param termcol
	 * @param datacol
	 * @param idcol
	 */
	public void addRuleTerm(String termstr,Boolean isnewRule,String ruleHead,String ruleBody,DBCollection termcol,DBCollection datacol,DBCollection idcol){
		String id=null;
		
		ArrayList<String> idlist=new ArrayList<String>();
		idlist=this.getDupRelTermID(termstr, termcol);
		if (idlist.size()==0){
			
			id=getID("relid", idcol);
			addTermDocument(termstr, termcol);
			addRelTermID(termstr, id, termcol);
			addRelationNoni(id,termstr,datacol);
			}else{
				if(isnewRule) {
					id=this.getID("relid", idcol);
					addRelTermID(termstr, id, termcol);
					addRelationNoni(id, termstr, datacol);
					
				}
				
			}
		
		delRelationValueofAtt(id, "rule", "0000000000", datacol);
		addRelationValueofAtt(id, "rule", "r"+id+ruleHead, datacol);
		addRelationValueofAtt(id, "rule", ruleBody, datacol);
		String content="\r\n"+"r"+id+ruleHead+ruleBody;
		//д�ŵ�pl�ļ�����
		try {
			PrologFile p=new PrologFile();
			p.addContentToProFile("e:/excel/", "people.pl", content);//����ֻ���ļ������ĺ����������ŵ�ConRelAdmin����
			System.out.println("writefile ok!");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("UnknownHostException ok!");
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			System.out.println("MongoException ok!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException ok!");
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	public ArrayList<ArrayList<String>> reasonProFile(String rule){
		
		ArrayList<ArrayList<String>> result= new ArrayList<ArrayList<String>>();
		PrologCKI pcki=new PrologCKI();
		result=pcki.reasonPro("e:/excel/people.pl", rule);
		System.out.println(result);
//		try {
//		//	PrologFile p=new PrologFile();
//			
//			
//			try {
//				
//			//	result=p.reasonPro("e:/excel/people.pl", rule);
//				
//				
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvalidTheoryException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (MalformedGoalException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoSolutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoMoreSolutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MongoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return result;
	}
	
	
	
	/**
	 * ����һ����ϵ����termstr ÿ�����ﶼ��һ�����ϵ��ntermstr���棬��������ĺ������һ�����桱 �֣���������idҲ�Ǽ���1�������ϵ��˫����
	 * ��ϵ�ǵ��š�
	 * @param attorrel  relid or attid
	 * @param termstr
	 * @param newrel
	 * @param termcol
	 * @param datacol
	 * @param idcol
	 */
	public void addRelTerm(String attorrel,String termstr,Boolean newrel,DBCollection termcol,DBCollection datacol,DBCollection idcol){
		
		String[] Str=new String[1];
		String[] nStr=new String[1];
		Str[0]=termstr;
		String ntermstr;
		ntermstr=termstr.concat("��");
		nStr[0]=ntermstr;
		ArrayList<String> idlist=new ArrayList<String>();
		idlist=this.getDupRelTermID(termstr, termcol);
		if (idlist.size()==0){
			String id=this.getID(attorrel, idcol);
			this.addTermDocument(termstr, termcol);
			this.addRelTermID(termstr, id, termcol);
			String nid=String.valueOf(Integer.valueOf(id)+1);
			this.addTermDocument(ntermstr, termcol);
			this.addRelTermID(ntermstr, nid, termcol);
			this.addRelation(id, termstr, datacol);
			}else {
			if(newrel) {
				String id=this.getID(attorrel, idcol);
				this.addRelTermID(termstr, id, termcol);
				String nid=String.valueOf(Integer.valueOf(id)+1);
				this.addRelTermID(ntermstr, nid, termcol);
				this.addRelation(id, termstr, datacol);
				
			}
			
		}
		
	}
	
	
	
	
	
	
	/**
	 * ���������в������������������һ������Ϊtermstr���ĵ�
	 * @param termstr
	 * @param termcol
	 */
	
	public void addTermDocument(String termstr,DBCollection termcol){
		String[] Str=new String[1];
		Str[0]=termstr;
		dbCon.addDocument("term", Str, termcol);	
		
		
	}
	
	
	/**
	 * ��������е�cid�����в���������һ������ĸ���š�
	 * @param termstr
	 * @param idstr
	 * @param termcol
	 */
	public void addConTermID(String termstr,String idstr,DBCollection termcol){
		String[] Str=new String[1];
		Str[0]=termstr;
		dbCon.addKeyandValueArray("term", Str, "cid", idstr, termcol);
		
		
	}
	
	/**
	 * ������������Ӧ���id�ţ���ֱ������һ��������������У������������ֻ��Ӧһ��id����ֱ�Ӹ���term �õ��ķ�������ɾ����������һ���µ����
	 * @param newTerm
	 * @param oldTerm
	 * @param colTerm
	 * @param cidOrrid
	 * @param termId
	 * @param hasIds
	 */
	public void updateTerm(String newTerm,String oldTerm,DBCollection colTerm,String cidOrrid,String termId,boolean hasIds){
		
		String[] strOld=new String[1];
		strOld[0]=oldTerm;
		String[] strNew=new String[1];
		strNew[0]=newTerm;
		
		if (hasIds){
			
			dbCon.addDocument("term", strNew, colTerm);
			dbCon.addKeyandValueArray("term", strNew, cidOrrid, termId, colTerm);
			
		}else{
			
			
			dbCon.delDocument("term", strOld, colTerm);
			dbCon.addDocument("term", strNew, colTerm);
			dbCon.addKeyandValueArray("term", strNew, cidOrrid, termId, colTerm);
			
		}
		
		
	}
	
	/**
	 * ��������е�rid�����в���������һ������ĸ���š�
	 * @param termstr
	 * @param idstr
	 * @param termcol
	 */
	public void addRelTermID(String termstr,String idstr,DBCollection termcol){
		String[] Str=new String[1];
		Str[0]=termstr;
		dbCon.addKeyandValueArray("term", Str, "rid", idstr, termcol);
		
		
	}
	
	
	
	
	/**
	 * ����һ���������ݿ⣬ֻ�����ݿ⣬û�б�
	 */
	public DB creatOntoDB(String dbName) throws UnknownHostException, MongoException{
		
		DB ontoDB=dbCon.creatDb(dbName);
		return ontoDB;
		
	}
	
	/**
	 * ���ɱ������ݿ��е�һ����
	 * @param db
	 * @param tableName
	 * @return
	 */
	
	public DBCollection creatTable(DB db,String tableName){
		
		DBCollection col=dbCon.creatTable(db, tableName);
		return col;
		
	}
	
	
	public void addConDocument(DBObject doc,DBCollection col){
		
		col.insert(doc);
		
	}
	
	
	/**
	 * ����һ������
	 * @param cid
	 * @param syn
	 * @param gloss
	 * @param col
	 */
	public void addConcept(String cid,String syn,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.addDocument("cid", cidStr, col);
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, noIDrelofCon.get(1), syn, col);
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, noIDrelofCon.get(2), "����������˵��", col);
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, noIDrelofCon.get(2), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, noIDrelofCon.get(3), "English Synonym", col);
		
	}
	/**
	 * ��Ȩֵ��������һ���ĵ�
	 * @param cid
	 * @param col
	 */
	public void addConWeightDoc(String cid,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.addDocument("cid", cidStr, col);
				
	}
	
	/**
	 * ��һ��Ȩֵ���е�һ���ĵ�������һʲֵ�ԣ��� ���Ȩֵ
	 * @param cid
	 * @param subCid
	 * @param weight
	 * @param col
	 */
	public void addConWeightVal(String cid,String subCid, float weight,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
	
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, subCid, weight, col);
		
	}
	
	/**
	 * �õ�һ����������Ӧ��Ȩֵ,��float��
	 * @param cid
	 * @param rel
	 * @param col
	 * @return
	 */
	public float getConWeightVal(String cid,String rel,DBCollection col){
		
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		float val=(float) dbCon.getKeysDoubleValue(noIDrelofCon.get(0), cidStr, rel, col);
		return val;
		
	}
	
	
	/**
	 * �õ�һ��������ĵ���
	 * @param cid
	 * @param col
	 * @return
	 */
	public DBObject getConDocument(String cid,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
	
		DBObject doc=dbCon.findOneDocument("cid", cidStr, col);
		return doc;
		
	}
	
	/**
	 * �����Ĺ���������ӽڵ㣬relid��ʾisa��ϵ��id�š�
	 * @param relid
	 * @param superConID
	 * @param subConID
	 * @param colCon
	 * @param colRel
	 */
	public void addSubConcept(String relid,String superConID,String subConID,DBCollection colCon,DBCollection colRel){
		
		this.addConceptRel(subConID, relid, superConID, colCon);
				
		//��鸸�ڵ����Ƿ��п��Լ̳еĹ�ϵ�����ԣ�����У��򽫸����Ժ�ֵ���ȡ�ӽڵ���
		ArrayList<String> rellist=new ArrayList<String>();
		ArrayList<String> inhlist=new ArrayList<String>();
		rellist=this.getPartRelsofConcept(superConID, colCon);
		for(int i=0;i<rellist.size();i++){
			if(this.getPropofRel(rellist.get(i), "prop",colRel).contains(this.propofRel.get(4)))
				inhlist.add(rellist.get(i));
		}//�õ����ڵ���м̳��ԵĹ�ϵ�б�
		for(int j=0;j<inhlist.size();j++){
			ArrayList<String> vallist=this.getConceptofRel(superConID, inhlist.get(j), colCon);
			this.addConceptRel(subConID, inhlist.get(j), vallist, colCon);
			//cra.addConceptValueofRel(subConID, rel, valnew, col)
		}
	}
		
	
	
	
	
	/**
	 * ����һ������������͹�ϵ������������һ��������
	 * @param cid
	 * @param syn
	 * @param gloss
	 * @param col
	 */
	
	/*
	public void addTerm(String term,String cid,DBCollection col){
		String[] termStr=new String[1];
		termStr[0]=term;
		dbCon.addDocument("term", termStr, col);
		dbCon.addKeyandValueArray("term", termStr,noIDrelofCon.get(0), cid,col);
		
	}
	*/
	
	/**
	 * ����һ����ϵ�����й̶���ϵ��rid, dom, ran, syn, gloss, prop
	 * ���ﲻ�������ϵ�������Ҽ���һ��rule
	 * ����prop������Ϊpro rfl sym tra fun inh
	 * @param cid
	 * @param syn
	 * @param col
	 */
	public void addRelationNoni(String rid,String syn,DBCollection col){
		String r1=rid;
		String[] ridStr1=new String[1];
		ridStr1[0]=r1;
		dbCon.addDocument(noIDrelofRel.get(0), ridStr1, col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(1), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(2), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(3), syn, col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(4), "English Synonym", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "�������ϵ˵��", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(6), "pro", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(7), "0000000000", col);
		
	}
	
	
	/**
	 * ����һ����ϵ�����й̶���ϵ��rid, dom, ran, syn, gloss, prop
	 * ͬʱ������һ�����ϵ��
	 * ����prop������Ϊpro rfl sym tra fun inh
	 * @param cid
	 * @param syn
	 * @param col
	 */
	public void addRelation(String rid,String syn,DBCollection col){
		String r1,r2;
		r1=rid;
		r2=String.valueOf(Integer.valueOf(rid)+1);
		
		String[] ridStr1=new String[1];
		ridStr1[0]=r1;
		dbCon.addDocument(noIDrelofRel.get(0), ridStr1, col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(1), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(2), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(3), syn, col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(4), "English Synonym", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "�������ϵ˵��", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(6), "pro", col);
		
		String[] ridStr2=new String[1];
		ridStr2[0]=r2;
		dbCon.addDocument(noIDrelofRel.get(0), ridStr2, col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(1), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(2), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(3), syn+"��", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(4), "English Synonym", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(5), "�������ϵ˵��", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(5), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(6), "pro", col);
		
	}
	
	
	
	/**
	 * Ϊ���ڹ�ϵ�б��ڼ����ϵ������ڵ�
	 * @param rid
	 * @param syn
	 * @param col
	 */
	public void addRelationForRootNode(DBCollection col){
		
		String[] ridStr1=new String[1];
		ridStr1[0]="2000000000";
		dbCon.addDocument(noIDrelofRel.get(0), ridStr1, col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(1), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(2), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(3), "��ϵ", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(4), "English Synonym", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "�������ϵ˵��", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(6), "pro", col);
		
		
		
		
	}
	
	
	
	
	
	/**�����������
	 * ���·�IDʽ��ϵ������ֻ��syn gloss��ϵ�ĸ���
	 * fΪtrueʱ���� syn  fΪfalseʱ���� gloss
	 * @param cid
	 * @param valold(ԭ����ֵ)
	 * @param valnew
	 * @param f
	 * @param col
	 */
	/*public void updateConceptSynGloss(String cid,String valold,String valnew,Boolean f,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		if(f)
		{
		dbCon.delOneofKeyandValueArray("cid", cidStr, "syn", valold, col);
		dbCon.addKeyandValueArray("cid", cidStr, "syn", valnew, col);
		}
		
		else
		{
		dbCon.delOneofKeyandValueArray("cid", cidStr, "gloss", valold, col);
		dbCon.addKeyandValueArray("cid", cidStr, "gloss", valnew, col);
		}
		
	}*/
	
	
	/**
	 * ����һ������Ĺ�ϵ�Լ���ϵֵ������Ĺ�ϵֵ��һ���������Ƕ������ͬʱ��ϵֵ����Ҳ������һ�����ϵ�����ϵֵ
	 * @param cid
	 * @param rel
	 * @param relval
	 * @param col
	 */
	
	public void addConceptRel(String cid,String rel,String relval,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, rel, relval, col);
		int r=Integer.valueOf(rel);
		if(r%2==0)r=r-1;else r=r+1;
		String relStr=String.valueOf(r);
		String[] relvalStr=new String[1];
		relvalStr[0]=relval;
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), relvalStr, relStr, cid, col);
		//dbCon.addKeyandValueArray("cid", cidStr, "gloss", gloss, col);
		
	}
	
	/**
	 * ����ֻΪһ���������һ����ϵ����ϵֵΪ""
	 * @param cid
	 * @param rel
	 * @param col
	 */
	public void addOnlyRelNoValueOfConcept(String cid,String rel,DBCollection col){
		
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, rel,"1000000000", col);
		
		
	}
	
	/**
	 * ��ͨ��ǰ̨��������ӹ�ϵ�͹�ϵֵʱ�����ӹ�ϵ����Ĭ�ϸ���һ����1000000000�����������id�ţ������ڸ������ϵ����ֵʱ��
	 * Ҫ�ȿ�һ���ǲ��ǡ�1000000000��������ǣ�����³��µĸ���ţ���Ϊ������������һ�����ϵ��������ǣ����������ϵ��������һ����ϵֵ��
	 * ͬʱ���������Ҳ������һ�����ϵ
	 * @param cid
	 * @param rel
	 * @param relval
	 * @param col
	 */
	public void addVauleToConceptRel(String cid,String rel,String relval,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		if (relval.equals("1000000000")){
			
			this.updateConceptValueofRel(cid, rel, "1000000000", relval, col);
		}else{
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, rel, relval, col);
		}
		int r=Integer.valueOf(rel);
		if(r%2==0)r=r-1;else r=r+1;
		String relStr=String.valueOf(r);
		String[] relvalStr=new String[1];
		relvalStr[0]=relval;
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), relvalStr, relStr, cid, col);
		//dbCon.addKeyandValueArray("cid", cidStr, "gloss", gloss, col);
		
	}
	
	
	
	
	
	
	/**
	 * ��������һ������Ĺ�ϵ�Ĺ�ϵֵ
	 * @param cid
	 * @param rel
	 * @param relval
	 * @param col
	 */
	public void addConceptRel(String cid,String rel,ArrayList<String> relval,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		for(int i=0;i<relval.size();i++){
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, rel, relval.get(i), col);
		}
		int r=Integer.valueOf(rel);
		if(r%2==0)r=r-1;else r=r+1;
		String relStr=String.valueOf(r);
		for(int j=0;j<relval.size();j++){
		String[] relvalStr=new String[1];
		relvalStr[0]=relval.get(j);
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), relvalStr, relStr, cid, col);
		}
		//dbCon.addKeyandValueArray("cid", cidStr, "gloss", gloss, col);
		
	}
	
	
	
	/**
	 * ���ڸ��¸���Ĺ�ϵ�Ĺ�ϵֵ������ȥ���ģ�����������
	 * @param cid
	 * @param rel
	 * @param valold
	 * @param valnew
	 * @param col
	 */
	public void updateConceptValueofRel(String cid,String rel,String valold,String valnew,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, rel, valnew, col);
		dbCon.delOneofKeyandValueArray(noIDrelofCon.get(0), cidStr, rel, valold, col);
	
	}
	
	/**
	 *ɾ��������ĳ��ϵ��ֵ
	 * @param cid
	 * @param rel
	 * @param valold
	 * @param col
	 */
	public void delConceptValueofRel(String cid,String rel,String valold,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.delOneofKeyandValueArray(noIDrelofCon.get(0), cidStr, rel, valold, col);
	
	}
	
	
	/**
	 * �������ĳ����ϵ����ֵ�����Ǹ���
	 * @param cid
	 * @param rel
	 * @param valold
	 * @param valnew
	 * @param col
	 */
	public void addConceptValueofRel(String cid,String rel,String valnew,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, rel, valnew, col);
		
	
	}
	
	/**
	 * �������ĳ����ϵ������ӹ�ϵֵ
	 * @param cid
	 * @param rel
	 * @param valnew
	 * @param col
	 */
	public void addConceptValueofRel(String cid,String rel,ArrayList<String> valnew,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		for(int i=0;i<valnew.size();i++){
			dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, rel, valnew.get(i), col);
			
		}
		
	
	}
	
	
	/**
	 * ���¹�ϵ������ֵ�������Ǹ��ģ�����������.�������Ĺ�ϵֵold,���ڹ�ϵֵ���У���Ὣ��ֵ���ӵ���ϵֵ���У�
	 * �������Ĺ�ϵold�ڱ��У����¹�ϵֵ���档
	 * ������������һ��bug,����ʵ��Ӧ�úͲ����У���ϵֵold�϶����ڹ�ϵֵ���еġ�
	 * @param rid
	 * @param att
	 * @param valold
	 * @param valnew
	 * @param col
	 */
	public void updateRelationValueofAtt(String rid,String att,String valold,String valnew,DBCollection col){
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr, att, valnew, col);
		dbCon.delOneofKeyandValueArray(noIDrelofRel.get(0), ridStr, att, valold, col);
	
	}
	
	
	
	/**
	 * ɾ����ϵ������ֵ
	 * @param rid
	 * @param att
	 * @param valold
	 * @param col
	 */
	public void delRelationValueofAtt(String rid,String att,String valold,DBCollection col){
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		dbCon.delOneofKeyandValueArray(noIDrelofRel.get(0), ridStr, att, valold, col);
	
	}
	
	
	
	/**
	 * ����ϵ����������ֵ�����Ǹ��¡�
	 * @param rid
	 * @param att
	 * @param valnew
	 * @param col
	 */
	public void addRelationValueofAtt(String rid,String att,String valnew,DBCollection col){
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr, att, valnew, col);
	
	}
	
	/**
	 * ����ϵ���Ӷ������ֵ�����������ĳ����ϵ�Ķ�������������ϵ����ֵ��
	 * @param rid
	 * @param domorran
	 * @param val
	 * @param col
	 */
	public void addDomainorRangeofRel(String rid,String domorran,String val,DBCollection col){
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		int r=Integer.valueOf(rid);
		if(r%2==0)r=r-1;else r=r+1;
		String relStr=String.valueOf(r);
		String[] relvalStr=new String[1];
		relvalStr[0]=relStr;
		if(domorran==noIDrelofRel.get(1))
		{
			dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr, noIDrelofRel.get(1), val, col);
			dbCon.addKeyandValueArray(noIDrelofRel.get(0), relvalStr, noIDrelofRel.get(2), val, col);
		}
			
		if(domorran==noIDrelofRel.get(2))
		{
			dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr, noIDrelofRel.get(2), val, col);
			dbCon.addKeyandValueArray(noIDrelofRel.get(0), relvalStr, noIDrelofRel.get(1), val, col);
		}
		
			
	}
	
	/**
	 * ���ӹ�ϵ�Ĺ�ϵֵ����������Ĺ�ϵ��id�ŵġ�
	 * @param rid
	 * @param rel
	 * @param relval
	 * @param col
	 */
	
	public void addRelationRel(String rid,String rel,String relval,DBCollection col){
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr, rel, relval, col);
		int r=Integer.valueOf(rel);
		if(r%2==0)r=r-1;else r=r+1;
		String relStr=String.valueOf(r);
		String[] relvalStr=new String[1];
		relvalStr[0]=relval;
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), relvalStr, relStr, rid, col);
		//dbCon.addKeyandValueArray("cid", cidStr, "gloss", gloss, col);
		
	}
	
	/**
	 * �޸Ĺ�ϵ�������Ǹ���͹�ϵ��id�ֶμ���ֵ�ǲ��������޸ĵģ����Գ��������û�����ֺ�����
	 * @param cid
	 * @param valold
	 * @param valnew
	 * @param col
	 */
	public void updateConceptRel(String cid,String valold,String valnew,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.updateKey(noIDrelofCon.get(0), cidStr, valold, valnew, col);
	}
	
	public void delConDocument(String cid,String val,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=val;
		
		dbCon.delDocument(cid, cidStr, col);
		
	}
	
	/**
	 * ɾ��һ������ķ���,ע�⿼��Ҫ���ø����������������е�ֵҲҪɾ����
	 * @param cid Ҫɾ���ĸ����id��
	 * @param colCon
	 * @param colPool
	 */
	public void delOneConcept(String cid,DBCollection colCon,DBCollection colPool){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		ArrayList<String> keyArray=getPartRelsofConcept(cid, colCon);
		keyArray.removeAll(noIDrelofCon);
		for(int i=0;i<keyArray.size();i++){
			ArrayList<String> valList=getConceptofRel(cid,keyArray.get(i),colCon);
			
			for(int j=0;j<valList.size();j++){
				int r=Integer.valueOf(keyArray.get(i).toString());
				if(r%2==0) r=r-1;else r=r+1;
				String relStr=String.valueOf(r);
				String[] valStr=new String[1];
				valStr[0]=valList.get(j);
				dbCon.delOneofKeyandValueArray(noIDrelofCon.get(0), valStr, relStr, cid, colCon);
				boolean a=!this.getPartRelsofConcept(valList.get(j), colCon).contains("2100000001");
				boolean b=!valList.get(j).contains("1000000000");
				//�����ʾ���ܰѸ���������ڵ�ɾ����
				if(a&&b)
				
				{
					if(!valList.get(j).equals("0000000000"))//����ɾ����ֵ
					this.delOneConcept(valList.get(j), colCon, colPool);//�ݹ�ɾ��һ���ӽڵ�
									
				}
				
					
				}
			
		}
		
		
		
		dbCon.delDocument(noIDrelofCon.get(0), cidStr, colCon);
		this.putID(cid, colPool);
	}
	
	
	/**
	 * ɾ��һ����ϵ�ķ���,ע�⿼��Ҫ���ù�ϵ��������ϵ���е�ֵҲҪɾ�����Ǵӹ�ϵ���ݱ���ɾ��һ����ϵ
	 * @param cid Ҫɾ���ĸ����id��
	 * @param colCon
	 * @param colPool
	 */
	public void delOneRelation(String rid,DBCollection colCon,DBCollection colPool){
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		ArrayList<String> keyArray=getPartRelsofRelation(rid, colCon);
		keyArray.removeAll(noIDrelofRel);
		for(int i=0;i<keyArray.size();i++){
			ArrayList<String> valList=getPropofRel(rid,keyArray.get(i),colCon);
			
			for(int j=0;j<valList.size();j++){
				int r=Integer.valueOf(keyArray.get(i).toString());
				if(r%2==0) r=r-1;else r=r+1;
				String relStr=String.valueOf(r);
				String[] valStr=new String[1];
				valStr[0]=valList.get(j);
				dbCon.delOneofKeyandValueArray(noIDrelofRel.get(0), valStr, relStr, rid, colCon);
				boolean a=!this.getPartRelsofRelation(valList.get(j), colCon).contains("2100000001");
				boolean b=!valList.get(j).contains("2000000000");
				//�����ʾ���ܰѸ���������ڵ�ɾ����
				if(a&&b)
				
				{
					
					this.delOneRelation(valList.get(j), colCon, colPool);//�ݹ�ɾ��һ���ӽڵ�
									
				}
				
					
				}
			
		}
		
		
		
		dbCon.delDocument(noIDrelofRel.get(0), ridStr, colCon);
		this.putID(rid, colPool);
	}
	
	
	
	/**
	 * ɾ��һ�������ID
	 * @param valStr
	 * @param delkeyStr
	 * @param delvalStr
	 * @param col
	 */
	
	public void delTermId(String valStr,String delkeyStr,String delvalStr,DBCollection col){
		
		String[] str=new String[1];
		str[0]=valStr;
		dbCon.delOneofKeyandValueArrayandDocument("term", str, delkeyStr, delvalStr, col);
		
		
	}
	
	
	
	/**
	 * ��ѯһ�������е�ĳ����ϵ�Ĺ�ϵֵ��
	 * @param cid
	 * @param rel
	 * @param col
	 * @return
	 */
	public ArrayList<String> getConceptofRel(String cid,String rel,DBCollection col){
		
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		ArrayList<String> vallist=dbCon.getValueArrayofKey(noIDrelofCon.get(0), cidStr, rel, col);
		return vallist;
		
	}
	

	
	
	
	/**
	 * ���������ȡ�������Ӧ��id�ŵ��б�
	 * @param term ����
	 * @param idType ��������"cid" ��ϵ����"rid"
	 * @param col ��ϵ��������������
	 * @return
	 */
	public ArrayList<String>  getTermId(String term,String idType,DBCollection col){
		
		String[] termStr=new String[1];
		termStr[0]=term;
		ArrayList<String> idlist=dbCon.getValueArrayofKey("term", termStr, idType, col);
		
		return idlist;
		
	}
	
	
	/**
	 * �õ���ϵ�Ĺ�ϵֵ��
	 * @param rid
	 * @param rel ���relΪprop��õ���ϵ������
	 * @param col
	 * @return
	 */
	public ArrayList<String> getPropofRel(String rid,String rel,DBCollection col){
		
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		ArrayList<String> vallist=dbCon.getValueArrayofKey(noIDrelofRel.get(0), ridStr, rel, col);
		return vallist;
		
	}
	
	
	
	
	/**
	 * ɾ��һ����ϵ�����Ǹ������������õ��ù�ϵ�ĸ�����Ҳ�Ὣ�ù�ϵ�Լ���ϵֵɾ����
	 * @param rid
	 * @param conCol
	 * @param relCol
	 */
	public void delOneRelationofConcepts(String rid,DBCollection conCol,DBCollection relCol){
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		int r1=0,r2=0, r=Integer.valueOf(rid);
		if(r%2==0) {r1=r-1;r2=r;}else {r2=r+1;r1=r;}//r1��r2��ʾ��ϵ�͹�ϵ��
		ArrayList<String> conArray1=getConceptWithRel(String.valueOf(r1),conCol);
		for(int i=0;i<conArray1.size();i++){
			String[] cidStr=new String[1];
			cidStr[0]=conArray1.get(i);
			dbCon.delKeyandValue(noIDrelofCon.get(0), cidStr, String.valueOf(r1), conCol);
		}
		
		ArrayList<String> conArray2=getConceptWithRel(String.valueOf(r2),conCol);
		for(int i=0;i<conArray2.size();i++){
			String[] cidStr=new String[1];
			cidStr[0]=conArray2.get(i);
			dbCon.delKeyandValue(noIDrelofCon.get(0), cidStr, String.valueOf(r2), conCol);
		}
		String[] rStr=new String[1];
		rStr[0]=String.valueOf(r1);					
		dbCon.delDocument(noIDrelofRel.get(0), rStr, relCol);
		rStr[0]=String.valueOf(r2);
		dbCon.delDocument(noIDrelofRel.get(0), rStr, relCol);
	}
	
	
	
	/**
	 * �ҵ�����ĳ��ϵ�ĸ���ϡ�������ǹ�ϵ��������Ǹ���ϡ�
	 * @param rel
	 * @param col
	 * @return
	 */
	public ArrayList<String> getConceptWithRel(String rel,DBCollection col){
		DBCursor cur=dbCon.findDocumentWithKey(rel, col);
		ArrayList<String> conlist=new ArrayList<String>();
		//ArrayList<String> conlist2=new ArrayList();
		while (cur.hasNext()){
			conlist.add(cur.next().toMap().values().toArray()[1].toString());//����㣬�������3��7��id�ŵĳ�����صĲ�����ֱ��Ӱ�쵽id�ŵĶԱ�
		}
		return conlist;
	}
	
	/**
	 * ��ȡ�����ȫ����ϵ���ϡ�����cid syn gloss
	 * @param conID
	 * @param col
	 * @return
	 */
	public ArrayList<String> getAllRelsofConcept(String conID,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=conID;
		Object[] keys=dbCon.getKeySet(noIDrelofCon.get(0), cidStr, col);
		ArrayList<String> keysStr=new ArrayList<String>();
		for(int i=1;i<keys.length;i++)//������i=1��ʼ����˿���ȥ��  _id ��
			keysStr.add(keys[i].toString());
		return keysStr;
				
	}
	
	/**
	 * ��ȡ��ϵ��ȫ����ϵ���� ������rid syn....
	 */
	public ArrayList<String> getAllRelsofRelation(String relID,DBCollection col){
		String[] ridStr=new String[1];
		ridStr[0]=relID;
		Object[] keys=dbCon.getKeySet(noIDrelofRel.get(0), ridStr, col);
		ArrayList<String> keysStr=new ArrayList<String>();
		for(int i=1;i<keys.length;i++)//������i=1��ʼ����˿���ȥ��  _id ��
			keysStr.add(keys[i].toString());
		return keysStr;
				
	}
	
	
	/**
	 * ֻ�õ����ֵĸ���Ĺ�ϵ���ϣ�ȥ����cid syn gloss��
	 * @param conID
	 * @param col
	 * @return
	 */
	public ArrayList<String> getPartRelsofConcept(String conID,DBCollection col){
		ArrayList<String> keysStr=new ArrayList<String>();
		keysStr=getAllRelsofConcept(conID,col);
		keysStr.removeAll(noIDrelofCon);
		return keysStr;
		
	}
	
	/**
	 * ֻ�õ����ֵĹ�ϵ�Ĺ�ϵ���ϣ�ȥ����cid syn gloss��
	 * @param conID
	 * @param col
	 * @return
	 */
	public ArrayList<String> getPartRelsofRelation(String relID,DBCollection col){
		ArrayList<String> keysStr=new ArrayList<String>();
		keysStr=getAllRelsofRelation(relID,col);
		keysStr.removeAll(noIDrelofRel);
		return keysStr;
		
	}
	
	
	

	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub
		ConRelAdmin cra=new ConRelAdmin();
		DB db=cra.creatOntoDB("ontopeople");
		DBCollection colConData=cra.creatTable(db, "ConData");
		DBCollection colConTerm=cra.creatTable(db, "ConTerm");
		DBCollection colRelData=cra.creatTable(db, "RelData");
		DBCollection colRelTerm=cra.creatTable(db, "RelTerm");
		DBCollection colId=cra.creatTable(db, "idPool");
		
	//	System.out.println(cra.getTermId("11", "cid", colConTerm));
		//String str=new String();
		//str="nn��";
		//System.out.println(str.substring(0, str.length()-1));
		//cra("2100000018", colRelData, colId);
		//System.out.println(cra.getPartRelsofRelation("2000000000", colRelData));
		//System.out.println(cra.getPropofRel("2000000000", "2100000002", colRelData));
		//System.out.println(cra.getConceptofRel("1000000000", "2100000002", colConData));
		//cra.addRelationForRootNode(colRelData);
//		cra.addRelTerm("relid", "subclass_of", true, colRelTerm, colRelData, colId);
		//cra.delTermId("newterm8", "cid", "1000000058", colConTerm);
		//cra.updateTerm("�ٴβ���", "newterm1", colConTerm, "cid", "1000000022", true);
		//cra.delOneConcept("1000000069", colConData, colId);
		//cra.putID("1000000032", colId);
		//cra.delTermId("newterm0", "cid", "1000000041", colConTerm);
		//cra.delOneConcept("1000000027", colConData, colId);
		//System.out.println(cra.getTermId("test1", "cid", colConTerm));
		//cra.generateID(colId);     
		//cra.addConceptRel("1000000019", "2100000001", "1000000016", colConData);
		//cra.addConceptRel("1000000016", "2100000002", "1000000019", colConData);
		//cra.addConceptRel("1000000006", "2100000001", "1000000002", colConData);
		//cra.putID("1000000059", colId);
		//System.out.println(cra.getID("attid", colId));
		//System.out.println(cra.getDupConTermID("����", colConTerm));
		//cra.addConTerm("conid","����", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","����", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","��������", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","��������", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","�˶�Ա", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","����", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","����Ա", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","����", true, colConTerm, colConData, colId);
		//cra.addRelTerm("relid","��һ", true, colRelTerm, colRelData, colId);
		//System.out.println(cra.getConceptofRel("1000", "2001", colConData));
		//cra.addConTermID("����", "1006000000", colConTerm);		
//		cra.addConTermID("����", "1000001136", colConTerm);	
//		cra.addConTermID("��������", "1000001135", colConTerm);	
//		cra.addConTermID("��", "1100000001", colConTerm);	
//		cra.addConTermID("Ů", "1100000002", colConTerm);	
		//System.out.println(cra.getPartRelsofConcept("1005", colConData));
		//cra.delOneConcept("1001", colConData);
		//System.out.println(cra.findConceptofRel("1000", "2005", colConData));
		//System.out.println(cra.getConceptWithRel("syn", colConData).get(0));
		//cra.addTerm("���ӽڵ�", "2001", colRelTerm);
		//cra.addTerm("��һ", "2002", colRelTerm);
		//cra.addRelation("2001", "���ӽڵ�", colRelData);
		//cra.addRelation("2002", "��һ", colRelData);
		
		//System.out.println(cra.getPropofRel("2001", "prop", colRelData));
		//cra.addTerm("����", "1000", colConTerm);
		//cra.addConcept("1000","����" , colConData);
		//cra.addConcept("1005","����" , colConData);
		//DB db;
		//db=dbCon.creatDb("onto");
		//�������ɸ���
		//DBCollection conCol=dbCon.creatTable(db,"ConData");
		//DBCollection relCol=dbCon.creatTable(db,"RelData");
		//for(int i=1000;i<1010;i++){
		//	cra.addConcept("1000", "syn", "gloss", relCol);
		//}
		//�������ɹ�ϵ
		//for(int i=2001;i<2010;i++){
		//cra.addRelation("2013", "syn", colRelData);
		//cra.delOneRelation("2010", colConData, colRelData);
		//}
		//cra.addConcept("1000", "2003", "1001", col);
		//cra.addConceptRel("1000", "2006", "1003", colConData);
		//cra.updateConceptValueofRel("1000", "syn", "syn", "new1",col);
		//cra.addRelation("2001", "syn", "gloss", relCol);
		//cra.delOneConcept("1001", col);
		//cra.delOneRelation("2005", conCol, relCol);
		//cra.delRelationValueofAtt("2001", "prop", "rfl", relCol);
		//cra.addRelationValueofAtt("2006", "prop", "inh", colRelData);
		//cra.delConceptValueofRel("1003", "syn", "syn", conCol);
		//cra.updateRelationValueofAtt("2010", "syn", "syn", "sc", relCol);
		//cra.updateConceptValueofRel("1005", "syn", "syn", "test", conCol);
		
		//cra.delOneConcept("1000000137", colConData, colId);
	//	cra.addRuleTerm("newrule11", true, "(X,Y)", "r1(X,Z),r2(Y,Z)", colRelTerm, colRelData, colId);
		//cra.reasonProFile("r2100000001(1000000317,1000000000).");
		cra.reasonProFile("r2100000001(X,Y).");
		
		
		
		System.out.println("ok");
		//cra.delOneConcept("1009",col);
		
		
		
		//System.out.println(cra.findConceptWithRel("2003", col));
		
		
		
	}

}
