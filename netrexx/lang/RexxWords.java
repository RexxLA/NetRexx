/* Generated from 'RexxWords.nrx' 11 Jun 2011 03:25:37 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.RexxWords - Rexx class word functions                 */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1997.  All Rights Reserved.       */
/* Author    Mike Cowlishaw                                           */
/*                                                                    */
/* ------------------------------------------------------------------ */
/* 96.03.03 Initial version                                           */
/* 96.10.16 remove allblanks processing                               */



/**
   This defines 'helper functions' for the Rexx class for NetRexx.
   The functions here are all word- or character-like, and act on char[]
   strings rather than Rexx or String objects, and return either char[]
   or primitives.  See Rexx, Object-Rexx, or ANSI specification for full
   descriptions.  Many of the implementations here are based on the ANSI
   BIF descriptions; the significant difference is that words are
   delimited by blanks, not whitespace [Oct96].

   Argument checking (type and range) is assumed done by caller.
   Currently, all arguments should always be supplied.  Option letters
   should be a single character, uppercase.  Arguments are never
   altered.

   A 'position' is origin 1; an 'offset' is origin 0.
    */


public final class RexxWords{
 private static final java.lang.String $0="RexxWords.nrx";
 /* ----- Constants ----- */
 /* properties constant */

 
 /* ----- Functions ----- */
 /** Return 1 if string B is an abbreviation of string A.
    <br>Arg1 is string A.
    <br>Arg2 is string B.
    <br>Arg3 is minimum length
     */
 
 public static boolean abbrev(char a[],char b[],int len){
  int i=0;
  if ((a.length<len)|(b.length<len)) 
   return false;
  if (b.length>a.length) 
   return false;
  if ((b.length==0)&(len==0)) 
   return true; // 'default keyword' rule
  {int $1=b.length-1;i=0;i:for(;i<=$1;i++){
   if (a[i]!=b[i]) 
    return false; // mismatch
   }
  }/*i*/
  return true; // all match
  }

 /** Return centred string.
    <br>Arg1 is string work on.
    <br>Arg2 is the final width requested.
    <br>Arg3 is the pad character.
     */
 
 public static char[] centre(char s[],int wid,char pad){
  int chop;
  char ret[];
  int gap=0;
  int i=0;
  chop=s.length-wid;
  if (chop==0) 
   return s; // exact fit
  ret=new char[wid]; // need a return array
  if (chop>0) /* reducing */
   java.lang.System.arraycopy((java.lang.Object)s,chop/2,(java.lang.Object)ret,0,wid);
  else 
   {/* expanding */
    gap=((int)-chop)/2;
    {int $2=gap-1;i=0;i:for(;i<=$2;i++){
     ret[i]=pad;
     }
    }/*i*/
    java.lang.System.arraycopy((java.lang.Object)s,0,(java.lang.Object)ret,gap,s.length);
    {int $3=ret.length-1;i=gap+s.length;i:for(;i<=$3;i++){
     ret[i]=pad;
     }
    }/*i*/
   }
  return ret;
  }

 /** Return changed string.
    <br>Arg1 is string A.
    <br>Arg2 is string B.
    <br>Arg3 is pad character.
     */
 
 public static char[] changestr(char needle[],char haystack[],char $new[]){
  int reps;
  int newlen;
  char res[];
  int oin;
  int out;
  int p=0;
  int i=0;
  
  reps=countstr(needle,haystack);
  newlen=haystack.length+(reps*(($new.length-needle.length)));
  res=new char[newlen];
  oin=0;/* offset into haystack */
  out=0;/* offset into res */
  {int $4=reps;k:for(;$4>0;$4--){
   p=pos(needle,haystack,oin+1);
   /* copy stuff up to p */
   {int $5=p-2;i=oin;i:for(;i<=$5;i++){
    res[out]=haystack[i];
    out++;
    }
   }/*i*/
   /* add new */
   {int $6=$new.length-1;i=0;i:for(;i<=$6;i++){
    res[out]=$new[i];
    out++;
    }
   }/*i*/
   oin=(p+needle.length)-1;/* step over found needle */
   }
  }/*k*/
  /* add any leftovers */
  {int $7=haystack.length-1;i=oin;i:for(;i<=$7;i++){
   res[out]=haystack[i];
   out++;
   }
  }/*i*/
  return res;
  }

 /** Return comparison of two strings.
    <br>Arg1 is string A.
    <br>Arg2 is string B.
    <br>Arg3 is pad character.
     */
 
 public static int compare(char a[],char b[],char pad){
  int maxlen;
  int i=0;
  maxlen=a.length;
  if (b.length>maxlen) 
   maxlen=b.length;
  {int $8=maxlen;i=1;i:for(;i<=$8;i++){
   {/*select*/
   if (i>a.length)
    {if ((b[i-1])!=pad) 
     return i;
    }/* still have some A */
   else if (i>b.length)
    {if ((a[i-1])!=pad) 
     return i;
    }/* have both A and B */
   else{
    if ((a[i-1])!=(b[i-1])) 
     return i;
   }
   }
   }
  }/*i*/
  return 0;
  }

 /** Return count of occurrences in a string.
    <br>Arg1 is string of words to look for.
    <br>Arg2 is the string to check.
     */
 
 public static int countstr(char needle[],char haystack[]){
  int count;
  int p;
  count=0;
  p=pos(needle,haystack,1);
  {for(;;){if(!(p>0))break;
   count++;
   p=pos(needle,haystack,p+needle.length);
   }
  }
  return count;
  }

 /** Return string without substring.
    <br>Arg1 is string of words to look in.
    <br>Arg2 is the start character for delete.
    <br>Arg3 is count of characters.
     */
 
 public static char[] delstr(char s[],int start,int len){
  int fin;
  int need;
  char res[];
  if (len==0) 
   return s;/* nothing deleted */
  if (start>s.length) 
   return s;/* nothing deleted */
  /* start is start of first deleted */
  fin=start+len;/* start of continuation */
  if (fin>s.length) 
   fin=s.length+1;/* clamp */
  need=(start+s.length)-fin;
  res=new char[need];/* make array for result */
  if (start>1) 
   java.lang.System.arraycopy((java.lang.Object)s,0,(java.lang.Object)res,0,start-1);
  if (fin<=s.length) 
   java.lang.System.arraycopy((java.lang.Object)s,fin-1,(java.lang.Object)res,start-1,(s.length-fin)+1);
  return res;
  }

 /** Return string without substring of words.
    <br>Arg1 is string of words to look in.
    <br>Arg2 is the start word number for delete.
    <br>Arg3 is count of words.
     */
 
 public static char[] delword(char s[],int num,int len){
  int start;
  int fin;
  int need;
  char res[];
  if (len==0) 
   return s;/* nothing deleted */
  start=wordindex(s,num);/* start of first excluded */
  if (start==0) 
   return s;/* none */
  fin=wordindex(s,num+len);/* start of first continuation word */
  /* fin=0 if no continuation */
  /* [could use delstr here] */
  if (fin==0) 
   fin=s.length+1;
  need=(start+s.length)-fin;
  res=new char[need];/* make array for result */
  if (start>1) 
   java.lang.System.arraycopy((java.lang.Object)s,0,(java.lang.Object)res,0,start-1);
  if (fin<=s.length) 
   java.lang.System.arraycopy((java.lang.Object)s,fin-1,(java.lang.Object)res,start-1,(s.length-fin)+1);
  return res;
  }

 /** Insert string into another
    <br>Arg1 is target string     ) NB ...
    <br>Arg2 is string to insert  )  .. not classic Rexx order
    <br>Arg3 is where to insert
    <br>Arg4 is insert length
    <br>Arg5 is pad character
     */
 
 public static char[] insert(char chars[],char newchars[],int num,int len,char padchar){
  int reslen;
  char res[];
  int i=0;
  
  reslen=num+len;
  if (num<chars.length) 
   reslen=(reslen+chars.length)-num;
  res=new char[reslen];/* where we will lay out */
  if (num>0) 
   {/* have left part */
    if (num<=chars.length) 
     java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,0,num);
    else 
     {/* pad needed */
      java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,0,chars.length);
      {int $9=num-1;i=chars.length;i:for(;i<=$9;i++){
       res[i]=padchar;
       }
      }/*i*/
     }
   }
  if (len>0) 
   {/* have insert */
    if (len<=newchars.length) 
     java.lang.System.arraycopy((java.lang.Object)newchars,0,(java.lang.Object)res,num,len);
    else 
     {/* pad needed */
      java.lang.System.arraycopy((java.lang.Object)newchars,0,(java.lang.Object)res,num,newchars.length);
      {int $10=len-1;i=newchars.length;i:for(;i<=$10;i++){
       res[num+i]=padchar;
       }
      }/*i*/
     }
   }
  if (num<chars.length) 
   {/* have right part */
    java.lang.System.arraycopy((java.lang.Object)chars,num,(java.lang.Object)res,num+len,chars.length-num);
   }
  return res;
  }

 /** Overlay a string onto another
    <br>Arg1 is target string     ) NB ...
    <br>Arg2 is string to overlay )  .. not classic Rexx order
    <br>Arg3 is where to overlay
    <br>Arg4 is overlay length
    <br>Arg5 is pad character
     */
 
 public static char[] overlay(char chars[],char newchars[],int num,int len,char padchar){
  int reslen;
  char res[];
  int i=0;
  
  reslen=(num+len)-1;
  if (reslen<chars.length) 
   reslen=chars.length;
  res=new char[reslen];/* where we will lay out */
  if (num>1) 
   {/* have left part */
    if ((num-1)<=chars.length) 
     java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,0,num-1);
    else 
     {/* pad needed */
      java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,0,chars.length);
      {int $11=num-2;i=chars.length;i:for(;i<=$11;i++){
       res[i]=padchar;
       }
      }/*i*/
     }
   }
  if (len>0) 
   {/* have insert */
    if (len<=newchars.length) 
     java.lang.System.arraycopy((java.lang.Object)newchars,0,(java.lang.Object)res,num-1,len);
    else 
     {/* pad needed */
      java.lang.System.arraycopy((java.lang.Object)newchars,0,(java.lang.Object)res,num-1,newchars.length);
      {int $12=len-1;i=newchars.length;i:for(;i<=$12;i++){
       res[(num-1)+i]=padchar;
       }
      }/*i*/
     }
   }
  if ((((num+len)-1))<chars.length) 
   {/* have right part */
    java.lang.System.arraycopy((java.lang.Object)chars,(num+len)-1,(java.lang.Object)res,(num+len)-1,chars.length-(((num+len)-1)));
   
   }
  return res;
  }

 /** Find next blank.
    <br>Arg1 is string to search.
    <br>Arg2 is where to seach from.
    <br>Returns 0 if no blanks found.
     */
 
 public static final int nextblank(char s[],int start){
  int i=0;
  {int $13=s.length;i=start;i:for(;i<=$13;i++){
   if ((s[i-1])==(' ')) 
    return i;
   }
  }/*i*/
  return 0;/* not found */
  }

 /** Find next non-blank.
    <br>Arg1 is string to search.
    <br>Arg2 is where to seach from.
    <br>Returns 0 if no non-blanks found.
     */
 
 public static final int nextnonblank(char s[],int start){
  int i=0;
  {int $14=s.length;i=start;i:for(;i<=$14;i++){
   if ((s[i-1])!=(' ')) 
    return i;
   }
  }/*i*/
  return 0;/* none found */
  }

 /** Return position of a needle character in a haystack.
    <br>Arg1 is character to look for.
    <br>Arg2 is the string to check.
    <br>Arg3 is start position.
     */
 
 public static int pos(char needle,java.lang.String haystack,int start){
  return pos(needle,haystack.toCharArray(),start);
  }

 
 public static int pos(char needle,char haystack[],int start){
  int o=0;
  {int $15=haystack.length-1;o=start-1;o:for(;o<=$15;o++){
   if (haystack[o]==needle) 
    return o+1;
   }
  }/*o*/
  return 0;/* not found */
  }

 /** Return position of a needle in a haystack.
    <br>Arg1 is string to look for.
    <br>Arg2 is the string to check.
    <br>Arg3 is start position.
     */
 
 public static int pos(java.lang.String needle,java.lang.String haystack,int start){
  return pos(needle.toCharArray(),haystack.toCharArray(),start);
  }

 
 public static int pos(char needle[],char haystack[],int start){
  int i=0;
  int j=0;
  if (needle.length==0) 
   return 0;
  {int $16=(haystack.length-needle.length)+1;i=start;i:for(;i<=$16;i++){
   {int $17=needle.length-1;j=0;j:for(;j<=$17;j++){
    if (needle[j]!=(haystack[(i+j)-1])) 
     continue i;
    }
   }/*j*/
   return i;/* all matched */
   }
  }/*i*/
  return 0;/* not found */
  }

 /** Return spaced-out string.
    <br>Arg1 is string of words to work on.
    <br>Arg2 is the spacing requested.
    <br>Arg3 is the pad character.
     */
 
 public static char[] space(char s[],int gap,char pad){
  int start;
  int count;
  int nonspaces;
  int nextblank=0;
  int len;
  char res[];
  int out;
  int c=0;
  /* first count words and non-spaces */
  start=1;
  count=0;
  nonspaces=0;
  {$18:for(;;){
   start=nextnonblank(s,start);
   if (start==0) 
    break $18;
   count++;
   nextblank=nextblank(s,start+1);
   if (nextblank==0) 
    {
     nonspaces=((nonspaces+s.length)+1)-start;
     break $18;
    }
   nonspaces=(nonspaces+nextblank)-start;
   start=nextblank;
   }
  }
  if (count==0) 
   return new char[0];/* wordless */
  len=(((count-1))*gap)+nonspaces;/* total chars */
  res=new char[len];
  /* Roughly the same loop again, but this time copy nonspaces */
  start=1;
  out=0;
  {int $19=count;c=1;c:for(;c<=$19;c++){
   start=nextnonblank(s,start);
   if (start==0) 
    break c;
   {$20:for(;;){
    res[out]=s[start-1];
    start++;
    if (start>s.length) 
     break c;/* all done */
    out++;
    if ((s[start-1])==(' ')) 
     break $20;
    }
   }
   /* Add gap padding, unless last word */
   if (c==count) 
    break c;
   {int $21=gap;i:for(;$21>0;$21--){
    res[out]=pad;
    out++;
    }
   }/*i*/
   }
  }/*c*/
  return res;
  }

 /** Return substring of words.
    <br>Arg1 is string of words to look in.
    <br>Arg2 is the start word number.
    <br>Arg3 is count of words.
     */
 
 public static char[] subword(char s[],int num,int len){
  int start;
  int fin;
  int need;
  char res[];
  if (len==0) 
   return new char[0];
  start=wordindex(s,num);
  if (start==0) 
   return new char[0];
  fin=wordindex(s,num+len);/* start of first excluded word */
  if (fin==0) 
   fin=s.length+1;
  {int $22=start;fin=fin-1;fin:for(;fin>=$22;fin--){/* drop trailing blanks */
   if ((s[fin-1])!=(' ')) 
    break fin;
   }
  }/*fin*/
  need=(fin-start)+1;
  res=new char[need];/* make & copy substring */
  java.lang.System.arraycopy((java.lang.Object)s,start-1,(java.lang.Object)res,0,need);
  return res;
  }

 /** Return position of first character that doesn't or does
    match.
    <br>Arg1 is primary string to check.
    <br>Arg2 is list of characters to check for.
    <br>Arg3 is 'M' for match or 'N' for nomatch.
    <br>Arg4 is start position.
    
    Should be unused, nowadays. */
 
 public static int verify(char s[],char v[],char opt,int start){
  if (opt=='N') 
   return verifyn(s,v,start);
  return verifym(s,v,start);
  }

 /** Return position of first character that matches.
    <br>Arg1 is primary string to check.
    <br>Arg2 is list of characters to check for.
    <br>Arg3 is start position.
     */
 
 public static int verifym(char s[],char v[],int start){
  int last;
  int i=0;
  char $try=0;
  int t=0;
  last=s.length;
  if (start>last) 
   return 0;
  if (v.length==0) 
   return 0;
  {int $23=last;i=start;i:for(;i<=$23;i++){
   $try=s[i-1];
   if ($try==v[0]) 
    return i; // common case, fastpath
   t=pos($try,v,2); // try the others
   if (t>0) 
    return i; // matched
   }
  }/*i*/
  return 0;
  }

 /** Return position of first character that doesn't match.
    <br>Arg1 is primary string to check.
    <br>Arg2 is list of characters to check for.
    <br>Arg3 is start position.
     */
 
 public static int verifyn(char s[],char v[],int start){
  int last;
  int i=0;
  int t=0;
  last=s.length;
  if (start>last) 
   return 0;
  if (v.length==0) 
   return start;
  {int $24=last;i=start;i:for(;i<=$24;i++){
   t=pos(s[i-1],v,1);
   if (t==0) 
    return i;/* unmatched */
   }
  }/*i*/
  return 0;
  }

 /** Return word from a string.
    <br>Arg1 is string of words to look in.
    <br>Arg2 is the word number.
     */
 
 public static char[] word(char s[],int num){
  return subword(s,num,1);
  }

 /** Return character position of first character of a word.
    <br>Arg1 is string to search
    <br>Arg2 is word position selected
     */
 
 public static int wordindex(char s[],int num){
  int start;
  int count=0;
  start=1;
  {count=1;count:for(;;count++){
   start=nextnonblank(s,start);
   if (start==0) 
    return 0;
   if (count==num) 
    break count;
   start=nextblank(s,start+1);
   if (start==0) 
    return 0;
   }
  }/*count*/
  return start;
  }

 /** Return length of a word.
    <br>Arg1 is string to search
    <br>Arg2 is word position selected
     */
 
 public static int wordlength(char s[],int num){
  int start;
  int fin;
  start=wordindex(s,num);
  if (start==0) 
   return 0;
  fin=nextblank(s,start+1);
  if (fin==0) 
   fin=s.length+1;
  return fin-start;
  }

 /** Return position of a words needle in a haystack.
    <br>Arg1 is string of words to look for.
    <br>Arg2 is the string to check.
    <br>Arg3 is start position (in words).
     */
 
 public static int wordpos(char needle[],char haystack[],int wpos){
  int nlen;
  int nbeg;
  int hbeg;
  int hlen;
  int nb=0;
  int hb=0;
  int nend=0;
  int hend=0;
  int h=0;
  int n=0;
  nlen=needle.length; // cache
  if (nlen==0) 
   return 0; // null string
  /* find and record start of first word in needle */
  nbeg=nextnonblank(needle,1);
  if (nbeg==0) 
   return 0; // no words
  /* find position of start word in haystack */
  hbeg=wordindex(haystack,wpos);
  if (hbeg==0) 
   return 0; // wordless haystack
  hlen=haystack.length; // cache
  {$26:for(;;){ // each possible start in haystack
   /* compare by words [nbeg and hbeg starts each list] */
   nb=nbeg;
   hb=hbeg;
   {compare:for(;;){
    /* start of word in both; find ends */
    nend=nextblank(needle,nb+1);
    if (nend==0) 
     nend=nlen+1;
    hend=nextblank(haystack,hb+1);
    if (hend==0) 
     hend=hlen+1;
    if ((hend-hb)!=(nend-nb))  // word lengths differ
     break compare;
    // compare words
    h=hb-1;
    {int $25=nend-2;n=nb-1;n:for(;n<=$25;n++){ // offsets
     if (needle[n]!=haystack[h]) 
      break compare; // character mismatch
     h++;
     }
    }/*n*/
    // word matched
    if (nend>nlen) 
     return wpos; // needle all matched
    nb=nextnonblank(needle,nend); // skip spaces
    if (nb==0) 
     return wpos; // needle all matched
    // more words in needle
    if (hend>hlen) 
     break compare; // no more hay
    hb=nextnonblank(haystack,hend); // skip spaces
    if (hb==0) 
     break compare; // no more hay
    }
   }/*compare*/
   /* here if not found at this wpos start */
   wpos++;
   hbeg=nextblank(haystack,hbeg+1); // skip word
   if (hbeg==0) 
    break $26;
   hbeg=nextnonblank(haystack,hbeg+1); // skip spaces
   if (hbeg==0) 
    break $26;
   }
  }
  return 0;/* not found */
  }

 /** Return number of words in a string.
    <br>Arg1 is string to search
     */
 
 public static int words(char s[]){
  int start;
  int count;
  start=1;
  count=0;
  {$27:for(;;){
   start=nextnonblank(s,start);
   if (start==0) 
    break $27;
   count++;
   start=nextblank(s,start+1);
   if (start==0) 
    break $27;
   }
  }
  return count;
  }
 
 private RexxWords(){return;}
 }
