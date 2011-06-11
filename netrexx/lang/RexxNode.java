/* Generated from 'RexxNode.nrx' 11 Jun 2011 03:25:34 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.RexxNode -- node in a Rexx stem structure             */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1998.  All Rights Reserved.       */
/* Author    Mike Cowlishaw                                           */
/*                                                                    */
/* ------------------------------------------------------------------ */
/* Circularity: this class and the Rexx class interdepend.  To        */
/* bootstrap, add this class to Rexx.nrx, as private; compile both    */
/* together, then compile this and then the original Rexx.nrx         */
/* [1998 -- use NetRexx compile-together option]                      */
/* ------------------------------------------------------------------ */
/* Note:                                                              */
/*   -- this is just a data proxy; no function resides here.          */
/*   -- this is quite profligate on storage; first reference to an    */
/*      unknown node will allocate a new RexxNode and a new Rexx      */
/*      object                                                        */
/* ------------------------------------------------------------------ */
/* 96.03.10 Initial version (template)                                */
/* 96.03.15 First working version -- linear linked list               */
/* 96.03.23 Changed to a data proxy                                   */
/* 96.09.20 Save initial setting reference, so we can detect sets     */
/*          for the exists() test                                     */




/**
   This class defines a Rexx stem node; it is used to provide a
   reference to a Rexx object.
 */


public final class RexxNode implements java.io.Serializable{
 private static final java.lang.String $0="RexxNode.nrx";
 
 /* ----- Properties ----- */
 /* properties public */
 /** The data item owned by this node.  */
 public netrexx.lang.Rexx leaf;
 
 /** The data item when the node was constructed.  */
 public netrexx.lang.Rexx initleaf;

 
 /* ----- Constructors ----- */
 /** Make a Rexx node */
 
 public RexxNode(netrexx.lang.Rexx argleaf){super();
  leaf=argleaf;
  initleaf=argleaf;
  return;}
 }
