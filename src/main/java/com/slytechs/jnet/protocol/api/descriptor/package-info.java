/**
 * Provides various levels of high level information about packets. Descriptors
 * are used to attach "meta" data to packets. Every descriptor type is designed
 * to provide different kinds of information about each packet received
 * depending on the program's needs.
 * <p>
 * Descriptors are also a mechanism that low level libraries provide information
 * about the raw packet. The various descriptors in this package are designed to
 * work with many different descriptors provided by the low level libraries and
 * make them available through the {@code Packet} class API.
 * </p>
 */
package com.slytechs.jnet.protocol.api.descriptor;