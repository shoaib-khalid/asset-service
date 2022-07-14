package com.kalsym.assetservice.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class AssetFile {
   
    public String fileName;
    public String filePath;    
    public double size; //kb
    public String fileType;   
    // public String assetUrl;
    public String relativePath;   
    public Boolean isFile;  
    
    // public double getSize() {
  
    //     return size/1000;
    // }

    public void setSize(double num) {
		this.size = num/1000;
	}





}
