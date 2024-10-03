/*
 * Copyright 2014-2017 the original author or authors.
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
package org.springframework.data.jpa.convert.threeten;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.springframework.data.jpa.support.EntityManagerTestUtils.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.support.AbstractAttributeConverterIntegrationTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link Jsr310JpaConverters}.
 * 
 * @author Oliver Gierke
 */
@ContextConfiguration
@Transactional
public class Jsr310JpaConvertersIntegrationTests extends AbstractAttributeConverterIntegrationTests {

	@Configuration
	static class Config extends InfrastructureConfig {

		@Override
		protected String getPackageName() {
			return getClass().getPackage().getName();
		}
	}

	@PersistenceContext EntityManager em;

	@Test // DATAJPA-650
	public void usesJsr310JpaConverters() {
	}
}
