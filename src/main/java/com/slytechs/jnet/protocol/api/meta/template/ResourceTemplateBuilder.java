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

import static com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.BuilderFactory.*;

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

	private final Builder<ResourceTemplate> rootBuilder;

	public ResourceTemplateBuilder() {
		this.rootBuilder = new MapBuilder<ResourceTemplate>(Context.root()) {

			@Override
			protected void configure(MapBuilder<ResourceTemplate> builder) {
				builder
						.requireField("Templates", buildTemplates())

						.optionalField("Oveview", buildOverview())
						.optionalField("Defaults", buildDefaults())
						.optionalField("Macros", buildMacros())
						.optionalField("Imports", buildImports())

				;
			}

			@Override
			protected ResourceTemplate newInstance(Context ctx) {
				return new ResourceTemplate(

						ctx.getField("Overview", Overview.class),
						ctx.getField("Defaults", Defaults.class),
						ctx.getField("Macros", Macros.class),
						ctx.getField("Imports", Imports.class),
						ctx.getField("Templates", Templates.class)

				);
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
						.optionalField("align", Builder.buildEnum(Align.class))
						.optionalField("width", Builder.buildNumber())
						.optionalField("indent", Builder.buildNumber())

				;
			}

			@Override
			protected Defaults newInstance(Context ctx) {

				Defaults defaults = ctx.getSubField("defaults", Defaults.class);
				assert defaults != null : "defaults not initialized properly";

				return new Defaults(

						defaults,
						ctx.getField("indent", Integer.class),
						ctx.getField("width", Integer.class),
						ctx.getField("align", Align.class)

				);
			}
		};
	}

	private BuilderFactory<Item> buildFieldItem() {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Item> builder) {
				builder
						.requireField("Field", Builder.buildString())

						.optionalField("template", buildTemplatePattern())
						.optionalField("label", Builder.buildString())
						.optionalField("Defaults", buildDefaults())
						.optionalField("Tags", buildTags())
						.optionalField("Items", buildItems())

				;
			}

			@Override
			protected FieldItem newInstance(Context ctx) {
				return new FieldItem(

						ctx.getField("Field", String.class),
						ctx.getField("template", TemplatePattern.class),
						ctx.getField("label", String.class),
						ctx.getSubField("Defaults", Defaults.class),
						ctx.getField("Tags", Tags.class),
						ctx.getField("Items", Items.class)

				);
			}
		};
	}

	private BuilderFactory<HeaderDetail> buildHeaderDetail() {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<HeaderDetail> builder) {
				builder
						.requireField("Detail", Builder.buildEnum(Detail.class))

						.optionalField("summary", buildTemplatePattern())
						.optionalField("Defaults", buildDefaults())
						.optionalField("Items", buildItems())

				;
			}

			@Override
			protected HeaderDetail newInstance(Context ctx) {
				return new HeaderDetail(

						ctx.getField("Detail", Detail.class),
						ctx.getSubField("summary", TemplatePattern.class),
						ctx.getField("Items", Items.class),
						ctx.getField("Defaults", Defaults.class)

				);
			}
		};
	}

	private BuilderFactory<Item> buildHeaderItem() {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Item> builder) {
				builder
						.requireField("Header", Builder.buildString())

						.optionalField("summary", buildTemplatePattern())
						.optionalField("repeatable", Builder.buildBoolean())
						.optionalField("Defaults", buildDefaults())
						.optionalField("Tags", buildTags())
						.optionalField("Items", buildItems())

				;
			}

			@Override
			protected HeaderItem newInstance(Context ctx) {
				return new HeaderItem(

						ctx.getField("Header", String.class),
						ctx.getField("summary", TemplatePattern.class),
						ctx.getField("repeatable", false),
						ctx.getSubField("Defaults", Defaults.class),
						ctx.getField("Tags", Tags.class),
						ctx.getField("Items", Items.class)

				);
			}
		};
	}

	private BuilderFactory<Template> buildHeaderTemplate() {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Template> builder) {
				builder
						.requireField("Header", Builder.buildString())
						.requireField("Details", buildHeaderTemplateDetails())

						.optionalField("class", Builder.buildString())
						.optionalField("Imports", buildImports())
						.optionalField("summary", buildTemplatePattern())

				;
			}

			@Override
			protected Template newInstance(Context ctx) {
				return new HeaderTemplate(

						ctx.getField("name", String.class),
						ctx.getField("Details", HeaderTemplate.Details.class),
						ctx.getSubField("Macros", Macros.class),
						ctx.getSubField("Defaults", Defaults.class)

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

				return new Details(list);
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
								value = map.get("import");

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
		return context -> new ListBuilder<>(
				context,
				ofAnyFactory(
						buildHeaderItem(),
						buildFieldItem(),
						buildInfoItem())) {

			/**
			 * @see com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.ListBuilder#configure(com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.ListBuilder)
			 */
			@Override
			protected void configure(ListBuilder<Items, Item> builder) {
				builder.minSize(1)
						.map(this::inflactInfoItems);
			}

			private Object inflactInfoItems(Object value) {
				if (value instanceof Map<?, ?> map
						&& (map.containsKey("Header")
								|| map.containsKey("Field")
								|| map.containsKey("Info"))) {
					return value;

				} else if (value instanceof Map map) {
					// Missing Info and its not a Field or a Header, inflate in new map so we can
					// modify
					@SuppressWarnings("unchecked")
					var newMap = new HashMap<String, Object>(map);
					newMap.put("Info", "");

					value = newMap;

				} else if (value instanceof String templateString) {
					var newMap = new HashMap<String, Object>();
					newMap.put("Info", "");
					newMap.put("template", templateString);

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

	private BuilderFactory<Item> buildInfoItem() {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Item> builder) {
				builder
						.requireField("Info", Builder.buildString())

						.optionalField("template", buildTemplatePattern())
						.optionalField("label", Builder.buildString())
						.optionalField("Defaults", buildDefaults())
						.optionalField("Tags", buildTags())
						.optionalField("Items", buildItems())

				;
			}

			@Override
			protected InfoItem newInstance(Context ctx) {
				return new InfoItem(

						ctx.getField("Info", String.class),
						ctx.getField("template", TemplatePattern.class),
						ctx.getField("label", String.class),
						ctx.getSubField("Defaults", Defaults.class),
						ctx.getField("Tags", Tags.class),
						ctx.getField("Items", Items.class)

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
						.optionalField("align", Builder.buildEnum(Align.class))
						.optionalField("width", Builder.buildNumber())
						.optionalField("indent", Builder.buildNumber())
						.customBuilder("all-macros", this::mapAsFieldMap)

				;
			}

			@SuppressWarnings("unchecked")
			private Map<String, Object> mapAsFieldMap(Object value) {
				assert value instanceof Map<?, ?> : "expecting a map";

				return (Map<String, Object>) value;
			}

			@Override
			protected Macros newInstance(Context ctx) {

				Macros macros = ctx.getSubField("macros", Macros.class);
				assert macros != null : "macros not initialized properly";

				return new Macros(

						macros,
						ctx.getField("all-macros", TYPE)

				);
			}
		};
	}

	private BuilderFactory<Overview> buildOverview() {
		return context -> new MapBuilder<>(context) {

			@Override
			protected void configure(MapBuilder<Overview> builder) {
				builder
						.requireField("description", Builder.buildString())
						.optionalField("name", Builder.buildString())
						.optionalField("version", Builder.buildString())

				;
			}

			@Override
			protected Overview newInstance(Context ctx) {
				return new Overview(

						ctx.getField("String", String.class),
						ctx.getField("name", String.class),
						ctx.getField("version", String.class)

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

				return new TemplatePattern(

						templateString,
						context.getSubField("formats", FormatRegistry.of()),
						context.getSubField("Macros", Macros.class)

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
