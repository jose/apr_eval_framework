/*
 * Copyright 2014-2017 the original author or authors.
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
package org.springframework.data.jpa.repository.query;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.object.IsCompatibleType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.repository.query.Param;
import org.springframework.util.ReflectionUtils;

/**
 * Unit tests for {@link StoredProcedureAttributeSource}.
 * 
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @since 1.6
 */
@RunWith(MockitoJUnitRunner.class)
public class StoredProcedureAttributeSourceUnitTests {

	StoredProcedureAttributeSource creator;
	@Mock JpaEntityMetadata<User> entityMetadata;

	@Before
	public void setup() {

		creator = StoredProcedureAttributeSource.INSTANCE;

		when(entityMetadata.getJavaType()).thenReturn(User.class);
		when(entityMetadata.getEntityName()).thenReturn("User");
	}

	@Test // DATAJPA-455
	public void shouldCreateStoredProcedureAttributesFromProcedureMethodWithImplicitProcedureName() {
	}

	@Test // DATAJPA-455
	public void shouldCreateStoredProcedureAttributesFromProcedureMethodWithExplictName() {
	}

	@Test // DATAJPA-455
	public void shouldCreateStoredProcedureAttributesFromProcedureMethodWithExplictProcedureNameValue() {
	}

	@Test // DATAJPA-455
	public void shouldCreateStoredProcedureAttributesFromProcedureMethodWithExplictProcedureNameAlias() {
	}

	@Test // DATAJPA-455
	public void shouldCreateStoredProcedureAttributesFromProcedureMethodBackedWithExplicitlyNamedProcedure() {
	}

	@Test // DATAJPA-455
	public void shouldCreateStoredProcedureAttributesFromProcedureMethodBackedWithImplicitlyNamedProcedure() {
	}

	@Test // DATAJPA-871
	public void aliasedStoredProcedure() {
	}

	@Test // DATAJPA-871
	public void aliasedStoredProcedure2() {
	}

	private static Method method(String name, Class<?>... paramTypes) {
		return ReflectionUtils.findMethod(DummyRepository.class, name, paramTypes);
	}

	/**
	 * @author Thomas Darimont
	 */
	static interface DummyRepository {

		/**
		 * Explicitly mapped to a procedure with name "plus1inout" in database.
		 */
		@Procedure("plus1inout") // DATAJPA-455
		Integer explicitlyNamedPlus1inout(Integer arg);

		/**
		 * Explicitly mapped to a procedure with name "plus1inout" in database via alias.
		 */
		@Procedure(procedureName = "plus1inout") // DATAJPA-455
		Integer explicitPlus1inoutViaProcedureNameAlias(Integer arg);

		/**
		 * Implicitly mapped to a procedure with name "plus1inout" in database via alias.
		 */
		@Procedure // DATAJPA-455
		Integer plus1inout(Integer arg);

		/**
		 * Explicitly mapped to named stored procedure "User.plus1IO" in {@link EntityManager}.
		 */
		@Procedure(name = "User.plus1IO") // DATAJPA-455
		Integer entityAnnotatedCustomNamedProcedurePlus1IO(@Param("arg") Integer arg);

		/**
		 * Implicitly mapped to named stored procedure "User.plus1" in {@link EntityManager}.
		 */
		@Procedure // DATAJPA-455
		Integer plus1(@Param("arg") Integer arg);

		@ComposedProcedureUsingAliasFor(explicitProcedureName = "plus1inout")
		Integer plus1inoutWithComposedAnnotationOverridingProcedureName(Integer arg);

		@ComposedProcedureUsingAliasFor(emProcedureName = "User.plus1")
		Integer plus1inoutWithComposedAnnotationOverridingName(Integer arg);
	}

	@Procedure
	@Retention(RetentionPolicy.RUNTIME)
	static @interface ComposedProcedureUsingAliasFor {

		@AliasFor(annotation = Procedure.class, attribute = "value")
		String dbProcedureName() default "";

		@AliasFor(annotation = Procedure.class, attribute = "procedureName")
		String explicitProcedureName() default "";

		@AliasFor(annotation = Procedure.class, attribute = "name")
		String emProcedureName() default "";

		@AliasFor(annotation = Procedure.class, attribute = "outputParameterName")
		String outParamName() default "";
	}
}
