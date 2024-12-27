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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class Ip4Constants {

    /* Header constants */
    public static final int IPv4_HEADER_LEN = 20;
    public static final int IPv4_HEADER_MAX_LEN = 60;
    public static final int IPv4_FIELD_VER = 0;
    public static final int IPv4_FIELD_PROTOCOL = 9;
    public static final int IPv4_FIELD_DST_LEN = 4;
    public static final int IPv4_FIELD_SRC = 12;
    public static final int IPv4_FIELD_DST = 16;
    public static final int IPv4_FIELD_SRC_LEN = 4;
    public static final int IPv4_FIELD_FLAGS = 6;
    public static final int IPv4_FIELD_IDENT = 4;
    public static final int IPv4_FIELD_TOTAL_LEN = 2;

    /* Fragmentation constants */
    public static final int IPv4_MASK16_FRAGOFF = 0x1FFF;
    public static final int IPv4_FLAG16_DF = 0x4000;
    public static final int IPv4_FLAG16_MF = 0x2000;

    /* Options */
    public static final int IPv4_OPTION_TYPE_EOOL = 0x00;
    public static final int IPv4_OPTION_TYPE_NOP = 0x01;
    public static final int IPv4_OPTION_TYPE_SEC_DEF = 0x02;
    public static final int IPv4_OPTION_TYPE_RR = 0x07;
    public static final int IPv4_OPTION_TYPE_MTUP = 0x0B;
    public static final int IPv4_OPTION_TYPE_MTUR = 0x0C;
    public static final int IPv4_OPTION_TYPE_ENCODE = 0x0F;
    public static final int IPv4_OPTION_TYPE_QS = 0x19;
    public static final int IPv4_OPTION_TYPE_TS = 0x44;
    public static final int IPv4_OPTION_TYPE_TR = 0x52;
    public static final int IPv4_OPTION_TYPE_SEC = 0x82;
    public static final int IPv4_OPTION_TYPE_RTRALT = 0x94;

    private Ip4Constants() {
    }
}