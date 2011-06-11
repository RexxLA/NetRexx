/* Generated from 'RexxSet.nrx' 11 Jun 2011 03:25:35 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.RexxSet -- Rexx context settings                      */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author    Mike Cowlishaw                                           */
/*                                                                    */
/* ------------------------------------------------------------------ */
/* 96.02.23 Initial version                                           */
/* 97.07.18 Add setDigits and setForm for checking                    */



/**
   This defines settings to be used by the Rexx class for NetRexx.  A
   RexxSet object is passed to operation methods in the Rexx class,
   along with the usual arguments, in order to influence the operations
   in various ways -- for example, the precision of arithmetic, and
   other variable settings.

   Note that null can be passed for 'all default' settings.

   The properties are public (so they remain accessible to NetRexx 1.0
   programs), but should only be changed through the setXxxxx methods.
   Later they could become shared.

    */


public final class RexxSet{
 private static final netrexx.lang.Rexx $01=new netrexx.lang.Rexx('1');
 private static final java.lang.String $0="RexxSet.nrx";
 
 /* ----- Properties ----- */
 /* properties public constant */
 public static final byte SCIENTIFIC=0;
 public static final byte ENGINEERING=1;
 public static final byte PLAIN=2; // only used internally
 public static final byte DEFAULT_FORM=SCIENTIFIC;
 public static final int DEFAULT_DIGITS=9;
 
 /* properties public */
 /** The DIGITS setting  */
 public int digits=DEFAULT_DIGITS;
 /** The FORM setting  */
 public byte form=DEFAULT_FORM;

 
 /* ----- Constructors ----- */
 /** Build a default Rexx settings context  */
 
 public RexxSet(){
  super();
  return;
  }

 /** Build a Rexx settings context with given DIGITS  */
 
 public RexxSet(int newdigits){
  this();
  digits=newdigits;
  return;
  }

 /** Build a Rexx settings context with given DIGITS and FORM
      */
 
 public RexxSet(int newdigits,byte newform){
  this();
  digits=newdigits;
  form=newform;
  return;
  }

 /** Clone a Rexx settings context  */
 
 public RexxSet(netrexx.lang.RexxSet old){
  this();
  digits=old.digits;
  form=old.form;
  return;
  }

 /** Check and set digits */
 
 public void setDigits(netrexx.lang.Rexx d){
  netrexx.lang.Rexx r;
  r=d.OpPlus(this); // round under current setting
  if (r.ind==netrexx.lang.Rexx.ispos)  // must be >0
   if ((r.datatype(new netrexx.lang.Rexx('w'))).OpEqS(null,$01))  // .. and whole
    if ((r.mant.length+r.exp)<=9) 
     { // .. and not too big
      digits=r.toint();
      return;
     }
  throw new netrexx.lang.BadNumericException(netrexx.lang.Rexx.toString(d)); // not good
  }

 /** Check and set form */
 
 public void setForm(netrexx.lang.Rexx f){
  {/*select*/
  if (f.OpEq((netrexx.lang.RexxSet)null,netrexx.lang.Rexx.toRexx("engineering")))
   form=ENGINEERING;
  else if (f.OpEq((netrexx.lang.RexxSet)null,netrexx.lang.Rexx.toRexx("scientific")))
   form=SCIENTIFIC;
  else{
   throw new netrexx.lang.BadNumericException(netrexx.lang.Rexx.toString(f)); // unexpected
  }
  }
  return;}

 /** Return a word for the form setting */
 
 public netrexx.lang.Rexx formword(){
  if (form==SCIENTIFIC) 
   return netrexx.lang.Rexx.toRexx("scientific");
  return netrexx.lang.Rexx.toRexx("engineering");
  }
 }
