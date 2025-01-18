package com.example.demo.entity;


import java.util.Date;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.validator.NotFullWidthSpace;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Todo {
	/* ID */
	private int id;
	/* 件名 */
	@NotBlank
	@NotFullWidthSpace
	private String title;
	/* 重要度 */
	@Range(min = 0, max = 1)
	private int importance;
	/* 緊急度 */
	@Range(min = 0, max = 1)
	private int urgency;
	/* 期限 */
	@NotNull
	@FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date deadline;
	/* 完了 */
	private String done;
}
