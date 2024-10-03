/*
 * Copyright 2013-2014 the original author or authors.
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

import org.junit.Test;
import org.springframework.data.jpa.repository.query.StringQuery.LikeParameterBinding;
import org.springframework.data.repository.query.parser.Part.Type;

/**
 * Unit tests for {@link LikeParameterBinding}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
public class LikeBindingUnitTests {

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsNullName() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsEmptyName() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsNullType() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsInvalidType() {
	}

	@Test //(expected = IllegalArgumentException.class)
	public void rejectsInvalidPosition() {
	}

	@Test
	public void setsUpInstanceForName() {
	}

	@Test
	public void setsUpInstanceForIndex() {
	}

	@Test
	public void augmentsValueCorrectly() {
	}

	private static void assertAugmentedValue(Type type, Object value) {

		LikeParameterBinding binding = new LikeParameterBinding("foo", type);
		assertThat(binding.prepare("value"), is(value));
	}
}
