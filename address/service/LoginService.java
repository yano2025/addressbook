package com.example.address.service;

import java.util.Optional;

import com.example.address.entity.Login;

public interface LoginService {
	//全件取得
	Iterable<Login> selectAll();
	//IDで1件指定取得
	Optional<Login> selectOneById(Integer loginid);
	//登録する
	void insert(Login login);
	//更新する
	void update(Login login);
	//削除する
	void deleteById(Integer loginid);
}