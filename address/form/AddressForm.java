package com.example.address.form;

import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="address")
public class AddressForm {
	private Integer id;
	@NotBlank
	private String name;
	@NotNull
	@Max(9999999)
	@Min(1000000)
	private Integer postno;
	@NotBlank
	private String address;
	@Min(0)
	private Integer age;
	private String text; 
	private Boolean newAddress;
}
