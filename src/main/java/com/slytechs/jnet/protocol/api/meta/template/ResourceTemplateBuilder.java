/*
 * Sly Technologies Free License
 * 
 * Copyright 2025 Sly Technologies Inc.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.platform.api.util.function.TypeLiteral;
import com.slytechs.jnet.protocol.api.meta.FormatRegistry;
import com.slytechs.jnet.protocol.api.meta.template.Defaults.Align;
import com.slytechs.jnet.protocol.api.meta.template.Import.Imports;
import com.slytechs.jnet.protocol.api.meta.template.Item.FieldItem;
import com.slytechs.jnet.protocol.api.meta.template.Item.HeaderItem;
import com.slytechs.jnet.protocol.api.meta.template.Item.InfoItem;
import com.slytechs.jnet.protocol.api.meta.template.Item.Items;
import com.slytechs.jnet.protocol.api.meta.template.Template.HeaderTemplate;
import com.slytechs.jnet.protocol.api.meta.template.Template.HeaderTemplate.Details;
import com.slytechs.jnet.protocol.api.meta.template.Template.HeaderTemplate.Details.HeaderDetail;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class ResourceTemplateBuilder extends TreeBuilder<ResourceTemplate> {

	/**
	 * 
	 */
	private static final String CLASS = "class";

	private static final String FORMATS = "formats";

	private static final String IMPORT = "import";

	private static final String VERSION = "version";

	private static final String NAME = "name";

	private static final String INFO = "Info";

	private static final String ALL_MACROS = "all-macros";

	private static final String DETAILS = "Details";

	private static final String HEADER = "Header";

	private static final String REPEATABLE = "repeatable";

	private static final String SUMMARY = "summary";

	private static final String TAGS = "Tags";

	private static final String DETAIL = "Detail";

	private static final String LABEL = "label";

	private static final String TEMPLATE = "template";

	private static final String ITEMS = "Items";

	private static final String FIELD = "Field";

	private static final String INDENT = "indent";

	private static final String WIDTH = "width";

	private static final String ALIGN = "align";

	private static final String TEMPLATES = "Templates";

	private static final String IMPORTS = "Imports";

	private static final String DEFAULTS = "Defaults";

	private static final String OVERVIEW = "Overview";

	private static final String DESCRIPTION = "description";

	public static final String MACROS = "Macros";

	private final Builder<ResourceTemplate> rootBuilder;

	public ResourceTemplateBuilder() {
		this.rootBuilder = new MapBuilder<>(Context.root()) {

			@Override
			protected void configure(MapBuilder<ResourceTemplate> builder) {
				// Move initialization to root context
				Context root = context.getRoot();
				root.put(MACROS, Macros.root());
				root.put(DEFAULTS, Defaults.root());

				builder
						.requireField(TEMPLATES, buildTemplates())
						.requireField(OVERVIEW, buildOverview())
						.optionalField(DEFAULTS, buildDefaults())
						.optionalField(MACROS, buildMacros())
						.optionalField(IMPORTS, buildImports());
			}

			@Override
			protected ResourceTemplate newInstance(Context ctx) {
				Defaults defaults = ctx.getSubField(DEFAULTS, Defaults.class);
				if (defaults == null) {
					defaults = new Defaults(null, 50, 50, Align.LEFT); // Create default instance
				}

				return new ResourceTemplate(
						ctx.requireNonNull(OVERVIEW, Overview.class),
						defaults,
						ctx.requireNonNull(MACROS, Macros.class),
						ctx.getField(IMPORTS, Imports.class),
						ctx.getField(TEMPLATES, Templates.class));
			}
		};
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.template.TreeBuilder#build(java.lang.Object)
	 */
	@Override
	public ResourceTemplate build(Object target) {
		return rootBuilder.build(target);
	}

	private BuilderFactory<Defaults> buildDefaults() {
		return context -> new MapBuilder<>(context) {
			@Override
			protected void configure(MapBuilder<Defaults> builder) {
				builder
						.optionalField(ALIGN, Builder.buildEnum(Align.class))
						.optionalField(WIDTH, Builder.buildNumber())
						.optionalField(INDENT, Builder.buildNumber());
			}

			@Override
			protected Defaults newInstance(Context ctx) {
				// Get parent defaults first
				Defaults parentDefaults = ctx.getSubField(DEFAULTS, Defaults.class);

				// Use parent values as defaults if not overridden locally
				Integer width = ctx.getSubField(WIDTH, Integer.class);
				width = (width != null) ? width : (parentDefaults != null ? parentDefaults.width() : 50);

				Integer indent = ctx.getSubField(INDENT, Integer.class);
				indent = (indent != null) ? indent : (parentDefaults != null ? parentDefaults.indent() : 50);

				Align align = ctx.getSubField(ALIGN, Align.class);
				align = (align != null) ? align : (parentDefaults != null ? parentDefaults.align() : Align.LEFT);

				return new Defaults(parentDefaults, indent, width, align);
			}
		};
	}

	private BuilderFactory<Item> buildFieldItem(Builder<Items> itemsBuilder) {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Item> builder) {
				builder
						.requireField(FIELD, Builder.buildString())

						.optionalField(TEMPLATE, buildTemplatePattern())
						.optionalField(LABEL, Builder.buildString())
						.optionalField(DEFAULTS, buildDefaults())
						.optionalField(TAGS, buildTags())
						.optionalField(ITEMS, itemsBuilder)

				;
			}

			@Override
			protected FieldItem newInstance(Context ctx) {
				return new FieldItem(

						ctx.getField(FIELD, String.class),
						ctx.getField(TEMPLATE, TemplatePattern.class),
						ctx.getField(LABEL, String.class),
						ctx.requireNonNull(DEFAULTS, Defaults.class),
						ctx.getField(TAGS, Tags.class),
						ctx.getField(ITEMS, Items.class)

				);
			}
		};
	}

	private BuilderFactory<HeaderDetail> buildHeaderDetail() {
		return context -> new MapBuilder<>(context) {
			@Override
			protected void configure(MapBuilder<HeaderDetail> builder) {
				builder
						.requireField(DETAIL, Builder.buildEnum(Detail.class))
						.optionalField(SUMMARY, buildTemplatePattern())
						.optionalField(DEFAULTS, buildDefaults())
						.optionalField(ITEMS, buildItems());
			}

			@Override
			protected HeaderDetail newInstance(Context ctx) {
				return new HeaderDetail(
						ctx.getField(DETAIL, Detail.class),
						ctx.getSubField(SUMMARY, TemplatePattern.class),
						ctx.getField(ITEMS, Items.class),
						ctx.getSubField(DEFAULTS, Defaults.class) // Changed from requireNonNull
				);
			}
		};
	}

	private BuilderFactory<Item> buildHeaderItem(Builder<Items> builder) {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Item> builder) {
				builder
						.requireField(HEADER, Builder.buildString())

						.optionalField(SUMMARY, buildTemplatePattern())
						.optionalField(REPEATABLE, Builder.buildBoolean())
						.optionalField(DEFAULTS, buildDefaults())
						.optionalField(TAGS, buildTags())
						.optionalField(ITEMS, builder)

				;
			}

			@Override
			protected HeaderItem newInstance(Context ctx) {
				return new HeaderItem(

						ctx.getField(HEADER, String.class),
						ctx.getField(SUMMARY, TemplatePattern.class),
						ctx.getField(REPEATABLE, false),
						ctx.requireNonNull(DEFAULTS, Defaults.class),
						ctx.getField(TAGS, Tags.class),
						ctx.getField(ITEMS, Items.class)

				);
			}
		};
	}

	private BuilderFactory<Template> buildHeaderTemplate() {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Template> builder) {
				builder
						.requireField(HEADER, Builder.buildString())
						.requireField(DETAILS, buildHeaderTemplateDetails())

						.optionalField(CLASS, Builder.buildString())
						.optionalField(IMPORTS, buildImports())
						.optionalField(SUMMARY, buildTemplatePattern())

				;
			}

			@Override
			protected Template newInstance(Context ctx) {
				return new HeaderTemplate(

						ctx.getField(NAME, String.class),
						ctx.getField(DETAILS, HeaderTemplate.Details.class),
						ctx.requireNonNull(MACROS, Macros.class),
						ctx.requireNonNull(DEFAULTS, Defaults.class)

				);
			}
		};
	}

	private BuilderFactory<HeaderTemplate.Details> buildHeaderTemplateDetails() {
		return context -> new ListBuilder<>(context, buildHeaderDetail()) {

			@Override
			protected Details newInstance(
					Context context,
					List<HeaderDetail> list) {

				return Details.ofInheritMissing(list);
			}
		};
	}

	private BuilderFactory<Imports> buildImports() {
		return context -> new ListBuilder<>(context, StringBuilder.of(Import::new)) {

			/**
			 * @see com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.ListBuilder#configure(com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.ListBuilder)
			 */
			@Override
			protected void configure(ListBuilder<Imports, Import> builder) {
				builder.minSize(1)
						.map(value -> {
							if (value instanceof Map<?, ?> map)
								value = map.get(IMPORT);

							return value;
						});
			}

			@Override
			protected Imports newInstance(Context context, List<Import> list) {
				return new Imports(list);
			}

		};
	}

	private BuilderFactory<Items> buildItems() {
		return context -> new ListBuilder<Items, Item>(context) {

			/**
			 * @see com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.ListBuilder#configure(com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.ListBuilder)
			 */
			@Override
			protected void configure(ListBuilder<Items, Item> builder) {
				builder.minSize(1)
						.map(this::inflateInfoItems)
						.elementBuilder(BuilderFactory.ofAnyFactory(
								buildHeaderItem(builder),
								buildFieldItem(builder),
								buildInfoItem(builder)));
			}

			private Object inflateInfoItems(Object value) {
				if (value instanceof Map<?, ?> map
						&& (map.containsKey(HEADER)
								|| map.containsKey(FIELD)
								|| map.containsKey(INFO))) {
					return value;

				} else if (value instanceof Map map) {
					// Missing Info and its not a Field or a Header, inflate in new map so we can
					// modify
					@SuppressWarnings("unchecked")
					var newMap = new HashMap<String, Object>(map);
					newMap.put(INFO, "");

					value = newMap;

				} else if (value instanceof String templateString) {
					var newMap = new HashMap<String, Object>();
					newMap.put(INFO, "");
					newMap.put(TEMPLATE, templateString);

					value = newMap;
				}

				return value;
			}

			@Override
			protected Items newInstance(Context context, List<Item> newList) {
				return new Items(newList);
			}
		};
	}

	private BuilderFactory<Item> buildInfoItem(Builder<Items> builder) {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Item> builder) {
				builder
						.requireField(INFO, Builder.buildString())

						.optionalField(TEMPLATE, buildTemplatePattern())
						.optionalField(LABEL, Builder.buildString())
						.optionalField(DEFAULTS, buildDefaults())
						.optionalField(TAGS, buildTags())
						.optionalField(ITEMS, builder)

				;
			}

			@Override
			protected InfoItem newInstance(Context ctx) {
				return new InfoItem(

						ctx.getField(INFO, String.class),
						ctx.getField(TEMPLATE, TemplatePattern.class),
						ctx.getField(LABEL, String.class),
						ctx.getSubField(DEFAULTS, Defaults.class),
						ctx.getField(TAGS, Tags.class),
						ctx.getField(ITEMS, Items.class)

				);
			}
		};
	}

	private BuilderFactory<Macros> buildMacros() {
		return context -> new MapBuilder<>(context) {

			private static final TypeLiteral<Map<String, String>> TYPE = new TypeLiteral<>() {};

			@Override
			protected void configure(MapBuilder<Macros> builder) {
				builder
						.optionalField(ALIGN, Builder.buildEnum(Align.class))
						.optionalField(WIDTH, Builder.buildNumber())
						.optionalField(INDENT, Builder.buildNumber())
						.customBuilder(ALL_MACROS, this::mapAsFieldMap)

				;
			}

			@SuppressWarnings("unchecked")
			private Map<String, Object> mapAsFieldMap(Object value) {
				assert value instanceof Map<?, ?> : "expecting a map";

				return (Map<String, Object>) value;
			}

			@Override
			protected Macros newInstance(Context ctx) {

				Macros macros = ctx.requireNonNull(MACROS, Macros.class);
				assert macros != null : "macros not initialized properly";

				return new Macros(

						macros,
						ctx.getField(ALL_MACROS, TYPE)

				);
			}
		};
	}

	private BuilderFactory<Overview> buildOverview() {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Overview> builder) {
				builder
						.requireField(DESCRIPTION, Builder.buildString())
						.optionalField(NAME, Builder.buildString())
						.optionalField(VERSION, Builder.buildString())

				;
			}

			@Override
			protected Overview newInstance(Context ctx) {
				return new Overview(

						ctx.getField(DESCRIPTION, String.class),
						ctx.getField(NAME, String.class),
						ctx.getField(VERSION, String.class)

				);
			}
		};
	}

	private Builder<Tags> buildTags() {
		return new Builder<>() {

			@SuppressWarnings("unchecked")
			@Override
			public Tags build(Object value) throws TemplateException {
				if (value instanceof List<?> list)
					return new Tags(new ArrayList<>((List<String>) list));

				else if (value instanceof String str)
					return new Tags(List.of(str));

				return null;
			}
		};
	}

	private BuilderFactory<TemplatePattern> buildTemplatePattern() {
		return context -> new StringBuilder<>() {

			@Override
			public TemplatePattern newInstance(String templateString) {
				if (templateString == null)
					return null;

				context.entryDetails()
						.forEach(System.out::println);

				context.contexts()
						.forEach(Detail.HIGH::printlnToStdout);

				return new TemplatePattern(

						templateString,
						context.getSubField(FORMATS, FormatRegistry.of()),
						context.requireNonNull(MACROS, Macros.class)

				);
			}
		};
	}

	private BuilderFactory<Templates> buildTemplates() {
		return context -> new ListBuilder<>(context, buildHeaderTemplate()) {

			@Override
			protected Templates newInstance(Context context, List<Template> list) {
				return new Templates(list);
			}

		};
	}
}
