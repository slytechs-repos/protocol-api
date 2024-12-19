/**
 * Network redundancy and high-availability protocols.
 * <p>
 * Implements protocols for network redundancy, gateway failover, and
 * high-availability services including HSRP, VRRP, GLBP, and related protocols.
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
 * <td>HSRP</td>
 * <td>{@link Hsrp}</td>
 * <td>Router redundancy</td>
 * </tr>
 * <tr>
 * <td>VRRP</td>
 * <td>{@link Vrrp}</td>
 * <td>Virtual router redundancy</td>
 * </tr>
 * <tr>
 * <td>GLBP</td>
 * <td>{@link Glbp}</td>
 * <td>Gateway load balancing</td>
 * </tr>
 * <tr>
 * <td>UDLD</td>
 * <td>{@link Udld}</td>
 * <td>Unidirectional link detection</td>
 * </tr>
 * <tr>
 * <td>PIM</td>
 * <td>{@link Pim}</td>
 * <td>Multicast routing</td>
 * </tr>
 * </table>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>{@code
 * try (NetPcap pcap = NetPcap.openOffline("redundancy.pcap")) {
 * 	Packet packet = new Packet();
 * 	Hsrp hsrp = new Hsrp();
 * 	Vrrp vrrp = new Vrrp();
 * 
 * 	while (pcap.nextEx(packet) == true) {
 * 		if (packet.hasHeader(hsrp)) {
 * 			System.out.printf("HSRP Group: %d State: %d%n",
 * 					hsrp.group(), hsrp.state());
 * 			System.out.printf("Virtual IP: %s Priority: %d%n",
 * 					FormatUtils.ip(hsrp.virtualIp()),
 * 					hsrp.priority());
 * 
 * 			// Check for state changes
 * 			if (hsrp.state() == HSRP_STATE_ACTIVE) {
 * 				System.out.println("Router became Active");
 * 			}
 * 		} else if (packet.hasHeader(vrrp)) {
 * 			System.out.printf("VRRP Version: %d Virtual Router: %d%n",
 * 					vrrp.version(), vrrp.virtualRouterId());
 * 
 * 			// Process virtual IP addresses
 * 			byte[] vip = new byte[4];
 * 			for (int i = 0; i < vrrp.addressCount(); i++) {
 * 				vrrp.getVirtualAddress(i, vip);
 * 				System.out.printf("Virtual IP %d: %s%n",
 * 						i, FormatUtils.ip(vip));
 * 			}
 * 		}
 * 	}
 * } catch (IOException e) {
 * 	e.printStackTrace();
 * }
 * }</pre>
 */
package com.slytechs.jnet.protocol.tcpip.redundancy;
