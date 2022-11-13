package org.example.functions;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class Zad3 {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("zad3")
    @StorageAccount("AzureWebJobsStorage")
    public HttpResponseMessage copyBlobHttp(
        @HttpTrigger(name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
        @BlobInput(
            name = "file",
            dataType = "binary",
            path = "myblob/zad3")
            byte[] content,
        @BlobOutput(
            name = "target",
            path = "myblob/zad3")
            OutputBinding<String> outputItem,
        @QueueOutput(name = "queueOutput", queueName = "myqueue")
            OutputBinding<String> queueOutput,
        ExecutionContext context) {
        
        try {
            final String body = request.getBody().orElseThrow(RuntimeException::new);
            
            String oldBlob = new String(content, StandardCharsets.UTF_8);
            outputItem.setValue(oldBlob + "\n" + body);
            queueOutput.setValue(body);
    
            if ("delete".equals(body.trim())) {
                outputItem.setValue("0");
            }
            
            return request.createResponseBuilder(HttpStatus.OK)
                .body("old blob was: " + oldBlob + "\n" + "new blob is: " + outputItem.getValue() + "\n")
                .build();
        } catch (Exception e) {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("error\n" + e.getMessage())
                .build();
        }
        
    }
}
