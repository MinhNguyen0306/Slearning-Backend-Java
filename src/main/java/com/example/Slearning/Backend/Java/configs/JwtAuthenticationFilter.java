package com.example.Slearning.Backend.Java.configs;

import com.example.Slearning.Backend.Java.services.JWTService;
import com.example.Slearning.Backend.Java.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Lấy ra Token từ Header user truyền vào
        jwt = authHeader.substring(7);

        //Extract email từ JWT Token
        userEmail = jwtService.extractUsername(jwt);

        //Kiểm tra token có còn trong security context không nếu còn thì bỏ qua
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(userEmail);

            //Kiểm tra token hợp lệ
            if(jwtService.isTokenValid(jwt, userDetails)) {
                //Tạo ra một auth token với email và password hiện tại
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info(request.getRequestURI());
                log.info(String.valueOf(response.getStatus()));
                log.info(SecurityContextHolder.getContext().getAuthentication().getName());
            }
        }

        filterChain.doFilter(request, response);
    }
}
