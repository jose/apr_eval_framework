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
package org.springframework.data.jpa.repository.support;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.sample.Address;
import org.springframework.data.jpa.domain.sample.QUser;
import org.springframework.data.jpa.domain.sample.Role;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;

/**
 * Integration test for {@link QuerydslJpaRepository}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 * @author Christoph Strobl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:infrastructure.xml" })
@Transactional
public class QuerydslJpaRepositoryTests {

	@PersistenceContext EntityManager em;

	QuerydslJpaRepository<User, Integer> repository;
	QUser user = new QUser("user");
	User dave, carter, oliver;
	Role adminRole;

	@Before
	public void setUp() {

		JpaEntityInformation<User, Integer> information = new JpaMetamodelEntityInformation<User, Integer>(User.class,
				em.getMetamodel());

		repository = new QuerydslJpaRepository<User, Integer>(information, em);
		dave = repository.save(new User("Dave", "Matthews", "dave@matthews.com"));
		carter = repository.save(new User("Carter", "Beauford", "carter@beauford.com"));
		oliver = repository.save(new User("Oliver", "matthews", "oliver@matthews.com"));
		adminRole = em.merge(new Role("admin"));
	}

	@Test
	public void executesPredicatesCorrectly() throws Exception {
	}

	@Test
	public void executesStringBasedPredicatesCorrectly() throws Exception {
	}

	@Test // DATAJPA-243
	public void considersSortingProvidedThroughPageable() {
	}

	@Test // DATAJPA-296
	public void appliesIgnoreCaseOrdering() {
	}

	@Test // DATAJPA-427
	public void findBySpecificationWithSortByPluralAssociationPropertyInPageableShouldUseSortNullValuesLast() {
	}

	@Test // DATAJPA-427
	public void findBySpecificationWithSortBySingularAssociationPropertyInPageableShouldUseSortNullValuesLast() {
	}

	@Test // DATAJPA-427
	public void findBySpecificationWithSortBySingularPropertyInPageableShouldUseSortNullValuesFirst() {
	}

	@Test // DATAJPA-427
	public void findBySpecificationWithSortByOrderIgnoreCaseBySingularPropertyInPageableShouldUseSortNullValuesFirst() {
	}

	@Test // DATAJPA-427
	public void findBySpecificationWithSortByNestedEmbeddedPropertyInPageableShouldUseSortNullValuesFirst() {
	}

	@Test // DATAJPA-12
	public void findBySpecificationWithSortByQueryDslOrderSpecifierWithQPageRequestAndQSort() {
	}

	@Test // DATAJPA-12
	public void findBySpecificationWithSortByQueryDslOrderSpecifierWithQPageRequest() {
	}

	@Test // DATAJPA-12
	public void findBySpecificationWithSortByQueryDslOrderSpecifierForAssociationShouldGenerateLeftJoinWithQPageRequest() {
	}

	@Test // DATAJPA-491
	public void sortByNestedAssociationPropertyWithSpecificationAndSortInPageable() {
	}

	@Test // DATAJPA-500, DATAJPA-635
	public void sortByNestedEmbeddedAttribute() {
	}

	@Test // DATAJPA-566, DATAJPA-635
	public void shouldSupportSortByOperatorWithDateExpressions() {
	}

	@Test // DATAJPA-665
	public void shouldSupportExistsWithPredicate() throws Exception {
	}

	@Test // DATAJPA-679
	public void shouldSupportFindAllWithPredicateAndSort() {
	}

	@Test // DATAJPA-585
	public void worksWithUnpagedPageable() {
	}

	@Test // DATAJPA-912
	public void pageableQueryReportsTotalFromResult() {
	}

	@Test // DATAJPA-912
	public void pageableQueryReportsTotalFromCount() {
	}

	@Test // DATAJPA-1115
	public void findOneWithPredicateReturnsResultCorrectly() {
	}

	@Test // DATAJPA-1115
	public void findOneWithPredicateReturnsOptionalEmptyWhenNoDataFound() {
	}

	@Test //(expected = IncorrectResultSizeDataAccessException.class) // DATAJPA-1115
	public void findOneWithPredicateThrowsExceptionForNonUniqueResults() {
	}
}
