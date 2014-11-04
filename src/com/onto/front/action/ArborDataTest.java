package com.onto.front.action;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;

public class ArborDataTest {

	private Map params;
	private static boolean nodeExpand;
	private String glossText;
//	private List<NodeModelForArbor> nodelist;
//	private List<EdgeModelForArbor> edgelist;
//	
	
	public ConRelAdmin cra;
	 DB db;
	DBCollection colConData;
	


	DBCollection colConTerm;
	DBCollection colRelData;
	DBCollection colRelTerm;
	DBCollection colId;
	
	private static String  fatherId;
	
	String subRelId="2100000002";
	
public ArborDataTest() throws UnknownHostException, MongoException{
		
		cra=new ConRelAdmin();
		db=cra.creatOntoDB("ontopeople");
		colConData=cra.creatTable(db, "ConData");
		colConTerm=cra.creatTable(db, "ConTerm");
		colRelData=cra.creatTable(db, "RelData");
		colRelTerm=cra.creatTable(db, "RelTerm");
		colId=cra.creatTable(db, "idPool");
		
		
		
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String arbor(){
		
		/*List<Map> mlist=new ArrayList<Map>();
	
		Map<String,Object> m=new HashMap<String, Object>();
		
		
		//NodeModelForArbor node=new NodeModelForArbor();
				
		NodeDataModelOfArbor	nmodel=new NodeDataModelOfArbor();
		nmodel=this.NodeModel();*/
		
//		nmodel.setColor("green");//���ýڵ����ɫ
//		nmodel.setShape("dot");
//		nmodel.setAlpha("1");
//		
		//node.setNmodel(nmodel);
//		m.put("aa",nmodel);//���ýڵ���ı�����
//		m.put("bb",nmodel);
//		m.put("cc",nmodel);
//		mlist.add(m);
		//System.out.println(fatherId);
		
		//�����Ƕ�ȡĳ���ڵ�������ӽڵ㡣�Լ����ӽڵ����һ���ڵ㣬���չ��������
		/*ArrayList<String> subNodes=cra.getConceptofRel(fatherId,subRelId, colConData);//��ȡ�ӽڵ㼯��
		m.put("����", nmodel);
		for(int i=0;i<subNodes.size();i++){
			
			ArrayList<String> texts=cra.getConceptofRel(subNodes.get(i),"csyn", colConData);//��ȡ�ӽڵ���ı�
			m.put(texts.get(0),nmodel);
		}
		
		mlist.add(m);
		System.out.println(mlist);*/
//		System.out.println(con.getMenus()+"   ok");
		
		
//		nmodel=new NodeDataModelOfArbor();
//		nmodel.setColor("yellow");
//		nmodel.setShape("dot");
//		nmodel.setAlpha("1");
//		//node.setNmodel(nmodel);
//		m.put("bb",nmodel);
//		mlist.add(m);
//		
//		nmodel=new NodeDataModelOfArbor();
//		nmodel.setColor("green");
//		nmodel.setShape("dot");
//		nmodel.setAlpha("1");
//		//node.setNmodel(nmodel);
//		m.put("cc",nmodel);
//		mlist.add(m);
		
//		System.out.println(m);
		
		/*
		EdgeModelOfArbor edgemodel=new EdgeModelOfArbor();
//		List<Map> melist=new ArrayList<Map>();
		Map<String, Object> me=new HashMap<String, Object>();
		Map<String, Object> melist=new HashMap<String, Object>();
	//	List<Map> edge=new ArrayList<Map>();
		
		edgemodel.setLength((float) 0.8);
		edgemodel.setLabel("rel1");
		me.put("bb", edgemodel);
		
		
		edgemodel=new EdgeModelOfArbor();
		edgemodel.setLength((float) 0.8);
		edgemodel.setLabel("rel2");
		me.put("cc", edgemodel);
		melist.put("aa",me);
			*/
//		System.out.println(mlist);
//		Map<String,Object> nme=new HashMap<String, Object>();
//		nme.put("aa", melist);
//		
//		edge.add(nme);
//		
//		edgemodel.setLength((float) 0.8);
//		edgemodel.setLabel("rel1");
//		me.put("aa", edgemodel);
//		melist.add(me);
//		
//		edgemodel.setLength((float) 0.8);
//		edgemodel.setLabel("rel2");
//		me.put("cc", edgemodel);
//		melist.add(me);
//		
//		//Map<String, EdgeModelOfArbor> nme=new HashMap();
//		nme.put("bb", melist);
//		edge.add(nme);
//		
//		edgemodel.setLength((float) 0.8);
//		edgemodel.setLabel("rel1");
//		me.put("bb", edgemodel);
//		melist.add(me);
//		
//		edgemodel.setLength((float) 0.8);
//		edgemodel.setLabel("rel2");
//		me.put("aa", edgemodel);
//		melist.add(me);
//		
//		//Map<String, EdgeModelOfArbor> nme=new HashMap();
//		nme.put("cc",  melist);
//		edge.add(nme);
//		System.out.println("afasdfafad");
		
			
//		params=new HashMap();
//		
//		params.put("nodes", m);
//		params.put("edges", melist);
	//	params=this.NodeandEdgeTwoLayer(fatherId);//չ������
		//System.out.println(nodeExpand);
		if(!nodeExpand)
		params=this.NodeandEdge(fatherId);//ֻչ��һ��
		else params=this.NodeandEdgeTwoLayer(fatherId);
		//params.put("edges", edge);
		
	//	System.out.println(params.toString());
		
		this.setParams(params);
		
		
		
		/*EdgeModelForArbor edge=new EdgeModelForArbor();
		EdgeModelOfArbor edgemodel=new EdgeModelOfArbor();
		List<NodeModelForEdge> subnodes=new ArrayList<NodeModelForEdge>();
		NodeModelForEdge nodeforedge=new NodeModelForEdge();
		edgelist=new ArrayList<EdgeModelForArbor>();
		
		edgemodel.setLength((float) 0.8);
		edgemodel.setLabel("rel1");
		nodeforedge.setEmodel(edgemodel);
		nodeforedge.setNode("bb");
		subnodes.add(nodeforedge);
		edge.setNode("aa");
		edge.setSubnodes(subnodes);
		edgelist.add(edge);
		
		edgemodel.setLength((float) 0.8);
		edgemodel.setLabel("rel2");
		nodeforedge.setEmodel(edgemodel);
		nodeforedge.setNode("cc");
		subnodes.add(nodeforedge);
		edge.setNode("aa");
		edge.setSubnodes(subnodes);
		edgelist.add(edge);
		
		edgemodel.setLength((float) 0.8);
		edgemodel.setLabel("rel3");
		nodeforedge.setEmodel(edgemodel);
		nodeforedge.setNode("cc");
		subnodes.add(nodeforedge);
		edge.setNode("bb");
		edgelist.add(edge);
		
		
		
		
		edge.setSubnodes(subnodes);
		
		edge.setSubnodes(subnodes);
		
		params=new DataForArbor();
		
		params.setNodelist(nodelist);
		params.setEdgelist(edgelist);
		
		System.out.println(params.toString());*/
		
	return "success";
	}
	
	//�ڵ��ģ��
	public NodeDataModelOfArbor NodeModel(String color) {
		
		NodeDataModelOfArbor	nmodel=new NodeDataModelOfArbor();
		nmodel.setColor(color);//���ýڵ����ɫ
		//nmodel.setShape("dot");
		nmodel.setAlpha("2");
		return nmodel;
	}
	
	
	
	//�ߵ�ģ��
	public EdgeModelOfArbor EdgeModel(String relname) {
		
		EdgeModelOfArbor edgemodel=new EdgeModelOfArbor();
		edgemodel.setLength((float) 0.8);
		edgemodel.setLabel(relname);
		return edgemodel;
		
	}
/**
 * ��ʾһ���ڵ�����й�ϵ�ڵ㣬ֻ��ʾһ�㡣
 * @param nodeId
 * @return
 */
	public HashMap NodeandEdge(String nodeId){
		Map para= new HashMap();
		Map<String, Object> melist=new HashMap<String, Object>();
		Map<String,Object> m=new HashMap<String, Object>();//�ڵ�Map
		Map<String, Object> me=new HashMap<String, Object>();//�ߵ�Map
		NodeDataModelOfArbor	fnmodel=this.NodeModel("red");
		NodeDataModelOfArbor	csynnmodel=this.NodeModel("purple");
		NodeDataModelOfArbor	inmodel=this.NodeModel("blue");//instance node color
		NodeDataModelOfArbor	snmodel=this.NodeModel("green");
		EdgeModelOfArbor emodel=new EdgeModelOfArbor();
		ArrayList<String> ntexts=cra.getConceptofRel(nodeId,"csyn", colConData);//��ȡ�ڵ���ı�
		
		for(int k=0;k<ntexts.size();k++){
			
			emodel=this.EdgeModel("ͬ��ʼ�");
			me.put(ntexts.get(k), emodel);
			m.put(ntexts.get(k), csynnmodel);
			
		}
		m.put(ntexts.get(0),fnmodel);//ֻѡ��һ�����������Ӧ��ͬ��ʼ��ĵ�һ����
		

		
		ArrayList<String> relofnode=cra.getPartRelsofConcept(nodeId, colConData);
		for (int i=0;i<relofnode.size();i++){
			ArrayList<String> etexts=cra.getPropofRel(relofnode.get(i),"csyn", colRelData);//��ȡ��ϵ���ı�
			emodel=this.EdgeModel(etexts.get(etexts.size()-1));
			ArrayList<String> valofrel=cra.getConceptofRel(nodeId,relofnode.get(i), colConData);//��ȡ�ڵ��ϵ��ֵ
			for(int j=0;j<valofrel.size();j++){
				ArrayList<String> valtexts=cra.getConceptofRel(valofrel.get(j),"csyn", colConData);//��ȡ�ڵ���ı�
				me.put(valtexts.get(valtexts.size()-1),emodel);
				if(valofrel.get(j).charAt(1)=='1')
					m.put(valtexts.get(valtexts.size()-1), inmodel);
				else m.put(valtexts.get(valtexts.size()-1), snmodel);
			}
			
		}
		melist.put(ntexts.get(0),me);
		//cra.get
		//System.out.println(m);
		//System.out.println(melist);
		para.put("nodes", m);
		para.put("edges",melist);
	//	System.out.println(para);
		ArrayList<String> gloss=cra.getConceptofRel(nodeId,"gloss", colConData);//��ȡgloss���ı�
		
		glossText=gloss.get(gloss.size()-1);
		
		this.setGlossText(glossText);
		
		
		return (HashMap) para;
	}
/**
 * 
 * @param nodeId
 * @return
 */
	public HashMap NodeandEdgeTwoLayer(String nodeId){
		Map para= new HashMap();
		Map<String, Object> melist=new HashMap<String, Object>();
		Map<String,Object> m=new HashMap<String, Object>();//�ڵ�Map
		Map<String, Object> me=new HashMap<String, Object>();//�ߵ�Map
		
		NodeDataModelOfArbor	fnmodel=this.NodeModel("red");//father node color
		NodeDataModelOfArbor	inmodel=this.NodeModel("blue");//instance node color
		NodeDataModelOfArbor	snmodel=this.NodeModel("green");//sons node color
		EdgeModelOfArbor emodel=new EdgeModelOfArbor();
		ArrayList<String> subnodes=new ArrayList<String>();
		ArrayList<String> ntexts=cra.getConceptofRel(nodeId,"csyn", colConData);//��ȡ�ڵ���ı�
		m.put(ntexts.get(0),fnmodel);
		
		ArrayList<String> relofnode=cra.getPartRelsofConcept(nodeId, colConData);//��ȡ�ڵ�Ĳ��ֹ�ϵ ���ڵ�ţ���Ӣͬ���
		for (int i=0;i<relofnode.size();i++){
			ArrayList<String> etexts=cra.getPropofRel(relofnode.get(i),"csyn", colRelData);//��ȡ��ϵ���ı�
			emodel=this.EdgeModel(etexts.get(etexts.size()-1));
			ArrayList<String> valofrel=new ArrayList<String>();
			valofrel=cra.getConceptofRel(nodeId,relofnode.get(i), colConData);//��ȡ�ڵ��ϵ��ֵ
			subnodes.addAll(valofrel);
			for(int j=0;j<valofrel.size();j++){
				ArrayList<String> valtexts=cra.getConceptofRel(valofrel.get(j),"csyn", colConData);//��ȡ�ڵ���ı�
				if(valofrel.get(j).charAt(1)=='1')
					m.put(valtexts.get(valtexts.size()-1), inmodel);
				else
					m.put(valtexts.get(valtexts.size()-1), snmodel);
				me.put(valtexts.get(valtexts.size()-1),emodel);
				
				
//				ArrayList<String> relofsubnode=cra.getPartRelsofConcept(valofrel.get(j), colConData);
//				for(int k=0;k<relofsubnode.size();k++){
//					ArrayList<String> subetexts=cra.getPropofRel(relofsubnode.get(k),"csyn", colRelData);//��ȡ��ϵ���ı�
//					emodel=this.EdgeModel(subetexts.get(subetexts.size()-1));
//					ArrayList<String> valofrelofsubnode=cra.getConceptofRel(valofrel.get(j),relofsubnode.get(k), colConData);//��ȡ�ڵ��ϵ��ֵ
//					
//					for(int l=0;l<valofrelofsubnode.size();l++){
//						ArrayList<String> subvaltexts=cra.getConceptofRel(valofrelofsubnode.get(l),"csyn", colConData);//��ȡ�ڵ���ı�
//					if(!m.containsKey(subvaltexts)){
//						m.put(subvaltexts.get(subvaltexts.size()-1), snmodel);
//						me.put(subvaltexts.get(subvaltexts.size()-1),emodel);
//					}
//						
//					}
//					
//				}
//				
//				melist.put(valtexts.get(valtexts.size()-1),me);
			}
			
		}
		
		melist.put(ntexts.get(0),me);
		
		//System.out.println(subnodes);
		
		for (int sub=0;sub<subnodes.size();sub++){
			Map<String, Object> subme=new HashMap<String, Object>();//�ߵ�Map
			ArrayList<String> valtexts=cra.getConceptofRel(subnodes.get(sub),"csyn", colConData);//��ȡ�ڵ���ı�
			ArrayList<String> relofsubnode=cra.getPartRelsofConcept(subnodes.get(sub), colConData);
			for(int k=0;k<relofsubnode.size();k++){
				ArrayList<String> subetexts=cra.getPropofRel(relofsubnode.get(k),"csyn", colRelData);//��ȡ��ϵ���ı�
				emodel=this.EdgeModel(subetexts.get(subetexts.size()-1));
				ArrayList<String> valofrelofsubnode=cra.getConceptofRel(subnodes.get(sub),relofsubnode.get(k), colConData);//��ȡ�ڵ��ϵ��ֵ
				
				for(int l=0;l<valofrelofsubnode.size();l++){
					ArrayList<String> subvaltexts=cra.getConceptofRel(valofrelofsubnode.get(l),"csyn", colConData);//��ȡ�ڵ���ı�
					
					
				if(!m.containsKey(subvaltexts.get(subvaltexts.size()-1)))
					
					if(valofrelofsubnode.get(l).charAt(1)=='1')
					
					m.put(subvaltexts.get(subvaltexts.size()-1), inmodel);
				
					else m.put(subvaltexts.get(subvaltexts.size()-1), snmodel);
				
				
					subme.put(subvaltexts.get(subvaltexts.size()-1),emodel);
					
				
					
		//		System.out.println(m.keySet());
				}
				
			}
			
			melist.put(valtexts.get(valtexts.size()-1),subme);
			
			
		}
		
		
		
	
		
		
	//	System.out.println(m);
		
	//	System.out.println(melist);
		
		
		
		//System.out.println(m);
		//System.out.println(melist);
		para.put("nodes", m);
		para.put("edges",melist);
	//	System.out.println(para);
		
		return (HashMap) para;
	}

	
	
	


	

	public String getGlossText() {
	return glossText;
}



public void setGlossText(String glossText) {
	this.glossText = glossText;
}



	public Map<String, Object> getParams() {
		return params;
	}


	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	
	public String getFatherId() {
		return fatherId;
	}



	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}

	
	public boolean isNodeExpand() {
		return nodeExpand;
	}



	public void setNodeExpand(boolean nodeExpand) {
		this.nodeExpand = nodeExpand;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArborDataTest d = null;
		try {
			d = new ArborDataTest();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		d.NodeandEdge("1100008959");
		
	}

}
