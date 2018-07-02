package com.swamy.samplejms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swamy.samplejms.model.Sale;
import com.swamy.samplejms.model.SalesWrapper;
import com.swamy.samplejms.publishers.SalesPublisher;

@RestController
public class SalesController {
	
	@Autowired
	private SalesPublisher salesPublisher;
	
	@PostMapping("/postsales")
	public ResponseEntity<String> postSales(@RequestBody SalesWrapper wrapper) {
		List<Sale> sales = wrapper.getSales();
		ResponseEntity<String> response = null;
		try {
			salesPublisher.publishSales(sales);
			response = new ResponseEntity<String>("Sales Published Successfully", HttpStatus.OK);
		}catch(Exception e) {
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
}
