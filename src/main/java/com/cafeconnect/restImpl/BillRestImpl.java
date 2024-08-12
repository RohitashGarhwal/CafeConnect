package com.cafeconnect.restImpl;

import com.cafeconnect.Model.GenerateReportRequest;
import com.cafeconnect.Model.GetPdfRequest;
import com.cafeconnect.POJO.Bill;
import com.cafeconnect.constents.CafeConstants;
import com.cafeconnect.rest.BillRest;
import com.cafeconnect.service.BillService;
import com.cafeconnect.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BillRestImpl implements BillRest {

    @Autowired
    BillService billService;


    @Override
    public ResponseEntity<String> generateReport(GenerateReportRequest generateReportRequest) {
        try {
            return billService.generateReport(generateReportRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            return billService.getBills();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(GetPdfRequest getPdfRequest) {
        try {
            return billService.getPdf(getPdfRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            return billService.deleteBill(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
