package com.steam_discount.discountList.service;


import com.steam_discount.crawling.SteamDiscountCrawling;
import com.steam_discount.discountList.entity.Discount;
import com.steam_discount.discountList.repository.DiscountRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final SteamDiscountCrawling steamDiscountCrawling;


    public List<Discount> findAllDiscoutList() {
        return discountRepository.findAll();
    }

    @Transactional
    public void crawlingDiscountListAndSave(){
        List<Discount> discountList = steamDiscountCrawling.getDiscountList();

        discountRepository.saveAll(discountList);
    }
}