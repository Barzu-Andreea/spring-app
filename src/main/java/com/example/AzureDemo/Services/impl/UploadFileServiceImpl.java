package com.example.AzureDemo.Services.impl;

import com.example.AzureDemo.Entities.UploadFileDto;
import com.example.AzureDemo.Services.IUploadFileService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    @Override
    public String uploadFileAzure(UploadFileDto uploadFileDto) {
        String resultService = "";
        String storageConnectionAzure="DefaultEndpointsProtocol=https;AccountName=azurestoragecloud1;AccountKey=XosarRDtZ3j11Oh6iRqDKJsYLwd+L736li6+1L5r1iQLQrCxnxQfdSydPhmv3a+uKfAFL+xXVp9C+AStTqbeyA==;EndpointSuffix=core.windows.net";
        String nameContainer ="files";

        try{

            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionAzure);
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            CloudBlobContainer container = serviceClient.getContainerReference(nameContainer);

            CloudBlob blob;
            blob = container.getBlockBlobReference(uploadFileDto.getName());
            byte[] decodedBytes = Base64.getDecoder().decode(uploadFileDto.getFileBase64());
            blob.uploadFromByteArray(decodedBytes,0,decodedBytes.length);
            resultService = "OK";
        }catch (Exception e){

            resultService = e.getMessage();
        }
        return resultService;
    }
}
