/*
 * Copyright 2016-2018 the original author or authors.
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
package io.lettuce.core.dynamic.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class ParametrizedTypeInformationUnitTests {

    @Test
    void isAssignableShouldConsiderExactType() {
    }

    @Test
    void isAssignableShouldConsiderCompatibleType() {
    }

    @Test
    void isAssignableShouldConsiderWildcardOfNumberType() {
    }

    @Test
    void isAssignableShouldConsiderWildcard() {
    }

    @Test
    void returnsNullMapValueTypeForNonMapProperties() {
    }

    @Test
    void isAssignableShouldConsiderNestedParameterTypes() {
    }

    private interface Bar extends List<String> {

    }

    private static interface TestType {

        Collection<Number> collectionOfNumber();

        Collection<Iterable<Number>> collectionOfIterableOfNumber();

        List<Number> exactNumber();

        List<?> anything();

        List<? extends Number> numberOrSubtype();
    }

    private static interface ListOfNumber extends List<Number> {

    }

    static interface ListOfIterableOfNumber extends List<Iterable<Number>> {

    }

    private static interface ListOfSetOfNumber extends List<Set<Number>> {

    }

    private static interface ListOfIterableOfInteger extends List<Iterable<Integer>> {

    }

    private static interface ListOfListOfNumber extends List<List<Number>> {

    }

    private static interface ListOfString extends List<String> {

    }

    private static interface ListOfInteger extends List<Integer> {

    }
}
