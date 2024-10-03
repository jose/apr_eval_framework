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
package io.lettuce.core.dynamic.intercept;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class InvocationProxyFactoryUnitTests {

    @Test
    void shouldDelegateCallsToInterceptor() {
    }

    @Test
    void shouldNotFailWithoutFurtherInterceptors() {
    }

    @Test
    void shouldCallInterceptorsInOrder() {
    }

    private interface TargetWithBooleanMethod {

        Boolean someMethod();
    }

    private static class ReturnValue implements MethodInterceptor {

        private final Object value;

        ReturnValue(Object value) {
            this.value = value;
        }

        @Override
        public Object invoke(MethodInvocation invocation) {
            return value;
        }
    }

    private interface TargetWithStringMethod {

        String run();
    }

    private static class StringAppendingMethodInterceptor implements MethodInterceptor {

        private final String toAppend;

        StringAppendingMethodInterceptor(String toAppend) {
            this.toAppend = toAppend;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return invocation.proceed().toString() + toAppend;
        }
    }
}
