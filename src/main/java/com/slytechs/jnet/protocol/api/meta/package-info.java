/**
 * Provides a powerful meta or instrumented access to packet and protocol data.
 * Meta package provides a instrumentable way to access all packet data. A
 * {@code Packet} or {@code Header} can be wrapped in {@code Meta*} objects
 * which provide instrumented access to various underlying object attributes and
 * fields. In addition to direct network object instrumentation, additional
 * context information can be associated as well, including various tables and
 * attribute data. Attributes are similar to {@code MetaField} objects but are
 * either computed or extra information associated with the underlying Meta
 * object.
 * <p>
 * Note that with {@code MetaContext} any type of object can be used as a "key"
 * in mapped context types and as well as any object as a value. So storage and
 * lookup of complex keys and values is possible besides the mainly discussed
 * {@code MetaField} value types. Further explained below.
 * </p>
 * 
 * <h2>MetaPacket, MetaHeader, MetaField</h2>
 * <p>
 * A packet once captured, or created, can be wrapped as a {@code MetaPacket}
 * object. This allows so called "parsing" of packet information to be exposed
 * as meta objects. Each {@code MetaPacket} can be broken down as a list of
 * {@code MetaHeader} and sub header objects and further each header can be
 * broken down as a set of {@code MetaField} objects and sub-fields.
 * </p>
 * <h2>Meta attributes</h2>
 * <p>
 * Additionally, a subset of {@code MetaField} objects of type
 * {@code MetaType.ATTRIBUTE} is also provided that derives its values from
 * either computed or external sources. For example,
 * {@code MetaPacket.getField("frameNo")} will return an arbitrary assigned
 * frame number for a given packet as an attribute, where frame numbers are not
 * part of the captured packet data but assigned afterwards, yet are provided in
 * meta packets as readable attributes. Both objects types are accessed the same
 * way and only vary by {@code MetaType} assignment. See
 * {@link MetaField#metaType()}.
 * </p>
 * <h2>MetaContext</h2>
 * <p>
 * In the meta package, all meta objects are part of a larger
 * {@code MetaContext} instrumentation. This allows context specific information
 * to be associated with capture, analysis, formatting and other types of
 * sessions with a set of meta objects. The {@code MetaContext} provides a
 * hierarchal stateful context configuration and state to such meta sessions.
 * Further more, {@code MetaContext} objects are assembled into named domains
 * with easy grouping and lookup functions for accessing data out of the overall
 * domain.
 * </p>
 * <h2>MetaDomain interface</h2>
 * <p>
 * The meta context is divided up into a set of named domains where each domain
 * can be looked up by a {@code MetaPath} object. This allows any part of any
 * meta context to be accessed from any other via a meta paths (think filesystem
 * paths). Since all object types part of the meta package are linked into a
 * meta context, they all become accessible from anywhere in the meta domain.
 * For example, all meta packets, headers and even fields are mini meta domains
 * them selves, you can traverse or access any part of the meta domain.
 * </p>
 * <h2>Types of contexts</h2>
 * <p>
 * There are several types of meta contexts which are used by the meta package.
 * Each type of context is designed to best represent certain type of data. For
 * example each {@code MetaPacket} object is associated with both a
 * {@code MetaContextMapped} and {@code MetaContextIndexed} types, so that each
 * header of the {@code MetaPacket} can be accessed as keyed map element and
 * indexed element as well. The same goes for headers and each field of a
 * header. While other elements, such as natively derived IPF tables are only
 * accessed as {@code MetaContextIndexed} types.
 * </p>
 * <h2>MetaPath</h2>
 * <p>
 * All elements within a meta context domain can be addressed for retrieval or
 * storage using a {@code MetaPath} object. Meta paths are used to select a
 * particular meta context which further accesses its elements via a context
 * specific way (i.e. mapped or index, etc...)
 * </p>
 * <p>
 * For example {@code new MetaPath("Ethernet.destination")} path element when
 * applied to a {@code MetaPacket.getField(path)} will return a
 * {@code MetaField} value which will contain the <em>ethernet II</em>
 * destination address or null if field or header is not found.
 */
package com.slytechs.jnet.protocol.api.meta;
