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
public final class Ip6Constants {

    /* Header constants */
    public static final int IPv6_HEADER_LEN = 40;
    public static final int IPv6_FIELD_NEXT_HOP = 6;
    public static final int IPv6_FIELD_SRC = 8;
    public static final int IPv6_FIELD_DST = 24;
    public static final int IPv6_FIELD_SRC_LEN = 16;
    public static final int IPv6_FIELD_DST_LEN = 16;

    /* Fragment constants */
    public static final int IPv6_FIELD_FRAG_OFFSET = 2;
    public static final int IPv6_FIELD_IDENTIFICATION = 4;
    public static final int IPv6_FLAG16_MF = 0x8000;
    public static final int IPv6_MASK16_FRAGOFF = 0x1FFF;

    /* Extension header types */
    public static final int IPv6_OPTION_TYPE_HOP_BY_HOP = 0;
    public static final int IPv6_OPTION_TYPE_FRAGMENT = 44;
    public static final int IPv6_OPTION_TYPE_DESTINATION = 60;
    public static final int IPv6_OPTION_TYPE_ROUTING = 43;
    public static final int IPv6_OPTION_TYPE_SECURITY = 50;
    public static final int IPv6_OPTION_TYPE_AUTHENTICATION = 51;
    public static final int IPv6_OPTION_TYPE_NO_NEXT = 59;
    public static final int IPv6_OPTION_TYPE_MOBILITY = 135;
    public static final int IPv6_OPTION_TYPE_IDENTITY = 139;
    public static final int IPv6_OPTION_TYPE_SHIMv6 = 140;
    
    /* Extension header sizes */
    public static final int IPv6_OPTION_TYPE_FRAGMENT_LEN = 8;

    private Ip6Constants() {
    }
}