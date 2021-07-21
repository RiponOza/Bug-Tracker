package com.ripon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class FileUploadController {
	

	@GetMapping(value = "/getimage/{image-name}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImage(@PathVariable("image-name") String imageName, HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");
		File file = new File( "C:\\Users\\user\\Desktop\\images\\" + imageName + ".jpg");
		FileInputStream fin = new FileInputStream(file);
		return IOUtils.toByteArray(fin);
		
	}

	
	
	/** 
	 *
	 * @param imageName
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 * 
	 * this method saves the image to file system.
	 */
	
	@PostMapping("/uploadimage")
	public String saveUser(@RequestParam("file") MultipartFile multipartFile) throws IOException {

		File file = new File("C:\\Users\\user\\Desktop\\images\\" + multipartFile.getName() + ".jpg");
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream fout = new FileOutputStream(file);
		byte b[] = multipartFile.getBytes();
		fout.write(b);
		return "uploaded !!";
	}
}
