/**
 * Basic link layer protocols for network connectivity.
 * <p>
 * Implements foundational link layer protocols including Ethernet, VLAN, PPP,
 * LLC, and ARP. These protocols handle the basic frame formatting and
 * addressing at Layer 2.
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
 * <td>Ethernet</td>
 * <td>{@link Ethernet}</td>
 * <td>Basic frame format</td>
 * </tr>
 * <tr>
 * <td>VLAN</td>
 * <td>{@link Vlan}</td>
 * <td>Virtual LAN tagging</td>
 * </tr>
 * <tr>
 * <td>PPP</td>
 * <td>{@link Ppp}</td>
 * <td>Point-to-point links</td>
 * </tr>
 * <tr>
 * <td>LLC</td>
 * <td>{@link Llc}</td>
 * <td>Logical link control</td>
 * </tr>
 * <tr>
 * <td>ARP</td>
 * <td>{@link Arp}</td>
 * <td>Address resolution</td>
 * </tr>
 * </table>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>{@code
 * try (NetPcap pcap = NetPcap.openOffline("capture.pcap")) {
 * 	Packet packet = new Packet();
 * 	Ethernet eth = new Ethernet();
 * 	Vlan vlan = new Vlan();
 * 
 * 	while (pcap.nextEx(packet) == true) {
 * 		// Process Ethernet frame
 * 		if (packet.hasHeader(eth)) {
 * 			System.out.printf("Ethernet: %s -> %s%n",
 * 					FormatUtils.mac(eth.source()),
 * 					FormatUtils.mac(eth.destination()));
 * 
 * 			// Check for VLAN tagging
 * 			if (packet.hasHeader(vlan)) {
 * 				System.out.printf("VLAN ID: %d Priority: %d%n",
 * 						vlan.id(), vlan.priority());
 * 			}
 * 		}
 * 	}
 * } catch (IOException e) {
 * 	e.printStackTrace();
 * }
 * }</pre>
 */
package com.slytechs.jnet.protocol.tcpipREFACTOR.ppp;

import com.slytechs.jnet.protocol.tcpipREFACTOR.arp.Arp;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.Ethernet;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.Llc;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.Vlan;
