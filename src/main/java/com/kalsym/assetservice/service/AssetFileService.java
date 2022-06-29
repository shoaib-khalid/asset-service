package com.kalsym.assetservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kalsym.assetservice.model.AssetFile;
import com.kalsym.assetservice.utility.LogUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Transparency;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


@Service
public class AssetFileService {

    @Value("${asset.file.path}")
    String filePathOri;
    
    //Progressive Bilinear scalling https://stackoverflow.com/questions/12879540/image-resizing-in-java-to-reduce-image-size
    public BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {

        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;
    
        int w = img.getWidth();
        int h = img.getHeight();
    
        int prevW = w;
        int prevH = h;
    
        do {
            if (w > targetWidth) {
                w /= 2;
                w = (w < targetWidth) ? targetWidth : w;
            }
    
            if (h > targetHeight) {
                h /= 2;
                h = (h < targetHeight) ? targetHeight : h;
            }
    
            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }
    
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
    
            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);
    
        if (g2 != null) {
            g2.dispose();
        }
    
        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }
    
        return ret;
    
    }

    //https://www.baeldung.com/java-resize-image
    public BufferedImage resizeImage(BufferedImage originalImage, Integer targetWidth, Integer targetHeight) throws IOException {
      
        Integer targetedWidth = targetWidth == null ? originalImage.getWidth() : targetWidth;

        Integer targetedHeight = targetHeight == null ? originalImage.getHeight() : targetHeight;

        BufferedImage resizedImage = new BufferedImage(targetedWidth, targetedHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetedWidth, targetedHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public List<AssetFile> fileListing(String filePath){

        //======================================
        //      A : File with detail  //https://www.tutorialspoint.com/how-to-get-list-of-all-files-folders-from-a-folder-in-java
        //======================================
    
        //Creating a File object for directory
        File directoryPath = new File(filePath);
       
        //List of all files and directories
        File filesList[] = directoryPath.listFiles();
   
        // convert array to array list
        List<File> newFileList= new ArrayList<>(Arrays.asList(filesList));

        // initialize arraylist in order to append elements 
        List<AssetFile> fileArrayList= new ArrayList<>();

        //to get root folder name
        int l = filePathOri.lastIndexOf('/');
        String rootFolderName = filePathOri.substring(l+1);
        LogUtil.info("", "ASSET FILE SERVICE", "Response with filePathOri" , filePathOri);

        LogUtil.info("", "ASSET FILE SERVICE", "Response with rootFolderName" , rootFolderName);


        for (File file : newFileList){

            //to get extension type pf file
            int i = file.getAbsolutePath().lastIndexOf('.');
            String fileType = file.getAbsolutePath().substring(i+1);

            //check if it folder type
            // if(fileType.contains("\\")){
            //     fileType = "File folder";
            // }
            if(file.isFile() == false){
                fileType = "File folder";
            }

            // String asseturlTemp = "http://symplified.it/store-assets";

            String[] splitString = file.getAbsolutePath().split(rootFolderName);
            String relativePathSplit = splitString[1];
            String relativePath = relativePathSplit.replace("\\", "/");


            //to get parent folder name
            int j = file.getParent().lastIndexOf('\\');
            String parentFolderName = file.getParent().substring(j+1);

            AssetFile af = new AssetFile();
            af.setFileName(file.getName());
            af.setFilePath(file.getAbsolutePath());
            af.setSize(file.length());
            af.setFileType(fileType);
            af.setIsFile(file.isFile());
            // af.setAssetUrl(asseturlTemp + relativePath);

            if(parentFolderName.equals(rootFolderName)){
                af.setRelativePath("/"+file.getName());
            }else{
                af.setRelativePath(relativePath);
            }
            

            fileArrayList.add(af);
            
        }

        return fileArrayList;
        
    }

    public AssetFile fileDetails(String directorypath){

        //Creating a File object for directory
        File directoryPath = new File(directorypath);

        //To get extension file
        int i = directoryPath.getAbsolutePath().lastIndexOf('.');
        String fileType = directoryPath.getAbsolutePath().substring(i+1);

        //check if it folder type
        if(fileType.contains("\\")){
            fileType = "File folder";
        }

        // String asseturlTemp = "http://symplified.it/store-assets";

        String[] splitString = directoryPath.getAbsolutePath().split("file-listing");
        String relativePath = splitString[1];
        String relativePathUrl = relativePath.replace("\\", "/");

        AssetFile af = new AssetFile();
        af.setFileName(directoryPath.getName());
        af.setFilePath(directoryPath.getAbsolutePath());
        af.setSize(directoryPath.length());
        af.setFileType(fileType);
        // af.setAssetUrl(asseturlTemp + relativePathUrl);


        return af;
    }

    public String getFileDirectoryPath(String asseturl){

        String[] splitAssetUrl = asseturl.split("store-assets");
     
        String absolutePath;
        String fileDirectory;

        if(splitAssetUrl.length == 1){
            fileDirectory = filePathOri;

        }else{
            absolutePath = splitAssetUrl[1];
            fileDirectory = filePathOri + absolutePath;
        }
    
        return fileDirectory;
    }

    public String getFolderFilePath(String relativePath){
    
        return filePathOri+relativePath;
    }
    


}
