package ru.alitryel;

import ru.alitryel.config.ConfigLoader;
import ru.alitryel.jms.JMSClient;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            Properties prop = ConfigLoader.loadProperties("config.properties");

            String hostName = prop.getProperty("hostName");
            int port = Integer.parseInt(prop.getProperty("port"));
            String queueManager = prop.getProperty("queueManager");
            String channel = prop.getProperty("channel");
            String inputQueueName = prop.getProperty("inputQueueName");
            String outputQueueName = prop.getProperty("outputQueueName");

            JMSClient mqInClient = new JMSClient(hostName, port, queueManager, channel, inputQueueName, outputQueueName);
            mqInClient.startListening();

            Thread.sleep(Long.parseLong(prop.getProperty("timeWork")));

            mqInClient.stopListening();

        } catch (InterruptedException | JMSException | IOException e) {
            e.printStackTrace();
        }
    }
}
