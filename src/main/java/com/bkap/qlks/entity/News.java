package com.bkap.qlks.entity;



import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bkap_news")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long news_id;
	private String title;
	private String content;
	private String image;
	@Column(name = "created_at")
	private Date createdat;
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "accountId")
	private Account account;
}