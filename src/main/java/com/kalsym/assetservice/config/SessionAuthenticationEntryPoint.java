// package com.kalsym.assetservice.config;

// import java.io.IOException;
// import java.io.Serializable;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.AuthenticationEntryPoint;
// import org.springframework.stereotype.Component;

// /**
//  *
//  * @author Sarosh
//  */
// @Component
// public class SessionAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

//     @Override
//     public void commence(HttpServletRequest request, HttpServletResponse response,
//             AuthenticationException authException) throws IOException {
//         System.out.println("CHECKING SessionAuthenticationEntryPoint commence ::: ");
//         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//     }
// }
