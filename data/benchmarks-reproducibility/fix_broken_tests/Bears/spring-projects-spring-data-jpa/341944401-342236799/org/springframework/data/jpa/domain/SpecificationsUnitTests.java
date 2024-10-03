/*
 * Copyright 2013-2017 the original author or authors.
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
package org.springframework.data.jpa.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.Specifications.*;
import static org.springframework.util.SerializationUtils.*;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(MockitoJUnitRunner.class)
public class SpecificationsUnitTests {

	Specification<Object> mockSpec;
	@Mock(extraInterfaces = Serializable.class) Root<Object> root;
	@Mock(extraInterfaces = Serializable.class) CriteriaQuery<?> query;
	@Mock(extraInterfaces = Serializable.class) CriteriaBuilder builder;

	@Mock(extraInterfaces = Serializable.class) Predicate predicate;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {

		mockSpec = (Specification<Object>) mock(Specification.class, withSettings().serializable());

		when(mockSpec.toPredicate(root, query, builder)).thenReturn(predicate);
	}

	@Test // DATAJPA-300
	public void createsSpecificationsFromNull() {
	}

	@Test // DATAJPA-300
	public void negatesNullSpecToNull() {
	}

	@Test // DATAJPA-300
	public void andConcatenatesSpecToNullSpec() {
	}

	@Test // DATAJPA-300
	public void andConcatenatesNullSpecToSpec() {
	}

	@Test // DATAJPA-300
	public void orConcatenatesSpecToNullSpec() {
	}

	@Test // DATAJPA-300
	public void orConcatenatesNullSpecToSpec() {
	}

	@Test // DATAJPA-523
	public void specificationsShouldBeSerializable() {
	}

	@Test // DATAJPA-523
	public void complexSpecificationsShouldBeSerializable() {
	}

}
