package com.example.address.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="loginid")

public class Login {
	@Id
	private Integer loginid;
	private String username;
	private String pass;
	private String myname;
	private Integer mypostno;
	private String myaddress;
}
