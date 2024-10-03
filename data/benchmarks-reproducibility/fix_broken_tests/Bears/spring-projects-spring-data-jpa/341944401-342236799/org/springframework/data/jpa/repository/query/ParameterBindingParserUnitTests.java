/*
 * Copyright 2017 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.springframework.data.jpa.repository.query.StringQuery.ParameterBinding;
import org.springframework.data.jpa.repository.query.StringQuery.ParameterBindingParser;

/**
 * Unit tests for the {@link ParameterBindingParser}.
 *
 * @author Jens Schauder
 */
public class ParameterBindingParserUnitTests {

	@Test // DATAJPA-1200
	public void idenficationOfParameters() {
	}

	public void checkHasParameter(SoftAssertions softly, String query, boolean containsParameter, String label) {

		List<ParameterBinding> bindings = new ArrayList<>();
		ParameterBindingParser.INSTANCE.parseParameterBindingsOfQueryIntoBindingsAndReturnCleanedQuery(query, bindings);
		softly.assertThat(bindings.size()) //
				.describedAs(String.format("<%s> (%s)", query, label)) //
				.isEqualTo(containsParameter ? 1 : 0);
	}
}
