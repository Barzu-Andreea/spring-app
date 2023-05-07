package com.example.AzureDemo.Controllers;

import com.example.AzureDemo.Entities.Person;
import com.example.AzureDemo.Entities.UploadFileDto;
import com.example.AzureDemo.Repositories.PersonRepository;
import com.example.AzureDemo.Services.IUploadFileService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class AppController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private IUploadFileService iUploadFileService;

    @PostMapping ("/upload-file")
    public ResponseEntity<String> uploadFileBlobStorage(@RequestBody UploadFileDto uploadFileDto){
        String resultService = iUploadFileService.uploadFileAzure(uploadFileDto);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<Person> list(){
        return personRepository.findAll();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Person person){
        personRepository.save(person);
    }

    @GetMapping(path ="/login")
    public String login
            (@RequestParam String username, @RequestParam String password){
        if (!personRepository.findByUsernameAndPassword(username,password).isEmpty())
            return "logged in successfully";

        return "you are not logged in, please register first";

    }
    @GetMapping("/")
    public String home() {
        return "Hello from Azure App Service ";
    }

    @GetMapping(value = "/weather")
    public JsonNode weather (){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://atlas.microsoft.com/weather/currentConditions/json?api-version=1.0&query=47.165222, 27.604736&subscription-key=UwC-YUGM77leO589nwXnaesMfJVxea7ib3shhvKJWCM";
        JsonNode information = restTemplate.getForObject(url,JsonNode.class);
        return information;
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


    @PostMapping("/translator")
    public String translate(@RequestBody String text) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String key = "9396cc5a3b074b648ba65caa77ab203c";
        String endpoint = "https://api.cognitive.microsofttranslator.com";
        String route = "/translate?api-version=3.0&to=ro";
        String url = endpoint.concat(route);
        String location = "westeurope";
        String requestBody = "[{\"Text\": \"" + text + "\"}]";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Ocp-Apim-Subscription-Key", key)
                .header("Ocp-Apim-Subscription-Region", location)
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
