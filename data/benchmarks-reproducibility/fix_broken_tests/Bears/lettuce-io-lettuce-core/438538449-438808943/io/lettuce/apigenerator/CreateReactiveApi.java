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
package io.lettuce.apigenerator;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.Type;

import io.lettuce.core.internal.LettuceSets;

/**
 * Create reactive API based on the templates.
 *
 * @author Mark Paluch
 */
@RunWith(Parameterized.class)
class CreateReactiveApi {

    private static Set<String> KEEP_METHOD_RESULT_TYPE = LettuceSets.unmodifiableSet("digest", "close", "isOpen",
            "BaseRedisCommands.reset", "getStatefulConnection", "setAutoFlushCommands", "flushCommands");

    private static Set<String> FORCE_FLUX_RESULT = LettuceSets.unmodifiableSet("eval", "evalsha", "dispatch");

    private static Set<String> VALUE_WRAP = LettuceSets.unmodifiableSet("geopos", "bitfield");

    private static final Map<String, String> RESULT_SPEC;

    static {

        Map<String, String> resultSpec = new HashMap<>();
        resultSpec.put("geopos", "Flux<Value<GeoCoordinates>>");
        resultSpec.put("bitfield", "Flux<Value<Long>>");

        RESULT_SPEC = resultSpec;
    }

    private CompilationUnitFactory factory;

    @Parameterized.Parameters(name = "Create {0}")
    static List<Object[]> arguments() {
        List<Object[]> result = new ArrayList<>();

        for (String templateName : Constants.TEMPLATE_NAMES) {
            result.add(new Object[] { templateName });
        }

        return result;
    }

    /**
     *
     * @param templateName
     */
    public CreateReactiveApi(String templateName) {

        String targetName = templateName.replace("Commands", "ReactiveCommands");
        File templateFile = new File(Constants.TEMPLATES, "io/lettuce/core/api/" + templateName + ".java");
        String targetPackage;

        if (templateName.contains("RedisSentinel")) {
            targetPackage = "io.lettuce.core.sentinel.api.reactive";
        } else {
            targetPackage = "io.lettuce.core.api.reactive";
        }

        factory = new CompilationUnitFactory(templateFile, Constants.SOURCES, targetPackage, targetName, commentMutator(),
                methodTypeMutator(), methodDeclaration -> true, importSupplier(), null, methodCommentMutator());
        factory.keepMethodSignaturesFor(KEEP_METHOD_RESULT_TYPE);
    }

    /**
     * Mutate type comment.
     *
     * @return
     */
    Function<String, String> commentMutator() {
        return s -> s.replaceAll("\\$\\{intent\\}", "Reactive executed commands").replaceAll("@since 3.0", "@since 4.0")
                + "* @generated by " + getClass().getName() + "\r\n ";
    }

    Function<Comment, Comment> methodCommentMutator() {
        return comment -> {
            if (comment != null && comment.getContent() != null) {
                comment.setContent(
                        comment.getContent().replaceAll("List&lt;(.*)&gt;", "$1").replaceAll("Set&lt;(.*)&gt;", "$1"));
            }
            return comment;
        };
    }

    /**
     * Mutate type to async result.
     *
     * @return
     */
    Function<MethodDeclaration, Type> methodTypeMutator() {
        return method -> {

            ClassOrInterfaceDeclaration declaringClass = (ClassOrInterfaceDeclaration) method.getParentNode().get();

            String baseType = "Mono";
            String typeArgument = method.getType().toString().trim();

            if (getResultType(method, declaringClass) != null) {
                typeArgument = getResultType(method, declaringClass);
            } else if (CompilationUnitFactory.contains(FORCE_FLUX_RESULT, method)) {
                baseType = "Flux";
            } else if (typeArgument.startsWith("List<")) {
                baseType = "Flux";
                typeArgument = typeArgument.substring(5, typeArgument.length() - 1);
            } else if (typeArgument.startsWith("Set<")) {
                baseType = "Flux";
                typeArgument = typeArgument.substring(4, typeArgument.length() - 1);
            } else {
                baseType = "Mono";
            }

            if (CompilationUnitFactory.contains(VALUE_WRAP, method)) {
                typeArgument = String.format("Value<%s>", typeArgument);
            }

            return CompilationUnitFactory.createParametrizedType(baseType, typeArgument);
        };
    }



    private String getResultType(MethodDeclaration method,
            ClassOrInterfaceDeclaration classOfMethod) {

        if(RESULT_SPEC.containsKey(method.getName())){
            return RESULT_SPEC.get(method.getName());
        }

        String key = classOfMethod.getName() + "." + method.getName();

        if(RESULT_SPEC.containsKey(key)){
            return RESULT_SPEC.get(key);
        }

        return null;
    }


    /**
     * Supply additional imports.
     *
     * @return
     */
    Supplier<List<String>> importSupplier() {
        return () -> Arrays.asList("reactor.core.publisher.Flux", "reactor.core.publisher.Mono");
    }

    @Test
    void createInterface() throws Exception {
    }
}
