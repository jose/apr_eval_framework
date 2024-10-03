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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.jpa.repository.query.StringQuery.InParameterBinding;
import org.springframework.data.jpa.repository.query.StringQuery.LikeParameterBinding;
import org.springframework.data.jpa.repository.query.StringQuery.ParameterBinding;
import org.springframework.data.repository.query.parser.Part.Type;

/**
 * Unit tests for {@link StringQuery}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Jens Schauder
 */
public class StringQueryUnitTests {

	public @Rule ExpectedException exception = ExpectedException.none();

	SoftAssertions softly = new SoftAssertions();

	@Test // DATAJPA-341
	public void doesNotConsiderPlainLikeABinding() {
	}

	@Test
	public void detectsPositionalLikeBindings() {
	}

	@Test
	public void detectsNamedLikeBindings() {
	}

	@Test // DATAJPA-461
	public void detectsNamedInParameterBindings() {
	}

	@Test // DATAJPA-461
	public void detectsMultipleNamedInParameterBindings() {
	}

	@Test // DATAJPA-461
	public void detectsPositionalInParameterBindings() {
	}

	@Test // DATAJPA-461
	public void detectsMultiplePositionalInParameterBindings() {
	}

	@Test // DATAJPA-373
	public void handlesMultipleNamedLikeBindingsCorrectly() {
	}

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-292, DATAJPA-362
	public void rejectsDifferentBindingsForRepeatedParameter() {
	}

	@Test // DATAJPA-461
	public void treatsGreaterThanBindingAsSimpleBinding() {
	}

	@Test // DATAJPA-473
	public void removesLikeBindingsFromQueryIfQueryContainsSimpleBinding() {
	}

	@Test // DATAJPA-483
	public void detectsInBindingWithParentheses() {
	}

	@Test // DATAJPA-545
	public void detectsInBindingWithSpecialFrenchCharactersInParentheses() {
	}

	@Test // DATAJPA-545
	public void detectsInBindingWithSpecialCharactersInParentheses() {
	}

	@Test // DATAJPA-545
	public void detectsInBindingWithSpecialAsianCharactersInParentheses() {
	}

	@Test // DATAJPA-545
	public void detectsInBindingWithSpecialCharactersAndWordCharactersMixedInParentheses() {
	}

	@Test //(expected = IllegalArgumentException.class) // DATAJPA-362
	public void rejectsDifferentBindingsForRepeatedParameter2() {
	}

	@Test // DATAJPA-712
	public void shouldReplaceAllNamedExpressionParametersWithInClause() {
	}

	@Test // DATAJPA-712
	public void shouldReplaceAllPositionExpressionParametersWithInClause() {
	}

	@Test // DATAJPA-864
	public void detectsConstructorExpressions() {
	}

	/**
	 * @see <a href="download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf">JPA 2.1
	 *      specification, section 4.8</a>
	 */
	@Test // DATAJPA-886
	public void detectsConstructorExpressionForDefaultConstructor() {
	}

	@Test // DATAJPA-1179
	public void bindingsMatchQueryForIdenticalSpelExpressions() {
	}

	@Test // DATAJPA-1235
	public void getProjection() {
	}

	void checkProjection(String query, String expected, String description) {

		softly.assertThat(new StringQuery(query).getProjection()) //
				.as("%s (%s)", description, query) //
				.isEqualTo(expected);
	}

	@Test // DATAJPA-1235
	public void getAlias() {
	}

	private void checkAlias(String query, String expected, String description) {

		softly.assertThat(new StringQuery(query).getAlias()) //
				.as("%s (%s)", description, query) //
				.isEqualTo(expected);
	}

	@Test // DATAJPA-1200
	public void testHasNamedParameter() {
	}

	@Test // DATAJPA-1235
	public void ignoresQuotedNamedParameterLookAlike() {
	}

	public void checkNumberOfNamedParameters(String query, int expectedSize, String label) {

		DeclaredQuery declaredQuery = DeclaredQuery.of(query);

		softly.assertThat(declaredQuery.hasNamedParameter()) //
				.describedAs("hasNamed Parameter " + label) //
				.isEqualTo(expectedSize > 0);
		softly.assertThat(declaredQuery.getParameterBindings()) //
				.describedAs("parameterBindings " + label) //
				.hasSize(expectedSize);
	}

	private void checkHasNamedParameter(String query, boolean expected, String label) {

		softly.assertThat(new StringQuery(query).hasNamedParameter()) //
				.describedAs(String.format("<%s> (%s)", query, label)) //
				.isEqualTo(expected);
	}

	private void assertPositionalBinding(Class<? extends ParameterBinding> bindingType, Integer position,
			ParameterBinding expectedBinding) {

		assertThat(bindingType.isInstance(expectedBinding), is(true));
		assertThat(expectedBinding, is(notNullValue()));
		assertThat(expectedBinding.hasPosition(position), is(true));
	}

	private void assertNamedBinding(Class<? extends ParameterBinding> bindingType, String parameterName,
			ParameterBinding expectedBinding) {

		assertThat(bindingType.isInstance(expectedBinding), is(true));
		assertThat(expectedBinding, is(notNullValue()));
		assertThat(expectedBinding.hasName(parameterName), is(true));
	}
}
