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
package io.lettuce.core.internal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class AbstractInvocationHandlerUnitTests {

    @Test
    void shouldHandleInterfaceMethod() {
    }

    @Test
    void shouldBeEqualToSelf() {
    }

    @Test
    void shouldBeNotEqualToProxiesWithDifferentInterfaces() {
    }

    private ReturnOne createProxy() {

        return (ReturnOne) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { ReturnOne.class },
                new InvocationHandler());

    }

    static class InvocationHandler extends AbstractInvocationHandler {

        @Override
        protected Object handleInvocation(Object proxy, Method method, Object[] args) {
            return 1;
        }
    }

    static interface ReturnOne {
        int returnOne();
    }

}
