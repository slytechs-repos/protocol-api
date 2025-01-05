/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
 *
 * Licensed under the Sly Technologies Free License (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.slytechs.com/free-license-text
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.slytechs.jnet.protocol.api.meta;

import com.slytechs.jnet.platform.api.util.format.Detail;

public interface MetaPredicates {

	interface OpenPredicate {

		interface OfDepth {
			boolean testOpen(int level, int depth, int maxDepth);
		}

		interface OfElement {
			boolean testOpen(MetaElement element);
		}

		interface OfLevel {
			boolean testOpen(int level);
		}

		static OpenPredicate depth(OpenPredicate.OfDepth depthPredicate) {
			return (level, depth, maxDepth, _) -> depthPredicate.testOpen(level, depth, maxDepth);
		}

		static OpenPredicate element(OpenPredicate.OfElement elementPredicate) {
			return (_, _, _, element) -> elementPredicate.testOpen(element);
		}

		static OpenPredicate level(OpenPredicate.OfLevel levelPredicate) {
			return (level, _, _, _) -> levelPredicate.testOpen(level);
		}

		static OpenPredicate level(Detail detail, MetaElement element) {
			return (level, _, _, _) -> level < detail.ordinal() && !element.isEmpty();
		}

		boolean testOpen(int level, int depth, int maxDepth, MetaElement element);

	}
}