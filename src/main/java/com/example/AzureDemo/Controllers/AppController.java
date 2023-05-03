package com.example.AzureDemo.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@RestController
public class AppController {

    @GetMapping("/")
    public String home() {
        return "Hello from Andreea ";
    }
    
    @GetMapping("/maps")
    public void getMapImage(HttpServletResponse response) throws IOException {
        String imageUrl = "https://atlas.microsoft.com/map/static/png?api-version=1.0&style=main&layer=basic&zoom=14&height=700&Width=700&center=-122.13230609893799,47.64599069048016&path=lcFF0000|lw2|la0.60|ra1000||-122.13230609893799%2047.64599069048016&pins=default|la15+50|al0.66|lc003C62|co002D62||%27Microsoft%20Corporate%20Headquarters%27-122.14131832122801%20%2047.64690503939462|%27Microsoft%20Visitor%20Center%27-122.136828%2047.642224|%27Microsoft%20Conference%20Center%27-122.12552547454833%2047.642940335653996|%27Microsoft%20The%20Commons%27-122.13687658309935%20%2047.64452336193245&subscription-key=UwC-YUGM77leO589nwXnaesMfJVxea7ib3shhvKJWCM";

        URL url = new URL(imageUrl);
        URLConnection conn = url.openConnection();
        conn.connect();

        InputStream in = conn.getInputStream();

        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=map.png");

        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            response.getOutputStream().write(buffer, 0, len);
        }
        in.close();
    }
}
