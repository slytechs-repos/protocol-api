/**
 * Link discovery and control protocols for network management.
 * <p>
 * Implements various discovery protocols (LLDP, CDP, FDP), link aggregation
 * protocols (LACP, PAgP), and VLAN management protocols (VTP, DTP).
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
 * <td>LLDP</td>
 * <td>{@link Lldp}</td>
 * <td>Link layer discovery</td>
 * </tr>
 * <tr>
 * <td>CDP</td>
 * <td>{@link Cdp}</td>
 * <td>Cisco discovery</td>
 * </tr>
 * <tr>
 * <td>LACP</td>
 * <td>{@link Lacp}</td>
 * <td>Link aggregation</td>
 * </tr>
 * <tr>
 * <td>VTP</td>
 * <td>{@link Vtp}</td>
 * <td>VLAN trunking</td>
 * </tr>
 * </table>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>{@code
 * try (NetPcap pcap = NetPcap.openOffline("discovery.pcap")) {
 * 	Packet packet = new Packet();
 * 	Lldp lldp = new Lldp();
 * 	Cdp cdp = new Cdp();
 * 
 * 	while (pcap.nextEx(packet) == true) {
 * 		if (packet.hasHeader(lldp)) {
 * 			// Process LLDP TLVs
 * 			LldpTlv tlv = new LldpTlv();
 * 			while (lldp.hasNextTlv(tlv)) {
 * 				switch (tlv.type()) {
 * 				case SYSTEM_NAME:
 * 					System.out.printf("System: %s%n",
 * 							tlv.valueAsString());
 * 					break;
 * 				case PORT_ID:
 * 					System.out.printf("Port: %s%n",
 * 							tlv.valueAsString());
 * 					break;
 * 				}
 * 			}
 * 		} else if (packet.hasHeader(cdp)) {
 * 			System.out.printf("CDP Device: %s Version: %s%n",
 * 					cdp.deviceId(), cdp.version());
 * 		}
 * 	}
 * } catch (IOException e) {
 * 	e.printStackTrace();
 * }
 * }</pre>
 */
package com.slytechs.jnet.protocol.tcpip.discovery;
