package com.ripon.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ripon.service.TicketResourceService;

@Controller
public class FileUploadRestController {

	@Autowired
	TicketResourceService ticketResourceService;

//	@PostMapping("/upload-files")
//	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile[] files, @RequestParam("ticketId") String ticketId) throws IOException {
//		System.out.println("No of files = " + files.length);
//		if(ticketResourceService.uploadTicketResource(ticketId, files)) {
//			return ResponseEntity.ok("Working");
//		}else {
//			return ResponseEntity.ok("Error occured");
//		}
//		
//	}

	

}
