/* Generated from 'RexxUtil.nrx' 11 Jun 2011 03:09:47 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.RexxUtil - Rexx class miscellaneous helpers           */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1998.  All Rights Reserved.       */
/* Author    Mike Cowlishaw                                           */
/*                                                                    */
/* ------------------------------------------------------------------ */
/* 96.10.17 Initial version (translate); Keystone                     */
/* 96.10.18 d2x                                                       */
/* 96.10.20 x2d, format stub                                          */
/* 96.11.16 doublePow and floatPow                                    */
/* 97.07.18 x2b, x2c, and hexint moved from Rexx.nrx                  */
/* 98.12.20 format revised to match BigDecimal algorithm (=ANSI)      */



/**
   This defines 'helper functions' and 'helper methods' for the Rexx
   class for NetRexx.  The functions here are the miscellaneous helpers,
   and return either char[] or primitives.
   
   See Rexx, Object-Rexx, or ANSI specification for full descriptions.
   
   Argument checking (type and range) is assumed done by caller.
   Currently, all arguments should always be supplied.  Option letters
   should be a single character, uppercase.  Arguments are never
   altered.
   
   A 'position' is origin 1; an 'offset' is origin 0.
 */


public final class RexxUtil extends netrexx.lang.Rexx{
 private static final netrexx.lang.Rexx $01=netrexx.lang.Rexx.toRexx("Too big");
 private static final java.lang.String $0="RexxUtil.nrx";
 /* ----- Constants ----- */
 /* properties constant private */
 private static final netrexx.lang.Rexx zero=new netrexx.lang.Rexx(0);
 private static final netrexx.lang.Rexx one=new netrexx.lang.Rexx(1);
 private static final netrexx.lang.Rexx sixteen=new netrexx.lang.Rexx(16);

 
 /* ----- Functions ----- */
 /** Translate.
    <br>Arg1 is string to translate
    <br>Arg2 is output table
    <br>Arg3 is input table
    <br>Arg4 is pad character
     */
 
 public static final char[] translate(char s[],char out[],char in[],char pad){
  int maxc;
  int i=0;
  char tran[];
  char use=0;
  char res[];
  int n=0;
  if (in.length==0) 
   return s; // none to translate
  
  /* First find out how large a translate table we'll need */
  maxc=-1;
  {int $1=in.length-1;i=0;i:for(;i<=$1;i++){
   if (((int)(in[i]))>maxc) 
    maxc=(int)(in[i]);
   }
  }/*i*/
  tran=new char[maxc+1];
  /* apply the identities through the table */
  {int $2=maxc;i=0;i:for(;i<=$2;i++){
   tran[i]=(char)(i);
   }
  }/*i*/
  /* fill in the translates; the input table is traversed
     right-to-left so the leftmost wins */
  {i=in.length-1;i:for(;i>=0;i--){
   if (i<out.length) 
    use=out[i];
   else 
    use=pad;
   tran[(int)(in[i])]=use;
   }
  }/*i*/
  /* Now create the result array and translate into it */
  res=new char[s.length];
  {int $3=s.length-1;i=0;i:for(;i<=$3;i++){
   n=(int)(s[i]);
   if (n>maxc) 
    res[i]=(char)(n);
   else 
    res[i]=tran[n];
   }
  }/*i*/
  return res;
  }

 /** D2x.
    <br>Arg1 is number to convert.
    <br>Arg2 is N (-1 if not specified)
    <br>returns char[]
     */
 
 public static final char[] d2x(netrexx.lang.Rexx d,int n){
  netrexx.lang.RexxSet set=null;
  netrexx.lang.Rexx work=null;
  boolean neg=false;
  char fill=0;
  int need=0;
  char res[];
  int i=0;
  int rem=0;
  int j=0;
  int newlen=0;
  char newres[]=null;
  if (d.ind==netrexx.lang.Rexx.NotaNum) 
   throw new java.lang.NumberFormatException(netrexx.lang.Rexx.toString(d));
  if (d.mant.length>netrexx.lang.Rexx.DefaultDigits) 
   set=new netrexx.lang.RexxSet(d.mant.length);
  else 
   set=(netrexx.lang.RexxSet)null;
  /* Negative only allowed if n is given */
  if (d.ind==netrexx.lang.Rexx.isneg) 
   {
    if (n<0) 
     throw new netrexx.lang.BadArgumentException(netrexx.lang.Rexx.toString(d));
    work=d.OpMinus(set).OpSub(set,one);
    neg=true;
    fill='F';
   }
  else 
   {
    work=d;
    neg=false;
    fill='0';
   }
  if (n<0) 
   need=work.mant.length; // probably too long
  else 
   need=n;
  /* We do this the 'heavy' way for now */
  res=new char[need];
  {i=res.length-1;i:for(;i>=0;i--){ // from the right
   rem=(work.OpRem(set,sixteen)).toint(); // this//16
   work=work.OpDivI(set,sixteen); // this%16
   if (neg) 
    res[i]=netrexx.lang.Rexx.Hexes[15-rem];
   else 
    res[i]=netrexx.lang.Rexx.Hexes[rem];
   if (work.ind==netrexx.lang.Rexx.iszero) 
    {
     // if n given, pad with 0's or 'F's and leave; otherwise shorten
     // and return
     if (n>=0) 
      {
       {j=i-1;j:for(;j>=0;j--){
        res[j]=fill;
        }
       }/*j*/
       break i;
      }
     if (i==0) 
      return res; // exact fit
     // need to shorten
     newlen=res.length-i;
     newres=new char[newlen];
     java.lang.System.arraycopy((java.lang.Object)res,i,(java.lang.Object)newres,0,newlen);
     return newres;
    }
   }
  }/*i*/
  /* get here iff n was given */
  return res; // all done
  }

 /** X2b hexadecimal to binary conversion.
    <br>Arg1 is hexstring to convert.
    <br>returns char[]
     */
 
 public static final char[] x2b(netrexx.lang.Rexx x){
  char res[];
  int i=0;
  int j=0;
  int k=0;
  res=new char[x.chars.length*4];
  {int $4=x.chars.length;i=0;i:for(;$4>0;$4--,i++){
   j=hexint(x.chars[i]);
   if (j<0) 
    throw new netrexx.lang.BadArgumentException(netrexx.lang.Rexx.toString(netrexx.lang.Rexx.toRexx("Bad hexadecimal").OpCcblank(null,x)));
   k=i*4;
   if (j>7) 
    res[k]='1';
   else 
    res[k]='0';
   if (((j&4))!=0) 
    res[k+1]='1';
   else 
    res[k+1]='0';
   if (((j&2))!=0) 
    res[k+2]='1';
   else 
    res[k+2]='0';
   if (((j&1))!=0) 
    res[k+3]='1';
   else 
    res[k+3]='0';
   }
  }/*i*/
  return res;
  }

 /** X2c hexadecimal to coded conversion.
    <br>Arg1 is hexstring to convert.
    <br>returns char
     */
 
 public static final char x2c(netrexx.lang.Rexx x){
  int i=0;
  int acc;
  int j=0;
  /* Skip insignificant leading zeros then convert the remainder */
  {int $5=x.chars.length-2;i=0;i:for(;i<=$5;i++){
   if (x.chars[i]!='0') 
    break i;
   }
  }/*i*/
  acc=0;
  {int $6=x.chars.length-1;i:for(;i<=$6;i++){
   j=hexint(x.chars[i]);
   if (j<0) 
    throw new netrexx.lang.BadArgumentException(netrexx.lang.Rexx.toString(netrexx.lang.Rexx.toRexx("Bad hexadecimal").OpCcblank(null,x)));
   acc=(acc*16)+j;
   if (acc>65535) 
    throw new netrexx.lang.BadArgumentException(netrexx.lang.Rexx.toString($01.OpCcblank(null,x)));
   }
  }/*i*/
  return (char)(acc);
  }

 /** X2d.
    <br>Arg1 is hexstring to convert.
    <br>Arg2 is N (-1 if not specified)
    <br>returns char[]
     */
 
 public static final char[] x2d(netrexx.lang.Rexx x,int n){
  boolean neg;
  int start=0;
  int digs;
  netrexx.lang.RexxSet set=null;
  netrexx.lang.Rexx work;
  int i=0;
  int nibble=0;
  /* see if we're a negative, and decide where to start */
  neg=false; // assume non-negative
  {/*select*/
  if ((n==0)|(x.chars.length==0))
   return "0".toCharArray();
  else if (n<0)
   start=0;
   /* n>0 */
  else if (n>x.chars.length)
   start=0;
  else{ // n is in string
   start=x.chars.length-n;
   if ((x.chars[start]>'7')|(x.chars[start]<'0')) 
    neg=true;
  } // baddies caught later
  }
  /* determine a safe DIGITS */
  digs=((x.chars.length*5)/4)+1;
  if (digs>netrexx.lang.Rexx.DefaultDigits) 
   set=new netrexx.lang.RexxSet(digs);
  else 
   set=(netrexx.lang.RexxSet)null;
  work=new netrexx.lang.Rexx(0); // accumulator
  {int $7=x.chars.length-1;i=start;i:for(;i<=$7;i++){ // from the left
   nibble=hexint(x.chars[i]);
   if (nibble<0) 
    throw new netrexx.lang.BadArgumentException(netrexx.lang.Rexx.toString(netrexx.lang.Rexx.toRexx("Bad hexadecimal").OpCcblank(null,x)));
   if (neg) 
    nibble=15-nibble;
   work=work.OpMult(set,sixteen).OpAdd(set,new netrexx.lang.Rexx(nibble));
   }
  }/*i*/
  if (neg) 
   work=work.OpAdd(set,one).OpMinus(set);
  return netrexx.lang.Rexx.tochararray(work);
  }

 /** doublePow -- the primitives' power operator
    <br>Arg1 is number raise to power
    <br>Arg2 is int power to raise to
    <br>returns result as a double
    <br>Will raise overflow if infinity is the result
     */
 
 public static final double doublePow(double x,int n){
  boolean neg=false;
  double acc;
  int i=0;
  /* Precision is 'best can do' -- i.e., double */
  if (n==0) 
   return (double)((byte)1);/* x**0 == 1 */
  if (n>0) 
   neg=false;/* not negative */
  else 
   {/* <0 */
    n=(int)-n;/* drop sign */
    neg=true;/* but remember it */
   }
  /* Not worth tracking top-bit-seen here; square of 1 is cheap */
  acc=(double)1;/* accumulator */
  {i=1;i:for(;;i++){/* for each bit [top bit ignored] */
   n=n+n;/* shift left 1 bit */
   if (n<0) 
    acc=acc*x;/* top bit is set */
   if (i==31) 
    break i;/* that was the last bit */
   acc=acc*acc;/* square */
   }
  }/*i*//* 32 bits */
  if (java.lang.Double.isInfinite(acc)) 
   throw new java.lang.NumberFormatException("Overflow");
  if (neg) 
   return ((double)1)/acc;/* was a **-n */
  return acc;
  }

 /** floatPow -- the primitives' power operator
    <br>Arg1 is number raise to power
    <br>Arg2 is int power to raise to
    <br>returns result as a float
    <br>Will raise overflow if infinity is the result
     */
 
 public static final float floatPow(double x,int n){
  float res;
  res=(float)(doublePow(x,n));
  if (java.lang.Float.isInfinite(res)) 
   throw new java.lang.NumberFormatException("Overflow");
  return res;
  }

 /** doubleToRexx -- convert a double to a Rexx string
    <br>Arg1 is number to convert
    <br>Arg2 is digits to round to
    <br>returns Rexx string
     */
 
 public static final netrexx.lang.Rexx doubleToRexx(double num,int digits){
  long m;
  boolean neg=false;
  long fmraw;
  long fmant;
  long fexp;
  netrexx.lang.RexxSet workset;
  netrexx.lang.Rexx res;
  netrexx.lang.Rexx twoexp=null;
  m=java.lang.Double.doubleToLongBits(num);/* map to long, bitwise */
  
  if (m>=0) 
   neg=false;
  else 
   {
    neg=true;
    m=m&9223372036854775807L;/* clear sign bit */
   }
  
  if (m==0) 
   return new netrexx.lang.Rexx(0);/* +/- zero always returns '0' */
  
  fmraw=m%4503599627370496L;/* mantissa is low 52 bits */
  fmant=fmraw|4503599627370496L;/* .. with the implied 1 bit */
  fexp=m/4503599627370496L;/* exponent part is the rest */
  
  if (fexp==2047) 
   {/* NaN or Infinity */
    if (fmraw==0) 
     return netrexx.lang.Rexx.toRexx("Infinity");
    return netrexx.lang.Rexx.toRexx("NaN");
   }
  
  /* Calculate the mantissa to decimal */
  workset=new netrexx.lang.RexxSet(digits+2);/* final digits +2 */
  res=(new netrexx.lang.Rexx(fmant)).OpDiv(workset,new netrexx.lang.Rexx("4503599627370496"));/* normalize to 0-1 */
  if (fexp!=0) 
   {/* is exponent to apply */
    /* 1023 is IEEE exponent bias */
    twoexp=(new netrexx.lang.Rexx((byte)2)).OpPow(workset,new netrexx.lang.Rexx(fexp-1023));/* 2**n */
    res=res.OpMult(workset,twoexp);/* adjust */
   }
  /* Finally prepare to requested digits */
  workset.digits=digits;
  if (neg) 
   if (res.ind==netrexx.lang.Rexx.ispos) 
    res.ind=netrexx.lang.Rexx.isneg;/* invert */
  return res.OpDiv(workset,one);/* format, drop 0's */
  }

 /** Trunc
    <br>Arg1 is number to truncate (known a number, unformatted)
    <br>Arg2 is places after point  [>=0]
    <br>returns char[]
     */
 
 public static final char[] trunc(netrexx.lang.Rexx num,int after){
  netrexx.lang.RexxSet set=null;
  int need;
  char newmant[]=null;
  int i=0;
  if (num.mant.length>netrexx.lang.Rexx.DefaultDigits) 
   set=new netrexx.lang.RexxSet(num.mant.length);
  else 
   set=(netrexx.lang.RexxSet)null;
  num=num.OpPlus(set); // make canonical [checks/corrects zeros
  //  and also makes private copy]
  need=num.mant.length+after;
  if (num.exp>0) 
   need=need+num.exp;
  if (need>netrexx.lang.Rexx.DefaultDigits) 
   set=new netrexx.lang.RexxSet(need);
  else 
   set=(netrexx.lang.RexxSet)null;
  num.exp=num.exp+after; // * 10**after
  num=num.OpDivI(set,one); // truncate unwanted/too-small digits
  if (num.ind==netrexx.lang.Rexx.iszero) 
   num.exp=after; // we lost exponent
  if (num.exp>0) 
   { // need some trailing zeros [perhaps many]
    newmant=new char[num.mant.length+num.exp];
    java.lang.System.arraycopy((java.lang.Object)num.mant,0,(java.lang.Object)newmant,0,num.mant.length);
    {int $8=newmant.length-1;i=num.mant.length;i:for(;i<=$8;i++){
     newmant[i]='0';
     }
    }/*i*/
    num.mant=newmant;
    num.exp=0;
   }
  num.exp=num.exp-after; // / 10**after
  return num.layoutnum(); // return laid-out number
  }

 /** Format
    <br>Arg1 is number to format (known a number, unformatted)
    <br>Arg2 is places before point [>0]
    <br>Arg3 is places after point  [>=0]
    <br>Arg4 is exponent places     [>0]
    <br>Arg5 is exponent digits     [>=0]
    <br>Arg6 is exponent form       ['E'/'S', uppercase]
    <br>returns char[]
    <p>Arg2 through Arg5 are -1 if not specified
     */
 
 public static final char[] format(netrexx.lang.Rexx num,int before,int after,int explaces,int exdigits,char exform){
  netrexx.lang.RexxSet set=null;
  int mag=0;
  int thisafter=0;
  int lead=0;
  char newmant[]=null;
  int i=0;
  int need=0;
  boolean bump=false;
  netrexx.lang.Rexx incr=null;
  char a[];
  int p=0;
  int epos=0;
  int places=0;
  
  
  if (num.mant.length>netrexx.lang.Rexx.DefaultDigits) 
   set=new netrexx.lang.RexxSet(num.mant.length);
  else 
   set=(netrexx.lang.RexxSet)null;
  
  num=num.OpPlus(set); // make canonical [checks/corrects zeros
  //  and also makes private copy]
  
  /* Determine the form of the result.  exform may become 'P' to
     signify Plain (no exponential form). */
  {setform:do{/*select*/
  if (exdigits==(-1))
   exform='P';
  else if (num.ind==netrexx.lang.Rexx.iszero)
   exform='P';
  else{
   // determine whether triggers
   mag=num.exp+num.mant.length;
   if (mag>exdigits) 
    ; // use requested format
   else 
    if (mag<(-5)) 
     ; // ..
    else 
     exform='P';
  }
  }while(false);}/*setform*/
  
  /* If 'after' was specified then we may need to adjust the
     mantissa.  This is tricky, as we must conform to the rules of
     exponential layout (e.g., we cannot end up with 10.0 if
     scientific). */
  if (after>=0) 
   {setafter:for(;;){
    // calculate the current after-length
    {/*select*/
    if (exform=='P')
     thisafter=(int)-num.exp;
    else if (exform=='S')
     thisafter=num.mant.length-1;
    else{ // engineering
     lead=(((num.exp+num.mant.length)-1))%3; // exponent to use
     if (lead<0) 
      lead=3+lead; // negative exponent case
     lead++; // number of leading digits
     if (lead>=num.mant.length) 
      thisafter=0;
     else 
      thisafter=num.mant.length-lead;
    }
    }
    
    if (thisafter==after) 
     break setafter; // we're in luck
    if (thisafter<after) 
     { // need added trailing zeros
      newmant=new char[(num.mant.length+after)-thisafter];
      java.lang.System.arraycopy((java.lang.Object)num.mant,0,(java.lang.Object)newmant,0,num.mant.length);
      {int $9=newmant.length-1;i=num.mant.length;i:for(;i<=$9;i++){
       newmant[i]='0';
       }
      }/*i*/
      num.mant=newmant;
      num.exp=num.exp-((after-thisafter)); // adjust exponent
      if (num.exp<netrexx.lang.Rexx.MinExp) 
       throw new netrexx.lang.ExponentOverflowException();
      break setafter;
     }
    // we have too many digits after the decimal point <sigh>
    // watch out for implied leading zeros in PLAIN case
    need=num.mant.length-((thisafter-after));
    if (need<0) 
     { // all digits go; no carry possible
      num.mant=zero.mant; // carry on with 0
      num.ind=zero.ind;
      num.exp=zero.exp;
      continue setafter; // recheck; may need trailing zeros
     }
    bump=(num.mant[need]>='5');
    if (need==0) 
     if ((!bump)) 
      {
       num.mant=zero.mant; // carry on with 0
       num.ind=zero.ind;
       num.exp=zero.exp;
       continue setafter; // recheck; may need trailing zeros
      }
    // truncate the mantissa
    newmant=new char[need];
    java.lang.System.arraycopy((java.lang.Object)num.mant,0,(java.lang.Object)newmant,0,need);
    num.mant=newmant;
    num.exp=num.exp+((thisafter-after)); // adjust exponent
    if (num.exp>netrexx.lang.Rexx.MaxExp) 
     throw new netrexx.lang.ExponentOverflowException();
    if ((!bump)) 
     break setafter; // we're done
    // here if we're rounding up/down
    incr=new netrexx.lang.Rexx(1);
    incr.ind=num.ind;
    incr.exp=num.exp;
    num=num.OpAdd(set,incr);
    // now go around and check again
    }
   }/*setafter*/
  
  if (exform=='S') 
   num.form=netrexx.lang.RexxSet.SCIENTIFIC;
  else 
   if (exform=='E') 
    num.form=netrexx.lang.RexxSet.ENGINEERING;
   else 
    num.form=netrexx.lang.RexxSet.PLAIN;
  if (exdigits==(-1)) 
   num.dig=0; // [form will be PLAIN]
  else 
   num.dig=exdigits;
  
  a=num.layoutnum(); // lay out, with exponent, etc.
  
  /* Here we have laid-out number in 'a' */
  // now apply 'before' and 'explaces' as needed
  if (before>0) 
   {
    p=netrexx.lang.RexxWords.pos('.',a,1); // find '.'
    {/*select*/
    if (p>0)
     p--; // found it
    else if (exdigits==(-1))
     p=a.length;
    else{ // try for 'E'
     p=netrexx.lang.RexxWords.pos('E',a,1);
     if (p>0) 
      p--; // found E
     else 
      p=a.length;
    }
    }
    // p is now current length of before part
    if (p>before) 
     throw new netrexx.lang.BadArgumentException(java.lang.String.valueOf(before));
    if (p<before)  // need leading blanks
     a=(new netrexx.lang.Rexx(a,true)).right(new netrexx.lang.Rexx((a.length+before)-p)).toCharArray();
   // [if p=before then it's just the right length]
   }
  if (explaces>0) 
   {
    epos=netrexx.lang.RexxWords.pos('E',a,1);
    if (epos==0) 
     a=(new netrexx.lang.Rexx(a,true)).left(new netrexx.lang.Rexx((a.length+explaces)+2)).toCharArray();
    else 
     { // may need to insert zeros
      places=(a.length-epos)-1; // number so far
      if (places>explaces) 
       throw new netrexx.lang.BadArgumentException(java.lang.String.valueOf(explaces));
      if (places<explaces)  // need zeros
       a=(new netrexx.lang.Rexx(a,true)).insert(netrexx.lang.Rexx.toRexx(""),new netrexx.lang.Rexx(epos+1),new netrexx.lang.Rexx(explaces-places),new netrexx.lang.Rexx('0')).toCharArray();
     }
   }
  return a;
  }

 /* ----- Local Helper methods ----- */
 /** Converts a hexadecimal character to int in range 0 to 15
    Valid characters are 0-9 a-f A-F
    Returns -1 if error */
 
 private static int hexint(char c){
  if (c>='0') 
   if (c<='9') 
    return ((int)(c))-((int)('0'));
  if (c>='A') 
   if (c<='F') 
    return (((int)(c))-((int)('A')))+10;
  if (c>='a') 
   if (c<='f') 
    return (((int)(c))-((int)('a')))+10;
  return -1;
  }
 
 private RexxUtil(){return;}
 }
