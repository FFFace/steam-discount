package com.steam_discount.discountList.controller;

import com.steam_discount.discountList.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiscountRestController {
    private final DiscountService discountService;

    @GetMapping("/test")
    public void test(){
        discountService.crawlingDiscountListAndSave();
    }
}
