package xyz.spoonmap.server.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

public class MultipartBuilder {

    public  static MockMultipartHttpServletRequestBuilder build(String url, String method) {
        MockMultipartHttpServletRequestBuilder multipartBuilder = multipart(url);
        multipartBuilder.with(request -> {
            request.setMethod(method);
            return request;
        });
        return multipartBuilder;
    }

}
