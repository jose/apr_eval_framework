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
package io.lettuce.core.output;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import io.lettuce.core.GeoCoordinates;
import io.lettuce.core.GeoWithin;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.Utf8StringCodec;

/**
 * @author Mark Paluch
 */
class GeoWithinListOutputUnitTests {

    private GeoWithinListOutput<String, String> sut = new GeoWithinListOutput<>(new Utf8StringCodec(), false, false, false);

    @Test
    void defaultSubscriberIsSet() {
    }

    @Test
    void commandOutputKeyOnlyDecoded() {
    }

    @Test
    void commandOutputKeyAndDistanceDecoded() {
    }

    @Test
    void commandOutputKeyAndHashDecoded() {
    }

    @Test
    void commandOutputLongKeyAndHashDecoded() {
    }

    @Test
    void commandOutputKeyAndCoordinatesDecoded() {
    }
}
