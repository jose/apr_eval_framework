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
package org.springframework.data.jpa.domain;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.domain.Sort.Direction.*;
import static org.springframework.data.jpa.domain.JpaSort.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.JpaSort.JpaOrder;
import org.springframework.data.jpa.domain.JpaSort.Path;
import org.springframework.data.jpa.domain.sample.Address_;
import org.springframework.data.jpa.domain.sample.MailMessage_;
import org.springframework.data.jpa.domain.sample.MailSender_;
import org.springframework.data.jpa.domain.sample.User_;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for {@link JpaSort}. This has to be an integration test due to the design of the statically
 * generated meta-model classes. The properties cannot be referred to statically (quite a surprise, as they're static)
 * but only after they've been enhanced by the persistence provider. This requires an {@link EntityManagerFactory} to be
 * bootstrapped.
 * 
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:infrastructure.xml")
public class JpaSortTests {

	private static final @Nullable Attribute<?, ?> NULL_ATTRIBUTE = null;
	private static final Attribute<?, ?>[] EMPTY_ATTRIBUTES = new Attribute<?, ?>[0];

	private static final @Nullable PluralAttribute<?, ?, ?> NULL_PLURAL_ATTRIBUTE = null;
	private static final PluralAttribute<?, ?, ?>[] EMPTY_PLURAL_ATTRIBUTES = new PluralAttribute<?, ?, ?>[0];

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-12
	public void rejectsNullAttribute() {
	}

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-12
	public void rejectsEmptyAttributes() {
	}

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-12
	public void rejectsNullPluralAttribute() {
	}

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-12
	public void rejectsEmptyPluralAttributes() {
	}

	@Test // DATAJPA-12
	public void sortBySinglePropertyWithDefaultSortDirection() {
	}

	@Test // DATAJPA-12
	public void sortByMultiplePropertiesWithDefaultSortDirection() {
	}

	@Test // DATAJPA-12
	public void sortByMultiplePropertiesWithDescSortDirection() {
	}

	@Test // DATAJPA-12
	public void combiningSortByMultipleProperties() {
	}

	@Test // DATAJPA-12
	public void combiningSortByMultiplePropertiesWithDifferentSort() {
	}

	@Test // DATAJPA-12
	public void combiningSortByNestedEmbeddedProperty() {
	}

	@Test // DATAJPA-12
	public void buildJpaSortFromJpaMetaModelSingleAttribute() {
	}

	@Test // DATAJPA-12
	public void buildJpaSortFromJpaMetaModelNestedAttribute() {
	}

	@Test // DATAJPA-702
	public void combiningSortByMultiplePropertiesWithDifferentSortUsingSimpleAnd() {
	}

	@Test // DATAJPA-702
	public void combiningSortByMultiplePathsWithDifferentSortUsingSimpleAnd() {
	}

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-702
	public void rejectsNullAttributesForCombiningCriterias() {
	}

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-702
	public void rejectsNullPathsForCombiningCriterias() {
	}

	@Test // DATAJPA-702
	public void buildsUpPathForPluralAttributesCorrectly() {
	}

	@Test // DATAJPA-965
	public void createsUnsafeSortCorrectly() {
	}

	@Test // DATAJPA-965
	public void createsUnsafeSortWithMultiplePropertiesCorrectly() {
	}

	@Test // DATAJPA-965
	public void combinesSafeAndUnsafeSortCorrectly() {
	}

	@Test // DATAJPA-965
	public void combinesUnsafeAndSafeSortCorrectly() {
	}
}
