/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.tcpip.arp;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class ArpConstants {

    public static final int ARP_HEADER_LEN = 28;
    public static final int ARP_FIELD_SHA = 8;
    public static final int ARP_FIELD_SPA = 14;
    public static final int ARP_FIELD_THA = 18;
    public static final int ARP_FIELD_TPA = 24;
    public static final int ARP_LEN_HALEN = 6;
    public static final int ARP_LEN_PALEN = 4;

    private ArpConstants() {
    }
}