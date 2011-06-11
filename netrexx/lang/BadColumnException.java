/* Generated from 'BadColumnException.nrx' 11 Jun 2011 03:27:58 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.BadColumnException                                    */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author: Mike Cowlishaw                                             */
/*                                                                    */
/* Must be compiled with binary defaults; must not use Rexx class     */
/* ------------------------------------------------------------------ */
/* 96.03.31 Initial version in NetRexx                                */



/**
   Signals that a bad column value was found during a Rexx parse
   operation.
 */

public class BadColumnException extends java.lang.RuntimeException{
 private static final java.lang.String $0="BadColumnException.nrx";

 /**
    Constructs a BadColumnException with no detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public BadColumnException(){
  super();
  return;}

 /**
    Constructs a BadColumnException with a detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public BadColumnException(java.lang.String arg){
  super(arg);
  return;}
 }
