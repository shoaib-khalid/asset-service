package com.kalsym.assetservice.controller;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kalsym.assetservice.model.AssetFile;
import com.kalsym.assetservice.model.HttpReponse;
import com.kalsym.assetservice.service.AssetFileService;
import com.kalsym.assetservice.utility.HttpResponse;

import org.springframework.ui.Model;

import java.awt.image.BufferedImage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import java.io.File;
import org.springframework.core.io.ByteArrayResource;
import java.nio.file.Files;
import java.nio.file.Paths;



@Controller
public class IndexController {

    @Autowired
    AssetFileService assetFileService;

    @Value("${asset.file.path}")
    String filePathOri;

    @Value("${asset.index.show}")
    String enableIndexPage;

    /// Get All file asset
    @RequestMapping("index")
    public String serverFileList(Model model) {

        if(enableIndexPage.equals("true")){
            List<AssetFile> fileArrayList= assetFileService.fileListing(filePathOri);

            model.addAttribute("index", fileArrayList);		
    
            return "index";
        }else {
            return "notfound";

        }

      
    }

    /// Get All file asset
    @RequestMapping("open")
    public String openFolder(Model model, @RequestParam(required = false) String relativePath) {

        String fileDirectory = assetFileService.getFolderFilePath(relativePath);
        // String fileDirectory = fullPath;

        List<AssetFile> fileArrayList= assetFileService.fileListing(fileDirectory);

        model.addAttribute("index", fileArrayList);		

        return "index";
    }

    //https://github.com/lokeshnous/migration/blob/e171df65f7c6eb59901854c3ed069973b5359c80/jobboard/src/main/java/com/advanceweb/afc/jb/home/web/controller/HomeController.java
    @RequestMapping("**")
    public ResponseEntity<?> getResizeImage(
        @RequestParam(required = false) Integer width,
        @RequestParam(required = false) Integer height,
        @RequestParam(required = false) Float compressValue,
            HttpServletRequest request) {
        try {

            String fileDirectory = assetFileService.getFolderFilePath(request.getServletPath());
            //To get extension file
            int i = request.getServletPath().lastIndexOf('.');
            String fileType = request.getServletPath().substring(i+1);

            BufferedImage scaleImage;

            if(width != null || height!=null) {

                BufferedImage originalImage = ImageIO.read(new File(fileDirectory));

                switch (fileType){
           
                    case "png":
                    scaleImage = assetFileService.resizeImagePng(originalImage, width, height);
                    break;
    
                    default:
                    scaleImage = assetFileService.resizeImageJpg(originalImage, width, height);
    
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(scaleImage, fileType, baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.IMAGE_PNG);
                responseHeaders.setContentLength(imageInByte.length);
        
                return new ResponseEntity<byte[]>(imageInByte, responseHeaders,
                        HttpStatus.OK);

            }

            if(compressValue != null){
                
                BufferedImage originalImage = ImageIO.read(new File(fileDirectory));
                ByteArrayOutputStream compressed = assetFileService.compressedImage(originalImage,compressValue,fileType);
                
                // Get data for further processing...
                // byte[] jpegData = compressed.toByteArray();

                // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // ImageIO.write(scaleImage, "jpg", baos);
                compressed.flush();
                byte[] imageInByte = compressed.toByteArray();
                compressed.close();

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.IMAGE_PNG);
                responseHeaders.setContentLength(imageInByte.length);
        
                return new ResponseEntity<byte[]>(imageInByte, responseHeaders,
                        HttpStatus.OK);
            }


            final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(
                fileDirectory
            )));
           
                       MediaType mediatype ;
           
                       switch (fileType){
           
                           case "png":
                           mediatype = MediaType.IMAGE_PNG;
                           break;
           
                           case "jpg":
                           mediatype = MediaType.IMAGE_JPEG;
                           break;
           
                           case "gif":
                           mediatype = MediaType.IMAGE_GIF;
                           break;
           
                           case "pdf":
                           mediatype = MediaType.APPLICATION_PDF;
                           break;

                           case "svg":
                           mediatype = MediaType.APPLICATION_XML;
                           break;
           
                           default:
                           mediatype = MediaType.IMAGE_PNG;
           
                       }
           
           
                   return ResponseEntity
                           .status(HttpStatus.OK)
                           .contentLength(inputStream.contentLength())
                           .contentType(mediatype)
                           .body(inputStream);


       

            // Display the image
            // write(response, result.getBody());
        } catch (Exception e) {
            System.out.println("ERROR :::::::"+e.getMessage());
            e.printStackTrace();

            HttpResponse response = new HttpResponse(request.getRequestURI());
            response.setStatus(HttpStatus.NOT_FOUND);

            return ResponseEntity.status(response.getStatus()).body(response);
            
        }
    }

}

