package com.rsc.bhopal.utills;

public enum RSCDateFormat {
	YYYY_MM_DD("YYYY-MM-dd"), DD_MM_YYYY("dd-MM-YYYY");
	String value;
	RSCDateFormat(String value){
		this.value=value;
	}

	public String getValue(){
		return this.value;
	}
}
