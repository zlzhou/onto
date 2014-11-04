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

/**该类的主要工作是对概念和关系进行增删改查
 * @author Administrator周子力
 * 
 * 
 *2014年1月7日
 */
@SuppressWarnings("unchecked")
public class ConRelAdmin {
	
	public static Dbcontrol dbCon=new Dbcontrol(); 
	public static ArrayList<String> noIDrelofCon=new ArrayList<String>();
	public ArrayList<String> noIDrelofRel=new ArrayList<String>();
	public ArrayList<String> propofRel=new ArrayList<String>();
	
	
	public ConRelAdmin(){
		//概念中没有ID号的关系，这里的顺序是不能变的。
		noIDrelofCon.add("cid");//0
		noIDrelofCon.add("csyn");//1
		noIDrelofCon.add("gloss");//2
		noIDrelofCon.add("esyn");//3
		
		//关系中没有ID号的关系，这里的顺序是不能变的
		noIDrelofRel.add("rid");//0
		noIDrelofRel.add("dom");//1
		noIDrelofRel.add("ran");//2
		noIDrelofRel.add("csyn");//3
		noIDrelofRel.add("esyn");//4
		noIDrelofRel.add("gloss");//5
		noIDrelofRel.add("prop");//6
		noIDrelofRel.add("rule");//7
		
		//关系的属性  rfl sym tra inh fun invfun
		propofRel.add("rfl");//1自反性
		propofRel.add("sym");//2对称性
		propofRel.add("tra");//3传递性
		propofRel.add("inh");//4继承性
		propofRel.add("fun");//5函数性
		propofRel.add("invfun");//6反函数性
		
	
		
		
	}
	
	
	
	/**
	 * 1开头的是概念ID，2开头的是关系ID
	 * 对于概念：如果第2位是0，则是指的抽象概念，如果是1，则是指的具体概念也就是实例。
	 * 对于关系： 对于关系号，如果第2位是0，表示此关系是属性，如果是1，则表示是关系。
	 * 
	 * 对于关系，里面产生的都是单号，双号可以在取出号码后自动加1产生。用于作为逆关系的ID号。
	 * 
	 * 此处在产生id号时有问题，估计是mongodb
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
	 *方法的功能是从ID池中获取id号，一共有四种，分别是抽象概念，实例，属性，关系
	 *取出后，则id池中就少一个号
	 * @param typestr，分别对应四种类型，conid insid attid relid
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
	 * 将删除掉的ID号放回idpool 函数会自动检查是属于哪一个类型的id
	 * 对于关系属性的ID号，只放单号，因为双号是逆关系号
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
	 * 查找术语表中有没有和输入相同的术语，如果有，则返回术语列表，如果没有，则返回size为0的list
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
	 * 查找术语表中有没有和输入相同的术语，如果有，则返回术语列表，如果没有，则返回size为0的list
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
	 * 增加一个术语termstr,如果术语表中没有这个术语，则新增一个术语，并分配概念号，在数据表中增加一个概念
	 * 如果术语表中有该术语，刚看一下是否有这个对应的概念，如果有，不操作，如果没有则新建（newcon为真），
	 * 在术语表对应的术语中增加一个概念号，数据表中增加一个概念。其中conorins是增加实例还是增加概念
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
				
			}else termid=idlist.get(0);//返回已有的ID
		}
			
	return termid;
		
	}
	
	
	
	
	/**
	 * 增加一条规则到关系列表中，这里把规则看成一个关系，但是没有逆关系，并且在关系体的后面加上了一个rule 一共是一个二元的字符串数组，第一个是规则头，第二个是规则体。
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
		//写放到pl文件当中
		try {
			PrologFile p=new PrologFile();
			p.addContentToProFile("e:/excel/", "people.pl", content);//这里只对文件操作的函数，不妨放到ConRelAdmin类中
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
	 * 增加一个关系术语termstr 每个术语都有一个逆关系用ntermstr代替，其中术语的后面加了一个“逆” 字，并且术语id也是加了1。即逆关系是双数。
	 * 关系是单号。
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
		ntermstr=termstr.concat("逆");
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
	 * 对术语表进行操作，在术语表中增加一个术语为termstr的文档
	 * @param termstr
	 * @param termcol
	 */
	
	public void addTermDocument(String termstr,DBCollection termcol){
		String[] Str=new String[1];
		Str[0]=termstr;
		dbCon.addDocument("term", Str, termcol);	
		
		
	}
	
	
	/**
	 * 对术语表中的cid键进行操作，增加一个术语的概念号。
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
	 * 如果术语里面对应多个id号，则直接增加一个术语在术语表中，如果术语里面只对应一个id，则直接更新term 用到的方法是先删除，再增加一个新的术语。
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
	 * 对术语表中的rid键进行操作，增加一个术语的概念号。
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
	 * 生成一个本体数据库，只是数据库，没有表
	 */
	public DB creatOntoDB(String dbName) throws UnknownHostException, MongoException{
		
		DB ontoDB=dbCon.creatDb(dbName);
		return ontoDB;
		
	}
	
	/**
	 * 生成本体数据库中的一个表
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
	 * 增加一个概念
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
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, noIDrelofCon.get(2), "请输入中文说明", col);
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, noIDrelofCon.get(2), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofCon.get(0), cidStr, noIDrelofCon.get(3), "English Synonym", col);
		
	}
	/**
	 * 在权值表中增加一个文档
	 * @param cid
	 * @param col
	 */
	public void addConWeightDoc(String cid,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=cid;
		dbCon.addDocument("cid", cidStr, col);
				
	}
	
	/**
	 * 向一个权值表中的一个文档中增加一什值对，即 概念：权值
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
	 * 得到一个概念所对应的权值,是float型
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
	 * 得到一个概念的文档。
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
	 * 函数的功能是添加子节点，relid表示isa关系的id号。
	 * @param relid
	 * @param superConID
	 * @param subConID
	 * @param colCon
	 * @param colRel
	 */
	public void addSubConcept(String relid,String superConID,String subConID,DBCollection colCon,DBCollection colRel){
		
		this.addConceptRel(subConID, relid, superConID, colCon);
				
		//检查父节点中是否有可以继承的关系或属性，如果有，则将该属性和值添加取子节点中
		ArrayList<String> rellist=new ArrayList<String>();
		ArrayList<String> inhlist=new ArrayList<String>();
		rellist=this.getPartRelsofConcept(superConID, colCon);
		for(int i=0;i<rellist.size();i++){
			if(this.getPropofRel(rellist.get(i), "prop",colRel).contains(this.propofRel.get(4)))
				inhlist.add(rellist.get(i));
		}//得到父节点具有继承性的关系列表
		for(int j=0;j<inhlist.size();j++){
			ArrayList<String> vallist=this.getConceptofRel(superConID, inhlist.get(j), colCon);
			this.addConceptRel(subConID, inhlist.get(j), vallist, colCon);
			//cra.addConceptValueofRel(subConID, rel, valnew, col)
		}
	}
		
	
	
	
	
	/**
	 * 增加一个术语，这个概念和关系术语表可以用这一个方法。
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
	 * 新增一个关系，其中固定关系是rid, dom, ran, syn, gloss, prop
	 * 这里不增加逆关系。，并且加上一个rule
	 * 其中prop的内容为pro rfl sym tra fun inh
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
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "请输入关系说明", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(6), "pro", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(7), "0000000000", col);
		
	}
	
	
	/**
	 * 新增一个关系，其中固定关系是rid, dom, ran, syn, gloss, prop
	 * 同时会新增一个逆关系。
	 * 其中prop的内容为pro rfl sym tra fun inh
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
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "请输入关系说明", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(6), "pro", col);
		
		String[] ridStr2=new String[1];
		ridStr2[0]=r2;
		dbCon.addDocument(noIDrelofRel.get(0), ridStr2, col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(1), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(2), "0000000000", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(3), syn+"逆", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(4), "English Synonym", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(5), "请输入关系说明", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(5), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr2, noIDrelofRel.get(6), "pro", col);
		
	}
	
	
	
	/**
	 * 为了在关系列表在加入关系这个根节点
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
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(3), "关系", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(4), "English Synonym", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "请输入关系说明", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(5), "English gloss", col);
		dbCon.addKeyandValueArray(noIDrelofRel.get(0), ridStr1, noIDrelofRel.get(6), "pro", col);
		
		
		
		
	}
	
	
	
	
	
	/**这个函数作废
	 * 更新非ID式关系，这里只有syn gloss关系的更新
	 * f为true时更新 syn  f为false时更新 gloss
	 * @param cid
	 * @param valold(原来的值)
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
	 * 增加一个概念的关系以及关系值（这里的关系值是一个，而不是多个），同时关系值概念也会增加一个逆关系及逆关系值
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
	 * 这里只为一个概念加上一个关系，关系值为""
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
	 * 在通过前台给概念添加关系和关系值时，增加关系后，是默认给了一个“1000000000”，即概念的id号，所以在给这个关系增加值时，
	 * 要先看一下是不是“1000000000”，如果是，则更新成新的概念号，并为这个概念号增加一个逆关系。如果不是，则向这个关系中再增加一个关系值，
	 * 同时，这个概念也会增加一下逆关系
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
	 * 批量增加一个概念的关系的关系值
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
	 * 用于更新概念的关系的关系值，这里去更改，而不是增加
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
	 *删除概念中某关系的值
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
	 * 给概念的某个关系增加值，不是更新
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
	 * 给概念的某个关系批量添加关系值
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
	 * 更新关系的属性值，这里是更改，而不是增加.如果输入的关系值old,不在关系值表中，则会将新值增加到关系值表中，
	 * 如果输入的关系old在表中，则被新关系值代替。
	 * 因此这个方法有一个bug,但是实际应用和操作中，关系值old肯定会在关系值表中的。
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
	 * 删除关系的属性值
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
	 * 给关系的属性增加值，不是更新。
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
	 * 给关系增加定义域或值域，如果增加了某个关系的定义域，则给其逆关系增加值域。
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
	 * 增加关系的关系值，但是这里的关系是id号的。
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
	 * 修改关系名，但是概念和关系的id字段及其值是不能轻易修改的，所以程序里面就没有这种函数。
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
	 * 删除一个概念的方法,注意考虑要将该概念在其它概念体中的值也要删掉，
	 * @param cid 要删除的概念的id号
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
				//这里表示不能把概念这个根节点删除。
				if(a&&b)
				
				{
					if(!valList.get(j).equals("0000000000"))//不能删除空值
					this.delOneConcept(valList.get(j), colCon, colPool);//递归删除一串子节点
									
				}
				
					
				}
			
		}
		
		
		
		dbCon.delDocument(noIDrelofCon.get(0), cidStr, colCon);
		this.putID(cid, colPool);
	}
	
	
	/**
	 * 删除一个关系的方法,注意考虑要将该关系在其它关系体中的值也要删掉，是从关系数据表中删除一个关系
	 * @param cid 要删除的概念的id号
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
				//这里表示不能把概念这个根节点删除。
				if(a&&b)
				
				{
					
					this.delOneRelation(valList.get(j), colCon, colPool);//递归删除一串子节点
									
				}
				
					
				}
			
		}
		
		
		
		dbCon.delDocument(noIDrelofRel.get(0), ridStr, colCon);
		this.putID(rid, colPool);
	}
	
	
	
	/**
	 * 删除一个术语的ID
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
	 * 查询一个概念中的某个关系的关系值。
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
	 * 从术语表中取出术语对应的id号的列表
	 * @param term 术语
	 * @param idType 概念术语"cid" 关系术语"rid"
	 * @param col 关系术语表或概念术语表
	 * @return
	 */
	public ArrayList<String>  getTermId(String term,String idType,DBCollection col){
		
		String[] termStr=new String[1];
		termStr[0]=term;
		ArrayList<String> idlist=dbCon.getValueArrayofKey("term", termStr, idType, col);
		
		return idlist;
		
	}
	
	
	/**
	 * 得到关系的关系值，
	 * @param rid
	 * @param rel 如果rel为prop则得到关系的属性
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
	 * 删除一个关系，但是概念体中所有用到该关系的概念中也会将该关系以及关系值删除。
	 * @param rid
	 * @param conCol
	 * @param relCol
	 */
	public void delOneRelationofConcepts(String rid,DBCollection conCol,DBCollection relCol){
		String[] ridStr=new String[1];
		ridStr[0]=rid;
		int r1=0,r2=0, r=Integer.valueOf(rid);
		if(r%2==0) {r1=r-1;r2=r;}else {r2=r+1;r1=r;}//r1和r2表示关系和关系逆
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
	 * 找到具有某关系的概念集合。输入的是关系，输出的是概念集合。
	 * @param rel
	 * @param col
	 * @return
	 */
	public ArrayList<String> getConceptWithRel(String rel,DBCollection col){
		DBCursor cur=dbCon.findDocumentWithKey(rel, col);
		ArrayList<String> conlist=new ArrayList<String>();
		//ArrayList<String> conlist2=new ArrayList();
		while (cur.hasNext()){
			conlist.add(cur.next().toMap().values().toArray()[1].toString());//错误点，这里面的3，7是id号的长度相关的参数，直接影响到id号的对比
		}
		return conlist;
	}
	
	/**
	 * 获取概念的全部关系集合。包含cid syn gloss
	 * @param conID
	 * @param col
	 * @return
	 */
	public ArrayList<String> getAllRelsofConcept(String conID,DBCollection col){
		String[] cidStr=new String[1];
		cidStr[0]=conID;
		Object[] keys=dbCon.getKeySet(noIDrelofCon.get(0), cidStr, col);
		ArrayList<String> keysStr=new ArrayList<String>();
		for(int i=1;i<keys.length;i++)//这里是i=1开始，因此可以去掉  _id 项
			keysStr.add(keys[i].toString());
		return keysStr;
				
	}
	
	/**
	 * 获取关系的全部关系集合 包含：rid syn....
	 */
	public ArrayList<String> getAllRelsofRelation(String relID,DBCollection col){
		String[] ridStr=new String[1];
		ridStr[0]=relID;
		Object[] keys=dbCon.getKeySet(noIDrelofRel.get(0), ridStr, col);
		ArrayList<String> keysStr=new ArrayList<String>();
		for(int i=1;i<keys.length;i++)//这里是i=1开始，因此可以去掉  _id 项
			keysStr.add(keys[i].toString());
		return keysStr;
				
	}
	
	
	/**
	 * 只得到部分的概念的关系集合，去掉了cid syn gloss，
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
	 * 只得到部分的关系的关系集合，去掉了cid syn gloss，
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
		//str="nn逆";
		//System.out.println(str.substring(0, str.length()-1));
		//cra("2100000018", colRelData, colId);
		//System.out.println(cra.getPartRelsofRelation("2000000000", colRelData));
		//System.out.println(cra.getPropofRel("2000000000", "2100000002", colRelData));
		//System.out.println(cra.getConceptofRel("1000000000", "2100000002", colConData));
		//cra.addRelationForRootNode(colRelData);
//		cra.addRelTerm("relid", "subclass_of", true, colRelTerm, colRelData, colId);
		//cra.delTermId("newterm8", "cid", "1000000058", colConTerm);
		//cra.updateTerm("再次测试", "newterm1", colConTerm, "cid", "1000000022", true);
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
		//System.out.println(cra.getDupConTermID("概念", colConTerm));
		//cra.addConTerm("conid","概念", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","人物", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","体育人物", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","娱乐人物", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","运动员", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","教练", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","裁判员", true, colConTerm, colConData, colId);
		//cra.addConTerm("conid","教练", true, colConTerm, colConData, colId);
		//cra.addRelTerm("relid","是一", true, colRelTerm, colRelData, colId);
		//System.out.println(cra.getConceptofRel("1000", "2001", colConData));
		//cra.addConTermID("测试", "1006000000", colConTerm);		
//		cra.addConTermID("籍贯", "1000001136", colConTerm);	
//		cra.addConTermID("出生日期", "1000001135", colConTerm);	
//		cra.addConTermID("男", "1100000001", colConTerm);	
//		cra.addConTermID("女", "1100000002", colConTerm);	
		//System.out.println(cra.getPartRelsofConcept("1005", colConData));
		//cra.delOneConcept("1001", colConData);
		//System.out.println(cra.findConceptofRel("1000", "2005", colConData));
		//System.out.println(cra.getConceptWithRel("syn", colConData).get(0));
		//cra.addTerm("有子节点", "2001", colRelTerm);
		//cra.addTerm("是一", "2002", colRelTerm);
		//cra.addRelation("2001", "有子节点", colRelData);
		//cra.addRelation("2002", "是一", colRelData);
		
		//System.out.println(cra.getPropofRel("2001", "prop", colRelData));
		//cra.addTerm("概念", "1000", colConTerm);
		//cra.addConcept("1000","概念" , colConData);
		//cra.addConcept("1005","概念" , colConData);
		//DB db;
		//db=dbCon.creatDb("onto");
		//批量生成概念
		//DBCollection conCol=dbCon.creatTable(db,"ConData");
		//DBCollection relCol=dbCon.creatTable(db,"RelData");
		//for(int i=1000;i<1010;i++){
		//	cra.addConcept("1000", "syn", "gloss", relCol);
		//}
		//批量生成关系
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
