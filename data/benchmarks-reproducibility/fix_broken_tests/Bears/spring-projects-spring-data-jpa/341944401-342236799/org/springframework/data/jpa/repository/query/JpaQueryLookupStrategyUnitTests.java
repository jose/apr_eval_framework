/*
 * Copyright 2012-2017 the original author or authors.
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
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.query.DefaultEvaluationContextProvider;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

/**
 * Unit tests for {@link JpaQueryLookupStrategy}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(MockitoJUnitRunner.class)
public class JpaQueryLookupStrategyUnitTests {

	private static final EvaluationContextProvider EVALUATION_CONTEXT_PROVIDER = DefaultEvaluationContextProvider.INSTANCE;
	@Mock EntityManager em;
	@Mock EntityManagerFactory emf;
	@Mock QueryExtractor extractor;
	@Mock NamedQueries namedQueries;
	@Mock Metamodel metamodel;
	@Mock ProjectionFactory projectionFactory;

	public @Rule ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {

		when(em.getMetamodel()).thenReturn(metamodel);
		when(em.getEntityManagerFactory()).thenReturn(emf);
		when(emf.createEntityManager()).thenReturn(em);
		when(em.getDelegate()).thenReturn(em);
	}

	@Test // DATAJPA-226
	public void invalidAnnotatedQueryCausesException() throws Exception {
	}

	@Test // DATAJPA-554
	public void sholdThrowMorePreciseExceptionIfTryingToUsePaginationInNativeQueries() throws Exception {
	}

	interface UserRepository extends Repository<User, Long> {

		@Query("something absurd")
		User findByFoo(String foo);

		@Query(value = "select u.* from User u", nativeQuery = true)
		Page<User> findByInvalidNativeQuery(String param, Pageable page);
	}
}
