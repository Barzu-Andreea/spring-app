package com.example.AzureDemo.Services;

import com.example.AzureDemo.Entities.UploadFileDto;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface IAppService {
    public String uploadFileAzure (UploadFileDto uploadFileDto);
    public String translateTest (String text) throws IOException, InterruptedException;
    public JsonNode getWeather(JsonNode address);
    public JsonNode getLocation(String location);
}
