package com.example.demo.entity;



import lombok.Data;

/**
 *ソート用エンティティ 
 */
@Data
public class SortUrl {
	/* ID */
	private String id;
	/* 件名 */
	private String title;
	/* 重要度 */
	private String importance;
	/* 緊急度 */
	private String urgency;
	/* 期限 */
	private String deadline;
	
	/* カラム */
	private String column;
	/* オーダー */
	private String order;
	
	}
