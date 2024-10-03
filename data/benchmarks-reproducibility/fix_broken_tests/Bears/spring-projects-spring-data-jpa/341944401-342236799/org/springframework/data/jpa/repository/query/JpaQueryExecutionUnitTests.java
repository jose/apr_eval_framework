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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.JpaQueryExecution.ModifyingExecution;
import org.springframework.data.jpa.repository.query.JpaQueryExecution.PagedExecution;
import org.springframework.data.repository.query.DefaultParameters;
import org.springframework.data.repository.query.Parameters;

/**
 * Unit test for {@link JpaQueryExecution}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 * @author Nicolas Cirigliano
 * @author Jens Schauder
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class JpaQueryExecutionUnitTests {

	@Mock EntityManager em;
	@Mock AbstractStringBasedJpaQuery jpaQuery;
	@Mock Query query;
	@Mock JpaQueryMethod method;

	@Mock TypedQuery<Long> countQuery;

	@Before
	public void setUp(){

		when(query.executeUpdate()).thenReturn(0);
		when(jpaQuery.createQuery(Mockito.any(Object[].class))).thenReturn(query);
		when(jpaQuery.getQueryMethod()).thenReturn(method);
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsNullQuery() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsNullBinder() throws Exception {
	}

	@Test
	public void transformsNoResultExceptionToNull() {
	}

	@Test // DATAJPA-806
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void modifyingExecutionFlushesEntityManagerIfSet() {
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void modifyingExecutionClearsEntityManagerIfSet() {
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void allowsMethodReturnTypesForModifyingQuery() throws Exception {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test //(expected = IllegalArgumentException.class)
	public void modifyingExecutionRejectsNonIntegerOrVoidReturnType() throws Exception {
	}

	@Test // DATAJPA-124, DATAJPA-912
	public void pagedExecutionRetrievesObjectsForPageableOutOfRange() throws Exception {
	}

	@Test // DATAJPA-477, DATAJPA-912
	public void pagedExecutionShouldNotGenerateCountQueryIfQueryReportedNoResults() throws Exception {
	}

	@Test // DATAJPA-912
	public void pagedExecutionShouldUseCountFromResultIfOffsetIsZeroAndResultsWithinPageSize() throws Exception {
	}

	@Test // DATAJPA-912
	public void pagedExecutionShouldUseCountFromResultWithOffsetAndResultsWithinPageSize() throws Exception {
	}

	@Test // DATAJPA-912
	public void pagedExecutionShouldUseRequestCountFromResultWithOffsetAndResultsHitLowerPageSizeBounds()
			throws Exception {
	}

	@Test // DATAJPA-912
	public void pagedExecutionShouldUseRequestCountFromResultWithOffsetAndResultsHitUpperPageSizeBounds()
			throws Exception {
	}

	@Test // DATAJPA-951
	public void doesNotPreemtivelyWrapResultIntoOptional() throws Exception {
	}

	public static void sampleMethod(Pageable pageable) {}

	static class StubQueryExecution extends JpaQueryExecution {

		@Override
		protected Object doExecute(AbstractJpaQuery query, Object[] values) {
			return null;
		}
	}
}
