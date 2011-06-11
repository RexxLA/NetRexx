/* Generated from 'RexxParse.nrx' 11 Jun 2011 03:25:35 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.RexxParse - Rexx parse function                       */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author    Mike Cowlishaw                                           */
/*                                                                    */
/* ------------------------------------------------------------------ */
/* 96.03.19 Initial version                                           */
/* 96.03.31 Full Rexx parsing                                         */



/**
   This defines 'helper functions' for the NetRexx PARSE instruction.

   The primary function here, parse, takes two arguments: an instruction
   array that is effectively a 'play list' for doing the parse, and an
   array of Rexx object references which are 'local variables' for the
   parse.  The latter may be both in and out.

   See Rexx, Object-Rexx, or ANSI specification for full semantics of
   Parse.

   A 'position' is origin 1; an 'offset' is origin 0.
 */

/* The instruction-list is driven by the following instructions:
 *
 *   EOP      End-of-parse
 *   EOS      Pattern to match end-of-string
 *   STRING   String pattern follows:
 *             I+1  =length of pattern, L
 *             I+2  =first character of string (if any)
 *             I+L-1=last character of string (if any)
 *   ABS      Absolute number
 *             I+1  =length of number, L (each character holds 0-255)
                     length may be 0 if the number is 0
 *             I+2  =high byte of number
 *             I+L-1=last byte of number
 *   PLUS     Relative number (+), following bytes as ABS
 *   MINUS    Relative number (-), following bytes as ABS
 *   VAR      Variable that contains string
 *             I+1  =variable offset (in vars array)
 *   VARABS   Absolute number from variable
 *             I+1  =variable offset (in vars array)
 *   VARPLUS  Relative number from variable (+), following byte as VARABS
 *   VARMINUS Relative number from variable (-), following byte as VARABS
 *   VARLIST  List of variables to be set from last parsed segment
 *             I+1  =number of variables in list, N (>=1)
 *             I+2  =first variable offset
 *             I+N-1=last variable offset
 *            Omitted if none to assign between targets
 */


public final class RexxParse{
 private static final java.lang.String $0="RexxParse.nrx";
 /* ----- Constants ----- */
 /* properties public constant */
 /* (Used by RxParse) */
 public static final char EOP='\000';
 public static final char EOS='\001';
 public static final char STRING='\002';
 public static final char ABS='\003';
 public static final char PLUS='\004';
 public static final char MINUS='\005';
 public static final char VAR='\006';
 public static final char VARABS='\007';
 public static final char VARPLUS='\010';
 public static final char VARMINUS='\011';
 public static final char VARLIST='\012';
 
 /* Minimum and maximum column number */
 public static final int MinCol=0;
 public static final int MaxCol=999999999;

 
 /* ----- Functions ----- */
 /** Parse a string into variables.
    <br>Arg1 is Rexx object to be parsed
    <br>Arg2 is parse instruction list (no length restriction)
    <br>Arg3 is list of Rexx object references (length 0-250)
  */
 
 public static void parse(netrexx.lang.Rexx obj,char list[],netrexx.lang.Rexx vars[]){
  char chars[];
  int onext;
  int omatch;
  int obeg=0;
  int oend;
  int ip;
  char ins;
  char needle[]=null;
  int slen=0;
  int i=0;
  int nextcol=0;
  int varcount=0;
  int v=0;
  int vbeg=0;
  int vend=0;
  int len=0;
  char value[]=null;
  /* ensure we have character version */
  chars=obj.toCharArray();/* get character array */
  onext=0;/* next offset into the character array */
  omatch=0;/* offset of last match start */
  /* offset into the character array */
  oend=0;/* offset of end of (sub)string to parse, +1 */
  ip=0;/* 'instruction pointer' */
  ins=list[0];/* first instruction */
  {instruction:for(;;){
   obeg=onext;/* ready for next pattern match */
   {code:do{/*select*/switch(ins){
    
   case STRING: case VAR:
    {
     if (ins==VAR) 
      {
       needle=vars[(int)(list[ip+1])].toCharArray();
       slen=needle.length;
       ip=ip+2;/* step over */
      }
     else 
      {
       slen=(int)(list[ip+1]);
       needle=new char[slen];
       ip=ip+2;/* -> first data */
       {int $1=slen-1;i=0;i:for(;i<=$1;i++){
        needle[i]=list[ip];
        ip++;
        }
       }/*i*/
      }
     /* now look for the needle */
     omatch=(netrexx.lang.RexxWords.pos(needle,chars,obeg+1))-1;
     if (omatch<0) 
      {/* not found */
       oend=chars.length;
       omatch=oend;
       onext=oend;
      }
     else 
      {
       oend=omatch;/* offset */
       onext=omatch+slen;
      }
    }break;
    
   case EOP:
    break instruction;
    
   case EOS:
    {
     oend=chars.length;
     omatch=oend;
     onext=oend;
     ip++;
    }break;
    
   case VARABS: case VARPLUS: case VARMINUS:
    {
     nextcol=vars[(int)(list[ip+1])].toint();
     if ((nextcol<MinCol)|(nextcol>MaxCol)) 
      throw new netrexx.lang.BadColumnException(netrexx.lang.Rexx.toString(vars[(int)(list[ip+1])]));
     ip=ip+2;/* step over */
     {/*select*/
     if (ins==VARABS)
      omatch=nextcol-1;
     else if (ins==VARPLUS)
      {
       obeg=omatch;
       omatch=omatch+nextcol;
      }
     else if (ins==VARMINUS)
      {
       obeg=omatch;
       omatch=omatch-nextcol;
      }
     
     else{throw new netrexx.lang.NoOtherwiseException();}
     }
     if (omatch<0) 
      omatch=0;
     oend=omatch;
     onext=oend;
     if (oend<=obeg) 
      oend=chars.length;/* wasn't forward */
    }break;
    
   default:{
    {/* ABS, PLUS, MINUS */
     nextcol=0;
     ip++;/* to count (may be 0) */
     {int $2=(int)(list[ip]);i=1;i:for(;i<=$2;i++){
      ip++;
      nextcol=(nextcol*256)+((int)(list[ip]));/* accumulate integer */
      }
     }/*i*/
     ip++;/* to next instruction */
     /* we have the column -- checked at compile time */
     {/*select*/
     if (ins==ABS)
      omatch=nextcol-1;
     else if (ins==PLUS)
      {
       obeg=omatch;
       omatch=omatch+nextcol;
      }
     else if (ins==MINUS)
      {
       obeg=omatch;
       omatch=omatch-nextcol;
      }
     
     else{throw new netrexx.lang.NoOtherwiseException();}
     }
     if (omatch<0) 
      omatch=0;
     oend=omatch;
     onext=oend;
     if (oend<=obeg) 
      oend=chars.length;/* wasn't forward */
    }
   }}
   }while(false);}/*code*/
   
   if (obeg>chars.length) 
    obeg=chars.length;/* clamp */
   if (oend>chars.length) 
    oend=chars.length;
   /* here, obeg is the offset of first character in string to parse */
   /*       oend is the offset of last character in string to parse, +1 */
   /*       ip   is offset of next instruction */
   ins=list[ip];
   if (ins!=VARLIST) 
    continue instruction;/* no vars to assign */
   /* here if vars to assign */
   varcount=(int)(list[ip+1]);
   ip=ip+2;
   
   /* say varcount 'variable(s) to assign' */
   {int $3=varcount;v=1;v:for(;v<=$3;v++){
    if (v==varcount) 
     {/* use whole remaining string */
      vbeg=obeg;
      vend=oend;
      obeg=oend;
     }
    else 
     {/* take word */
      {vbeg=obeg;vbeg:for(;;vbeg++){if(!(vbeg<oend))break;
       if (chars[vbeg]!=(' ')) 
        break vbeg;
       }
      }/*vbeg*/
      {vend=vbeg;vend:for(;;vend++){if(!(vend<oend))break;
       if (chars[vend]==(' ')) 
        break vend;
       }
      }/*vend*/
      if (vend<oend) 
       obeg=vend+1;/* don't reparse trailing blank */
      else 
       obeg=vend;
     }
    /* here, vbeg is start of variable's value */
    /*       vend is end+1 of variable's value */
    /*       obeg is start for next value */
    len=vend-vbeg;/* calculate length */
    /* say 'vbeg vend obeg len' vbeg vend obeg len */
    value=new char[len];/* make char array */
    {int $4=len-1;i=0;i:for(;i<=$4;i++){
     value[i]=chars[vbeg+i];
     }
    }/*i*//* copy */
    /* say 'Var['(int(list[ip]))']=|'value'|' */
    vars[(int)(list[ip])]=new netrexx.lang.Rexx(value);/* create and check object */
    /* [could use non-copy version,
       if it were public] */
    ip++;/* step to next slot */
    }
   }/*v*/
   
   ins=list[ip];/* prepare for next target */
   }
  }/*instruction*/
  /* drop out when done */
  return;
  }
 
 private RexxParse(){return;}
 }
