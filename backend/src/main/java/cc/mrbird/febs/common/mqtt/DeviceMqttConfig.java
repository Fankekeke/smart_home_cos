package cc.mrbird.febs.common.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.time.Instant;

@Configuration
@IntegrationComponentScan
public class DeviceMqttConfig {

    private final DeviceMqttProperties prop;
    private final DeviceMqttInboundMessageHandler deviceMqttInboundMessageHandler;

    public DeviceMqttConfig(DeviceMqttProperties prop,
                            DeviceMqttInboundMessageHandler deviceMqttInboundMessageHandler) {
        this.prop = prop;
        this.deviceMqttInboundMessageHandler = deviceMqttInboundMessageHandler;
    }

    @Bean("toiletMqttClientFactory")
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[]{prop.getUrl()});
        mqttConnectOptions.setUserName(prop.getUsername());
        mqttConnectOptions.setPassword(prop.getPassword().toCharArray());
        // 客户端断线时暂时不清除，直到超时注销
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    @Bean("toiletMqttOutbound")
    @ServiceActivator(inputChannel = "toiletMqttOutboundChannel")
    public MessageHandler mqttOutbound(@Qualifier("toiletMqttClientFactory") MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                prop.getClientId() + "-pub-" + Instant.now().toEpochMilli(), mqttClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultRetained(false);
        messageHandler.setAsyncEvents(false);
        // Exactly Once
        messageHandler.setDefaultQos(2);
        messageHandler.setDefaultTopic(prop.getDefaultTopic());
        return messageHandler;
    }

    @Bean("toiletMqttOutboundChannel")
    public MessageChannel toiletMqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean("toiletMqttInbound")
    public MessageProducerSupport mqttInbound(@Qualifier("toiletMqttClientFactory") MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(prop.getClientId() + "-sub-" + Instant.now().toEpochMilli(), mqttClientFactory,
                        prop.getTopics());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(toiletMqttInboundChannel());
        return adapter;
    }

    @Bean("toiletInboundMessageHandler")
    @ServiceActivator(inputChannel = "toiletMqttInboundChannel")
    public MessageHandler InboundMessageHandler() {
        return deviceMqttInboundMessageHandler;
    }

    @Bean("toiletMqttInboundChannel")
    public MessageChannel toiletMqttInboundChannel() {
        return new DirectChannel();
    }
}
