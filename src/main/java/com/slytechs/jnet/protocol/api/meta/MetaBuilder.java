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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slytechs.jnet.platform.api.incubator.StableValue;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderNotFound;
import com.slytechs.jnet.protocol.api.common.Packet;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.ProtocolTemplate;
import com.slytechs.jnet.protocol.api.meta.impl.DummyHeaderRegistry;
import com.slytechs.jnet.protocol.api.meta.impl.HeaderRegistry;
import com.slytechs.jnet.protocol.api.meta.impl.MetaReflections;
import com.slytechs.jnet.protocol.api.meta.spi.HeaderTemplateService;
import com.slytechs.jnet.protocol.api.meta.spi.impl.CachedHeaderTemplateService;
import com.slytechs.jnet.protocol.api.pack.PackId;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class MetaBuilder {
	private static final Logger logger = LoggerFactory.getLogger(MetaBuilder.class);

	private final HeaderTemplateService TEMPLATE_SERVICE = CachedHeaderTemplateService.CACHED.get();
	private final StableValue<MetaPacket> UNBOUND_PACKET = StableValue.ofSupplier(this::initPacket);
	private final Map<Long, MetaHeader> UNBOUND_HEADERS = new HashMap<>();

	private MetaPacket initPacket() {
		var attributes = MetaReflections.listAttributes(Packet.class);
		var template = loadTemplate(Packet.class, "packet");

		return new MetaPacket(this, attributes, template);
	}

	private MetaHeader initHeader(int id, int parentId) {
		boolean isOption = registry.isOption(id);

		Header header = isOption
				? registry.lookupOption(id, parentId)
				: registry.lookupHeader(id);

		ProtocolTemplate template = loadTemplate(header, header.headerName());
		var hdrClass = header.getClass();
		var attributes = MetaReflections.listAttributes(hdrClass);
		var fields = MetaReflections.listFields(hdrClass, template);

		var meta = new MetaHeader(header, fields, attributes, template);
		
		var attDebug = attributes.stream()
				.map(a -> a.name())
				.toList();
		
//		System.out.printf("initPacket:: %s, att=%s%n", meta.name(), attDebug, meta.attributes());
		
		return meta;
	}

	private ProtocolTemplate loadTemplate(Object targetObjOrClass, String targetName) {
		Class<?> targetClass = targetObjOrClass instanceof Class cl ? cl : targetObjOrClass.getClass();
		var resourceAnnotation = targetClass.getAnnotation(MetaResource.class);
		if (resourceAnnotation == null) {
			logger.warn("{} did not define a MetaResource template definition", targetName);
			return null;
		}

		var resource = resourceAnnotation.value();

		if (resource.isEmpty()) {
			logger.warn("{} did not define a MetaResource template definition", targetName);
			return null;
		}

		var arr = resource.split("#");
		var name = arr.length > 1 ? arr[1] : null;

		ProtocolTemplate template = TEMPLATE_SERVICE.loadHeaderTemplate(arr[0], name);
		if (template == null)
			logger.warn("{} template '{}#{}' definition not found", targetName, arr[0], name);

		return template;
	}

	private HeaderRegistry registry = new DummyHeaderRegistry();

	public MetaPacket buildPacket(Packet packet) {
		return UNBOUND_PACKET.get().bindTo(packet);
	}

	public MetaHeader buildHeader(Header header) {
		if (!header.isBound())
			throw new IllegalStateException("header [%s] not bound".formatted(header.headerName()));

		int id = header.id();
		var metaHeader = getHeaderFromCache(id, 0);

		return metaHeader.bindTo(header);
	}

	List<MetaHeader> listHeaders(Packet packet) {

//		System.out.println(packet.descriptor().toString(Detail.HIGH));

		var headerList = new ArrayList<MetaHeader>();
		long[] descriptorRecords = packet.descriptor().listHeaders();

		int lastId = 0;
		for (long r : descriptorRecords) {
			int id = PackId.decodeRecordId(r);
			boolean isOption = registry.isOption(id);

			Header header = isOption
					? registry.lookupOption(id, lastId)
					: registry.lookupHeader(id);

			try {
				header = packet.getHeader(header);
			} catch (HeaderNotFound e) {
				e.printStackTrace();
			}

			var metaHeader = getHeaderFromCache(id, isOption ? lastId : 0)
					.bindTo(packet);

			headerList.add(metaHeader);

			if (!isOption)
				lastId = id;
		}

		return headerList;
	}

	/**
	 * @param header
	 * @return
	 */
	private MetaHeader getHeaderFromCache(int id, int parentId) {
		long key = parentId << 32 | id;

		if (!UNBOUND_HEADERS.containsKey(key))
			UNBOUND_HEADERS.put(key, initHeader(id, parentId));

		return UNBOUND_HEADERS.get(key);
	}

}
