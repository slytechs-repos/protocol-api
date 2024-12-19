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
package com.slytechs.jnet.protocol.tcpip.link;

/**
 * PPP Protocol field values as defined in RFC 1661 and related RFCs.
 */
public enum PppProtocol {

	// Network Layer Protocols (0x0000 - 0x3fff)
	PADDING(PppProtocol.PADDING_PROTOCOL),
	ROHC_SMALL_CID(PppProtocol.ROHC_SMALL_CID_PROTOCOL),
	ROHC_LARGE_CID(PppProtocol.ROHC_LARGE_CID_PROTOCOL),
	IPv4(PppProtocol.IPv4_PROTOCOL),
	OSI_NETWORK_LAYER(PppProtocol.OSI_NETWORK_LAYER_PROTOCOL),
	XEROX_NS_IDP(PppProtocol.XEROX_NS_IDP_PROTOCOL),
	DECNET_IV(PppProtocol.DECNET_IV_PROTOCOL),
	APPLETALK(PppProtocol.APPLETALK_PROTOCOL),
	NOVELL_IPX(PppProtocol.NOVELL_IPX_PROTOCOL),
	VJ_COMPRESSED_TCP_IP(PppProtocol.VJ_COMPRESSED_TCP_IP_PROTOCOL),
	VJ_UNCOMPRESSED_TCP_IP(PppProtocol.VJ_UNCOMPRESSED_TCP_IP_PROTOCOL),
	BRIDGING_PDU(PppProtocol.BRIDGING_PDU_PROTOCOL),
	STREAM_PROTOCOL(PppProtocol.STREAM_PROTOCOL_PROTOCOL),
	BANYAN_VINES(PppProtocol.BANYAN_VINES_PROTOCOL),
	APPLETALK_EDDP(PppProtocol.APPLETALK_EDDP_PROTOCOL),
	APPLETALK_SMARTBUFFERED(PppProtocol.APPLETALK_SMARTBUFFERED_PROTOCOL),
	MULTI_LINK(PppProtocol.MULTI_LINK_PROTOCOL),
	NETBIOS_FRAMING(PppProtocol.NETBIOS_FRAMING_PROTOCOL),
	CISCO_SYSTEMS(PppProtocol.CISCO_SYSTEMS_PROTOCOL),
	ASCOM_TIMEPLEX(PppProtocol.ASCOM_TIMEPLEX_PROTOCOL),
	LBLB(PppProtocol.LBLB_PROTOCOL),
	DCA_REMOTE_LAN(PppProtocol.DCA_REMOTE_LAN_PROTOCOL),
	SERIAL_DATA_TRANSPORT(PppProtocol.SERIAL_DATA_TRANSPORT_PROTOCOL),
	SNA_OVER_802_2(PppProtocol.SNA_OVER_802_2_PROTOCOL),
	SNA(PppProtocol.SNA_PROTOCOL),
	IPv6_HEADER_COMPRESSION(PppProtocol.IPv6_HEADER_COMPRESSION_PROTOCOL),
	KNX_BRIDGING_DATA(PppProtocol.KNX_BRIDGING_DATA_PROTOCOL),
	ENCRYPTION(PppProtocol.ENCRYPTION_PROTOCOL),
	INDIVIDUAL_LINK_ENCRYPTION(PppProtocol.INDIVIDUAL_LINK_ENCRYPTION_PROTOCOL),
	IPv6(PppProtocol.IPv6_PROTOCOL),
	PPP_MUXING(PppProtocol.PPP_MUXING_PROTOCOL),
	VENDOR_SPECIFIC_NETWORK(PppProtocol.VENDOR_SPECIFIC_NETWORK_PROTOCOL),
	RTP_IPHC_FULL_HEADER(PppProtocol.RTP_IPHC_FULL_HEADER_PROTOCOL),
	RTP_IPHC_COMPRESSED_TCP(PppProtocol.RTP_IPHC_COMPRESSED_TCP_PROTOCOL),
	MP_PLUS_PROTOCOL(PppProtocol.MP_PLUS_PROTOCOL_PROTOCOL),
	NTCITS_IPI(PppProtocol.NTCITS_IPI_PROTOCOL),
	SINGLE_LINK_COMPRESSION(PppProtocol.SINGLE_LINK_COMPRESSION_PROTOCOL),
	COMPRESSED_DATAGRAM(PppProtocol.COMPRESSED_DATAGRAM_PROTOCOL),

	// Protocol Field Assignments (0x4000 - 0x7fff)
	LINK_QUALITY_REPORT(PppProtocol.LINK_QUALITY_REPORT_PROTOCOL),
	MAGIC_NUMBER(PppProtocol.MAGIC_NUMBER_PROTOCOL),
	LCP(PppProtocol.LCP_PROTOCOL),
	PAP(PppProtocol.PAP_PROTOCOL),
	LQR(PppProtocol.LQR_PROTOCOL),
	SPAP(PppProtocol.SPAP_PROTOCOL),
	CBCP(PppProtocol.CBCP_PROTOCOL),
	BACP(PppProtocol.BACP_PROTOCOL),
	BAP(PppProtocol.BAP_PROTOCOL),
	CERTIFICATE_AUTH_PROTOCOL(PppProtocol.CERTIFICATE_AUTH_PROTOCOL_PROTOCOL),
	STAMPED_DATA_PATH(PppProtocol.STAMPED_DATA_PATH_PROTOCOL),
	MP_PLUS_CONTROL(PppProtocol.MP_PLUS_CONTROL_PROTOCOL),
	NTCITS_IPI_CONTROL(PppProtocol.NTCITS_IPI_CONTROL_PROTOCOL),
	VENDOR_SPECIFIC_AUTH(PppProtocol.VENDOR_SPECIFIC_AUTH_PROTOCOL),
	CHAP(PppProtocol.CHAP_PROTOCOL),
	RSA_AUTH_PROTOCOL(PppProtocol.RSA_AUTH_PROTOCOL_PROTOCOL),

	// Network Control Protocols (0x8000 - 0xbfff)
	IPCP(PppProtocol.IPCP_PROTOCOL),
	OSI_NETWORK_CONTROL(PppProtocol.OSI_NETWORK_CONTROL_PROTOCOL),
	XEROX_NS_IDP_CONTROL(PppProtocol.XEROX_NS_IDP_CONTROL_PROTOCOL),
	DECNET_IV_CONTROL(PppProtocol.DECNET_IV_CONTROL_PROTOCOL),
	APPLETALK_CONTROL(PppProtocol.APPLETALK_CONTROL_PROTOCOL),
	NOVELL_IPX_CONTROL(PppProtocol.NOVELL_IPX_CONTROL_PROTOCOL),
	BRIDGING_NCP(PppProtocol.BRIDGING_NCP_PROTOCOL),
	STREAM_PROTOCOL_CONTROL(PppProtocol.STREAM_PROTOCOL_CONTROL_PROTOCOL),
	BANYAN_VINES_CONTROL(PppProtocol.BANYAN_VINES_CONTROL_PROTOCOL),
	MULTI_LINK_CONTROL(PppProtocol.MULTI_LINK_CONTROL_PROTOCOL),
	NETBIOS_FRAMING_CONTROL(PppProtocol.NETBIOS_FRAMING_CONTROL_PROTOCOL),
	CISCO_SYSTEMS_CONTROL(PppProtocol.CISCO_SYSTEMS_CONTROL_PROTOCOL),
	ASCOM_TIMEPLEX_CONTROL(PppProtocol.ASCOM_TIMEPLEX_CONTROL_PROTOCOL),
	LBLB_CONTROL(PppProtocol.LBLB_CONTROL_PROTOCOL),
	DCA_REMOTE_LAN_CONTROL(PppProtocol.DCA_REMOTE_LAN_CONTROL_PROTOCOL),
	SERIAL_DATA_CONTROL(PppProtocol.SERIAL_DATA_CONTROL_PROTOCOL),
	SNA_802_2_CONTROL(PppProtocol.SNA_802_2_CONTROL_PROTOCOL),
	SNA_CONTROL(PppProtocol.SNA_CONTROL_PROTOCOL),
	IPv6_HEADER_COMPRESSION_CONTROL(PppProtocol.IPv6_HEADER_COMPRESSION_CONTROL_PROTOCOL),
	KNX_BRIDGING_CONTROL(PppProtocol.KNX_BRIDGING_CONTROL_PROTOCOL),
	ENCRYPTION_CONTROL(PppProtocol.ENCRYPTION_CONTROL_PROTOCOL),
	INDIVIDUAL_LINK_ENCRYPTION_CONTROL(PppProtocol.INDIVIDUAL_LINK_ENCRYPTION_CONTROL_PROTOCOL),
	IPv6CP_CONTROL(PppProtocol.IPv6CP_CONTROL_PROTOCOL),
	PPP_MUXING_CONTROL(PppProtocol.PPP_MUXING_CONTROL_PROTOCOL),
	VENDOR_SPECIFIC_NETWORK_CONTROL(PppProtocol.VENDOR_SPECIFIC_NETWORK_CONTROL_PROTOCOL),
	STAMPEDE_BRIDGING_CONTROL(PppProtocol.STAMPEDE_BRIDGING_CONTROL_PROTOCOL),

	// Link Control Protocols (0xc000 - 0xffff)
	LCP_LINK_CONTROL(PppProtocol.LCP_LINK_CONTROL_PROTOCOL),
	PAP_AUTH(PppProtocol.PAP_AUTH_PROTOCOL),
	LQR_LINK_QUALITY(PppProtocol.LQR_LINK_QUALITY_PROTOCOL),
	SPAP_AUTH(PppProtocol.SPAP_AUTH_PROTOCOL),
	CBCP_CALLBACK(PppProtocol.CBCP_CALLBACK_PROTOCOL),
	BACP_BANDWIDTH(PppProtocol.BACP_BANDWIDTH_PROTOCOL),
	BAP_BANDWIDTH(PppProtocol.BAP_BANDWIDTH_PROTOCOL),
	CAP_CERTIFICATE(PppProtocol.CAP_CERTIFICATE_PROTOCOL),
	SDCP_DATA_PATH(PppProtocol.SDCP_DATA_PATH_PROTOCOL),
	MP_PLUS_CP(PppProtocol.MP_PLUS_CP_PROTOCOL),
	NTCITS_IPI_CP(PppProtocol.NTCITS_IPI_CP_PROTOCOL),
	VENDOR_SPECIFIC_CP(PppProtocol.VENDOR_SPECIFIC_CP_PROTOCOL),
	CONTAINER_CONTROL(PppProtocol.CONTAINER_CONTROL_PROTOCOL),
	CHAP_AUTH(PppProtocol.CHAP_AUTH_PROTOCOL),
	STAMPEDE_BRIDGING_AUTH(PppProtocol.STAMPEDE_BRIDGING_AUTH_PROTOCOL),
	PROPRIETARY_AUTH_1(PppProtocol.PROPRIETARY_AUTH_1_PROTOCOL),
	PROPRIETARY_AUTH_2(PppProtocol.PROPRIETARY_AUTH_2_PROTOCOL),
	PROPRIETARY_NODE_ID_AUTH(PppProtocol.PROPRIETARY_NODE_ID_AUTH_PROTOCOL);

	/* Network Layer Protocol Constants (0x0000 - 0x3fff) */

	/** Protocol for padding between data elements */
	public static final int PADDING_PROTOCOL = 0x0001;

	/** RObust Header Compression with small Context Identifier */
	public static final int ROHC_SMALL_CID_PROTOCOL = 0x0003;

	/** RObust Header Compression with large Context Identifier */
	public static final int ROHC_LARGE_CID_PROTOCOL = 0x0005;

	/** Internet Protocol version 4 */
	public static final int IPv4_PROTOCOL = 0x0021;

	/** OSI Network Layer protocol */
	public static final int OSI_NETWORK_LAYER_PROTOCOL = 0x0023;

	/** Xerox NS IDP protocol */
	public static final int XEROX_NS_IDP_PROTOCOL = 0x0025;

	/** DECnet Phase IV protocol */
	public static final int DECNET_IV_PROTOCOL = 0x0027;

	/** Apple Talk protocol */
	public static final int APPLETALK_PROTOCOL = 0x0029;

	/** Novell Internet Packet Exchange protocol */
	public static final int NOVELL_IPX_PROTOCOL = 0x002b;

	/** Van Jacobson Compressed TCP/IP protocol */
	public static final int VJ_COMPRESSED_TCP_IP_PROTOCOL = 0x002d;

	/** Van Jacobson Uncompressed TCP/IP protocol */
	public static final int VJ_UNCOMPRESSED_TCP_IP_PROTOCOL = 0x002f;

	/** Ethernet bridging protocol for PDUs */
	public static final int BRIDGING_PDU_PROTOCOL = 0x0031;

	/** Stream protocol (ST-II) */
	public static final int STREAM_PROTOCOL_PROTOCOL = 0x0033;

	/** Banyan Vines protocol */
	public static final int BANYAN_VINES_PROTOCOL = 0x0035;

	/** AppleTalk EDDP protocol */
	public static final int APPLETALK_EDDP_PROTOCOL = 0x0039;

	/** AppleTalk SmartBuffered protocol */
	public static final int APPLETALK_SMARTBUFFERED_PROTOCOL = 0x003b;

	/** Multi-Link protocol */
	public static final int MULTI_LINK_PROTOCOL = 0x003d;

	/** NETBIOS Framing protocol */
	public static final int NETBIOS_FRAMING_PROTOCOL = 0x003f;

	/** Cisco Systems protocol */
	public static final int CISCO_SYSTEMS_PROTOCOL = 0x0041;

	/** Ascom Timeplex protocol */
	public static final int ASCOM_TIMEPLEX_PROTOCOL = 0x0043;

	/** LBLB (Land/Bridge/Land/Bridge) protocol */
	public static final int LBLB_PROTOCOL = 0x0045;

	/** DCA Remote LAN protocol */
	public static final int DCA_REMOTE_LAN_PROTOCOL = 0x0047;

	/** Serial Data Transport Protocol */
	public static final int SERIAL_DATA_TRANSPORT_PROTOCOL = 0x0049;

	/** SNA over IEEE 802.2 protocol */
	public static final int SNA_OVER_802_2_PROTOCOL = 0x004b;

	/** IBM SNA protocol */
	public static final int SNA_PROTOCOL = 0x004d;

	/** IPv6 Header Compression protocol */
	public static final int IPv6_HEADER_COMPRESSION_PROTOCOL = 0x004f;

	/** KNX Bridging Data protocol */
	public static final int KNX_BRIDGING_DATA_PROTOCOL = 0x0051;

	/** Encryption protocol */
	public static final int ENCRYPTION_PROTOCOL = 0x0053;

	/** Individual Link Encryption protocol */
	public static final int INDIVIDUAL_LINK_ENCRYPTION_PROTOCOL = 0x0055;

	/** Internet Protocol version 6 */
	public static final int IPv6_PROTOCOL = 0x0057;

	/** PPP Multiplexing protocol */
	public static final int PPP_MUXING_PROTOCOL = 0x0059;

	/** Vendor-Specific Network protocol */
	public static final int VENDOR_SPECIFIC_NETWORK_PROTOCOL = 0x005b;

	/** RTP IPHC Full Header protocol */
	public static final int RTP_IPHC_FULL_HEADER_PROTOCOL = 0x0061;

	/** RTP IPHC Compressed TCP protocol */
	public static final int RTP_IPHC_COMPRESSED_TCP_PROTOCOL = 0x0063;

	/** Multi-Protocol Plus protocol */
	public static final int MP_PLUS_PROTOCOL_PROTOCOL = 0x0065;

	/** NTCITS IPI protocol */
	public static final int NTCITS_IPI_PROTOCOL = 0x0067;

	/** Single Link Compression protocol */
	public static final int SINGLE_LINK_COMPRESSION_PROTOCOL = 0x00fb;

	/** Compressed Datagram protocol */
	public static final int COMPRESSED_DATAGRAM_PROTOCOL = 0x00fd;

	/* Protocol Field Assignments (0x4000 - 0x7fff) */

	/** Link Quality Report protocol */
	public static final int LINK_QUALITY_REPORT_PROTOCOL = 0x4001;

	/** Magic Number protocol */
	public static final int MAGIC_NUMBER_PROTOCOL = 0x4003;

	/** Link Control Protocol */
	public static final int LCP_PROTOCOL = 0x4021;

	/** Password Authentication Protocol */
	public static final int PAP_PROTOCOL = 0x4023;

	/** Link Quality Monitor protocol */
	public static final int LQR_PROTOCOL = 0x4025;

	/** Shiva Password Authentication Protocol */
	public static final int SPAP_PROTOCOL = 0x4027;

	/** Callback Control Protocol */
	public static final int CBCP_PROTOCOL = 0x4029;

	/** Bandwidth Allocation Control Protocol */
	public static final int BACP_PROTOCOL = 0x402b;

	/** Bandwidth Allocation Protocol */
	public static final int BAP_PROTOCOL = 0x402d;

	/** Certificate Authentication Protocol */
	public static final int CERTIFICATE_AUTH_PROTOCOL_PROTOCOL = 0x4051;

	/** Stamped Data Path Protocol */
	public static final int STAMPED_DATA_PATH_PROTOCOL = 0x4053;

	/** Multi-Protocol Plus Control Protocol */
	public static final int MP_PLUS_CONTROL_PROTOCOL = 0x4055;

	/** NTCITS IPI Control Protocol */
	public static final int NTCITS_IPI_CONTROL_PROTOCOL = 0x4057;

	/** Vendor-Specific Authentication Protocol */
	public static final int VENDOR_SPECIFIC_AUTH_PROTOCOL = 0x405b;

	/** Challenge Handshake Authentication Protocol */
	public static final int CHAP_PROTOCOL = 0x4223;

	/** RSA Authentication Protocol */
	public static final int RSA_AUTH_PROTOCOL_PROTOCOL = 0x4235;

	/* Network Control Protocols (0x8000 - 0xbfff) */

	/** Internet Protocol Control Protocol */
	public static final int IPCP_PROTOCOL = 0x8021;

	/** OSI Network Layer Control Protocol */
	public static final int OSI_NETWORK_CONTROL_PROTOCOL = 0x8023;

	/** Xerox NS IDP Control Protocol */
	public static final int XEROX_NS_IDP_CONTROL_PROTOCOL = 0x8025;

	/** DECnet Phase IV Control Protocol */
	public static final int DECNET_IV_CONTROL_PROTOCOL = 0x8027;

	/** AppleTalk Control Protocol */
	public static final int APPLETALK_CONTROL_PROTOCOL = 0x8029;

	/** Novell IPX Control Protocol */
	public static final int NOVELL_IPX_CONTROL_PROTOCOL = 0x802b;

	/** Bridging NCP Protocol */
	public static final int BRIDGING_NCP_PROTOCOL = 0x8031;

	/** Stream Protocol Control Protocol */
	public static final int STREAM_PROTOCOL_CONTROL_PROTOCOL = 0x8033;

	/** Banyan Vines Control Protocol */
	public static final int BANYAN_VINES_CONTROL_PROTOCOL = 0x8035;

	/** Multi-Link Control Protocol */
	public static final int MULTI_LINK_CONTROL_PROTOCOL = 0x803d;

	/** NETBIOS Framing Control Protocol */
	public static final int NETBIOS_FRAMING_CONTROL_PROTOCOL = 0x803f;

	/** Cisco Systems Control Protocol */
	public static final int CISCO_SYSTEMS_CONTROL_PROTOCOL = 0x8041;

	/** Ascom Timeplex Control Protocol */
	public static final int ASCOM_TIMEPLEX_CONTROL_PROTOCOL = 0x8043;

	/** LBLB Control Protocol */
	public static final int LBLB_CONTROL_PROTOCOL = 0x8045;

	/** DCA Remote LAN Network Control Protocol */
	public static final int DCA_REMOTE_LAN_CONTROL_PROTOCOL = 0x8047;

	/** Serial Data Control Protocol */
	public static final int SERIAL_DATA_CONTROL_PROTOCOL = 0x8049;

	/** SNA over 802.2 Control Protocol */
	public static final int SNA_802_2_CONTROL_PROTOCOL = 0x804b;

	/** SNA Control Protocol */
	public static final int SNA_CONTROL_PROTOCOL = 0x804d;

	/** IPv6 Header Compression Control Protocol */
	public static final int IPv6_HEADER_COMPRESSION_CONTROL_PROTOCOL = 0x804f;

	/** KNX Bridging Control Protocol */
	public static final int KNX_BRIDGING_CONTROL_PROTOCOL = 0x8051;

	/** Encryption Control Protocol */
	public static final int ENCRYPTION_CONTROL_PROTOCOL = 0x8053;

	/** Individual Link Encryption Control Protocol */
	public static final int INDIVIDUAL_LINK_ENCRYPTION_CONTROL_PROTOCOL = 0x8055;

	/** IPv6CP Control Protocol */
	public static final int IPv6CP_CONTROL_PROTOCOL = 0x8057;

	/** PPP Muxing Control Protocol */
	public static final int PPP_MUXING_CONTROL_PROTOCOL = 0x8059;

	/** Vendor-Specific Network Control Protocol */
	public static final int VENDOR_SPECIFIC_NETWORK_CONTROL_PROTOCOL = 0x805b;

	/** Stampede Bridging Control Protocol */
	public static final int STAMPEDE_BRIDGING_CONTROL_PROTOCOL = 0x806f;

	/* Link Control Protocols (0xc000 - 0xffff) */

	/** Link Control Protocol */
	public static final int LCP_LINK_CONTROL_PROTOCOL = 0xc021;

	/** Password Authentication Protocol */
	public static final int PAP_AUTH_PROTOCOL = 0xc023;

	/** Link Quality Report Protocol */
	public static final int LQR_LINK_QUALITY_PROTOCOL = 0xc025;

	/** Shiva Password Authentication Protocol */
	public static final int SPAP_AUTH_PROTOCOL = 0xc027;

	/** Callback Control Protocol */
	public static final int CBCP_CALLBACK_PROTOCOL = 0xc029;

	/** Bandwidth Allocation Control Protocol */
	public static final int BACP_BANDWIDTH_PROTOCOL = 0xc02b;

	/** Bandwidth Allocation Protocol */
	public static final int BAP_BANDWIDTH_PROTOCOL = 0xc02d;

	/** Certificate Authentication Protocol */
	public static final int CAP_CERTIFICATE_PROTOCOL = 0xc051;

	/** Stamped Data Path Protocol */
	public static final int SDCP_DATA_PATH_PROTOCOL = 0xc053;

	/** MP+ Control Protocol */
	public static final int MP_PLUS_CP_PROTOCOL = 0xc055;

	/** NTCITS IPI Control Protocol */
	public static final int NTCITS_IPI_CP_PROTOCOL = 0xc057;

	/** Vendor-Specific Protocol */
	public static final int VENDOR_SPECIFIC_CP_PROTOCOL = 0xc059;

	/** Container Control Protocol */
	public static final int CONTAINER_CONTROL_PROTOCOL = 0xc081;

	/** Challenge Handshake Authentication Protocol */
	public static final int CHAP_AUTH_PROTOCOL = 0xc223;

	/** Stampede Bridging Authorization Protocol */
	public static final int STAMPEDE_BRIDGING_AUTH_PROTOCOL = 0xc26f;

	/** Proprietary Authentication Protocol 1 */
	public static final int PROPRIETARY_AUTH_1_PROTOCOL = 0xc281;

	/** Proprietary Authentication Protocol 2 */
	public static final int PROPRIETARY_AUTH_2_PROTOCOL = 0xc283;

	/** Proprietary Node ID Authentication Protocol */
	public static final int PROPRIETARY_NODE_ID_AUTH_PROTOCOL = 0xc481;

	/**
	 * Resolve protocol value to name.
	 *
	 * @param protocolIntValue the protocol int value
	 * @return the protocol name or "Unknown" if not matched
	 */
	public static String resolveProtocol(Object protocolIntValue) {
		var resolved = valueOf((Integer) protocolIntValue);
		if (resolved == null)
			return "Unknown";

		return resolved.toString();
	}

	/**
	 * Looks up a protocol by its ID value.
	 *
	 * @param id the protocol ID to look up
	 * @return the corresponding PppProtocol enum value, or null if not found
	 */
	public static PppProtocol valueOf(int id) {
		for (PppProtocol p : values()) {
			if (p.id == id) {
				return p;
			}
		}
		return null;
	}

	private final int id;

	PppProtocol(int id) {
		this.id = id;
	}

	/**
	 * Gets the protocol ID value.
	 *
	 * @return the protocol ID
	 */
	public int getId() {
		return id;
	}
}