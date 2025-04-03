package com.example.address.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.address.entity.Address;
import com.example.address.entity.Login;
import com.example.address.form.AddressForm;
import com.example.address.form.LoginForm;
import com.example.address.service.AddressService;
import com.example.address.service.LoginService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;





@Controller
@RequestMapping(path = "/print", method = RequestMethod.POST)

public class PrintController {
	
	@Autowired
	AddressService service;
	@Autowired
	LoginService loginservice;
    @Autowired
    private SpringTemplateEngine templateEngine;  // SpringTemplateEngine を Autowire

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
//		System.out.println(loginForm.getUserid());

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
	 //PDF出力用クラス	


	@PostMapping("/pdf/{id}")
	public void pdf(@PathVariable Integer id, Model model, HttpServletResponse response) {
	    try {
	        // アドレス情報を取得
	        Optional<Address> addressOpt = service.selectOneById(id);
	        if (addressOpt.isEmpty()) {
	            throw new RuntimeException("指定されたIDのアドレスが見つかりません。");
	        }
	        
	        Address address = addressOpt.get();
	        model.addAttribute("list", address);

	        // Thymeleaf で HTML をレンダリングする
	        String htmlContent = generateHtmlFromTemplate(model);

	        // HTML を PDF に変換
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ConverterProperties converterProperties = new ConverterProperties();
	        HtmlConverter.convertToPdf(new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)), baos, converterProperties);

	        // PDF をレスポンスとして返す
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "attachment; filename=\"output.pdf\"");
	        response.getOutputStream().write(baos.toByteArray());
	        response.getOutputStream().flush();
	    } catch (Exception e) {
	        e.printStackTrace();  // エラーログを記録
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // HTTP 500 エラー
	        try {
	            response.getWriter().write("PDFの生成に失敗しました。");
	        } catch (IOException ioException) {
	            ioException.printStackTrace();
	        }
	    }
	}
	private String generateHtmlFromTemplate(Model model) {
	    // HttpServletRequest を取得
	    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    
	    // ServletContext を取得
	    ServletContext servletContext = request.getServletContext();

	    // WebContext を作成（HttpServletRequest と ServletContext と Locale を渡します）
	    WebContext context = new WebContext(request, servletContext, request.getLocale());

	    //WebContext context = new WebContext(request, servletContext, request.getLocale());

	    // モデルを WebContext にセット
	    model.asMap().forEach(context::setVariable);

	    // Thymeleaf テンプレートを処理して HTML を生成
	    return templateEngine.process("print", context);  // テンプレート名と WebContext を渡す
	}



	
//	private String generateHtmlFromTemplate(Model model) {
//	    try {
//	        // HttpServletRequest を取得
//	        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//
//	        // WebContext を作成して、モデルのデータをセット
//	        WebContext context = new WebContext(request, request.getServletContext(), request.getLocale());
//	        model.asMap().forEach(context::setVariable);  // ModelのデータをWebContextにセット
//
//	        // Thymeleafテンプレートを処理してHTMLを生成
//	        return templateEngine.process("print", context);  // IContextを渡す
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return "";  // HTML 生成に失敗した場合のフォールバック
//	    }
//	}



//	@PostMapping("/pdf/{id}")
//	public void pdf(@PathVariable Integer id, HttpServletResponse response) {
//	    Optional<Address> addressOpt = service.selectOneById(id);
//
//	    if (addressOpt.isEmpty()) {
//	        throw new RuntimeException("指定されたIDのアドレスが見つかりません。");
//	    }
//
//	    try {
//	        // レスポンス設定
//	        response.setContentType("application/pdf");
//	        response.setHeader("Content-Disposition", "attachment; filename=\"report.pdf\"");
//
//	        // PDFWriterインスタンスを作成（OutputStreamに直接書き込む）
//	        PdfWriter writer = new PdfWriter(response.getOutputStream());
//
//	        // PdfDocumentインスタンスを作成
//	        PdfDocument pdf = new PdfDocument(writer);
//
//	        // Documentインスタンスを作成
//	        Document document = new Document(pdf);
//
//	        // Addressデータの取得
//	        Address address = addressOpt.get();
//	        document.add(new Paragraph("名前: " + address.getName()));
//	        document.add(new Paragraph("郵便番号: " + address.getPostno()));
//	        document.add(new Paragraph("住所: " + address.getAddress()));
//	        document.add(new Paragraph("年齢: " + address.getAge()));
//	        document.add(new Paragraph("備考: " + address.getText()));
//
//	        // ドキュメントを閉じる
//	        document.close();
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
//	 @PostMapping("/pdf")
//	 public String pdf(LoginForm loginForm,AddressForm addressForm,@PathVariable Integer id,Model model) {
//		 //★後で消す
//		 System.out.println(123);
//		 
//	        String dest = "report.pdf";
//	        try {
//	            // PDFWriterインスタンスを作成
//	            PdfWriter writer = new PdfWriter(dest);
//
//	            // PdfDocumentインスタンスを作成
//	            PdfDocument pdf = new PdfDocument(writer);
//
//	            // Documentインスタンスを作成
//	            Document document = new Document(pdf);
//
//	            // コンテンツを追加
//	            document.add(new Paragraph("Hello, this is a sample PDF report!"));
//
//	            // ドキュメントを閉じる
//	            document.close();
//
//	            System.out.println("PDF created successfully.");
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	        return "print";
//	    }
}
