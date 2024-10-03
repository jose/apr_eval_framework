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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.jpa.domain.sample.SampleEntity;
import org.springframework.data.jpa.domain.sample.SampleEntityPK;
import org.springframework.data.jpa.domain.sample.PersistableWithIdClass;
import org.springframework.data.jpa.domain.sample.PersistableWithIdClassPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test for {@link JpaRepository}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:infrastructure.xml" })
@Transactional
public class JpaRepositoryTests {

	@PersistenceContext EntityManager em;

	JpaRepository<SampleEntity, SampleEntityPK> repository;
	CrudRepository<PersistableWithIdClass, PersistableWithIdClassPK> idClassRepository;

	@Before
	public void setUp() {

		repository = new JpaRepositoryFactory(em).getRepository(SampleEntityRepository.class);
		idClassRepository = new JpaRepositoryFactory(em).getRepository(SampleWithIdClassRepository.class);
	}

	@Test
	public void testCrudOperationsForCompoundKeyEntity() throws Exception {
	}

	@Test // DATAJPA-50
	public void executesCrudOperationsForEntityWithIdClass() {
	}

	@Test // DATAJPA-266
	public void testExistsForDomainObjectsWithCompositeKeys() throws Exception {
	}

	@Test // DATAJPA-527
	public void executesExistsForEntityWithIdClass() {
	}

	private static interface SampleEntityRepository extends JpaRepository<SampleEntity, SampleEntityPK> {

	}

	private static interface SampleWithIdClassRepository extends CrudRepository<PersistableWithIdClass, PersistableWithIdClassPK> {

	}
}
