/*
 * Sly Technologies Free License
 * 
 * Copyright 2023 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.tcpip.transport;

import java.util.Set;

import com.slytechs.jnet.jnetruntime.internal.util.format.BitFormat;
import com.slytechs.jnet.protocol.api.common.HasOption;
import com.slytechs.jnet.protocol.api.common.HeaderNotFound;
import com.slytechs.jnet.protocol.api.common.OptionsHeader;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.api.packet.Packet;
import com.slytechs.jnet.protocol.tcpip.constants.CoreId;
import com.slytechs.jnet.protocol.tcpip.constants.TcpFlag;

/**
 * Transmission Control Protocol (TCP) header.
 * <p>
 * The Transmission Control Protocol (TCP) is a connection-oriented protocol
 * that provides a reliable, ordered, and error-checked delivery of a stream of
 * octets (bytes) between applications running on hosts communicating via an IP
 * network. Major internet applications such as the World Wide Web, email,
 * remote administration, and file transfer rely on TCP, which is part of the
 * Transport Layer of the TCP/IP suite. SSL/TLS often runs on top of TCP.
 * </p>
 * <p>
 * TCP is a connection-oriented protocol, which means that a connection is
 * established between the two hosts before any data is sent. This connection is
 * used to keep track of the data that is being sent and received, and to ensure
 * that the data is delivered in the correct order.
 * </p>
 * <p>
 * TCP provides a number of features that make it a reliable protocol:
 * </p>
 * <ul>
 * <li><b>Connection establishment:</b> TCP uses a three-way handshake to
 * establish a connection between two hosts. This ensures that both hosts are
 * ready to receive data before any data is sent.</li>
 * <li><b>Flow control:</b> TCP uses flow control to ensure that the sender does
 * not send data too quickly for the receiver to handle. This is done by using a
 * window size, which is the maximum amount of data that the sender can send
 * without waiting for an acknowledgment from the receiver.</li>
 * <li><b>Error detection:</b> TCP uses a checksum to detect errors in the data
 * that is being sent. If an error is detected, the sender will resend the
 * data.</li>
 * <li><b>Retransmission:</b> If data is lost or corrupted, TCP will retransmit
 * the data. This ensures that all of the data is eventually delivered to the
 * receiver.</li>
 * </ul>
 * <p>
 * TCP is a complex protocol, but it is essential for reliable communication
 * over the internet.
 * </p>
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("tcp-meta.json")
public final class Tcp
		extends OptionsHeader
		implements HasOption<TcpOption> {

	/** Numerical TCP protocol ID constant. */
	public static final int ID = CoreId.CORE_ID_TCP;

	/** The Constant FLAGS_FORMAT. */
	private static final String FLAGS_FORMAT = "..B WEUA PRSF";
	private static final BitFormat FLAGS_FORMATTER = new BitFormat(FLAGS_FORMAT);

	/**
	 * The wscale option. We persist the object, once its lazily created in case it
	 * will be needed again during the next header binding. The option is unbound
	 * when the main TCP header is unbound.
	 */
	private TcpWindowScaleOption wscaleOption;
	private BitFormat flagsFormatter = FLAGS_FORMATTER;

	/**
	 * Instantiates a new tcp header instance.
	 */
	public Tcp() {
		super(ID);
	}

	/**
	 * Formats all the TCP flag bits as string using an internal {@code BitFormat}
	 * class.
	 *
	 * @return the string representing all of the flag bits
	 */
	public String flagsGetFormatted() {
		return flagsFormatter.format(flags());
	}

	/**
	 * Sets a new bit format string for formatting bits within the flag field.
	 * <p>
	 * The bit field format string uses the following structure to define how bits
	 * within a bit field are formatted and represented:
	 * </p>
	 * <ul>
	 * <li>Bits are formatted from left to right where left most bit is the most
	 * significant bit.</li>
	 * <li>Whitespace is copied as is from source format string, to output
	 * string.</li>
	 * <li>A '.' dot character is used to indicate a bit that should not be
	 * formatted.</li>
	 * <li>Any character, with the exception of an 'offChar', will placed in the
	 * output formatted string when a bit is set or has a value of 1.</li>
	 * <li>When a bit is not set, or with a value of 0, the 'offChar' (default is
	 * '.' character) will be placed in the output formatted string.</li>
	 * </ul>
	 * <p>
	 * For example this is the default format string used to format the flags field
	 * <b>{@code "..B WEUA PRSF"}</b>. For TCP flags field value of {@code 0x002},
	 * would generate the following output <b>{@code "... .... ..S."}.</b>
	 * </p>
	 *
	 * @param bitFormatString the bit format string or null to reset back to the
	 *                        default formatter
	 * @return this tcp header instance
	 */
	public synchronized Tcp flagsSetFormatString(String bitFormatString) {
		if (bitFormatString == null)
			flagsFormatter = FLAGS_FORMATTER;
		else
			flagsFormatter = new BitFormat(bitFormatString);

		return this;
	}

	/**
	 * Sets a new bit format string for formatting bits within the flag field.
	 * <p>
	 * The bit field format string uses the following structure to define how bits
	 * within a bit field are formatted and represented:
	 * </p>
	 * <ul>
	 * <li>Bits are formatted from left to right where left most bit is the most
	 * significant bit.</li>
	 * <li>Whitespace is copied as is from source format string, to output
	 * string.</li>
	 * <li>A '.' dot character is used to indicate a bit that has should not be
	 * formatted.</li>
	 * <li>Any character, with the exception of an 'offChar', will placed in the
	 * output formatted string when a bit is set or has a value of 1.</li>
	 * <li>When a bit is not set, or with a value of 0, the 'offChar' will be placed
	 * in the output formatted string.</li>
	 * </ul>
	 * <p>
	 * For example this is the default format string used to format the flags field
	 * <b>{@code "..B WEUA PRSF"}</b>. For TCP flags field value of {@code 0x002},
	 * would generate the following output <b>{@code "... .... ..S."}.</b>
	 * </p>
	 *
	 * @param bitFormatString the bit format string or null to reset back to the
	 *                        default formatter
	 * @param offChar         the off char to be used when a bit set to 0 is
	 *                        encountered
	 * @return this tcp header instance
	 */
	synchronized Tcp flagsSetFormatString(String bitFormatString, char offChar) {
		if (bitFormatString == null)
			flagsFormatter = FLAGS_FORMATTER;
		else
			flagsFormatter = new BitFormat(bitFormatString, offChar);

		return this;
	}

	/**
	 * Gets Tcp Flags field bits as a descriptive string.
	 *
	 * @return the formatted set of of TCP flags as a string
	 */
	public String flagsAsString() {
		return flagsAsEnumSet().toString();
	}

	/**
	 * Gets TCP Flags field bits as an enum set.
	 *
	 * @return The set of all of the flag as constants
	 */
	@Meta(MetaType.ATTRIBUTE)
	public Set<TcpFlag> flagsAsEnumSet() {
		return TcpFlag.valueOfInt(flags());
	}

	/**
	 * Gets a TCP 32-bit Acknowledgment number.
	 * <p>
	 * The TCP ACK field is a 32-bit field in the TCP header that specifies the
	 * sequence number of the next byte of data that the sender expects to receive.
	 * The ACK field is used to acknowledge the receipt of data from the sender.
	 * When the receiver receives data, it increments the ACK field and sends it
	 * back to the sender. This tells the sender that the receiver has received the
	 * data and is ready for more.
	 * </p>
	 * <p>
	 * The ACK field is used to ensure that data is delivered in the correct order.
	 * If the sender sends data with a sequence number that is higher than the
	 * sequence number of the last data that the receiver received, the receiver
	 * will discard the data. This ensures that the receiver never receives data out
	 * of order.
	 * </p>
	 * <p>
	 * The ACK field is also used to detect lost data. If the receiver does not
	 * receive an ACK for a certain amount of time, the sender will assume that the
	 * data was lost and will retransmit it. This ensures that all of the data is
	 * eventually delivered to the receiver.
	 * </p>
	 * <p>
	 * The ACK field is an important part of the TCP protocol and is essential for
	 * reliable communication over the internet.
	 * </p>
	 *
	 * @return the unsigned 32-bit field value
	 */
	@Meta
	public long ack() {
		return TcpStruct.ACK.getUnsignedInt(buffer());
	}

	/**
	 * Sets a TCP 32-bit Acknowledgment number.
	 * <p>
	 * The TCP ACK field is a 32-bit field in the TCP header that specifies the
	 * sequence number of the next byte of data that the sender expects to receive.
	 * The ACK field is used to acknowledge the receipt of data from the sender.
	 * When the receiver receives data, it increments the ACK field and sends it
	 * back to the sender. This tells the sender that the receiver has received the
	 * data and is ready for more.
	 * </p>
	 * <p>
	 * The ACK field is used to ensure that data is delivered in the correct order.
	 * If the sender sends data with a sequence number that is higher than the
	 * sequence number of the last data that the receiver received, the receiver
	 * will discard the data. This ensures that the receiver never receives data out
	 * of order.
	 * </p>
	 * <p>
	 * The ACK field is also used to detect lost data. If the receiver does not
	 * receive an ACK for a certain amount of time, the sender will assume that the
	 * data was lost and will retransmit it. This ensures that all of the data is
	 * eventually delivered to the receiver.
	 * </p>
	 * <p>
	 * The ACK field is an important part of the TCP protocol and is essential for
	 * reliable communication over the internet.
	 * </p>
	 * 
	 * @param newAck the new ack value in header
	 * @return this tcp header instance
	 */
	public Tcp ack(long newAck) {
		TcpStruct.ACK.setInt((int) newAck, buffer());

		return this;
	}

	/**
	 * Gets the TCP header's 16-bit checksum field.
	 * <p>
	 * The TCP checksum field is a 16-bit field in the TCP header that is used to
	 * verify the integrity of the TCP header and payload. The checksum is
	 * calculated by taking the one's complement of the one's complement sum of all
	 * the 16-bit words in the TCP header and payload. The checksum is then placed
	 * in the checksum field.
	 * </p>
	 * <p>
	 * When the TCP segment arrives at the destination, the receiving TCP
	 * implementation recalculates the checksum and compares it to the checksum in
	 * the TCP header. If the two checksums do not match, the TCP segment is
	 * discarded. This ensures that the TCP segment has not been corrupted in
	 * transit.
	 * </p>
	 * <p>
	 * The TCP checksum is an important part of the TCP protocol and is essential
	 * for reliable communication over the internet.
	 * </p>
	 * <p>
	 * Here are the steps on how to calculate the TCP checksum:
	 * </p>
	 * <ol>
	 * <li>Set the checksum field to zero.</li>
	 * <li>Concatenate the source and destination IP addresses, the reserved field,
	 * the protocol field (set to 6 for TCP), the TCP length, and the TCP data.</li>
	 * <li>Treat the resulting binary value as a large integer and perform a
	 * bit-wise ones complement.</li>
	 * <li>Convert the resulting value to 16-bit binary and place it in the checksum
	 * field of the TCP header.</li>
	 * </ol>
	 * <p>
	 * If the calculated checksum does not match the checksum in the TCP header, the
	 * TCP segment is discarded. This ensures that the TCP segment has not been
	 * corrupted in transit.
	 * </p>
	 * 
	 * @return the value of the of checksum field
	 */
	@Meta
	public int checksum() {
		return TcpStruct.CHECKSUM.getUnsignedShort(buffer());
	}

	/**
	 * Sets the TCP header's 16-bit checksum field.
	 * <p>
	 * The TCP checksum field is a 16-bit field in the TCP header that is used to
	 * verify the integrity of the TCP header and payload. The checksum is
	 * calculated by taking the one's complement of the one's complement sum of all
	 * the 16-bit words in the TCP header and payload. The checksum is then placed
	 * in the checksum field.
	 * </p>
	 * <p>
	 * When the TCP segment arrives at the destination, the receiving TCP
	 * implementation recalculates the checksum and compares it to the checksum in
	 * the TCP header. If the two checksums do not match, the TCP segment is
	 * discarded. This ensures that the TCP segment has not been corrupted in
	 * transit.
	 * </p>
	 * <p>
	 * The TCP checksum is an important part of the TCP protocol and is essential
	 * for reliable communication over the internet.
	 * </p>
	 * <p>
	 * Here are the steps on how to calculate the TCP checksum:
	 * </p>
	 * <ol>
	 * <li>Set the checksum field to zero.</li>
	 * <li>Concatenate the source and destination IP addresses, the reserved field,
	 * the protocol field (set to 6 for TCP), the TCP length, and the TCP data.</li>
	 * <li>Treat the resulting binary value as a large integer and perform a
	 * bit-wise ones complement.</li>
	 * <li>Convert the resulting value to 16-bit binary and place it in the checksum
	 * field of the TCP header.</li>
	 * </ol>
	 * <p>
	 * If the calculated checksum does not match the checksum in the TCP header, the
	 * TCP segment is discarded. This ensures that the TCP segment has not been
	 * corrupted in transit.
	 * </p>
	 * *
	 * 
	 * @param checksum new value for the checksum field
	 * @return this tcp header instance
	 */
	public Tcp checksum(int checksum) {
		TcpStruct.CHECKSUM.setInt(checksum, buffer());

		return this;
	}

	/**
	 * Gets the TCP 4-bit data offset field (aka. header length) in units of 32-bit
	 * words.
	 * <p>
	 * The TCP data offset field is a 4-bit field in the TCP header that specifies
	 * the size of the TCP header, expressed in 32-bit words. One word represents
	 * four bytes. The TCP header can be anywhere from 20 to 60 bytes long, so the
	 * data offset field tells the receiving host where the data payload starts.
	 * </p>
	 * <p>
	 * The data offset field is located in the first byte of the TCP header. The
	 * first bit of the data offset field is always set to 0, and the remaining
	 * three bits indicate the number of 32-bit words in the TCP header. For
	 * example, if the data offset field is set to 5, then the TCP header is 20
	 * bytes long.
	 * </p>
	 * <p>
	 * The data offset field is used by the receiving host to determine where the
	 * data payload starts. The data payload is the part of the TCP segment that
	 * contains the actual data that is being sent between the two hosts. The data
	 * offset field ensures that the receiving host can correctly interpret the TCP
	 * segment and deliver the data to the correct application.
	 * </p>
	 * <p>
	 * The data offset field is an important part of the TCP protocol and is
	 * essential for reliable communication over the internet.
	 * </p>
	 * 
	 * @return the 16-bit data offset value in units of 32-bit words.
	 */
	@Meta
	public int dataOffset() {
		return TcpStruct.HDR_LEN.getUnsignedShort(buffer());
	}

	/**
	 * Sets the TCP 4-bit data offset field (aka. header length) in units of 32-bit
	 * words.
	 * <p>
	 * The TCP data offset field is a 4-bit field in the TCP header that specifies
	 * the size of the TCP header, expressed in 32-bit words. One word represents
	 * four bytes. The TCP header can be anywhere from 20 to 60 bytes long, so the
	 * data offset field tells the receiving host where the data payload starts.
	 * </p>
	 * <p>
	 * The data offset field is located in the first byte of the TCP header. The
	 * first bit of the data offset field is always set to 0, and the remaining
	 * three bits indicate the number of 32-bit words in the TCP header. For
	 * example, if the data offset field is set to 5, then the TCP header is 20
	 * bytes long.
	 * </p>
	 * <p>
	 * The data offset field is used by the receiving host to determine where the
	 * data payload starts. The data payload is the part of the TCP segment that
	 * contains the actual data that is being sent between the two hosts. The data
	 * offset field ensures that the receiving host can correctly interpret the TCP
	 * segment and deliver the data to the correct application.
	 * </p>
	 * <p>
	 * The data offset field is an important part of the TCP protocol and is
	 * essential for reliable communication over the internet.
	 * </p>
	 *
	 * @param newOffset the new data offset value
	 * @return this tcp header instance
	 */
	public Tcp dataOffset(int newOffset) {
		TcpStruct.HDR_LEN.setInt(newOffset, buffer());

		return this;
	}

	/**
	 * Computes the TCP 32-bit data offset field (aka. header length) in units of
	 * 8-bit bytes.
	 * <p>
	 * The TCP data offset field is a 4-bit field in the TCP header that specifies
	 * the size of the TCP header, expressed in 32-bit words. One word represents
	 * four bytes. The TCP header can be anywhere from 20 to 60 bytes long, so the
	 * data offset field tells the receiving host where the data payload starts.
	 * </p>
	 * <p>
	 * The data offset field is located in the first byte of the TCP header. The
	 * first bit of the data offset field is always set to 0, and the remaining
	 * three bits indicate the number of 32-bit words in the TCP header. For
	 * example, if the data offset field is set to 5, then the TCP header is 20
	 * bytes long.
	 * </p>
	 * <p>
	 * The data offset field is used by the receiving host to determine where the
	 * data payload starts. The data payload is the part of the TCP segment that
	 * contains the actual data that is being sent between the two hosts. The data
	 * offset field ensures that the receiving host can correctly interpret the TCP
	 * segment and deliver the data to the correct application.
	 * </p>
	 * <p>
	 * The data offset field is an important part of the TCP protocol and is
	 * essential for reliable communication over the internet.
	 * </p>
	 * 
	 * @return the calculated 32-bit data offset value in units of 8-bit bytes.
	 */
	public int dataOffsetInBytes() {
		return dataOffset() << 2;
	}

	/**
	 * Gets the TCP destination port field, a 16-bit field value.
	 * <p>
	 * The TCP destination port field is a 16-bit field in the TCP header that
	 * specifies the port number of the destination host. The port number is used by
	 * the receiving host to determine which application to deliver the data to.
	 * </p>
	 * <p>
	 * The destination port field is located in the second byte of the TCP header.
	 * The port number is a 16-bit value that identifies the application that is
	 * listening for data on the destination host. For example, the port number for
	 * HTTP is 80, and the port number for HTTPS is 443.
	 * </p>
	 * <p>
	 * The destination port field is used by the receiving host to determine which
	 * application to deliver the data to. When a TCP segment arrives at the
	 * destination host, the receiving host looks up the port number in its routing
	 * table to find the application that is listening for data on that port. The
	 * receiving host then delivers the data to the application.
	 * </p>
	 * <p>
	 * The destination port field is an important part of the TCP protocol and is
	 * essential for reliable communication over the internet.
	 * </p>
	 * <p>
	 * Here are some examples of common TCP destination ports:
	 * </p>
	 * <ul>
	 * <li>HTTP: 80</li>
	 * <li>HTTPS: 443</li>
	 * <li>FTP: 21</li>
	 * <li>SMTP: 25</li>
	 * <li>POP3: 110</li>
	 * <li>IMAP: 143</li>
	 * </ul>
	 * <p>
	 * These are just a few examples of the many TCP destination ports that are used
	 * on the internet.
	 * </p>
	 * 
	 * @return the 16-bit TCP destination port value
	 */
	@Meta
	public int destination() {
		return TcpStruct.DST_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Sets the TCP destination port field, a 16-bit field value.
	 * <p>
	 * The TCP destination port field is a 16-bit field in the TCP header that
	 * specifies the port number of the destination host. The port number is used by
	 * the receiving host to determine which application to deliver the data to.
	 * </p>
	 * <p>
	 * The destination port field is located in the second byte of the TCP header.
	 * The port number is a 16-bit value that identifies the application that is
	 * listening for data on the destination host. For example, the port number for
	 * HTTP is 80, and the port number for HTTPS is 443.
	 * </p>
	 * <p>
	 * The destination port field is used by the receiving host to determine which
	 * application to deliver the data to. When a TCP segment arrives at the
	 * destination host, the receiving host looks up the port number in its routing
	 * table to find the application that is listening for data on that port. The
	 * receiving host then delivers the data to the application.
	 * </p>
	 * <p>
	 * The destination port field is an important part of the TCP protocol and is
	 * essential for reliable communication over the internet.
	 * </p>
	 * <p>
	 * Here are some examples of common TCP destination ports:
	 * </p>
	 * <ul>
	 * <li>HTTP: 80</li>
	 * <li>HTTPS: 443</li>
	 * <li>FTP: 21</li>
	 * <li>SMTP: 25</li>
	 * <li>POP3: 110</li>
	 * <li>IMAP: 143</li>
	 * </ul>
	 * <p>
	 * These are just a few examples of the many TCP destination ports that are used
	 * on the internet.
	 * </p>
	 *
	 * @param newPort a new TCP destination port number
	 * @return this tcp header instance
	 */
	public Tcp destination(int newPort) {
		TcpStruct.DST_PORT.setInt(newPort, buffer());

		return this;
	}

	/**
	 * Gets the 6-bit TCP flags header field.
	 * <p>
	 * The TCP flags header field is a 6-bit field in the TCP header that specifies
	 * the state of the connection and the actions that the sender and receiver
	 * should take. The flags are:
	 * </p>
	 * <ul>
	 * <li>SYN: The SYN flag is used to initiate a connection. When the SYN flag is
	 * set, the sender is requesting to establish a connection with the
	 * receiver.</li>
	 * <li>ACK: The ACK flag is used to acknowledge the receipt of data. When the
	 * ACK flag is set, the sender is acknowledging that it has received data from
	 * the receiver.</li>
	 * <li>FIN: The FIN flag is used to terminate a connection. When the FIN flag is
	 * set, the sender is requesting to close the connection.</li>
	 * <li>URG: The URG flag is used to indicate that the data in the segment is
	 * urgent and should be processed immediately.</li>
	 * <li>PSH: The PSH flag is used to request that the receiver push the data in
	 * the segment up to the application layer.</li>
	 * <li>RST: The RST flag is used to reset a connection. When the RST flag is
	 * set, the sender is indicating that the connection is broken and should be
	 * discarded.</li>
	 * </ul>
	 * <p>
	 * The TCP flags are used by the sender and receiver to coordinate the
	 * establishment, maintenance, and termination of a connection. The flags ensure
	 * that data is delivered in the correct order and that errors are detected and
	 * corrected.
	 * </p>
	 * <p>
	 * Here are some examples of how the TCP flags are used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to connect to a server, it sends a TCP segment with
	 * the SYN flag set. The server responds with a TCP segment with the SYN and ACK
	 * flags set. This establishes the connection.</li>
	 * <li>When a client wants to send data to a server, it sends a TCP segment with
	 * the ACK flag set. The server responds with a TCP segment with the ACK flag
	 * set. This acknowledges the receipt of data.</li>
	 * <li>When a client wants to close a connection, it sends a TCP segment with
	 * the FIN flag set. The server responds with a TCP segment with the ACK flag
	 * set. This closes the connection.</li>
	 * </ul>
	 * <p>
	 * The TCP flags are an important part of the TCP protocol and are essential for
	 * reliable communication over the internet.
	 * </p>
	 *
	 * @return the 6-bit flags field value
	 */
	@Meta
	public int flags() {
		return TcpStruct.FLAGS.getUnsignedShort(buffer());
	}

	/**
	 * Sets the 6-bit TCP flags header field.
	 * <p>
	 * The TCP flags header field is a 6-bit field in the TCP header that specifies
	 * the state of the connection and the actions that the sender and receiver
	 * should take. The flags are:
	 * </p>
	 * <ul>
	 * <li>SYN: The SYN flag is used to initiate a connection. When the SYN flag is
	 * set, the sender is requesting to establish a connection with the
	 * receiver.</li>
	 * <li>ACK: The ACK flag is used to acknowledge the receipt of data. When the
	 * ACK flag is set, the sender is acknowledging that it has received data from
	 * the receiver.</li>
	 * <li>FIN: The FIN flag is used to terminate a connection. When the FIN flag is
	 * set, the sender is requesting to close the connection.</li>
	 * <li>URG: The URG flag is used to indicate that the data in the segment is
	 * urgent and should be processed immediately.</li>
	 * <li>PSH: The PSH flag is used to request that the receiver push the data in
	 * the segment up to the application layer.</li>
	 * <li>RST: The RST flag is used to reset a connection. When the RST flag is
	 * set, the sender is indicating that the connection is broken and should be
	 * discarded.</li>
	 * </ul>
	 * <p>
	 * The TCP flags are used by the sender and receiver to coordinate the
	 * establishment, maintenance, and termination of a connection. The flags ensure
	 * that data is delivered in the correct order and that errors are detected and
	 * corrected.
	 * </p>
	 * <p>
	 * Here are some examples of how the TCP flags are used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to connect to a server, it sends a TCP segment with
	 * the SYN flag set. The server responds with a TCP segment with the SYN and ACK
	 * flags set. This establishes the connection.</li>
	 * <li>When a client wants to send data to a server, it sends a TCP segment with
	 * the ACK flag set. The server responds with a TCP segment with the ACK flag
	 * set. This acknowledges the receipt of data.</li>
	 * <li>When a client wants to close a connection, it sends a TCP segment with
	 * the FIN flag set. The server responds with a TCP segment with the ACK flag
	 * set. This closes the connection.</li>
	 * </ul>
	 * <p>
	 * The TCP flags are an important part of the TCP protocol and are essential for
	 * reliable communication over the internet.
	 * </p>
	 *
	 * @param newFlags the 6-bit flags field value
	 * @return this tcp header instance
	 */
	public Tcp flags(int newFlags) {
		TcpStruct.FLAGS.setInt(newFlags, buffer());

		return this;
	}

	/**
	 * Checks if the TCP ack flag bit is set.
	 * <p>
	 * The TCP ACK flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is acknowledging the receipt of data from the receiver. When the
	 * ACK flag is set, the sender is acknowledging that it has received data from
	 * the receiver. The ACK flag is used to ensure that data is delivered in the
	 * correct order and that errors are detected and corrected.
	 * </p>
	 * <p>
	 * The ACK flag is set in the TCP header of every packet that is sent after the
	 * initial connection setup. This ensures that the receiver knows which data has
	 * been received and which data is still outstanding. If the receiver does not
	 * receive an ACK for a certain amount of time, it will assume that the data was
	 * lost and will request a retransmission.
	 * </p>
	 * <p>
	 * The ACK flag is an important part of the TCP protocol and is essential for
	 * reliable communication over the internet.
	 * </p>
	 * <p>
	 * Here are some examples of how the ACK flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to connect to a server, it sends a TCP segment with
	 * the SYN flag set. The server responds with a TCP segment with the SYN and ACK
	 * flags set. This establishes the connection.</li>
	 * <li>When a client wants to send data to a server, it sends a TCP segment with
	 * the ACK flag set. The server responds with a TCP segment with the ACK flag
	 * set. This acknowledges the receipt of data.</li>
	 * <li>When a client wants to close a connection, it sends a TCP segment with
	 * the FIN flag set. The server responds with a TCP segment with the ACK flag
	 * set. This closes the connection.</li>
	 * </ul>
	 * <p>
	 * The ACK flag is an important part of the TCP protocol and is essential for
	 * reliable communication over the internet.
	 * </p>
	 * 
	 * @return true, if the flag bit is set, otherwise false
	 */
	public boolean flags_ACK() {
		return TcpStruct.FLAGS_ACK.getBit(buffer());
	}

	/**
	 * Set the TCP ack flag bit to 1 or 0.
	 * <p>
	 * The TCP ACK flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is acknowledging the receipt of data from the receiver. When the
	 * ACK flag is set, the sender is acknowledging that it has received data from
	 * the receiver. The ACK flag is used to ensure that data is delivered in the
	 * correct order and that errors are detected and corrected.
	 * </p>
	 * <p>
	 * The ACK flag is set in the TCP header of every packet that is sent after the
	 * initial connection setup. This ensures that the receiver knows which data has
	 * been received and which data is still outstanding. If the receiver does not
	 * receive an ACK for a certain amount of time, it will assume that the data was
	 * lost and will request a retransmission.
	 * </p>
	 * <p>
	 * The ACK flag is an important part of the TCP protocol and is essential for
	 * reliable communication over the internet.
	 * </p>
	 * <p>
	 * Here are some examples of how the ACK flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to connect to a server, it sends a TCP segment with
	 * the SYN flag set. The server responds with a TCP segment with the SYN and ACK
	 * flags set. This establishes the connection.</li>
	 * <li>When a client wants to send data to a server, it sends a TCP segment with
	 * the ACK flag set. The server responds with a TCP segment with the ACK flag
	 * set. This acknowledges the receipt of data.</li>
	 * <li>When a client wants to close a connection, it sends a TCP segment with
	 * the FIN flag set. The server responds with a TCP segment with the ACK flag
	 * set. This closes the connection.</li>
	 * </ul>
	 * <p>
	 * The ACK flag is an important part of the TCP protocol and is essential for
	 * reliable communication over the internet.
	 * </p>
	 * 
	 * @param b if true, sets the flag bit to on (1), otherwise the bit is turned
	 *          off (0)
	 * @return this tcp header instance
	 */
	public Tcp flags_ACK(boolean b) {
		TcpStruct.FLAGS_ACK.setBoolean(b, buffer());
		return this;
	}

	/**
	 * Checks if the TCP CWR flag bit is set.
	 * <p>
	 * The TCP CWR flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting that the receiver reduce its congestion window. When
	 * the CWR flag is set, the sender is indicating that it has experienced
	 * congestion and is asking the receiver to reduce its sending rate.
	 * </p>
	 * <p>
	 * The CWR flag is used to help prevent congestion in the network. When a sender
	 * experiences congestion, it sets the CWR flag in the TCP header of the next
	 * packet that it sends. This tells the receiver to reduce its congestion
	 * window, which will help to reduce the amount of traffic that is flowing
	 * through the network.
	 * </p>
	 * <p>
	 * The CWR flag is an important part of the TCP congestion control mechanism. It
	 * helps to prevent congestion in the network and to ensure that data is
	 * delivered reliably.
	 * </p>
	 * <p>
	 * Here are some examples of how the CWR flag is used:
	 * </p>
	 * <ul>
	 * <li>When a sender experiences congestion, it sets the CWR flag in the TCP
	 * header of the next packet that it sends.</li>
	 * <li>The receiver receives the packet with the CWR flag set and reduces its
	 * congestion window.</li>
	 * <li>The sender sends fewer packets, which helps to reduce the amount of
	 * congestion in the network.</li>
	 * </ul>
	 * <p>
	 * The CWR flag is an important part of the TCP congestion control mechanism and
	 * helps to ensure that data is delivered reliably. *
	 * </p>
	 * *
	 * 
	 * @return true, if the CWR flag bit is set, otherwise false
	 */
	public boolean flags_CWR() {
		return TcpStruct.FLAGS_CWR.getBit(buffer());
	}

	/**
	 * Sets the TCP CWR flag bit to 1 or 0.
	 * <p>
	 * The TCP CWR flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting that the receiver reduce its congestion window. When
	 * the CWR flag is set, the sender is indicating that it has experienced
	 * congestion and is asking the receiver to reduce its sending rate.
	 * </p>
	 * <p>
	 * The CWR flag is used to help prevent congestion in the network. When a sender
	 * experiences congestion, it sets the CWR flag in the TCP header of the next
	 * packet that it sends. This tells the receiver to reduce its congestion
	 * window, which will help to reduce the amount of traffic that is flowing
	 * through the network.
	 * </p>
	 * <p>
	 * The CWR flag is an important part of the TCP congestion control mechanism. It
	 * helps to prevent congestion in the network and to ensure that data is
	 * delivered reliably.
	 * </p>
	 * <p>
	 * Here are some examples of how the CWR flag is used:
	 * </p>
	 * <ul>
	 * <li>When a sender experiences congestion, it sets the CWR flag in the TCP
	 * header of the next packet that it sends.</li>
	 * <li>The receiver receives the packet with the CWR flag set and reduces its
	 * congestion window.</li>
	 * <li>The sender sends fewer packets, which helps to reduce the amount of
	 * congestion in the network.</li>
	 * </ul>
	 * <p>
	 * The CWR flag is an important part of the TCP congestion control mechanism and
	 * helps to ensure that data is delivered reliably. *
	 * </p>
	 * 
	 * @param b if true, sets the flag bit to on (1), otherwise the bit is turned
	 *          off (0)
	 * @return this tcp header instance
	 */
	public Tcp flags_CWR(boolean b) {
		TcpStruct.FLAGS_CWR.setBoolean(b, buffer());
		return this;
	}

	/**
	 * Checks if TCP ECE flag bit is set.
	 * <p>
	 * The TCP ECE flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is using Explicit Congestion Notification (ECN). When the ECE flag
	 * is set, the sender is indicating that it is willing to use ECN to signal
	 * congestion to the receiver.
	 * </p>
	 * <p>
	 * ECN is a congestion control mechanism that allows routers to signal
	 * congestion to end hosts without dropping packets. When a router experiences
	 * congestion, it marks the packets that it sends to the end hosts with the ECN
	 * bit set. The end hosts can then use this information to reduce their sending
	 * rates and avoid congestion.
	 * </p>
	 * <p>
	 * The ECE flag is an important part of the ECN congestion control mechanism. It
	 * allows end hosts to cooperate with routers to prevent congestion and to
	 * ensure that data is delivered reliably.
	 * </p>
	 * <p>
	 * Here are some examples of how the ECE flag is used:
	 * </p>
	 * <ul>
	 * <li>When a router experiences congestion, it marks the packets that it sends
	 * to the end hosts with the ECN bit set.</li>
	 * <li>The end hosts receive the packets with the ECN bit set and reduce their
	 * sending rates.</li>
	 * <li>The router experiences less congestion, which helps to ensure that data
	 * is delivered reliably.</li>
	 * </ul>
	 * <p>
	 * The ECE flag is an important part of the ECN congestion control mechanism and
	 * helps to ensure that data is delivered reliably. * * @return true, if the
	 * flag bit is set, otherwise false
	 * </p>
	 *
	 * @return if true the ECE flag bit is set, otherwise false
	 */
	public boolean flags_ECE() {
		return TcpStruct.FLAGS_ECE.getBit(buffer());
	}

	/**
	 * Sets TCP ECE flag bit to 1 or 0.
	 * <p>
	 * The TCP ECE flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is using Explicit Congestion Notification (ECN). When the ECE flag
	 * is set, the sender is indicating that it is willing to use ECN to signal
	 * congestion to the receiver.
	 * </p>
	 * <p>
	 * ECN is a congestion control mechanism that allows routers to signal
	 * congestion to end hosts without dropping packets. When a router experiences
	 * congestion, it marks the packets that it sends to the end hosts with the ECN
	 * bit set. The end hosts can then use this information to reduce their sending
	 * rates and avoid congestion.
	 * </p>
	 * <p>
	 * The ECE flag is an important part of the ECN congestion control mechanism. It
	 * allows end hosts to cooperate with routers to prevent congestion and to
	 * ensure that data is delivered reliably.
	 * </p>
	 * <p>
	 * Here are some examples of how the ECE flag is used:
	 * </p>
	 * <ul>
	 * <li>When a router experiences congestion, it marks the packets that it sends
	 * to the end hosts with the ECN bit set.</li>
	 * <li>The end hosts receive the packets with the ECN bit set and reduce their
	 * sending rates.</li>
	 * <li>The router experiences less congestion, which helps to ensure that data
	 * is delivered reliably.</li>
	 * </ul>
	 * <p>
	 * The ECE flag is an important part of the ECN congestion control mechanism and
	 * helps to ensure that data is delivered reliably. * * @return true, if the
	 * flag bit is set, otherwise false
	 * 
	 * @param b if true, sets the flag bit to on (1), otherwise the bit is turned
	 *          off (0)
	 * @return this tcp header instance for Fluent pattern usage
	 */
	public Tcp flags_ECE(boolean b) {
		TcpStruct.FLAGS_ECE.setBoolean(b, buffer());
		return this;
	}

	/**
	 * Checks if the TCP FIN flag bit is set.
	 * <p>
	 * The TCP FIN flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting to close the connection. When the FIN flag is set,
	 * the sender is indicating that it has no more data to send and is requesting
	 * that the receiver close the connection.
	 * </p>
	 * <p>
	 * The FIN flag is used to close a TCP connection. When a sender wants to close
	 * a connection, it sends a TCP segment with the FIN flag set. The receiver
	 * responds with a TCP segment with the ACK flag set. This acknowledges the
	 * receipt of the FIN flag and indicates that the receiver is ready to close the
	 * connection.
	 * </p>
	 * <p>
	 * The FIN flag is an important part of the TCP connection termination process.
	 * It ensures that both sides of the connection are aware that the connection is
	 * being closed and that no more data will be sent.
	 * </p>
	 * <p>
	 * Here are some examples of how the FIN flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to close a connection to a server, it sends a TCP
	 * segment with the FIN flag set.</li>
	 * <li>The server responds with a TCP segment with the ACK flag set.</li>
	 * <li>The client and server both close their respective sockets.</li>
	 * </ul>
	 * <p>
	 * The FIN flag is an important part of the TCP connection termination process
	 * and helps to ensure that connections are closed gracefully.
	 * </p>
	 * 
	 * @return true, if the flag bit is set, otherwise false
	 */
	public boolean flags_FIN() {
		return TcpStruct.FLAGS_FIN.getBit(buffer());
	}

	/**
	 * Sets the TCP FIN flag bit to 1 or 0.
	 * <p>
	 * The TCP FIN flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting to close the connection. When the FIN flag is set,
	 * the sender is indicating that it has no more data to send and is requesting
	 * that the receiver close the connection.
	 * </p>
	 * <p>
	 * The FIN flag is used to close a TCP connection. When a sender wants to close
	 * a connection, it sends a TCP segment with the FIN flag set. The receiver
	 * responds with a TCP segment with the ACK flag set. This acknowledges the
	 * receipt of the FIN flag and indicates that the receiver is ready to close the
	 * connection.
	 * </p>
	 * <p>
	 * The FIN flag is an important part of the TCP connection termination process.
	 * It ensures that both sides of the connection are aware that the connection is
	 * being closed and that no more data will be sent.
	 * </p>
	 * <p>
	 * Here are some examples of how the FIN flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to close a connection to a server, it sends a TCP
	 * segment with the FIN flag set.</li>
	 * <li>The server responds with a TCP segment with the ACK flag set.</li>
	 * <li>The client and server both close their respective sockets.</li>
	 * </ul>
	 * <p>
	 * The FIN flag is an important part of the TCP connection termination process
	 * and helps to ensure that connections are closed gracefully.
	 * </p>
	 * 
	 * @param b if true, sets the flag bit to on (1), otherwise the bit is turned
	 *          off (0)
	 * @return this tcp header instance
	 */
	public Tcp flags_FIN(boolean b) {
		TcpStruct.FLAGS_FIN.setBoolean(b, buffer());
		return this;
	}

	/**
	 * Checks if the TCP PSH flag bit is set.
	 * <p>
	 * The TCP PUSH flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting that the receiver push the data in the segment up to
	 * the application layer. When the PUSH flag is set, the sender is indicating
	 * that the receiver should not wait for any more data to be sent before
	 * delivering the data to the application layer.
	 * </p>
	 * <p>
	 * The PUSH flag is used to ensure that data is delivered to the application
	 * layer as quickly as possible. When the PUSH flag is set, the receiver will
	 * deliver the data to the application layer immediately, even if there is more
	 * data that has not yet been sent. This is useful for applications that need to
	 * receive data as quickly as possible, such as real-time audio and video
	 * streaming.
	 * </p>
	 * <p>
	 * Here are some examples of how the PUSH flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to send data to a server, it sends a TCP segment with
	 * the PUSH flag set.</li>
	 * <li>The server responds with a TCP segment with the ACK flag set.</li>
	 * <li>The data is delivered to the application layer on the server
	 * immediately.</li>
	 * </ul>
	 * <p>
	 * The PUSH flag is an important part of the TCP protocol and helps to ensure
	 * that data is delivered to applications as quickly as possible.
	 * </p>
	 * 
	 * @return true, if the flag bit is set, otherwise false
	 */
	public boolean flags_PSH() {
		return TcpStruct.FLAGS_PSH.getBit(buffer());
	}

	/**
	 * Sets the TCP PSH flag bit to 1 or 0.
	 * <p>
	 * The TCP PUSH flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting that the receiver push the data in the segment up to
	 * the application layer. When the PUSH flag is set, the sender is indicating
	 * that the receiver should not wait for any more data to be sent before
	 * delivering the data to the application layer.
	 * </p>
	 * <p>
	 * The PUSH flag is used to ensure that data is delivered to the application
	 * layer as quickly as possible. When the PUSH flag is set, the receiver will
	 * deliver the data to the application layer immediately, even if there is more
	 * data that has not yet been sent. This is useful for applications that need to
	 * receive data as quickly as possible, such as real-time audio and video
	 * streaming.
	 * </p>
	 * <p>
	 * Here are some examples of how the PUSH flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to send data to a server, it sends a TCP segment with
	 * the PUSH flag set.</li>
	 * <li>The server responds with a TCP segment with the ACK flag set.</li>
	 * <li>The data is delivered to the application layer on the server
	 * immediately.</li>
	 * </ul>
	 * <p>
	 * The PUSH flag is an important part of the TCP protocol and helps to ensure
	 * that data is delivered to applications as quickly as possible.
	 * </p>
	 * 
	 * @param b if true, sets the flag bit to on (1), otherwise the bit is turned
	 *          off (0)
	 * @return this tcp header instance
	 */
	public Tcp flags_PSH(boolean b) {
		TcpStruct.FLAGS_PSH.setBoolean(b, buffer());
		return this;
	}

	/**
	 * Checks if the TCP RST flag bit is set.
	 * <p>
	 * The TCP RST flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting to reset the connection. When the RST flag is set,
	 * the sender is indicating that the connection is no longer valid and should be
	 * closed immediately.
	 * </p>
	 * <p>
	 * The RST flag is used to close a TCP connection forcefully. When a sender
	 * wants to close a connection forcefully, it sends a TCP segment with the RST
	 * flag set. The receiver will not respond to this packet, and the connection
	 * will be closed immediately.
	 * </p>
	 * <p>
	 * The RST flag is an important part of the TCP connection termination process.
	 * It ensures that both sides of the connection are aware that the connection is
	 * being closed and that no more data will be sent.
	 * </p>
	 * <p>
	 * Here are some examples of how the RST flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client tries to connect to a server that is not running, the
	 * server will send a TCP segment with the RST flag set.</li>
	 * <li>When a client tries to send data to a server that has closed the
	 * connection, the server will send a TCP segment with the RST flag set.</li>
	 * <li>When a client tries to send data to a server that is under attack, the
	 * server may send a TCP segment with the RST flag set to prevent the attack
	 * from succeeding.</li>
	 * </ul>
	 * <p>
	 * The RST flag is an important part of the TCP protocol and helps to ensure
	 * that connections are closed gracefully and that attacks are prevented.
	 * </p>
	 * *
	 * 
	 * @return true, if the flag bit is set, otherwise false
	 */
	public boolean flags_RST() {
		return TcpStruct.FLAGS_RST.getBit(buffer());
	}

	/**
	 * Sets the TCP RST flag bit to 1 or 0.
	 * <p>
	 * The TCP RST flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting to reset the connection. When the RST flag is set,
	 * the sender is indicating that the connection is no longer valid and should be
	 * closed immediately.
	 * </p>
	 * <p>
	 * The RST flag is used to close a TCP connection forcefully. When a sender
	 * wants to close a connection forcefully, it sends a TCP segment with the RST
	 * flag set. The receiver will not respond to this packet, and the connection
	 * will be closed immediately.
	 * </p>
	 * <p>
	 * The RST flag is an important part of the TCP connection termination process.
	 * It ensures that both sides of the connection are aware that the connection is
	 * being closed and that no more data will be sent.
	 * </p>
	 * <p>
	 * Here are some examples of how the RST flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client tries to connect to a server that is not running, the
	 * server will send a TCP segment with the RST flag set.</li>
	 * <li>When a client tries to send data to a server that has closed the
	 * connection, the server will send a TCP segment with the RST flag set.</li>
	 * <li>When a client tries to send data to a server that is under attack, the
	 * server may send a TCP segment with the RST flag set to prevent the attack
	 * from succeeding.</li>
	 * </ul>
	 * <p>
	 * The RST flag is an important part of the TCP protocol and helps to ensure
	 * that connections are closed gracefully and that attacks are prevented.
	 * </p>
	 * 
	 * @param b if true, sets the flag bit to on (1), otherwise the bit is turned
	 *          off (0)
	 * @return this tcp header instance
	 */
	public Tcp flags_RST(boolean b) {
		TcpStruct.FLAGS_RST.setBoolean(b, buffer());
		return this;
	}

	/**
	 * Checks if the TCP SYN flag bit is set.
	 * <p>
	 * The TCP SYN flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting to synchronize sequence numbers. When the SYN flag
	 * is set, the sender is indicating that it wants to establish a connection with
	 * the receiver and is requesting that the receiver send its sequence number.
	 * </p>
	 * <p>
	 * The SYN flag is used to initiate a TCP connection. When a sender wants to
	 * establish a connection with a receiver, it sends a TCP segment with the SYN
	 * flag set. The receiver responds with a TCP segment with the SYN and ACK flags
	 * set. This establishes the connection and allows data to be sent between the
	 * sender and receiver.
	 * </p>
	 * <p>
	 * The SYN flag is an important part of the TCP connection establishment
	 * process. It ensures that both sides of the connection are aware of the
	 * sequence numbers that will be used to send data.
	 * </p>
	 * <p>
	 * Here are some examples of how the SYN flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to connect to a server, it sends a TCP segment with
	 * the SYN flag set.</li>
	 * <li>The server responds with a TCP segment with the SYN and ACK flags
	 * set.</li>
	 * <li>The connection is established and data can be sent between the client and
	 * server.</li>
	 * </ul>
	 * <p>
	 * The SYN flag is an important part of the TCP protocol and helps to ensure
	 * that connections are established reliably.
	 * </p>
	 * <p>
	 * Here are some additional details about the SYN flag:
	 * </p>
	 * <ul>
	 * <li>The SYN flag is always set in the first packet of a TCP connection.</li>
	 * <li>The SYN flag is always cleared in subsequent packets of a TCP
	 * connection.</li>
	 * <li>The SYN flag is used to synchronize the sequence numbers of the sender
	 * and receiver.</li>
	 * <li>The SYN flag is an important part of the TCP connection establishment
	 * process.</li>
	 * </ul>
	 * 
	 * @return true, if the flag bit is set, otherwise false
	 */
	public boolean flags_SYN() {
		return TcpStruct.FLAGS_SYN.getBit(buffer());
	}

	/**
	 * Sets the TCP SYN flag bit to 1 or 0.
	 * <p>
	 * The TCP SYN flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting to synchronize sequence numbers. When the SYN flag
	 * is set, the sender is indicating that it wants to establish a connection with
	 * the receiver and is requesting that the receiver send its sequence number.
	 * </p>
	 * <p>
	 * The SYN flag is used to initiate a TCP connection. When a sender wants to
	 * establish a connection with a receiver, it sends a TCP segment with the SYN
	 * flag set. The receiver responds with a TCP segment with the SYN and ACK flags
	 * set. This establishes the connection and allows data to be sent between the
	 * sender and receiver.
	 * </p>
	 * <p>
	 * The SYN flag is an important part of the TCP connection establishment
	 * process. It ensures that both sides of the connection are aware of the
	 * sequence numbers that will be used to send data.
	 * </p>
	 * <p>
	 * Here are some examples of how the SYN flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to connect to a server, it sends a TCP segment with
	 * the SYN flag set.</li>
	 * <li>The server responds with a TCP segment with the SYN and ACK flags
	 * set.</li>
	 * <li>The connection is established and data can be sent between the client and
	 * server.</li>
	 * </ul>
	 * <p>
	 * The SYN flag is an important part of the TCP protocol and helps to ensure
	 * that connections are established reliably.
	 * </p>
	 * <p>
	 * Here are some additional details about the SYN flag:
	 * </p>
	 * <ul>
	 * <li>The SYN flag is always set in the first packet of a TCP connection.</li>
	 * <li>The SYN flag is always cleared in subsequent packets of a TCP
	 * connection.</li>
	 * <li>The SYN flag is used to synchronize the sequence numbers of the sender
	 * and receiver.</li>
	 * <li>The SYN flag is an important part of the TCP connection establishment
	 * process.</li>
	 * </ul>
	 * 
	 * @param b if true, sets the flag bit to on (1), otherwise the bit is turned
	 *          off (0)
	 * @return this tcp header instance for Fluent pattern usage
	 */
	public Tcp flags_SYN(boolean b) {
		TcpStruct.FLAGS_SYN.setBoolean(b, buffer());
		return this;
	}

	/**
	 * Checks if the TCP URG flag bit is set.
	 * <p>
	 * The TCP URG flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting that the receiver deliver the data in the segment
	 * immediately. When the URG flag is set, the sender is indicating that the
	 * receiver should not wait for any more data to be sent before delivering the
	 * data to the application layer.
	 * </p>
	 * <p>
	 * The URG flag is used to ensure that urgent data is delivered to the
	 * application layer as quickly as possible. Urgent data is data that needs to
	 * be processed immediately, such as a notification that a user has sent a chat
	 * message. When the URG flag is set, the receiver will deliver the data to the
	 * application layer immediately, even if there is more data that has not yet
	 * been sent.
	 * </p>
	 * <p>
	 * Here are some examples of how the URG flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to send an urgent chat message to a server, it sends
	 * a TCP segment with the URG flag set.</li>
	 * <li>The server responds with a TCP segment with the ACK flag set.</li>
	 * <li>The urgent chat message is delivered to the application layer on the
	 * server immediately.</li>
	 * </ul>
	 * <p>
	 * The URG flag is an important part of the TCP protocol and helps to ensure
	 * that urgent data is delivered to applications as quickly as possible.
	 * </p>
	 * <p>
	 * Here are some additional details about the URG flag:
	 * </p>
	 * <ul>
	 * <li>The URG flag is only set in segments that contain urgent data.</li>
	 * <li>The URG flag is cleared in segments that do not contain urgent data.</li>
	 * <li>The URG flag is used to indicate that the data in the segment should be
	 * delivered to the application layer immediately.</li>
	 * <li>The URG flag is an important part of the TCP protocol and helps to ensure
	 * that urgent data is delivered to applications as quickly as possible.</li>
	 * </ul>
	 * 
	 * @return true, if the flag bit is set, otherwise false
	 */
	public boolean flags_URG() {
		return TcpStruct.FLAGS_URG.getBit(buffer());
	}

	/**
	 * Sets the TCP URG flag bit to 1 or 0.
	 * <p>
	 * The TCP URG flag is a 1-bit field in the TCP header that indicates whether
	 * the sender is requesting that the receiver deliver the data in the segment
	 * immediately. When the URG flag is set, the sender is indicating that the
	 * receiver should not wait for any more data to be sent before delivering the
	 * data to the application layer.
	 * </p>
	 * <p>
	 * The URG flag is used to ensure that urgent data is delivered to the
	 * application layer as quickly as possible. Urgent data is data that needs to
	 * be processed immediately, such as a notification that a user has sent a chat
	 * message. When the URG flag is set, the receiver will deliver the data to the
	 * application layer immediately, even if there is more data that has not yet
	 * been sent.
	 * </p>
	 * <p>
	 * Here are some examples of how the URG flag is used:
	 * </p>
	 * <ul>
	 * <li>When a client wants to send an urgent chat message to a server, it sends
	 * a TCP segment with the URG flag set.</li>
	 * <li>The server responds with a TCP segment with the ACK flag set.</li>
	 * <li>The urgent chat message is delivered to the application layer on the
	 * server immediately.</li>
	 * </ul>
	 * <p>
	 * The URG flag is an important part of the TCP protocol and helps to ensure
	 * that urgent data is delivered to applications as quickly as possible.
	 * </p>
	 * <p>
	 * Here are some additional details about the URG flag:
	 * </p>
	 * <ul>
	 * <li>The URG flag is only set in segments that contain urgent data.</li>
	 * <li>The URG flag is cleared in segments that do not contain urgent data.</li>
	 * <li>The URG flag is used to indicate that the data in the segment should be
	 * delivered to the application layer immediately.</li>
	 * <li>The URG flag is an important part of the TCP protocol and helps to ensure
	 * that urgent data is delivered to applications as quickly as possible.</li>
	 * </ul>
	 * 
	 * @param b if true, sets the flag bit to on (1), otherwise the bit is turned
	 *          off (0)
	 * @return this tcp header instance for Fluent pattern usage
	 */
	public Tcp flags_URG(boolean b) {
		TcpStruct.FLAGS_URG.setBoolean(b, buffer());
		return this;
	}

	/**
	 * Checks if is this TCP frame is part of a reassembled TCP stream.
	 *
	 * @return true, if is reassembled
	 */
	public boolean isReassembled() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * On unbind.
	 *
	 * @see com.slytechs.jnet.protocol.api.common.OptionsHeader#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		super.onUnbind();

		if (wscaleOption != null)
			wscaleOption.unbind();
	}

	/**
	 * Reaseembled segments.
	 *
	 * @return the packet[]
	 */
	public Packet[] reaseembledSegments() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Reserved (4 bits). For future use and should be set to zero. The last bit was
	 * previously defined by Historic RFC 3540 as NS flag (Nonce Sum).
	 *
	 * 
	 * @return the int
	 */
	public int reserved() {
		return TcpStruct.RESERVED.getUnsignedShort(buffer());
	}

	/**
	 * Set a new value in reserved field.
	 *
	 * @param res the res
	 * @return this tcp header instance for Fluent pattern usage
	 */
	public Tcp reserved(int res) {
		TcpStruct.RESERVED.setInt(res, buffer());

		return this;
	}

	/**
	 * Gets the TCP sequence number field from the header.
	 * <p>
	 * The TCP sequence number is a 32-bit number in the TCP header that indicates
	 * the order of data bytes in a TCP connection. The sequence number is used to
	 * ensure that data is delivered in the correct order and to detect lost or
	 * corrupted data.
	 * </p>
	 * <p>
	 * The sequence number is assigned by the sender of data. The first byte of data
	 * sent in a TCP connection will have a sequence number of 0. The next byte of
	 * data will have a sequence number of 1, and so on. The sequence number is
	 * incremented by 1 for each byte of data sent.
	 * </p>
	 * <p>
	 * The receiver of data uses the sequence number to determine the order of the
	 * data bytes. The receiver will not accept data bytes that are out of order. If
	 * the receiver receives a data byte with a sequence number that is less than
	 * the expected sequence number, it will discard the data byte.
	 * </p>
	 * <p>
	 * The sequence number is also used to detect lost or corrupted data. If the
	 * receiver does not receive a data byte with the expected sequence number, it
	 * will assume that the data byte was lost or corrupted. The receiver will then
	 * request that the sender resend the data byte.
	 * </p>
	 * <p>
	 * The TCP sequence number is an important part of the TCP protocol. It ensures
	 * that data is delivered in the correct order and to detect lost or corrupted
	 * data.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP sequence number:
	 * </p>
	 * <ul>
	 * <li>The TCP sequence number is a 32-bit number.</li>
	 * <li>The sequence number is assigned by the sender of data.</li>
	 * <li>The sequence number is incremented by 1 for each byte of data sent.</li>
	 * <li>The receiver of data uses the sequence number to determine the order of
	 * the data bytes.</li>
	 * <li>The receiver will not accept data bytes that are out of order.</li>
	 * <li>The sequence number is also used to detect lost or corrupted data.</li>
	 * <li>The TCP sequence number is an important part of the TCP protocol.</li>
	 * </ul>
	 * <p>
	 * The sequence number a dual role:
	 * </p>
	 * <ul>
	 * <li>If the SYN flag is set (1), then this is the initial sequence number. The
	 * sequence number of the actual first data byte and the acknowledged number in
	 * the corresponding ACK are then this sequence number plus 1.</li>
	 * <li>If the SYN flag is clear (0), then this is the accumulated sequence
	 * number of the first data byte of this segment for the current session.</li>
	 * </ul>
	 *
	 * @return the unsigned 32-bit sequence number
	 */
	@Meta
	public long seq() {
		return TcpStruct.SEQ.getUnsignedInt(buffer());
	}

	/**
	 * Sets the TCP sequence number field in the header.
	 * <p>
	 * The TCP sequence number is a 32-bit number in the TCP header that indicates
	 * the order of data bytes in a TCP connection. The sequence number is used to
	 * ensure that data is delivered in the correct order and to detect lost or
	 * corrupted data.
	 * </p>
	 * <p>
	 * The sequence number is assigned by the sender of data. The first byte of data
	 * sent in a TCP connection will have a sequence number of 0. The next byte of
	 * data will have a sequence number of 1, and so on. The sequence number is
	 * incremented by 1 for each byte of data sent.
	 * </p>
	 * <p>
	 * The receiver of data uses the sequence number to determine the order of the
	 * data bytes. The receiver will not accept data bytes that are out of order. If
	 * the receiver receives a data byte with a sequence number that is less than
	 * the expected sequence number, it will discard the data byte.
	 * </p>
	 * <p>
	 * The sequence number is also used to detect lost or corrupted data. If the
	 * receiver does not receive a data byte with the expected sequence number, it
	 * will assume that the data byte was lost or corrupted. The receiver will then
	 * request that the sender resend the data byte.
	 * </p>
	 * <p>
	 * The TCP sequence number is an important part of the TCP protocol. It ensures
	 * that data is delivered in the correct order and to detect lost or corrupted
	 * data.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP sequence number:
	 * </p>
	 * <ul>
	 * <li>The TCP sequence number is a 32-bit number.</li>
	 * <li>The sequence number is assigned by the sender of data.</li>
	 * <li>The sequence number is incremented by 1 for each byte of data sent.</li>
	 * <li>The receiver of data uses the sequence number to determine the order of
	 * the data bytes.</li>
	 * <li>The receiver will not accept data bytes that are out of order.</li>
	 * <li>The sequence number is also used to detect lost or corrupted data.</li>
	 * <li>The TCP sequence number is an important part of the TCP protocol.</li>
	 * </ul>
	 * <p>
	 * The sequence number a dual role:
	 * </p>
	 * <ul>
	 * <li>If the SYN flag is set (1), then this is the initial sequence number. The
	 * sequence number of the actual first data byte and the acknowledged number in
	 * the corresponding ACK are then this sequence number plus 1.</li>
	 * <li>If the SYN flag is clear (0), then this is the accumulated sequence
	 * number of the first data byte of this segment for the current session.</li>
	 * </ul>
	 *
	 * @param newSeq the new unsigned 32-bit sequence number
	 * @return this tcp header instance for Fluent pattern usage
	 */
	public Tcp seq(long newSeq) {
		TcpStruct.SEQ.setInt((int) newSeq, buffer());

		return this;
	}

	/**
	 * Gets the TCP source port number.
	 * <p>
	 * The TCP source port is a 16-bit number in the TCP header that identifies the
	 * process on the sending host that is sending the data. The source port is used
	 * to uniquely identify the sending process and to ensure that data is delivered
	 * to the correct process on the receiving host.
	 * </p>
	 * <p>
	 * The source port is assigned by the sending host. The first 1024 ports are
	 * reserved for well-known services, such as HTTP (port 80), HTTPS (port 443),
	 * and FTP (port 21). Applications that use well-known services will typically
	 * use the same port number for all connections. Applications that use
	 * non-standard services will typically choose a random port number for each
	 * connection.
	 * </p>
	 * <p>
	 * The receiving host uses the source port to determine which process on the
	 * receiving host should receive the data. The receiving host will keep track of
	 * the source port for each connection and will deliver data to the process on
	 * the receiving host that has the same source port as the sending process.
	 * </p>
	 * <p>
	 * The TCP source port is an important part of the TCP protocol. It ensures that
	 * data is delivered to the correct process on the receiving host.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP source port:
	 * </p>
	 * <ul>
	 * <li>The TCP source port is a 16-bit number.</li>
	 * <li>The source port is assigned by the sending host.</li>
	 * <li>The source port is used to uniquely identify the sending process.</li>
	 * <li>The source port is used to ensure that data is delivered to the correct
	 * process on the receiving host.</li>
	 * </ul>
	 * <p>
	 * The TCP source port is an important part of the TCP protocol.
	 * </p>
	 * 
	 * @return the unsigned 16-bit port number
	 */
	@Meta
	public int source() {
		return TcpStruct.SRC_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Sets the TCP source port number.
	 * <p>
	 * The TCP source port is a 16-bit number in the TCP header that identifies the
	 * process on the sending host that is sending the data. The source port is used
	 * to uniquely identify the sending process and to ensure that data is delivered
	 * to the correct process on the receiving host.
	 * </p>
	 * <p>
	 * The source port is assigned by the sending host. The first 1024 ports are
	 * reserved for well-known services, such as HTTP (port 80), HTTPS (port 443),
	 * and FTP (port 21). Applications that use well-known services will typically
	 * use the same port number for all connections. Applications that use
	 * non-standard services will typically choose a random port number for each
	 * connection.
	 * </p>
	 * <p>
	 * The receiving host uses the source port to determine which process on the
	 * receiving host should receive the data. The receiving host will keep track of
	 * the source port for each connection and will deliver data to the process on
	 * the receiving host that has the same source port as the sending process.
	 * </p>
	 * <p>
	 * The TCP source port is an important part of the TCP protocol. It ensures that
	 * data is delivered to the correct process on the receiving host.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP source port:
	 * </p>
	 * <ul>
	 * <li>The TCP source port is a 16-bit number.</li>
	 * <li>The source port is assigned by the sending host.</li>
	 * <li>The source port is used to uniquely identify the sending process.</li>
	 * <li>The source port is used to ensure that data is delivered to the correct
	 * process on the receiving host.</li>
	 * </ul>
	 * <p>
	 * The TCP source port is an important part of the TCP protocol.
	 * </p>
	 *
	 * @param newPort new unsigned 16-bit port number
	 * @return this tcp header instance for Fluent pattern usage
	 */
	public Tcp source(int newPort) {
		TcpStruct.SRC_PORT.setInt(newPort, buffer());

		return this;
	}

	/**
	 * Gets the TCP urgent pointer field value.
	 * <p>
	 * The TCP urgent pointer is a 16-bit field in the TCP header that indicates the
	 * last byte of urgent data in a TCP segment. Urgent data is data that needs to
	 * be processed immediately, such as a notification that a user has sent a chat
	 * message. When the urgent pointer is set, the receiver will deliver the urgent
	 * data to the application layer immediately, even if there is more data that
	 * has not yet been sent.
	 * </p>
	 * <p>
	 * The urgent pointer is calculated by adding the sequence number of the segment
	 * to the value of the urgent pointer. For example, if the sequence number is
	 * 1000 and the urgent pointer is 100, the last byte of urgent data in the
	 * segment is byte 1100.
	 * </p>
	 * <p>
	 * The urgent pointer is only set in segments that contain urgent data. When the
	 * urgent pointer is not set, the receiver will deliver the data in the segment
	 * to the application layer in the order that it was sent.
	 * </p>
	 * <p>
	 * The urgent pointer is an important part of the TCP protocol. It allows
	 * applications to send urgent data that needs to be processed immediately.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP urgent pointer:
	 * </p>
	 * <ul>
	 * <li>The urgent pointer is a 16-bit field in the TCP header.</li>
	 * <li>The urgent pointer is only set in segments that contain urgent data.</li>
	 * <li>The urgent pointer indicates the last byte of urgent data in the
	 * segment.</li>
	 * <li>The receiver will deliver the urgent data to the application layer
	 * immediately.</li>
	 * </ul>
	 * <p>
	 * The urgent pointer is an important part of the TCP protocol.
	 * </p>
	 *
	 * @return the unsigned 16-bit urgent pointer value
	 */
	@Meta
	public int urgent() {
		return TcpStruct.URGENT_POINTER.getUnsignedShort(buffer());
	}

	/**
	 * Sets the TCP urgent pointer field value.
	 * <p>
	 * The TCP urgent pointer is a 16-bit field in the TCP header that indicates the
	 * last byte of urgent data in a TCP segment. Urgent data is data that needs to
	 * be processed immediately, such as a notification that a user has sent a chat
	 * message. When the urgent pointer is set, the receiver will deliver the urgent
	 * data to the application layer immediately, even if there is more data that
	 * has not yet been sent.
	 * </p>
	 * <p>
	 * The urgent pointer is calculated by adding the sequence number of the segment
	 * to the value of the urgent pointer. For example, if the sequence number is
	 * 1000 and the urgent pointer is 100, the last byte of urgent data in the
	 * segment is byte 1100.
	 * </p>
	 * <p>
	 * The urgent pointer is only set in segments that contain urgent data. When the
	 * urgent pointer is not set, the receiver will deliver the data in the segment
	 * to the application layer in the order that it was sent.
	 * </p>
	 * <p>
	 * The urgent pointer is an important part of the TCP protocol. It allows
	 * applications to send urgent data that needs to be processed immediately.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP urgent pointer:
	 * </p>
	 * <ul>
	 * <li>The urgent pointer is a 16-bit field in the TCP header.</li>
	 * <li>The urgent pointer is only set in segments that contain urgent data.</li>
	 * <li>The urgent pointer indicates the last byte of urgent data in the
	 * segment.</li>
	 * <li>The receiver will deliver the urgent data to the application layer
	 * immediately.</li>
	 * </ul>
	 * <p>
	 * The urgent pointer is an important part of the TCP protocol.
	 * </p>
	 *
	 * @param newUrgentPointer new unsigned 16-bit urgent pointer value
	 * @return this tcp header instance for Fluent pattern usage
	 */
	public Tcp urgent(int newUrgentPointer) {
		TcpStruct.URGENT_POINTER.setInt(newUrgentPointer, buffer());

		return this;
	}

	/**
	 * Gets the TCP window field value.
	 * <p>
	 * The TCP window field is a 16-bit field in the TCP header that indicates the
	 * amount of data that the sender is allowed to send before it must wait for an
	 * acknowledgment from the receiver. The window size is negotiated during the
	 * TCP connection establishment process and can be changed during the connection
	 * lifetime.
	 * </p>
	 * <p>
	 * The window size is used to control the flow of data between the sender and
	 * receiver. When the sender has data to send, it will send up to the amount of
	 * data specified by the window size. The receiver will then acknowledge the
	 * data that it has received and the sender can then send more data.
	 * </p>
	 * <p>
	 * The window size is an important part of the TCP protocol. It ensures that
	 * data is not sent too quickly and that the receiver has time to process the
	 * data that it has received.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP window field:
	 * </p>
	 * <ul>
	 * <li>The TCP window field is a 16-bit field in the TCP header.</li>
	 * <li>The window size is negotiated during the TCP connection establishment
	 * process.</li>
	 * <li>The window size can be changed during the connection lifetime.</li>
	 * <li>The window size is used to control the flow of data between the sender
	 * and receiver.</li>
	 * <li>The window size is an important part of the TCP protocol.</li>
	 * </ul>
	 * <p>
	 * The TCP window field is a critical part of the TCP protocol. It ensures that
	 * data is transferred reliably and efficiently between two hosts.
	 * </p>
	 *
	 * @return the unsigned 16-bit window field value
	 */
	@Meta
	public int window() {
		return TcpStruct.WIN_SIZE.getUnsignedShort(buffer());
	}

	/**
	 * Sets the TCP window field value.
	 * <p>
	 * The TCP window field is a 16-bit field in the TCP header that indicates the
	 * amount of data that the sender is allowed to send before it must wait for an
	 * acknowledgment from the receiver. The window size is negotiated during the
	 * TCP connection establishment process and can be changed during the connection
	 * lifetime.
	 * </p>
	 * <p>
	 * The window size is used to control the flow of data between the sender and
	 * receiver. When the sender has data to send, it will send up to the amount of
	 * data specified by the window size. The receiver will then acknowledge the
	 * data that it has received and the sender can then send more data.
	 * </p>
	 * <p>
	 * The window size is an important part of the TCP protocol. It ensures that
	 * data is not sent too quickly and that the receiver has time to process the
	 * data that it has received.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP window field:
	 * </p>
	 * <ul>
	 * <li>The TCP window field is a 16-bit field in the TCP header.</li>
	 * <li>The window size is negotiated during the TCP connection establishment
	 * process.</li>
	 * <li>The window size can be changed during the connection lifetime.</li>
	 * <li>The window size is used to control the flow of data between the sender
	 * and receiver.</li>
	 * <li>The window size is an important part of the TCP protocol.</li>
	 * </ul>
	 * <p>
	 * The TCP window field is a critical part of the TCP protocol. It ensures that
	 * data is transferred reliably and efficiently between two hosts.
	 * </p>
	 *
	 * @param newSize the new unsigned 16-bit window field value
	 * @return this tcp header instance for Fluent pattern usage
	 * @see #windowScaled()
	 */
	public Tcp window(int newSize) {
		TcpStruct.WIN_SIZE.setInt(newSize, buffer());

		return this;
	}

	/**
	 * A scaled window size in units of bytes.
	 * <p>
	 * If the TCP header has the window-scale option the shift count of the option
	 * will be used. If no TCP window-scale option, then a shift 0 will be used.
	 * </p>
	 * <p>
	 * TCP window scaling is a technique used to increase the window size of a TCP
	 * connection beyond the default 65,535 bytes. This is done by using a window
	 * scale factor, which is a 3-bit field in the TCP header. The window scale
	 * factor can be set from 0 to 14, which allows for a maximum window size of
	 * 1,073,741,824 bytes.
	 * </p>
	 * <p>
	 * To calculate the actual window size, the window scale factor is multiplied by
	 * 2^14, or 16,384. For example, if the window scale factor is 3, the actual
	 * window size will be 1,073,741,824 bytes.
	 * </p>
	 * <p>
	 * TCP window scaling is used to improve the performance of TCP connections by
	 * allowing the sender to send more data before it must wait for an
	 * acknowledgment from the receiver. This can be especially beneficial for
	 * connections with high bandwidth and low latency.
	 * </p>
	 * <p>
	 * Here are some additional details about TCP window scaling:
	 * </p>
	 * <ul>
	 * <li>The window scale factor is a 3-bit field in the TCP header.</li>
	 * <li>The window scale factor can be set from 0 to 14.</li>
	 * <li>The maximum window size is 1,073,741,824 bytes.</li>
	 * <li>TCP window scaling is used to improve the performance of TCP connections
	 * by allowing the sender to send more data before it must wait for an
	 * acknowledgment from the receiver.</li>
	 * </ul>
	 * <p>
	 * TCP window scaling is a valuable tool for improving the performance of TCP
	 * connections. It is especially beneficial for connections with high bandwidth
	 * and low latency.
	 * </p>
	 * 
	 * @return the scaled window value
	 */
	public int windowScaled() {

		if (wscaleOption == null)
			wscaleOption = new TcpWindowScaleOption();

		int shiftCount = 0;
		if (hasOption(wscaleOption))
			shiftCount = wscaleOption.shiftCount();

		return windowScaled(shiftCount);
	}

	/**
	 * A scaled window size in units of bytes using the supplied scaling bitshift
	 * size.
	 * <p>
	 * If the TCP header has the window-scale option the shift count of the option
	 * will be used. If no TCP window-scale option, then a shift 0 will be used.
	 * </p>
	 * <p>
	 * TCP window scaling is a technique used to increase the window size of a TCP
	 * connection beyond the default 65,535 bytes. This is done by using a window
	 * scale factor, which is a 3-bit field in the TCP header. The window scale
	 * factor can be set from 0 to 14, which allows for a maximum window size of
	 * 1,073,741,824 bytes.
	 * </p>
	 * <p>
	 * To calculate the actual window size, the window scale factor is multiplied by
	 * 2^14, or 16,384. For example, if the window scale factor is 3, the actual
	 * window size will be 1,073,741,824 bytes.
	 * </p>
	 * <p>
	 * TCP window scaling is used to improve the performance of TCP connections by
	 * allowing the sender to send more data before it must wait for an
	 * acknowledgment from the receiver. This can be especially beneficial for
	 * connections with high bandwidth and low latency.
	 * </p>
	 * <p>
	 * Here are some additional details about TCP window scaling:
	 * </p>
	 * <ul>
	 * <li>The window scale factor is a 3-bit field in the TCP header.</li>
	 * <li>The window scale factor can be set from 0 to 14.</li>
	 * <li>The maximum window size is 1,073,741,824 bytes.</li>
	 * <li>TCP window scaling is used to improve the performance of TCP connections
	 * by allowing the sender to send more data before it must wait for an
	 * acknowledgment from the receiver.</li>
	 * </ul>
	 * <p>
	 * TCP window scaling is a valuable tool for improving the performance of TCP
	 * connections. It is especially beneficial for connections with high bandwidth
	 * and low latency.
	 * </p>
	 *
	 * @param scale the bitshift scale to use
	 * @return the scaled window value
	 */
	public int windowScaled(int scale) {
		return window() << scale;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#getOption(com.slytechs.jnet.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends TcpOption> E getOption(E extension, int depth) throws HeaderNotFound {
		return super.getOptionHeader(extension, depth);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#hasOption(int, int)
	 */
	@Override
	public boolean hasOption(int extensionId, int depth) {
		return super.hasOptionHeader(extensionId, depth);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#peekOption(com.slytechs.jnet.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends TcpOption> E peekOption(E extension, int depth) {
		return super.peekOptionHeader(extension, depth);
	}

}
