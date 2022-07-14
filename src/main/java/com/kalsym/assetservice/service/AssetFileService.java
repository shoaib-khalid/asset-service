package com.kalsym.assetservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.kalsym.assetservice.model.AssetFile;
import com.kalsym.assetservice.utility.LogUtil;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Transparency;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import java.awt.*;
// import java.awt.*;

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
                System.out.println("HELLLLOOOOOOOOOO scratchImage==============");
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
            System.out.println("HELLLLOOOOOOOOOO g2==============");

        }
    
        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
            System.out.println("HELLLLOOOOOOOOOO targetWidth==============");

        }
    
        return ret;
    
    }

    //https://www.baeldung.com/java-resize-image
    public BufferedImage resizeImageJpg(BufferedImage originalImage, Integer targetWidth, Integer targetHeight) throws IOException {
      
        Integer targetedWidth = targetWidth == null ? originalImage.getWidth() : targetWidth;

        Integer targetedHeight = targetHeight == null ? originalImage.getHeight() : targetHeight;

        BufferedImage resizedImage = new BufferedImage(targetedWidth, targetedHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetedWidth, targetedHeight, null);

        graphics2D.dispose();
        return resizedImage;
    }
    
    public BufferedImage resizeImagePng(BufferedImage originalImage, Integer targetWidth, Integer targetHeight) throws IOException {
      
        Integer targetedWidth = targetWidth == null ? originalImage.getWidth() : targetWidth;

        Integer targetedHeight = targetHeight == null ? originalImage.getHeight() : targetHeight;

        BufferedImage resizedImage = new BufferedImage(targetedWidth, targetedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        //use this if you want to have smooth image
        Image tmp = originalImage.getScaledInstance(targetedWidth, targetedHeight, Image.SCALE_SMOOTH);
        graphics2D.drawImage(tmp, 0, 0, targetedWidth, targetedHeight, null);
        // graphics2D.drawImage(originalImage, 0, 0, targetedWidth, targetedHeight, null);

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
                //split string for first word only
                String[] splitString = file.getAbsolutePath().split(rootFolderName,2);
                String relativePathSplit = splitString[1];
                String relativePath = relativePathSplit.replace("\\", "/");
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
    
    public ByteArrayOutputStream compressedImage(BufferedImage originalImage,Float compressValue, String fileType) throws IOException {
      
        ByteArrayOutputStream compressed = new ByteArrayOutputStream();

        try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressed)) {
                
        
            //https://stackoverflow.com/questions/28439136/java-image-compression-for-any-image-formatjpg-png-gif

            // Obtain writer for JPEG format
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("JPEG").next();
                
            // Configure JPEG compression: 70% quality 0.7f)
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();

            //if file format is jpeg we can use this one ;else we need to do some logic for image colour not same for png file
            if(fileType.equals("jpg")){
            
                jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpgWriteParam.setCompressionQuality(compressValue);
                jpgWriter.setOutput(outputStream);
                jpgWriter.write(null, new IIOImage(originalImage, null, null), jpgWriteParam);
                jpgWriter.dispose();

            }
            else{

                BufferedImage newBufferedImage = new BufferedImage(
                originalImage.getWidth(), // Returns the width of the BufferedImage.
                originalImage.getHeight(),  // Returns the height of the BufferedImage.
                BufferedImage.TYPE_INT_BGR);//TYPE_INT_BGR || TYPE_INT_ARGB   BufferedImage.TYPE_INT_BGR

                // Image tmp = originalImage.getScaledInstance(originalImage.getWidth(), originalImage.getHeight(), Image.SCALE_SMOOTH);

                newBufferedImage.createGraphics()
                .drawImage(newBufferedImage, 0, 0, Color.white, null);

                //fix colour  
                int[] rgb = originalImage.getRGB(0, 0, originalImage.getWidth(), originalImage.getHeight(), null, 0, originalImage.getWidth());
                newBufferedImage.setRGB(0, 0, originalImage.getWidth(), originalImage.getHeight(), rgb, 0, originalImage.getWidth());

                //fix colour 
                // newBufferedImage = this.fixBadImageJPEG(newBufferedImage);
                newBufferedImage.flush();

                jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpgWriteParam.setCompressionQuality(compressValue);

                jpgWriter.setOutput(outputStream);
                jpgWriter.write(null, new IIOImage(newBufferedImage, null, null), jpgWriteParam);
                jpgWriter.dispose();

            }

        }


        return compressed;

    }

    //https://stackoverflow.com/questions/9340569/jpeg-image-with-wrong-colors

    public BufferedImage fixBadImageJPEG(BufferedImage img)
    {
        int[] ary = new int[img.getWidth() * img.getHeight()];
        img.getRGB(0, 0, img.getWidth(), img.getHeight(), ary, 0, img.getWidth());
        for (int i = ary.length - 1; i >= 0; i--)
        {
            int y = ary[i] >> 16 & 0xFF; // Y
            int b = (ary[i] >> 8 & 0xFF) - 128; // Pb
            int r = (ary[i] & 0xFF) - 128; // Pr

            int g = (y << 8) + -88 * b + -183 * r >> 8; //
            b = (y << 8) + 454 * b >> 8;
            r = (y << 8) + 359 * r >> 8;

            if (r > 255)
                r = 255;
            else if (r < 0) r = 0;
            if (g > 255)
                g = 255;
            else if (g < 0) g = 0;
            if (b > 255)
                b = 255;
            else if (b < 0) b = 0;

            ary[i] = 0xFF000000 | (r << 8 | g) << 8 | b;
        }
        img.setRGB(0, 0, img.getWidth(), img.getHeight(), ary, 0, img.getWidth());
        return img;
    }

    // public boolean containsTransparency(BufferedImage image){
    //     for (int i = 0; i < image.getHeight(); i++) {
    //         for (int j = 0; j < image.getWidth(); j++) {
    //             if (isTransparent(image, j, i)){
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }

    // public boolean isTransparent(BufferedImage image, int x, int y ) {
    //     int pixel = image.getRGB(x,y);
    //     return (pixel>>24) == 0x00;
    // }


}
