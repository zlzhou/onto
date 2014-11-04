package com.onto.front.action;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class ConData extends ActionSupport {
	
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
	private boolean sonNodeFlag;//旧的术语名称
	private String searchTerm;
	
	//用来为概念添加关系和关系值的参数
	private String newRelTerm;//从前台传来的概念的新关系的Id呈。
	private String newConId;
	private String relation;
	private String relationval;
	
	//用来删除关系和关系值的参数
	private String aimCon;//被删除关系和关系值的概念。
	private String relofCon="2000000004";//被删除的概念的关系

	//用来处理概念体数据展示的前台参数
	private String ConIdForBody;//需要展示关系属性的概念
	private String BodyTreeRelId;//用来展示的关系属性
	private List<TreeModel> conbodyvalues;
	private List<TreeModel> conbodyreles;
	private List<IdAndTermModel> termsforclass;
	private List<IdAndTermModel> valueforclass;
	private String fatherPath="";
	private String expPath;
	private String aimIdForExp;
	
	private String isa="2100000001";
	private String sub="2100000002";
	private String cid="cid";
	private String rid="rid";
	private static int connum=0;
	public String idnull="0000000000";
	
	public ConRelAdmin cra;
	DB db;
	DBCollection colConData;
	DBCollection colConTerm;
	DBCollection colRelData;
	DBCollection colRelTerm;
	DBCollection colId;
	
	/*private Map dataMap;
	private TreeModel user;
	private String menuString;*/
	
	//private Map m;
	
	public ConData() throws UnknownHostException, MongoException{
		
		cra=new ConRelAdmin();
		db=cra.creatOntoDB("ontopeople");
		colConData=cra.creatTable(db, "ConData");
		colConTerm=cra.creatTable(db, "ConTerm");
		colRelData=cra.creatTable(db, "RelData");
		colRelTerm=cra.creatTable(db, "RelTerm");
		colId=cra.creatTable(db, "idPool");
		
		
		
	}
	
	/**
	 * 用来编辑概念树结点的操作。
	 * 增加新的术语以及对应概念,如果是一个没有ID号的新概念，则分配一个ID号，然后修改术语表和数据表，如果有一个相同的术语，则只将术语添加到术语表，概念表只改变关系
	 * @return
	 */
	public String addConTerm(){
		
		
		String term=new String();
		term=newConTerm+"_"+String.valueOf(connum);
		System.out.println("前台传过来的新术语:   "+term);
		System.out.println("新术语 的父节点:   "+fatherNode);
		System.out.println("新术语 的节点ID:   "+newConTermId);
		
		this.setNewConTerm(term);
		
		if (newConTermId.isEmpty()){
			
			cra.addConTerm("conid", term, true, colConTerm, colConData, colId);
			
			ArrayList<String> idlist=new ArrayList<String>();
			idlist=cra.getTermId(term, "cid", colConTerm);
			System.out.println("为新术语分配的ID号:  "+idlist);
			this.setNewConTermId(idlist.get(idlist.size()-1));
			
			cra.addConceptRel(fatherNode,sub, idlist.get(idlist.size()-1), colConData);
		//	cra.addConceptRel(idlist.get(idlist.size()-1),isa, fatherNode, colConData);
			//插入子节点，如果父节点的属性或关系中有可继承的，则子节点中添加。属性都是可继承的。
			if(sonNodeFlag){
				this.addSonCon(fatherNode,newConTermId,false);
//				ArrayList<String> rellist=new ArrayList<String>();
//				rellist=cra.getPartRelsofConcept(fatherNode, colConData);
//				for(int i=0;i<rellist.size();i++)
//				{
//					if(cra.getPropofRel(rellist.get(i),"prop", colRelData).contains("inh")) {
//						
//						ArrayList<String> vallist=new ArrayList<String>();
//						vallist=cra.getConceptofRel(fatherNode, rellist.get(i), colConData);
//						for(int j=0;j<vallist.size();j++)
//						{
//							cra.addConceptRel(idlist.get(idlist.size()-1), rellist.get(i), vallist.get(j), colConData);
//						}
//						
//					}
//				}
			}
			
		}else{
			
			cra.addConTermID(term, newConTermId, colConTerm);
			
			
			cra.addConceptRel(fatherNode,sub, newConTermId, colConData);
		//	cra.addConceptRel(newConTermId,isa, fatherNode, colConData);
			
			if(sonNodeFlag){
				
				this.addSonCon(fatherNode,newConTermId,false);
//				ArrayList<String> rellist=new ArrayList<String>();
//				rellist=cra.getPartRelsofConcept(fatherNode, colConData);
//				for(int i=0;i<rellist.size();i++)
//				{
//					if(cra.getPropofRel(rellist.get(i),"prop", colRelData).contains("inh")) {
//						
//						ArrayList<String> vallist=new ArrayList<String>();
//						vallist=cra.getConceptofRel(fatherNode, rellist.get(i), colConData);
//						for(int j=0;j<vallist.size();j++)
//						{
//							cra.addConceptRel(newConTermId, rellist.get(i), vallist.get(j), colConData);
//						}
//						
//					}
//				}
			}
			
		}
		
		connum=connum+1; 
		//System.out.println(connum);
		
		return SUCCESS;
	}
	
	
	/**
	 * 功能是首先检查父节点中有没有可以继承的属性，如果有，则将该属性及其值都给子节点。并且还要添加一个新的属性，子节点至少要有一个异于其它兄弟节点的属性。
	 * 其中的标志符号是区别“属性分类”还是“一般的添加子节点，” 如果是属性分类，则true 添加子节点是false 如果是true则因为关系和关系值已确定，所以在这里不再增加用于分类的属性及其值。
	 */
	public void addSonCon(String fatherid,String newTermid,Boolean classornot){
		
	
		ArrayList<String> rellist=new ArrayList<String>();
		rellist=cra.getPartRelsofConcept(fatherid, colConData);
		if(classornot){
			rellist.remove(BodyTreeRelId);
		}
		for(int i=0;i<rellist.size();i++)
		{
			if(cra.getPropofRel(rellist.get(i),"prop", colRelData).contains("inh")) {
				
				ArrayList<String> vallist=new ArrayList<String>();
				vallist=cra.getConceptofRel(fatherid, rellist.get(i), colConData);//获取某关系的关系值
				for(int j=0;j<vallist.size();j++)
				{
					cra.addConceptRel(newTermid, rellist.get(i), vallist.get(j), colConData);
				}
				
			}
		}
		ArrayList<String> term=cra.getConceptofRel(newTermid, "csyn", colConData);
		this.addAttOfNewSonCon(newTermid, term.get(term.size()-1)+"属性");
		ArrayList<String> newattidlist=cra.getTermId(term.get(term.size()-1)+"属性", "rid",colRelTerm);
		cra.addConceptRel(newTermid, newattidlist.get(newattidlist.size()-1), idnull, colConData);
	}
	
	
	/**
	 * 为一个子节点，增加一个新的属性，因为每一个节点的子节点都应该有一个自己的不同于别的兄弟节点的属性。
	 * @param newattid 新的子节点的id值。
	 * @param newattterm 新的子节点的新属性术语名。
	 */
	public void addAttOfNewSonCon(String newattid,String newattterm){
		
			cra.addRelTerm("attid", newattterm, true, colRelTerm, colRelData, colId);//增加一个属性术语，得到一个属性ID
			
			ArrayList<String> idlist=new ArrayList<String>();
			idlist=cra.getTermId(newattterm, "rid", colRelTerm);
			System.out.println("为新术语分配的ID号:  "+idlist);
			
			this.setNewConTermId(idlist.get(idlist.size()-1));//取idlist中的字符串
			
			cra.addRelationRel("2000000000", sub, idlist.get(idlist.size()-1), colRelData);
			//cra.addRelationRel(idlist.get(idlist.size()-1),isa, fatherNode, colRelData);
			cra.addRelationValueofAtt(idlist.get(idlist.size()-1), "prop", "inh", colRelData);//属性均为可继承的。
			
			String r=new String();
			r=String.valueOf(Integer.valueOf(idlist.get(idlist.size()-1))+1);
			cra.addRelationRel("2000000000", sub, r, colRelData);
			//cra.addRelationRel(r,isa, fatherNode, colRelData);
			cra.addRelationValueofAtt(r, "prop", "inh", colRelData);//属性均为可继承的。				
	}	
		
	
	/**
	 * 增加属性分类关系的action函数
	 * @return
	 */
	public String addClassCons(){
		
		ArrayList<String> cons=cra.getConceptofRel(ConIdForBody, BodyTreeRelId, colConData);//得到概念的某个关系的id集合
		
		for(int i=0;i<cons.size();i++){
			ArrayList<String> relstrc=new ArrayList<String>();
			ArrayList<String> rels=cra.getPartRelsofConcept(cons.get(i), colConData);
			if(rels.contains(sub))	relstrc=cra.getConceptofRel(cons.get(i), sub, colConData);
			if(relstrc.isEmpty()) relstrc.add(cons.get(i));
//			relstrc=cra.getConceptofRel(cons.get(i), sub, colConData);
//			if(relstrc.isEmpty()) relstrc.add(cons.get(i));
		
		
		for (int j=0;j<relstrc.size();j++){
			
			ArrayList<String> tlist=new ArrayList<String>();
			tlist=cra.getConceptofRel(relstrc.get(j), "csyn", colConData);//得到这个关系的第一个id的术语的集合
			cra.addConTerm("conid", tlist.get(tlist.size()-1)+newConTerm, true, colConTerm, colConData, colId);//增加这个id的术语到术语表
			ArrayList<String> idlist=new ArrayList<String>();
			idlist=cra.getTermId(tlist.get(tlist.size()-1)+newConTerm, "cid", colConTerm);
			System.out.println("为新分类术语分配的ID号:  "+idlist);
			this.setNewConTermId(idlist.get(idlist.size()-1));
			cra.addConceptRel(ConIdForBody,sub, idlist.get(idlist.size()-1), colConData);//增加为父概念的子节点
			cra.addConceptRel(idlist.get(idlist.size()-1), BodyTreeRelId, relstrc.get(j), colConData);//子概念的BodyTreeRelID属性应该是确定的。
			this.addSonCon(ConIdForBody, idlist.get(idlist.size()-1),true);
			//this.addAttOfNewSonCon(idlist.get(idlist.size()-1), tlist.get(tlist.size()-1)+newConTerm+"属性");//增加一个新的属性，并且把继承的属性也加到概念中
			//ArrayList<String> newattidlist=cra.getTermId(tlist.get(tlist.size()-1)+newConTerm+"属性", "rid", colRelData);
			//cra.addConceptRel(idlist.get(idlist.size()-1), newattidlist.get(newattidlist.size()-1), idnull, colConData);
			//cra.addConceptRel(idlist.get(idlist.size()-1),isa, fatherNode, colConData);
					
			
		}
		
	}
		return SUCCESS;
	}
	
	/**
	 * 判断是否是一个分类属性。即该概念的分类是按这个属性值进行分类的。
	 * @param conid
	 * @return
	 */
	public String getClassAttofConcept(String conid){
		String classatt=new String();
		ArrayList<String> reloffather=cra.getPartRelsofConcept(conid, colConData);//得到概念的关系列表
		for(int i=0;i<reloffather.size();i++){
			//只对属性来操作
			if(reloffather.get(i).charAt(1)=='0'){
				
				ArrayList<String> vallist=cra.getConceptofRel(conid, reloffather.get(i), colConData);//得到属性的值，一般应该是一个，这里不失一般性
				for(int j=0;j<vallist.size();j++){
					
					//这里还没有完成
				}
				
				
			}
			
			
		}
		
		
		
		
		
		return classatt;
	}
	
	
	
	/**
	 * 
	 * 用来删除术语，注意，如果一个术语有多个概念，则只删除里面的概念，不能将这个术语从术语表中删除。
	 * 
	 * @return
	 */
	public String delConTerm(){
		
		System.out.println("delte term:---"+newConTerm);
		System.out.println("delte id:---"+newConTermId);
		
		if(newConTermId!="0000000000"){
			cra.delTermId(newConTerm, cid, newConTermId, colConTerm);
			cra.delOneConcept(newConTermId, colConData, colId);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 删除一个概念的关系及关系值，注意：这些关系值中的关系中也将删除该概念
	 * @return
	 */
	public String delRelofCon(){
		
		
		ArrayList<String> vals=cra.getConceptofRel(aimCon, relofCon, colConData);
		int r=Integer.valueOf(relofCon);
		if(r%2==0)r=r-1;else r=r+1;
		String nrelofCon=String.valueOf(r);
		
		System.out.println(aimCon+"   "+relofCon);
		for (int i=0;i<vals.size();i++){
			
			cra.delConceptValueofRel(aimCon, relofCon, vals.get(i), colConData);
			cra.delConceptValueofRel(vals.get(i), nrelofCon, aimCon, colConData);
			
		}
		
		
		return SUCCESS;
	}
	
	
	/**
	 * 删除一个概念的关系值，注意：这个关系值所对应的逆关系中也将删除该概念。
	 * @return
	 */
	public String delRelValofCon(){
		
		System.out.println(aimCon+"  "+relofCon+"    "+newConTermId);
		cra.delConceptValueofRel(aimCon, relofCon, newConTermId, colConData);
		int r=Integer.valueOf(relofCon);
		if(r%2==0)r=r-1;else r=r+1;
		String nrelofCon=String.valueOf(r);
		cra.delConceptValueofRel(newConTermId, nrelofCon, aimCon, colConData);
		return SUCCESS;
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
	ArrayList<String> idlist=cra.getTermId(updateConOldTerm, cid, colConTerm);
	if(idlist.size()==1){ hasIds=false;
		
	}else{
		
		hasIds=true;
		
	}
	
	cra.updateTerm(updateConTerm, updateConOldTerm, colConTerm, cid, updateConId, hasIds);
	
	if(hasIds){
		
		cra.addConceptValueofRel(updateConId, "csyn", updateConTerm, colConData);
		
	}else{
		
		cra.updateConceptValueofRel(updateConId, "csyn", updateConOldTerm, updateConTerm, colConData);
		
	}
	
	
	
		
		return SUCCESS;
	}
	
	
	/**
	 * 用来提供前台概念编辑树中的数据
	 * @return
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	
	
	/**
	 * 将数据库中的数据转成可以展示到前台的数据。
	 * @param fatherId
	 * @param subRelId
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	public List<TreeModel> covertDbtoFront(String fatherId,String subRelId){
		
		
		List<TreeModel> list=new ArrayList<TreeModel>();
		
		ArrayList<String> subNodes=cra.getConceptofRel(fatherId,subRelId, colConData);//获取子节点集合
		
		for(int i=0;i<subNodes.size();i++){
			
			ArrayList<String> texts=cra.getConceptofRel(subNodes.get(i),"csyn", colConData);//获取子节点的文本
			//System.out.println("展开节点的子节点集合:    "+texts);
			TreeModel tm=new TreeModel();
			tm.setText(texts.get(texts.size()-1));
			String nodestr=subNodes.get(i);
			/*ArrayList<String> fathNodes=cra.getConceptofRel(nodestr, isa, colConData);//这里是防止树中出现相同id号的节点。因为这会导致在页面显示时的错误。
			for (int k=0;k<fathNodes.size();k++){
				
				if ((fathNodes.get(k)==fatherId)&&(k!=0)) nodestr=nodestr+String.valueOf(k);
			}*/
			
			tm.setId(nodestr);
			ArrayList<String> leafs=cra.getPartRelsofConcept(subNodes.get(i), colConData);//获取部分属性，
			if (!leafs.contains(sub)){
				
				tm.setLeaf(true);
				
			}//如果没有子节点关系，则这个概念是个叶子节点。
			
			list.add(tm);
		//	System.out.println(list);
		}
	   //System.out.println(subNodes);
		
		
		
		
	   return list;
	}
	
	
	
	
	
	
	/**
	 * 将概念树的数据传到前台。	
	 * @return
	 */
	public String conTreeDataForFront() {
		
		//System.out.println("ok1"+text);
		System.out.println("conTreeDataForFront-test  "+text);
		System.out.println("conTreeDataForFront-id   "+id);
		System.out.println("conTreeDataForFront-newconterm   "+newConTerm);
		menus=new ArrayList<TreeModel>();
		//System.out.println("浏览概念传来的参数:   "+id);
		try {
			menus=this.covertDbtoFront(id,sub);
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	    this.setMenus(menus);
	    //System.out.println(menus);
	    return SUCCESS;
	    
	 
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
 * 将数据库中的term及Id号配置成前台所需要的形式。
 * @return
 */
@SuppressWarnings("unchecked")
public List<TermModel> covertTermDbtoFront(){
		
		
		List<TermModel> list=new ArrayList<TermModel>();
		
		ArrayList<String> tstr=new ArrayList<String>();
		ArrayList<String> istr=new ArrayList<String>();
		DBObject db;
		DBCursor cur=colConTerm.find();
		//int termNum=0;
		while (cur.hasNext()){
			//tm.setId(String.valueOf(termNum));
//			db=cur.next();
//			TermModel tm=new TermModel();
//			tstr=(ArrayList<String>) db.get("term");
//			tm.setText(tstr.get(tstr.size()-1));
//			istr=(ArrayList<String>) db.get(cid);
//			tm.setId(istr);			
//			list.add(tm);
			
			db=cur.next();
			istr=(ArrayList<String>) db.get(cid);
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
 * 将term数据传递到前台
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
    return SUCCESS;
}



/**
 * 用于针对前台传过来的某一个术语，找到相同的术语的不同的ID
 * @param term
 * @return
 */
public List<TermModel> covertSearchTermDbtoFront(String term){
	List<TermModel> list=new ArrayList<TermModel>();
	ArrayList<String> idlist=cra.getDupConTermID(term, colConTerm);
	for(int i=0;i<idlist.size();i++){
		TermModel tm=new TermModel();
		ArrayList<String> temp=new ArrayList<String>();
		temp.add(idlist.get(i));
		tm.setId(temp);
		ArrayList<String> fid=new ArrayList<String>();
		if(idlist.get(i).charAt(1)=='1') fid=cra.getConceptofRel(idlist.get(i), "2100000003", colConData);
		else fid=cra.getConceptofRel(idlist.get(i), isa, colConData);
		ArrayList<String> ft=cra.getConceptofRel(fid.get(0), "csyn", colConData);
		tm.setText(term+"->>"+ft.get(0));
		System.out.println(term+"--"+ft.get(0));
		list.add(tm);
		System.out.println(tm);
	}
	
	 return list;
	
}



/**
 * 将term数据传递到前台
 * @return
 * @throws UnsupportedEncodingException 
 */
public String conSearchTermDataForFront() throws UnsupportedEncodingException {
	
	String newparam = new String(searchTerm.getBytes("iso-8859-1"),"utf-8");
	
	System.out.println("searching term:  "+newparam);
	/*System.out.println(text);
	System.out.println(id);
	System.out.println(newConTerm);*/
	terms=new ArrayList<TermModel>();
	
	try {
		terms=this.covertSearchTermDbtoFront(newparam);
	} catch (MongoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
    this.setTerms(terms);
    return SUCCESS;
}


/**
 * 将数据库中的term及Id号配置成前台所需要的形式。
 * @return
 */
@SuppressWarnings("unchecked")
public List<TermModel> covertTermOnlyDbtoFront(){
		
		
		List<TermModel> list=new ArrayList<TermModel>();
		
		ArrayList<String> tstr=new ArrayList<String>();
		ArrayList<String> istr=new ArrayList<String>();
		
		DBObject db;
		DBCursor cur=colConTerm.find();
		//int termNum=0;
		while (cur.hasNext()){
			//tm.setId(String.valueOf(termNum));
			db=cur.next();
		istr=(ArrayList<String>) db.get(cid);
			for(int i=0;i<istr.size();i++){
				if (istr.get(i).charAt(1)=='0')
				{ArrayList<String> temp=new ArrayList<String>();
					TermModel tm=new TermModel();
					temp.add(istr.get(i));
					tm.setId(temp);
					
					tstr=(ArrayList<String>) db.get("term");
				tm.setText(tstr.get(tstr.size()-1));
				list.add(tm);
				//temp.remove(istr.get(i));
				}
			}
			
			
			
			//termNum=termNum+1;
			//System.out.println(tm.getId());
			//System.out.println(termNum);
		//System.out.println(cur.next().get("term"));
		//System.out.println(cur.size(.;
		}
		System.out.println(list);
	   return list;
	}

/**
 * 将term数据传递到前台
 * @return
 */
public String conTermDataOnlyForFront() {
	
	//System.out.println("ok1"+text);
	/*System.out.println(text);
	System.out.println(id);
	System.out.println(newConTerm);*/
	terms=new ArrayList<TermModel>();
	
	try {
		terms=this.covertTermOnlyDbtoFront();
	} catch (MongoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
    this.setTerms(terms);
    return SUCCESS;
}






/**
 * 前台传过来一个概念的ID，一个关系的ID，给这个概念加上关系，并赋给关系值一个“1000000000”
 * 
 * @return
 */
public String addRelofConcept(){
	
	System.out.println("add relation    "+relationval);
	//System.out.println("add relation of concept   "+idTemp);
	System.out.println("add relation    "+ConIdForBody);
	ArrayList<String> idstr=new ArrayList<String>();
	idstr=cra.getTermId(newRelTerm, rid, colRelTerm);//取id号，是个列表
	System.out.println("add relation id   "+idstr.get(0));
	System.out.println("add relation of concept   "+ConIdForBody);
	cra.addOnlyRelNoValueOfConcept(ConIdForBody, idstr.get(0), colConData);//为一个概念增加一个关系，值为“1000000000”
	return SUCCESS;
			
}

/**
 * 为概念增加关系值
 * @return
 */
public String addVauleToRelofConcept(){
	
	ArrayList<String> relidstr=new ArrayList<String>();
	ArrayList<String> validstr=new ArrayList<String>();
	
	relidstr=cra.getTermId(relation, rid, colRelTerm);
	validstr=cra.getTermId(relationval, cid, colConTerm);
	System.out.println("add relation ++   "+relidstr);
	System.out.println("add relation val ++   "+validstr);
	
	if(!ConIdForBody.equals(validstr)){
	cra.addVauleToConceptRel(ConIdForBody, relidstr.get(0), validstr.get(0), colConData);//
	cra.delConceptValueofRel(ConIdForBody, relidstr.get(0), idnull, colConData);
	}
	
	return SUCCESS;
}



/*public List<TreeModel> convertConBodyTreeRelDataForFront(){
	
	System.out.println("okok");
	List<TreeModel> list=new ArrayList<TreeModel>();
	ArrayList<String> relstr=new ArrayList<String>();
	relstr=cra.getAllRelsofConcept(ConIdForBody, colConData);
	for(int i=0;i<relstr.size();i++){
		TreeModel tm=new TreeModel();
		//this.setBodyTreeRelId(relstr.get(0));
		tm.setText(relstr.get(i));
		tm.setId(relstr.get(i));
		ArrayList<String> relstrc=new ArrayList<String>();
		List<TreeModel> listc=new ArrayList<TreeModel>();
		relstrc=cra.getConceptofRel(ConIdForBody, relstr.get(i), colConData);
		for (int j=0;j<relstrc.size();j++){
			TreeModel tmc=new TreeModel();
			tmc.setText(relstrc.get(j));
			//tmc.setId(relstrc.get(j));
			tmc.setLeaf(true);
			listc.add(tmc);
		}
		
		list.add(tm);
	}
	
	return list;
	
}*/



/**
 * 为概念体的树结构提供数据准备
 * @return
 */
public List<TreeModel> convertConBodyTreeRelDataForFront(){
	
	
	
	List<TreeModel> list=new ArrayList<TreeModel>();
	List<TermModel> termlist=new ArrayList<TermModel>();
	ArrayList<String> relstr=new ArrayList<String>();
	relstr=cra.getPartRelsofConcept(ConIdForBody, colConData);//得到概念的所有属性
	for(int i=0;i<relstr.size();i++){
		//this.setBodyTreeRelId(relstr.get(0));
		ArrayList<String> tlist=new ArrayList<String>();
		tlist=cra.getPropofRel(relstr.get(i), "csyn", colRelData);
		TreeModel tm=new TreeModel();
		TermModel term=new TermModel();
		tm.setText(tlist.get(tlist.size()-1));//显示关系的同义词庥中的第一个术语
		term.setText(tlist.get(tlist.size()-1));
		tm.setId(relstr.get(i));
		term.setId(relstr);
		list.add(tm);
		termlist.add(term);
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
/**
 * 为属性分类中的属性关系框，提供某一个概念的属性和关系
 * @return
 */
public List<IdAndTermModel> convertConBodyTreeRelDataForClass(){
	
	//List<TreeModel> list=new ArrayList<TreeModel>();
	List<IdAndTermModel> termlist=new ArrayList<IdAndTermModel>();
	ArrayList<String> relstr=new ArrayList<String>();
	relstr=cra.getPartRelsofConcept(ConIdForBody, colConData);//得到概念的所有属性
	for(int i=0;i<relstr.size();i++){
		//this.setBodyTreeRelId(relstr.get(0));
		ArrayList<String> tlist=new ArrayList<String>();
		tlist=cra.getPropofRel(relstr.get(i), "csyn", colRelData);
	//	TreeModel tm=new TreeModel();
		IdAndTermModel term=new IdAndTermModel();
	//	tm.setText(tlist.get(tlist.size()-1));//显示关系的同义词庥中的第一个术语
		term.setText(tlist.get(tlist.size()-1));
	//	tm.setId(relstr.get(i));
		term.setId(relstr.get(i));
	//	list.add(tm);
		termlist.add(term);
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
	//System.out.println(termlist);
	
	return termlist;
	
}

public String conBodyDataForClass() {
	
	System.out.println("选择的概念ID  "+ConIdForBody);
	
	termsforclass=new ArrayList<IdAndTermModel>();
	
	try {
		termsforclass=this.convertConBodyTreeRelDataForClass();
	} catch (MongoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	   
	this.setTermsforclass(termsforclass);
	
		
    return SUCCESS;
}



public List<TreeModel> convertConBodyTreeValueDataForFront(String RelId){
		ArrayList<String> relstrc=new ArrayList<String>();
		List<TreeModel> listc=new ArrayList<TreeModel>();
		
		relstrc=cra.getConceptofRel(ConIdForBody, RelId, colConData);
		for (int j=0;j<relstrc.size();j++){
			ArrayList<String> tlist=new ArrayList<String>();
			tlist=cra.getConceptofRel(relstrc.get(j), "csyn", colConData);
			
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

/**
 * 是用于得到某一个关系或属性的值，并且提供给属性分类窗口中的分类信息里面，即显示出将要分成的类的名称。
 * @param RelId
 * @return
 */
public List<IdAndTermModel> convertConBodyTreeValueDataForClass(String RelId){
	List<IdAndTermModel> listc=new ArrayList<IdAndTermModel>();
	
	ArrayList<String> cons=cra.getConceptofRel(ConIdForBody, RelId, colConData);
	for(int i=0;i<cons.size();i++){
		ArrayList<String> relstrc=new ArrayList<String>();
		ArrayList<String> rels=cra.getPartRelsofConcept(cons.get(i), colConData);
		if(rels.contains(sub))	relstrc=cra.getConceptofRel(cons.get(i), sub, colConData);
		if(relstrc.isEmpty()) relstrc.add(cons.get(i));
	
	for (int j=0;j<relstrc.size();j++){
		ArrayList<String> tlist=new ArrayList<String>();
		tlist=cra.getConceptofRel(relstrc.get(j), "csyn", colConData);
		
		IdAndTermModel tmc=new IdAndTermModel();
		
		tmc.setText(tlist.get(tlist.size()-1));
		tmc.setId(relstrc.get(j));
		listc.add(tmc);
	}
	
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

public String asyncConBodyLoadforClass(){
	
	System.out.println("选择的概念ID  "+ConIdForBody);
	System.out.println("这是属性节点的ID值："+BodyTreeRelId);
	valueforclass=new ArrayList<IdAndTermModel>();
	try {
		valueforclass=this.convertConBodyTreeValueDataForClass(BodyTreeRelId);
	} catch (MongoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	 this.setConbodyreles(conbodyreles);	
		
	 return SUCCESS;
	
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
	
		
    return SUCCESS;
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
		
	 return SUCCESS;
	
}

//
public String getAllFathers(String conid){
	ArrayList<String> father=cra.getConceptofRel(conid, isa, colConData);
	String temp=new String();
	temp=father.get(0);
	fatherPath="/"+temp+fatherPath;
	if(!temp.equals("1000000000")) {temp=getAllFathers(temp);}
	System.out.println("father path is:"+fatherPath);
	return fatherPath;	
}
//这里是得到一个节点的父节点路径，注意：一个节点可能有两个以上父节点，这个要考虑。不过，本函数只考虑一条路径。以后再考虑多条路路径。
public String treePath(){
	 
	expPath=this.getAllFathers(aimIdForExp).concat("/").concat(aimIdForExp);
	 
	
	System.out.println("extpath:  " +expPath);
	
	 
	this.setExpPath(expPath);
	 return SUCCESS;
}



public static void main(String[] args) throws UnknownHostException, MongoException {
	
	ConData d=new ConData();
	d.ConIdForBody="1000000105";
	d.BodyTreeRelId="2000000039";
	d.newConTerm="test2";
	d.aimIdForExp="1000000114";
	
	//d.addRelofConcept();
	//d.covertDbtoFront("1000000000","2100000002");
	//d.conTreeDataForFront();
	//d.conTermDataOnlyForFront();
	//d.convertConBodyTreeDataForFront();
	//d.convertConBodyTreeValueDataForFront("gloss");
	//d.addConTerm();
	//d.convertConBodyTreeRelDataForFront();
	//d.delRelValofCon();
	//d.conBodyDataForClass();
	//d.delRelofCon();
	//d.addClassCons();
	//d.convertConBodyTreeValueDataForClass("2000000039");
	//d.getAllFathers("1000000114");
	//d.getPath();
	d.covertSearchTermDbtoFront("星爷");
}

	


public String getSearchTerm() {
	return searchTerm;
}

public void setSearchTerm(String searchTerm) {
	this.searchTerm = searchTerm;
}

public String getAimIdForExp() {
	return aimIdForExp;
}

public void setAimIdForExp(String aimIdForExp) {
	this.aimIdForExp = aimIdForExp;
}

public String getExpPath() {
	return expPath;
}

public void setExpPath(String expPath) {
	this.expPath = expPath;
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

public String getNewConId() {
	return newConId;
}

public void setNewConId(String newConId) {
	this.newConId = newConId;
}

public String getNewRelTerm() {
	return newRelTerm;
}

public void setNewRelTerm(String newRelTerm) {
	this.newRelTerm = newRelTerm;
}

public String getConIdForBody() {
	return ConIdForBody;
}

public void setConIdForBody(String conIdForBody) {
	ConIdForBody = conIdForBody;
}

	public List<TreeModel> getConbodyvalues() {
	return conbodyvalues;
}

public void setConbodyvalues(List<TreeModel> conbodyvalues) {
	this.conbodyvalues = conbodyvalues;
}

public List<TreeModel> getConbodyreles() {
	return conbodyreles;
}

public void setConbodyreles(List<TreeModel> conbodyreles) {
	this.conbodyreles = conbodyreles;
}

	public boolean isSonNodeFlag() {
	return sonNodeFlag;
}

public void setSonNodeFlag(boolean sonNodeFlag) {
	this.sonNodeFlag = sonNodeFlag;
}

	public String getBodyTreeRelId() {
	return BodyTreeRelId;
}

	public void setBodyTreeRelId(String bodyTreeRelId) {
	BodyTreeRelId = bodyTreeRelId;
}

	public String getNewConTermId() {
		return newConTermId;
	}

	public void setNewConTermId(String newConTermId) {
		this.newConTermId = newConTermId;
	}

	public String getNewConTerm() {
		return newConTerm;
	}

	public void setNewConTerm(String newConTerm) {
		this.newConTerm = newConTerm;
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
	
	public String execute()
	{
		return "success";
	}

	public List<TreeModel> getMenus() {
		return menus;
	}

	public void setMenus(List<TreeModel> menus) {
		this.menus = menus;
	}
	
	public String getFatherNode() {
		return fatherNode;
	}



	public void setFatherNode(String fatherNode) {
		this.fatherNode = fatherNode;
	}

	public List<TermModel> getTerms() {
		return terms;
	}

		public void setTerms(List<TermModel> terms) {
		this.terms = terms;
	}
	
	
	public String getUpdateConId() {
			return updateConId;
		}

		public void setUpdateConId(String updateConId) {
			this.updateConId = updateConId;
		}

	public String getUpdateConTerm() {
			return updateConTerm;
		}

		public void setUpdateConTerm(String updateConTerm) {
			this.updateConTerm = updateConTerm;
		}
		
	
	public String getUpdateConOldTerm() {
			return updateConOldTerm;
		}

		public void setUpdateConOldTerm(String updateConOldTerm) {
			this.updateConOldTerm = updateConOldTerm;
		}

		public String getAimCon() {
			return aimCon;
		}

		public void setAimCon(String aimCon) {
			this.aimCon = aimCon;
		}

		public String getRelofCon() {
			return relofCon;
		}

		public void setRelofCon(String relofCon) {
			this.relofCon = relofCon;
		}

		public List<IdAndTermModel> getTermsforclass() {
			return termsforclass;
		}

		public void setTermsforclass(List<IdAndTermModel> termsforclass) {
			this.termsforclass = termsforclass;
		}

		public List<IdAndTermModel> getValueforclass() {
			return valueforclass;
		}

		public void setValueforclass(List<IdAndTermModel> valueforclass) {
			this.valueforclass = valueforclass;
		}
		
	

	/*public String getMenuString() {
		return menuString;
	}


	public void setMenuString(String menuString) {
		this.menuString = menuString;
	}
*/

	
}
