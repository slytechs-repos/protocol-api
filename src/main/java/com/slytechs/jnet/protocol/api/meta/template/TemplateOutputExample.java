/*
 * Sly Technologies Free License
 * 
 * Copyright 2025 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.api.meta.template;

import com.slytechs.jnet.protocol.api.meta.MetaFormatter;
import com.slytechs.jnet.protocol.api.meta.MetaPrinter;
import com.slytechs.jnet.protocol.api.meta.WiresharkFormatter;
import com.slytechs.jnet.protocol.api.meta.WiresharkPrinter;

/**
 * Example usage and helper class to demonstrate the formatting system.
 */
class TemplateOutputExample {
	public static void main(String[] args) {
		StringBuilder output = new StringBuilder();
		MetaFormatter formatter = new WiresharkFormatter();
		MetaPrinter printer = new WiresharkPrinter();
		OpenPredicate predicate = m -> true;

		try {
			// Frame Summary
			printer.printSummary(output, "Frame 1: 62 bytes on wire (496 bits), 62 bytes captured (496 bits)");

			// Frame Details
			printer.printField(output, formatter.formatField("Encapsulation type", "Ethernet (1)"), 1, true);
			printer.printField(output, formatter.formatField("Arrival Time", "Nov 19, 2004 17:29:14.159269000 EST"), 1, true);
			printer.printField(output, formatter.formatField("UTC Arrival Time", "Nov 19, 2004 22:29:14.159269000 UTC"),
					1, true);
			printer.printField(output, formatter.formatField("Epoch Arrival Time", "1100903354.159269000"), 1, true);
			printer.printField(output, formatter.formatField("Time shift for this packet", "0.000000000 seconds"), 1, true);
			printer.printField(output,
					formatter.formatField("Time delta from previous captured frame", "0.000000000 seconds"), 1, true);
			printer.printField(output,
					formatter.formatField("Time delta from previous displayed frame", "0.000000000 seconds"), 1, true);
			printer.printField(output, formatter.formatField("Time since reference or first frame", "0.000000000 seconds"),
					1, true);
			printer.printField(output, formatter.formatField("Frame Number", "1"), 1, true);
			printer.printField(output, formatter.formatField("Frame Length", "62 bytes (496 bits)"), 1, true);
			printer.printField(output, formatter.formatField("Capture Length", "62 bytes (496 bits)"), 1, true);
			printer.printField(output, formatter.formatField("Frame is marked", "False"), 1, true);
			printer.printField(output, formatter.formatField("Frame is ignored", "False"), 1, true);
			printer.printField(output, formatter.formatField("Protocols in frame", "eth:ethertype:ip:tcp"), 1, true);
			printer.printField(output, formatter.formatField("Coloring Rule Name", "HTTP"), 1, true);
			printer.printField(output, formatter.formatField("Coloring Rule String", "http || tcp.port == 80 || http2"),
					1, true);

			// Ethernet II
			printer.printBranch(
					output,
					"Ethernet II, Src: SMCNetworks_22:5a:03 (00:04:e2:22:5a:03), Dst: KYE_20:6c:df (00:c0:df:20:6c:df)", 0, true);
			printer.printField(output, formatter.formatField("Destination", "KYE_20:6c:df (00:c0:df:20:6c:df)"), 1, true);
			printer.printField(output, formatter.formatField("Source", "SMCNetworks_22:5a:03 (00:04:e2:22:5a:03)"), 1, true);
			printer.printField(output, formatter.formatField("Type", "IPv4 (0x0800)"), 1, true);

			// IPv4
			printer.printBranch(output, "Internet Protocol Version 4, Src: 10.1.1.101, Dst: 10.1.1.1", 0, true);
			printer.printField(output, formatter.formatField("Version", "4"), 1, true);
			printer.printField(output, formatter.formatField("Header Length", "20 bytes (5)"), 1, true);
			printer.printField(output,
					formatter.formatField("Differentiated Services Field", "0x00 (DSCP: CS0, ECN: Not-ECT)"), 1, true);
			printer.printField(output, formatter.formatField("Total Length", "48"), 1, true);
			printer.printField(output, formatter.formatField("Identification", "0xb305 (45829)"), 1, true);
			printer.printField(output, formatter.formatField("Flags", "0x2, Don't fragment"), 1, true);
			printer.printField(output, formatter.formatField("Fragment Offset", "0"), 1, true);
			printer.printField(output, formatter.formatField("Time to Live", "128"), 1, true);
			printer.printField(output, formatter.formatField("Protocol", "TCP (6)"), 1, true);
			printer.printField(output, formatter.formatField("Header Checksum", "0x315b [validation disabled]"), 1, true);
			printer.printField(output, formatter.formatField("Header checksum status", "Unverified"), 1, true);
			printer.printField(output, formatter.formatField("Source Address", "10.1.1.101"), 1, true);
			printer.printField(output, formatter.formatField("Destination Address", "10.1.1.1"), 1, true);

			// TCP
			printer.printBranch(output, "Transmission Control Protocol, Src Port: 3177, Dst Port: 80, Seq: 0, Len: 0", 0, true);
			printer.printField(output, formatter.formatField("Source Port", "3177"), 1, true);
			printer.printField(output, formatter.formatField("Destination Port", "80"), 1, true);
			printer.printField(output, formatter.formatField("Stream index", "0"), 1, true);
			printer.printField(output, formatter.formatField("Conversation completeness", "Complete, WITH_DATA (31)"), 1, true);
			printer.printField(output, formatter.formatField("TCP Segment Len", "0"), 1, true);
			printer.printField(output, formatter.formatField("Sequence Number", "0    (relative sequence number)"), 1, true);
			printer.printField(output, formatter.formatField("Sequence Number (raw)", "882639998"), 1, true);
			printer.printField(output, formatter.formatField("Next Sequence Number", "1    (relative sequence number)"),
					1, true);
			printer.printField(output, formatter.formatField("Acknowledgment Number", "0"), 1, true);
			printer.printField(output, formatter.formatField("Acknowledgment number (raw)", "0"), 1, true);
			printer.printField(output, formatter.formatField("Header Length", "28 bytes (7)"), 1, true);
			printer.printField(output, formatter.formatField("Flags", "0x002 (SYN)"), 1, true);
			printer.printField(output, formatter.formatField("Window", "0"), 1, true);
			printer.printField(output, formatter.formatField("Calculated window size", "0"), 1, true);
			printer.printField(output, formatter.formatField("Checksum", "0x26e5 [unverified]"), 1, true);
			printer.printField(output, formatter.formatField("Checksum Status", "Unverified"), 1, true);
			printer.printField(output, formatter.formatField("Urgent Pointer", "0"), 1, true);
			printer.printField(output, formatter.formatField("Options",
							"(8 bytes), Maximum segment size, No-Operation (NOP), No-Operation (NOP), SACK permitted"),
					1, true);
			printer.printField(output, formatter.formatField("Timestamps", ""), 1, true);

			// Print the captured output
			System.out.println(output.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}