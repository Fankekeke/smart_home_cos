package cc.mrbird.febs.common.mqtt;

import cc.mrbird.febs.cos.service.IDeviceTypeService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceMqttInboundMessageHandler implements MessageHandler {

    private final IDeviceTypeService deviceTypeService;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        if (StrUtil.isEmpty(message.getPayload().toString())) {
            return;
        }
        System.out.println("接收到MQTT消息：" + message.getPayload());
        // 解析数据
        deviceTypeService.setDeviceRecordMqtt(message.getPayload().toString());
    }

}
