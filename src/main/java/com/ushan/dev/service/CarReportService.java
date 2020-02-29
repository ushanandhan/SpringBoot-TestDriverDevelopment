package com.ushan.dev.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushan.dev.model.Car;
import com.ushan.dev.repository.CarRepository;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class CarReportService {

	@Autowired
	CarRepository carRepository;
	
	List<Car> carList = Arrays.asList(
				new Car("Nissan Kicks", "Compact SUV"),
				new Car("Nissan Micra", "Hashback"),
				new Car("Renault Duster", "Compact SUV")
			);
	
	public String generateReport() {
		try {
			
			List<Car> carList = carRepository.findAll();
			
			String reportPath = "src/main/resources/reports";

			// Compile the Jasper report from .jrxml to .japser
			JasperReport jasperReport = JasperCompileManager.compileReport("src//main//resources//reports//Car_Detail_Report.jrxml");

			// Get your data source
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(carList);

			// Add parameters
			Map<String, Object> parameters = new HashMap<>();

			parameters.put("createdBy", "Websparrow.org");

			// Fill the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					jrBeanCollectionDataSource);

			// Export the report to a PDF file
			JasperExportManager.exportReportToPdfFile(jasperPrint, "src//main//resources//reports//Car-Detail-Report.pdf");

			System.out.println("Done");

			return "Report successfully generated @path= " + reportPath;

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
