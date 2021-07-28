package com.ripon.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ripon.service.TicketResourceService;

@Controller
public class TicketResourceController {
	
	@Autowired
	private TicketResourceService ticketResourceService;
	
	// uploading ticket resource
	@PostMapping("/upload-files")
	public String uploadFile(@RequestParam("file") MultipartFile[] files, @RequestParam("ticketId") String ticketId) throws IOException {
		//System.out.println("No of files = " + files.length);
		ticketResourceService.uploadTicketResource(ticketId, files);
		return "redirect:/ticket-detail/"+ticketId+"?status_success=File+is+added+successfully+!";
	}

	// showing resource of a ticket on the browser
	@GetMapping(value = "/show-resource", produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> showResource(@RequestParam("id") String ticketId, @RequestParam("name") String resourceName, HttpServletResponse response ) {
		
		String extension = FilenameUtils.getExtension(resourceName);
		String contentType = null;
		if(extension.equals("pdf")) {
			contentType = "application/pdf";
		} else if(extension.equals("png")) {
			contentType = "image/png";
		} else if(extension.equals("jpg")) {
			contentType = "image/jpg";
		}else if(extension.equals("jpeg")) {
			contentType = "image/jpeg";
		}else if(extension.equals("docx")) {
			contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}else if(extension.equals("txt")) {
			contentType = "text/plain";
		}
		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(ticketResourceService.showTicketResource(ticketId, resourceName));
	}
	
	
	@GetMapping("/delete-resource")
	public String deleteResource(@RequestParam("resourceId") String resourceId, @RequestParam("ticketId") String ticketId, @RequestParam("resourceName") String resourceName) {
		ticketResourceService.deleteTicketResource(resourceId, ticketId, resourceName);
		return "redirect:/ticket-detail/"+ticketId+"?status_success=File+is+deleted+!";
	}
	
	
	
	
	
	
}
