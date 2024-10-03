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
package org.springframework.data.jpa.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.sample.EmbeddedIdExampleDepartment;
import org.springframework.data.jpa.domain.sample.EmbeddedIdExampleEmployee;
import org.springframework.data.jpa.domain.sample.EmbeddedIdExampleEmployeePK;
import org.springframework.data.jpa.domain.sample.IdClassExampleDepartment;
import org.springframework.data.jpa.domain.sample.IdClassExampleEmployee;
import org.springframework.data.jpa.domain.sample.IdClassExampleEmployeePK;
import org.springframework.data.jpa.domain.sample.QEmbeddedIdExampleEmployee;
import org.springframework.data.jpa.domain.sample.QIdClassExampleEmployee;
import org.springframework.data.jpa.repository.sample.EmployeeRepositoryWithEmbeddedId;
import org.springframework.data.jpa.repository.sample.EmployeeRepositoryWithIdClass;
import org.springframework.data.jpa.repository.sample.SampleConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests some usage variants of composite keys with spring data jpa.
 *
 * @author Thomas Darimont
 * @author Mark Paluch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SampleConfig.class)
@Transactional
public class RepositoryWithCompositeKeyTests {

	@Rule public ExpectedException expectedException = ExpectedException.none();

	@Autowired EmployeeRepositoryWithIdClass employeeRepositoryWithIdClass;
	@Autowired EmployeeRepositoryWithEmbeddedId employeeRepositoryWithEmbeddedId;

	/**
	 * @see <a href="download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf">Final JPA 2.0
	 *      Specification 2.4.1.3 Derived Identities Example 2</a>
	 */
	@Test // DATAJPA-269
	public void shouldSupportSavingEntitiesWithCompositeKeyClassesWithIdClassAndDerivedIdentities() {
	}

	/**
	 * @see <a href="download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf">Final JPA 2.0
	 *      Specification 2.4.1.3 Derived Identities Example 3</a>
	 */
	@Test // DATAJPA-269
	public void shouldSupportSavingEntitiesWithCompositeKeyClassesWithEmbeddedIdsAndDerivedIdentities() {
	}

	@Test // DATAJPA-472, DATAJPA-912
	public void shouldSupportFindAllWithPageableAndEntityWithIdClass() throws Exception {
	}

	@Test // DATAJPA-497
	public void sortByEmbeddedPkFieldInCompositePkWithEmbeddedIdInQueryDsl() {
	}

	@Test // DATAJPA-497
	public void sortByEmbeddedPkFieldInCompositePkWithIdClassInQueryDsl() {
	}

	@Test // DATAJPA-527, DATAJPA-1148
	public void testExistsWithIdClass() {
	}

	@Test // DATAJPA-527
	public void testExistsWithEmbeddedId() {
	}

	@Test // DATAJPA-611
	public void shouldAllowFindAllWithIdsForEntitiesWithCompoundIdClassKeys() {
	}

	@Test // DATAJPA-920
	public void shouldExecuteExistsQueryForEntitiesWithEmbeddedId() {
	}

	@Test // DATAJPA-920
	public void shouldExecuteExistsQueryForEntitiesWithCompoundIdClassKeys() {
	}
}
