package com.steam_discount.discountList.controller;

import com.steam_discount.discountList.entity.Discount;
import com.steam_discount.discountList.service.DiscountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiscountRestController {
    private final DiscountService discountService;

    @GetMapping("/test")
    public void test(){
        discountService.crawlingDiscountListAndSave();
    }

    @GetMapping("/discount-list")
    public ResponseEntity<List<Discount>> getDiscountList(){
        return ResponseEntity.ok(discountService.findAllDiscoutList());
    }
}
