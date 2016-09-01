package com.weitaomi.systemconfig.util;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.weitaomi.application.model.dto.MessageJPushDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by supumall on 2016/8/10.
 */
public class JpushUtils {
    private static Logger LOG= LoggerFactory.getLogger(JpushUtils.class);

    /**
     * 向客户端推送消息
     * @param message
     */
    public static void buildRequest(String message){
        JPushClient jpushClient = new JPushClient("4081bded186173ba62a4c2b9", "f8a12e0714dc3c2d9e7a8154");

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert(message);

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
        }
    }

    /**
     * 向客户端推送消息
     * @param alert
     * @param content
     */
    public static void buildRequest(String alert,String content){
        JPushClient jpushClient = new JPushClient("4081bded186173ba62a4c2b9", "f8a12e0714dc3c2d9e7a8154");

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(alert, content);

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
        }
    }

    public static PushPayload buildPushObject_all_all_alert(String content) {
        return PushPayload.alertAll(content);
    }

    public static PushPayload buildPushObject_all_alias_alert(String alias,String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(content))
                .build();
    }

    public static PushPayload buildPushObject_android_tag_alertWithTitle(String alert,String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android(alert, title, null))
                .build();
    }
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String alert,String msgContent) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(alert)
                                .setBadge(5)
                                .build())
                        .build())
                .setMessage(Message.content(msgContent))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }

    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(String alert,String msgContent) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag())
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }

    public static String getJpushMessage(Long memberId,String message){
        MessageJPushDto messageJPushDto=new MessageJPushDto();
        messageJPushDto.setMemberId(memberId);
        messageJPushDto.setMessage(message);
        return JSON.toJSONString(messageJPushDto);
    }
}
