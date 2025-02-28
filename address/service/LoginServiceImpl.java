package com.example.address.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.address.Repository.LoginRepository;
import com.example.address.entity.Login;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	LoginRepository logrepository;

	@Override
	public Iterable<Login> selectAll() {
		return logrepository.findAll();
	}

	@Override
	public Optional<Login> selectOneById(Integer loginid) {
		return logrepository.findById(loginid);
	}

	@Override
	public void update(Login login) {
		logrepository.save(login);

	}

	@Override
	public void deleteById(Integer loginid) {
		logrepository.deleteById(loginid);

	}

	@Override
	public void insert(Login login) {
		logrepository.save(login);
		
	}
	
	//address
/*	@Override
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
*/		
}