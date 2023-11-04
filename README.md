# Core Protocol Pack
Core Protocol pack consisting of many common protocols and runtime support for other modules.

## Optional additional modules
* [Web protocol pack][web-protocols] - TLS/SSL, Quic, etc...
* (several others under development) - Coming soon!
  - Telco/Carrier protocol pack
  - Voice/Video protocol pack - with various codecs and streaming protocols
  - Database protocol pack - database monitoring and quality assurance
  - System tables - auxilary module with support for ARP, ROUTING and other system tables


## How to use this module
This module, **core-protocols** is an extension module which provides protocol level support for one of the main public APIs. You need to setup your application using either [**jnetpcap-pro**][jnetpcap-pro] or **jNetWorks** (_Coming Soon!_) modules which provide the main APIs for building applications using any of the protocol packs.

## What's inside
The **core-protocols** module provides support for the following services:
- Runtime support for all of the modules using combination of public and private APIs
  - Private APIs are exported to other modules and provide common implementation features across all modules
  - A small public API is also exported for things like capture [`Timestamp`][timestamp-src] and [`TimestampUnit`][timestamp-unit-src] classes, etc...
- Raw packet dissection
  - Advanced packet descriptors store the results of the dissection process after packet capture
  - Information about the presence of each protocol header is recorded in [`PacketDescriptorType.TYPE2`][packet-descriptor-type-src]
- IP Fragmentation processing
  - IP fragment reassembly into full IP datagrams
  - IP fragment tracking regardless if reassembly is enabled
- A very efficient packet implementation using the [`Packet`][packet-src] class
  - Instrumentation using `MetaPacket` class, similar to java beans
- Packet formatters, various packet formatters for displaying packet state and fields
  - A pretty print formatter which dumps easy to read details about a packet and its headers
  - Coverters for XML, JSON and other output types
- A set of protocols which are considered **core** or common on most networks

### Protocol Table
Here is a table of all of the protocol definitions provided by this **core-protocols** module.

| Builtin | Layer2  | Layer3 | Layer4 | Layer7 |
|---------|---------|--------|--------|--------|
|[Payload][payload]  |[Ethernet][eth] |[IPv4][ip4]    |[TCP][tcp]     |[DHCP<sup>2</sup>][phase2]    |
|[Frame][frame]    |[LLC][llc] |[IPv6][ip6]    |[UDP][udp]     |
|         |[SNAP][snap]     |[IPX<sup>2</sup>][phase2]     |[SCTP<sup>2</sup>][phase2]    |
|         |[VLAN][vlan]      |[MPLS<sup>2</sup>][phase2]    |[ICMPv4][icmp4]
|         |[STP][stp]      |[IGMP<sup>2</sup>][phase2]    |[ICMPv6][icmp6]
|         |[ARP][arp] |        |        |
|         |[PPP<sup>2</sup>][phase2] |        |        |

[payload]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/Payload.java
[frame]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/Frame.java
[eth]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Ethernet.java
[arp]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Arp.java
[ip4]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Ip4.java
[ip6]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Ip6.java
[udp]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Udp.java
[tcp]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Tcp.java
[icmp4]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Icmp4.java
[icmp6]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Icmp6.java
[llc]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Llc.java
[snap]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Snap.java
[vlan]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Vlan.java
[stp]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Stp.java
[phase2]: https://github.com/slytechs-repos/core-protocols/pull/5
[jnetpcap]: https://github.com/slytechs-repos/jnetpcap
[jnetpcap-pro]: https://github.com/slytechs-repos/jnetpcap-pro
[timestamp-src]: https://github.com/slytechs-repos/core-protocols/blob/feature-ipf-processing/src/main/java/com/slytechs/protocol/runtime/time/Timestamp.java
[timestamp-unit-src]: https://github.com/slytechs-repos/core-protocols/blob/feature-ipf-processing/src/main/java/com/slytechs/protocol/runtime/time/TimestampUnit.java
[packet-descriptor-type-src]: https://github.com/slytechs-repos/core-protocols/blob/feature-ipf-processing/src/main/java/com/slytechs/protocol/pack/core/constants/PacketDescriptorType.java
[packet-src]: https://github.com/slytechs-repos/core-protocols/blob/feature-ipf-processing/src/main/java/com/slytechs/protocol/Packet.java
[web-protocols]: https://github.com/slytechs-repos/web-protocols

## Status and Updates
* May 5th, 2023 - Currently working IP fragmentation and reassembly. Check this [pull request #11](https://github.com/slytechs-repos/core-protocols/pull/11) for the latest on IPF support, including some examples!
