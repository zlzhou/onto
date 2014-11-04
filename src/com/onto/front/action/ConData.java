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
	private boolean sonNodeFlag;//�ɵ���������
	private String searchTerm;
	
	//����Ϊ������ӹ�ϵ�͹�ϵֵ�Ĳ���
	private String newRelTerm;//��ǰ̨�����ĸ�����¹�ϵ��Id�ʡ�
	private String newConId;
	private String relation;
	private String relationval;
	
	//����ɾ����ϵ�͹�ϵֵ�Ĳ���
	private String aimCon;//��ɾ����ϵ�͹�ϵֵ�ĸ��
	private String relofCon="2000000004";//��ɾ���ĸ���Ĺ�ϵ

	//�����������������չʾ��ǰ̨����
	private String ConIdForBody;//��Ҫչʾ��ϵ���Եĸ���
	private String BodyTreeRelId;//����չʾ�Ĺ�ϵ����
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
	 * �����༭���������Ĳ�����
	 * �����µ������Լ���Ӧ����,�����һ��û��ID�ŵ��¸�������һ��ID�ţ�Ȼ���޸����������ݱ������һ����ͬ�������ֻ��������ӵ�����������ֻ�ı��ϵ
	 * @return
	 */
	public String addConTerm(){
		
		
		String term=new String();
		term=newConTerm+"_"+String.valueOf(connum);
		System.out.println("ǰ̨��������������:   "+term);
		System.out.println("������ �ĸ��ڵ�:   "+fatherNode);
		System.out.println("������ �Ľڵ�ID:   "+newConTermId);
		
		this.setNewConTerm(term);
		
		if (newConTermId.isEmpty()){
			
			cra.addConTerm("conid", term, true, colConTerm, colConData, colId);
			
			ArrayList<String> idlist=new ArrayList<String>();
			idlist=cra.getTermId(term, "cid", colConTerm);
			System.out.println("Ϊ����������ID��:  "+idlist);
			this.setNewConTermId(idlist.get(idlist.size()-1));
			
			cra.addConceptRel(fatherNode,sub, idlist.get(idlist.size()-1), colConData);
		//	cra.addConceptRel(idlist.get(idlist.size()-1),isa, fatherNode, colConData);
			//�����ӽڵ㣬������ڵ�����Ի��ϵ���пɼ̳еģ����ӽڵ�����ӡ����Զ��ǿɼ̳еġ�
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
	 * ���������ȼ�鸸�ڵ�����û�п��Լ̳е����ԣ�����У��򽫸����Լ���ֵ�����ӽڵ㡣���һ�Ҫ���һ���µ����ԣ��ӽڵ�����Ҫ��һ�����������ֵܽڵ�����ԡ�
	 * ���еı�־�������������Է��ࡱ���ǡ�һ�������ӽڵ㣬�� ��������Է��࣬��true ����ӽڵ���false �����true����Ϊ��ϵ�͹�ϵֵ��ȷ�������������ﲻ���������ڷ�������Լ���ֵ��
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
				vallist=cra.getConceptofRel(fatherid, rellist.get(i), colConData);//��ȡĳ��ϵ�Ĺ�ϵֵ
				for(int j=0;j<vallist.size();j++)
				{
					cra.addConceptRel(newTermid, rellist.get(i), vallist.get(j), colConData);
				}
				
			}
		}
		ArrayList<String> term=cra.getConceptofRel(newTermid, "csyn", colConData);
		this.addAttOfNewSonCon(newTermid, term.get(term.size()-1)+"����");
		ArrayList<String> newattidlist=cra.getTermId(term.get(term.size()-1)+"����", "rid",colRelTerm);
		cra.addConceptRel(newTermid, newattidlist.get(newattidlist.size()-1), idnull, colConData);
	}
	
	
	/**
	 * Ϊһ���ӽڵ㣬����һ���µ����ԣ���Ϊÿһ���ڵ���ӽڵ㶼Ӧ����һ���Լ��Ĳ�ͬ�ڱ���ֵܽڵ�����ԡ�
	 * @param newattid �µ��ӽڵ��idֵ��
	 * @param newattterm �µ��ӽڵ����������������
	 */
	public void addAttOfNewSonCon(String newattid,String newattterm){
		
			cra.addRelTerm("attid", newattterm, true, colRelTerm, colRelData, colId);//����һ����������õ�һ������ID
			
			ArrayList<String> idlist=new ArrayList<String>();
			idlist=cra.getTermId(newattterm, "rid", colRelTerm);
			System.out.println("Ϊ����������ID��:  "+idlist);
			
			this.setNewConTermId(idlist.get(idlist.size()-1));//ȡidlist�е��ַ���
			
			cra.addRelationRel("2000000000", sub, idlist.get(idlist.size()-1), colRelData);
			//cra.addRelationRel(idlist.get(idlist.size()-1),isa, fatherNode, colRelData);
			cra.addRelationValueofAtt(idlist.get(idlist.size()-1), "prop", "inh", colRelData);//���Ծ�Ϊ�ɼ̳еġ�
			
			String r=new String();
			r=String.valueOf(Integer.valueOf(idlist.get(idlist.size()-1))+1);
			cra.addRelationRel("2000000000", sub, r, colRelData);
			//cra.addRelationRel(r,isa, fatherNode, colRelData);
			cra.addRelationValueofAtt(r, "prop", "inh", colRelData);//���Ծ�Ϊ�ɼ̳еġ�				
	}	
		
	
	/**
	 * �������Է����ϵ��action����
	 * @return
	 */
	public String addClassCons(){
		
		ArrayList<String> cons=cra.getConceptofRel(ConIdForBody, BodyTreeRelId, colConData);//�õ������ĳ����ϵ��id����
		
		for(int i=0;i<cons.size();i++){
			ArrayList<String> relstrc=new ArrayList<String>();
			ArrayList<String> rels=cra.getPartRelsofConcept(cons.get(i), colConData);
			if(rels.contains(sub))	relstrc=cra.getConceptofRel(cons.get(i), sub, colConData);
			if(relstrc.isEmpty()) relstrc.add(cons.get(i));
//			relstrc=cra.getConceptofRel(cons.get(i), sub, colConData);
//			if(relstrc.isEmpty()) relstrc.add(cons.get(i));
		
		
		for (int j=0;j<relstrc.size();j++){
			
			ArrayList<String> tlist=new ArrayList<String>();
			tlist=cra.getConceptofRel(relstrc.get(j), "csyn", colConData);//�õ������ϵ�ĵ�һ��id������ļ���
			cra.addConTerm("conid", tlist.get(tlist.size()-1)+newConTerm, true, colConTerm, colConData, colId);//�������id�����ﵽ�����
			ArrayList<String> idlist=new ArrayList<String>();
			idlist=cra.getTermId(tlist.get(tlist.size()-1)+newConTerm, "cid", colConTerm);
			System.out.println("Ϊ�·�����������ID��:  "+idlist);
			this.setNewConTermId(idlist.get(idlist.size()-1));
			cra.addConceptRel(ConIdForBody,sub, idlist.get(idlist.size()-1), colConData);//����Ϊ��������ӽڵ�
			cra.addConceptRel(idlist.get(idlist.size()-1), BodyTreeRelId, relstrc.get(j), colConData);//�Ӹ����BodyTreeRelID����Ӧ����ȷ���ġ�
			this.addSonCon(ConIdForBody, idlist.get(idlist.size()-1),true);
			//this.addAttOfNewSonCon(idlist.get(idlist.size()-1), tlist.get(tlist.size()-1)+newConTerm+"����");//����һ���µ����ԣ����ҰѼ̳е�����Ҳ�ӵ�������
			//ArrayList<String> newattidlist=cra.getTermId(tlist.get(tlist.size()-1)+newConTerm+"����", "rid", colRelData);
			//cra.addConceptRel(idlist.get(idlist.size()-1), newattidlist.get(newattidlist.size()-1), idnull, colConData);
			//cra.addConceptRel(idlist.get(idlist.size()-1),isa, fatherNode, colConData);
					
			
		}
		
	}
		return SUCCESS;
	}
	
	/**
	 * �ж��Ƿ���һ���������ԡ����ø���ķ����ǰ��������ֵ���з���ġ�
	 * @param conid
	 * @return
	 */
	public String getClassAttofConcept(String conid){
		String classatt=new String();
		ArrayList<String> reloffather=cra.getPartRelsofConcept(conid, colConData);//�õ�����Ĺ�ϵ�б�
		for(int i=0;i<reloffather.size();i++){
			//ֻ������������
			if(reloffather.get(i).charAt(1)=='0'){
				
				ArrayList<String> vallist=cra.getConceptofRel(conid, reloffather.get(i), colConData);//�õ����Ե�ֵ��һ��Ӧ����һ�������ﲻʧһ����
				for(int j=0;j<vallist.size();j++){
					
					//���ﻹû�����
				}
				
				
			}
			
			
		}
		
		
		
		
		
		return classatt;
	}
	
	
	
	/**
	 * 
	 * ����ɾ�����ע�⣬���һ�������ж�������ֻɾ������ĸ�����ܽ����������������ɾ����
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
	 * ɾ��һ������Ĺ�ϵ����ϵֵ��ע�⣺��Щ��ϵֵ�еĹ�ϵ��Ҳ��ɾ���ø���
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
	 * ɾ��һ������Ĺ�ϵֵ��ע�⣺�����ϵֵ����Ӧ�����ϵ��Ҳ��ɾ���ø��
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
	 * //���޸�������е�term�ֶε����ݣ����ҵ���Ӧ��id��ȥ�޸�ͬ��ʼ�
    //�޸�����ʱҪע�⣬���������и������Ӧ�������ϵĸ��������һ������.����������ֻ��Ӧһ��id�ţ���ֱ���޸ĸ��������ơ�
	 * @return
	 */
public String updateTerm(){
		
	System.out.println("�����������ݿ������������---> "+updateConTerm);	
	System.out.println("�����������ݿ�ľ���������---> "+updateConOldTerm);
	System.out.println("�����������ݿ��������Id---> "+updateConId);	
		//���޸�������е�term�ֶε����ݣ����ҵ���Ӧ��id��ȥ�޸�ͬ��ʼ�
    //�޸�����ʱҪע��
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
	 * �����ṩǰ̨����༭���е�����
	 * @return
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	
	
	/**
	 * �����ݿ��е�����ת�ɿ���չʾ��ǰ̨�����ݡ�
	 * @param fatherId
	 * @param subRelId
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	public List<TreeModel> covertDbtoFront(String fatherId,String subRelId){
		
		
		List<TreeModel> list=new ArrayList<TreeModel>();
		
		ArrayList<String> subNodes=cra.getConceptofRel(fatherId,subRelId, colConData);//��ȡ�ӽڵ㼯��
		
		for(int i=0;i<subNodes.size();i++){
			
			ArrayList<String> texts=cra.getConceptofRel(subNodes.get(i),"csyn", colConData);//��ȡ�ӽڵ���ı�
			//System.out.println("չ���ڵ���ӽڵ㼯��:    "+texts);
			TreeModel tm=new TreeModel();
			tm.setText(texts.get(texts.size()-1));
			String nodestr=subNodes.get(i);
			/*ArrayList<String> fathNodes=cra.getConceptofRel(nodestr, isa, colConData);//�����Ƿ�ֹ���г�����ͬid�ŵĽڵ㡣��Ϊ��ᵼ����ҳ����ʾʱ�Ĵ���
			for (int k=0;k<fathNodes.size();k++){
				
				if ((fathNodes.get(k)==fatherId)&&(k!=0)) nodestr=nodestr+String.valueOf(k);
			}*/
			
			tm.setId(nodestr);
			ArrayList<String> leafs=cra.getPartRelsofConcept(subNodes.get(i), colConData);//��ȡ�������ԣ�
			if (!leafs.contains(sub)){
				
				tm.setLeaf(true);
				
			}//���û���ӽڵ��ϵ������������Ǹ�Ҷ�ӽڵ㡣
			
			list.add(tm);
		//	System.out.println(list);
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
		System.out.println("conTreeDataForFront-test  "+text);
		System.out.println("conTreeDataForFront-id   "+id);
		System.out.println("conTreeDataForFront-newconterm   "+newConTerm);
		menus=new ArrayList<TreeModel>();
		//System.out.println("���������Ĳ���:   "+id);
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
    return SUCCESS;
}



/**
 * �������ǰ̨��������ĳһ������ҵ���ͬ������Ĳ�ͬ��ID
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
 * ��term���ݴ��ݵ�ǰ̨
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
 * �����ݿ��е�term��Id�����ó�ǰ̨����Ҫ����ʽ��
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
 * ��term���ݴ��ݵ�ǰ̨
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
 * ǰ̨������һ�������ID��һ����ϵ��ID�������������Ϲ�ϵ����������ϵֵһ����1000000000��
 * 
 * @return
 */
public String addRelofConcept(){
	
	System.out.println("add relation    "+relationval);
	//System.out.println("add relation of concept   "+idTemp);
	System.out.println("add relation    "+ConIdForBody);
	ArrayList<String> idstr=new ArrayList<String>();
	idstr=cra.getTermId(newRelTerm, rid, colRelTerm);//ȡid�ţ��Ǹ��б�
	System.out.println("add relation id   "+idstr.get(0));
	System.out.println("add relation of concept   "+ConIdForBody);
	cra.addOnlyRelNoValueOfConcept(ConIdForBody, idstr.get(0), colConData);//Ϊһ����������һ����ϵ��ֵΪ��1000000000��
	return SUCCESS;
			
}

/**
 * Ϊ�������ӹ�ϵֵ
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
 * Ϊ����������ṹ�ṩ����׼��
 * @return
 */
public List<TreeModel> convertConBodyTreeRelDataForFront(){
	
	
	
	List<TreeModel> list=new ArrayList<TreeModel>();
	List<TermModel> termlist=new ArrayList<TermModel>();
	ArrayList<String> relstr=new ArrayList<String>();
	relstr=cra.getPartRelsofConcept(ConIdForBody, colConData);//�õ��������������
	for(int i=0;i<relstr.size();i++){
		//this.setBodyTreeRelId(relstr.get(0));
		ArrayList<String> tlist=new ArrayList<String>();
		tlist=cra.getPropofRel(relstr.get(i), "csyn", colRelData);
		TreeModel tm=new TreeModel();
		TermModel term=new TermModel();
		tm.setText(tlist.get(tlist.size()-1));//��ʾ��ϵ��ͬ������еĵ�һ������
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
 * Ϊ���Է����е����Թ�ϵ���ṩĳһ����������Ժ͹�ϵ
 * @return
 */
public List<IdAndTermModel> convertConBodyTreeRelDataForClass(){
	
	//List<TreeModel> list=new ArrayList<TreeModel>();
	List<IdAndTermModel> termlist=new ArrayList<IdAndTermModel>();
	ArrayList<String> relstr=new ArrayList<String>();
	relstr=cra.getPartRelsofConcept(ConIdForBody, colConData);//�õ��������������
	for(int i=0;i<relstr.size();i++){
		//this.setBodyTreeRelId(relstr.get(0));
		ArrayList<String> tlist=new ArrayList<String>();
		tlist=cra.getPropofRel(relstr.get(i), "csyn", colRelData);
	//	TreeModel tm=new TreeModel();
		IdAndTermModel term=new IdAndTermModel();
	//	tm.setText(tlist.get(tlist.size()-1));//��ʾ��ϵ��ͬ������еĵ�һ������
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
	
	System.out.println("ѡ��ĸ���ID  "+ConIdForBody);
	
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
 * �����ڵõ�ĳһ����ϵ�����Ե�ֵ�������ṩ�����Է��ര���еķ�����Ϣ���棬����ʾ����Ҫ�ֳɵ�������ơ�
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
	
	System.out.println("ѡ��ĸ���ID  "+ConIdForBody);
	System.out.println("�������Խڵ��IDֵ��"+BodyTreeRelId);
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
	
	System.out.println("ѡ��ĸ���ID  "+ConIdForBody);
	
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
//�����ǵõ�һ���ڵ�ĸ��ڵ�·����ע�⣺һ���ڵ�������������ϸ��ڵ㣬���Ҫ���ǡ�������������ֻ����һ��·�����Ժ��ٿ��Ƕ���··����
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
	d.covertSearchTermDbtoFront("��ү");
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
