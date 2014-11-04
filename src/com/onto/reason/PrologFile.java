package com.onto.reason;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.onto.front.action.TermModel;
import com.onto.mongo.control.ConRelAdmin;

public class PrologFile {
	
	private static String path = "e:/excel/";
    private static String filenameTemp;

    public ConRelAdmin cra;
	DB db;
	DBCollection colConData;
	DBCollection colConTerm;
	DBCollection colRelData;
	DBCollection colRelTerm;
	DBCollection colId;
    
    
public PrologFile() throws UnknownHostException, MongoException{
		
		cra=new ConRelAdmin();
		db=cra.creatOntoDB("ontopeople");
		colConData=cra.creatTable(db, "ConData");
		colConTerm=cra.creatTable(db, "ConTerm");
		colRelData=cra.creatTable(db, "RelData");
		colRelTerm=cra.creatTable(db, "RelTerm");
		colId=cra.creatTable(db, "idPool");
		
		
		
	}
    


//public void creatOntoPro(String name) throws IOException{
//	
//	creatTxtFile(name);
//	DBCursor cur=colConData.find();
//	
//	DBObject dbrow;
//	//int termNum=0;
//	while (cur.hasNext()){
//		//tm.setId(String.valueOf(termNum));
//		dbrow=cur.next();
//		
//		String proline=null;
//		@SuppressWarnings("unchecked")
//		ArrayList<String>  istr=(ArrayList<String>) dbrow.get("cid");
//		ArrayList<String> rellist=cra.getPartRelsofConcept(istr.get(0), colConData);
//		for(int reli=0;reli<rellist.size();reli++){
//		ArrayList<String> vallist=cra.getConceptofRel(istr.get(0), rellist.get(reli), colConData);
//		for(int vali=0;vali<vallist.size();vali++){
//			
//			proline="r"+rellist.get(reli)+"("+istr.get(0)+","+vallist.get(vali)+")"+".";
//			
//			writeTxtFile(proline);
//			
//		}
//		
//		}
//		
//		System.out.println(istr.get(0));
////		for (int i=0;i<istr.size();i++){
////			if (istr.get(i).charAt(1)=='0')	istr.remove(i);
////		}
//////		if(!istr.isEmpty()){
//////		ArrayList<String> tstr=(ArrayList<String>) dbrow.get("term");
//////		
////		
////		}
//		//termNum=termNum+1;
//		//System.out.println(tm.getId());
//		//System.out.println(termNum);
//	//System.out.println(cur.next().get("term"));
//	//System.out.println(cur.size(.;
//	}
//		
//		
//		
//	}
	
	
/**
 * ���������ķ���ֵ��ֻ�������������Ĵ𰸣��Ժ��ٿ��Ƿ��ض�������Ĵ𰸡�
 * @param name
 * @param rule
 * @return
 * @throws FileNotFoundException
 * @throws IOException
 * @throws InvalidTheoryException
 * @throws MalformedGoalException
 * @throws NoSolutionException
 * @throws NoMoreSolutionException
 */
	
public ArrayList<ArrayList<String>> reasonPro(String name,String rule) throws FileNotFoundException, IOException, InvalidTheoryException, MalformedGoalException, NoSolutionException, NoMoreSolutionException{
	
	ArrayList<ArrayList<String>> results=new ArrayList<ArrayList<String>>();
	Prolog p = new Prolog();
	Theory theory= new Theory(new FileInputStream(name));
	p.setTheory(theory);
	String resultFlag="no";	// Term goal = new Struct("p",new Int(1),new Var("X"));
	SolveInfo info = p.solve(rule);
    //Term solution = info.getSolution();
    
  //  System.out.println(info.getSetOfSolution());
	//System.out.println(solution);
    ArrayList<String> temp=new ArrayList<String>();
    ArrayList<String> temp0=new ArrayList<String>();
    ArrayList<String> temp1=new ArrayList<String>();
	
	while (info.isSuccess()){
		resultFlag="yes";
		 int varnum=info.getBindingVars().size();
	       for (int vi=0;vi<varnum;vi++)    {
	    	   
	    	   String result=info.getBindingVars().get(vi).getTerm().toString();
	    	  
	    	   if(vi==0) temp0.add(result);
	    	   if(vi==1) temp1.add(result);
	    	   
	    //	   System.out.println("solution: "+info.getBindingVars().get(vi).getTerm()); 
	    	   
	       }
        
        if (p.hasOpenAlternatives()){
                info=p.solveNext();
              //  System.out.println(info.getSolution());
        } else  break;
      
        
        
}
	temp.add(resultFlag);
	results.add(temp);
	results.add(temp0);results.add(temp1);
	System.out.println(results);
	return results;
	
}
	
	

    
    /**
     * �����ļ�
     * 
     * @throws IOException
     */
    public static boolean creatTxtFile(String name) throws IOException {
        boolean flag = false;
        filenameTemp = path + name + ".pl";
        File filename = new File(filenameTemp);
        if (!filename.exists()) {
            filename.createNewFile();
            flag = true;
        }
        return flag;
    }
	
    /**
     * ���ļ���׷����Ϣ
     * @param path
     * @param name
     * @param content
     */
    public void addContentToProFile(String path, String name, String content){
    	File dirFile = new File(path);

        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
    	
        try {
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(path + name, true));
			bw1.write(content);
			bw1.flush();
            bw1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	
    }
    
    
    /**
     * ����ļ��ǽ��������ݴ�ͷ��β�������ŵ�һ���ļ��С�
     * @param path
     * @param name
     * @param content
     * @throws IOException
     */
    public void  writeProFile(String path, String name, String content) throws IOException {
    	
    	File dirFile = new File(path);

        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

    	
        try {
            //new FileWriter(path + "t.txt", true)  �������true ���Բ�����ԭ��TXT�ļ����� ��д
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(path + name, true));
                      
            DBCursor cur=colConData.find();
        	DBObject dbrow;
        	//int termNum=0;
        	while (cur.hasNext()){
        		//tm.setId(String.valueOf(termNum));
        		dbrow=cur.next();
        		
        		String proline=null;
        		@SuppressWarnings("unchecked")
        		ArrayList<String>  istr=(ArrayList<String>) dbrow.get("cid");
        		ArrayList<String> rellist=cra.getPartRelsofConcept(istr.get(0), colConData);
        		for(int reli=0;reli<rellist.size();reli++){
        		ArrayList<String> vallist=cra.getConceptofRel(istr.get(0), rellist.get(reli), colConData);
        		for(int vali=0;vali<vallist.size();vali++){
        			
        			proline="r"+rellist.get(reli)+"("+istr.get(0)+","+vallist.get(vali)+")"+"."+"\r\n";
        			
        			bw1.write(proline);
        			
        		}
        		
        		}
        		
        		System.out.println(istr.get(0));
//        		
        	}
            
            
            
            bw1.flush();
            bw1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    	
    	
    }
    
    
	
    /**
     * д�ļ�
     * 
     * @param newStr
     *            ������
     * @throws IOException
     */
    public static boolean writeTxtFile(String newStr) throws IOException {
        // �ȶ�ȡԭ���ļ����ݣ�Ȼ�����д�����
        boolean flag = false;
        String filein = newStr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            // �ļ�·��
            File file = new File(filenameTemp);
            // ���ļ�����������
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // ������ļ�ԭ�е�����
            for (int j = 1; (temp = br.readLine()) != null; j++) {
                buf = buf.append(temp);
                // System.getProperty("line.separator")
                // ������֮��ķָ��� �൱�ڡ�\n��
                buf = buf.append(System.getProperty("line.separator"));
            }
            buf.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (IOException e1) {
            // TODO �Զ����� catch ��
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }
    
	
    
    /**
     * ��ȡ����
     */
    public void readData1() {
        try {
            FileReader read = new FileReader(filenameTemp);
            BufferedReader br = new BufferedReader(read);
            String row;
            while ((row = br.readLine()) != null) {
                System.out.println(row);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readDate() {
        // ����һ�������صĿ��ַ���
        String strs = "";
        try {
            FileReader read = new FileReader(new File(filenameTemp));
            StringBuffer sb = new StringBuffer();
            char ch[] = new char[1024];
            int d = read.read(ch);
            while (d != -1) {
                String str = new String(ch, 0, d);
                sb.append(str);
                d = read.read(ch);
            }
            System.out.print(sb.toString());
            String a = sb.toString().replaceAll("@@@@@", ",");
            strs = a.substring(0, a.length() - 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strs;
    }

    
    
    
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws IOException 
	 * @throws NoMoreSolutionException 
	 */
	public static void main(String[] args) throws MongoException, IOException, NoMoreSolutionException {
		// TODO Auto-generated method stub

		
		PrologFile myFile = new PrologFile();
//        try {
//            for (int i = 0; i < 5; i++) {
//                myFile.creatTxtFile("test");
//                myFile.writeTxtFile("��ʾ����׷�ӵ���Ϣ" + i);
//                String str = myFile.readDate();
//                System.out.println("*********\n" + str);
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
		
		myFile.writeProFile("e:/excel/", "people.pl", "dfadfadfa");
//		try {
//			myFile.reasonPro("e:/excel/people.pl", " r2100000001(1000000317,1000000000).");
//		} catch (InvalidTheoryException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedGoalException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSolutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		
 	System.out.println("ok!!!!");
//		
	}
	
	
	
	
	
	
	

}
