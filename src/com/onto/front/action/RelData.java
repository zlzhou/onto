package com.onto.front.action;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;
import com.onto.reason.PrologCKI;
import com.onto.reason.PrologFile;

public class RelData {

	//private String text;
		private String id;//树
		private String text;
		private List<TreeModel> menus;//向 前台传递概念树数据参数
		private String newConTerm;//从添加删除节点按钮传来的参数 text
		private String newConTermId;//从添加删除节点按钮传来的参数 id
		private String fatherNode;//从树显示传来的参数，用来确定展开的节点是哪个？
		private List<TermModel> terms;//向前台传递概念term的数据参数，用来显示到combobox中。
		
		//用来编辑概念术语所用到的参数
		private String updateConTerm;//新的术语
		private String updateConId;//所对应的术语id
		private String updateConOldTerm;//旧的术语名称
		private String relation;
		private String relationval;
		public String idnull="0000000000";
		
		
		//用来接收规则传来的参数
		public String ruleTerm;
		public String ruleHalfHead;
		public String ruleBody;
		public String reasonQuery;
		public String reasonResultText;
		
		
		
		//用来处理概念体数据展示的前台参数
		private String ConIdForBody="2100000001";//需要展示关系属性的概念
		private String BodyTreeRelId="2100000001";//用来展示的关系属性
		private List<TreeModel> conbodyvalues;
		private List<TreeModel> conbodyreles;
		
		private String isa="2100000001";
		private String sub="2100000002";
		private String cid="cid";
		private String rid="rid";
		private static int connum=0;
		public ConRelAdmin cra;
		DB db;
		DBCollection colConData;
		DBCollection colConTerm;
		DBCollection colRelData;
		DBCollection colRelTerm;
		DBCollection colId;
	
		public RelData() throws UnknownHostException, MongoException  {
			
			cra=new ConRelAdmin();
			db=cra.creatOntoDB("ontopeople");
			colConData=cra.creatTable(db, "ConData");
			colConTerm=cra.creatTable(db, "ConTerm");
			colRelData=cra.creatTable(db, "RelData");
			colRelTerm=cra.creatTable(db, "RelTerm");
			colId=cra.creatTable(db, "idPool");
			
		}	
	
	/**
	 * 用来增加一个关系,这里仍然用addConTerm 没有用addRelTerm，因为是在原来的ConData上修改的。这样可以和前台保持一致，很多函数都不用修改。
	 * @return
	 */
	public String addConTerm(){
			
			
			String term=new String();
			term=newConTerm+String.valueOf(connum);
			System.out.println("前台传过来的新术语:   "+term);
			System.out.println("新术语 的父节点:   "+fatherNode);
			System.out.println("新术语 的节点ID:   "+newConTermId);
			
			this.setNewConTerm(term);
			
			if (newConTermId.isEmpty()){
				
				cra.addRelTerm("relid", term, true, colRelTerm, colRelData, colId);
				
				ArrayList<String> idlist=new ArrayList<String>();
				idlist=cra.getTermId(term, "rid", colRelTerm);
				System.out.println("为新术语分配的ID号:  "+idlist);
				
				this.setNewConTermId(idlist.get(idlist.size()-1));//取idlist中的字符串
				
				cra.addRelationRel(fatherNode, sub, idlist.get(idlist.size()-1), colRelData);
				cra.addRelationRel(idlist.get(idlist.size()-1),isa, fatherNode, colRelData);
				
				String r=new String();
				r=String.valueOf(Integer.valueOf(idlist.get(idlist.size()-1))+1);
				cra.addRelationRel(fatherNode, sub, r, colRelData);
				cra.addRelationRel(r,isa, fatherNode, colRelData);
				
			}else{
				
				cra.addRelTermID(term, newConTermId, colConTerm);
				cra.addRelationRel(fatherNode,sub, newConTermId, colConData);
				cra.addRelationRel(newConTermId,isa, fatherNode, colConData);
				
				String r=new String();
				r=String.valueOf(Integer.valueOf(newConTermId)+1);
				cra.addRelTermID(term+"逆", r, colConTerm);
				cra.addRelationRel(fatherNode,sub, r, colConData);
				cra.addRelationRel(r,isa, fatherNode, colConData);
				
				
			}
			
			connum=connum+1; 
			//System.out.println(connum);
			
			return "success";
		}	
		
	
	/**
	 * 用来增加一个关系,这里仍然用addConTerm 没有用addRelTerm，因为是在原来的ConData上修改的。这样可以和前台保持一致，很多函数都不用修改。
	 * @return
	 */
	public String addAttTerm(){
			
			
			String term=new String();
			term=newConTerm+String.valueOf(connum);
			System.out.println("前台传过来的新术语:   "+term);
			System.out.println("新术语 的父节点:   "+fatherNode);
			System.out.println("新术语 的节点ID:   "+newConTermId);
			
			this.setNewConTerm(term);
			
			if (newConTermId.isEmpty()){
				
				cra.addRelTerm("attid", term, true, colRelTerm, colRelData, colId);
				
				ArrayList<String> idlist=new ArrayList<String>();
				idlist=cra.getTermId(term, "rid", colRelTerm);
				System.out.println("为新术语分配的ID号:  "+idlist);
				
				this.setNewConTermId(idlist.get(idlist.size()-1));//取idlist中的字符串
				
				cra.addRelationRel(fatherNode, sub, idlist.get(idlist.size()-1), colRelData);
				cra.addRelationRel(idlist.get(idlist.size()-1),isa, fatherNode, colRelData);
				cra.addRelationValueofAtt(idlist.get(idlist.size()-1), "prop", "inh", colRelData);//属性均为可继承的。
				
				String r=new String();
				r=String.valueOf(Integer.valueOf(idlist.get(idlist.size()-1))+1);
				cra.addRelationRel(fatherNode, sub, r, colRelData);
				cra.addRelationRel(r,isa, fatherNode, colRelData);
				cra.addRelationValueofAtt(r, "prop", "inh", colRelData);//属性均为可继承的。				
			}else{
				
				cra.addRelTermID(term, newConTermId, colConTerm);
				cra.addRelationRel(fatherNode,sub, newConTermId, colConData);
				cra.addRelationRel(newConTermId,isa, fatherNode, colConData);
				cra.addRelationValueofAtt(newConTermId, "prop", "inh", colRelData);//属性均为可继承的。
				
				
				String r=new String();
				r=String.valueOf(Integer.valueOf(newConTermId)+1);
				cra.addRelTermID(term+"逆", r, colConTerm);
				cra.addRelationRel(fatherNode,sub, r, colConData);
				cra.addRelationRel(r,isa, fatherNode, colConData);
				cra.addRelationValueofAtt(r, "prop", "inh", colRelData);//属性均为可继承的。
				
			}
			
			connum=connum+1; 
			//System.out.println(connum);
			
			return "success";
		}	
	
	
public String delConTerm(){
		
		System.out.println("delte term:---"+newConTerm);
		System.out.println("delte id:---"+newConTermId);
		
		
		if(newConTermId.charAt(0)=='2'){
		//这里的目的是确定删除的是带逆的关系还是不带逆的关系
		String delterm=new String();
		System.out.println("ok");
		String ndelterm=new String();
		String deltermid=new String();
		String ndeltermid=new String();
		int r=Integer.valueOf(newConTermId);
		System.out.println("this is r"+r);
		if(r%2==0) {ndelterm=newConTerm;
			ndeltermid=String.valueOf(r);
			delterm=newConTerm.substring(0, newConTerm.length()-1);
			deltermid=String.valueOf(r-1);
		}
		
		else {delterm=newConTerm;
			deltermid=String.valueOf(r);
			ndelterm=newConTerm+"逆";
			ndeltermid=String.valueOf(r+1);
		}//r1和r2表示关系和关系逆
		
		System.out.println("delte term:---"+ndelterm);
		System.out.println("delte term:---"+ndeltermid);
		System.out.println("delte term:---"+delterm);
		System.out.println("delte term:---"+deltermid);
		
		cra.delTermId(ndelterm, rid, ndeltermid, colRelTerm);
		cra.delOneRelation(ndeltermid, colRelData, colId);
		
		cra.delTermId(delterm, rid, deltermid, colRelTerm);
		cra.delOneRelation(deltermid, colRelData, colId);
		//cra.delTermId(delterm, rid, newConTermId, colRelTerm);
		//cra.delOneRelation(newConTermId, colRelData, colId);
		
		}
		else if(newConTermId.charAt(0)=='1'){
			
			
		}
		
		
		return "success";
	}
	
	
/**
 * //先修改术语表中的term字段的数据，再找到对应的id号去修改同义词集
//修改术语时要注意，如果术语表中该术语对应两个以上的概念，则新增一条术语.如果术语表中只对应一个id号，则直接修改该术语名称。
 * @return
 */
public String updateTerm(){
	
System.out.println("用来更新数据库的新术语名称---> "+updateConTerm);	
System.out.println("用来更新数据库的旧术语名称---> "+updateConOldTerm);
System.out.println("用来更新数据库的新术语Id---> "+updateConId);	
	//先修改术语表中的term字段的数据，再找到对应的id号去修改同义词集
//修改术语时要注意
boolean hasIds;
ArrayList<String> idlist=cra.getTermId(updateConOldTerm, rid, colConTerm);

int r=Integer.valueOf(updateConId);
if(r%2==0)r=r-1;else r=r+1;

//这里以后可以加上判断是否是后面带有逆字，如果带有，则新更新的逆就不带逆字。

if(idlist.size()==1){ hasIds=false;
	
}else{
	
	hasIds=true;
	
}

cra.updateTerm(updateConTerm, updateConOldTerm, colRelTerm, rid, updateConId, hasIds);
cra.updateTerm(updateConTerm.concat("逆"), updateConOldTerm, colRelTerm, rid, String.valueOf(r), hasIds);


if(hasIds){
	
	cra.addRelationValueofAtt(updateConId, "csyn", updateConTerm, colRelData);
	cra.addRelationValueofAtt(String.valueOf(r), "csyn", updateConTerm.concat("逆"), colRelData);
	
}else{
	
	cra.updateRelationValueofAtt(updateConId, "csyn", updateConOldTerm, updateConTerm, colRelData);
	cra.updateRelationValueofAtt(String.valueOf(r), "csyn", updateConOldTerm, updateConTerm.concat("逆"), colRelData);
	
}



	
	return "success";
}

/**
 * 响应添加规则的action函数
 * @return
 * @throws MongoException 
 * @throws UnknownHostException 
 */
public String addRuleTerm() throws UnknownHostException, MongoException{
	
	System.out.println("rueterm: "+ruleTerm );
	//PrologCKI p=new PrologCKI();
//	PrologFile f=new PrologFile();  程序曾在这里出了问题，就是通过前端访问时，这里要jar包的源文件。
//	System.out.println("PrologFile   "+f);
	
	System.out.println("ruleHalfHead: "+ruleHalfHead );
	System.out.println("ruleBody: "+ruleBody );
	cra.addRuleTerm(ruleTerm, true, ruleHalfHead, ruleBody, colRelTerm, colRelData, colId);
	
	
	return "success";
}

/**
 * 这里是将结果返回到前台，这里也只是对两个变量来返回的，以后考虑多个变量。
 * @return
 */
public String reasonByRule() {
	ArrayList<ArrayList<String>> result= new ArrayList<ArrayList<String>>();
	System.out.println("reason query is: "+reasonQuery);
	result=cra.reasonProFile(reasonQuery);
	reasonResultText="";
	if(result.get(0).get(0)=="no") reasonResultText="o(╯□╰)o 无结果!";
	else {
			String xstr,ystr;
			reasonResultText="( ^_^ ) 有结果!\n\n";
			for(int j=0;j<result.get(1).size();j++){
								
				if(result.get(1).size()!=0) xstr=cra.getConceptofRel(result.get(1).get(j), "csyn", colConData).get(0); else xstr="";
				if(result.get(2).size()!=0) 
					ystr=cra.getConceptofRel(result.get(2).get(j), "csyn", colConData).get(0); else ystr="";
				reasonResultText=reasonResultText+xstr+"   "+ystr+"\n";
						
			}
			
		System.out.println(reasonResultText);
	}
	
	return "success";
}

//public String reasonByRule() throws IOException, MongoException, InvalidTheoryException, MalformedGoalException, NoSolutionException, NoMoreSolutionException{
//	ArrayList<ArrayList<String>> result= new ArrayList<ArrayList<String>>();
//	PrologFile p=new PrologFile();
//	System.out.println("PrologFile ok");
//	result=p.reasonPro("e:/excel/people.pl", reasonQuery);
//	
//	System.out.println("results :"+result);
//	
//	return "success";
//}


/**
 * 为关系的关系赋值。这里只考虑定义域和值域
 * @return
 */
public String addVauleToRelofConcept(){
	
//	ArrayList<String> relidstrd=new ArrayList<String>();
//	ArrayList<String> relidstrr=new ArrayList<String>();
	ArrayList<String> validstr=new ArrayList<String>();
	String relationd=new String();
	String relationr=new String();
//	ArrayList<String> tempd=new ArrayList<String>();
//	ArrayList<String> tempr=new ArrayList<String>();
//	if(relation.equals("定义域")) {tempd.add("dom");relidstrd=tempd;tempr.add("ran");relidstrr=tempr;}
//	else if(relation.equals("值域")) {tempr.add("ran");relidstrr=tempr;tempd.add("dom");relidstrd=tempd;}
	if(relation.equals("定义域")) {relationd="dom";relationr="ran";}
	else if(relation.equals("值域")) {relationd="ran";relationr="dom";}
	//else	relidstr=cra.getTermId(relation, rid, colRelTerm);
	validstr=cra.getTermId(relationval, cid, colConTerm);
	System.out.println("add relation ++   "+relation);
	System.out.println("add relation val ++   "+validstr);
	
	cra.addRelationValueofAtt(ConIdForBody,relationd, validstr.get(0), colRelData);
	cra.delRelationValueofAtt(ConIdForBody,relationd, idnull, colRelData);
	int r=Integer.valueOf(ConIdForBody);
	if(r%2==0)r=r-1;else r=r+1;
	String relStr=String.valueOf(r);
	
	cra.addRelationValueofAtt(relStr,relationr, validstr.get(0), colRelData);
	cra.delRelationValueofAtt(relStr,relationr, idnull, colRelData);
	
//	if(relidstr.get(0).charAt(1)=='1') cra.addRelationRel(ConIdForBody, relidstr.get(0), validstr.get(0), colRelData);
//	else cra.addRelationValueofAtt(ConIdForBody, relidstr.get(0), validstr.get(0), colRelData);
//	cra.delRelationValueofAtt(ConIdForBody, relidstr.get(0), idnull, colRelData);
//
//	int r=Integer.valueOf(ConIdForBody);
//	if(r%2==0)r=r-1;else r=r+1;
//	String relStr=String.valueOf(r);
//	
//	if(relidstr.get(0).charAt(1)=='1') cra.addRelationRel(relStr, relidstr.get(0), validstr.get(0), colRelData);
//	else cra.addRelationValueofAtt(relStr, relidstr.get(0), validstr.get(0), colRelData);
//	cra.delRelationValueofAtt(relStr, relidstr.get(0), idnull, colRelData);
	
	//	if(!ConIdForBody.equals(validstr)){
//	cra.addVauleToConceptRel(ConIdForBody, relidstr.get(0), validstr.get(0), colConData);//
//	
//	}
	
	return "success";
}






/**
 * 将数据库中的数据做处理，使其在前台显示。
 * @param fatherId
 * @param subRelId
 * @return
 */

public List<TreeModel> covertRelDbtoFront(String fatherId,String subRelId){
		
		
		List<TreeModel> list=new ArrayList<TreeModel>();
		
		ArrayList<String> subNodes=new ArrayList<String>();
		subNodes=cra.getPropofRel(fatherId,subRelId, colRelData);//获取子节点集合
		
		for(int i=0;i<subNodes.size();i++){
			if(subNodes.get(i).charAt(1)=='1'){
			ArrayList<String> texts=cra.getPropofRel(subNodes.get(i),"csyn", colRelData);//获取子节点的文本
			//System.out.println("展开节点的子节点集合:    "+texts);
			TreeModel tm=new TreeModel();
			tm.setText(texts.get(texts.size()-1));
			//System.out.println("展开节点的子节点集合:    "+texts);
			tm.setId(subNodes.get(i));
			ArrayList<String> leafs=cra.getPartRelsofRelation(subNodes.get(i), colRelData);//获取部分属性，
			if (!leafs.contains(sub)){
				
				tm.setLeaf(true);
				
			}//如果没有子节点关系，则这个概念是个叶子节点。
			
			list.add(tm);
			}
		}
	   //System.out.println(subNodes);
	   return list;
	}
	

/**
 * 将数据库中的数据做处理，使其在前台显示。
 * @param fatherId
 * @param subRelId
 * @return
 */

public List<TreeModel> covertAttDbtoFront(String fatherId,String subRelId){
		
		
		List<TreeModel> list=new ArrayList<TreeModel>();
		
		ArrayList<String> subNodes=new ArrayList<String>();
		subNodes=cra.getPropofRel(fatherId,subRelId, colRelData);//获取子节点集合
		
		for(int i=0;i<subNodes.size();i++){
			if(subNodes.get(i).charAt(1)=='0'){
			ArrayList<String> texts=cra.getPropofRel(subNodes.get(i),"csyn", colRelData);//获取子节点的文本
			//System.out.println("展开节点的子节点集合:    "+texts);
			TreeModel tm=new TreeModel();
			tm.setText(texts.get(texts.size()-1));
			//System.out.println("展开节点的子节点集合:    "+texts);
			tm.setId(subNodes.get(i));
			ArrayList<String> leafs=cra.getPartRelsofRelation(subNodes.get(i), colRelData);//获取部分属性，
			if (!leafs.contains(sub)){
				
				tm.setLeaf(true);
				
			}//如果没有子节点关系，则这个概念是个叶子节点。
			
			list.add(tm);
			}
		}
	   //System.out.println(subNodes);
	   return list;
	}


	/**
	 * 将概念树的数据传到前台。	
	 * @return
	 */
	public String conTreeAttDataForFront() {
		
		//System.out.println("ok1"+text);
		/*System.out.println(text);
		System.out.println(id);
		System.out.println(newConTerm);*/
		menus=new ArrayList<TreeModel>();
		//System.out.println("浏览概念传来的参数:   "+id);
		try {
			menus=this.covertAttDbtoFront("2000000000",sub);
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	    this.setMenus(menus);
	    return "success";
	    
	 
	    /*String menuString=new String();
	    try {
			menuString=JSONUtil.serialize(menus);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ok2"+menuString);
		*/
		
		
	}
	
	
	/**
	 * 将概念树的数据传到前台。	
	 * @return
	 */
	public String conTreeRelDataForFront() {
		
		//System.out.println("ok1"+text);
		/*System.out.println(text);
		System.out.println(id);
		System.out.println(newConTerm);*/
		menus=new ArrayList<TreeModel>();
		//System.out.println("浏览概念传来的参数:   "+id);
		try {
			menus=this.covertRelDbtoFront("2000000000",sub);
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	    this.setMenus(menus);
	    return "success";
	    
	 
	    /*String menuString=new String();
	    try {
			menuString=JSONUtil.serialize(menus);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ok2"+menuString);
		*/
		
		
	}
	
	
	/**
	 * 将数据库中的关系term及Id号配置成前台所需要的形式。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TermModel> covertTermDbtoFront(){
			
			
			List<TermModel> list=new ArrayList<TermModel>();
			
			ArrayList<String> tstr=new ArrayList<String>();
			ArrayList<String> istr=new ArrayList<String>();
			DBObject db;
			DBCursor cur=colRelTerm.find();
			//int termNum=0;
			while (cur.hasNext()){
				//tm.setId(String.valueOf(termNum));
//				db=cur.next();
//				TermModel tm=new TermModel();
//				tstr=(ArrayList<String>) db.get("term");
//				tm.setText(tstr.get(tstr.size()-1));
//				istr=(ArrayList<String>) db.get(rid);
//				tm.setId(istr);
//				list.add(tm);
						
				db=cur.next();
				istr=(ArrayList<String>) db.get(rid);
					for(int i=0;i<istr.size();i++){
						
						ArrayList<String> temp=new ArrayList<String>();
							TermModel tm=new TermModel();
							temp.add(istr.get(i));
							tm.setId(temp);
							
							tstr=(ArrayList<String>) db.get("term");
						tm.setText(tstr.get(tstr.size()-1));
						list.add(tm);
						//temp.remove(istr.get(i));
						
					}
					ArrayList<String> tempd=new ArrayList<String>();
					TermModel tmd=new TermModel();
					tmd.setText("定义域");//显示关系的同义词庥中的第一个术语
					//System.out.println(tlist.get(tlist.size()-1));
					tempd.add("dom");
					tmd.setId(tempd);
					list.add(tmd);
					ArrayList<String> tempr=new ArrayList<String>();
					TermModel tmr=new TermModel();
					tmr.setText("值域");//显示关系的同义词庥中的第一个术语
					//System.out.println(tlist.get(tlist.size()-1));
					tempr.add("ran");
					tmr.setId(tempr);
					list.add(tmr);
				
				//termNum=termNum+1;
				//System.out.println(tm.getId());
				//System.out.println(termNum);
			//System.out.println(cur.next().get("term"));
			//System.out.println(cur.size(.;
			}
			//System.out.println(list);
		   return list;
		}

	/**
	 * 将关系term数据传递到前台
	 * @return
	 */
	public String conTermDataForFront() {
		
		//System.out.println("ok1"+text);
		/*System.out.println(text);
		System.out.println(id);
		System.out.println(newConTerm);*/
		terms=new ArrayList<TermModel>();
		
		try {
			terms=this.covertTermDbtoFront();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	    this.setTerms(terms);
	    return "success";
	}
	
	
	/**
	 * 为概念体的树结构提供数据准备
	 * @return
	 */
	public List<TreeModel> convertConBodyTreeRelDataForFront(){
		
		
		
		List<TreeModel> list=new ArrayList<TreeModel>();
		ArrayList<String> relstr=new ArrayList<String>();
		relstr=cra.getPartRelsofRelation(ConIdForBody, colRelData);//得到关系的所有属性,去掉cid gloss csyn等
		TreeModel tmd=new TreeModel();
		tmd.setText("定义域");//显示关系的同义词庥中的第一个术语
		//System.out.println(tlist.get(tlist.size()-1));
		tmd.setId("dom");
		list.add(tmd);
		TreeModel tmr=new TreeModel();
		tmr.setText("值域");//显示关系的同义词庥中的第一个术语
		//System.out.println(tlist.get(tlist.size()-1));
		tmr.setId("ran");
		list.add(tmr);
		for(int i=0;i<relstr.size();i++){
			//this.setBodyTreeRelId(relstr.get(0));
			ArrayList<String> tlist=new ArrayList<String>();
			tlist=cra.getPropofRel(relstr.get(i), "csyn", colRelData);
			TreeModel tm=new TreeModel();
			tm.setText(tlist.get(tlist.size()-1));//显示关系的同义词庥中的第一个术语
			//System.out.println(tlist.get(tlist.size()-1));
			tm.setId(relstr.get(i));
			list.add(tm);
		}
		/*
		for(int i=0;i<5;i++){
			TreeModel tm=new TreeModel();
			tm.setText("aaa");
			tm.setId(String.valueOf(i));
			tm.setLeaf(true);
			list.add(tm);
		}
		*/
		
		
		return list;
		
	}


	public List<TreeModel> convertConBodyTreeValueDataForFront(String RelId){
			ArrayList<String> relstrc=new ArrayList<String>();
			List<TreeModel> listc=new ArrayList<TreeModel>();
			
			relstrc=cra.getPropofRel(ConIdForBody, RelId, colRelData);
			for (int j=0;j<relstrc.size();j++){
				ArrayList<String> tlist=new ArrayList<String>();
				if(relstrc.get(j).charAt(0)!='2')
					tlist=cra.getConceptofRel(relstrc.get(j), "csyn", colConData);
				else
				tlist=cra.getPropofRel(relstrc.get(j), "csyn", colRelData);
				//System.out.println("关系的属性值："+tlist.get(tlist.size()-1));
				TreeModel tmc=new TreeModel();
				
				tmc.setText(tlist.get(tlist.size()-1));
				tmc.setId(relstrc.get(j));
				tmc.setLeaf(true);
				listc.add(tmc);
			}
			
			/*for(int i=0;i<5;i++){
				TreeModel tm=new TreeModel();
				tm.setText("bbb");
				tm.setId(String.valueOf(i));
				tm.setLeaf(true);
				listc.add(tm);
			}
			*/
			return listc;
		
	}

		
	public String conBodyDataForFront() {
		
		System.out.println("选择的概念ID  "+ConIdForBody);
		
		conbodyreles=new ArrayList<TreeModel>();
		
		try {
			conbodyreles=this.convertConBodyTreeRelDataForFront();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		   
		 this.setConbodyreles(conbodyreles);	
		
			
	    return "success";
	}


	public String asyncConBodyLoad(){
		
		System.out.println("选择的概念ID  "+ConIdForBody);
		System.out.println("这是属性节点的ID值："+BodyTreeRelId);
		conbodyreles=new ArrayList<TreeModel>();
		try {
			conbodyreles=this.convertConBodyTreeValueDataForFront(BodyTreeRelId);
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 this.setConbodyreles(conbodyreles);	
			
		 return "success";
		
	}

	
	
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub

		RelData d=new RelData();
		
		//d.ConIdForBody="2100000001";
		//d.convertConBodyTreeRelDataForFront();
		//d.convertConBodyTreeValueDataForFront("dom");
		//d.conTreeDataForFront();
		//d.delConTerm();
		
		//System.out.println("ok");
		//System.out.println(d.covertRelDbtoFront("2000000000", "2100000002"));
		//d.addRuleTerm();
		d.reasonByRule();
		
	}
	
	
	
	
	
	public String getReasonResultText() {
		return reasonResultText;
	}

	public void setReasonResultText(String reasonResultText) {
		this.reasonResultText = reasonResultText;
	}

	public String getRuleTerm() {
		return ruleTerm;
	}

	public void setRuleTerm(String ruleTerm) {
		this.ruleTerm = ruleTerm;
	}

	public String getRuleHalfHead() {
		return ruleHalfHead;
	}

	public void setRuleHalfHead(String ruleHalfHead) {
		this.ruleHalfHead = ruleHalfHead;
	}

	public String getRuleBody() {
		return ruleBody;
	}

	public void setRuleBody(String ruleBody) {
		this.ruleBody = ruleBody;
	}

	public String getReasonQuery() {
		return reasonQuery;
	}

	public void setReasonQuery(String reasonQuery) {
		this.reasonQuery = reasonQuery;
	}

	public String getBodyTreeRelId() {
		return BodyTreeRelId;
	}

	public void setBodyTreeRelId(String bodyTreeRelId) {
		BodyTreeRelId = bodyTreeRelId;
	}

	public String getConIdForBody() {
		return ConIdForBody;
	}

	public void setConIdForBody(String conIdForBody) {
		ConIdForBody = conIdForBody;
	}

	public List<TreeModel> getConbodyreles() {
		return conbodyreles;
	}

	public void setConbodyreles(List<TreeModel> conbodyreles) {
		this.conbodyreles = conbodyreles;
	}

	public List<TermModel> getTerms() {
		return terms;
	}

	public void setTerms(List<TermModel> terms) {
		this.terms = terms;
	}

	public String getFatherNode() {
		return fatherNode;
	}


	public void setFatherNode(String fatherNode) {
		this.fatherNode = fatherNode;
	}


	public String getNewConTerm() {
		return newConTerm;
	}


	public void setNewConTerm(String newConTerm) {
		this.newConTerm = newConTerm;
	}


	public String getNewConTermId() {
		return newConTermId;
	}


	public void setNewConTermId(String newConTermId) {
		this.newConTermId = newConTermId;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<TreeModel> getMenus() {
		return menus;
	}

	public void setMenus(List<TreeModel> menus) {
		this.menus = menus;
	}

	public String getUpdateConTerm() {
		return updateConTerm;
	}

	public void setUpdateConTerm(String updateConTerm) {
		this.updateConTerm = updateConTerm;
	}

	public String getUpdateConId() {
		return updateConId;
	}

	public void setUpdateConId(String updateConId) {
		this.updateConId = updateConId;
	}

	public String getUpdateConOldTerm() {
		return updateConOldTerm;
	}

	public void setUpdateConOldTerm(String updateConOldTerm) {
		this.updateConOldTerm = updateConOldTerm;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRelationval() {
		return relationval;
	}

	public void setRelationval(String relationval) {
		this.relationval = relationval;
	}
	
	
	
	
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	

}
