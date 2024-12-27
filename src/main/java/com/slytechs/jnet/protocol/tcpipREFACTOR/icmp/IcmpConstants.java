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
package com.slytechs.jnet.protocol.tcpipREFACTOR.icmp;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class IcmpConstants {

    /* ICMPv4 constants */
    public static final int ICMPv4_HEADER_LEN = 4;
    public static final int ICMPv4_FIELD_TYPE = 0;
    public static final int ICMPv4_FIELD_CODE = 1;
    public static final int ICMPv4_FIELD_CHECKSUM = 2;

    /* ICMPv4 types */
    public static final int ICMPv4_TYPE_ECHO_REQUEST = 8;
    public static final int ICMPv4_TYPE_ECHO_REPLY = 0;
    public static final int ICMP_TYPE_UNREACHABLE = 1;

    /* ICMPv4 Echo constants */
    public static final int ICMPv4_ECHO_HEADER_LEN = 8;
    public static final int ICMPv4_ECHO_FIELD_IDENTIFIER = 4;
    public static final int ICMPv4_ECHO_FIELD_SEQUENCE = 6;

    /* ICMPv6 constants */
    public static final int ICMPv6_HEADER_LEN = 4;
    
    /* ICMPv6 types */
    public static final int ICMPv6_TYPE_PACKET_TOO_BIG = 2;
    public static final int ICMPv6_TYPE_TIME_EXEEDED = 3;
    public static final int ICMPv6_TYPE_PARAMETER_PROBLEM = 4;
    public static final int ICMPv6_TYPE_ECHO_REQUEST = 128;
    public static final int ICMPv6_TYPE_ECHO_REPLY = 129;
    public static final int ICMPv6_TYPE_MULTICAST_LISTENER_QUERY = 130;
    public static final int ICMPv6_TYPE_MULTICAST_LISTENER_REPORT = 131;
    public static final int ICMPv6_TYPE_MULTICAST_LISTENER_DONE = 132;
    public static final int ICMPv6_TYPE_ROUTER_SOLICITATION = 133;
    public static final int ICMPv6_TYPE_ROUTER_ADVERTISEMENT = 134;
    public static final int ICMPv6_TYPE_NEIGHBOR_SOLICITATION = 135;
    public static final int ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT = 136;
    public static final int ICMPv6_TYPE_REDIRECT = 137;
    public static final int ICMPv6_TYPE_ROUTER_RENUMBER = 138;
    public static final int ICMPv6_TYPE_NODE_INFO_QUERY = 139;
    public static final int ICMPv6_TYPE_NODE_INFO_RESPONSE = 140;
    public static final int ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION = 141;
    public static final int ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT = 142;
    public static final int ICMPv6_TYPE_MULTICAST_LISTENER_REPORTv2 = 143;
    public static final int ICMPv6_TYPE_HOME_AGENT_REQUEST = 144;
    public static final int ICMPv6_TYPE_HOME_AGENT_REPLY = 145;
    public static final int ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION = 146;
    public static final int ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT = 147;

    private IcmpConstants() {
    }
}