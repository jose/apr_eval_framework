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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryCreationException;

/**
 * Unit tests for {@link NamedQuery}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(MockitoJUnitRunner.class)
public class NamedQueryUnitTests {

	@Mock RepositoryMetadata metadata;
	@Mock QueryExtractor extractor;
	@Mock EntityManager em;
	@Mock EntityManagerFactory emf;
	@Mock Metamodel metamodel;

	ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

	Method method;

	@Before
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setUp() throws SecurityException, NoSuchMethodException {

		method = SampleRepository.class.getMethod("foo", Pageable.class);
		when(metadata.getDomainType()).thenReturn((Class) String.class);
		when(metadata.getReturnedDomainClass(method)).thenReturn((Class) String.class);

		when(em.getMetamodel()).thenReturn(metamodel);
		when(em.getEntityManagerFactory()).thenReturn(emf);
		when(emf.createEntityManager()).thenReturn(em);
	}

	@Test //(expected = QueryCreationException.class)
	public void rejectsPersistenceProviderIfIncapableOfExtractingQueriesAndPagebleBeingUsed() {
	}

	@Test // DATAJPA-142
	@SuppressWarnings("unchecked")
	public void doesNotRejectPersistenceProviderIfNamedCountQueryIsAvailable() {
	}

	interface SampleRepository {

		Page<String> foo(Pageable pageable);
	}
}
