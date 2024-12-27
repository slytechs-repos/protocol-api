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
public final class IpConstants {

    /* Common IP Protocol Types */
    public static final int IP_TYPE_ICMPv4 = 1;
    public static final int IP_TYPE_IPv4_IN_IP = 4;
    public static final int IP_TYPE_TCP = 6;
    public static final int IP_TYPE_UDP = 17;
    public static final int IP_TYPE_IPv6_IN_IP = 41;
    public static final int IP_TYPE_GRE = 47;
    public static final int IP_TYPE_SCTP = 132;

    /* IPv6 Special Protocol Types */
    public static final int IP_TYPE_IPv6_HOP_BY_HOP = 0;
    public static final int IP_TYPE_IPv6_ROUTING_HEADER = 43;
    public static final int IP_TYPE_IPv6_FRAGMENT_HEADER = 44;
    public static final int IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD = 50;
    public static final int IP_TYPE_IPv6_AUTHENTICATION_HEADER = 51;
    public static final int IP_TYPE_ICMPv6 = 58;
    public static final int IP_TYPE_NO_NEXT = 59;
    public static final int IP_TYPE_IPv6_DESTINATION_OPTIONS = 60;
    public static final int IP_TYPE_IPv6_MOBILITY_HEADER = 135;
    public static final int IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL = 139;
    public static final int IP_TYPE_IPv6_SHIM6_PROTOCOL = 140;

    private IpConstants() {
    }
}