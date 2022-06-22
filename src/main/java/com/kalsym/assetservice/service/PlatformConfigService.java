// package com.kalsym.assetservice.service;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.kalsym.assetservice.model.PlatformConfig;
// import com.kalsym.assetservice.repository.PlatformConfigRepository;

// @Service
// public class PlatformConfigService {
    
//     @Autowired
//     PlatformConfigRepository platformConfigRepository; 

//     // READ
//     public List<PlatformConfig> getPlatformConfigs() {
        
//         List<PlatformConfig> result =platformConfigRepository.findAll();
//         System.out.println("Checking body :::"+result);

//         return result;
//     }

//     // READ by id
//     public Optional<PlatformConfig> getPlatformConfigId(String platformId){
//     return platformConfigRepository.findById(platformId);
//     }
// }
