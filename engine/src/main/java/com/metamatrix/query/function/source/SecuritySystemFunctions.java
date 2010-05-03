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

package com.metamatrix.query.function.source;

import com.metamatrix.api.exception.MetaMatrixComponentException;
import com.metamatrix.api.exception.query.FunctionExecutionException;
import com.metamatrix.query.eval.SecurityFunctionEvaluator;
import com.metamatrix.query.util.CommandContext;


public class SecuritySystemFunctions {

    public static boolean hasRole(CommandContext context, Object roleName) throws FunctionExecutionException {
        SecurityFunctionEvaluator eval = context.getSecurityFunctionEvaluator();
        
        if (eval == null) {
            return true;
        }
        
        try {
            return eval.hasRole(SecurityFunctionEvaluator.DATA_ROLE, (String)roleName);
        } catch (MetaMatrixComponentException err) {
            throw new FunctionExecutionException(err, err.getMessage());
        }
    }
	
    public static boolean hasRole(CommandContext context, Object roleType, Object roleName) throws FunctionExecutionException {
        
        SecurityFunctionEvaluator eval = context.getSecurityFunctionEvaluator();
        
        if (eval == null) {
            return true;
        }
        
        try {
            return eval.hasRole((String)roleType, (String)roleName);
        } catch (MetaMatrixComponentException err) {
            throw new FunctionExecutionException(err, err.getMessage());
        }
    }
    
    
}
