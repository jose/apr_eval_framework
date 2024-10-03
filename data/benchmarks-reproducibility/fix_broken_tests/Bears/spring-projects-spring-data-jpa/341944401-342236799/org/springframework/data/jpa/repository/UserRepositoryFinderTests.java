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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.domain.Sort.Direction.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.sample.Role;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.jpa.repository.sample.RoleRepository;
import org.springframework.data.jpa.repository.sample.UserRepository;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test for executing finders, thus testing various query lookup strategies.
 * 
 * @see QueryLookupStrategy
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:config/namespace-application-context.xml")
@Transactional
public class UserRepositoryFinderTests {

	@Autowired UserRepository userRepository;
	@Autowired RoleRepository roleRepository;

	User dave, carter, oliver;
	Role drummer, guitarist, singer;

	@Before
	public void setUp() {

		drummer = roleRepository.save(new Role("DRUMMER"));
		guitarist = roleRepository.save(new Role("GUITARIST"));
		singer = roleRepository.save(new Role("SINGER"));

		dave = userRepository.save(new User("Dave", "Matthews", "dave@dmband.com", singer));
		carter = userRepository.save(new User("Carter", "Beauford", "carter@dmband.com", singer, drummer));
		oliver = userRepository.save(new User("Oliver August", "Matthews", "oliver@dmband.com"));
	}

	@After
	public void clearUp() {

		userRepository.deleteAll();
		roleRepository.deleteAll();
	}

	/**
	 * Tests creation of a simple query.
	 */
	@Test
	public void testSimpleCustomCreatedFinder() {
	}

	/**
	 * Tests that the repository returns {@code null} for not found objects for finder methods that return a single domain
	 * object.
	 */
	@Test
	public void returnsNullIfNothingFound() {
	}

	/**
	 * Tests creation of a simple query consisting of {@code AND} and {@code OR} parts.
	 */
	@Test
	public void testAndOrFinder() {
	}

	@Test
	public void executesPagingMethodToPageCorrectly() {
	}

	@Test
	public void executesPagingMethodToListCorrectly() {
	}

	@Test
	public void executesInKeywordForPageCorrectly() {
	}

	@Test
	public void executesNotInQueryCorrectly() throws Exception {
	}

	@Test // DATAJPA-92
	public void findsByLastnameIgnoringCase() throws Exception {
	}

	@Test // DATAJPA-92
	public void findsByLastnameIgnoringCaseLike() throws Exception {
	}

	@Test // DATAJPA-92
	public void findByLastnameAndFirstnameAllIgnoringCase() throws Exception {
	}

	@Test // DATAJPA-94
	public void respectsPageableOrderOnQueryGenerateFromMethodName() throws Exception {
	}

	@Test // DATAJPA-486
	public void executesQueryToSlice() {
	}

	@Test // DATAJPA-830
	public void executesMethodWithNotContainingOnStringCorrectly() {
	}

	@Test // DATAJPA-829
	public void translatesContainsToMemberOf() {
	}

	@Test // DATAJPA-829
	public void translatesNotContainsToNotMemberOf() {
	}

	@Test // DATAJPA-974
	public void executesQueryWithProjectionContainingReferenceToPluralAttribute() {
	}

	@Test //(expected = InvalidDataAccessApiUsageException.class) // DATAJPA-1023, DATACMNS-959
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void rejectsStreamExecutionIfNoSurroundingTransactionActive() {
	}
}
