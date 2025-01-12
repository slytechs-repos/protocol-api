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
package com.slytechs.jnet.protocol.api.meta.template;

import java.io.IOException;
import java.io.InputStream;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.DetailTemplate;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.FieldTemplate;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.Item;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.Template;

/**
 * Test class for validating template loading and parsing.
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class TemplateTest {

    private static void dumpItem(Item item, String indent) {
        if (item == null)
            return;
            
        System.out.printf("%sItem [%s]%n", indent, item.name());
        System.out.printf("%s  template: %s%n", indent, item.template());
        System.out.printf("%s  defaults: %s%n", indent, item.defaults());
        
        if (item.items() != null) {
            for (Item child : item.items()) {
                dumpItem(child, indent + "    ");
            }
        }
    }

    private static void dumpField(FieldTemplate field, String indent) {
        if (field == null)
            return;
            
        System.out.printf("%sField [%s]%n", indent, field.name());
        System.out.printf("%s  label: %s%n", indent, field.label());
        System.out.printf("%s  template: %s%n", indent, field.template());
        System.out.printf("%s  defaults: %s%n", indent, field.defaults());
        
        if (field.items() != null) {
            for (Item item : field.items()) {
                dumpItem(item, indent + "    ");
            }
        }
    }

    private static void dumpDetailTemplate(DetailTemplate dt, String indent) {
        if (dt == null)
            return;
            
        System.out.printf("%sDetail [%s]%n", indent, dt.detail());
        System.out.printf("%s  summary: %s%n", indent, dt.summary());
        System.out.printf("%s  defaults: %s%n", indent, dt.defaults());
        System.out.printf("%s  fields:%n", indent);
        
        for (FieldTemplate field : dt.fieldList()) {
            dumpField(field, indent + "    ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        InputStream is = TemplateReader.class.getResourceAsStream("/meta/tcpip/ip4.yaml");
        if (is == null) {
            System.err.println("Sample IP4 template not found in resources");
            return;
        }

        Template proto = TemplateReader.parseYamlTemplate(is);
        System.out.printf("Protocol [%s]%n", proto.name());
        System.out.printf("  Macros: %s%n", proto.macros());
        System.out.printf("  Defaults: %s%n%n", proto.defaults());

        for (Detail detail : Detail.values()) {
            DetailTemplate dt = proto.detail(detail);
            if (dt != null) {
                dumpDetailTemplate(dt, "");
            }
        }
    }
}