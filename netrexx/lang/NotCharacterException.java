/* Generated from 'NotCharacterException.nrx' 11 Jun 2011 03:28:01 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.NotCharacterException                                 */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author: Mike Cowlishaw                                             */
/*                                                                    */
/* Must be compiled with binary defaults; must not use Rexx class     */
/* ------------------------------------------------------------------ */
/* 96.02.15 Initial version in NetRexx                                */



/**
 *  Signals that a character was expected but none (or more than
   one) were found.
 */

public class NotCharacterException extends java.lang.RuntimeException{
 private static final java.lang.String $0="NotCharacterException.nrx";

 /**
    Constructs a NotCharacterException with no detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public NotCharacterException(){
  super();
  return;}

 /**
    Constructs a NotCharacterException with a detail message.
    A detail message is a String that describes this particular exception.
  */
 
 public NotCharacterException(java.lang.String arg){
  super(arg);
  return;}
 }
