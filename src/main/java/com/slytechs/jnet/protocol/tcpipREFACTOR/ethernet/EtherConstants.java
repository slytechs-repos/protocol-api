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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class EtherConstants {

    public static final int ETHER_HEADER_LEN = 14;
    public static final int ETHER_FIELD_TYPE = 12;
    public static final int ETHER_FIELD_DST = 0;
    public static final int ETHER_FIELD_SRC = 6;
    public static final int ETHER_FIELD_PREAMBLE = 0;
    public static final int ETHER_FIELD_LEN_PREAMBLE = 8;
    public static final int ETHER_FIELD_LEN_CRC = 4;
    public static final int ETHER_FIELD_DST_LEN = 6;
    public static final int ETHER_FIELD_SRC_LEN = 6;
    public static final int ETHER_FIELD_LEN_TYPE = 2;
    public static final int ETHER_MIN_VALUE_FOR_TYPE = 0x600;
    
    /* LLC constants */
    public static final int LLC_HEADER_LEN = 3;
    public static final int LLC_FIELD_DSAP = 0;
    public static final int LLC_FIELD_SSAP = 1;
    public static final int LLC_FIELD_CONTROL = 2;
    public static final int LLC_TYPE_FRAME = 3;
    public static final int LLC_TYPE_SNAP = 0xAA;
    public static final int LLC_TYPE_NETWARE = 0xFF;
    public static final int LLC_TYPE_STP = 0x42;
    
    /* SNAP constants */
    public static final int SNAP_HEADER_LEN = 5;
    public static final int SNAP_FIELD_TYPE = 3;
    public static final int SNAP_FIELD_OUI = 0;
    public static final int SNAP_TYPE_ETHER = 4;

    private EtherConstants() {
    }
}