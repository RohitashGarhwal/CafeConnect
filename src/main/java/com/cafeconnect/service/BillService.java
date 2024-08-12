package com.cafeconnect.service;

import com.cafeconnect.Model.GenerateReportRequest;
import com.cafeconnect.Model.GetPdfRequest;
import com.cafeconnect.POJO.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BillService {
    ResponseEntity<String> generateReport(GenerateReportRequest generateReportRequest);

    ResponseEntity<List<Bill>> getBills();

    ResponseEntity<byte[]> getPdf(GetPdfRequest getPdfRequest);

    ResponseEntity<String> deleteBill(Integer id);
}
