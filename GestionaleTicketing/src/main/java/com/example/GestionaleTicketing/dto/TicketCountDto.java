package com.example.GestionaleTicketing.dto;



public class TicketCountDto {
	private String date;
	private Long count;
	
	
	public TicketCountDto(String date, Long count) {
		super();
		this.date = date;
		this.count = count;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
