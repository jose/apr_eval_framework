/*
 * Copyright 2015-2017 the original author or authors.
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;

import org.junit.Test;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

/**
 * Unit tests for {@link Jpa21Utils}.
 * 
 * @author Thomas Darimont
 * @author Oliver Gierke
 */
public class Jpa21UtilsUnitTests {

	@Test // DATAJPA-696
	public void shouldBuildCorrectSubgraphForJpaEntityGraph() throws Exception {
	}
}
