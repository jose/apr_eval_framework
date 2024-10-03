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
package org.springframework.data.jpa.repository.query;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.sample.Category;
import org.springframework.data.jpa.domain.sample.Order;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.jpa.infrastructure.HibernateTestUtils;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for {@link QueryUtils}.
 * 
 * @author Oliver Gierke
 * @author Sébastien Péralta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:infrastructure.xml")
public class QueryUtilsIntegrationTests {

	@PersistenceContext EntityManager em;

	@Test // DATAJPA-403
	public void reusesExistingJoinForExpression() {
	}

	@Test // DATAJPA-401
	public void createsJoinForOptionalAssociation() {
	}

	@Test // DATAJPA-401
	public void doesNotCreateAJoinForNonOptionalAssociation() {
	}

	@Test // DATAJPA-454
	public void createsJoingToTraverseCollectionPath() {
	}

	@Test // DATAJPA-476
	public void traversesPluralAttributeCorrectly() {
	}

	@Test // DATAJPA-763
	@SuppressWarnings("unchecked")
	public void doesNotCreateAJoinForAlreadyFetchedAssociation() {
	}

	@Test // DATAJPA-1080
	public void toOrdersCanSortByJoinColumn() {
	}

	@Entity
	@SuppressWarnings("unused")
	static class Merchant {

		@Id String id;
		@OneToMany Set<Employee> employees;
	}

	@Entity
	@SuppressWarnings("unused")
	static class Employee {

		@Id String id;
		@OneToMany Set<Credential> credentials;
	}

	@Entity
	@SuppressWarnings("unused")
	static class Credential {

		@Id String id;
		String uid;
	}

	/**
	 * A {@link PersistenceProviderResolver} that returns only a Hibernate {@link PersistenceProvider} and ignores others.
	 * 
	 * @author Thomas Darimont
	 * @author Oliver Gierke
	 */
	static class HibernateOnlyPersistenceProviderResolver implements PersistenceProviderResolver {

		@Override
		public List<PersistenceProvider> getPersistenceProviders() {
			return singletonList(HibernateTestUtils.getPersistenceProvider());
		}

		@Override
		public void clearCachedProviders() {}
	}
}
