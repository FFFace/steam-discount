package com.steam_discount.discountList.controller;

import com.steam_discount.common.crawling.SteamDiscountCrawling;
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
    private final SteamDiscountCrawling steamDiscountCrawling;

    @GetMapping("/discount-list")
    public ResponseEntity<List<Discount>> getDiscountList(){
        return ResponseEntity.ok(discountService.findAllDiscoutList());
    }

    @GetMapping("/discount-five")
    public ResponseEntity<List<Discount>> getDiscountFive(){
        return ResponseEntity.ok(discountService.findRandomFive());
    }

    @GetMapping("/new-discount")
    public void newDiscount(){
        discountService.crawlingDiscountListAndSave();
    }
}
