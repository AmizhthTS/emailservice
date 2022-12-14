package com.amizhth.email.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amizhth.email.dao.EmailDetailsDao;
import com.amizhth.email.dto.EmailDetails;
import com.amizhth.email.entitymodel.EmailDetailsModel;
import com.amizhth.email.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailDetailsDao emailDetailsDao;


	@PostMapping(value = "/sendMail", produces = { MediaType.APPLICATION_JSON_VALUE })
	public EmailDetails sendMail(@RequestBody EmailDetails details, HttpServletRequest request) {
		EmailDetailsModel emailDetailsModel = new EmailDetailsModel();
		EmailDetails emailDetails = null;
		try {
			//
			int id = mailsave(details);
			emailDetailsModel = emailDetailsDao.findById(id);
			emailDetailsModel.setModifiedby(details.getRequestedBy());
			emailDetailsModel.setModifiedtime(LocalDateTime.now());
			try {
				emailDetails = emailService.sendSimpleMail(details);
				emailDetailsModel.setStatus(emailDetails.getStatus());
				emailDetailsModel.setErrorcode(emailDetails.getErrorcode());
				emailDetailsModel.setErrormessage(emailDetails.getErrormessage());
			} catch (Exception e) {
				emailDetailsModel.setStatus("Failed");
				emailDetailsModel.setErrorcode("500");
				emailDetailsModel.setErrormessage(e.getMessage());
			}
			emailDetailsDao.save(emailDetailsModel);
			details.setStatus(emailDetails.getStatus());
			return details;
		} catch (Exception e) {
			emailDetails = new EmailDetails();
			emailDetails.setStatus("Failed");
			emailDetails.setErrorcode("500");
			emailDetails.setErrormessage(e.getMessage());
			emailDetailsModel.setErrorcode("500");
			emailDetailsModel.setErrormessage(e.getMessage());
			emailDetailsModel.setStatus(emailDetails.getStatus());
			emailDetailsDao.save(emailDetailsModel);
			return emailDetails;
		}
	} 

	// Sending email with attachment
	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailDetails details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}

	public int mailsave(EmailDetails details) throws Exception {
		EmailDetailsModel emailDetailsModel = new EmailDetailsModel();
		emailDetailsModel = EmailDetailsModel.builder().id(0).active(1).attachment(details.getAttachment())
				.body(details.getMsgBody()).recipient(details.getRecipient()).subject(details.getSubject())
				.createdtime(LocalDateTime.now()).createdby(details.getRequestedBy()).build();
		
		emailDetailsModel = emailDetailsDao.save(emailDetailsModel);
		System.out.println(emailDetailsModel.getId());
		details.setStatus("Success");
		return emailDetailsModel.getId();
	}

}