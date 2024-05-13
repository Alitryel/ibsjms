package ru.alitryel.jms;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import javax.jms.JMSException;

public class JMSConnectionFactoryBuilder {
    public static MQQueueConnectionFactory buildConnectionFactory(String hostName, int port, String queueManager, String channel) throws JMSException {
        MQQueueConnectionFactory mqCF = new MQQueueConnectionFactory();
        mqCF.setHostName(hostName);
        mqCF.setPort(port);
        mqCF.setQueueManager(queueManager);
        mqCF.setChannel(channel);
        return mqCF;
    }
}
