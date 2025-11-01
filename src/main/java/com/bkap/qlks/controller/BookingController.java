package com.bkap.qlks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bkap.qlks.repository.RoomRepository;
import com.bkap.qlks.service.RoomService;

@Controller
@RequestMapping("/booking")
public class BookingController {
	@Autowired
	private RoomService roomService;

	@Autowired
	private RoomRepository roomRepository;

}