package com.kalsym.assetservice.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kalsym.assetservice.AssetServiceApplication;
import com.kalsym.assetservice.model.AssetFile;
import com.kalsym.assetservice.service.AssetFileService;
import com.kalsym.assetservice.utility.HttpResponse;
import com.kalsym.assetservice.utility.Logger;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;


@RestController
@RequestMapping("/assets")
public class AssetFileController {
    

    @Value("${asset.file.path}")
    String filePath;

    @Autowired
    AssetFileService assetFileService;

    /// Get All file asset
    @GetMapping(path = {""})
    public ResponseEntity<HttpResponse> getAllFiles(
        HttpServletRequest request
    ) {

        List<AssetFile> fileArrayList= assetFileService.fileListing(filePath);

        HttpResponse response = new HttpResponse(request.getRequestURI());

        response.setData(fileArrayList);
        response.setStatus(HttpStatus.OK);
        
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    /// Get file asset by file path (encoded uri)
    // @GetMapping(path = {"/file"})
    // public ResponseEntity<HttpResponse> getFileAssetByDirectoryPath(
    //     HttpServletRequest request,
    //     @RequestParam(required = false) String asseturl
    // ) {

    //     String fileDirectory = assetFileService.getFileDirectoryPath(asseturl);

    //     AssetFile af = assetFileService.fileDetails(fileDirectory);

 
    //     HttpResponse response = new HttpResponse(request.getRequestURI());

    //     response.setData(af);
    //     response.setStatus(HttpStatus.OK);
        
    //     return ResponseEntity.status(response.getStatus()).body(response);

    // }

    /// Get file asset by encoded uri file path 
    // @GetMapping(value = "/view")
    // public ResponseEntity<Resource> viewOriginalImage(        
    //     @RequestParam(required = false) String asseturl

    // ) throws IOException {

    //     //To get file directory based on asset url
    //     int k = asseturl.lastIndexOf('/');
    //     String filename = asseturl.substring(k+1);
    //     // String fileDirectory = filePath + "/" + filename;

    //     String fileDirectory = assetFileService.getFileDirectoryPath(asseturl);

    //     final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(
    //         fileDirectory
    //     )));
        
    //         //To get extension file
    //         int i = filename.lastIndexOf('.');
    //         String fileType = filename.substring(i+1);

    //         MediaType mediatype ;

    //         switch (fileType){

    //             case "png":
    //             mediatype = MediaType.IMAGE_PNG;
    //             break;

    //             case "jpg":
    //             mediatype = MediaType.IMAGE_JPEG;
    //             break;

    //             case "gif":
    //             mediatype = MediaType.IMAGE_GIF;
    //             break;

    //             case "pdf":
    //             mediatype = MediaType.APPLICATION_PDF;
    //             break;

    //             default:
    //             mediatype = MediaType.APPLICATION_JSON;

    //         }


    //     return ResponseEntity
    //             .status(HttpStatus.OK)
    //             .contentLength(inputStream.contentLength())
    //             .contentType(mediatype)
    //             .body(inputStream);

    // }

    //https://github.com/lokeshnous/migration/blob/e171df65f7c6eb59901854c3ed069973b5359c80/jobboard/src/main/java/com/advanceweb/afc/jb/home/web/controller/HomeController.java
    // @RequestMapping("/resize")
	// public ResponseEntity<byte[]> getResizeImage(
    //     @RequestParam(required = false) String relativePath,
    //     @RequestParam(required = false) Integer width,
    //     @RequestParam(required = false) Integer height,
	// 		HttpServletResponse response, 
    //         HttpServletRequest request) {
	// 	try {

    //         String fileDirectory = assetFileService.getFolderFilePath(relativePath);

	// 		BufferedImage originalImage = ImageIO.read(new File(fileDirectory));

    //         BufferedImage scaleImage = assetFileService.resizeImage(originalImage, width, height);
    //         // BufferedImage scaleImage = assetFileService.scale(originalImage, width, height);


    //         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// 		ImageIO.write(scaleImage, "jpg", baos);
	// 		baos.flush();
	// 		byte[] imageInByte = baos.toByteArray();
	// 		baos.close();

	// 		// ResponseEntity<byte[]> result = handleGetMyBytesRequest(imageInByte);

    //         HttpHeaders responseHeaders = new HttpHeaders();
    //         responseHeaders.setContentType(MediaType.IMAGE_PNG);
    //         responseHeaders.setContentLength(imageInByte.length);
    
    //         return new ResponseEntity<byte[]>(imageInByte, responseHeaders,
    //                 HttpStatus.OK);

	// 		// Display the image
	// 		// write(response, result.getBody());
	// 	} catch (Exception e) {
    //         System.out.println("ERROR :::::::"+e.getMessage());
    //         e.printStackTrace();
            
    //         return new ResponseEntity<byte[]>(
    //                 HttpStatus.OK);
            
	// 	}
	// }

    /// Get All file asset  Note : asseturl (only for folder)
    // @GetMapping(path = {"/folder"})
    // public ResponseEntity<HttpResponse> getAllFilesSubFolder(
    //     HttpServletRequest request,
    //     @RequestParam(required = false) String asseturl
    // ) {

    //     String fileDirectory = assetFileService.getFileDirectoryPath(asseturl);

    //     List<AssetFile> fileArrayList= assetFileService.fileListing(fileDirectory);

    //     HttpResponse response = new HttpResponse(request.getRequestURI());

    //     response.setData(fileArrayList);
    //     response.setStatus(HttpStatus.OK);
        
    //     return ResponseEntity.status(response.getStatus()).body(response);

    // }


}
