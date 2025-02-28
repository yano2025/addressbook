package com.example.address.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="address")

public class Address {
	@Id
	private Integer id;
	private String name;
	private Integer postno;
	private String address;
	private Integer age;
	private String text;
}
