package com.weitaomi.systemconfig.listener;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.weitaomi.application.model.dto.AddResponseTaskDto;
import com.weitaomi.application.service.interf.IOfficeAccountService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by supumall on 2016/8/10.
 */
public class AddOfficialAccountListener implements ChannelAwareMessageListener {
    @Autowired
    private IOfficeAccountService officeAccountService;
    @Autowired
    private Gson2JsonMessageConverter messageConverter;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        channel.basicQos(100);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        String jsonParams=(String)messageConverter.fromMessage(message);
        AddResponseTaskDto addResponseTaskDto= JSONObject.parseObject(jsonParams,AddResponseTaskDto.class);
        if (addResponseTaskDto!=null){
            officeAccountService.receiveAddOffical(addResponseTaskDto);
        }
    }
}
