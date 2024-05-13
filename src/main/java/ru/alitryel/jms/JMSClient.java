package ru.alitryel.jms;

import com.ibm.mq.jms.*;

import javax.jms.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JMSClient {
    private MQQueueConnection mqConn;
    private MQQueueSession mqQueueSession;
    private Queue inputQueue;
    private Queue outputQueue;
    private MQQueueReceiver mqQueueReceiver;
    private MQQueueSender mqQueueSender;
    private boolean isListening = false;

    public JMSClient(String hostName, int port, String queueManager, String channel, String inputQueueName, String outputQueueName) throws JMSException {
        MQQueueConnectionFactory mqCF = JMSConnectionFactoryBuilder.buildConnectionFactory(hostName, port, queueManager, channel);

        mqConn = (MQQueueConnection) mqCF.createQueueConnection();
        mqQueueSession = (MQQueueSession) mqConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        inputQueue = mqQueueSession.createQueue(inputQueueName);
        outputQueue = mqQueueSession.createQueue(outputQueueName);

        mqQueueReceiver = (MQQueueReceiver) mqQueueSession.createReceiver(inputQueue);
        mqQueueSender = (MQQueueSender) mqQueueSession.createSender(outputQueue);
    }

    public void startListening() throws JMSException {
        if (!isListening) {
            isListening = true;
            mqConn.start();
            mqQueueReceiver.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        String msgText = textMessage.getText();
                        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                        System.out.println("[MQ-OUT | " + timestamp + "] " + msgText);
                        sendMessageToMQOUT("[MQ-OUT | " + timestamp + "] " + msgText);
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void stopListening() throws JMSException {
        if (isListening) {
            isListening = false;
            mqConn.stop();
            mqConn.close();
        }
    }

    private void sendMessageToMQOUT(String text) throws JMSException {
        TextMessage message = mqQueueSession.createTextMessage(text);
        mqQueueSender.send(message);
    }
}
