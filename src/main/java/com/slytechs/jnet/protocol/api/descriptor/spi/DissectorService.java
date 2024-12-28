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
package com.slytechs.jnet.protocol.api.descriptor.spi;

import java.util.List;
import java.util.ServiceLoader;

import com.slytechs.jnet.protocol.api.descriptor.Descriptor;
import com.slytechs.jnet.protocol.api.descriptor.DescriptorType;
import com.slytechs.jnet.protocol.api.descriptor.Dissector;
import com.slytechs.jnet.protocol.api.descriptor.impl.CachedDissectorService;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface DissectorService {

	static DissectorService cached() {
		return CachedDissectorService.CACHED_SERVICE.get();
	}

	static List<Dissector> mergeAllServices() {

		var dissectors = ServiceLoader.load(DissectorService.class)
				.stream()
				.flatMap(s -> s.get().listDissectors().stream())
				.toList();

		return dissectors;
	}

	static <T extends Dissector> List<T> mergeServicesByType(Class<T> dissectorClass) {

		@SuppressWarnings("unchecked")
		var dissectors = ServiceLoader.load(DissectorService.class)
				.stream()
				.flatMap(s -> s.get().listDissectors().stream())
				.filter(d -> dissectorClass.isAssignableFrom(d.getClass()))
				.map(d -> (T) d)
				.toList();

		return dissectors;
	}

	List<Dissector> listDissectors();

	default <T extends Dissector> List<T> listByType(Class<T> dissectorClass) {
		@SuppressWarnings("unchecked")
		var dissectors = listDissectors().stream()
				.filter(d -> dissectorClass.isAssignableFrom(d.getClass()))
				.map(d -> (T) d)
				.toList();

		return dissectors;
	}

	default <T extends Dissector> List<T> listByType(DescriptorType<? extends Descriptor> type, Class<T> dissectorClass) {
		@SuppressWarnings("unchecked")
		var dissectors = listDissectors().stream()
				.filter(d -> dissectorClass.isAssignableFrom(d.getClass()))
				.map(d -> (T) d)
				.toList();

		return dissectors;
	}
}
