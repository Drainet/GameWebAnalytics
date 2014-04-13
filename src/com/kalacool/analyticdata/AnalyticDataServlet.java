package com.kalacool.analyticdata;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;

public class AnalyticDataServlet extends HttpServlet{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 469453243451966141L;
	private Cursor cursor;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");

		String entityType[] = req.getParameterValues("Entity");
		Query q;
		if(entityType!=null&&entityType[0]!=null){
			q = new Query(entityType[0]);
		}else{
			q = new Query("default");
		}
		
		String cursorStr = req.getParameter("cursor");
		
		
		if(cursorStr!=null)
			cursor = Cursor.fromWebSafeString(cursorStr);
		else
			cursor = null;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		PreparedQuery pq = datastore.prepare(q);
		FetchOptions fetchOptions;
		if(cursor!=null)
			fetchOptions = FetchOptions.Builder.withLimit(10).startCursor(cursor);
		else
			fetchOptions = FetchOptions.Builder.withLimit(10);
		
		
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		
		if(results!=null)
		{
			JSONArray JSONAry = new JSONArray();
			Gson gson = new Gson();
			for (Entity result : results) {
				
				
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject = new JSONObject(gson.toJson(result));
					JSONAry.put(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("data",JSONAry);
				JSONObject cursorJson = new JSONObject();
				cursorJson.put("cursor", results.getCursor().toWebSafeString());
				
				
				jsonObject.put("info", cursorJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			if(jsonObject!=null)
				resp.getWriter().println(jsonObject.toString());
		}
		else
			resp.getWriter().println("query is null");
		
	}

}
