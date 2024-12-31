/**
 * Bridge and spanning tree protocols for Layer 2 network control.
 * <p>
 * Implements various spanning tree protocol variants including STP, RSTP, MSTP,
 * and vendor-specific implementations like PVST.
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
 * <td>STP</td>
 * <td>{@link Stp}</td>
 * <td>Basic spanning tree</td>
 * </tr>
 * <tr>
 * <td>RSTP</td>
 * <td>{@link Rstp}</td>
 * <td>Rapid spanning tree</td>
 * </tr>
 * <tr>
 * <td>MSTP</td>
 * <td>{@link Mstp}</td>
 * <td>Multiple spanning tree</td>
 * </tr>
 * <tr>
 * <td>PVST</td>
 * <td>{@link Pvst}</td>
 * <td>Per-VLAN spanning tree</td>
 * </tr>
 * </table>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>{@code
 * try (NetPcap pcap = NetPcap.openOffline("bridge.pcap")) {
 * 	Packet packet = new Packet();
 * 	Stp stp = new Stp();
 * 	Mstp mstp = new Mstp();
 * 
 * 	while (pcap.nextEx(packet) == true) {
 * 		if (packet.hasHeader(stp)) {
 * 			System.out.printf("STP Root Bridge: %s%n",
 * 					FormatUtils.mac(stp.rootBridgeId()));
 * 			System.out.printf("Root Path Cost: %d%n",
 * 					stp.rootPathCost());
 * 		} else if (packet.hasHeader(mstp)) {
 * 			System.out.printf("MSTP Region: %s Instance: %d%n",
 * 					mstp.regionName(), mstp.mstInstance());
 * 		}
 * 	}
 * } catch (IOException e) {
 * 	e.printStackTrace();
 * }
 * }</pre>
 */
package com.slytechs.jnet.protocol.tcpip.stp;
