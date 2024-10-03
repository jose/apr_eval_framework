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
package io.lettuce.core.masterslave;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.lettuce.core.AbstractRedisClientTest;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.models.role.RedisInstance;
import io.lettuce.core.models.role.RedisNodeDescription;
import io.lettuce.core.models.role.RoleParser;
import io.lettuce.test.Futures;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
class MasterSlaveTest extends AbstractRedisClientTest {

    private RedisURI masterURI = RedisURI.Builder.redis(host, TestSettings.port(3)).withPassword(passwd)
            .withClientName("my-client").withDatabase(5).build();

    private StatefulRedisMasterSlaveConnectionImpl<String, String> connection;
    private RedisAsyncCommands<String, String> connectionToNode1;
    private RedisAsyncCommands<String, String> connectionToNode2;

    private RedisURI master;
    private RedisURI slave;

    @BeforeEach
    void before() throws Exception {

        RedisURI node1 = RedisURI.Builder.redis(host, TestSettings.port(3)).withDatabase(2).build();
        RedisURI node2 = RedisURI.Builder.redis(host, TestSettings.port(4)).withDatabase(2).build();

        connectionToNode1 = client.connect(node1).async();
        connectionToNode2 = client.connect(node2).async();

        RedisInstance node1Instance = RoleParser.parse(connectionToNode1.role().get(2, TimeUnit.SECONDS));
        RedisInstance node2Instance = RoleParser.parse(connectionToNode2.role().get(2, TimeUnit.SECONDS));

        if (node1Instance.getRole() == RedisInstance.Role.MASTER && node2Instance.getRole() == RedisInstance.Role.SLAVE) {
            master = node1;
            slave = node2;
        } else if (node2Instance.getRole() == RedisInstance.Role.MASTER
                && node1Instance.getRole() == RedisInstance.Role.SLAVE) {
            master = node2;
            slave = node1;
        } else {
            assumeTrue(false, String.format(
                    "Cannot run the test because I don't have a distinct master and slave but %s and %s", node1Instance,
                    node2Instance));
        }

        connectionToNode1.configSet("requirepass", passwd);
        connectionToNode1.configSet("masterauth", passwd);
        connectionToNode1.auth(passwd);

        connectionToNode2.configSet("requirepass", passwd);
        connectionToNode2.configSet("masterauth", passwd);
        connectionToNode2.auth(passwd);

        connection = (StatefulRedisMasterSlaveConnectionImpl) MasterSlave.connect(client, new Utf8StringCodec(), masterURI);
        connection.setReadFrom(ReadFrom.SLAVE);
    }

    @AfterEach
    void after() {

        if (connectionToNode1 != null) {
            connectionToNode1.configSet("requirepass", "");
            Futures.await(connectionToNode1.configSet("masterauth", ""));
            connectionToNode1.getStatefulConnection().close();
        }

        if (connectionToNode2 != null) {
            connectionToNode2.configSet("requirepass", "");
            Futures.await(connectionToNode2.configSet("masterauth", ""));
            connectionToNode2.getStatefulConnection().close();
        }

        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testMasterSlaveReadFromMaster() {
    }

    @Test
    void testMasterSlaveReadFromSlave() {
    }

    @Test
    void testMasterSlaveReadWrite() {
    }

    @Test
    void testConnectToSlave() {
    }

    @Test
    void noSlaveForRead() {
    }

    @Test
    void masterSlaveConnectionShouldSetClientName() {
    }

    @Test
    void testConnectionCount() {
    }

    @Test
    void testReconfigureTopology() {
    }

    static String slaveCall(StatefulRedisMasterSlaveConnection<String, String> connection) {
        return connection.sync().info("replication");
    }

    MasterSlaveConnectionProvider getConnectionProvider() {
        MasterSlaveChannelWriter writer = connection.getChannelWriter();
        return writer.getMasterSlaveConnectionProvider();
    }
}
