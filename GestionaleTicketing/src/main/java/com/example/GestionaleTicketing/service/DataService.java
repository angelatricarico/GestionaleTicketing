package com.example.GestionaleTicketing.service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GestionaleTicketing.dto.TicketCountDto;
import com.example.GestionaleTicketing.model.Ticket;
import com.example.GestionaleTicketing.repository.TicketRepository;

@Service
public class DataService {
	
	private final TicketRepository ticketRepository;
	
	@Autowired
	public DataService(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}
	
	public List<TicketCountDto> getAllTicketsCountByMonth() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
		Map<String, Long> data = ticketRepository.findAll().stream()
				.collect(Collectors.groupingBy(
						ticket -> ticket.getDataApertura().format(formatter),
						Collectors.counting()
						));
		return data.entrySet().stream().map(entry -> {
			return new TicketCountDto(entry.getKey(), entry.getValue());
		})
		.sorted((a,b) -> YearMonth.parse(a.getDate(), formatter).compareTo(YearMonth.parse(b.getDate(), formatter)))
		.collect(Collectors.toList());
	}
	
	public List<TicketCountDto> getAllClosedTicketsCountByMonth() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
		Map<String, Long> data = ticketRepository.findAllByStatus(Ticket.Status.CHIUSO).stream()
				.collect(Collectors.groupingBy(
						ticket -> ticket.getDataChiusura().format(formatter),
						Collectors.counting()
						));
		return data.entrySet().stream().map(entry -> {
			return new TicketCountDto(entry.getKey(), entry.getValue());
		})
		.sorted((a,b) -> YearMonth.parse(a.getDate(), formatter).compareTo(YearMonth.parse(b.getDate(), formatter)))
		.collect(Collectors.toList());
	}
}
