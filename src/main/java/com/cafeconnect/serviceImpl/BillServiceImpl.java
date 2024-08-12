package com.cafeconnect.serviceImpl;

import com.cafeconnect.JWT.JwtFilter;
import com.cafeconnect.Model.GenerateReportRequest;
import com.cafeconnect.Model.GetPdfRequest;
import com.cafeconnect.POJO.Bill;
import com.cafeconnect.constents.CafeConstants;
import com.cafeconnect.dao.BillDao;
import com.cafeconnect.service.BillService;
import com.cafeconnect.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<String> generateReport(GenerateReportRequest generateReportRequest) {
        try {
            String fileName;
            if (generateReportRequest.getIsGenerate() != null && !generateReportRequest.getIsGenerate()) {
                fileName = generateReportRequest.getUuid();
            } else {
                fileName = CafeUtils.getUUID();
                generateReportRequest.setUuid(fileName);
                insertBill(generateReportRequest);
            }

            String data = "Name: " + generateReportRequest.getName() + "\n" + "Contact Number: " + generateReportRequest.getContactNumber() +
                    "\n" + "Email: " + generateReportRequest.getEmail() + "\n" + "Payment Method: " + generateReportRequest.getPaymentMethod();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));

            document.open();
            setRectangleInPdf(document);

            Paragraph chunk = new Paragraph("Cafe Connect Bill", getFont("Header"));
            chunk.setAlignment(Element.ALIGN_CENTER);
            document.add(chunk);

            Paragraph paragraph = new Paragraph(data + "\n \n", getFont("data"));
            document.add(paragraph);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            addTableHeader(table);

            JSONArray jsonArray = CafeUtils.getJsonArrayFromString(generateReportRequest.getProductDetails());
            for (int i = 0; i < jsonArray.length(); i++) {
                addRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
            }
            document.add(table);

            Paragraph footer = new Paragraph("Total : " + generateReportRequest.getTotal() + "\n"
                    + "Thank you for Visiting. Please visit Again!!", getFont("Data"));
            document.add(footer);
            document.close();
            return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.GREEN);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }


    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.RED);
        rect.setBorderWidth(2);
        document.add(rect);
    }

    private void insertBill(GenerateReportRequest generateReportRequest) {
        try {
            Bill bill = new Bill();
            bill.setUuid(generateReportRequest.getUuid());
            bill.setName(generateReportRequest.getName());
            bill.setEmail(generateReportRequest.getEmail());
            bill.setContactNumber(generateReportRequest.getContactNumber());
            bill.setPaymentMethod(generateReportRequest.getPaymentMethod());
            bill.setTotal(generateReportRequest.getTotal());
            bill.setProductDetails(generateReportRequest.getProductDetails());
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jwtFilter.isAdmin()) {
            list = billDao.getAllBills();
        } else {
            list = billDao.getBillByUserName(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(GetPdfRequest getPdfRequest) {
        log.info("Inside getPdf : getPdfRequest {}", getPdfRequest);
        try {
            byte[] byteArray = new byte[0];
            if (getPdfRequest.getUuid() == null)
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            String filePath = CafeConstants.STORE_LOCATION + "\\" + (String) getPdfRequest.getUuid() + ".pdf";
            if (CafeUtils.isFileExist(filePath)) {
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            } else {
                GenerateReportRequest generateReportRequest = new GenerateReportRequest();
                generateReportRequest.setUuid(getPdfRequest.getUuid());
                generateReportRequest.setName(getPdfRequest.getName());
                generateReportRequest.setEmail(getPdfRequest.getEmail());
                generateReportRequest.setContactNumber(getPdfRequest.getContactNumber());
                generateReportRequest.setPaymentMethod(getPdfRequest.getPaymentMethod());
                generateReportRequest.setProductDetails(getPdfRequest.getProductDetails());
                generateReportRequest.setTotal(getPdfRequest.getTotal());
                generateReportRequest.setIsGenerate(false);
                generateReport(generateReportRequest);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filePath) throws Exception{
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }


    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional optional = billDao.findById(id);
            if (!optional.isEmpty()) {
                billDao.deleteById(id);
                return CafeUtils.getResponseEntity("Bill Deleted Successfully", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Bill id doesn't exist", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
