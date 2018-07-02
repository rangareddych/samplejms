package com.swamy.samplejms.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.swamy.samplejms.model.Sale;

@Service
public class AdjustmentService {

	public void adjustAllSales(Sale sale, List<Sale> totalSales, List<Sale> sales) {
		String prodType = sale.getType();
		totalSales.stream()
				  .filter(s -> s.getType().equals(prodType))
				  .forEach((s)->applyAdjustment(s, sale));
		
		sales.stream()
			 .filter(s -> s.getType().equals(prodType))
			 .forEach((s)->applyAdjustment(s, sale));
	}
	
	private void applyAdjustment(Sale sale, Sale adjustmentSale) {
		if(sale.isAdjustment()) {
			return;
		}
		String adjustType = adjustmentSale.getAdjustType();
		BigDecimal price = sale.getPrice();
		switch(adjustType) {
			case "ADD":
				sale.setPrice(price.add(adjustmentSale.getAdjustValue()));
				break;
				
			case "SUB":
				sale.setPrice(price.subtract(adjustmentSale.getAdjustValue()));
				break;
				
			case "MUL":
				sale.setPrice(price.multiply(adjustmentSale.getAdjustValue()));
				break;
		}
	}
	
}
