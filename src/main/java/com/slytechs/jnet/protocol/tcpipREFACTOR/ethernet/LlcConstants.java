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
public final class LlcConstants {

    public static final int LLC_HEADER_LEN = 3;
    public static final int LLC_FIELD_DSAP = 0;
    public static final int LLC_FIELD_SSAP = 1;
    public static final int LLC_FIELD_CONTROL = 2;
    public static final int LLC_TYPE_FRAME = 3;
    public static final int LLC_TYPE_SNAP = 0xAA;
    public static final int LLC_TYPE_NETWARE = 0xFF;
    public static final int LLC_TYPE_STP = 0x42;

    private LlcConstants() {
    }
}