package com.onto.front.action;

import java.util.List;

public class TreeModel {

	//private String root;
	private String text;
	//private String cls;
	private List<TreeModel> children;
	private String id;
	//private boolean expanflag;
	private boolean leaf;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/*public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}*/
	public List<TreeModel> getChildren() {
		return children;
	}
	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}
	/*public boolean isExpanflag() {
		return expanflag;
	}
	public void setExpanflag(boolean expanflag) {
		this.expanflag = expanflag;
	}*/
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	
	
	
	
}
