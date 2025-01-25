package com.example.address.service;

import java.util.Optional;

import com.example.address.entity.Address;

public interface AddressService {
	//全件取得
	Iterable<Address> selectAll();
	//IDで1件指定取得
	Optional<Address> selectOneById(Integer id);
	//登録する
	void insertAddress(Address address);
	//更新する
	void updateAddress(Address address);
	//削除する
	void deleteAddressById(Integer id);

}