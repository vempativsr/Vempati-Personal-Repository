package com.goeuro.beans;

import java.io.Serializable;

import org.json.JSONObject;

import pl.zientarski.SchemaMapper;

public class DirectRoute implements Serializable {
	
	private static final long serialVersionUID = 1112L;

	private int dep_sid;
	private int arr_sid;
	boolean direct_bus_route = false;
	
	public DirectRoute(int dep_sid, int arr_sid)
	{
		this.setDep_sid(dep_sid);
		this.setArr_sid(arr_sid);
	}
	
	public int getDep_sid() {
		return dep_sid;
	}

	public void setDep_sid(int dep_sid) {
		this.dep_sid = dep_sid;
	}

	public int getArr_sid() {
		return arr_sid;
	}

	public void setArr_sid(int arr_sid) {
		this.arr_sid = arr_sid;
	}

	public boolean isDirect_bus_route() {
		return direct_bus_route;
	}

	public void setDirect_bus_route(boolean direct_bus_route) {
		this.direct_bus_route = direct_bus_route;
	}
	
	public String toString()
	{
		return ("" + this.getDep_sid() + "_" + this.getArr_sid());
	}
	
	public String toJson() {

		return "{\n   \"dep_sid\": " 
					+ dep_sid + ",\n   "
					+ "\"arr_sid\": " 
					+ arr_sid + ",\n   "
					+ "\"direct_bus_route\": "
					+ direct_bus_route + "\n}";
		
		/*
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("dep_sid", this.getDep_sid());
		jsonObj.put("arr_sid", this.getArr_sid());
		jsonObj.put("direct_bus_route", "true");
		
		return jsonObj.toString();
		*/
	}
	
	public String toJson4() {		

		JSONObject jsonObject = new SchemaMapper().toJsonSchema4(this.getClass(), true);
		jsonObject.put("dep_sid", this.getDep_sid());
		jsonObject.put("arr_sid", this.getArr_sid());
		jsonObject.put("direct_bus_route", this.isDirect_bus_route());
		
		return jsonObject.toString(4);
	}
}

