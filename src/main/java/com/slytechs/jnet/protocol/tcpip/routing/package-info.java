/**
 * Routing protocols for network path determination.
 * <p>
 * Implements various interior and exterior routing protocols including OSPF,
 * BGP, RIP, EIGRP, and related protocols for path determination and route
 * exchange.
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
 * <td>OSPF</td>
 * <td>{@link Ospf}</td>
 * <td>Link state routing</td>
 * </tr>
 * <tr>
 * <td>BGP</td>
 * <td>{@link Bgp}</td>
 * <td>Internet routing</td>
 * </tr>
 * <tr>
 * <td>RIP</td>
 * <td>{@link Rip}</td>
 * <td>Distance vector routing</td>
 * </tr>
 * <tr>
 * <td>EIGRP</td>
 * <td>{@link Eigrp}</td>
 * <td>Advanced distance vector</td>
 * </tr>
 * <tr>
 * <td>IGRP</td>
 * <td>{@link Igrp}</td>
 * <td>Legacy interior routing</td>
 * </tr>
 * </table>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>{@code
 * try (NetPcap pcap = NetPcap.openOffline("routing.pcap")) {
 * 	Packet packet = new Packet();
 * 	Ospf ospf = new Ospf();
 * 	Bgp bgp = new Bgp();
 * 
 * 	while (pcap.nextEx(packet) == true) {
 * 		if (packet.hasHeader(ospf)) {
 * 			System.out.printf("OSPF Type: %d Area: %s%n",
 * 					ospf.type(), FormatUtils.ip(ospf.areaId()));
 * 
 * 			// Process LSAs for LSU packets
 * 			if (ospf.type() == OSPF_TYPE_LSU) {
 * 				OspfLsa lsa = new OspfLsa();
 * 				while (ospf.hasNextLsa(lsa)) {
 * 					System.out.printf("LSA Type: %d Age: %d%n",
 * 							lsa.type(), lsa.age());
 * 				}
 * 			}
 * 		} else if (packet.hasHeader(bgp)) {
 * 			System.out.printf("BGP Type: %d Length: %d%n",
 * 					bgp.type(), bgp.length());
 * 
 * 			// Handle BGP Update messages
 * 			if (bgp.type() == BGP_UPDATE) {
 * 				BgpPath path = new BgpPath();
 * 				while (bgp.hasNextPath(path)) {
 * 					System.out.printf("Path: AS%d%n",
 * 							path.asNumber());
 * 				}
 * 			}
 * 		}
 * 	}
 * } catch (IOException e) {
 * 	e.printStackTrace();
 * }
 * }</pre>
 */
package com.slytechs.jnet.protocol.tcpip.routing;
