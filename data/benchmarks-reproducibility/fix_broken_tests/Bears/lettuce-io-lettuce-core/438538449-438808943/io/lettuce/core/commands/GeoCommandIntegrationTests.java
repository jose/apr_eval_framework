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
package io.lettuce.core.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.condition.EnabledOnCommand;
import io.lettuce.test.condition.RedisConditions;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@EnabledOnCommand("GEOADD")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GeoCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected GeoCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void geoadd() {
    }

    @Test
    public void geoaddInTransaction() {
    }

    @Test
    void geoaddMulti() {
    }

    @Test
    public void geoaddMultiInTransaction() {
    }

    @Test
    void geoaddMultiWrongArgument() {
    }

    @Test
    void georadius() {
    }

    @Test
    public void georadiusInTransaction() {
    }

    @Test
    void georadiusWithCoords() {
    }

    @Test
    void geodist() {
    }

    @Test
    void geodistMissingElements() {
    }

    @Test
    public void geodistInTransaction() {
    }

    @Test
    public void geopos() {
    }

    @Test
    public void geoposInTransaction() {
    }

    @Test
    void georadiusWithArgs() {
    }

    @Test
    public void georadiusWithArgsAndTransaction() {
    }

    @Test
    void geohash() {
    }

    @Test
    void geohashUnknownKey() {
    }

    @Test
    public void geohashInTransaction() {
    }

    @Test
    void georadiusStore() {
    }

    @Test
    void georadiusStoreWithCountAndSort() {
    }

    @Test
    void georadiusStoreDist() {
    }

    @Test
    void georadiusStoreDistWithCountAndSort() {
    }

    @Test
    void georadiusWithNullArgs() {
    }

    @Test
    void georadiusStoreWithNullArgs() {
    }

    @Test
    void georadiusbymember() {
    }

    @Test
    void georadiusbymemberStoreDistWithCountAndSort() {
    }

    @Test
    void georadiusbymemberWithArgs() {
    }

    @Test
    public void georadiusbymemberWithArgsInTransaction() {
    }

    @Test
    void georadiusbymemberWithNullArgs() {
    }

    @Test
    void georadiusStorebymemberWithNullArgs() {
    }

    protected void prepareGeo() {
        redis.geoadd(key, 8.6638775, 49.5282537, "Weinheim");
        redis.geoadd(key, 8.3796281, 48.9978127, "EFS9", 8.665351, 49.553302, "Bahn");
    }

    private static double getY(List<GeoWithin<String>> georadius, int i) {
        return georadius.get(i).getCoordinates().getY().doubleValue();
    }

    private static double getX(List<GeoWithin<String>> georadius, int i) {
        return georadius.get(i).getCoordinates().getX().doubleValue();
    }

}
