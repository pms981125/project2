package com.lec.project.shoppingmall.controller.Stock;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.service.Stock.StockService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.apache.poi.ss.usermodel.Workbook; // Workbook 클래스 추가
import org.apache.poi.ss.usermodel.Sheet;  // Sheet 클래스 추가
import org.apache.poi.ss.usermodel.Row;    // Row 클래스 추가
import org.apache.poi.ss.usermodel.Cell;   // Cell 클래스 추가
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // XSSFWorkbook 클래스 추가


@Controller
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // 재고 목록 페이지 + 상품 코드 검색 기능 추가
    @GetMapping("/stocks")
    public String viewStock(@RequestParam(value = "productCode", required = false) String productCode, Model model) {
        List<Product> products;

        // 상품 코드가 있으면 해당 상품만 검색 (부분 검색)
        if (productCode != null && !productCode.isEmpty()) {
            products = stockService.getProductsByCode(productCode);
        } else {
            // 상품 코드가 없으면 모든 상품 목록을 조회
            products = stockService.getAllProducts();
        }

        model.addAttribute("products", products);
        return "stock/stockList";  // 재고 목록을 표시할 HTML 페이지 이름
    }
    
    // 엑셀 다운로드 기능
    @GetMapping("/stocks/download")
    @ResponseBody
    public void downloadExcel(HttpServletResponse response) throws IOException {
        // 데이터 가져오기 (전체 재고 목록)
        List<Product> products = stockService.getAllProducts();

        // 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("재고 목록");

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("상품 코드");
        headerRow.createCell(1).setCellValue("재고");

        // 데이터 행 생성
        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getProductCode());
            row.createCell(1).setCellValue(product.getProductStock());
        }

        // 응답 헤더 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=product_stock.xlsx");

        // 워크북을 출력 스트림으로 작성
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    
    @GetMapping("/stocks/edit/{productCode}")
    public String editStock(@PathVariable("productCode") String productCode, Model model) {
        // 수정할 상품 정보를 불러옴
        Product product = stockService.getProductByCode(productCode)
                                      .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        System.out.println("Product Code: " + product.getProductCode());  // 로그 추가
        model.addAttribute("product", product);  // product 객체를 model에 추가하여 전달
        return "stock/stockEdit";  // 재고 수정 페이지
    }

    @PostMapping("/stocks/update/{productCode}")
    public String updateStock(@PathVariable("productCode") String productCode,
                              @RequestParam("productStock") int productStock,
                              RedirectAttributes redirectAttributes) {
        // 상품 수정 로직
        stockService.updateStock(productCode, productStock);

        redirectAttributes.addFlashAttribute("message", "상품 재고가 성공적으로 수정되었습니다.");
        return "redirect:/stocks";  // 재고 목록 페이지로 리디렉션
    }

}
