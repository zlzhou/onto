package com.onto.excel;

import java.io.File;
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;  
import java.util.List;

import javax.swing.text.html.StyleSheet;

import org.apache.commons.lang.math.Range;
import org.apache.poi.hssf.usermodel.HSSFCell;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Workbook;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.onto.mongo.control.ConRelAdmin;

public class ExcelRead {
	
	
	public static ConRelAdmin cra;
	DB db;
	static DBCollection colConData;
	static DBCollection colConTerm;
	static DBCollection colRelData;
	static DBCollection colRelTerm;
	static DBCollection colId;
	
	/*private Map dataMap;
	private TreeModel user;
	private String menuString;*/
	
	//private Map m;
	
	public ExcelRead() throws UnknownHostException, MongoException{
		
		cra=new ConRelAdmin();
		db=cra.creatOntoDB("ontopeople");
		colConData=cra.creatTable(db, "ConData");
		colConTerm=cra.creatTable(db, "ConTerm");
		colRelData=cra.creatTable(db, "RelData");
		colRelTerm=cra.creatTable(db, "RelTerm");
		colId=cra.creatTable(db, "idPool");
		
		
		
	}
	
	
	
	
	
	

	public void readXml(String fileName){
		boolean isE2007 = false;	//判断是否是excel2007格式
		if(fileName.endsWith("xlsx"))
			isE2007 = true;
		try {
			InputStream input = new FileInputStream(fileName);	//建立输入流
			Workbook wb  = null;
			//根据文件格式(2003或者2007)来初始化
			if(isE2007)
				wb = new XSSFWorkbook(input);
			else
				wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);		//获得第一个表单
			Iterator<Row> rows = sheet.rowIterator();	//获得第一个表单的迭代器
			
			int rownum=sheet.getLastRowNum();
			for(int i=0;i<6542;i++){
				Row onerow=sheet.getRow(i);
				
				String name=onerow.getCell(0).getStringCellValue().trim();
				if(!name.isEmpty()) {
				String insid=cra.addConTerm("insid",name, true, colConTerm, colConData, colId);
				
				String sex=onerow.getCell(1).getStringCellValue().trim();// sex
				if(sex.equals("男")) cra.addConceptRel(insid, "2000000005", "1100000001", colConData);
				else if(sex.equals("女"))  cra.addConceptRel(insid, "2000000005", "1100000002", colConData);
				
				String birth=onerow.getCell(2).getStringCellValue().trim(); //birthday
				if(birth.isEmpty()) {cra.addConceptRel(insid, "2000000007", "0000000000", colConData); birth="未知年";}
				else{
				String birid=cra.addConTerm("insid", birth, false, colConTerm, colConData, colId);
				cra.addConceptRel(insid, "2000000007", birid, colConData);
				cra.addConceptRel(birid,"2100000003", "1000000002", colConData);
				}
				
				String place=onerow.getCell(3).getStringCellValue().trim();// place
				if(place.isEmpty()) {cra.addConceptRel(insid, "2000000009", "0000000000", colConData);place=" 未知";}
				else{
				String placeid=cra.addConTerm("insid", place, false, colConTerm, colConData, colId);
				cra.addConceptRel(insid, "2000000009", placeid, colConData);
				cra.addConceptRel(placeid,"2100000003", "1000000003", colConData);
				}
				
				String career=onerow.getCell(4).getStringCellValue().trim();//career
				ArrayList<String> idlist=cra.getTermId(career, "cid", colConTerm);
				//这里要加的信息是：如果在分类中找不到对应的分类，则给定其为“其他”
				if (!idlist.isEmpty())
				cra.addConceptRel(insid, "2100000003", idlist.get(0), colConData);
				else cra.addConceptRel(insid, "2100000003", "1000000622", colConData);
				
				
				
				
				String temp=" 昵称有: ";
				for(int j=6;j<onerow.getLastCellNum();j++){
				//	for(int j=6;j<11;j++){
					
					String csyn=onerow.getCell(j).getStringCellValue().trim();
					
					if (!csyn.isEmpty()){
						temp=temp+csyn+", ";
					cra.addConceptValueofRel(insid, "csyn", csyn, colConData);
					cra.addTermDocument(csyn, colConTerm);
					cra.addConTermID(csyn, insid, colConTerm);
					
					}
					//这个地方要加入一个方法，即id不变，术语变。
					
				}
				
				String gloss=name+": "+sex+", 生于 "+birth+", 籍贯位于"+place+", 是一位"+career+"("+onerow.getCell(5).getStringCellValue()+")。"+temp;// gloss
				cra.addConceptValueofRel(insid, "gloss", gloss, colConData);
				
				System.out.println(gloss);
				System.out.println("working "+ i+" of"+rownum);
				
				}
				
				System.out.println("finish!!!");
			}
//			
			
			
//			for(int i=1;i<=675;i++){
//				Row onerow=sheet.getRow(i);
//				if (!onerow.getCell(0).getStringCellValue().isEmpty()){
//				for(int j=0;j<onerow.getLastCellNum();j++){
//					String name=onerow.getCell(j).getStringCellValue();
//					if(name.isEmpty()) {System.out.println("i am null");}
//					System.out.print(" "+name);
//				}
//				
//				System.out.println("cols  "+onerow.getLastCellNum());
//				
//			}
//			}
//			
//			System.out.println("rows of sheet:  "+sheet.getLastRowNum());
//			Row row1=sheet.getRow(0);
//			
//			
//			System.out.println("collumn of row:  "+row1.getLastCellNum());
			
			
//			while (rows.hasNext()) {
//				Row row = rows.next();	//获得行数据
//				System.out.println("Row #" + row.getRowNum());	//获得行号从0开始
//				Iterator<Cell> cells = row.cellIterator();	//获得第一行的迭代器
//				while (cells.hasNext()) {
//					Cell cell = cells.next();
//					System.out.println("Cell #" + cell.getColumnIndex());
//					
//					switch (cell.getCellType()) {	//根据cell中的类型来输出数据
//					case HSSFCell.CELL_TYPE_NUMERIC:
//						System.out.println(cell.getNumericCellValue());
//						break;
//					case HSSFCell.CELL_TYPE_STRING:
//						System.out.println(cell.getStringCellValue());
//						break;
//					case HSSFCell.CELL_TYPE_BOOLEAN:
//						System.out.println(cell.getBooleanCellValue());
//						break;
//					case HSSFCell.CELL_TYPE_FORMULA:
//						System.out.println(cell.getCellFormula());
//						break;
//					default:
//						System.out.println("unsuported sell type");
//					break;
//					}
//				}
//			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
/**
 * 用来将文章中按标题做的类读入到本体库当中。	
 * @param path
 * @return
 * @throws IOException
 */
	public List<String> getWordTitles2003(String path) throws IOException{


		File file = new File(path);


		String filename = file.getName();


		filename = filename.substring(0, filename.lastIndexOf("."));


		InputStream is = new FileInputStream(path);


		HWPFDocument doc = new HWPFDocument(is); 


		org.apache.poi.hwpf.usermodel.Range r = doc.getRange();



	
		ArrayList<String> fcidlist=new ArrayList<String>();
		for(int id=0;id<20;id++)
			fcidlist.add(" ");
		fcidlist.set(0,"1000000000");
		

		List<String> list = new ArrayList<String>();


		for (int i = 0; i < r.numParagraphs(); i++) {
		//for (int i = 0; i < 471; i++) {

		 Paragraph p = r.getParagraph(i);

		 // check if style index is greater than total number of styles

		 int numStyles =doc.getStyleSheet().numStyles();

		 int styleIndex = p.getStyleIndex();

		 if (numStyles > styleIndex) {

		 org.apache.poi.hwpf.model.StyleSheet style_sheet = doc.getStyleSheet();

		 StyleDescription style = style_sheet.getStyleDescription(styleIndex);

		 String styleName = style.getName();

		 if (styleName!=null&&styleName.contains("标题")) {
			 String temp=styleName.substring(3);
			 Integer bt=Integer.valueOf(temp);
			 String scid=new String();
			 String newterm=p.text().replaceAll("\r","").trim();
			 scid=cra.addConTerm("conid", newterm, true, colConTerm, colConData, colId);
			 fcidlist.set(bt,scid);
			 cra.addConceptRel(fcidlist.get(bt-1), "2100000002", scid, colConData);
			 cra.addConceptRel(scid, "2000000005", "1000000001", colConData);
			 cra.addConceptRel(scid, "2000000007", "1000000002", colConData);
			 cra.addConceptRel(scid, "2000000009", "1000000003", colConData);
//			 
			 
//			 if(bt>min){
//				 
//				 
//				 String newterm=p.text().replaceAll("\r","");
//				 
//				 scid=cra.addConTerm("conid", newterm, true, colConTerm, colConData, colId);
//				 cra.addConceptRel(fcid, "2100000002", scid, colConData);
//				 
//				 ffcid=fcid;
//				 fcid=scid;
//				 min=bt;
//			 }else if(bt==min){
//				 String newterm=p.text().replaceAll("\r","");
//				 scid=cra.addConTerm("con",newterm, true, colConTerm, colConData, colId);
//				 cra.addConceptRel(ffcid, "2100000001", scid, colConData);
//				 
//			 }else if(bt<min){
//				 
//			 }
			 
			 
			 
			 
		// write style name and associated text


		  System.out.println(styleName +"->"+ p.text());
		//

		System.out.println(p.text()+i);


		String text = p.text();


		list.add(text);

		


		}

		}


		}
		return list;
	}
	
	
	
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub
		ExcelRead er=new ExcelRead();
		er.readXml("e:/excel/athlete1028.xls"); 
		//System.out.println("-------------");
		//readXml("d:/test2.xls");  
//		try {
//			er.getWordTitles2003("e:/excel/1024.doc");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//System.out.println(Integer.valueOf("标题1".substring(2)));
		
	}

}

