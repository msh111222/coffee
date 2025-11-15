package com.example.coffee.repository;

import com.example.coffee.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // 根据用户ID获取所有地址
    List<Address> findByUserId(Long userId);
    
    // 根据用户ID获取默认地址
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
}
