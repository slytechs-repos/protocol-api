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
package com.slytechs.jnet.protocol.tcpip.ipx;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;

@MetaResource("ipx-meta.json")
public final class Ipx extends Header {

    public static final int ID = CoreId.CORE_ID_IPX;

    public Ipx() {
        super(ID);
    }

    @Meta(offset = 0, length = 2)
    public int checksum() {
        return Short.toUnsignedInt(buffer().getShort(0));
    }

    @Meta(offset = 2, length = 2)
    public int length() {
        return Short.toUnsignedInt(buffer().getShort(2));
    }

    @Meta(offset = 4, length = 1)
    public int transportControl() {
        return Byte.toUnsignedInt(buffer().get(4));
    }

    @Meta(offset = 5, length = 1)
    public int type() {
        return Byte.toUnsignedInt(buffer().get(5));
    }

    public IpxType typeEnum() {
        return IpxType.valueOfIpxType(type());
    }

    @Meta(offset = 6, length = 4)
    public int destinationNetwork() {
        return buffer().getInt(6);
    }

    @Meta(offset = 10, length = 6)
    public byte[] destinationNode() {
        byte[] node = new byte[6];
        buffer().get(10, node);
        return node;
    }

    @Meta(offset = 16, length = 2)
    public int destinationSocket() {
        return Short.toUnsignedInt(buffer().getShort(16));
    }

    @Meta(offset = 18, length = 4)
    public int sourceNetwork() {
        return buffer().getInt(18);
    }

    @Meta(offset = 22, length = 6)
    public byte[] sourceNode() {
        byte[] node = new byte[6];
        buffer().get(22, node);
        return node;
    }

    @Meta(offset = 28, length = 2)
    public int sourceSocket() {
        return Short.toUnsignedInt(buffer().getShort(28));
    }

    @Meta(offset = 6, length = 10)
    public IpxAddress destinationAddress() {
        byte[] addr = new byte[10];
        buffer().get(6, addr, 0, 4);  // network
        buffer().get(10, addr, 4, 6); // node
        return new IpxAddress(addr);
    }

    @Meta(offset = 18, length = 10)
   public IpxAddress sourceAddress() {
        byte[] addr = new byte[10];
        buffer().get(18, addr, 0, 4);  // network
        buffer().get(22, addr, 4, 6);  // node
        return new IpxAddress(addr);
    }
}