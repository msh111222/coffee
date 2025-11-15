package com.example.coffee.service;

import com.example.coffee.entity.Address;
import com.example.coffee.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    
    // 获取用户的所有地址
    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }
    
    // 添加地址
    public Address addAddress(Address address) {
        address.setCreateTime(new Date());
        address.setUpdateTime(new Date());
        
        // 如果是设为默认地址，则取消其他默认地址
        if (address.getIsDefault()) {
            List<Address> addresses = addressRepository.findByUserId(address.getUserId());
            for (Address addr : addresses) {
                if (addr.getIsDefault()) {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                }
            }
        }
        
        return addressRepository.save(address);
    }
    
    // 更新地址
    public Address updateAddress(Address address) {
        Address existingAddress = addressRepository.findById(address.getId()).orElse(null);
        if (existingAddress == null) {
            throw new RuntimeException("地址不存在");
        }
        
        // 如果设为默认地址，取消其他默认地址
        if (address.getIsDefault() && !existingAddress.getIsDefault()) {
            List<Address> addresses = addressRepository.findByUserId(address.getUserId());
            for (Address addr : addresses) {
                if (addr.getIsDefault()) {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
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
        
        return addressRepository.save(existingAddress);
    }
    
    // 删除地址
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
    
    // 获取默认地址
    public Address getDefaultAddress(Long userId) {
        return addressRepository.findByUserIdAndIsDefaultTrue(userId).orElse(null);
    }
}
