package com.kalacool.devicedata.input;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DeviceDataInputServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4108966020866368152L;

	public void doGet(HttpServletRequest req, final HttpServletResponse resp) throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		String deviceID = req.getParameter("deviceID[str]");
		Key key = KeyFactory.createKey("Devices", deviceID);
		Entity entity;
		try{
			entity = datastore.get(key);
		}catch(EntityNotFoundException e){
			entity = new Entity("Devices", deviceID);
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<String> keyList = ((ArrayList<String>) entity.getProperty("keyList"));
		if(keyList==null)
			keyList = new ArrayList<String>();
		
		final Entity property = new Entity("Properties");
		@SuppressWarnings("unchecked")
		GetParametersProcessor getParameterProcessor = new GetParametersProcessor(req.getParameterMap());
		getParameterProcessor.setOnParametersProcessListener(new OnParametersProcessListener() {
			
			@Override
			public void run(String name, String[] values) {
				if(name.indexOf("[str]",name.length()-5)!=-1){
					String str = name.substring(0,name.length()-5);
					property.setProperty(str, values[0]);
				}else{
					try{
						property.setProperty(name,Integer.valueOf(values[0]));
					}catch(NumberFormatException e){
						System.out.println(e.toString());
					}
				}
			}
		});
		getParameterProcessor.run();
		datastore.put(property);
		keyList.add(KeyFactory.createKeyString(property.getKey().getKind(),property.getKey().getId()));
		entity.setProperty("keyList", keyList);
		datastore.put(entity);
	}
}
