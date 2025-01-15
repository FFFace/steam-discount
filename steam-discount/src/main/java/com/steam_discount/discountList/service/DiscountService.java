package com.steam_discount.discountList.service;


import com.steam_discount.common.crawling.SteamDiscountCrawling;
import com.steam_discount.discountList.entity.Discount;
import com.steam_discount.discountList.repository.DiscountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final SteamDiscountCrawling steamDiscountCrawling;


    public List<Discount> findAllDiscoutList() {
        return discountRepository.findAll();
    }

    public List<Discount> findRandomFive(){
        return discountRepository.findRandomFive();
    }

    @Transactional
    public void crawlingDiscountListAndSave(){
        List<Discount> discountList = steamDiscountCrawling.getDiscountList();

        discountRepository.saveAll(discountList);
    }
}