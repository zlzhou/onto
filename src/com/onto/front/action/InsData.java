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

	private String id;//��
	private String text;
	private List<TreeModel> menus;//�� ǰ̨���ݸ��������ݲ���
	private String newConTerm;//�����ɾ���ڵ㰴ť�����Ĳ��� text
	private String newConTermId;//�����ɾ���ڵ㰴ť�����Ĳ��� id
	private String fatherNode;//������ʾ�����Ĳ���������ȷ��չ���Ľڵ����ĸ���
	private List<TermModel> terms;//��ǰ̨���ݸ���term�����ݲ�����������ʾ��combobox�С�
	
	//�����༭�����������õ��Ĳ���
	private String updateConTerm;//�µ�����
	private String updateConId;//����Ӧ������id
	private String updateConOldTerm;//�ɵ���������
	
	//����Ϊ������ӹ�ϵ�͹�ϵֵ�Ĳ���
	private String newRelTerm;//��ǰ̨�����ĸ�����¹�ϵ��Id�ʡ�
	private String newConId;
	private String relation;
	private String relationval;

	//�����������������չʾ��ǰ̨����
	private String ConIdForBody;//��Ҫչʾ��ϵ���Եĸ���
	private String BodyTreeRelId;//����չʾ�Ĺ�ϵ����
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
	 * �����༭���������Ĳ�����
	 * �����µ������Լ���Ӧ����,�����һ��û��ID�ŵ��¸�������һ��ID�ţ�Ȼ���޸����������ݱ������һ����ͬ�������ֻ��������ӵ�����������ֻ�ı��ϵ
	 * @return
	 */
	public String addConTerm(){
		
		
		String term=new String();
		term=newConTerm+String.valueOf(connum);
		System.out.println("ǰ̨����������ʵ��:   "+term);
		System.out.println("��ʵ�� ����:   "+fatherNode);
		System.out.println("��ʵ���Ľڵ�ID:   "+newConTermId);
		
		this.setNewConTerm(term);
		
		if (newConTermId.isEmpty()){
			
			cra.addConTerm("insid", term, true, colConTerm, colConData, colId);
			
			ArrayList<String> idlist=new ArrayList<String>();
			idlist=cra.getTermId(term, "cid", colConTerm);
			System.out.println("Ϊ��ʵ�������ID��:  "+idlist);
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
	 * Ϊʵ����ǰ̨�ṩ����ת��
	 * @param fatherId
	 * @param subRelId
	 * @return
	 */
	
public List<TreeModel> covertDbtoFront(String fatherId,String subRelId){
		
		
		List<TreeModel> list=new ArrayList<TreeModel>();
		
		ArrayList<String> subNodes=cra.getConceptofRel(fatherId,subRelId, colConData);//��ȡ�ӽڵ㼯��
		
		for(int i=0;i<subNodes.size();i++){
			
			ArrayList<String> texts=cra.getConceptofRel(subNodes.get(i),"csyn", colConData);//��ȡ�ӽڵ���ı�
			System.out.println("չ���ڵ���ӽڵ㼯��:    "+subNodes);
			TreeModel tm=new TreeModel();
			tm.setText(texts.get(texts.size()-1));
			tm.setId(subNodes.get(i));
			ArrayList<String> leafs=cra.getPartRelsofConcept(subNodes.get(i), colConData);//��ȡ�������ԣ�
			if (!leafs.contains(sub)){
				
				tm.setLeaf(true);
				
			}//���û���ӽڵ��ϵ������������Ǹ�Ҷ�ӽڵ㡣
			
			list.add(tm);
			
		}
	   //System.out.println(subNodes);
		
		
		
		
	   return list;
	}


/**
 * �������������ݴ���ǰ̨��	
 * @return
 */
public String conTreeDataForFront() {
	
	//System.out.println("ok1"+text);
	/*System.out.println(text);
	System.out.println(id);
	System.out.println(newConTerm);*/
	menus=new ArrayList<TreeModel>();
	System.out.println("���������Ĳ���:   "+id);
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
 * �����ݿ��е�term��Id�����ó�ǰ̨����Ҫ����ʽ��
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
 * ��term���ݴ��ݵ�ǰ̨
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
 * Ϊ����������ṹ�ṩ����׼��
 * @return
 */
public List<TreeModel> convertConBodyTreeRelDataForFront(){
	
	
	
	List<TreeModel> list=new ArrayList<TreeModel>();
	ArrayList<String> relstr=new ArrayList<String>();
	relstr=cra.getPartRelsofConcept(ConIdForBody, colConData);//�õ��������������
	for(int i=0;i<relstr.size();i++){
		//this.setBodyTreeRelId(relstr.get(0));
		ArrayList<String> tlist=new ArrayList<String>();
		tlist=cra.getPropofRel(relstr.get(i), "csyn", colRelData);
		TreeModel tm=new TreeModel();
		tm.setText(tlist.get(tlist.size()-1));//��ʾ��ϵ��ͬ������еĵ�һ������
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
	
	System.out.println("ѡ��ĸ���ID  "+ConIdForBody);
	
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
	
	System.out.println("ѡ��ĸ���ID  "+ConIdForBody);
	System.out.println("�������Խڵ��IDֵ��"+BodyTreeRelId);
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
