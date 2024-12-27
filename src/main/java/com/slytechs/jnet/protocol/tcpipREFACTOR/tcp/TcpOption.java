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
package com.slytechs.jnet.protocol.tcpipREFACTOR.tcp;

import static com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreConstants.*;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreConstants;

/**
 * Base header class for all TCP options.
 * <p>
 * TCP options are optional fields in the TCP header that can be used to control
 * the behavior of the TCP protocol. There are a number of different TCP
 * options, but some of the most common ones include:
 * </p>
 * <ul>
 * <li>Maximum Segment Size (MSS): The MSS option is used to negotiate the
 * maximum size of a TCP segment that can be sent over the connection. This is
 * important because it helps to prevent data from being fragmented, which can
 * lead to performance problems.</li>
 * <li>Window Scale Option: The Window Scale option is used to increase the
 * window size of a TCP connection beyond the default 65,535 bytes. This can be
 * beneficial for connections with high bandwidth and low latency.</li>
 * <li>Selective Acknowledgments (SACK) Option: The SACK option allows the
 * receiver to acknowledge individual segments of data, rather than just the
 * entire window. This can help to improve the performance of TCP connections by
 * reducing the number of retransmissions that are needed.</li>
 * <li>Timestamp Option: The Timestamp option allows the sender and receiver to
 * keep track of the time that data is sent and received. This can be used to
 * help diagnose performance problems and to detect lost or corrupted data.</li>
 * </ul>
 * <p>
 * TCP options are a valuable tool for improving the performance and reliability
 * of TCP connections. They can be used to control the behavior of the TCP
 * protocol and to adapt the protocol to the specific needs of the network.
 * </p>
 * <p>
 * Here are some additional details about TCP options:
 * </p>
 * <ul>
 * <li>TCP options are optional fields in the TCP header.</li>
 * <li>TCP options are used to control the behavior of the TCP protocol.</li>
 * <li>There are a number of different TCP options, but some of the most common
 * ones include MSS, Window Scale, SACK, and Timestamp.</li>
 * <li>TCP options can be used to improve the performance and reliability of TCP
 * connections.</li>
 * </ul>
 */
@MetaResource("tcp-opt-meta.json")
public class TcpOption extends Header {

	/** The TCP base option header ID constant. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_OPTION;;

	/** The kind. */
	private final int kind;

	/** The length. */
	private final int length;

	/**
	 * Instantiates a new tcp option.
	 */
	public TcpOption() {
		super(ID);

		this.length = length();
		this.kind = kind();
	}

	/**
	 * Instantiates a new tcp option.
	 *
	 * @param id   the id
	 * @param kind the kind
	 */
	protected TcpOption(int id, int kind) {
		super(id);
		this.kind = kind;
		this.length = -1; // Dynamic length
	}

	/**
	 * Instantiates a new tcp option.
	 *
	 * @param id             the id
	 * @param kind           the kind
	 * @param constantLength the constant length
	 */
	protected TcpOption(int id, int kind, int constantLength) {
		super(id);
		this.kind = kind;
		this.length = constantLength;
	}

	/**
	 * Gets the TCP option kind field value.
	 * <p>
	 * The TCP option kind field is a 1-byte field in the TCP header that identifies
	 * the type of TCP option. There are a number of different TCP options, each
	 * with its own unique kind field value.
	 * </p>
	 * <p>
	 * The following table lists the most common TCP options and their kind field
	 * values:
	 * </p>
	 * <table>
	 * <caption>Common TCP options</caption>
	 * <tr>
	 * <th>Option</th>
	 * <th>Kind Field Value</th>
	 * </tr>
	 * <tr>
	 * <td>Maximum Segment Size (MSS)</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>Window Scale</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td>Selected Acknowledgments (SACK)</td>
	 * <td>4</td>
	 * </tr>
	 * <tr>
	 * <td>Timestamp</td>
	 * <td>8</td>
	 * </tr>
	 * <tr>
	 * <td>Nop</td>
	 * <td>0</td>
	 * </tr>
	 * </table>
	 * <p>
	 * The TCP option kind field is used by the sender and receiver to determine
	 * which TCP options are present in a TCP segment. The sender and receiver use
	 * this information to interpret the data in the TCP segment and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * The TCP option kind field is an important part of the TCP protocol. It allows
	 * the sender and receiver to communicate with each other and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * 
	 * @return the unsigned 8-bit field value
	 */
	@Meta
	public int kind() {
		return kind;
	}

	/**
	 * Sets the TCP option kind field value.
	 * <p>
	 * The TCP option kind field is a 1-byte field in the TCP header that identifies
	 * the type of TCP option. There are a number of different TCP options, each
	 * with its own unique kind field value.
	 * </p>
	 * <p>
	 * The following table lists the most common TCP options and their kind field
	 * values:
	 * </p>
	 * <table>
	 * <caption>Common TCP options</caption>
	 * <tr>
	 * <th>Option</th>
	 * <th>Kind Field Value</th>
	 * </tr>
	 * <tr>
	 * <td>Maximum Segment Size (MSS)</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>Window Scale</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td>Selected Acknowledgments (SACK)</td>
	 * <td>4</td>
	 * </tr>
	 * <tr>
	 * <td>Timestamp</td>
	 * <td>8</td>
	 * </tr>
	 * <tr>
	 * <td>Nop</td>
	 * <td>0</td>
	 * </tr>
	 * </table>
	 * <p>
	 * The TCP option kind field is used by the sender and receiver to determine
	 * which TCP options are present in a TCP segment. The sender and receiver use
	 * this information to interpret the data in the TCP segment and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * The TCP option kind field is an important part of the TCP protocol. It allows
	 * the sender and receiver to communicate with each other and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 *
	 * @param newKind new unsigned 8-bit field value
	 * @return the tcp option
	 */
	public TcpOption kind(int newKind) {
		buffer().put(CoreConstants.TCP_OPTION_FIELD_KIND, (byte) newKind);

		return this;
	}

	/**
	 * Gets the TCP option length field value.
	 * <p>
	 * The TCP option length field is a 1-byte field in the TCP header that
	 * indicates the length of the TCP option. The length of the TCP option is
	 * measured in bytes, and it includes the length of the kind field and the
	 * length of the option data.
	 * </p>
	 * <p>
	 * The TCP option length field is used by the sender and receiver to determine
	 * how much data to read from the TCP segment. The sender and receiver use this
	 * information to interpret the data in the TCP segment and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * The TCP option length field is an important part of the TCP protocol. It
	 * allows the sender and receiver to communicate with each other and to control
	 * the behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP option length field:
	 * </p>
	 * <ul>
	 * <li>The TCP option length field is a 1-byte field in the TCP header.</li>
	 * <li>The length of the TCP option is measured in bytes.</li>
	 * <li>The length of the TCP option includes the length of the kind field and
	 * the length of the option data.</li>
	 * <li>The TCP option length field is used by the sender and receiver to
	 * determine how much data to read from the TCP segment.</li>
	 * </ul>
	 * <p>
	 * The TCP option length field is an important part of the TCP protocol.
	 * </p>
	 * 
	 * @return the usigned 8-bit length field value in units of bytes
	 */
	@Meta
	public int length() {
		return ((length != -1)
				? length
				: Byte.toUnsignedInt(buffer().get(TCP_OPTION_FIELD_LENGTH)));
	}

	/**
	 * Sets the TCP option length field value.
	 * <p>
	 * The TCP option length field is a 1-byte field in the TCP header that
	 * indicates the length of the TCP option. The length of the TCP option is
	 * measured in bytes, and it includes the length of the kind field and the
	 * length of the option data.
	 * </p>
	 * <p>
	 * The TCP option length field is used by the sender and receiver to determine
	 * how much data to read from the TCP segment. The sender and receiver use this
	 * information to interpret the data in the TCP segment and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * The TCP option length field is an important part of the TCP protocol. It
	 * allows the sender and receiver to communicate with each other and to control
	 * the behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP option length field:
	 * </p>
	 * <ul>
	 * <li>The TCP option length field is a 1-byte field in the TCP header.</li>
	 * <li>The length of the TCP option is measured in bytes.</li>
	 * <li>The length of the TCP option includes the length of the kind field and
	 * the length of the option data.</li>
	 * <li>The TCP option length field is used by the sender and receiver to
	 * determine how much data to read from the TCP segment.</li>
	 * </ul>
	 * <p>
	 * The TCP option length field is an important part of the TCP protocol.
	 * </p>
	 *
	 * @param newLength new unsigned 8-bit length field value, in units of bytes
	 */
	public void length(int newLength) {
		buffer().put(TCP_OPTION_FIELD_LENGTH, (byte) kind);
	}

}