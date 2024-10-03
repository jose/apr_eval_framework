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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.sample.UserRepository;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.ExtensionAwareEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Unit test for {@link SimpleJpaQuery}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SimpleJpaQueryUnitTests {

	static final String USER_QUERY = "select u from User u";
	static final SpelExpressionParser PARSER = new SpelExpressionParser();
	private static final EvaluationContextProvider EVALUATION_CONTEXT_PROVIDER = new ExtensionAwareEvaluationContextProvider();

	JpaQueryMethod method;

	@Mock EntityManager em;
	@Mock EntityManagerFactory emf;
	@Mock QueryExtractor extractor;
	@Mock javax.persistence.Query query;
	@Mock TypedQuery<Long> typedQuery;
	@Mock RepositoryMetadata metadata;
	@Mock ParameterBinder binder;
	@Mock Metamodel metamodel;

	ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

	public @Rule ExpectedException exception = ExpectedException.none();

	@Before
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setUp() throws SecurityException, NoSuchMethodException {

		when(em.getMetamodel()).thenReturn(metamodel);
		when(em.createQuery(anyString())).thenReturn(query);
		when(em.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
		when(em.getEntityManagerFactory()).thenReturn(emf);
		when(emf.createEntityManager()).thenReturn(em);
		when(metadata.getDomainType()).thenReturn((Class) User.class);
		when(metadata.getReturnedDomainClass(Mockito.any(Method.class))).thenReturn((Class) User.class);

		Method setUp = UserRepository.class.getMethod("findByLastname", String.class);
		method = new JpaQueryMethod(setUp, metadata, factory, extractor);
	}

	@Test
	public void prefersDeclaredCountQueryOverCreatingOne() throws Exception {
	}

	@Test // DATAJPA-77
	public void doesNotApplyPaginationToCountQuery() throws Exception {
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void discoversNativeQuery() throws Exception {
	}

	@Test //(expected = InvalidJpaQueryMethodException.class) // DATAJPA-554
	public void rejectsNativeQueryWithDynamicSort() throws Exception {
	}

	@Test //(expected = InvalidJpaQueryMethodException.class) // DATAJPA-554
	public void rejectsNativeQueryWithPageable() throws Exception {
	}

	@Test // DATAJPA-352
	@SuppressWarnings("unchecked")
	public void doesNotValidateCountQueryIfNotPagingMethod() throws Exception {
	}

	@Test // DATAJPA-352
	@SuppressWarnings("unchecked")
	public void validatesAndRejectsCountQueryIfPagingMethod() throws Exception {
	}

	@Test
	public void createsASimpleJpaQueryFromAnnotation() throws Exception {
	}

	@Test
	public void createsANativeJpaQueryFromAnnotation() throws Exception {
	}

	@Test // DATAJPA-757
	public void createsNativeCountQuery() throws Exception {
	}

	@Test // DATAJPA-885
	public void projectsWithManuallyDeclaredQuery() throws Exception {
	}

	private AbstractJpaQuery createJpaQuery(Method method) {

		JpaQueryMethod queryMethod = new JpaQueryMethod(method, metadata, factory, extractor);
		return JpaQueryFactory.INSTANCE.fromQueryAnnotation(queryMethod, em, EVALUATION_CONTEXT_PROVIDER);
	}

	interface SampleRepository {

		@Query(value = "SELECT u FROM User u WHERE u.lastname = ?1", nativeQuery = true)
		List<User> findNativeByLastname(String lastname);

		@Query(value = "SELECT u FROM User u WHERE u.lastname = ?1", nativeQuery = true)
		List<User> findNativeByLastname(String lastname, Sort sort);

		@Query(value = "SELECT u FROM User u WHERE u.lastname = ?1", nativeQuery = true)
		List<User> findNativeByLastname(String lastname, Pageable pageable);

		@Query(USER_QUERY)
		List<User> findByAnnotatedQuery();

		@Query(USER_QUERY)
		Page<User> pageByAnnotatedQuery(Pageable pageable);

		@Query("select u from User u")
		Collection<UserProjection> projectWithExplicitQuery();
	}

	interface UserProjection {}
}
