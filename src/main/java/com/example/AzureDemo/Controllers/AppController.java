package com.example.AzureDemo.Controllers;

import com.example.AzureDemo.Entities.Person;
import com.example.AzureDemo.Entities.UploadFileDto;
import com.example.AzureDemo.Repositories.PersonRepository;
import com.example.AzureDemo.Services.IAppService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
public class AppController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private IAppService iAppService;

    @GetMapping("/")
    public String home() {
        return "Hello from Azure App Service ";
    }

    @PostMapping ("/upload-file")
    public ResponseEntity<String> uploadFileBlobStorage(@RequestBody UploadFileDto uploadFileDto){
        String resultService = iAppService.uploadFileAzure(uploadFileDto);
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

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password){
        if (!personRepository.findByUsernameAndPassword(username,password).isEmpty())
            return "logged in successfully";
        return "you are not logged in, please register first";

    }

    @GetMapping("/location")
    public JsonNode location (@RequestBody String location){
        JsonNode information = iAppService.getLocation(location);
        return information;
    }

    @GetMapping("/weather")
    public JsonNode weather (@RequestBody JsonNode address){
        JsonNode information = iAppService.getWeather(address);
        return information;
    }

    @PostMapping("/translator")
    public String translate(@RequestBody String text) throws IOException, InterruptedException{
        String resultService = iAppService.translateTest(text);
        return resultService;
    }


}
