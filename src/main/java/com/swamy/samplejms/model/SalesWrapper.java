package com.swamy.samplejms.model;

import java.io.Serializable;
import java.util.List;

public class SalesWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Sale> sales;
	
	public SalesWrapper() {
		
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}
	
	
}
