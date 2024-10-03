/*
 * Copyright 2011-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.pubsub;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.lettuce.core.*;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.internal.LettuceFactories;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.test.Delay;
import io.lettuce.test.Futures;
import io.lettuce.test.Wait;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.resource.TestClientResources;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
class PubSubCommandTest extends AbstractRedisClientTest implements RedisPubSubListener<String, String> {

    private RedisPubSubAsyncCommands<String, String> pubsub;

    private BlockingQueue<String> channels;
    private BlockingQueue<String> patterns;
    private BlockingQueue<String> messages;
    private BlockingQueue<Long> counts;

    private String channel = "channel0";
    private String pattern = "channel*";
    private String message = "msg!";

    @BeforeEach
    void openPubSubConnection() {
        try {
            pubsub = client.connectPubSub().async();
            pubsub.getStatefulConnection().addListener(this);
        } finally {
            channels = LettuceFactories.newBlockingQueue();
            patterns = LettuceFactories.newBlockingQueue();
            messages = LettuceFactories.newBlockingQueue();
            counts = LettuceFactories.newBlockingQueue();
        }
    }

    @AfterEach
    void closePubSubConnection() {
        if (pubsub != null) {
            pubsub.getStatefulConnection().close();
        }
    }

    @Test
    void auth() {
    }

    @Test
    void authWithReconnect() {
    }

    private long findNamedClient(String name) {

        Pattern pattern = Pattern.compile(".*id=(\\d+).*name=" + name + ".*", Pattern.MULTILINE);
        String clients = redis.clientList();
        Matcher matcher = pattern.matcher(clients);

        if (!matcher.find()) {
            throw new IllegalStateException("Cannot find PubSub client in: " + clients);
        }

        return Long.parseLong(matcher.group(1));
    }

    @Test
    void message() throws Exception {
    }

    @Test
    void pipelinedMessage() throws Exception {
    }

    @Test
    void pmessage() throws Exception {
    }

    @Test
    void pipelinedSubscribe() throws Exception {
    }

    @Test
    void psubscribe() throws Exception {
    }

    @Test
    void psubscribeWithListener() throws Exception {
    }

    @Test
    void pubsubEmptyChannels() {
    }

    @Test
    void pubsubChannels() {
    }

    @Test
    void pubsubMultipleChannels() {
    }

    @Test
    void pubsubChannelsWithArg() {
    }

    @Test
    void pubsubNumsub() {
    }

    @Test
    void pubsubNumpat() {
    }

    @Test
    void punsubscribe() throws Exception {
    }

    @Test
    void subscribe() throws Exception {
    }

    @Test
    void unsubscribe() throws Exception {
    }

    @Test
    void pubsubCloseOnClientShutdown() {
    }

    @Test
    void utf8Channel() throws Exception {
    }

    @Test
    void resubscribeChannelsOnReconnect() throws Exception {
    }

    @Test
    void resubscribePatternsOnReconnect() throws Exception {
    }

    @Test
    void adapter() throws Exception {
    }

    @Test
    void removeListener() throws Exception {
    }

    @Test
    void pingNotAllowedInSubscriptionState() {
    }

    // RedisPubSubListener implementation

    @Override
    public void message(String channel, String message) {
        channels.add(channel);
        messages.add(message);
    }

    @Override
    public void message(String pattern, String channel, String message) {
        patterns.add(pattern);
        channels.add(channel);
        messages.add(message);
    }

    @Override
    public void subscribed(String channel, long count) {
        channels.add(channel);
        counts.add(count);
    }

    @Override
    public void psubscribed(String pattern, long count) {
        patterns.add(pattern);
        counts.add(count);
    }

    @Override
    public void unsubscribed(String channel, long count) {
        channels.add(channel);
        counts.add(count);
    }

    @Override
    public void punsubscribed(String pattern, long count) {
        patterns.add(pattern);
        counts.add(count);
    }
}
