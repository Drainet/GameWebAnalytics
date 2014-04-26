package com.kalacool.devicedata.input;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GetParametersProcessor {
	Map<String, String[]> params;
	OnParametersProcessListener onParametersProcessListener;
	public GetParametersProcessor( Map<String, String[]> params){
		this.params = params;
	}
	
	public void run(){
		if(onParametersProcessListener!=null){
		Set<Entry<String, String[]>> set = params.entrySet();
        Iterator<Entry<String, String[]>> it = set.iterator();
        while (it.hasNext()) {
        		Map.Entry<String, String[]> entry = 
                        (Entry<String, String[]>) it.next();
        		onParametersProcessListener.run(entry.getKey(),entry.getValue());
        	}
        }
	}
	
	public void setOnParametersProcessListener(OnParametersProcessListener onParametersProcessListener){
		this.onParametersProcessListener = onParametersProcessListener;
	}
}
