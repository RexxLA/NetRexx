/* Generated from 'RexxIO.nrx' 11 Jun 2011 03:28:04 [v3.00] *//* Options: Binary Comments Compact Crossref Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */package netrexx.lang;/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.RexxIO -- I/O utilities for NetRexx                   */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author    Mike Cowlishaw                                           */
/*                                                                    */
/* ------------------------------------------------------------------ */
/* 96.04.03 Ask method (read a line)                                  */
/* 96.07.07 Say method (write a line or partial line)                 */
/* 97.02.01 Make I/O streams externally accessible [for IFB]          */
/*          [this may be a security exposure, removed for 1.1]        */
/* 97.06.28 Upgrade for 1.1 NLS                                       */
/* 98.08.30 Use PrintWriter for output                                */
/* ------------------------------------------------------------------ */




/**
   This defines the I/O utility class for NetRexx.
 */

public class RexxIO{private static final java.lang.String $0="RexxIO.nrx";

/* ----- Constants ----- */
/* properties private static */
private static java.io.BufferedReader StdIn=new java.io.BufferedReader((java.io.Reader)(new java.io.InputStreamReader(java.lang.System.in)));
private static java.io.PrintWriter StdOut=new java.io.PrintWriter((java.io.OutputStream)java.lang.System.out);

/** Function to return a line read from the standard input stream.
   If the stream is closed or otherwise unavailable, null is returned.
 */
public static netrexx.lang.Rexx Ask(){java.lang.String line=null;
{try{
line=StdIn.readLine();
if (line==null) return (netrexx.lang.Rexx)null;}
catch (java.io.IOException $1){
return (netrexx.lang.Rexx)null;
}}
return new netrexx.lang.Rexx(line);}

/** Function to return a character read from the standard input stream.
   If the stream is closed or otherwise unavailable, null is returned.
   [This still seems to require that Enter be pressed, in 1.1.1]
    */
public static netrexx.lang.Rexx AskOne(){char one=0;
{try{
one=(char)(StdIn.read());}
catch (java.io.IOException $2){
return (netrexx.lang.Rexx)null;
}}
return new netrexx.lang.Rexx(one);}

/** Function to write a line to the standard output stream.
   Arg1 is the Object or Rexx string to be written.
   If the line ends in the NUL character ('\-' or '\0') then no
     line termination is provided (and the NUL is deleted).
   If the write succeeds 0 is returned, otherwise 1 is returned.

   We handle all the well-known character forms directly to
     minimize code generation in caller; null is allowed for all
     objects.
   We also handle Object here, so null can be processed efficiently.
   We provide versions for all the other primitives, too.
    */
public static boolean Say(java.lang.Object obj){
if (obj==null) return Say((char[])null);
return Say(obj.toString().toCharArray());}

public static boolean Say(java.lang.String str){
if (str==null) return Say((char[])null);
return Say(str.toCharArray());}

public static boolean Say(netrexx.lang.Rexx line){
return Say(netrexx.lang.Rexx.tochararray(line));} // null passes through

public static boolean Say(char c){char ca[];
ca=new char[1];ca[0]=c;
return Say(ca);}

// numeric primitives
public static boolean Say(long n){ // handles byte, short, int
return Say(java.lang.Long.toString(n).toCharArray());}
public static boolean Say(float f){return Say(new netrexx.lang.Rexx(f));}
public static boolean Say(double d){return Say(new netrexx.lang.Rexx(d));}
public static boolean Say(boolean b){return Say(new netrexx.lang.Rexx(b));}

public static boolean Say(char aline[]){char bline[]=null;
if (aline==null) StdOut.println(); // just new line
else { // have some data
{/*select*/
if (aline.length==0)StdOut.println();
else if ((aline[aline.length-1])!=('\000'))StdOut.println(aline);
else{ // rarer continuation case
bline=new char[aline.length-1];
java.lang.System.arraycopy((java.lang.Object)aline,0,(java.lang.Object)bline,0,bline.length);
StdOut.print(bline);}
}
}
StdOut.flush(); // could be a real user out there
return false;}private RexxIO(){return;}}

