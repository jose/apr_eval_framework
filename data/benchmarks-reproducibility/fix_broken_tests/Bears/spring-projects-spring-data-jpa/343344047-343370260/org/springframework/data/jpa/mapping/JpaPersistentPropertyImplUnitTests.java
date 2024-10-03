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
package org.springframework.data.jpa.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Version;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

/**
 * Unit tests for {@link JpaPersistentPropertyImpl}.
 * 
 * @author Oliver Gierke
 * @author Greg Turnquist
 */
@RunWith(MockitoJUnitRunner.class)
public class JpaPersistentPropertyImplUnitTests {

	@Mock Metamodel model;

	JpaMetamodelMappingContext context;
	JpaPersistentEntity<?> entity;

	@Before
	public void setUp() {

		context = new JpaMetamodelMappingContext(Collections.singleton(model));
		entity = context.getRequiredPersistentEntity(Sample.class);
	}

	@Test // DATAJPA-284
	public void considersOneToOneMappedPropertyAnAssociation() {
	}

	@Test // DATAJPA-376
	public void considersJpaTransientFieldsAsTransient() {
	}

	@Test // DATAJPA-484
	public void considersEmbeddableAnEntity() {
	}

	@Test // DATAJPA-484
	public void considersEmbeddablePropertyAnAssociation() {
	}

	@Test // DATAJPA-484
	public void considersEmbeddedPropertyAnAssociation() {
	}

	@Test // DATAJPA-619
	public void considersPropertyLevelAccessTypeDefinitions() {
	}

	@Test // DATAJPA-619
	public void propertyLevelAccessTypeTrumpsTypeLevelDefinition() {
	}

	@Test // DATAJPA-619
	public void considersJpaAccessDefinitionAnnotations() {
	}

	@Test // DATAJPA-619
	public void springDataAnnotationTrumpsJpaIfBothOnTypeLevel() {
	}

	@Test // DATAJPA-619
	public void springDataAnnotationTrumpsJpaIfBothOnPropertyLevel() {
	}

	@Test // DATAJPA-605
	public void detectsJpaVersionAnnotation() {
	}

	@Test // DATAJPA-664
	@SuppressWarnings("rawtypes")
	public void considersTargetEntityTypeForPropertyType() {
	}

	@Test // DATAJPA-716
	public void considersNonUpdateablePropertyNotWriteable() {
	}

	@Test // DATAJPA-904
	public void isEntityWorksEvenWithManagedTypeWithNullJavaType() {
	}

	@Test // DATAJPA-1064
	public void simplePropertyIsNotConsideredAnAssociation() {
	}

	private JpaPersistentProperty getProperty(Class<?> ownerType, String propertyName) {

		JpaPersistentEntity<?> entity = context.getRequiredPersistentEntity(ownerType);
		return entity.getRequiredPersistentProperty(propertyName);
	}

	static class Sample {

		@OneToOne Sample other;
		@Transient String transientProp;
		SampleEmbeddable embeddable;
		@Embedded SampleEmbedded embedded;
	}

	@Embeddable
	static class SampleEmbeddable {

	}

	static class SampleEmbedded {

	}

	@Access(AccessType.PROPERTY)
	static class TypeLevelPropertyAccess {

		private String id;

		public String getId() {
			return id;
		}
	}

	static class PropertyLevelPropertyAccess {

		String field;
		String property;

		/**
		 * @return the property
		 */
		@org.springframework.data.annotation.AccessType(Type.PROPERTY)
		public String getProperty() {
			return property;
		}
	}

	@Access(AccessType.FIELD)
	static class PropertyLevelDefinitionTrumpsTypeLevelOne {

		String field;
		String property;

		/**
		 * @return the property
		 */
		@org.springframework.data.annotation.AccessType(Type.PROPERTY)
		public String getProperty() {
			return property;
		}
	}

	@org.springframework.data.annotation.AccessType(Type.PROPERTY)
	static class PropertyLevelDefinitionTrumpsTypeLevelOne2 {

		@Access(AccessType.FIELD) String field;
		String property;

		/**
		 * @return the property
		 */
		public String getProperty() {
			return property;
		}
	}

	@org.springframework.data.annotation.AccessType(Type.FIELD)
	@Access(AccessType.PROPERTY)
	static class CompetingTypeLevelAnnotations {

		private String id;

		public String getId() {
			return id;
		}
	}

	@org.springframework.data.annotation.AccessType(Type.FIELD)
	@Access(AccessType.PROPERTY)
	static class CompetingPropertyLevelAnnotations {

		private String id;

		@org.springframework.data.annotation.AccessType(Type.FIELD)
		@Access(AccessType.PROPERTY)
		public String getId() {
			return id;
		}
	}

	static class SpringDataVersioned {

		@Version long version;
	}

	static class JpaVersioned {

		@javax.persistence.Version long version;
	}

	static class SpecializedAssociation {

		@ManyToOne(targetEntity = Implementation.class) Api api;
	}

	static interface Api {}

	static class Implementation {}

	static class WithReadOnly {
		@Column(updatable = false) String name;
		String updatable;
	}
}
