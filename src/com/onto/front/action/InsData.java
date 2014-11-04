package com.onto.front.action;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;

public class InsData {

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
	
	//用来为概念添加关系和关系值的参数
	private String newRelTerm;//从前台传来的概念的新关系的Id呈。
	private String newConId;
	private String relation;
	private String relationval;

	//用来处理概念体数据展示的前台参数
	private String ConIdForBody;//需要展示关系属性的概念
	private String BodyTreeRelId;//用来展示的关系属性
	private List<TreeModel> conbodyvalues;
	private List<TreeModel> conbodyreles;
	
	private String isa="2100000003";
	private String sub="2100000004";
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
	
	
public InsData() throws UnknownHostException, MongoException{
		
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
		term=newConTerm+String.valueOf(connum);
		System.out.println("前台传过来的新实例:   "+term);
		System.out.println("新实例 的类:   "+fatherNode);
		System.out.println("新实例的节点ID:   "+newConTermId);
		
		this.setNewConTerm(term);
		
		if (newConTermId.isEmpty()){
			
			cra.addConTerm("insid", term, true, colConTerm, colConData, colId);
			
			ArrayList<String> idlist=new ArrayList<String>();
			idlist=cra.getTermId(term, "cid", colConTerm);
			System.out.println("为新实例分配的ID号:  "+idlist);
			this.setNewConTermId(idlist.get(idlist.size()-1));
			
			cra.addConceptRel(fatherNode,sub, idlist.get(idlist.size()-1), colConData);
			cra.addConceptRel(idlist.get(idlist.size()-1),isa, fatherNode, colConData);
			this.copyRelofFather(newConTermId, fatherNode);
			
		}else{
			
			cra.addConTermID(term, newConTermId, colConTerm);
			
			
			cra.addConceptRel(fatherNode,sub, newConTermId, colConData);
			cra.addConceptRel(newConTermId,isa, fatherNode, colConData);
			this.copyRelofFather(newConTermId, fatherNode);
			
		}
		
		connum=connum+1; 
		//System.out.println(connum);
		
		return "success";
	}
	
	
	
	public void copyRelofFather(String insId,String fatherId){
		
		ArrayList<String> rellist=cra.getPartRelsofConcept(fatherId, colConData);
		for(int i=0;i<rellist.size();i++){
			if(!rellist.get(i).equals("2100000001")&&!rellist.get(i).equals("2100000004"))
			cra.addConceptValueofRel(insId, rellist.get(i), "0000000000", colConData);
			
		}
		
		
		
		
	}
	
	
	/**
	 * 为实例的前台提供数据转换
	 * @param fatherId
	 * @param subRelId
	 * @return
	 */
	
public List<TreeModel> covertDbtoFront(String fatherId,String subRelId){
		
		
		List<TreeModel> list=new ArrayList<TreeModel>();
		
		ArrayList<String> subNodes=cra.getConceptofRel(fatherId,subRelId, colConData);//获取子节点集合
		
		for(int i=0;i<subNodes.size();i++){
			
			ArrayList<String> texts=cra.getConceptofRel(subNodes.get(i),"csyn", colConData);//获取子节点的文本
			System.out.println("展开节点的子节点集合:    "+subNodes);
			TreeModel tm=new TreeModel();
			tm.setText(texts.get(texts.size()-1));
			tm.setId(subNodes.get(i));
			ArrayList<String> leafs=cra.getPartRelsofConcept(subNodes.get(i), colConData);//获取部分属性，
			if (!leafs.contains(sub)){
				
				tm.setLeaf(true);
				
			}//如果没有子节点关系，则这个概念是个叶子节点。
			
			list.add(tm);
			
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
	/*System.out.println(text);
	System.out.println(id);
	System.out.println(newConTerm);*/
	menus=new ArrayList<TreeModel>();
	System.out.println("浏览概念传来的参数:   "+id);
	try {
		menus=this.covertDbtoFront(id,sub);
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
			db=cur.next();
			TermModel tm=new TermModel();
			istr=(ArrayList<String>) db.get(cid);
			for (int i=0;i<istr.size();i++){
				if (istr.get(i).charAt(1)=='0')	istr.remove(i);
			}
			if(!istr.isEmpty()){
			tstr=(ArrayList<String>) db.get("term");
			
			
			tm.setText(tstr.get(tstr.size()-1));
			tm.setId(istr);
			list.add(tm);
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
    //System.out.println(terms);
    return "success";
}



/**
 * 为概念体的树结构提供数据准备
 * @return
 */
public List<TreeModel> convertConBodyTreeRelDataForFront(){
	
	
	
	List<TreeModel> list=new ArrayList<TreeModel>();
	ArrayList<String> relstr=new ArrayList<String>();
	relstr=cra.getPartRelsofConcept(ConIdForBody, colConData);//得到概念的所有属性
	for(int i=0;i<relstr.size();i++){
		//this.setBodyTreeRelId(relstr.get(0));
		ArrayList<String> tlist=new ArrayList<String>();
		tlist=cra.getPropofRel(relstr.get(i), "csyn", colRelData);
		TreeModel tm=new TreeModel();
		tm.setText(tlist.get(tlist.size()-1));//显示关系的同义词庥中的第一个术语
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

	InsData d=new InsData();
	//d.addConTerm();
	//d.covertDbtoFront("1000000013",d.sub);
	d.conTermDataForFront();
	
	
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




	public List<TreeModel> getMenus() {
	return menus;
}






public void setMenus(List<TreeModel> menus) {
	this.menus = menus;
}






	
	
	
	
	
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	

}
