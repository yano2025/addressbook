package com.example.address.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
	private Integer loginid;
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9]*")
	private String username;
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9]*")
	private String pass;
	@NotBlank
	private String myname;
	@NotNull
	@Max(9999999)
	@Min(1000000)
	private Integer mypostno;
	@NotBlank
	private String myaddress;
	private String inputuser;
	private String inputpass;
	private String newLogin;
	private Boolean newSelect; 
	private Integer userid;
}
