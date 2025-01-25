package com.example.address.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.address.entity.Address;
import com.example.address.form.AddressForm;
import com.example.address.service.AddressService;

@Controller
@RequestMapping(path = "/address", method = RequestMethod.POST)

public class AddressController {	
	@Autowired
	AddressService service;
	@ModelAttribute
	public AddressForm setUpForm() {
		AddressForm form = new AddressForm();
		return form;
	}
	@GetMapping
	public String showList(AddressForm addressForm,Model model) {
		addressForm.setNewAddress(true);
		Iterable<Address>list = service.selectAll();
		model.addAttribute("list",list);
		model.addAttribute("title","登録用フォーム");
		return "address";
	}
	//新規登録用
	@PostMapping("/insert")
	public String insert(@Validated AddressForm addressForm,BindingResult bindingResult,
			Model model,RedirectAttributes redirectAttributes) {
		Address address = new Address();
		address.setName(addressForm.getName());
		address.setPostno(addressForm.getPostno());
		address.setAddress(addressForm.getAddress());
		address.setAge(addressForm.getAge());
		address.setText(addressForm.getText());
		if(!bindingResult.hasErrors()) {
			service.insertAddress(address);
			redirectAttributes.addFlashAttribute("complete","登録が完了しました");
			return"redirect:/address";
		}else {
			return showList(addressForm,model);
		}
	}
	//更新用
	@GetMapping("/{id}")
	public String showUpdate(AddressForm addressForm,@PathVariable Integer id,Model model) {
		Optional<Address>addressOpt =service.selectOneById(id);
		Optional<AddressForm>addressFormOpt = addressOpt.map(t -> makeAddressForm(t));
		if(addressFormOpt.isPresent()) {
			addressForm = addressFormOpt.get();
		}
		makeUpdateModel(addressForm,model);
		return"address";
	}
	//更新用のModelを作成
	private void makeUpdateModel(AddressForm addressForm,Model model) {
		model.addAttribute("id",addressForm.getId());
		addressForm.setNewAddress(false);
		model.addAttribute("addressForm",addressForm);
		model.addAttribute("title","更新用フォーム");
	}
	//idをキーとしてデータを更新する。
	@PostMapping("/update")
	public String update(@Validated AddressForm addressForm,
			BindingResult result,Model model,RedirectAttributes redirectAttributes) {
		Address address = makeAddress(addressForm);
		if(!result.hasErrors()) {
			service.updateAddress(address);
			redirectAttributes.addFlashAttribute("complete","更新が完了しました");
			return "redirect:/address/" +address.getId();
		}else {
			makeUpdateModel(addressForm,model);
			return"address";
		}
	}
	//AddressForm→Addressへの詰め直し
	private Address makeAddress(AddressForm addressForm) {
		Address address = new Address();
		address.setId(addressForm.getId());
		address.setName(addressForm.getName());
		address.setPostno(addressForm.getPostno());
		address.setAddress(addressForm.getAddress());
		address.setAge(addressForm.getAge());
		address.setText(addressForm.getText());
		return address;
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
	//削除用
	@PostMapping("/delete")
	public String delete(@RequestParam("id")String id,Model model,
			RedirectAttributes redirectAttributes) {
		service.deleteAddressById(Integer.parseInt(id));
		redirectAttributes.addFlashAttribute("delcomplete","削除が完了しました");
		return "redirect:/address";
	}
} 

