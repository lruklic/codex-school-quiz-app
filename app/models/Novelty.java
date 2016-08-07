package models;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.enums.NewsPriority;
import models.enums.NewsType;

@Entity
@Table(name = "novelty")
public class Novelty extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "noveltyTitle")
	public String noveltyTitle;
	
	@Column(name = "noveltyText", length = 500)
	public String noveltyText;
	
	@Column(name = "created")
	public long created;
	
	@Enumerated(EnumType.STRING)
	public NewsType newsType;
	
	@Enumerated(EnumType.STRING)
	public NewsPriority newsPriority;
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Admin admin;

	public String getCreatedDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
		return Instant.ofEpochSecond(created).atZone(ZoneId.of("GMT+2")).format(formatter);
	}

	public Novelty() {
		
	}
	
	public Novelty(String noveltyTitle, String noveltyText, NewsType newsType, NewsPriority newsPriority, Admin admin) {
		super();
		this.noveltyTitle = noveltyTitle;
		this.noveltyText = noveltyText;
		this.created = System.currentTimeMillis() / 1000;
		this.newsType = newsType;
		this.newsPriority = newsPriority;
		this.admin = admin;
	}
	
	
	
}
