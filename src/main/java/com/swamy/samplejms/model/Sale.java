package com.swamy.samplejms.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Sale implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private BigDecimal price;
	private int quantity=1;
	
	private boolean adjustment;
	private BigDecimal adjustValue;
	private String adjustType;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Long getTotal() {
		if(this.isAdjustment() || this.getPrice() == null) {
			return null;
		}
		return this.price.multiply(BigDecimal.valueOf(this.quantity)).longValue();
	}
	
	public boolean isAdjustment() {
		return adjustment;
	}
	public void setAdjustment(boolean adjustment) {
		this.adjustment = adjustment;
	}
	public BigDecimal getAdjustValue() {
		return adjustValue;
	}
	public void setAdjustValue(BigDecimal adjustValue) {
		this.adjustValue = adjustValue;
	}
	public String getAdjustType() {
		return adjustType;
	}
	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}
	
}
