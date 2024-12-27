/**
 * Transport layer protocols for end-to-end communication.
 * <p>
 * Implements Layer 4 protocols including TCP, UDP, SCTP, and DCCP for reliable
 * and unreliable data transport.
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
 * <td>TCP</td>
 * <td>{@link Tcp}</td>
 * <td>Reliable transport</td>
 * </tr>
 * <tr>
 * <td>UDP</td>
 * <td>{@link Udp}</td>
 * <td>Datagram service</td>
 * </tr>
 * <tr>
 * <td>SCTP</td>
 * <td>{@link Sctp}</td>
 * <td>Stream control</td>
 * </tr>
 * <tr>
 * <td>DCCP</td>
 * <td>{@link Dccp}</td>
 * <td>Datagram control</td>
 * </tr>
 * </table>
 *
 * <h2>Example Usage:</h2>
 * 
 * <pre>{@code
 * try (NetPcap pcap = NetPcap.openOffline("transport.pcap")) {
 *     Packet packet = new Packet();
 *     Tcp tcp = new Tcp();
 *     Udp udp = new Udp();
 *     
 *     while (pcap.nextEx(packet) == true) {
 *         if (packet.hasHeader(tcp)) {
 *             System.out.printf("TCP %d -> %d [%s]%n",
 *                 tcp.source(), tcp.destination(),
 *                 getTcpFlags(tcp));
 *                 
 *             // Handle TCP options
 *             TcpOption opt = new TcpOption();
 *             while (tcp.hasNextOption(opt)) {
 *                 System.out.printf("Option %d: %d bytes%n",
 *                     opt.kind(), opt.length());
 *             }
 *         } else if (packet.hasHeader(udp)) {
 *             System.out.printf("UDP %d -> %d Length: %d%n",
 *                 udp.source(), udp.destination(),
 *                 udp.length());
 *         }
 *     }
 * } catch (IOException e) {
 *     e.printStackTrace();
 * }
 * 
 * private static String getTcpFlags(Tcp tcp) {
 *     StringBuilder flags = new StringBuilder();
 *     if (tcp.flags_SYN()) flags.append("SYN ");
 *     if (tcp.flags_ACK()) flags.append("ACK ");
 *     if (tcp.flags_FIN()) flags.append("FIN ");
 *     if (tcp.flags_RST()) flags.append("RST ");
 *     return flags.toString().trim();
 * }
 * }</pre>
 */
package com.slytechs.jnet.protocol.tcpipREFACTOR.tcp;

import com.slytechs.jnet.protocol.tcpipREFACTOR.udp.Udp;
