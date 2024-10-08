/*
 * Copyright 2011-2017 the original author or authors.
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

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.sample.Role;
import org.springframework.data.jpa.repository.sample.RoleRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link RoleRepository}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
@Transactional
public class RoleRepositoryIntegrationTests {

	@Autowired RoleRepository repository;

	@Test
	public void createsRole() throws Exception {
	}

	@Test
	public void updatesRole() throws Exception {
	}

	@Test // DATAJPA-509
	public void shouldUseExplicitlyConfiguredEntityNameInOrmXmlInCountQueries() {
	}

	@Test // DATAJPA-509
	public void shouldUseExplicitlyConfiguredEntityNameInOrmXmlInExistsQueries() {
	}

	@Test // DATAJPA-509
	public void shouldUseExplicitlyConfiguredEntityNameInDerivedCountQueries() {
	}
}
