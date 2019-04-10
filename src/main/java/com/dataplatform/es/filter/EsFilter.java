package com.dataplatform.es.filter;

/**
 * Created by czx on 3/29/19.
 */

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataplatform.es.Util.UuidUtils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Configuration
public class EsFilter extends OncePerRequestFilter {



    @Override
    public void destroy() {
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String uuid = UuidUtils.generateUUID();




        ContentCachingRequestWrapper wrapperRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrapperResponse = new ContentCachingResponseWrapper(response);


        chain.doFilter(wrapperRequest, wrapperResponse);

        wrapperResponse.copyBodyToResponse();


    }






}