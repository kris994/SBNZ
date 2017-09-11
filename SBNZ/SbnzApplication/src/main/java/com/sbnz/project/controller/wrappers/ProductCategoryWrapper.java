package com.sbnz.project.controller.wrappers;

public class ProductCategoryWrapper {
	
	private String name;
	private Integer parent;
	private Float perc;
	private Integer id;
	
	public void setPerc(Float perc) {
		this.perc = perc;
	}
	public String getName() {
		return name;
	}
	public Float getPerc() {
		return perc;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "ProductCategoryWrapper [name=" + name + ", parent=" + parent + ", perc=" + perc + ", id=" + id + "]";
	}
		
}
