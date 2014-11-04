package com.onto.front.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class Datatest extends ActionSupport {
	
	//private String text;
	private String id;
	

	private String text;
	
	
	

	private Map dataMap;
	private TreeModel user;
	private String menuString;
	private List<TreeModel> menus;
	//private Map m;
	private Object o;
	
	
	
	
	public String add(){
		
		//System.out.println("ok1"+text);
		System.out.println(text);
		System.out.println(id);
		
		/*m=new HashMap();
		m.put("text","aa");
		m.put("id",23);
		m.put("cls","file");*/
		
		
		menus=new ArrayList<TreeModel>();
		//TreeModel benz=new TreeModel();
		//benz.setText("Benz");
		//benz.setCls("file");
		//benz.setLeaf(false);
		//benz.setId("10");
		//menus.add(benz);
		
		List<TreeModel> benzList=new ArrayList<TreeModel>();
		
		//benz.setChildren(benzList);
		
		TreeModel menu;
		menu=new TreeModel();
		
		
        menu.setText("S600");
       // menu.setCls("file");
        menu.setLeaf(true);
        menu.setId("11");
        menus.add(menu);
        menu = new TreeModel();
        menu.setText("SLK200");
        //menu.setCls("file");
        menu.setLeaf(false);
        menu.setId("12");
        menus.add(menu);
       
       /* TreeModel bmw = new TreeModel();
        bmw.setText("BMW");
        bmw.setCls("folder");
        bmw.setLeaf(false);
        bmw.setId("20");
        menus.add(bmw);
       
        List<TreeModel> bmwList = new ArrayList<TreeModel>();
        bmw.setChildren(bmwList);

        menu = new TreeModel();
        menu.setText("325i");
        menu.setCls("file");
        menu.setLeaf(true);
        menu.setId("21");
        bmwList.add(menu);
       
        menu = new TreeModel();
        menu.setText("X5");
        menu.setCls("file");
        menu.setLeaf(true);
        menu.setId("22");
        bmwList.add(menu);*/
        
       /// menuString=menus.toString();
        this.setMenus(menus);
        
        /*JSONArray jsonObject =JSONArray.fromObject(m);
        //JSONObject jsonObject =new JSONObject();
       // jsonObject.accumulate(SUCCESS, menus);
        
        //JSONArray json=JSONArray.fromObject(jsonObject);
        
        try {
            menuString = jsonObject.toString();
        } catch (Exception e) {
            //return menuString;
        	menuString="ss";
        }*/
		
		
        //String ms= new String();
       // menuString=m.toString();
      // menuString=menus.toString().concat("adf\"a");
		//o=menus;
        /*try {
			menuString=JSONUtil.serialize(menus);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println("ok2"+menus);
		
		/*dataMap.clear();
		
		user.setText("a");
		user.setId("001");
		user.setExpanflag(true);
		
		dataMap.put("data",user);
		dataMap.put("success", true);
		//text="qqq";
		System.out.print(dataMap);*/
		
		//text="afaf";
		//id="tt";
		
		//System.out.println("name="+text);
		//System.out.println("age="+id);
		return SUCCESS;
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
	
	
	
	
	public static void main(String[] args) {
		
		Datatest d=new Datatest();
		
		d.add();
	}

	/*public String getMenuString() {
		return menuString;
	}


	public void setMenuString(String menuString) {
		this.menuString = menuString;
	}
*/

	
}
