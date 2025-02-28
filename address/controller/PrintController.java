package com.example.address.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.address.entity.Address;
import com.example.address.entity.Login;
import com.example.address.form.AddressForm;
import com.example.address.form.LoginForm;
import com.example.address.service.AddressService;
import com.example.address.service.LoginService;

@Controller
@RequestMapping(path = "/print", method = RequestMethod.POST)

public class PrintController {
	
	@Autowired
	AddressService service;
	@Autowired
	LoginService loginservice;
	@ModelAttribute
	public AddressForm setUpForm() {
		AddressForm form = new AddressForm();
		return form;
	}
	@GetMapping
	public String showList(LoginForm loginForm,AddressForm addressForm,Model model) {
		addressForm.setNewAddress(true);
		Iterable<Address>list = service.selectAll();
		model.addAttribute("list",list);
		model.addAttribute("title","登録用フォーム");
		//★後で消す
		//System.out.println(loginForm.getUserid());
		return "printselect";
	}
	@GetMapping("/print{id}")
	public String printaddress(LoginForm loginForm,AddressForm addressForm,@PathVariable Integer id,Model model) {
		//★後で消す
		//System.out.println(loginForm.getUserid());
		Optional<Address>addressOpt =service.selectOneById(id);
		Optional<AddressForm>addressFormOpt = addressOpt.map(t -> makeAddressForm(t));
		if(addressFormOpt.isPresent()) {
			addressForm = addressFormOpt.get();
		}
		model.addAttribute("list",addressForm);
		Optional<Login>loginOpt =loginservice.selectOneById(loginForm.getUserid());
		Optional<LoginForm>loginFormOpt = loginOpt.map(t -> makeLoginForm(t));
		if(loginFormOpt.isPresent()) {
			loginForm = loginFormOpt.get();
		}
		model.addAttribute("loginlist",loginForm);
		
		return"print";
	}
	//Address→AddressFormへの詰め直し
	private AddressForm makeAddressForm(Address address) {
		AddressForm form = new AddressForm();
		form.setId(address.getId());
		form.setName(address.getName());
		form.setPostno(address.getPostno());
		form.setAddress(address.getAddress());
		form.setAge(address.getAge());
		form.setText(address.getText());
		return form;
	}
	//Login→LoginFormへの詰め直し
	private LoginForm makeLoginForm(Login login) {
		LoginForm form = new LoginForm();
		form.setLoginid(login.getLoginid());
		form.setUsername(login.getUsername());
		form.setPass(login.getPass());
		form.setMyname(login.getMyname());
		form.setMypostno(login.getMypostno());
		form.setMyaddress(login.getMyaddress());
		return form;
	}
	@PostMapping("/cardtext")
	public String cardtext(){
		return "cardtext";
	}
}
