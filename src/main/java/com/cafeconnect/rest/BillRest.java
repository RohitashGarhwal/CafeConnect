package com.cafeconnect.rest;


import com.cafeconnect.Model.GenerateReportRequest;
import com.cafeconnect.Model.GetPdfRequest;
import com.cafeconnect.POJO.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/bill")
public interface BillRest {

    @PostMapping(path = "/generateReport")
    ResponseEntity<String> generateReport(@RequestBody GenerateReportRequest generateReportRequest);

    @GetMapping(path = "/getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping(path = "/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody GetPdfRequest getPdfRequest);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteBill(@PathVariable Integer id);
}
