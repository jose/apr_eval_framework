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
package io.lettuce.core.cluster.models.partitions;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisURI;

/**
 * @author Mark Paluch
 */
class PartitionsUnitTests {

    private RedisClusterNode node1 = new RedisClusterNode(RedisURI.create("localhost", 6379), "a", true, "", 0, 0, 0,
            Arrays.asList(1, 2, 3), new HashSet<>());
    private RedisClusterNode node2 = new RedisClusterNode(RedisURI.create("localhost", 6380), "b", true, "", 0, 0, 0,
            Arrays.asList(4, 5, 6), new HashSet<>());

    @Test
    void contains() {
    }

    @Test
    void containsUsesReadView() {
    }

    @Test
    void containsAll() {
    }

    @Test
    void containsAllUsesReadView() {
    }

    @Test
    void add() {
    }

    @Test
    void addPartitionClearsCache() {
    }

    @Test
    void addAll() {
    }

    @Test
    void getPartitionBySlot() {
    }

    @Test
    void getPartitionByAlias() {
    }

    @Test
    void remove() {
    }

    @Test
    void removeAll() {
    }

    @Test
    void clear() {
    }

    @Test
    void retainAll() {
    }

    @Test
    void toArray() {
    }

    @Test
    void toArrayUsesReadView() {
    }

    @Test
    void toArray2() {
    }

    @Test
    void toArray2UsesReadView() {
    }

    @Test
    void getPartitionByNodeId() {
    }

    @Test
    void reload() {
    }

    @Test
    void reloadEmpty() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void size() {
    }

    @Test
    void sizeUsesReadView() {
    }

    @Test
    void getPartition() {
    }

    @Test
    void iterator() {
    }

    @Test
    void iteratorUsesReadView() {
    }

    @Test
    void iteratorIsSafeDuringUpdate() {
    }

    @Test
    void testToString() {
    }
}
