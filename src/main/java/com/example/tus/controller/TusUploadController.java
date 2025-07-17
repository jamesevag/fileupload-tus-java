package com.example.tus.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class TusUploadController {

  private final TusFileUploadService tusService;

  public TusUploadController(TusFileUploadService tusService) {
    this.tusService = tusService;
  }

  @RequestMapping(value = "/**", method = {RequestMethod.POST, RequestMethod.PATCH,
      RequestMethod.HEAD, RequestMethod.OPTIONS})
  public void handleTusUpload(HttpServletRequest request, HttpServletResponse response)
      throws IOException, TusException {
    tusService.process(request, response);
    // Try to extract the full URI for a specific upload
    UploadInfo uploadInfo = tusService.getUploadInfo(request.getRequestURI());

    // Set Location header manually for POST
    if ("POST".equalsIgnoreCase(request.getMethod()) && uploadInfo != null) {
      String baseUrl = request.getRequestURL().toString(); // http://localhost:8080/files
      String uploadId = String.valueOf(uploadInfo.getId()); // e.g., 6787bd27-...
      String location = baseUrl.endsWith("/") ? baseUrl + uploadId : baseUrl + "/" + uploadId;
      response.setStatus(HttpServletResponse.SC_CREATED);
      response.setHeader("Location", location);
      response.setHeader("Tus-Resumable", "1.0.0");
      response.setHeader("Upload-Offset", String.valueOf(uploadInfo.getOffset()));
      response.setHeader("Upload-Length", String.valueOf(uploadInfo.getLength()));
      response.setHeader("Cache-Control", "no-store");
    }

    if (uploadInfo == null) {
      System.out.println("Upload info not available (might be initial POST or finished)");
    } else {
      System.out.println("Upload info found for: ");
    }
  }
}