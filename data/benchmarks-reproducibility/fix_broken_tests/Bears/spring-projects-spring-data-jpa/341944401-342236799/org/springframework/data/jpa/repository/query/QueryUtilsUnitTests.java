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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.jpa.repository.query.QueryUtils.*;

import java.util.Collections;
import java.util.Set;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.JpaSort;

/**
 * Unit test for {@link QueryUtils}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Komi Innocent
 * @author Christoph Strobl
 * @author Jens Schauder
 */
public class QueryUtilsUnitTests {

	static final String QUERY = "select u from User u";
	static final String FQ_QUERY = "select u from org.acme.domain.User$Foo_Bar u";
	static final String SIMPLE_QUERY = "from User u";
	static final String COUNT_QUERY = "select count(u) from User u";

	static final String QUERY_WITH_AS = "select u from User as u where u.username = ?";

	static final Matcher<String> IS_U = is("u");

	@Test
	public void createsCountQueryCorrectly() throws Exception {
	}

	@Test
	public void createsCountQueriesCorrectlyForCapitalLetterJPQL() {
	}

	@Test
	public void createsCountQueryForDistinctQueries() throws Exception {
	}

	@Test
	public void createsCountQueryForConstructorQueries() throws Exception {
	}

	@Test
	public void createsCountQueryForJoins() throws Exception {
	}

	@Test
	public void createsCountQueryForQueriesWithSubSelects() throws Exception {
	}

	@Test
	public void createsCountQueryForAliasesCorrectly() throws Exception {
	}

	@Test
	public void allowsShortJpaSyntax() throws Exception {
	}

	@Test
	public void detectsAliasCorrectly() throws Exception {
	}

	@Test
	public void allowsFullyQualifiedEntityNamesInQuery() {
	}

	@Test // DATAJPA-252
	public void detectsJoinAliasesCorrectly() {
	}

	@Test // DATAJPA-252
	public void doesNotPrefixOrderReferenceIfOuterJoinAliasDetected() {
	}

	@Test // DATAJPA-252
	public void extendsExistingOrderByClausesCorrectly() {
	}

	@Test // DATAJPA-296
	public void appliesIgnoreCaseOrderingCorrectly() {
	}

	@Test // DATAJPA-296
	public void appendsIgnoreCaseOrderingCorrectly() {
	}

	@Test // DATAJPA-342
	public void usesReturnedVariableInCOuntProjectionIfSet() {
	}

	@Test // DATAJPA-343
	public void projectsCOuntQueriesForQueriesWithSubselects() {
	}

	@Test //(expected = InvalidDataAccessApiUsageException.class) // DATAJPA-148
	public void doesNotPrefixSortsIfFunction() {
	}

	@Test // DATAJPA-377
	public void removesOrderByInGeneratedCountQueryFromOriginalQueryIfPresent() {
	}

	@Test // DATAJPA-375
	public void findsExistingOrderByIndependentOfCase() {
	}

	@Test // DATAJPA-409
	public void createsCountQueryForNestedReferenceCorrectly() {
	}

	@Test // DATAJPA-420
	public void createsCountQueryForScalarSelects() {
	}

	@Test // DATAJPA-456
	public void createCountQueryFromTheGivenCountProjection() {
	}

	@Test // DATAJPA-726
	public void detectsAliassesInPlainJoins() {
	}

	@Test // DATAJPA-736
	public void supportsNonAsciiCharactersInEntityNames() {
	}

	@Test // DATAJPA-798
	public void detectsAliasInQueryContainingLineBreaks() {
	}

	@Test // DATAJPA-815
	public void doesPrefixPropertyWith() {
	}

	@Test // DATAJPA-938
	public void detectsConstructorExpressionInDistinctQuery() {
	}

	@Test // DATAJPA-938
	public void detectsComplexConstructorExpression() {
	}

	@Test // DATAJPA-938
	public void detectsConstructorExpressionWithLineBreaks() {
	}

	@Test // DATAJPA-960
	public void doesNotQualifySortIfNoAliasDetected() {
	}

	@Test //(expected = InvalidDataAccessApiUsageException.class) // DATAJPA-965, DATAJPA-970
	public void doesNotAllowWhitespaceInSort() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void doesNotPrefixUnsageJpaSortFunctionCalls() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void doesNotPrefixMultipleAliasedFunctionCalls() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void doesNotPrefixSingleAliasedFunctionCalls() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void prefixesSingleNonAliasedFunctionCallRelatedSortProperty() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void prefixesNonAliasedFunctionCallRelatedSortPropertyWhenSelectClauseContainesAliasedFunctionForDifferentProperty() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void doesNotPrefixAliasedFunctionCallNameWithMultipleNumericParameters() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void doesNotPrefixAliasedFunctionCallNameWithMultipleStringParameters() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void doesNotPrefixAliasedFunctionCallNameWithUnderscores() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void doesNotPrefixAliasedFunctionCallNameWithDots() {
	}

	@Test // DATAJPA-965, DATAJPA-970
	public void doesNotPrefixAliasedFunctionCallNameWhenQueryStringContainsMultipleWhiteSpaces() {
	}

	@Test // DATAJPA-1000
	public void discoversCorrectAliasForJoinFetch() {
	}

	@Test // DATAJPA-1171
	public void doesNotContainStaticClauseInExistsQuery() {
	}

	private static void assertCountQuery(String originalQuery, String countQuery) {
		assertThat(createCountQueryFor(originalQuery), is(countQuery));
	}
}
