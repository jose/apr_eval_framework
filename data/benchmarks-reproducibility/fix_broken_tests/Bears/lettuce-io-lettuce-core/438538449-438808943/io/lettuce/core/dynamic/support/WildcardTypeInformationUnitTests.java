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

import static io.lettuce.core.dynamic.support.ClassTypeInformation.from;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class WildcardTypeInformationUnitTests {

    @Test
    void shouldResolveWildcardType() {
    }

    @Test
    void isAssignableFromExactType() {
    }

    @Test
    void isAssignableFromCompatibleFirstLevelType() {
    }

    @Test
    void isAssignableFromCompatibleComponentType() {
    }

    @Test
    void isAssignableFromUpperBoundComponentType() {
    }

    @Test
    void isAssignableFromLowerBoundComponentType() {
    }

    TypeInformation<?> componentTypeOf(String name) {
        return ClassTypeInformation.fromReturnTypeOf(methodOf(name)).getComponentType();
    }

    Method methodOf(String name) {
        return ReflectionUtils.findMethod(GenericReturnTypes.class, name);
    }

    private static interface GenericReturnTypes {

        List<Number> exactNumber();

        List<?> listOfAnything();

        List<?> anotherListOfAnything();

        Collection<?> collectionOfAnything();

        List<? super Integer> atMostInteger();

        List<Float> exactFloat();

        List<? extends Number> atLeastNumber();
    }
}
