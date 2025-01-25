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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.address.entity.Login;
import com.example.address.form.LoginForm;
import com.example.address.service.LoginService;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	LoginService service;
	@ModelAttribute
	public LoginForm setUpForm() {
		LoginForm loginform = new LoginForm();
		return loginform;
	}
	@GetMapping
	public String showList(LoginForm loginForm,Model model) {
		loginForm.setNewLogin("login");
		model.addAttribute("title","住所録へようこそ");
		
		//初回ログインの場合userSetメソッドへ移動、2回目ログインの場合ログイン画面へ移動
		Optional<Login> loginOpt = service.selectOneById(1);
		if(loginOpt.isPresent()) {
			return "login";
		}else {
			return userSet(loginForm,model);
		}
	}
		
	//ログインIDとパスワードの照合
	@PostMapping("/logincheck")
	public String logincheck(LoginForm loginForm,Model model) {
		
		//Optional<Login> loginOpt = service.selectOneById(1);
		Iterable<Login> testIte = service.selectAll();
		
		String testName = "";
		String testPass = "";
		
		
		for (Login i:testIte) {
			testName= i.getUsername();
			testPass = i.getPass();
			//Login usermap = loginOpt.get();
			//String userid = usermap.getUsername();
			//String userpass = usermap.getPass();
			if(testName.equals(loginForm.getInputuser()) && testPass.equals(loginForm.getInputpass())) {
				return loginOk(loginForm,model);
			}
		}
			model.addAttribute("title","ログインＩＤかパスワードが間違っています。");
			loginForm.setNewLogin("login");
			return "login";
		

	}	
	
	//メニュー画面へ移動
	public String loginOk(LoginForm loginForm,Model model) {
		Iterable<Login>list = service.selectAll();
		model.addAttribute("list",list);
		model.addAttribute("title","メニューを選択してください");
		return "select";
	}
	
	//初回の新規ログイン登録
	@PostMapping("/login/userset")
	public String userSet(LoginForm loginForm,Model model) {
		loginForm.setNewLogin("userset");
		Iterable<Login>list = service.selectAll();
		model.addAttribute("list",list);
		model.addAttribute("title","ユーザー新規登録");	
		return "login";
	}
	//初回のログイン登録実行
	@PostMapping("/logininsert")
	public String insert(@Validated LoginForm loginForm,BindingResult result,
			Model model,RedirectAttributes redirectAttributes) {
		Login login = new Login();
		login.setLoginid(loginForm.getLoginid());//追加
		login.setUsername(loginForm.getUsername());
		login.setPass(loginForm.getPass());
		login.setMyname(loginForm.getMyname());
		login.setMypostno(loginForm.getMypostno());
		login.setMyaddress(loginForm.getMyaddress());
		if(!result.hasErrors()) {
			service.insert(login);
			redirectAttributes.addFlashAttribute("complete","登録が完了しました");
			loginForm.setNewLogin("login");
			Iterable<Login>list = service.selectAll();
			model.addAttribute("list",list);
			return "select";
		}else {
			loginForm.setNewLogin("userset");
			return "login";
		}
	}
		
		
	//変更用
	@GetMapping("/{loginid}")
	public String showUpdate(LoginForm loginForm,@PathVariable Integer loginid,Model model) {
		Optional<Login>loginOpt =service.selectOneById(loginid);
		Optional<LoginForm>loginFormOpt = loginOpt.map(t -> makeLoginForm(t));
		if(loginFormOpt.isPresent()) {
			loginForm = loginFormOpt.get();
		}
		makeUpdateModel(loginForm,model);
		return"select";
	}
	//更新用のModelを作成
	private void makeUpdateModel(LoginForm loginForm,Model model) {
		model.addAttribute("loginid",loginForm.getLoginid());
		loginForm.setNewSelect(true);
		model.addAttribute("loginForm",loginForm);
		model.addAttribute("title","更新用フォーム");
	}
	//loginidをキーとしてデータを更新する。
	@PostMapping("/update")
	public String update(@Validated LoginForm loginForm,
			BindingResult result,Model model,RedirectAttributes redirectAttributes) {
		Login login = makeLogin(loginForm);
		if(!result.hasErrors()) {
			service.update(login);
			redirectAttributes.addFlashAttribute("complete","更新が完了しました");
			loginForm.setNewSelect(false);
			return "redirect:/login/" +login.getLoginid();
		}else {
			makeUpdateModel(loginForm,model);
			return"select";
		}
	}
	//LoginForm→Loginへの詰め直し
	private Login makeLogin(LoginForm loginForm) {
		Login login = new Login();
		login.setLoginid(loginForm.getLoginid());
		login.setUsername(loginForm.getUsername());
		login.setPass(loginForm.getPass());
		login.setMyname(loginForm.getMyname());
		login.setMypostno(loginForm.getMypostno());
		login.setMyaddress(loginForm.getMyaddress());
		return login;
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
	//メニュー画面（select）へもどる
	@PostMapping("/goselect")
	private String goSelect(LoginForm loginForm,Model model) {
		loginForm.setNewSelect(false);
		return loginOk(loginForm,model);
	}
	
	//ログイン画面へもどる※使用できない後日調査
	@PostMapping("/gologin")
	private String goLogin(LoginForm loginForm,Model model) {
		return "login";
//		return loginOk(loginForm,model);
	}
		
	@GetMapping("/print{id}")
	public String printaddress(LoginForm loginForm,@PathVariable Integer id,Model model) {
		Optional<Login>loginOpt =service.selectOneById(id);
		Optional<LoginForm>loginFormOpt = loginOpt.map(t -> makeLoginForm(t));
		if(loginFormOpt.isPresent()) {
			loginForm = loginFormOpt.get();
		}
		model.addAttribute("loginlist",loginForm);
		return"print";
	}
} 
