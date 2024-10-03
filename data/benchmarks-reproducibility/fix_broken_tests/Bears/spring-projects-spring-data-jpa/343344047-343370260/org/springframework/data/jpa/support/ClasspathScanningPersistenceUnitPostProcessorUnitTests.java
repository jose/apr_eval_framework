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
package org.springframework.data.jpa.support;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.persistence.Entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for {@link ClasspathScanningPersistenceUnitPostProcessor}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(MockitoJUnitRunner.class)
public class ClasspathScanningPersistenceUnitPostProcessorUnitTests {

	@Mock MutablePersistenceUnitInfo pui;
	String basePackage = getClass().getPackage().getName();

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsNullBasePackage() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsEmptyBasePackage() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsNullMappingFileNamePattern() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsEmptyMappingFileNamePattern() {
	}

	@Test
	public void findsEntityClassesForBasePackage() {
	}

	@Test // DATAJPA-407
	public void findsMappingFile() {
	}

	@Test // DATAJPA-353, DATAJPA-407
	public void shouldFindJpaMappingFilesFromMultipleLocationsOnClasspath() {
	}

	@Test // DATAJPA-519
	public void shouldFindJpaMappingFilesFromNestedJarLocationsOnClasspath() {
	}

	@Entity
	public static class SampleEntity {}
}
