package com.onto.interf;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;

public interface Interf {
	
	public DB creatDb(String str) throws UnknownHostException, MongoException;
	public String getKeysValue(String keyStr,String[] valStr,String aimKey,DBCollection col);

}
