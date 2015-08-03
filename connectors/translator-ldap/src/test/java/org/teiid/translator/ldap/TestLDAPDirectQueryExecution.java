/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package org.teiid.translator.ldap;

import static org.junit.Assert.*;

import javax.naming.directory.BasicAttributes;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapContext;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.teiid.cdk.api.TranslationUtility;
import org.teiid.cdk.unittest.FakeTranslationFactory;
import org.teiid.language.Command;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.translator.Execution;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.TranslatorException;

@SuppressWarnings("nls")
public class TestLDAPDirectQueryExecution {

    private static LDAPExecutionFactory TRANSLATOR; 

    @BeforeClass
    public static void setUp() throws TranslatorException {
        TRANSLATOR = new LDAPExecutionFactory();
        TRANSLATOR.setSupportsDirectQueryProcedure(true);
        TRANSLATOR.start();
    }	
    
    @Test public void testSearch() throws Exception {
        String input = "exec native('search;context-name=corporate;filter=(objectClass=*);count-limit=5;timeout=6;search-scope=ONELEVEL_SCOPE;attributes=uid,cn')"; 

        TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        ExecutionContext ec = Mockito.mock(ExecutionContext.class);
        RuntimeMetadata rm = Mockito.mock(RuntimeMetadata.class);
        LdapContext connection = Mockito.mock(LdapContext.class);
        LdapContext ctx = Mockito.mock(LdapContext.class);
        Mockito.stub(connection.lookup("corporate")).toReturn(ctx);
        
        LDAPDirectSearchQueryExecution execution = (LDAPDirectSearchQueryExecution)TRANSLATOR.createExecution(command, ec, rm, connection);
        execution.execute();
        LDAPSearchDetails details = execution.getDelegate().getSearchDetails();
        
        assertEquals("corporate", details.getContextName());
        assertEquals("(objectClass=*)", details.getContextFilter());
        assertEquals(5, details.getCountLimit());
        assertEquals(6, details.getTimeLimit());
        assertEquals(1, details.getSearchScope());
        assertEquals(2, details.getElementList().size());
        assertEquals("uid", details.getElementList().get(0).getName());
        assertEquals("cn", details.getElementList().get(1).getName());
    }
    
    @Test public void testSearchDefaultsAndEscaping() throws Exception {
        String input = "exec native('search;context-name=corporate;filter=(;;)')"; 

        TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        ExecutionContext ec = Mockito.mock(ExecutionContext.class);
        RuntimeMetadata rm = Mockito.mock(RuntimeMetadata.class);
        LdapContext connection = Mockito.mock(LdapContext.class);
        LdapContext ctx = Mockito.mock(LdapContext.class);
        Mockito.stub(connection.lookup("corporate")).toReturn(ctx);
        
        LDAPDirectSearchQueryExecution execution = (LDAPDirectSearchQueryExecution)TRANSLATOR.createExecution(command, ec, rm, connection);
        execution.execute();
        LDAPSearchDetails details = execution.getDelegate().getSearchDetails();
        
        assertEquals("corporate", details.getContextName());
        assertEquals("(;)", details.getContextFilter());
        assertEquals(-1, details.getCountLimit());
        assertEquals(0, details.getTimeLimit());
        assertEquals(1, details.getSearchScope());
        assertEquals(0, details.getElementList().size());
    } 
    
    @Test(expected=TranslatorException.class) public void testWithoutMarker() throws Exception {
        String input = "exec native('context-name=corporate;filter=(objectClass=*);count-limit=5;timout=6;search-scope=ONELEVEL_SCOPE;attributes=uid,cn')"; 

        TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        ExecutionContext ec = Mockito.mock(ExecutionContext.class);
        RuntimeMetadata rm = Mockito.mock(RuntimeMetadata.class);
        LdapContext connection = Mockito.mock(LdapContext.class);
        LdapContext ctx = Mockito.mock(LdapContext.class);
        Mockito.stub(connection.lookup("corporate")).toReturn(ctx);
        
		Execution execution = TRANSLATOR.createExecution(command, ec, rm, connection);
		assertTrue(!(execution instanceof LDAPDirectSearchQueryExecution));
		execution.execute();
    }    
    
    @Test public void testDelete() throws Exception {
        String input = "exec native('delete;uid=doe,ou=people,o=teiid.org')"; 

        TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        ExecutionContext ec = Mockito.mock(ExecutionContext.class);
        RuntimeMetadata rm = Mockito.mock(RuntimeMetadata.class);
        LdapContext connection = Mockito.mock(LdapContext.class);
        LdapContext ctx = Mockito.mock(LdapContext.class);
        Mockito.stub(connection.lookup("")).toReturn(ctx);
        
        LDAPDirectCreateUpdateDeleteQueryExecution execution = (LDAPDirectCreateUpdateDeleteQueryExecution)TRANSLATOR.createExecution(command, ec, rm, connection);
		execution.execute();
		
		Mockito.verify(ctx, Mockito.times(1)).destroySubcontext("uid=doe,ou=people,o=teiid.org");
    }      
    
    @Test public void testUpdate() throws Exception {
        String input = "exec native('update;uid=doe,ou=people,o=teiid.org;attributes=one,two,three', 'one', 2, 3.0)"; 

        TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        ExecutionContext ec = Mockito.mock(ExecutionContext.class);
        RuntimeMetadata rm = Mockito.mock(RuntimeMetadata.class);
        LdapContext connection = Mockito.mock(LdapContext.class);
        LdapContext ctx = Mockito.mock(LdapContext.class);
        Mockito.stub(connection.lookup("")).toReturn(ctx);
        
        LDAPDirectCreateUpdateDeleteQueryExecution execution = (LDAPDirectCreateUpdateDeleteQueryExecution)TRANSLATOR.createExecution(command, ec, rm, connection);
		execution.execute();
		
		ArgumentCaptor<String> nameArgument = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<ModificationItem[]> modificationItemArgument = ArgumentCaptor.forClass(ModificationItem[].class);
		Mockito.verify(ctx).modifyAttributes(nameArgument.capture(),modificationItemArgument.capture());

		assertEquals("uid=doe,ou=people,o=teiid.org", nameArgument.getValue());
		assertEquals("one", modificationItemArgument.getValue()[0].getAttribute().getID());
		assertEquals("one", modificationItemArgument.getValue()[0].getAttribute().get());
		assertEquals("two", modificationItemArgument.getValue()[1].getAttribute().getID());
		assertEquals("2", modificationItemArgument.getValue()[1].getAttribute().get());
		assertEquals("three", modificationItemArgument.getValue()[2].getAttribute().getID());
		assertEquals("3.0", modificationItemArgument.getValue()[2].getAttribute().get());
    } 
    
    @Test public void testCreate() throws Exception {
        String input = "exec native('create;uid=doe,ou=people,o=teiid.org;attributes=one,two,three', 'one', 2, 3.0)"; 

        TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        ExecutionContext ec = Mockito.mock(ExecutionContext.class);
        RuntimeMetadata rm = Mockito.mock(RuntimeMetadata.class);
        LdapContext connection = Mockito.mock(LdapContext.class);
        LdapContext ctx = Mockito.mock(LdapContext.class);
        Mockito.stub(connection.lookup("")).toReturn(ctx);
        
        LDAPDirectCreateUpdateDeleteQueryExecution execution = (LDAPDirectCreateUpdateDeleteQueryExecution)TRANSLATOR.createExecution(command, ec, rm, connection);
		execution.execute();
		
		ArgumentCaptor<String> nameArgument = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<BasicAttributes> createItemArgument = ArgumentCaptor.forClass(BasicAttributes.class);
		Mockito.verify(ctx).createSubcontext(nameArgument.capture(), createItemArgument.capture());

		assertEquals("uid=doe,ou=people,o=teiid.org", nameArgument.getValue());
		assertEquals("one", createItemArgument.getValue().get("one").getID());
		assertEquals("one", createItemArgument.getValue().get("one").get());
		assertEquals("two", createItemArgument.getValue().get("two").getID());
		assertEquals("2", createItemArgument.getValue().get("two").get());
		assertEquals("three", createItemArgument.getValue().get("three").getID());
		assertEquals("3.0", createItemArgument.getValue().get("three").get());
    }
    
    @Test(expected=TranslatorException.class) public void testCreateFail() throws Exception {
        String input = "exec native('create;uid=doe,ou=people,o=teiid.org;attributes=one,two,three', 'one')"; 

        TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        ExecutionContext ec = Mockito.mock(ExecutionContext.class);
        RuntimeMetadata rm = Mockito.mock(RuntimeMetadata.class);
        LdapContext connection = Mockito.mock(LdapContext.class);
        LdapContext ctx = Mockito.mock(LdapContext.class);
        Mockito.stub(connection.lookup("")).toReturn(ctx);
        
		LDAPDirectCreateUpdateDeleteQueryExecution execution = (LDAPDirectCreateUpdateDeleteQueryExecution)TRANSLATOR.createExecution(command, ec, rm, connection);
		execution.execute();
    }    
}
