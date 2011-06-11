/* Generated from 'NoOtherwiseException.nrx' 11 Jun 2011 03:28:00 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.NoOtherwiseException                                  */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author: Mike Cowlishaw                                             */
/*                                                                    */
/* Must be compiled with binary defaults; must not use Rexx class     */
/* ------------------------------------------------------------------ */
/* 96.03.23 Initial version in NetRexx                                */



/**
   Signals that a select construct with no Otherwise was
   processed without any of the When clauses being successful.
 */

public class NoOtherwiseException extends java.lang.RuntimeException{
 private static final java.lang.String $0="NoOtherwiseException.nrx";

 /**
    Constructs a NoOtherwiseException with no detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public NoOtherwiseException(){
  super();
  return;}

 /**
    Constructs a NoOtherwiseException with a detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public NoOtherwiseException(java.lang.String arg){
  super(arg);
  return;}
 }
