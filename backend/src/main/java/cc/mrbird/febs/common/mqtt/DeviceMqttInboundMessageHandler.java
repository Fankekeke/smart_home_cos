package cc.mrbird.febs.common.mqtt;

import com.xzhisoft.toilet.pross.AnalysisDataBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeviceMqttInboundMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        AnalysisDataBase.analysisMessage(message.getPayload().toString());
    }

}
