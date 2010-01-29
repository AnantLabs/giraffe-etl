/*
   Copyright 2010 Computer and Automation Research Institute, Hungarian Academy of Sciences (SZTAKI)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.sztaki.ilab.giraffe.dataprocessors.implementation;
import hu.sztaki.ilab.giraffe.core.util.*;
import org.xbill.DNS.*;
import java.io.IOException;
import java.net.UnknownHostException;
import hu.sztaki.ilab.giraffe.core.util.StringUtils;

/**
 *
 * @author neumark
 */
public class DNSResolver {
    
    private Resolver res;
    
       public DNSResolver(String[] nameservers) throws java.net.UnknownHostException {
           if (nameservers.length == 0) res = new ExtendedResolver();
           else res = new ExtendedResolver(nameservers);
       }

       public String reverseDns(String hostIp) throws IOException {
             Record opt = null;             
             Name name = ReverseMap.fromAddress(hostIp);
             int type = Type.PTR;
             int dclass = DClass.IN;
             Record rec = Record.newRecord(name, type, dclass);
             Message query = Message.newQuery(rec);
             Message response = res.send(query);

             Record[] answers = response.getSectionArray(Section.ANSWER);
             if (answers.length == 0)
                return null;
             else
                return StringUtils.removeTrailingString(answers[0].rdataToString(),".");
       }
       
       public static void main(String args[]) throws IOException {
             long now = System.currentTimeMillis();
             //System.out.println(reverseDns("217.20.132.5"));
             long after = System.currentTimeMillis();
             System.out.println((after - now) + " ms");
       }
    }