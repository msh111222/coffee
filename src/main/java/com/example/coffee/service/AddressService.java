package com.example.coffee.service;

import com.example.coffee.entity.Address;
import com.example.coffee.repository.AddressRepository;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
   @Autowired
   private AddressRepository addressRepository;

   public List<Address> getUserAddresses(Long userId) {
      return this.addressRepository.findByUserId(userId);
   }

   public Address addAddress(Address address) {
      address.setCreateTime(new Date());
      address.setUpdateTime(new Date());
      if (address.getIsDefault()) {
         List<Address> addresses = this.addressRepository.findByUserId(address.getUserId());
         Iterator var3 = addresses.iterator();

         while(var3.hasNext()) {
            Address addr = (Address)var3.next();
            if (addr.getIsDefault()) {
               addr.setIsDefault(false);
               this.addressRepository.save(addr);
            }
         }
      }

      return (Address)this.addressRepository.save(address);
   }

   public Address updateAddress(Address address) {
      Address existingAddress = (Address)this.addressRepository.findById(address.getId()).orElse(null);
      if (existingAddress == null) {
         throw new RuntimeException("地址不存在");
      } else {
         if (address.getIsDefault() && !existingAddress.getIsDefault()) {
            List<Address> addresses = this.addressRepository.findByUserId(address.getUserId());
            Iterator var4 = addresses.iterator();

            while(var4.hasNext()) {
               Address addr = (Address)var4.next();
               if (addr.getIsDefault()) {
                  addr.setIsDefault(false);
                  this.addressRepository.save(addr);
               }
            }
         }

         existingAddress.setRecipientName(address.getRecipientName());
         existingAddress.setPhoneNumber(address.getPhoneNumber());
         existingAddress.setProvince(address.getProvince());
         existingAddress.setCity(address.getCity());
         existingAddress.setDistrict(address.getDistrict());
         existingAddress.setDetail(address.getDetail());
         existingAddress.setIsDefault(address.getIsDefault());
         existingAddress.setUpdateTime(new Date());
         return (Address)this.addressRepository.save(existingAddress);
      }
   }

   public void deleteAddress(Long addressId) {
      this.addressRepository.deleteById(addressId);
   }

   public Address getDefaultAddress(Long userId) {
      return (Address)this.addressRepository.findByUserIdAndIsDefaultTrue(userId).orElse(null);
   }
}
