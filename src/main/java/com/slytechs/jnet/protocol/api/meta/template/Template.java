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
package com.slytechs.jnet.protocol.api.meta.template;

import static java.util.function.Predicate.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.template.Item.Items;
import com.slytechs.jnet.protocol.api.meta.template.Template.HeaderTemplate.Details.HeaderDetail;

public interface Template {

	String name();

	Defaults defaults();

	Macros macros();

	TemplateDetail templateDetail(Detail detail);

	public interface TemplateDetail {

		Detail detail();

		TemplatePattern summary();

		Items items();

		Defaults defaults();

	}

	public record PacketTemplate(FrameTemplate.Details details) {
		public record Details(List<PacketDetail> list, PacketDetail[] array) {

			public Details(List<PacketDetail> list) {
				this(list, list.toArray(PacketDetail[]::new));
			}
		}

		public record PacketDetail(Detail detail, TemplatePattern summary, Items items, Defaults defaults)
				implements TemplateDetail {}
	}

	public record FrameTemplate(FrameTemplate.Details details) {
		public record Details(List<FrameDetail> list, FrameDetail[] array) {

			public Details(List<FrameDetail> list) {
				this(list, list.toArray(FrameDetail[]::new));
			}
		}

		public record FrameDetail(Detail detail, TemplatePattern summary, Items items, Defaults defaults)
				implements TemplateDetail {}
	}

	/**
	 * Represents a template for a protocol, containing a collection of detailed
	 * templates for different levels of detail, along with macros, defaults, and a
	 * hierarchical structure for metadata.
	 * <p>
	 * This class provides:
	 * <ul>
	 * <li>{@code name}: The unique name of the protocol.</li>
	 * <li>{@code detailMap}: A mapping of {@link Detail} levels to corresponding
	 * {@link DetailTemplate} objects.</li>
	 * <li>{@code detailArray}: An array of {@link DetailTemplate} objects indexed
	 * by {@code Detail.ordinal()}.</li>
	 * <li>{@code detailList}: An immutable list of {@link DetailTemplate} objects
	 * ordered by {@link Detail} enumeration.</li>
	 * <li>{@code macros}: A {@link Macros} instance for resolving macros within the
	 * protocol.</li>
	 * <li>{@code defaults}: Default settings such as alignment, indentation, and
	 * width.</li>
	 * </ul>
	 * <p>
	 * The {@code detailMap} is the primary input for constructing the protocol
	 * template, and it is automatically converted into the array and list formats
	 * for efficient access by ordinal index or as an ordered collection.
	 * </p>
	 *
	 * <p>
	 * Example Usage:
	 * 
	 * <pre>{@code
	 * Map<Detail, DetailTemplate> detailMap = Map.of(
	 *     Detail.BASIC, new DetailTemplate(Detail.BASIC, "Basic Summary", List.of(...), defaults, null),
	 *     Detail.DETAILED, new DetailTemplate(Detail.DETAILED, "Detailed Summary", List.of(...), defaults, null)
	 * );
	 *
	 * HeaderTemplate protocolTemplate = new HeaderTemplate(
	 *     "ExampleProtocol",
	 *     detailMap,
	 *     new Macros(null, Map.of("$example", "Example Macro")),
	 *     Defaults.root()
	 * );
	 *
	 * System.out.println(protocolTemplate.detail(Detail.BASIC)); // Access detail template by level
	 * }</pre>
	 * </p>
	 *
	 * @param name        The unique name of the protocol.
	 * @param detailMap   A mapping of {@link Detail} levels to corresponding
	 *                    {@link DetailTemplate} objects.
	 * @param detailArray An array of {@link DetailTemplate} objects, derived from
	 *                    {@code detailMap}, indexed by {@code Detail.ordinal()}.
	 * @param detailList  An immutable list of {@link DetailTemplate} objects
	 *                    ordered by {@link Detail} enumeration.
	 * @param macros      A {@link Macros} instance for resolving macros within the
	 *                    protocol.
	 * @param defaults    Default settings for the protocol, such as alignment and
	 *                    indentation.
	 * @see TemplateResource
	 */
	public record HeaderTemplate(
			String name,
			HeaderTemplate.Details headerDetails,
			Macros macros,
			Defaults defaults)
			implements Template {

		public record Details(List<HeaderDetail> list, HeaderDetail[] array) {

			/**
			 * Fill missing details or holes within the list. Missing details are inherited
			 * from the previous lower detail level or if only higher level details are
			 * provided, the lower detail inherits from the higher, ensuring that all
			 * details are provided.
			 *
			 * @param list the list
			 * @return the list
			 * @throws TemplateException if the list is empty or contains only null elements
			 */
			static List<HeaderDetail> fillMissingDetails(List<HeaderDetail> list) {

				if (list.stream().filter(not(Objects::isNull)).count() == Detail.values().length)
					return list;

				var last = list.stream()
						.filter(p -> p != null)
						.filter(not(Objects::isNull))
						.findFirst()
						.orElseThrow(() -> new TemplateException("empty header details section"));

				var array = new HeaderDetail[Detail.values().length];
				for (HeaderDetail d : list)
					array[d.detail.ordinal()] = d;

				for (int i = 0; i < array.length; i++) {

					if (array[i] == null) {
						array[i] = last;

					} else {
						last = array[i];
					}
				}

				return Arrays.asList(array);
			}

			public record HeaderDetail(
					Detail detail,
					TemplatePattern summary,
					Items items,
					Defaults defaults)
					implements TemplateDetail {}

			/**
			 * Inherits missing details from lower level or the next level if nothing else
			 * is available.
			 *
			 * @param list the detail list with possibly missing detail elements
			 * @return the details with filled in missing details
			 */
			public static Details ofInheritMissing(List<HeaderDetail> list) {
				var newList = fillMissingDetails(list);

				return new Details(newList, newList.toArray(HeaderDetail[]::new));
			}

			/**
			 * @param detail
			 * @return
			 */
			public HeaderDetail templateDetail(Detail detail) {
				return array[detail.ordinal()];
			}
		}

		/**
		 * Retrieves the {@link DetailTemplate} associated with the given {@link Detail}
		 * level.
		 * <p>
		 * This method provides efficient access to the detail template using the
		 * {@code ordinal()} value of the {@link Detail} enumeration.
		 * </p>
		 *
		 * @param detail The detail level for which to retrieve the template.
		 * @return The {@link DetailTemplate} associated with the given detail level, or
		 *         {@code null} if no template is defined.
		 */
		@Override
		public HeaderDetail templateDetail(Detail detail) {
			return headerDetails.templateDetail(detail);
		}
	}
}