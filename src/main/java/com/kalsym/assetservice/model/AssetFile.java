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


}
