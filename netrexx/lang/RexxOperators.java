/* Generated from 'RexxOperators.nrx' 11 Jun 2011 03:25:34 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.RexxOperators interface class                         */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author   Mike Cowlishaw                                            */
/* 96.01.30 Initial version                                           */
/* 96.02.26 NetRexx 0.20 .. settings passed to all operators          */
/*                                                                    */



/**
   This interface class defines the methods that must be implemented for a
class to fully participate as a primary class in a NetRexx program.
 */


public interface RexxOperators{
 static final java.lang.String $0="RexxOperators.nrx";

 /** The ' ' operator.  */
 
 public abstract netrexx.lang.Rexx OpCcblank(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '||' operator.  */
 
 public abstract netrexx.lang.Rexx OpCc(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 
 /** The '+' operator.  */
 
 public abstract netrexx.lang.Rexx OpAdd(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '-' operator.  */
 
 public abstract netrexx.lang.Rexx OpSub(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '*' operator.  */
 
 public abstract netrexx.lang.Rexx OpMult(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '/' operator.  */
 
 public abstract netrexx.lang.Rexx OpDiv(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '%' operator.  */
 
 public abstract netrexx.lang.Rexx OpDivI(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '//' operator.  */
 
 public abstract netrexx.lang.Rexx OpRem(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '**' operator.  */
 
 public abstract netrexx.lang.Rexx OpPow(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 
 /** The '=' operator.  */
 
 public abstract boolean OpEq(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '\=' operator.  */
 
 public abstract boolean OpNotEq(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '<' operator.  */
 
 public abstract boolean OpLt(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '>' operator.  */
 
 public abstract boolean OpGt(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '<=' operator.  */
 
 public abstract boolean OpLtEq(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '>=' operator.  */
 
 public abstract boolean OpGtEq(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 
 /** The '==' operator.  */
 
 public abstract boolean OpEqS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '\==' operator.  */
 
 public abstract boolean OpNotEqS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '<<' operator.  */
 
 public abstract boolean OpLtS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '>>' operator.  */
 
 public abstract boolean OpGtS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '<<=' operator.  */
 
 public abstract boolean OpLtEqS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '>>=' operator.  */
 
 public abstract boolean OpGtEqS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 
 /** The '|' operator.  */
 
 public abstract boolean OpOr(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '&' operator.  */
 
 public abstract boolean OpAnd(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 /** The '&&' operator.  */
 
 public abstract boolean OpXor(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs);

 
 /** The '\' prefix operator.  */
 
 public abstract boolean OpNot(netrexx.lang.RexxSet set);

 /** The '+' prefix operator.  */
 
 public abstract netrexx.lang.Rexx OpPlus(netrexx.lang.RexxSet set);

 /** The '-' prefix operator.  */
 
 public abstract netrexx.lang.Rexx OpMinus(netrexx.lang.RexxSet set);
 }
