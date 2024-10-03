/*
 * Copyright 2008-2017 the original author or authors.
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

import static java.util.Collections.*;
import static javax.persistence.TemporalType.*;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Embeddable;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

/**
 * Unit test for {@link ParameterBinder}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Jens Schauder
 */
@RunWith(MockitoJUnitRunner.class)
public class ParameterBinderUnitTests {

	public static final int MAX_PARAMETERS = 1;
	private Method valid;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS) private Query query;
	private Method useIndexedParameters;

	@Before
	public void setUp() throws SecurityException, NoSuchMethodException {

		valid = SampleRepository.class.getMethod("valid", String.class);

		useIndexedParameters = SampleRepository.class.getMethod("useIndexedParameters", String.class);

		when(query.getParameters().size()).thenReturn(MAX_PARAMETERS);
	}

	static class User {

	}

	static interface SampleRepository {

		User useIndexedParameters(String lastname);

		User indexedParameterWithSort(String lastname, Sort sort);

		User valid(@Param("username") String username);

		User validWithPageable(@Param("username") String username, Pageable pageable);

		User validWithSort(@Param("username") String username, Sort sort);

		User validWithDefaultTemporalTypeParameter(@Temporal Date registerDate);

		User validWithCustomTemporalTypeParameter(@Temporal(TIMESTAMP) Date registerDate);

		User invalidWithTemporalTypeParameter(@Temporal String registerDate);

		List<User> validWithVarArgs(Integer... ids);

		User optionalParameter(Optional<String> name);

		@org.springframework.data.jpa.repository.Query("select x from User where name = :name")
		User withQuery(String name, String other);
	}

	@Test
	public void bindWorksWithNullForSort() throws Exception {
	}

	@Test
	public void bindWorksWithNullForPageable() throws Exception {
	}

	@Test
	public void usesIndexedParametersIfNoParamAnnotationPresent() throws Exception {
	}

	@Test

	public void usesParameterNameIfAnnotated() throws Exception {
	}

	@Test
	public void bindsEmbeddableCorrectly() throws Exception {
	}

	@Test // DATAJPA-107
	public void shouldSetTemporalQueryParameterToDate() throws Exception {
	}

	@Test // DATAJPA-107
	public void shouldSetTemporalQueryParameterToTimestamp() throws Exception {
	}

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-107
	public void shouldThrowIllegalArgumentExceptionIfIsAnnotatedWithTemporalParamAndParameterTypeIsNotDate()
			throws Exception {
	}

	@Test // DATAJPA-461
	public void shouldAllowBindingOfVarArgsAsIs() throws Exception {
	}

	@Test // DATAJPA-809
	public void unwrapsOptionalParameter() throws Exception {
	}

	@Test // DATAJPA-1172
	public void doesNotBindExcessParameters() throws Exception {
	}

	public SampleEntity findByEmbeddable(SampleEmbeddable embeddable) {

		return null;
	}

	@SuppressWarnings("unused")
	static class SampleEntity {

		private SampleEmbeddable embeddable;
	}

	@Embeddable
	@SuppressWarnings("unused")
	public static class SampleEmbeddable {

		private String foo;
		private String bar;
	}
}
