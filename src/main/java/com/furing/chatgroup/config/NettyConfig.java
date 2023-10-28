package com.furing.chatgroup.config;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author furing
 */
public class NettyConfig {

    /**
     * 定义全局单例的channel组，管理所有的channel
     */
    private static volatile ChannelGroup channelGroup = null;

    /**
     * 用户id与通道对应消息，用于给指定用户发送消息
     */
    private static volatile ConcurrentHashMap<Long, Channel> channelMap = null;

    private static final Object CHANNEL_GROUP_LOCK = new Object();
    private static final Object CHANNEL_MAP_LOCK = new Object();

    public static ChannelGroup getChannelGroup() {
        if (Objects.isNull(channelGroup)) {
            synchronized (CHANNEL_GROUP_LOCK) {
                if (Objects.isNull(channelGroup)) {
                    // GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
                    channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                }
            }
        }
        return channelGroup;
    }

    public static ConcurrentHashMap<Long, Channel> getChannelMap() {
        if (Objects.isNull(channelMap)) {
            synchronized (CHANNEL_MAP_LOCK) {
                if (Objects.isNull(channelMap)) {
                    channelMap = new ConcurrentHashMap<>();
                }
            }
        }
        return channelMap;
    }
}
