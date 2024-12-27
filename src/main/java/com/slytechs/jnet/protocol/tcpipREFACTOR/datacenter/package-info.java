/**
 * Data center and network virtualization protocols.
 * <p>
 * Implements modern data center protocols including overlay networking, fabric
 * protocols, and network virtualization technologies.
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
 * <td>FabricPath</td>
 * <td>{@link FabricPath}</td>
 * <td>Cisco fabric</td>
 * </tr>
 * <tr>
 * <td>TRILL</td>
 * <td>{@link Trill}</td>
 * <td>Routing in L2</td>
 * </tr>
 * <tr>
 * <td>SPB</td>
 * <td>{@link Spb}</td>
 * <td>Shortest path bridging</td>
 * </tr>
 * <tr>
 * <td>VxLAN</td>
 * <td>{@link Vxlan}</td>
 * <td>Virtual extensible LAN</td>
 * </tr>
 * <tr>
 * <td>NVGRE</td>
 * <td>{@link Nvgre}</td>
 * <td>Network virtualization</td>
 * </tr>
 * <tr>
 * <td>GENEVE</td>
 * <td>{@link Geneve}</td>
 * <td>Network virtualization</td>
 * </tr>
 * </table>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>{@code
 * try (NetPcap pcap = NetPcap.openOffline("datacenter.pcap")) {
 *     Packet packet = new Packet();
 *     Vxlan vxlan = new Vxlan();
 *     Trill trill = new Trill();
 *     
 *     while (pcap.nextEx(packet) == true) {
 *         if (packet.hasHeader(vxlan)) {
 *             System.out.printf("VxLAN VNI: %d Flags: 0x%X%n",
 *                 vxlan.vni(), vxlan.flags());
 *                 
 *             // Check inner Ethernet frame
 *             Ethernet innerEth = new Ethernet();
 *             if (packet.hasHeader(innerEth)) {
 *                 System.out.printf("Inner FrameHeader: %s -> %s%n",
 *                     FormatUtils.mac(innerEth.source()),
 *                     FormatUtils.mac(innerEth.destination()));
 *             }
 *         } else if (packet.hasHeader(trill)) {
 *             System.out.printf("TRILL Ingress: %d Egress: %d%n",
 *                 trill.ingressNickname(),
 *                 trill.egressNickname());
 *             System.out.printf("Hop Count: %d Options: %b%n",
 *                 trill.hopCount(),
 *                 trill.hasOptions());
 *                 
 *             // Process TRILL options if present
 *             if (trill.hasOptions()) {
 *                 TrillOption opt = new TrillOption();
 *                 while (trill.hasNextOption(opt)) {
 *                     System.out.printf("Option Type: %d Size: %d%n",
 *                         opt.type(), opt.size());
 *                 }
 *             }
 *         }
 *     }
 * } catch (IOException e) {
 *     e.printStackTrace();
 * }
 * 
 * // Example showing VxLAN encapsulation analysis
 * private static void analyzeVxlanEncapsulation(Packet packet, Vxlan vxlan) {
 *     // Get outer headers
 *     Ip4 outerIp = new Ip4();
 *     Udp outerUdp = new Udp();
 *     
 *     if (packet.hasHeader(outerIp) && packet.hasHeader(outerUdp)) {
 *         System.out.printf("Outer: %s:%d -> %s:%d%n",
 *             FormatUtils.ip(outerIp.source()),
 *             outerUdp.source(),
 *             FormatUtils.ip(outerIp.destination()),
 *             outerUdp.destination());
 *     }
 *     
 *     // Get inner headers
 *     Ethernet innerEth = new Ethernet();
 *     Ip4 innerIp = new Ip4();
 *     
 *     if (packet.hasHeader(innerEth)) {
 *         System.out.printf("Inner MAC: %s -> %s%n",
 *             FormatUtils.mac(innerEth.source()),
 *             FormatUtils.mac(innerEth.destination()));
 *             
 *         if (packet.hasHeader(innerIp)) {
 *             System.out.printf("Inner IP: %s -> %s%n",
 *                 FormatUtils.ip(innerIp.source()),
 *                 FormatUtils.ip(innerIp.destination()));
 *         }
 *     }
 * }
 * }</pre>
 */
package com.slytechs.jnet.protocol.tcpipREFACTOR.datacenter;
