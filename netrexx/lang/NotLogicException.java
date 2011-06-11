/* Generated from 'NotLogicException.nrx' 11 Jun 2011 03:28:01 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.NotLogicException                                     */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author: Mike Cowlishaw                                             */
/*                                                                    */
/* Must be compiled with binary defaults; must not use Rexx class     */
/* ------------------------------------------------------------------ */
/* 96.02.20 Initial version in NetRexx                                */



/**
   Signals that a logic value ('0' or '1') was expected but some
   other character, a null string, or more than one character was found.
 */

public class NotLogicException extends java.lang.RuntimeException{
 private static final java.lang.String $0="NotLogicException.nrx";

 /**
    Constructs a NotLogicException with no detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public NotLogicException(){
  super();
  return;}

 /**
    Constructs a NotLogicException with a detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public NotLogicException(java.lang.String arg){
  super(arg);
  return;}
 }
