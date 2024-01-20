package com.example.Slearning.Backend.Java.utils;

import com.example.Slearning.Backend.Java.domain.entities.Payment;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtils {

    public static String[] PAYMENT_OF_MENTOR_HEADERS = {
            "Ma thanh toan",
            "Khoa hoc",
            "Gia",
            "Hoa hong",
            "Con lai"
    };

    public static String SHEET_NAME_PAYMENT = "Thanh toán của người dạy";

    public static ByteArrayInputStream paymentsOfMentorToExcel(List<Payment> payments) throws IOException {
        // TODO: Tao workbookk
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // TODO: Tao sheet excel
            Sheet sheet = workbook.createSheet(SHEET_NAME_PAYMENT);

            // TODO: Tao header trong sheet
            Row row = sheet.createRow(0);
            for(int i = 0; i < PAYMENT_OF_MENTOR_HEADERS.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(PAYMENT_OF_MENTOR_HEADERS[i]);
            }

            // TODO: Tao du lieu trong sheet
            int rowIndex = 1;
            for(Payment payment : payments) {
                Row dataRow = sheet.createRow(rowIndex);
                rowIndex++;
                dataRow.createCell(0).setCellValue(payment.getId().toString());
                dataRow.createCell(1).setCellValue(payment.getCourse().getTitle());
                dataRow.createCell(2).setCellValue(payment.getAmount());
                dataRow.createCell(3).setCellValue("5%");
                dataRow.createCell(4).setCellValue(payment.getAmount() - payment.getAmount() * 0.05);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Fail export excel");
            return null;
        } finally {
            workbook.close();
            out.close();
        }
    }

    public static String[] PAYMENTS_IN_MONTH_OF_USER_HEADERS = {
            "Mã thanh toán",
            "Tên khóa học",
            "Học viên",
            "Ngày ghi danh",
            "Giá bán (VNĐ)",
            "Lợi nhuận sau khấu trừ (5%)"
    };

    public static ByteArrayInputStream paymentsInMonthOfUserToExcel(
            List<Payment> payments,
            double amountPaid,
            double profit,
            String sheetName
    ) throws IOException {
        // TODO: Tao workbookk
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // TODO: Tao sheet excel
            Sheet sheet = workbook.createSheet(sheetName);

            // TODO: Tao header trong sheet
            Row row = sheet.createRow(0);
            for(int i = 0; i < PAYMENTS_IN_MONTH_OF_USER_HEADERS.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(PAYMENTS_IN_MONTH_OF_USER_HEADERS[i]);
            }

            // TODO: Tao du lieu trong sheet
            int rowIndex = 1;
            for(Payment payment : payments) {
                Row dataRow = sheet.createRow(rowIndex);
                rowIndex++;
                dataRow.createCell(0).setCellValue(payment.getId().toString());
                dataRow.createCell(1).setCellValue(payment.getCourse().getTitle());
                dataRow.createCell(2).setCellValue(payment.getUser().getFullName());
                dataRow.createCell(3).setCellValue(AppUtils.formatLocalDateTimeToString(payment.getUpdateAt()));
                dataRow.createCell(4).setCellValue(payment.getAmount());
                dataRow.createCell(5).setCellValue(payment.getAmount() - (payment.getAmount() * 0.05));
            }

            Row principalRow = sheet.createRow(payments.size() + 1);
            principalRow.createCell(0).setCellValue("Doanh thu tổng cộng tháng này:");
            principalRow.createCell(1).setCellValue(amountPaid);

            Row profitRow = sheet.createRow(payments.size() + 2);
            profitRow.createCell(0).setCellValue("Lợi nhuận tổng cộng tháng này:");
            profitRow.createCell(1).setCellValue(profit);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Fail export excel");
            return null;
        } finally {
            workbook.close();
            out.close();
        }
    }
}
