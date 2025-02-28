package com.example.address.Repository;

import org.springframework.data.repository.CrudRepository;

import com.example.address.entity.Address;

public interface AddressRepository extends CrudRepository<Address,Integer>{

}
