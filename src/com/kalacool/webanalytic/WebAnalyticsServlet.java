package com.kalacool.webanalytic;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;





public class WebAnalyticsServlet extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4567486857635269829L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity entity;
		String entityType[] = req.getParameterValues("Entity");
		
		if(entityType!=null&&entityType[0]!=null){
			entity = new Entity(entityType[0]);
		}else{
			entity = new Entity("default");
		}
		

		@SuppressWarnings("rawtypes")
		Enumeration paramNames = req.getParameterNames();
		while(paramNames.hasMoreElements()){
			String paramName = (String)paramNames.nextElement();
			if(!paramName.equals("Entity")){
				String[] paramValues = req.getParameterValues(paramName);
				if (paramValues.length == 1)
				{
					String paramValue = paramValues[0];
					if (paramValue.length() == 0)
						resp.getWriter().println("No Value");
					else{
						if(paramValue.indexOf("[str]",paramValue.length()-5)!=-1){
							String str = paramValue.substring(0,paramValue.length()-5);
							entity.setProperty(paramName,str);
						}
						else{
							try{
								entity.setProperty(paramName,Integer.valueOf(paramValue));
							}catch(NumberFormatException e){
								System.out.println(e.toString());
							}

						}
					}
				}
			}
		}
		datastore.put(entity);


	}
}
