package com.example.jebeniis;

public class FilterItem {
	public String key;
	public int lev;

	public FilterItem(String key, int lev) {
		this.key = key;
		this.lev = lev;
	}

	public FilterItem() {}

	public String getKey() {
		return this.key;
	}
}
