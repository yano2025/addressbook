package com.example.address.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.address.Repository.AddressRepository;
import com.example.address.entity.Address;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	AddressRepository repository;

	@Override
	public Iterable<Address> selectAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Address> selectOneById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public void updateAddress(Address address) {
		repository.save(address);

	}

	@Override
	public void deleteAddressById(Integer id) {
		repository.deleteById(id);

	}

	@Override
	public void insertAddress(Address address) {
		repository.save(address);
		
	}

}
