package com.swamy.samplejms.listeners;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.swamy.samplejms.config.JmsConfig;
import com.swamy.samplejms.model.Sale;
import com.swamy.samplejms.services.AdjustmentService;
import com.swamy.samplejms.services.ReportGenerationService;

@Component
public class SalesListener {

	private static Logger log = LoggerFactory.getLogger(SalesListener.class);
	
	private List<Sale> sales = new ArrayList<Sale>();
	
	private int totalMessageCount;
	
	private List<Sale> totalSales = new ArrayList<Sale>();
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ReportGenerationService reportService;
	
	@Autowired
	private AdjustmentService adjustmentService;
	
	@Value("${report.sales.count}")
	private Integer SALES_REPORT_COUNT;
	
	@Value("${report.adjustment.count}")
	private Integer ADJUSTMENT_REPORT_COUNT;
	
	@JmsListener(destination = JmsConfig.SALES_QUEUE)
	public void consume(Sale sale) {
		sales.add(sale);
		totalMessageCount++;
		if(sale.isAdjustment()) {
			adjustmentService.adjustAllSales(sale, totalSales, sales);
		}
		if(sales.size() == SALES_REPORT_COUNT) {
			reportService.generateSalesReport(sales);
			prepareTotalSalesList(sales);
		}
		if(totalMessageCount % ADJUSTMENT_REPORT_COUNT == 0) {
			pauseListener();
			getReportForAdjustments(totalSales);
		}
		
	}
	
	private void pauseListener() {
		Runnable pauseRunnable = () -> {
			try {
				synchronized(jmsTemplate.getConnectionFactory()) {
					log.info("--------------------------------------------------------------");
					log.info("Suspending Message listener until Adjustment Report Generated.");
					log.info("--------------------------------------------------------------");
					jmsTemplate.getConnectionFactory().wait();
				}
			} catch (InterruptedException e) {
				log.error("There is some issue in suspending the listener : "+e.getMessage());
			}
		};
		Thread pauseThread = new Thread(pauseRunnable);
		pauseThread.start();
	}
	
	private void getReportForAdjustments(List<Sale> totalSales) {
		Runnable reportRunnable = ()->{
			synchronized(jmsTemplate.getConnectionFactory()) {
				reportService.generateAdjustmentsReport(totalSales.subList(totalSales.size()-ADJUSTMENT_REPORT_COUNT, totalSales.size()));
				reportService.generateSalesReportPostAdjust(totalSales);
				jmsTemplate.getConnectionFactory().notifyAll();
			}
		};
		Thread reportThread = new Thread(reportRunnable);
		reportThread.start();
	}
	
	/*private Runnable reportRunnable = ()->{
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			reportService.generateAdjustmentsReport(totalSales.subList(totalSales.size()-50, totalSales.size()));
			reportService.generateSalesReportPostAdjust(totalSales);
	};*/
	

	private void prepareTotalSalesList(List<Sale> sales) {
		totalSales.addAll(sales);
		log.info("Total no of recorded messages : "+totalSales.size() );
		sales.clear();
	}
}
