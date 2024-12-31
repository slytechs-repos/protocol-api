/**
 * Core network layer protocols for IP networking.
 * <p>
 * Implements fundamental Layer 3 protocols including IPv4, IPv6, ICMP, IGMP,
 * and related protocols like IPsec and GRE.
 * </p>
 * 
 * <h2>Supported Protocols:</h2>
 * <table>
 * <tr>
 * <th>Protocol</th>
 * <th>Class</th>
 * <th>Common Use</th>
 * </tr>
 * <tr>
 * <td>IPv4</td>
 * <td>{@link Ip4}</td>
 * <td>Internet Protocol v4</td>
 * </tr>
 * <tr>
 * <td>IPv6</td>
 * <td>{@link Ip6}</td>
 * <td>Internet Protocol v6</td>
 * </tr>
 * <tr>
 * <td>ICMPv4</td>
 * <td>{@link Icmp4}</td>
 * <td>Control messages</td>
 * </tr>
 * <tr>
 * <td>ICMPv6</td>
 * <td>{@link Icmp6}</td>
 * <td>IPv6 control</td>
 * </tr>
 * <tr>
 * <td>IGMP</td>
 * <td>{@link Igmp}</td>
 * <td>Multicast groups</td>
 * </tr>
 * </table>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>{@code
 * try (NetPcap pcap = NetPcap.openOffline("network.pcap")) {
 * 	Packet packet = new Packet();
 * 	Ip4 ip4 = new Ip4();
 * 	Icmp4 icmp4 = new Icmp4();
 * 
 * 	while (pcap.nextEx(packet) == true) {
 * 		if (packet.hasHeader(ip4)) {
 * 			System.out.printf("IP: %s -> %s TTL: %d%n",
 * 					FormatUtils.ip(ip4.source()),
 * 					FormatUtils.ip(ip4.destination()),
 * 					ip4.ttl());
 * 
 * 			if (packet.hasHeader(icmp4)) {
 * 				System.out.printf("ICMP Type: %d Code: %d%n",
 * 						icmp4.type(), icmp4.code());
 * 			}
 * 		}
 * 	}
 * } catch (IOException e) {
 * 	e.printStackTrace();
 * }
 * }</pre>
 */
package com.slytechs.jnet.protocol.tcpip.ip;
