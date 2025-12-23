package com.example.coffee.controller;

import com.example.coffee.common.Result;
import com.example.coffee.entity.Address;
import com.example.coffee.repository.AddressRepository;
import com.example.coffee.service.AddressService;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/address"})
@CrossOrigin
public class AddressController {
   @Autowired
   private AddressService addressService;
   @Autowired
   private AddressRepository addressRepository;

   @GetMapping({"/list/{userId}"})
   public Result getUserAddresses(@PathVariable Long userId) {
      try {
         List<Address> addresses = this.addressService.getUserAddresses(userId);
         return Result.success("获取地址列表成功", addresses);
      } catch (Exception var3) {
         return Result.error("获取地址列表失败: " + var3.getMessage());
      }
   }

   @PostMapping({"/add"})
   public Result addAddress(@RequestBody Address address) {
      try {
         if (address.getIsDefault() == null) {
            address.setIsDefault(false);
         }

         Address newAddress = this.addressService.addAddress(address);
         return Result.success("添加地址成功", newAddress);
      } catch (Exception var3) {
         return Result.error("添加地址失败: " + var3.getMessage());
      }
   }

   @PutMapping({"/update"})
   public Result updateAddress(@RequestBody Address address) {
      try {
         Address updatedAddress = this.addressService.updateAddress(address);
         return Result.success("更新地址成功", updatedAddress);
      } catch (Exception var3) {
         return Result.error("更新地址失败: " + var3.getMessage());
      }
   }

   @DeleteMapping({"/delete/{id}"})
   public Result deleteAddress(@PathVariable Long id) {
      try {
         this.addressService.deleteAddress(id);
         return Result.success("删除地址成功");
      } catch (Exception var3) {
         return Result.error("删除地址失败: " + var3.getMessage());
      }
   }

   @GetMapping({"/default/{userId}"})
   public Result getDefaultAddress(@PathVariable Long userId) {
      try {
         Address address = this.addressService.getDefaultAddress(userId);
         return address != null ? Result.success("获取默认地址成功", address) : Result.error("未设置默认地址");
      } catch (Exception var3) {
         return Result.error("获取默认地址失败: " + var3.getMessage());
      }
   }

   @PutMapping({"/setDefault/{id}"})
   public Result setDefaultAddress(@PathVariable Long id, @RequestBody Map<String, Long> request) {
      try {
         Long userId = (Long)request.get("userId");
         Address address = (Address)this.addressRepository.findById(id).orElse(null);
         if (address == null) {
            return Result.error("地址不存在");
         } else if (!address.getUserId().equals(userId)) {
            return Result.error("无权操作");
         } else {
            List<Address> addresses = this.addressRepository.findByUserId(userId);
            Iterator var6 = addresses.iterator();

            while(var6.hasNext()) {
               Address addr = (Address)var6.next();
               if (addr.getIsDefault()) {
                  addr.setIsDefault(false);
                  this.addressRepository.save(addr);
               }
            }

            address.setIsDefault(true);
            address.setUpdateTime(new Date());
            this.addressRepository.save(address);
            return Result.success("设置成功", address);
         }
      } catch (Exception var8) {
         return Result.error("设置失败: " + var8.getMessage());
      }
   }
}
