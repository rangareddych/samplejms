package com.swamy.samplejms.services;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.swamy.samplejms.model.Sale;

@Service
public class ReportGenerationService {
	
	private static Logger log = LoggerFactory.getLogger(ReportGenerationService.class);
	
	public void generateSalesReport(List<Sale> sales) {
		log.info("***************************************************");
		log.info("              Last 10 Messages Report              ");
		log.info("***************************************************");
		
		Map<String, Integer> counting = sales.stream()
											 .filter(sale -> !sale.isAdjustment())
											 .collect(Collectors.groupingBy(Sale::getType, Collectors.summingInt(Sale::getQuantity)));
		
		Map<String, Long> sum = sales.stream()
									 .filter(sale -> !sale.isAdjustment())
									 .collect(Collectors.groupingBy(Sale::getType, Collectors.summingLong(Sale::getTotal)));
		
		if(counting.size() > 0) {
			log.info("  Type   " +"No Of Sales"+"  Total Value  ");
			log.info("  ====   " +"==========="+"  ===========  ");
			
			counting.forEach((k, v) -> {
								log.info("  "+k+"  "+"   "+v+"                    "+ sum.get(k));
							});
			log.info("---------------------------------------------------");
		}
	}
	
	public void generateAdjustmentsReport(List<Sale> sales) {
		log.info("***************************************************");
		log.info("       Adjustments Report After 50 messages        ");
		log.info("***************************************************");
		
		if(sales.stream().filter(sale -> sale.isAdjustment()).count() == 0)
		{
			log.info(" No Adjustments Received in the last 50 messages!!");
		}else {
			log.info("   Type     Adjustment Type     Adjust Value");
			log.info("  ======   =================   ===============");	
			
			sales.stream().filter(sale -> sale.isAdjustment()).forEach(sale ->{
				log.info("   "+sale.getType()+"          "+sale.getAdjustType()+"                  "+sale.getAdjustValue());
			});
		}
		
		log.info("---------------------------------------------------");
	}
	
	public void generateSalesReportPostAdjust(List<Sale> totalSales) {
		log.info("***************************************************");
		log.info("        Post Adjustment Sales Prices Report        ");
		log.info("***************************************************");
		log.info("           Type              Price                          ");
		log.info("          ======           ========                         ");
		
		List<Sale> distinctSalesList = totalSales.stream()
				                                 .filter(distinctByKey(s -> s.getType()))
				                                 .collect(Collectors.toList());
		distinctSalesList.forEach(sale -> {
			log.info("           "+sale.getType()+"                "+sale.getPrice());
		});
		
		log.info("---------------------------------------------------");
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
	{
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
