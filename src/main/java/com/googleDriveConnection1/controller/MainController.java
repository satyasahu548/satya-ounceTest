package com.googleDriveConnection1.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.model.File;
import com.googleDriveConnection1.services.FileManager;

//import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class MainController {
	private FileManager fileManager;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	@GetMapping({ "/" })
	public ResponseEntity<List<File>> listEverything(@CookieValue(required = false) String anyCookieValue) throws IOException, GeneralSecurityException {
		List<File> files = fileManager.listEverything();
		return ResponseEntity.ok(files);
	}

	@GetMapping({ "/list", "/list/{parentId}" })
	public ResponseEntity<List<File>> list(@PathVariable(required = false) String parentId)
			throws IOException, GeneralSecurityException {
		List<File> files = fileManager.listFolderContent(parentId);
		return ResponseEntity.ok(files);
	}

	@GetMapping("/download/{id}")
	public void download(@PathVariable String id, HttpServletResponse response)
			throws IOException, GeneralSecurityException {
		fileManager.downloadFile(id, response.getOutputStream());
	}

	@GetMapping("/directory/create")
	public ResponseEntity<String> createDirecory(@RequestParam String path) throws Exception {
		String parentId = fileManager.getFolderId(path);
		return ResponseEntity.ok("parentId: " + parentId);
	}

	@PostMapping(value = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> uploadSingleFileExample4(@RequestBody MultipartFile file,
			@RequestParam(required = false) String path) {
		log.info("Request contains, File: " + file.getOriginalFilename());
		String fileId = fileManager.uploadFile(file, path);
		if (fileId == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok("Success, FileId: " + fileId);
	}

	@GetMapping("/delete/{id}")
	public void delete(@PathVariable String id) throws Exception {
		fileManager.deleteFile(id);
	}
}