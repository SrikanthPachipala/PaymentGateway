package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApproveRequest {
	@NotBlank(message = "Code is required")
    private String code;

	public String getCode() {
		return null;
	}
}
