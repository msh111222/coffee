package com.example.coffee.repository;

import com.example.coffee.entity.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
   List<Address> findByUserId(Long userId);

   Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
}
