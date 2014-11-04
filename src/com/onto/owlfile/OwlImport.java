package com.onto.owlfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;

public class OwlImport {

	static String filePath="e:/ontodata/test.owl";
	public ConRelAdmin cra;
	 DB db;
	DBCollection colConData;
	DBCollection colConTerm;
	DBCollection colRelData;
	DBCollection colRelTerm;
	DBCollection colId;
	
	//构造函数
	public OwlImport() throws UnknownHostException, MongoException{
	cra=new ConRelAdmin();
	 db=cra.creatOntoDB("owlin");
	 colConData=cra.creatTable(db, "ConData");
	 colConTerm=cra.creatTable(db, "ConTerm");
	 colRelData=cra.creatTable(db, "RelData");
	 colRelTerm=cra.creatTable(db, "RelTerm");
	 colId=cra.creatTable(db, "idPool");
	}
	
	
	/**
	 * 将一个OWL本体中的所有类加编号写到MongoDB ConTerm。
	 * @param filename
	 * @param ont
	 */
	
	public void writeOntClasstoTermfile(String filename,OntModel ont){
		
		int j=0,k=0;
		FileOutputStream out = null;
		
		try {
					out=new FileOutputStream(filename);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		PrintStream p=new PrintStream(out);
		
		for(Iterator i=ont.listClasses();i.hasNext();){
			OntClass c=(OntClass)i.next();
			if(!c.isAnon()){
				j++;
				cra.addConTerm("conid", c.toString(), true, colConTerm, colConData, colId);
				p.println(c+"  "+(1000000000+j));
				System.out.print("Class "+j+"   "+c.toString());	
				System.out.println(c.getLocalName());
			}
		
		}
		
		for(Iterator i=ont.listIndividuals();i.hasNext();){
			
			k++;
			j++;
			
			Individual ind=(Individual) i.next();
			if(!ind.isAnon()){
				p.println(ind+"   "+(1100000000+j));
				cra.addConTerm("insid", ind.toString(), true, colConTerm, colConData, colId);
				System.out.println(ind.getLocalName());
			}
		}
	}
	
	
	/**
	 * 将OWL文件中的属性写到文件中，并且是成对的属性。
	 * @param filename
	 * @param ont
	 */
	
	public void writeOntPropertytoTermfile(String filename,OntModel ont){
		
		int k=0,j=2;//这里j=2的原因是把 subclassof和superclassof 两个关系给留出代号。分别为2000000001和2000000002
		FileOutputStream out = null;
		
		
		try {
					out=new FileOutputStream(filename);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		PrintStream p=new PrintStream(out);
		p.println("subclass_of"+"  "+2000000001);
		p.println("superclass_of"+"  "+2000000002);
		cra.addRelTerm("relid", "subclass_of", true, colRelTerm, colRelData, colId);
		cra.addRelTerm("relid", "type", true, colRelTerm, colRelData, colId);
//		cra.addRelTerm("relid", "superclass_of", true, colRelTerm, colRelData, colId);	
		for(Iterator i=ont.listObjectProperties();i.hasNext();){
			
			j++;
			
			OntProperty op=(OntProperty) i.next();
			if(!op.isAnon()){
				cra.addRelTerm("relid", op.getLocalName().toString(),true, colRelTerm, colRelData, colId);
				p.println(op.getLocalName()+"  "+2000000000+j);
				j++;
				p.println(op.getLocalName()+"_inv  "+2000000000+(j));//逆属性
				System.out.println("Class "+j+"   "+op.getLocalName());	
				
			}
			
		}
		
		
		for(Iterator i=ont.listDatatypeProperties();i.hasNext();){
			
			k++;
			
			DatatypeProperty op=(DatatypeProperty) i.next();
			if(!op.isAnon()){
				cra.addRelTerm("attid", op.getLocalName().toString(),true, colRelTerm, colRelData, colId);
				p.println(op.getLocalName()+"  "+2100000000+k);
				p.println(op.getLocalName()+"_inv  "+2100000000+(k+1));//逆属性
				k++;
				System.out.println("Class "+k+"   "+op.getLocalName());	
				
			}
			
		}
		
		
		
		
		
	}
	
	
	
	/**
	 * 专门写两个上位关系
	 * @param p
	 */
	public void writeSupSubPro(PrintStream p){
		
		p.print(2000000001+"  ");
		p.print("dom"+"  "+1+"  "+1000000032+"  ");
		p.print("ran"+"  "+1+"  "+1000000032+"  ");
		p.print("esyn"+"  "+1+"  "+"subclass_of"+"  ");
		p.print("csyn"+"  "+1+"  "+"中文名称"+"  ");
		p.print("pro"+"  "+1+"  "+"pro"+"  ");
		p.println("gloss"+"  "+2+"  "+"englishgloss"+"  "+"中文gloss");
	
		p.print(2000000002+"  ");
		p.print("dom"+"  "+1+"  "+1000000032+"  ");
		p.print("ran"+"  "+1+"  "+1000000032+"  ");
		p.print("esyn"+"  "+1+"  "+"superclass_of"+"  ");
		p.print("csyn"+"  "+1+"  "+"中文名称"+"  ");
		p.print("pro"+"  "+1+"  "+"pro"+"  ");
		p.println("gloss"+"  "+2+"  "+"englishgloss"+"  "+"中文gloss");
		
	}
	
	/**
	 * 写relData文件
	 * @param filename
	 * @param ont
	 * 
	 */
	public void writeOntPropertytoDatafile(String filename,OntModel ont){
			
			int j=0,k=0;
			FileOutputStream out = null;

			try {
					out=new FileOutputStream(filename);
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			PrintStream p=new PrintStream(out);
			this.writeSupSubPro(p);
			
			for(Iterator i=ont.listObjectProperties();i.hasNext();){
				OntProperty pro=(OntProperty) i.next();
				if(!pro.isAnon()){
					
					ArrayList<String> rid=cra.getTermId(pro.getLocalName(), "rid", colRelTerm);//这个地方要注意：rid是一个列表，但是对OWL来说，一般只有一个值。因此在这里默认只有一个值。
					if(pro.getDomain()!=null){
						
						ArrayList<String> cid=cra.getTermId(pro.getDomain().toString(), "cid", colConTerm);//这个地方要注意：rid是一个列表，但是对OWL来说，一般只有一个值。因此在这里默认只有一个值。					
						cra.addDomainorRangeofRel(rid.get(0), "dom", cid.get(0), colRelData);// function write to Mongodb
						
						System.out.println(pro.getLocalName()+"  dom  "+pro.getDomain().getLocalName());
						p.print(pro.getLocalName()+"  dom  "+pro.getDomain().getLocalName());
							}
							else {
								
								cra.addDomainorRangeofRel(rid.get(0), "dom", "0000000000", colRelData);// function write to Mongodb
								
								System.out.println(pro.getLocalName()+"  dom  "+"null");
							p.print(pro.getLocalName()+"  dom  "+"null");
							}
					
					if(pro.getRange()!=null){
						
						ArrayList<String> cid=cra.getTermId(pro.getRange().toString(), "cid", colConTerm);//这个地方要注意：rid是一个列表，但是对OWL来说，一般只有一个值。因此在这里默认只有一个值。					
						cra.addDomainorRangeofRel(rid.get(0), "ran", cid.get(0), colRelData);// function write to Mongodb
						
						
						System.out.println(pro.getLocalName()+"  ran  "+pro.getRange().getLocalName());
						p.print("  ran  "+pro.getRange().getLocalName());
							}
							else {
								
								cra.addDomainorRangeofRel(rid.get(0), "ran", "0000000000", colRelData);// function write to Mongodb
								
								System.out.println(pro.getLocalName()+"  ran  "+"null");
							p.print("  ran  "+"null");
							}
					
					p.print("  esyn"+"  "+1+"  "+pro.getLocalName()+"  ");
					p.print("  csyn"+"  "+1+"  "+"中文名称"+"  ");
					p.print("  pro"+"  "+1+"  "+"pro"+"  ");
					p.println("  gloss"+"  "+2+"  "+"englishgloss"+"  "+"中文gloss");
					
					
					//下面是逆属性
					if(pro.getDomain()!=null){
						System.out.println(pro.getLocalName()+"  ran  "+pro.getDomain().getLocalName());
						p.print(pro.getLocalName()+"_inv"+"  ran  "+pro.getDomain().getLocalName());
							}
							else {System.out.println(pro.getLocalName()+"  ran  "+"null");
							p.print(pro.getLocalName()+"_inv"+"  ran  "+"null");
							}
					
					if(pro.getRange()!=null){
						System.out.println(pro.getLocalName()+"  dom  "+pro.getRange().getLocalName());
						p.print("  dom  "+pro.getRange().getLocalName());
							}
							else {System.out.println(pro.getLocalName()+"  dom  "+"null");
							p.print("  dom  "+"null");
							}
					p.print("  esyn"+"  "+1+"  "+pro.getLocalName()+"_inv"+"  ");
					p.print("  csyn"+"  "+1+"  "+"中文名称"+"  ");
					p.print("  pro"+"  "+1+"  "+"pro"+"  ");
					p.println("  gloss"+"  "+2+"  "+"englishgloss"+"  "+"中文gloss");
					
					
				}
				
				
			}
			
			//下面是data属性
			
			for(Iterator i=ont.listDatatypeProperties();i.hasNext();){
				
				OntProperty pro=(OntProperty) i.next(); 
				if(!pro.isAnon()){
					
					ArrayList<String> rid=cra.getTermId(pro.getLocalName(), "rid", colRelTerm);//这个地方要注意：rid是一个列表，但是对OWL来说，一般只有一个值。因此在这里默认只有一个值。
						if(pro.getDomain()!=null){
						
						ArrayList<String> cid=cra.getTermId(pro.getDomain().toString(), "cid", colConTerm);//这个地方要注意：rid是一个列表，但是对OWL来说，一般只有一个值。因此在这里默认只有一个值。		
						if(!cid.isEmpty())
						cra.addDomainorRangeofRel(rid.get(0), "dom", cid.get(0), colRelData);// function write to Mongodb
						
						System.out.println(pro.getLocalName()+"  dom  "+pro.getDomain().getLocalName());
						p.print(pro.getLocalName()+"  dom  "+pro.getDomain().getLocalName());
							}
							else {
								cra.addDomainorRangeofRel(rid.get(0), "dom", "0000000000", colRelData);// function write to Mongodb
							System.out.println(pro.getLocalName()+"  dom  "+"null");
							p.print(pro.getLocalName()+"  dom  "+"null");
							}
					
					if(pro.getRange()!=null){
						
						ArrayList<String> cid=cra.getTermId(pro.getRange().toString(), "cid", colConTerm);//这个地方要注意：rid是一个列表，但是对OWL来说，一般只有一个值。因此在这里默认只有一个值。		
						if(!cid.isEmpty())
						cra.addDomainorRangeofRel(rid.get(0), "ran", cid.get(0), colRelData);// function write to Mongodb
						
						System.out.println(pro.getLocalName()+"  ran  "+pro.getRange().getLocalName());
						p.print("  ran  "+pro.getRange().getLocalName());
							}
							else {
								
								cra.addDomainorRangeofRel(rid.get(0), "ran", "0000000000", colRelData);// function write to Mongodb
							System.out.println(pro.getLocalName()+"  ran  "+"null");
							p.print("  ran  "+"null");
							}
					
					p.print("  esyn"+"  "+1+"  "+pro.getLocalName()+"  ");
					p.print("  csyn"+"  "+1+"  "+"中文名称"+"  ");
					p.print("  pro"+"  "+1+"  "+"pro"+"  ");
					p.println("  gloss"+"  "+2+"  "+"englishgloss"+"  "+"中文gloss");
			
					//下面是逆属性
					if(pro.getDomain()!=null){
						System.out.println(pro.getLocalName()+"  ran  "+pro.getDomain().getLocalName());
						p.print(pro.getLocalName()+"_inv"+"  ran  "+pro.getDomain().getLocalName());
							}
							else {System.out.println(pro.getLocalName()+"  ran  "+"null");
							p.print(pro.getLocalName()+"_inv"+"  ran  "+"null");
							}
					
					if(pro.getRange()!=null){
						System.out.println(pro.getLocalName()+"  dom  "+pro.getRange().getLocalName());
						p.print("  dom  "+pro.getRange().getLocalName());
							}
							else {System.out.println(pro.getLocalName()+"  dom  "+"null");
							p.print("  dom  "+"null");
							}
					p.print("  esyn"+"  "+1+"  "+pro.getLocalName()+"_inv"+"  ");
					p.print("  csyn"+"  "+1+"  "+"中文名称"+"  ");
					p.print("  pro"+"  "+1+"  "+"pro"+"  ");
					p.println("  gloss"+"  "+2+"  "+"englishgloss"+"  "+"中文gloss");
					
				}
				
				
			}
			
			
			
	}
	
	//这里只讨论上下位关系，以后如果有其它关系，再添加。
	public void writeOntClasstoDatafile(String filename,OntModel ont){
		
		int j=0,k=0;
		FileOutputStream out = null;
		
		
		try {
					out=new FileOutputStream(filename);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		PrintStream p=new PrintStream(out);
		
		for(Iterator i=ont.listClasses();i.hasNext();){
			
			j++;
			
			OntClass c=(OntClass)i.next();
			if(!c.isAnon()){
		//		System.out.println(c.getDisjointWith());
				p.print(c.getLocalName()+"   ");
				ArrayList<String> cid=cra.getTermId(c.toString(), "cid", colConTerm);//对于owl文件来说，这里cid不妨只按只有一个值来看，即一个术语对应一个概念号。
				//该类是子类的父类，这一段程序是找其子类。
				if(c.hasSubClass()){
					p.print("superclass_of  ");
					for(Iterator subnum=c.listSubClasses();subnum.hasNext();){
						
						OntClass subc=(OntClass) subnum.next();
						if(!subc.isAnon()){
						
						ArrayList<String> cid1=cra.getTermId(subc.toString(), "cid", colConTerm);//对于owl文件来说，这里cid不妨只按只有一个值来看，即一个术语对应一个概念号。	
						cra.addConceptRel(cid.get(0), "2100000002", cid1.get(0), colConData);
						p.print(subc.getLocalName()+"  ");
						}
						
					}
				}
				
				
				//查找该类的父类。
				if(c.hasSuperClass()){
					p.print("subclass_of  ");
					for(Iterator subnum=c.listSuperClasses();subnum.hasNext();){
						
						OntClass subc=(OntClass) subnum.next();
						if(!subc.isAnon()){
							ArrayList<String> cid1=cra.getTermId(subc.toString(), "cid", colConTerm);//对于owl文件来说，这里cid不妨只按只有一个值来看，即一个术语对应一个概念号。	
							cra.addConceptRel(cid.get(0), "2100000001", cid1.get(0), colConData);	
						p.print(subc.getLocalName()+"  ");
						}
						
					}
				}
				
				//查找该类的父类。
				
					p.print("csyn  ");
					p.print(c.getLocalName()+"  ");
					for(Iterator subnum=c.listEquivalentClasses();subnum.hasNext();){
						
						OntClass subc=(OntClass) subnum.next();
						if(!subc.isAnon())
							
						p.print(subc.getLocalName()+"  ");
						
					}
				
				
				
				//这里是查找类的属性的值域
				
				for(Iterator<OntProperty> ipp=c.listDeclaredProperties(true);ipp.hasNext();){
				
					OntProperty pro=ipp.next();
					
					
					p.print(pro.getLocalName()+"  ");
					
					if(pro.getDomain()!=null){
						
						p.print(pro.getDomain().getLocalName()+"  ");
						
						
					}
					else  {
						p.print("null"+"  ");
					}
					
//					for(Iterator pv=pro.listRange();pv.hasNext();){
//					OntClass	pn= (OntClass) pv.next();
//					if(pn.getLocalName()==null){ p.print("nullb  ");}
//					else {p.print(pn.getLocalName()+"  ");}
						
//					System.out.println(pn.getLocalName());
		//			}
					
				}
				
				p.println();
			}
		}
		
		
		
//		for(Iterator i=ont.listIndividuals();i.hasNext();){
//			k++;
//			Individual ind=(Individual) i.next();
//			p.print(ind.getLocalName()+"  ");
//			for(Iterator indp=ind.listProperties();indp.hasNext();){
//				 
//				Statement pro=(Statement) indp.next();
//				System.out.println();
//				
//			}
			
			
			
			
		//}
			
		
		
		
		
		
		
	}
	
	
	
	
	/**
	 * 将概念写到condata文件中
	 * @param filename
	 * @param ont
	 */
	public void writeOntInstancetoDatafile(String filename,OntModel ont){
		
		int j=0,k=0;
		FileOutputStream out = null;
		
		
		try {
					out=new FileOutputStream(filename);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		PrintStream p=new PrintStream(out);
		
		
	for(Iterator i=ont.listIndividuals();i.hasNext();){
			
			k++;
			
			
			Individual ind=(Individual) i.next();
			if(!ind.isAnon()){
//				p.println(ind.getLocalName()+"   "+(1100000000+k));
//				System.out.println("SUUUU"+ind.getOntClass());
				System.out.println(ind);
				p.println(k+"  "+ind);
				ArrayList<String> cid=cra.getTermId(ind.toString(), "cid", colConTerm);//对于owl文件来说，这里cid不妨只按只有一个值来看，即一个术语对应一个概念号。
				ArrayList<String> prostr=new ArrayList<String>();
				for(StmtIterator id=ind.listProperties();id.hasNext();){
					Property pro=id.next().getPredicate();
					
					
//					System.out.println(pro.getLocalName());
					if(!prostr.contains(pro.getLocalName())){
					for(NodeIterator npro=ind.listPropertyValues(pro);npro.hasNext();){
						
						RDFNode proval=npro.next();
						
						ArrayList<String> cid1=cra.getTermId(pro.getLocalName().toString(), "rid", colRelTerm);//对于owl文件来说，这里cid不妨只按只有一个值来看，即一个术语对应一个概念号。
						if(!cid1.isEmpty()){
							
						ArrayList<String> cid2=cra.getTermId(proval.toString(), "cid", colConTerm);//对于owl文件来说，这里cid不妨只按只有一个值来看，即一个术语对应一个概念号。
						if(!cid2.isEmpty())
						cra.addConceptRel(cid.get(0), cid1.get(0), cid2.get(0), colConData);
						else cra.addConceptRel(cid.get(0), cid1.get(0), proval.toString(), colConData);
						
						p.println("       "+pro.getLocalName()+"   :   "+proval);
						
						System.out.println(pro.getLocalName()+"   :   "+proval);
						}
						
					}
					}
					
					prostr.add(pro.getLocalName());
					System.out.println("pro :"+prostr);
					
				}
				
			}
			
			
			
		}
		
		
		
		
		
		
	}
	
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub
		OwlImport owlin=new OwlImport();
		String conTermFile="e:/ontodata/conterm.txt";
		String conDataFile="e:/ontodata/condata.txt";
		String relTermFile="e:/ontodata/relterm.txt";
		String relDataFile="e:/ontodata/reldata.txt";
		
		OntModel ont=ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	
		System. out.println("Start read" );
    
		    try{
		          ont.read(new FileInputStream("e:/ontodata/test.owl" ),"" ); //read owl file
		   } catch(IOException ioe){
		         System. err.println(ioe.toString());
		   }
		   System. out.println("End read" );
	
//		owlin.writeOntClasstoTermfile(conTermFile, ont); 
		   
//		owlin.writeOntPropertytoTermfile(relTermFile, ont);
		   
//		owlin.writeOntPropertytoDatafile(relDataFile, ont);
//		owlin.writeOntClasstoDatafile(conDataFile, ont);
//   	    owlin.writeOntInstancetoDatafile(conDataFile, ont);
		
		  
	//程序基本没有问题。关键是从头再运行一遍。
		
		
		
		
	}

}
