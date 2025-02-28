package com.example.address.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
import org.springframework.web.multipart.MultipartFile;
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
	//CSV書き込み用ファイル作成
	@PostMapping("/csvcreit")
	public String scvcreit(RedirectAttributes redirectAttributes) {
		try {
			// 出力ファイルの作成
			FileWriter fw = new FileWriter("C:/Users/user/Desktop/Userdata.csv", false);
			// PrintWriterクラスのオブジェクトを生成
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			
			// ヘッダーの指定
			pw.print("名前＊必須");
			pw.print(",");
			pw.print("郵便番号※必須");
			pw.print(",");
			pw.print("住所※必須");
			pw.print(",");
			pw.print("年齢");
			pw.print(",");
			pw.print("備考");
			pw.println();
			
//			// データを書き込む（今後必要なため念のため残す）
//			for(int i = 0; i < number.length; i++){
//				pw.print(number[i]);
//				pw.print(",");
//				pw.print(userName[i]);
//				pw.print(",");
//				pw.print(userSex[i]);
//				pw.print(",");
//				pw.print(userDepartment[i]);
//				pw.print(",");
//				pw.print(userSalary[i]);
//				pw.println();
//			}
			
			// ファイルを閉じる
			pw.close();
			
			// 出力確認用のメッセージ
			redirectAttributes.addFlashAttribute("complete","デスクトップへ出力しました");
//			System.out.println("csvファイルを出力しました");
			
			// 出力に失敗したときの処理
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return"address";
	}
//	CSVファイルをアップロードする
	 @PostMapping("/csvupload")
	  public String uploadFile(@RequestParam MultipartFile file,
			AddressForm addressForm,BindingResult bindingResult,
			Model model,RedirectAttributes redirectAttributes
			) throws IOException {
		 
		 String csvName ="";
		 Integer csvPostno = 0 ;
		 String csvAddress = "";
		 Integer csvAge = 0 ;
		 String csvText = "";

		 try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))){
		      String line= br.readLine();
		      
		      while ((line = br.readLine()) != null) {
		    	  String[] split = line.split(",");
		    	  csvName = String.valueOf(split[0]);
		    	  csvPostno = Integer.parseInt(split[1]);
		    	  csvAddress = String.valueOf(split[2]);
		    	  csvAge = Integer.parseInt(split[3]);
		    	  csvText = String.valueOf(split[4]);
		    	  
		    	  Address address = new Address();
		    	  AddressForm form = new AddressForm();
		    	  
		    	  //SCV→AddressFormへの書き込み
		    	  addressForm.setName(csvName);
		    	  addressForm.setPostno(csvPostno);
		    	  addressForm.setAddress(csvAddress);
		    	  addressForm.setAge(csvAge);
		    	  addressForm.setText(csvText);
		    	  
		    	  //AddressForm→Addressへの書き込み
		    	  address.setName(addressForm.getName());
		    	  address.setPostno(addressForm.getPostno());
		    	  address.setAddress(addressForm.getAddress());
		    	  address.setAge(addressForm.getAge());
		    	  address.setText(addressForm.getText());
		    	  
		    	  //バリデーションのため、uploadDateへ移動
		    	  uploadDate(form, address, bindingResult, model, redirectAttributes);
		      }
		      br.close();
		 }catch(IOException e){
			 e.printStackTrace();
		 }
		 return"redirect:/address";
	 }
	
	 //CSV書き込みバリデーションのための書き込み用クラス
	 public void uploadDate(@Validated AddressForm addressForm,Address address,BindingResult bindingResult,
				Model model,RedirectAttributes redirectAttributes
				) throws IOException {
		 
		  if(!bindingResult.hasErrors()) {
	    	  service.insertAddress(address);
	    	  redirectAttributes.addFlashAttribute("complete","登録が完了しました");
	      }else {
	    	  System.out.println(bindingResult.getAllErrors());
	    	  showList(addressForm,model);
	      }
	 }

}

