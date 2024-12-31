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
package com.slytechs.jnet.protocol.tcpip.tcp;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class TcpOptionConstants {

    /* Option field offsets */
    public static final int TCP_OPTION_FIELD_KIND = 0;
    public static final int TCP_OPTION_FIELD_LENGTH = 1;
    public static final int TCP_OPTION_FIELD_DATA = 2;

    /* Option kinds */
    public static final int TCP_OPTION_KIND_EOL = 0;
    public static final int TCP_OPTION_KIND_NOP = 1;
    public static final int TCP_OPTION_KIND_MSS = 2;
    public static final int TCP_OPTION_KIND_WIN_SCALE = 3;
    public static final int TCP_OPTION_KIND_SACK_PERMITTED = 4;
    public static final int TCP_OPTION_KIND_SACK = 5;
    public static final int TCP_OPTION_KIND_TIMESTAMP = 8;
    public static final int TCP_OPTION_KIND_FASTOPEN = 34;

    /* Option lengths */
    public static final int TCP_OPTION_LEN_MSS = 4;
    public static final int TCP_OPTION_LEN_TIMESTAMP = 10;
    public static final int TCP_OPTION_LEN_WIN_SCALE = 3;
    public static final int TCP_OPTION_LEN_FASTOPEN = 18;
    public static final int TCP_OPTION_LEN_NOP = 1;
    public static final int TCP_OPTION_LEN_EOL = 1;

    private TcpOptionConstants() {
    }
}