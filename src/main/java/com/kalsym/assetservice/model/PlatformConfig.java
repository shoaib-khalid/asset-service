// package com.kalsym.assetservice.model;

// import java.io.Serializable;

// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.Id;
// import javax.persistence.Table;

// import com.fasterxml.jackson.annotation.JsonInclude;

// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;

// @Entity
// @Table(name = "platform_config")
// @Getter
// @Setter
// @ToString
// @NoArgsConstructor
// @JsonInclude(JsonInclude.Include.ALWAYS)

// public class PlatformConfig implements Serializable {

//     // @Id
//     // private String platformId;

//     // private String platformName;

//     // private String platformLogo;

//     // private String platformLogoDark;

//     // private String platformFavIcon;

//     // private String platformType;

//     // private String platformCountry;

//     // private String domain;
    
//     // private String gaCode;
    
//     // private String platformFavIcon32;


//     @Id
//     @Column(name="platformId")
//     private String platformId;

//     @Column(name="platformName")
//     private String platformName;

//     @Column(name="platformLogo")
//     private String platformLogo;

//     @Column(name="platformLogoDark")
//     private String platformLogoDark;

//     @Column(name="platformFavIcon")
//     private String platformFavIcon;

//     @Column(name="platformType")
//     private String platformType;

//     @Column(name="platformCountry")
//     private String platformCountry;

//     @Column(name="domain")
//     private String domain;

//     @Column(name="gaCode")
//     private String gaCode;

//     @Column(name="platformFavIcon32")
//     private String platformFavIcon32;
// }
