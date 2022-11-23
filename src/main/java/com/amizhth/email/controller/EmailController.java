package com.amizhth.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amizhth.email.dto.EmailDetails;
import com.amizhth.email.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping(value = "/sendMail", produces = { MediaType.APPLICATION_JSON_VALUE })
	public EmailDetails sendMail(@RequestBody EmailDetails details) {
		
		//To Do
		logrequest(details.getMsgBody(),details.getRecipient(),details.getSubject());
		EmailDetails status = emailService.sendSimpleMail(details);
		logresponse(details.getStatus(),details.geterrorCode(),details.geterrorMessage());
		return status;
	}

	// Sending email with attachment
	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailDetails details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}
}