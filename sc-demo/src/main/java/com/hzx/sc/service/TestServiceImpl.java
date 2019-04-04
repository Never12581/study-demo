package com.hzx.sc.service;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 14:12 2019/4/2
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private LocalClient localClient;

    @Override
    public String test(String hhh, String ooo) {
        return localClient.test(hhh, ooo);
    }

    public void cityAdd() throws IOException {
        String path = "/Users/huangbocai/Desktop/参数.xlsx";
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        for (int rowNum = 0; rowNum < xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                // 城市名
                StringBuilder sb = new StringBuilder();
                sb.append(xssfRow.getCell(0).getStringCellValue()).append("|")
                        .append(xssfRow.getCell(1).getStringCellValue()).append("|")
                        .append(xssfRow.getCell(2).getStringCellValue()).append("|")
                        .append(xssfRow.getCell(3).getStringCellValue());
                System.out.println(sb.toString());

            }
        }

    }

    /**
     * @Author: bocai.huang
     * @Descripition:
     * @Date: Create in 17:30 2019/3/21
     */
    @FeignClient(value = "${ali.coupon.server.name}", url = "${ali.coupon.server.url}")
    private interface LocalClient {

        @GetMapping(value = "test")
        String test(@RequestHeader(value = "hhh") String hhh, @RequestParam(value = "ooo") String ooo);

    }
}
