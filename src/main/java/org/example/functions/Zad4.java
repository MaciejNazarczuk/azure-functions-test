package org.example.functions;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.DoubleStream;

public class Zad4 {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("zad4")
    @StorageAccount("AzureWebJobsStorage")
    
    public void copyBlobHttp(
        @BlobTrigger(
            name = "triggerBlob",
            path = "myblob/zad3")
            String triggerBlob,
        @BlobOutput(
            name = "target",
            path = "myblob/zad4")
            OutputBinding<String> outputItem,
        ExecutionContext context) {
        
        try {
            Double sum = Arrays.stream(triggerBlob.split("\n"))
                .map(Double::parseDouble)
                .reduce(Double::sum)
                .orElseThrow(RuntimeException::new);
    
            context.getLogger().info("blob trigger!!! " + triggerBlob);
            context.getLogger().info(("sum: " + sum));
            
            outputItem.setValue(sum.toString());
        } catch (Exception e) {
            context.getLogger().info("error");
            outputItem.setValue("error");
        }
        
    }
}
