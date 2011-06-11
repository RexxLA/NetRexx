/* Generated from 'BadArgumentException.nrx' 11 Jun 2011 03:27:57 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.BadArgumentException                                  */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author: Mike Cowlishaw                                             */
/*                                                                    */
/* Must be compiled with binary defaults; must not use Rexx class     */
/* ------------------------------------------------------------------ */
/* 96.02.16 Initial version in NetRexx                                */



/**
   Signals that a bad argument was found in a Rexx method
   invocation.
 */

public class BadArgumentException extends java.lang.RuntimeException{
 private static final java.lang.String $0="BadArgumentException.nrx";

 /**
    Constructs a BadArgumentException with no detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public BadArgumentException(){
  super();
  return;}

 /**
    Constructs a BadArgumentException with a detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public BadArgumentException(java.lang.String arg){
  super(arg);
  return;}
 }
