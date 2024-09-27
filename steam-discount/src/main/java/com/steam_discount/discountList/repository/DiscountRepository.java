package com.steam_discount.discountList.repository;

import com.steam_discount.discountList.entity.Discount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query(value = "select * from discount order by RAND() limit 5", nativeQuery = true)
    List<Discount> findRandomFive();
}
