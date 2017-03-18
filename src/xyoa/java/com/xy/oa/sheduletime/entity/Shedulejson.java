package com.xy.oa.sheduletime.entity;

import java.util.ArrayList;
import java.util.List;

public class Shedulejson {

	private int total=0;
	//private List<OneWeekShedule> oneweeks=new ArrayList<>();
	private OneWeekShedule[] rows;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	/*public List<OneWeekShedule> getOneweeks() {
		return oneweeks;
	}
	public void setOneweeks(List<OneWeekShedule> oneweeks) {
		this.oneweeks = oneweeks;
	}*/
	public OneWeekShedule[] getRows() {
		return rows;
	}
	public void setRows(OneWeekShedule[] oneweeks) {
		this.rows = oneweeks;
	}
	
	
}
