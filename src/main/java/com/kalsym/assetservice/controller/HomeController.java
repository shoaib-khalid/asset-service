package com.kalsym.assetservice.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.kalsym.assetservice.model.AssetFile;
import com.kalsym.assetservice.service.AssetFileService;
import com.kalsym.assetservice.utility.HttpResponse;

import org.springframework.ui.Model;


@Controller
public class HomeController {

    @Autowired
    AssetFileService assetFileService;

    @Value("${asset.file.path}")
    String filePath;

    /// Get All file asset
    @RequestMapping("/file-listing")
    public String welcome(Model model) {

        List<AssetFile> fileArrayList= assetFileService.fileListing(filePath);

        model.addAttribute("index", fileArrayList);		

        return "index";
    }
    
}
