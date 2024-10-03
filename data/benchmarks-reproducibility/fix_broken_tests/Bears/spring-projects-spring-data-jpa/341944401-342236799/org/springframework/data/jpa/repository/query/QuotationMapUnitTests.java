/*
 * Copyright 2018 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.springframework.data.jpa.repository.query.StringQuery.QuotationMap;

/**
 * Unit tests for {@link QuotationMap}.
 *
 * @author Jens Schauder
 */
public class QuotationMapUnitTests {

	SoftAssertions softly = new SoftAssertions();

	@Test // DATAJPA-1235
	public void emptyStringDoesNotContainQuotes() {
	}

	@Test // DATAJPA-1235
	public void nullStringDoesNotContainQuotes() {
	}

	@Test // DATAJPA-1235
	public void simpleStringDoesNotContainQuotes() {
	}

	@Test // DATAJPA-1235
	public void fullySingleQuotedStringDoesContainQuotes() {
	}

	@Test // DATAJPA-1235
	public void fullyDoubleQuotedStringDoesContainQuotes() {
	}

	@Test // DATAJPA-1235
	public void stringWithEmptyQuotes() {
	}

	@Test // DATAJPA-1235
	public void doubleInSingleQuotes() {
	}

	@Test // DATAJPA-1235
	public void singleQuotesInDoubleQuotes() {
	}

	@Test // DATAJPA-1235
	public void escapedQuotes() {
	}

	@Test // DATAJPA-1235
	public void openEndedQuoteThrowsException() {
	}

	public void isNotQuoted(String query, Object label, int... indexes) {

		QuotationMap quotationMap = new QuotationMap(query);

		for (int index : indexes) {

			assertThat(quotationMap.isQuoted(index))
					.describedAs(String.format("(%s) %s does not contain a quote at %s", label, query, index)) //
					.isFalse();
		}
	}

	public void isQuoted(String query, Object label, int... indexes) {

		QuotationMap quotationMap = new QuotationMap(query);

		for (int index : indexes) {

			assertThat(quotationMap.isQuoted(index))
					.describedAs(String.format("(%s) %s does contain a quote at %s", label, query, index)).isTrue();
		}
	}
}
