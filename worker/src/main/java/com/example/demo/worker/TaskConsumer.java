package com.example.demo.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.grpc.Request;
import com.example.demo.grpc.Response;
import com.example.demo.grpc.ServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
@Service
public class TaskConsumer {
    private static final int MAX_LOOP = 100;
    private static final Logger log = LoggerFactory.getLogger(TaskConsumer.class);

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
            .usePlaintext()
            .build();
    
    ServiceGrpc.ServiceBlockingStub client = ServiceGrpc.newBlockingStub(channel);

    @Autowired
    private TaskProducer taskProducer;

    @RabbitListener(queues = "newTasks", ackMode = "AUTO", concurrency = "1")
    public void received(Task message) throws InterruptedException, Exception   {

        log.info("Received message as generic: {}", message.toString());
        final Integer id = message.getId();

        final Request request = Request.newBuilder()
                                        .setText(message.getText())
                                        .setId(message.getId())
                                        .build();
 
        final Response response = client.toUpperCase(request);

        final String result = response.getResult();

        for(int i=0; i<MAX_LOOP; i++){
            Thread.sleep(1000);
            taskProducer.sendMessage(getDefaultMessage(i, id));
        }
        taskProducer.sendMessage(getFinalMessage(result, id));
    }

    private TaskMessage getFinalMessage(final String result, final Integer id) {
        return TaskMessage.builder()
                    .completed(Boolean.TRUE)
                    .result(result)
                    .id(id)
                    .progress(100)
                    .build();
    }

    private TaskMessage getDefaultMessage(int i, Integer id) {
        return TaskMessage.builder()
                    .completed(Boolean.FALSE)
                    .result("In progress...")
                    .id(id)
                    .progress(i)
                    .build();
    }
}
