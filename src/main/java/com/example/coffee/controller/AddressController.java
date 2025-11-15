package com.example.coffee.controller;

import com.example.coffee.common.Result;
import com.example.coffee.entity.Address;
import com.example.coffee.repository.AddressRepository;
import com.example.coffee.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Date;

@RestController
@RequestMapping("/api/address")
@CrossOrigin
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    // 获取用户的所有地址
    @GetMapping("/list/{userId}")
    public Result getUserAddresses(@PathVariable Long userId) {
        try {
            List<Address> addresses = addressService.getUserAddresses(userId);
            return Result.success("获取地址列表成功", addresses);
        } catch(Exception e) {
            return Result.error("获取地址列表失败: " + e.getMessage());
        }
    }

    // 添加地址
    @PostMapping("/add")
    public Result addAddress(@RequestBody Address address) {
        try {
            // 确保isDefault有值，如果没有则默认为false
            if (address.getIsDefault() == null) {
                address.setIsDefault(false);
            }
            Address newAddress = addressService.addAddress(address);
            return Result.success("添加地址成功", newAddress);
        } catch(Exception e) {
            return Result.error("添加地址失败: " + e.getMessage());
        }
    }

    // 更新地址
    @PutMapping("/update")
    public Result updateAddress(@RequestBody Address address) {
        try {
            Address updatedAddress = addressService.updateAddress(address);
            return Result.success("更新地址成功", updatedAddress);
        } catch(Exception e) {
            return Result.error("更新地址失败: " + e.getMessage());
        }
    }

    // 删除地址
    @DeleteMapping("/delete/{id}")
    public Result deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return Result.success("删除地址成功");
        } catch(Exception e) {
            return Result.error("删除地址失败: " + e.getMessage());
        }
    }

    // 获取默认地址
    @GetMapping("/default/{userId}")
    public Result getDefaultAddress(@PathVariable Long userId) {
        try {
            Address address = addressService.getDefaultAddress(userId);
            if (address != null) {
                return Result.success("获取默认地址成功", address);
            }
            return Result.error("未设置默认地址");
        } catch(Exception e) {
            return Result.error("获取默认地址失败: " + e.getMessage());
        }
    }

    // 设置默认地址
    @PutMapping("/setDefault/{id}")
    public Result setDefaultAddress(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Address address = addressRepository.findById(id).orElse(null);

            if (address == null) {
                return Result.error("地址不存在");
            }

            if (!address.getUserId().equals(userId)) {
                return Result.error("无权操作");
            }

            // 取消其他默认地址
            List<Address> addresses = addressRepository.findByUserId(userId);
            for (Address addr : addresses) {
                if (addr.getIsDefault()) {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                }
            }

            // 设置新的默认地址
            address.setIsDefault(true);
            address.setUpdateTime(new Date());
            addressRepository.save(address);

            return Result.success("设置成功", address);
        } catch(Exception e) {
            return Result.error("设置失败: " + e.getMessage());
        }
    }
}