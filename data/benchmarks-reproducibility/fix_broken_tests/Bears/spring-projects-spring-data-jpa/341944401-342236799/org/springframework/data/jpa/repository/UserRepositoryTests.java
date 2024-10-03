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
package org.springframework.data.jpa.repository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.data.domain.Example.*;
import static org.springframework.data.domain.ExampleMatcher.*;
import static org.springframework.data.domain.Sort.Direction.*;
import static org.springframework.data.jpa.domain.Specification.*;
import static org.springframework.data.jpa.domain.Specification.not;
import static org.springframework.data.jpa.domain.sample.UserSpecifications.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.ExampleMatcher.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.sample.Address;
import org.springframework.data.jpa.domain.sample.Role;
import org.springframework.data.jpa.domain.sample.SpecialUser;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.jpa.repository.sample.SampleEvaluationContextExtension.SampleSecurityContextHolder;
import org.springframework.data.jpa.repository.sample.UserRepository;
import org.springframework.data.jpa.repository.sample.UserRepository.NameOnly;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

/**
 * Base integration test class for {@code UserRepository}. Loads a basic (non-namespace) Spring configuration file as
 * well as Hibernate configuration to execute tests.
 * <p>
 * To test further persistence providers subclass this class and provide a custom provider configuration.
 *
 * @author Oliver Gierke
 * @author Kevin Raymond
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Kevin Peters
 * @author Jens Schauder
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
@Transactional
public class UserRepositoryTests {

	@PersistenceContext EntityManager em;

	// CUT
	@Autowired UserRepository repository;

	// Test fixture
	User firstUser, secondUser, thirdUser, fourthUser;
	Integer id;
	Role adminRole;

	@Before
	public void setUp() throws Exception {

		firstUser = new User("Oliver", "Gierke", "gierke@synyx.de");
		firstUser.setAge(28);
		secondUser = new User("Joachim", "Arrasz", "arrasz@synyx.de");
		secondUser.setAge(35);
		Thread.sleep(10);
		thirdUser = new User("Dave", "Matthews", "no@email.com");
		thirdUser.setAge(43);
		fourthUser = new User("kevin", "raymond", "no@gmail.com");
		fourthUser.setAge(31);
		adminRole = new Role("admin");

		SampleSecurityContextHolder.clear();
	}

	@Test
	public void testCreation() {
	}

	@Test
	public void testRead() throws Exception {
	}

	@Test
	public void findsAllByGivenIds() {
	}

	@Test
	public void testReadByIdReturnsNullForNotFoundEntities() {
	}

	@Test
	public void savesCollectionCorrectly() throws Exception {
	}

	@Test
	public void savingEmptyCollectionIsNoOp() throws Exception {
	}

	@Test
	public void testUpdate() {
	}

	@Test
	public void existReturnsWhetherAnEntityCanBeLoaded() throws Exception {
	}

	@Test
	public void deletesAUserById() {
	}

	@Test
	public void testDelete() {
	}

	@Test
	public void returnsAllSortedCorrectly() throws Exception {
	}

	@Test // DATAJPA-296
	public void returnsAllIgnoreCaseSortedCorrectly() throws Exception {
	}

	@Test
	public void deleteColletionOfEntities() {
	}

	@Test
	public void batchDeleteColletionOfEntities() {
	}

	@Test
	public void deleteEmptyCollectionDoesNotDeleteAnything() {
	}

	@Test
	public void executesManipulatingQuery() throws Exception {
	}

	@Test
	public void testFinderInvocationWithNullParameter() {
	}

	@Test
	public void testFindByLastname() throws Exception {
	}

	/**
	 * Tests, that searching by the email address of the reference user returns exactly that instance.
	 */
	@Test
	public void testFindByEmailAddress() throws Exception {
	}

	/**
	 * Tests reading all users.
	 */
	@Test
	public void testReadAll() {
	}

	/**
	 * Tests that all users get deleted by triggering {@link UserRepository#deleteAll()}.
	 */
	@Test
	public void deleteAll() throws Exception {
	}

	@Test // DATAJPA-137
	public void deleteAllInBatch() {
	}

	/**
	 * Tests cascading persistence.
	 */
	@Test
	public void testCascadesPersisting() {
	}

	/**
	 * Tests, that persisting a relationsship without cascade attributes throws a {@code DataAccessException}.
	 */
	@Test //(expected = DataAccessException.class)
	public void testPreventsCascadingRolePersisting() {
	}

	/**
	 * Tests cascading on {@literal merge} operation.
	 */
	@Test
	public void testMergingCascadesCollegueas() {
	}

	@Test
	public void testCountsCorrectly() {
	}

	@Test
	public void testInvocationOfCustomImplementation() {
	}

	@Test
	public void testOverwritingFinder() {
	}

	@Test
	public void testUsesQueryAnnotation() {
	}

	@Test
	public void testExecutionOfProjectingMethod() {
	}

	@Test
	public void executesSpecificationCorrectly() {
	}

	@Test
	public void executesSingleEntitySpecificationCorrectly() throws Exception {
	}

	@Test
	public void returnsNullIfNoEntityFoundForSingleEntitySpecification() throws Exception {
	}

	@Test //(expected = IncorrectResultSizeDataAccessException.class)
	public void throwsExceptionForUnderSpecifiedSingleEntitySpecification() {
	}

	@Test
	public void executesCombinedSpecificationsCorrectly() {
	}

	@Test // DATAJPA-253
	public void executesNegatingSpecificationCorrectly() {
	}

	@Test
	public void executesCombinedSpecificationsWithPageableCorrectly() {
	}

	@Test
	public void executesMethodWithAnnotatedNamedParametersCorrectly() throws Exception {
	}

	@Test
	public void executesMethodWithNamedParametersCorrectlyOnMethodsWithQueryCreation() throws Exception {
	}

	@Test
	public void executesLikeAndOrderByCorrectly() throws Exception {
	}

	@Test
	public void executesNotLikeCorrectly() throws Exception {
	}

	@Test
	public void executesSimpleNotCorrectly() throws Exception {
	}

	@Test
	public void returnsSameListIfNoSpecGiven() throws Exception {
	}

	@Test
	public void returnsSameListIfNoSortIsGiven() throws Exception {
	}

	@Test
	public void returnsSamePageIfNoSpecGiven() throws Exception {
	}

	@Test
	public void returnsAllAsPageIfNoPageableIsGiven() throws Exception {
	}

	@Test
	public void removeDetachedObject() throws Exception {
	}

	@Test
	public void executesPagedSpecificationsCorrectly() throws Exception {
	}

	@Test
	public void executesPagedSpecificationsWithSortCorrectly() throws Exception {
	}

	@Test
	public void executesPagedSpecificationWithSortCorrectly2() throws Exception {
	}

	@Test
	public void executesQueryMethodWithDeepTraversalCorrectly() throws Exception {
	}

	@Test
	public void executesFindByColleaguesLastnameCorrectly() throws Exception {
	}

	@Test
	public void executesFindByNotNullLastnameCorrectly() throws Exception {
	}

	@Test
	public void executesFindByNullLastnameCorrectly() throws Exception {
	}

	@Test
	public void findsSortedByLastname() throws Exception {
	}

	@Test
	public void findsUsersBySpringDataNamedQuery() {
	}

	@Test // DATADOC-86
	public void readsPageWithGroupByClauseCorrectly() {
	}

	@Test
	public void executesLessThatOrEqualQueriesCorrectly() {
	}

	@Test
	public void executesGreaterThatOrEqualQueriesCorrectly() {
	}

	@Test // DATAJPA-117
	public void executesNativeQueryCorrectly() {
	}

	@Test // DATAJPA-132
	public void executesFinderWithTrueKeywordCorrectly() {
	}

	@Test // DATAJPA-132
	public void executesFinderWithFalseKeywordCorrectly() {
	}

	/**
	 * Ignored until the query declaration is supported by OpenJPA.
	 */
	@Test
	public void executesAnnotatedCollectionMethodCorrectly() {
	}

	@Test // DATAJPA-188
	public void executesFinderWithAfterKeywordCorrectly() {
	}

	@Test // DATAJPA-188
	public void executesFinderWithBeforeKeywordCorrectly() {
	}

	@Test // DATAJPA-180
	public void executesFinderWithStartingWithCorrectly() {
	}

	@Test // DATAJPA-180
	public void executesFinderWithEndingWithCorrectly() {
	}

	@Test // DATAJPA-180
	public void executesFinderWithContainingCorrectly() {
	}

	@Test // DATAJPA-201
	public void allowsExecutingPageableMethodWithUnpagedArgument() {
	}

	@Test // DATAJPA-207
	public void executesNativeQueryForNonEntitiesCorrectly() {
	}

	@Test // DATAJPA-232
	public void handlesIterableOfIdsCorrectly() {
	}

	protected void flushTestUsers() {

		em.persist(adminRole);

		firstUser = repository.save(firstUser);
		secondUser = repository.save(secondUser);
		thirdUser = repository.save(thirdUser);
		fourthUser = repository.save(fourthUser);

		repository.flush();

		id = firstUser.getId();

		assertThat(id).isNotNull();
		assertThat(secondUser.getId()).isNotNull();
		assertThat(thirdUser.getId()).isNotNull();
		assertThat(fourthUser.getId()).isNotNull();

		assertThat(repository.existsById(id)).isTrue();
		assertThat(repository.existsById(secondUser.getId())).isTrue();
		assertThat(repository.existsById(thirdUser.getId())).isTrue();
		assertThat(repository.existsById(fourthUser.getId())).isTrue();
	}

	private static <T> void assertSameElements(Collection<T> first, Collection<T> second) {

		for (T element : first) {
			assertThat(element).isIn(second);
		}

		for (T element : second) {
			assertThat(element).isIn(first);
		}
	}

	private void assertDeleteCallDoesNotDeleteAnything(List<User> collection) {

		flushTestUsers();
		long count = repository.count();

		repository.deleteAll(collection);
		assertThat(repository.count()).isEqualTo(count);
	}

	@Test
	public void ordersByReferencedEntityCorrectly() {
	}

	@Test // DATAJPA-252
	public void bindsSortingToOuterJoinCorrectly() {
	}

	@Test // DATAJPA-277
	public void doesNotDropNullValuesOnPagedSpecificationExecution() {
	}

	@Test // DATAJPA-346
	public void shouldGenerateLeftOuterJoinInfindAllWithPaginationAndSortOnNestedPropertyPath() {
	}

	@Test // DATAJPA-292
	public void executesManualQueryWithPositionLikeExpressionCorrectly() {
	}

	@Test // DATAJPA-292
	public void executesManualQueryWithNamedLikeExpressionCorrectly() {
	}

	@Test // DATAJPA-231
	public void executesDerivedCountQueryToLong() {
	}

	@Test // DATAJPA-231
	public void executesDerivedCountQueryToInt() {
	}

	@Test // DATAJPA-231
	public void executesDerivedExistsQuery() {
	}

	@Test // DATAJPA-332, DATAJPA-1168
	public void findAllReturnsEmptyIterableIfNoIdsGiven() {
	}

	@Test // DATAJPA-391
	public void executesManuallyDefinedQueryWithFieldProjection() {
	}

	@Test // DATAJPA-83
	public void looksUpEntityReference() {
	}

	@Test // DATAJPA-415
	public void invokesQueryWithVarargsParametersCorrectly() {
	}

	@Test // DATAJPA-415
	public void shouldSupportModifyingQueryWithVarArgs() {
	}

	@Test // DATAJPA-405
	public void executesFinderWithOrderClauseOnly() {
	}

	@Test // DATAJPA-427
	public void sortByAssociationPropertyShouldUseLeftOuterJoin() {
	}

	@Test // DATAJPA-427
	public void sortByAssociationPropertyInPageableShouldUseLeftOuterJoin() {
	}

	@Test // DATAJPA-427
	public void sortByEmbeddedProperty() {
	}

	@Test // DATAJPA-454
	public void findsUserByBinaryDataReference() throws Exception {
	}

	@Test // DATAJPA-461
	public void customFindByQueryWithPositionalVarargsParameters() {
	}

	@Test // DATAJPA-461
	public void customFindByQueryWithNamedVarargsParameters() {
	}

	@Test // DATAJPA-464
	public void saveAndFlushShouldSupportReturningSubTypesOfRepositoryEntity() {
	}

	@Test // DATAJPA-218
	public void findAllByUntypedExampleShouldReturnSubTypesOfRepositoryEntity() {
	}

	@Test // DATAJPA-218
	public void findAllByTypedUserExampleShouldReturnSubTypesOfRepositoryEntity() {
	}

	@Test // DATAJPA-218
	public void findAllByTypedSpecialUserExampleShouldReturnSubTypesOfRepositoryEntity() {
	}

	@Test // DATAJPA-491
	public void sortByNestedAssociationPropertyWithSortInPageable() {
	}

	@Test // DATAJPA-510
	public void sortByNestedAssociationPropertyWithSortOrderIgnoreCaseInPageable() {
	}

	@Test // DATAJPA-496
	public void findByElementCollectionAttribute() {
	}

	@Test // DATAJPA-460
	public void deleteByShouldReturnListOfDeletedElementsWhenRetunTypeIsCollectionLike() {
	}

	@Test // DATAJPA-460
	public void deleteByShouldRemoveElementsMatchingDerivedQuery() {
	}

	@Test // DATAJPA-460
	public void deleteByShouldReturnNumberOfEntitiesRemovedIfReturnTypeIsLong() {
	}

	@Test // DATAJPA-460
	public void deleteByShouldReturnZeroInCaseNoEntityHasBeenRemovedAndReturnTypeIsNumber() {
	}

	@Test // DATAJPA-460
	public void deleteByShouldReturnEmptyListInCaseNoEntityHasBeenRemovedAndReturnTypeIsCollectionLike() {
	}

	/**
	 * @see <a href="https://issues.apache.org/jira/browse/OPENJPA-2484">OPENJPA-2484</a>
	 */
	@Test // DATAJPA-505
	@Ignore
	public void findBinaryDataByIdJpaQl() throws Exception {
	}

	@Test // DATAJPA-506
	public void findBinaryDataByIdNative() throws Exception {
	}

	@Test // DATAJPA-456
	public void findPaginatedExplicitQueryWithCountQueryProjection() {
	}

	@Test // DATAJPA-456
	public void findPaginatedNamedQueryWithCountQueryProjection() {
	}

	@Test // DATAJPA-551
	public void findOldestUser() {
	}

	@Test // DATAJPA-551
	public void findYoungestUser() {
	}

	@Test // DATAJPA-551
	public void find2OldestUsers() {
	}

	@Test // DATAJPA-551
	public void find2YoungestUsers() {
	}

	@Test // DATAJPA-551
	public void find3YoungestUsersPageableWithPageSize2() {
	}

	@Test // DATAJPA-551
	public void find2YoungestUsersPageableWithPageSize3() {
	}

	@Test // DATAJPA-551
	public void find3YoungestUsersPageableWithPageSize2Sliced() {
	}

	@Test // DATAJPA-551
	public void find2YoungestUsersPageableWithPageSize3Sliced() {
	}

	@Test // DATAJPA-912
	public void pageableQueryReportsTotalFromResult() {
	}

	@Test // DATAJPA-912
	public void pageableQueryReportsTotalFromCount() {
	}

	@Test // DATAJPA-506
	public void invokesQueryWithWrapperType() {
	}

	@Test // DATAJPA-564
	public void shouldFindUserByFirstnameAndLastnameWithSpelExpressionInStringBasedQuery() {
	}

	@Test // DATAJPA-564
	public void shouldFindUserByLastnameWithSpelExpressionInStringBasedQuery() {
	}

	@Test // DATAJPA-564
	public void shouldFindBySpELExpressionWithoutArgumentsWithQuestionmark() {
	}

	@Test // DATAJPA-564
	public void shouldFindBySpELExpressionWithoutArgumentsWithColon() {
	}

	@Test // DATAJPA-564
	public void shouldFindUsersByAgeForSpELExpression() {
	}

	@Test // DATAJPA-564
	public void shouldfindUsersByFirstnameForSpELExpressionWithParameterNameVariableReference() {
	}

	@Test // DATAJPA-564
	public void shouldFindCurrentUserWithCustomQueryDependingOnSecurityContext() {
	}

	@Test // DATAJPA-564
	public void shouldFindByFirstnameAndCurrentUserWithCustomQuery() {
	}

	@Test // DATAJPA-564
	public void shouldfindUsersByFirstnameForSpELExpressionOnlyWithParameterNameVariableReference() {
	}

	@Test // DATAJPA-564
	public void shouldfindUsersByFirstnameForSpELExpressionOnlyWithParameterIndexReference() {
	}

	@Test // DATAJPA-564
	public void shouldFindUsersInNativeQueryWithPagination() {
	}

	@Test // DATAJPA-1140
	public void shouldFindUsersByUserFirstnameAsSpELExpressionAndLastnameAsStringInStringBasedQuery() {
	}

	@Test // DATAJPA-1140
	public void shouldFindUsersByFirstnameAsStringAndUserLastnameAsSpELExpressionInStringBasedQuery() {
	}

	@Test // DATAJPA-1140
	public void shouldFindUsersByUserFirstnameAsSpELExpressionAndLastnameAsFakeSpELExpressionInStringBasedQuery() {
	}

	@Test // DATAJPA-1140
	public void shouldFindUsersByFirstnameAsFakeSpELExpressionAndUserLastnameAsSpELExpressionInStringBasedQuery() {
	}

	@Test // DATAJPA-1140
	public void shouldFindUsersByFirstnameWithLeadingPageableParameter() {
	}

	@Test // DATAJPA-629
	public void shouldfindUsersBySpELExpressionParametersWithSpelTemplateExpression() {
	}

	@Test // DATAJPA-606
	public void findByEmptyCollectionOfStrings() throws Exception {
	}

	@Test // DATAJPA-606
	public void findByEmptyCollectionOfIntegers() throws Exception {
	}

	@Test // DATAJPA-606
	public void findByEmptyArrayOfIntegers() throws Exception {
	}

	@Test // DATAJPA-606
	public void findByAgeWithEmptyArrayOfIntegersOrFirstName() {
	}

	@Test // DATAJPA-677
	public void shouldSupportJava8StreamsForRepositoryFinderMethods() {
	}

	@Test // DATAJPA-677
	public void shouldSupportJava8StreamsForRepositoryDerivedFinderMethods() {
	}

	@Test // DATAJPA-677
	public void supportsJava8StreamForPageableMethod() {
	}

	@Test // DATAJPA-218
	public void findAllByExample() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithEmptyProbe() {
	}

	@Test //(expected = InvalidDataAccessApiUsageException.class) // DATAJPA-218
	public void findAllByNullExample() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithExcludedAttributes() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithAssociation() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithEmbedded() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithStartingStringMatcher() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithEndingStringMatcher() {
	}

	@Test //(expected = InvalidDataAccessApiUsageException.class) // DATAJPA-218
	public void findAllByExampleWithRegexStringMatcher() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithIgnoreCase() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithStringMatcherAndIgnoreCase() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithIncludeNull() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithPropertySpecifier() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithSort() {
	}

	@Test // DATAJPA-218
	public void findAllByExampleWithPageable() {
	}

	@Test //(expected = InvalidDataAccessApiUsageException.class) // DATAJPA-218
	public void findAllByExampleShouldNotAllowCycles() {
	}

	@Test //(expected = InvalidDataAccessApiUsageException.class) // DATAJPA-218
	public void findAllByExampleShouldNotAllowCyclesOverSeveralInstances() {
	}

	@Test // DATAJPA-218
	public void findOneByExampleWithExcludedAttributes() {
	}

	@Test // DATAJPA-218
	public void countByExampleWithExcludedAttributes() {
	}

	@Test // DATAJPA-218
	public void existsByExampleWithExcludedAttributes() {
	}

	@Test // DATAJPA-905
	public void executesPagedSpecificationSettingAnOrder() {
	}

	@Test // DATAJPA-1172
	public void exceptionsDuringParameterSettingGetThrown() {
	}

	@Test // DATAJPA-1172
	public void queryProvidesCorrectNumberOfParametersForNativeQuery() {
	}

	@Test // DATAJPA-1185
	public void dynamicProjectionReturningStream() {
	}

	@Test // DATAJPA-1185
	public void dynamicProjectionReturningList() {
	}

	@Test // DATAJPA-1179
	public void duplicateSpelsWorkAsIntended() {
	}

	@Test // DATAJPA-980
	public void supportsProjectionsWithNativeQueries() {
	}

	@Test // DATAJPA-1248
	public void supportsProjectionsWithNativeQueriesAndCamelCaseProperty() {
	}

	@Test // DATAJPA-1235
	public void handlesColonsFollowedByIntegerInStringLiteral() {
	}

	private Page<User> executeSpecWithSort(Sort sort) {

		flushTestUsers();

		Specification<User> spec = userHasFirstname("Oliver").or(userHasLastname("Matthews"));

		Page<User> result = repository.findAll(spec, PageRequest.of(0, 1, sort));
		assertThat(result.getTotalElements()).isEqualTo(2L);
		return result;
	}
}
