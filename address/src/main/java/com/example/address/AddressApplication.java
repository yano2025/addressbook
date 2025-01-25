package com.example.address;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.address.Repository.AddressRepository;
import com.example.address.Repository.LoginRepository;
import com.example.address.entity.Address;
import com.example.address.entity.Login;

@SpringBootApplication
public class AddressApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressApplication.class, args);
//		.getBean(AddressApplication.class).execute();
	}
	@Autowired
	AddressRepository repository;
	@Autowired
	LoginRepository logrepository;
	
	private void execute() {
//address用
//		setup();
//		showList();
//		showOne();
//		updateAddress();
//		deleteAddress();
//login用
//		logsetup();
//		logshowList();
//		logshowOne();
//		logupdateLogin();
//		logdeleteLogin();
//		
	}
	//データ登録用メソッド
	private void setup() {
		//　()内データ左からid,name,postno,address,age
		Address address1 =new Address(null,"太郎",5100000,"三重県",40,"兄");
		address1 = repository.save(address1);
		System.out.println("登録したデータは、" + address1 +"です。");
	}
	//全件データ取得用メソッド
	private void showList() {
		Iterable<Address> addresses=repository.findAll();
		for(Address address : addresses) {
			System.out.println(address);
		}
	}
	//1件データ取得用メソッド
	private void showOne() {
		//findById(*)の*には主キーのidを入れる
		Optional<Address> addressOpt = repository.findById(1);
		if(addressOpt.isPresent()) {
			System.out.println(addressOpt.get());
		}else {
			System.out.println("該当する住所録データは存在しません");
		}
	}
	//データ更新用メソッド
	private void updateAddress() {
		//　()内データ左からid,name,postno,address,age
		Address address1 = new Address(1,"花子",5200000,"愛知県",20,"姉");
		address1 = repository.save(address1);
		System.out.println("更新したデータは、" + address1 + "です。");
	}
	//データ削除用メソッド
	private void deleteAddress() {
		//　deleteById(*)の*には主キーのidを入れる
		repository.deleteById(2);
		System.out.println("---削除完了---");
	}
	
	
//login用
	//データ登録用メソッド
	private void logsetup() {
		//　()内データ左からid,name,postno,address,age
		Login login1 =new Login(null,"taro","pass","太郎",5100000,"三重県");
		login1 = logrepository.save(login1);
		System.out.println("登録したデータは、" + login1 +"です。");
	}
	//全件データ取得用メソッド
	private void logshowList() {
		Iterable<Login> logines=logrepository.findAll();
		for(Login login : logines) {
			System.out.println(login);
		}
	}
	//1件データ取得用メソッド
	private void logshowOne() {
		//findById(*)の*には主キーのidを入れる
		Optional<Login> loginOpt = logrepository.findById(1);
		if(loginOpt.isPresent()) {
			System.out.println(loginOpt.get());
		}else {
			System.out.println("該当する住所録データは存在しません");
		}
	}
	//データ更新用メソッド
	private void logupdateLogin() {
		//　()内データ左からid,name,postno,address,age
		Login login1 = new Login(4,"hanako","password","花子",5200000,"愛知県");
		login1 = logrepository.save(login1);
		System.out.println("更新したデータは、" + login1 + "です。");
	}
	//データ削除用メソッド
	private void logdeleteLogin() {
		//　deleteById(*)の*には主キーのidを入れる
		logrepository.deleteById(4);
		System.out.println("---削除完了---");
	}
}
