package com.rsc.bhopal.utills;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtills {
	public static String convertToJSON(Object obj) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		return om.writeValueAsString(obj);
	}

	public static <T> T convertJSONToObject(String parload, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();		
		try {
			return mapper.readValue(parload,clazz);
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertDateToString(Date date, RSCDateFormat rscDateFormat){
		DateFormat dateFormat = new SimpleDateFormat(rscDateFormat.getValue());  
		return dateFormat.format(date); 
	}
}
