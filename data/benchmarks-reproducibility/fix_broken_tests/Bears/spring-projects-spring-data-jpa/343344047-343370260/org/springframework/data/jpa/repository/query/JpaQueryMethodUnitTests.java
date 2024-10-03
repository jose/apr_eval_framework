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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.sample.UserRepository;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryMethod;

/**
 * Unit test for {@link QueryMethod}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Christoph Strobl
 */
@RunWith(MockitoJUnitRunner.class)
public class JpaQueryMethodUnitTests {

	static final Class<?> DOMAIN_CLASS = User.class;
	static final String METHOD_NAME = "findByFirstname";

	@Mock QueryExtractor extractor;
	@Mock RepositoryMetadata metadata;
	ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

	Method invalidReturnType, pageableAndSort, pageableTwice, sortableTwice, findWithLockMethod, findsProjections,
			findsProjection, queryMethodWithCustomEntityFetchGraph;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		invalidReturnType = InvalidRepository.class.getMethod(METHOD_NAME, String.class, Pageable.class);
		pageableAndSort = InvalidRepository.class.getMethod(METHOD_NAME, String.class, Pageable.class, Sort.class);
		pageableTwice = InvalidRepository.class.getMethod(METHOD_NAME, String.class, Pageable.class, Pageable.class);

		sortableTwice = InvalidRepository.class.getMethod(METHOD_NAME, String.class, Sort.class, Sort.class);

		findWithLockMethod = ValidRepository.class.getMethod("findOneLocked", Integer.class);

		findsProjections = ValidRepository.class.getMethod("findsProjections");
		findsProjection = ValidRepository.class.getMethod("findsProjection");

		queryMethodWithCustomEntityFetchGraph = ValidRepository.class.getMethod("queryMethodWithCustomEntityFetchGraph",
				Integer.class);
	}

	@Test
	public void testname() throws Exception {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void preventsNullRepositoryMethod() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void preventsNullQueryExtractor() throws Exception {
	}

	@Test
	public void returnsCorrectName() throws Exception {
	}

	@Test
	public void returnsQueryIfAvailable() throws Exception {
	}

	@Test //(expected = IllegalStateException.class)
	public void rejectsInvalidReturntypeOnPagebleFinder() {
	}

	@Test //(expected = IllegalStateException.class)
	public void rejectsPageableAndSortInFinderMethod() {
	}

	@Test //(expected = IllegalStateException.class)
	public void rejectsTwoPageableParameters() {
	}

	@Test //(expected = IllegalStateException.class)
	public void rejectsTwoSortableParameters() {
	}

	@Test
	public void recognizesModifyingMethod() throws Exception {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsModifyingMethodWithPageable() throws Exception {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsModifyingMethodWithSort() throws Exception {
	}

	@Test
	public void discoversHintsCorrectly() throws Exception {
	}

	private JpaQueryMethod getQueryMethod(Class<?> repositoryInterface, String methodName, Class<?>... parameterTypes)
			throws Exception {

		Method method = repositoryInterface.getMethod(methodName, parameterTypes);
		DefaultRepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(repositoryInterface);
		return new JpaQueryMethod(method, repositoryMetadata, factory, extractor);
	}

	@Test
	public void calculatesNamedQueryNamesCorrectly() throws Exception {
	}

	@Test // DATAJPA-117
	public void discoversNativeQuery() throws Exception {
	}

	@Test // DATAJPA-129
	public void considersAnnotatedNamedQueryName() throws Exception {
	}

	@Test // DATAJPA-73
	public void discoversLockModeCorrectly() throws Exception {
	}

	@Test // DATAJPA-142
	public void returnsDefaultCountQueryName() throws Exception {
	}

	@Test // DATAJPA-142
	public void returnsDefaultCountQueryNameBasedOnConfiguredNamedQueryName() throws Exception {
	}

	@Test // DATAJPA-185
	public void rejectsInvalidNamedParameter() throws Exception {
	}

	@Test // DATAJPA-207
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void returnsTrueIfReturnTypeIsEntity() {
	}

	@Test // DATAJPA-345
	public void detectsLockAndQueryHintsOnIfUsedAsMetaAnnotation() throws Exception {
	}

	@Test // DATAJPA-466
	public void shouldStoreJpa21FetchGraphInformationAsHint() {
	}

	@Test // DATAJPA-612
	public void shouldFindEntityGraphAnnotationOnOverriddenSimpleJpaRepositoryMethod() throws Exception {
	}

	@Test // DATAJPA-689
	public void shouldFindEntityGraphAnnotationOnOverriddenSimpleJpaRepositoryMethodFindOne() throws Exception {
	}

	/**
	 * DATAJPA-696
	 */
	@Test
	public void shouldFindEntityGraphAnnotationOnQueryMethodGetOneByWithDerivedName() throws Exception {
	}

	@Test // DATAJPA-758
	public void allowsPositionalBindingEvenIfParametersAreNamed() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForLockLockMode() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForQueryHints() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForQueryHintsCounting() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForModifyingClearAutomatically() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForHintsApplyToCountQuery() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForQueryValue() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForQueryCountQuery() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForQueryCountQueryProjection() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForQueryNamedQueryName() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForQueryNamedCountQueryName() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForQueryNativeQuery() throws Exception {
	}

	@Test // DATAJPA-871
	public void usesAliasedValueForEntityGraph() throws Exception {
	}

	/**
	 * Interface to define invalid repository methods for testing.
	 * 
	 * @author Oliver Gierke
	 */
	static interface InvalidRepository extends Repository<User, Long> {

		// Invalid return type
		User findByFirstname(String firstname, Pageable pageable);

		// Should not use Pageable *and* Sort
		Page<User> findByFirstname(String firstname, Pageable pageable, Sort sort);

		// Must not use two Pageables
		Page<User> findByFirstname(String firstname, Pageable first, Pageable second);

		// Must not use two Pageables
		Page<User> findByFirstname(String firstname, Sort first, Sort second);

		// Not backed by a named query or @Query annotation
		@Modifying
		void updateMethod(String firstname);

		// Modifying and Pageable is not allowed
		@Modifying
		Page<String> updateMethod(String firstname, Pageable pageable);

		// Modifying and Sort is not allowed
		@Modifying
		void updateMethod(String firstname, Sort sort);

		// Typo in named parameter
		@Query("select u from User u where u.firstname = :foo")
		List<User> findByAnnotatedQuery(@Param("param") String param);
	}

	static interface ValidRepository extends Repository<User, Long> {

		@Query(value = "query", nativeQuery = true)
		List<User> findByLastname(String lastname);

		@Query(name = "HateoasAwareSpringDataWebConfiguration.bar")
		List<User> findByNamedQuery();

		@Lock(LockModeType.PESSIMISTIC_WRITE)
		@Query("select u from User u where u.id = ?1")
		List<User> findOneLocked(Integer primaryKey);

		List<Integer> findsProjections();

		Integer findsProjection();

		@CustomAnnotation
		void withMetaAnnotation();

		// DATAJPA-466
		@EntityGraph(value = "User.propertyLoadPath", type = EntityGraphType.LOAD)
		User queryMethodWithCustomEntityFetchGraph(Integer id);

		@Query("select u from User u where u.firstname = ?1")
		User queryWithPositionalBinding(@Param("firstname") String firstname);

		@CustomComposedAnnotationWithAliasFor
		void withMetaAnnotationUsingAliasFor();
	}

	static interface JpaRepositoryOverride extends JpaRepository<User, Long> {

		/**
		 * DATAJPA-612
		 */
		@Override
		@EntityGraph("User.detail")
		List<User> findAll();

		/**
		 * DATAJPA-689
		 */
		@EntityGraph("User.detail")
		Optional<User> findOne(Long id);

		/**
		 * DATAJPA-696
		 */
		@EntityGraph
		User getOneById(Long id);

		@CustomComposedEntityGraphAnnotationWithAliasFor
		User getOneWithCustomEntityGraphAnnotation();

	}

	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	@QueryHints(@QueryHint(name = "foo", value = "bar"))
	@Retention(RetentionPolicy.RUNTIME)
	static @interface CustomAnnotation {

	}

	@Modifying
	@Query
	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	@QueryHints(@QueryHint(name = "foo", value = "bar"))
	@Retention(RetentionPolicy.RUNTIME)
	static @interface CustomComposedAnnotationWithAliasFor {

		@AliasFor(annotation = Modifying.class, attribute = "clearAutomatically")
		boolean doClear() default true;

		@AliasFor(annotation = Query.class, attribute = "value")
		String querystring() default "select u from User u where u.firstname = ?1";

		@AliasFor(annotation = Query.class, attribute = "countQuery")
		String countQueryString() default "select u from User u where u.lastname = ?1";

		@AliasFor(annotation = Query.class, attribute = "countProjection")
		String countProjectionString() default "foo-bar";

		@AliasFor(annotation = Query.class, attribute = "nativeQuery")
		boolean isNativeQuery() default true;

		@AliasFor(annotation = Query.class, attribute = "name")
		String namedQueryName() default "namedQueryName";

		@AliasFor(annotation = Query.class, attribute = "countName")
		String namedCountQueryName() default "namedCountQueryName";

		@AliasFor(annotation = Lock.class, attribute = "value")
		LockModeType lock() default LockModeType.PESSIMISTIC_FORCE_INCREMENT;

		@AliasFor(annotation = QueryHints.class, attribute = "value")
		QueryHint[] hints() default @QueryHint(name = "foo", value = "bar")
		;

		@AliasFor(annotation = QueryHints.class, attribute = "forCounting")
		boolean doCount() default true;
	}

	@EntityGraph
	@Retention(RetentionPolicy.RUNTIME)
	static @interface CustomComposedEntityGraphAnnotationWithAliasFor {

		@AliasFor(annotation = EntityGraph.class, attribute = "value")
		String graphName() default "User.detail";

		@AliasFor(annotation = EntityGraph.class, attribute = "type")
		EntityGraphType graphType() default EntityGraphType.LOAD;

		@AliasFor(annotation = EntityGraph.class, attribute = "attributePaths")
		String[] paths() default { "foo", "bar" };
	}
}
