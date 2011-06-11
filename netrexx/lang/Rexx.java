/* Generated from 'Rexx.nrx' 11 Jun 2011 03:28:02 [v3.00] *//* Options: Binary Comments Compact Crossref Java Logo Replace Sourcedir Strictargs Strictcase Trace2 Verbose3 */package netrexx.lang;/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.Rexx                                                  */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 1998.  All Rights Reserved.       */
/* Author    Mike Cowlishaw                                           */
/*                                                                    */
/* Must not use operators on Rexx class objects (or loop will ensue)  */
/* A Rexx object is never changed in value once constructed; this     */
/*   avoids the need for exclusivity (locking).  However, the         */
/*   contents of the indexed variables collection can change.         */
/* ------------------------------------------------------------------ */
/* Notes:                                                             */
/*   tochar and padcheck are identical code                           */
/*   System.arraycopy is faster than explicit loop as follows         */
/*     Mean length 4:  equal                                          */
/*     Mean length 8:  x2                                             */
/*     Mean length 16: x3                                             */
/*     Mean length 24: x4                                             */
/*   From Rexx experience, we expect mean length 8-16                 */
/*   Compiling self, though, made no significant difference           */
/*                                                                    */
/*   Mant could be changed to byte array, with '0' subtracted         */
/*                                                                    */
/*   DMSRCN referred to below is the original (1981) IBM S/370        */
/*   assembler code implementation of the algorithms below; it is     */
/*   now called IXXRCN and is available with the OS/390 and VM/ESA    */
/*   operating systems.                                               */
/* ------------------------------------------------------------------ */
/* 96.01.30 Initial version in NetRexx                                */
/* 96.02.08 Rework with String property; don't extend StringBuffer    */
/* 96.02.10 [RexxNum] Initial version in NetRexx                      */
/* 96.02.15 Fix to allow for incorrect String.getChars documentation  */
/* 96.02.17 [RexxNum] -> Primitive conversions                        */
/* 96.02.19 [RexxNum] complete toString, add rounding, exponents      */
/* 96.02.20 [RexxNum] charaddsub -- simplified from DMSRCN, multiply  */
/* 96.02.22 [RexxNum] dodivide -- using DMSRCN algorithm              */
/* 96.02.24 Major changes:                                            */
/*            -- merge Rexx and RexxNum classes                       */
/*            -- number lookaside determined at construction time     */
/*            -- Operators all have a RexxSet argument (may be null)  */
/*            -- conversions from number to string deferred to time-  */
/*               of-use [layout()]                                    */
/* 96.0x.xx Stubs for many standard methods (as for ANSI BIFs)        */
/* 96.03.05 Rexx(String[]) constructor                                */
/* 96.03.11 Add RexxNode and getnode(Rexx), etc., for stems           */
/* 96.05.17 tochar() returns first character [used to expect number]  */
/* 96.06.06 tochar(String) and tochar(char[]) helper functions added  */
/* 96.06.15 tochararray(char) helper added; also hashCode and equals. */
/* 96.06.23 use hashtables for indexed arrays, RexxNode becomes proxy */
/* 96.07.28 add numeric FORM                                          */
/* 96.08.11 gather built-ins, and ensure stubs for all                */
/* 96.08.21 use System.arraycopy for simple copies                    */
/* 96.09.20 exists() (and testnode); if leaf=null then dropped        */
/* 96.09.21 extended upper() and lower() with start/length            */
/* 96.09.24 use Character for upper/lower, so Unicode works on 1.0.1  */
/* 96.09.24 allow extra digits for numbers                            */
/* 96.09.29 stubs for d2x, format, sequence, x2d                      */
/* 96.11.16 power operator                                            */
/* 96.11.18 insert, overlay, trunc stubs                              */
/* 97.07.18 move x2b, x2c to RexxUtil; add conversion functions       */
/* 97.07.30 add copyIndexed                                           */
/* 97.10.28 revise overflow detection                                 */
/* 97.12.xx add operand preparation to power, multiply, add           */
/* 98.01.13 divide result of 0 not cleanly finished                   */
/* 98.03.07 copyindexed should return a value                         */
/* 98.05.31 correct finalization of remainder                         */
/* 98.07.01 boundary conditions for charaddsub adjusted               */
/* 98.12.20 extend layout to force/allow plain notation               */
/* ------------------------------------------------------------------ */



/**
   This defines the primary string class for NetRexx.
 */

public class Rexx implements netrexx.lang.RexxOperators,java.io.Serializable{private static final netrexx.lang.Rexx $01=netrexx.lang.Rexx.toRexx("Bad binary");private static final netrexx.lang.Rexx $02=new netrexx.lang.Rexx(0);private static final java.lang.String $0="Rexx.nrx";

/* ----- Constants ----- */
/* properties constant public */ // might be useful to others
public static final int DefaultDigits=netrexx.lang.RexxSet.DEFAULT_DIGITS; // default Digits
public static final byte DefaultForm=netrexx.lang.RexxSet.DEFAULT_FORM; // default Form
public static final java.lang.String Lowers="abcdefghijklmnopqrstuvwxyz";
public static final java.lang.String Uppers="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
public static final java.lang.String Digits09="0123456789";
public static final char Hexes[]="0123456789ABCDEFabcdef".toCharArray(); // note uppercase first

/* properties constant shared */ // local to package
static final byte ispos=1; // ind: indicates positive (must be 1)
static final byte iszero=0; // ind: indicates zero     (must be 0)
static final byte isneg=((byte)-1); // ind: indicates negative (must be -1)
static final byte NotaNum=((byte)-2); // ind: indicates non-number
static final int MinExp=-999999999; // minimum exponent allowed
static final int MaxExp=999999999; // maximum exponent allowed
static final int MinArg=-999999999; // minimum argument integer
static final int MaxArg=999999999; // maximum argument integer

/* ----- Properties ----- */
/* properties shared */
/** The immutable character value of the object.
   <p>When constructed from a number, or after an arithmetic
   operation, this may be null (and numeric lookaside must exist).
   Conversion from number to string is only done when necessary.
    */
char chars[];

/** The indicator
   <p>This may take the values:
   <pre>
   1  -- the number is positive
   0  -- the number is zero
   -1 -- the number is negative
   -2 -- not a number (NotaNum)
    */
// We only need two bits for this, but use a byte for now
byte ind;

/** The formatting indicator
   <p>This may take the values:
   <pre>
   RexxSet.SCIENTIFIC     -- use scientific notation
   RexxSet.ENGINEERING    -- use engineering notation
   RexxSet.PLAIN          -- use plain
    */
/* We only need two bits for this, at present, but use a byte */
byte form;

/** The character value of the mantissa, if a number.
   This is undefined if ind=NotaNum.
    */
char mant[];

/** The exponent, if a number.  This is undefined if
   ind=NotaNum.
    */
int exp;

/** The digits to use for laying out, if a number.  This is
   undefined if ind=NotaNum.
    */
int dig;

/** The stem collection for this Rexx object (null, if no
   collection yet).
    */
java.util.Hashtable coll;

/* ----- Constructors ----- */
/** Make a Rexx object from a char  */
/* 96.05.29 -- this is much more common than before */
public Rexx(char inchar){
super();char $new[];netrexx.lang.Rexx r;$new=new char[1];
$new[0]=inchar;
r=new netrexx.lang.Rexx($new,true); // we need to test numericality
chars=r.chars;ind=r.ind;mant=r.mant;exp=r.exp;
dig=r.dig;form=r.form;
return;}

/** Make a Rexx object from an array of chars.  This copies the
   character array.  */
public Rexx(char in[]){super();char $new[];netrexx.lang.Rexx r;
$new=new char[in.length];
java.lang.System.arraycopy((java.lang.Object)in,0,(java.lang.Object)$new,0,in.length);
r=new netrexx.lang.Rexx($new,true);
chars=r.chars;ind=r.ind;mant=r.mant;exp=r.exp;
dig=r.dig;form=r.form;
return;}

/** Make a Rexx object from a java.lang.String  */
public Rexx(java.lang.String string){
this(string.toCharArray(),true);return;}

/** Make a Rexx object from an array of java.lang.String; the
   array elements are concatenated with a single blank between each.
 */
public Rexx(java.lang.String strings[]){
this(sa2ca(strings),true);return;} // use helper

/** Clean-Clone a Rexx object
   It does not need to copy chars and mant arrays as Rexx objects are
   immutable; however, the collection is not (and must not) be copied.
 */
public Rexx(netrexx.lang.Rexx in){super();
chars=in.chars;ind=in.ind;mant=in.mant;
exp=in.exp;dig=in.dig;form=in.form;
coll=(java.util.Hashtable)null; // ensure no collection
return;}

/** Make a Rexx object directly from a boolean.  */
public Rexx(boolean flag){
super();exp=0;dig=9;form=DefaultForm;mant=new char[1];
if (flag) {mant[0]='1';ind=ispos;}
else {mant[0]='0';ind=iszero;}
chars=mant; // avoid layouts
return;}

/** Make a Rexx object from a byte.  */
public Rexx(byte num){
this((int)num);return;}

/** Make a Rexx object from a short.  */
public Rexx(short num){
this((int)num);return;}

/** Make a Rexx object directly from an int.  */
// We fastpath commoners
// chars is set to null only for negatives less than -9, and
// we then ensure will layout correctly by setting suitable digits
public Rexx(int num){
super();exp=0;form=DefaultForm;
if (num<=9) if (num>=(-9)) { // very common single digit case
mant=new char[1];
{/*select*/
if (num>0){
mant[0]=(char)(((int)('0'))+num);chars=mant;
ind=ispos;
}
else if (num==0){
mant[0]='0';chars=mant;
ind=iszero;
}
else{ // <0
chars=new char[2];
chars[0]='-';
chars[1]=(char)(((int)('0'))-num);
mant[0]=chars[1];
ind=isneg;}
}
return;
}
if (num>0) {
ind=ispos;
mant=java.lang.Integer.toString(num,10).toCharArray();
chars=mant;
return;
}
/* 0 case already handled */
ind=isneg;
chars=(char[])null;dig=10; // needs layout later
if (num==((int)(-2147483648L))) { // special case
mant=(new java.lang.String("2147483648")).toCharArray();
return;}
num=(int)-num;
mant=java.lang.Integer.toString(num,10).toCharArray();
return;}

/** Make a Rexx object directly from a long.  */
public Rexx(long num){
this(java.lang.String.valueOf(num).toCharArray(),true);return;}

/** Make a Rexx object directly from a float.  */
/* We used to rely on Java for this, using:
     this(String.valueOf(num).toCharArray(),1)
   but this did not return a canonical Rexx number (and only 6 digits) */
public Rexx(float num){
this(netrexx.lang.RexxUtil.doubleToRexx(num,7));return;}

/** Make a Rexx object directly from a double.  */
/* We used to rely on Java for this, using:
     this(String.valueOf(num).toCharArray(),1)
   but 6-digit result was unacceptable. */
public Rexx(double num){
this(netrexx.lang.RexxUtil.doubleToRexx(num,16));return;}

/** Make a Rexx object from an array of chars without copying
   the array.  The second argument indicates (by being present) that
   no copy is needed; if 1, numeric lookaside will be attempted, if 0,
   the string cannot be a number.
   <p>
   This is the primary constructor; all incoming strings end up here.
   <p>
   This constructor checks to see if the incoming array describes a
   valid number; if so, numeric lookaside is set up.  If not (which
   will usually be detected rapidly), the ind is set to NotaNum to
   indicate that this is syntactically not a valid number.
   <p>In all cases, a reference to the original character array is
   preserved for rapid re-use as a string, and to avoid changing all
   number-like inputs to canonical form.
    */
Rexx(char s[],boolean trynum){
super();int len;int i=0;int insign;int start;boolean exotic;int d;int e;int last;boolean eneg=false;int j=0;int dvalue=0;char c=0;chars=s; // we have chars
ind=NotaNum; // assume not a number
/* Fast path exit: numbers start with a digit, dot, blank, or +/- */
if (s.length==0) return; // null string
if (s[0]>'9') { // +/-/. and blank all < '9'
if (s[0]<=('\177')) return; // no ASCII number possible
// unless an extra digit it cannot be a number
// [more detailed check for base is below]
if ((!(java.lang.Character.isDigit(s[0])))) return;
}
if ((!trynum)) return; // no parse necessary
len=s.length;
/* Remove blanks and handle sign */
{i=len-1;i:for(;i>=0;i--){ // strip trailing
if (s[i]!=(' ')) break i;
len--;
if (len==0) return; // bad conversion (all blanks)
}}/*i*/

insign=0;
start=-1;
{int $1=len-1;i=0;i:for(;i<=$1;i++){
if (s[i]==(' ')) continue i;
if (s[i]==('-')) {
if (insign!=0) return; // two signs
insign=-1;
continue i;
}
if (s[i]!=('+')) { // start of number found
start=i;
break i;
}
/* found '+' */
if (insign!=0) return; // two signs
insign=1;
}}/*i*/
if (start<0) return; // no content

/* We're at the start of the number */
exotic=false; // have extra digits
d=0; // count of digits found
e=-1; // where dot was found
last=-1; // last character of mantissa
{int $2=len-1;i=start;i:for(;i<=$2;i++){
if (s[i]>='0')  // test for digit
if (s[i]<='9') {
last=i;d++; // still in mantissa
continue i;
}
if (s[i]=='.') { // record and ignore
if (e>=0) return; // two dots
e=i-start; // offset into mantissa
continue i;
}
if (s[i]!='e') if (s[i]!='E') { // expect an extra digit
if ((!(java.lang.Character.isDigit(s[i])))) return; // not a number
// defer the base 10 check until later to avoid extra method call
exotic=true; // will need testing later
last=i;d++; // still in mantissa
continue i;
}
/* Found 'e' or 'E' -- now process exponent */
if (i>(len-3)) return; // no room for sign + one digit
if ((len-i)>11) return; // more than sign + 9 digits
if ((s[i+1])==('-')) eneg=true;
else if ((s[i+1])==('+')) eneg=false;
else return; // not a number unless sign after the 'E'
if (((len-i)-2)>9) return;
{int $3=len-1;j=i+2;j:for(;j<=$3;j++){
if (s[j]<'0') return; // bad
if (s[j]>'9') { // maybe extra digit
if ((!(java.lang.Character.isDigit(s[j])))) return; // not a number
dvalue=java.lang.Character.digit(s[j],10); // check base
if (dvalue<0) return; // not base 10
}
else dvalue=((int)(s[j]))-((int)('0'));
exp=(exp*10)+dvalue;
}}/*j*/
if (eneg) exp=(int)-exp; // was negative
break i; // done with the exponent
}}/*i*/
/* Here when all done */
if (d==0) return; // no mantissa
if (e>=0) exp=(exp+e)-d; // adjust exponent if had dot

/* strip leading zeros/dot (leave final if all 0's) */
{int $4=last-1;i=start;i:for(;i<=$4;i++){
if (s[i]=='.') start++; // step past
else if (s[i]=='0') {start++;d--;}
else if (s[i]<='9') break i;/* non-0 */
else {/* exotic */
if ((java.lang.Character.digit(s[i],10))!=0) break i; // non-0 or bad
// is 0, strip like '0'
start++;d--;
}
}}/*i*/

{setmant:do{/*select*/
if (exotic){ // need to check for exotica
mant=new char[d]; // must use copy
j=0; // shift after dot
{int $5=d-1;i=0;i:for(;i<=$5;i++){
if ((s[start+i])=='.') j=1;
c=s[(start+i)+j];
if (c<='9') mant[i]=c;/* easy */
else {
dvalue=java.lang.Character.digit(c,10);
if (dvalue<0) return; // not a number after all
mant[i]=(char)(dvalue+((int)('0')));
}
}}/*i*/
}
else if (d==chars.length)mant=chars; // it's a pure integer already
else{{ // make and copy mantissa
mant=new char[d];
j=0; // shift after dot
{int $6=d-1;i=0;i:for(;i<=$6;i++){
if ((s[start+i])=='.') j=1;
mant[i]=s[(start+i)+j];
}}/*i*/
}}
}while(false);}/*setmant*/

/* Set the sign .. this confirms we have a number lookaside */
if (mant[0]=='0') {ind=iszero;exp=0;}
else if (insign<0) ind=isneg;
else ind=ispos;

/* say 'seenum: mant ind exp dig form' mant ind exp dig form */
return;}

/** Make an 'empty' Rexx object  */
Rexx(){super();return;}

/* ----- Collection methods ----- */
/* These are for NetRexx compiler to use; normally undocumented. */

/** Return the RexxNode keyed from a Rexx value.  */
/* The HashTable class protects the collection, but we have to
   protect the coll property if we change (initialize) it. */
public netrexx.lang.RexxNode getnode(netrexx.lang.Rexx key){netrexx.lang.RexxNode node;
if (coll==null) synchronized(this){{
if (coll==null)  // still virgin
coll=new java.util.Hashtable(37,0.67F); // make a hashtable
}}
node=(netrexx.lang.RexxNode)(coll.get((java.lang.Object)key)); // look it up
if (node!=null)  // found
if (node.leaf!=null) return node; // .. and not dropped
/* unknown node .. create an initialized proxy for it and return it */
/* Note: must clone with *no* collection for the initial value. */
// The clone operation is fast
node=new netrexx.lang.RexxNode(new netrexx.lang.Rexx(this)); // make node
/* Note: must add to collection, in case about to be set */
coll.put((java.lang.Object)key,(java.lang.Object)node); // save
return node;}

/** Test if a node exists in the collection.
          Returns 1 if it does, or 0 otherwise.  */
public boolean testnode(netrexx.lang.Rexx key){netrexx.lang.RexxNode node;
if (coll==null) return false; // no nodes
node=(netrexx.lang.RexxNode)(coll.get((java.lang.Object)key)); // look it up
if (node==null) return false; // no node
if (node.leaf==null) return false; // dropped
if (((java.lang.Object)node.leaf)==((java.lang.Object)node.initleaf))  // if the same as initial
return false; // .. means never set
return true;} // it's real

/** Return an enumeration of keys for a Rexx collection.  */
/* The HashTable class protects the collection, but we have to
   protect the coll property if we change (initialize) it. */
// This is used for LOOP OVER
public synchronized java.util.Enumeration keys(){
if (coll==null) synchronized(this){{
if (coll==null)  // still virgin
coll=new java.util.Hashtable(37,0.67F); // make a hashtable
}}
return coll.keys();}

/* ----- Base environment methods ----- */
/** Intlength returns the length of the value as an int  */
private final int intlength(){
if (chars==null) chars=layout();
return chars.length;}

/** Intwords returns count of words as an integer  */
private final int intwords(){
if (chars==null) chars=layout();
return netrexx.lang.RexxWords.words(chars);}

/** Return the value as a String  */
public java.lang.String toString(){
if (chars==null) chars=layout();
return new java.lang.String(chars);}

/** Return the value as a (new) char array  */
public char[] toCharArray(){char res[];
if (chars==null) chars=layout();
res=new char[chars.length];
java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,0,chars.length);
return res;}

/** Return the value as a char.
   Raises exception if not a single character.
    */
public char tochar() throws netrexx.lang.NotCharacterException{
if (chars==null) chars=layout();
if (this.chars.length!=1) throw new netrexx.lang.NotCharacterException(java.lang.String.valueOf(this.chars));
return this.chars[0];}

/** Return a hashcode for the value.
    */
public int hashCode(){int over=0;int hash;int i=0;
if (chars==null) chars=layout();
if (chars.length<14) over=((chars.length+1))/2;
else over=7;
/* Hash the first and last OVER characters */
hash=0;
{int $7=over-1;i=0;i:for(;i<=$7;i++){
hash=((hash*7)+(((int)(chars[i]))*2))+((int)(chars[(chars.length-i)-1]));
}}/*i*/
return hash;}

/** The '=' operator, Java-style.  */
public boolean equals(java.lang.Object rhs){
if (rhs==null) return false;
if ((rhs instanceof netrexx.lang.Rexx)) return (docomparestrict((netrexx.lang.RexxSet)null,(netrexx.lang.Rexx)rhs))==0;
if ((rhs instanceof java.lang.String)) return (docomparestrict((netrexx.lang.RexxSet)null,new netrexx.lang.Rexx((java.lang.String)rhs)))==0;
if ((rhs instanceof char[])) return (docomparestrict((netrexx.lang.RexxSet)null,new netrexx.lang.Rexx((char[])rhs)))==0;
return false;}

/** Return a character from the value  */
public char charAt(int index){
if (chars==null) chars=layout();
return chars[index];}

/** Return java.lang.String as a char.
   Helper function.
   Raises exception if not a single character.                .
    */
public static char tochar(java.lang.String s) throws netrexx.lang.NotCharacterException{
if ((s.length())!=1) throw new netrexx.lang.NotCharacterException(s);
return s.charAt(0);}

/** Return char[] as a char.
   Helper function.
   Raises exception if not a single character.                .
    */
public static char tochar(char s[]) throws netrexx.lang.NotCharacterException{
if (s.length!=1) throw new netrexx.lang.NotCharacterException(java.lang.String.valueOf(s));
return s[0];}

/** Return char as a char[].
   Helper function.
    */
public static char[] tochararray(char c){char ca[];
ca=new char[1];ca[0]=c;
return ca;}

/** Return Rexx as a char array
   Helper function, allows and will return null.
    */
public static final char[] tochararray(netrexx.lang.Rexx r){char res[];
if (r==null) return (char[])null;
if (r.chars==null) r.chars=r.layout();
res=new char[r.chars.length];
java.lang.System.arraycopy((java.lang.Object)r.chars,0,(java.lang.Object)res,0,r.chars.length);
return res;}

/** Return char[] as a Rexx.
   Helper function, allows and will return null.
    */
public static final netrexx.lang.Rexx toRexx(char ca[]){char $new[];
if (ca==null) return (netrexx.lang.Rexx)null;
$new=new char[ca.length];
java.lang.System.arraycopy((java.lang.Object)ca,0,(java.lang.Object)$new,0,ca.length);
return new netrexx.lang.Rexx($new,true);}

/** Return String as a Rexx.
   Helper function, allows and will return null.
    */
public static final netrexx.lang.Rexx toRexx(java.lang.String s){
if (s==null) return (netrexx.lang.Rexx)null;
return new netrexx.lang.Rexx(s.toCharArray(),true);}

/** Return Rexx as a String
   Helper function, allows and will return null.
    */
public static final java.lang.String toString(netrexx.lang.Rexx r){
if (r==null) return (java.lang.String)null;
if (r.chars==null) r.chars=r.layout();
return new java.lang.String(r.chars);}

/** Return significance (mantissa length in decimal digits)  of
   a number, or 0 if not a number.  */
public int significance(){
if (ind==NotaNum) return 0;
return mant.length;}

/* --- Convert to number primitives --- */
/* These may lose low-order precision, but may not lose high-order */

/** Return a number as an boolean
   Raises exception if not exactly 0 or 1
    */
public boolean toboolean() throws netrexx.lang.NotLogicException{
/* We know the number, if a number, is normalized */
if (ind==iszero) return false; // easy, and 50%
if (ind==ispos) 
if (mant.length==1) 
if (mant[0]=='1') return true;
throw new netrexx.lang.NotLogicException("Boolean must be 0 or 1.  Found:"+" "+this.toString());}

/** Return a number as a byte.
   Raises exception if information loss.
    */
public byte tobyte() throws java.lang.NumberFormatException{int num;
num=this.toint(); // this will check validity too
if ((num>127)|(num<(-128))) 
throw new java.lang.NumberFormatException("Conversion overflow");
return (byte)num;}

/** Return a number as a short.
   Raises exception if information loss.
    */
public short toshort() throws java.lang.NumberFormatException{int num;
num=this.toint(); // this will check validity too
if ((num>32767)|(num<(-32768))) 
throw new java.lang.NumberFormatException("Conversion overflow");
return (short)num;}

/** Return a number as an int.
   Raises exception if invalid or overflow.
   Decimal part must be 0.
   This does not use tolong() as the latter might be quite slow.
    */
public int toint() throws java.lang.NumberFormatException{int lodigit;int cstart=0;int j=0;int useexp=0;int result;int lastresult;int i=0;
if (ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
/* We know the number is normalized */
if (ind==iszero) return 0; // easy, and quite common
/* drop any trailing decimal part */
lodigit=mant.length-1;
if (exp<0) {
lodigit=lodigit+exp; // reduces by -(-exp)
/* all decimal places must be 0 */
if (lodigit<0) cstart=0;else cstart=lodigit+1;
{int $8=mant.length-1;j=cstart;j:for(;j<=$8;j++){
if (mant[j]!='0') 
throw new java.lang.NumberFormatException(netrexx.lang.Rexx.toString(netrexx.lang.Rexx.toRexx("Decimal part non-zero:").OpCcblank(null,this)));
}}/*j*/
if (lodigit<0) return 0; // -1<this<1
useexp=0;
}
else {/* >=0 */
if ((exp+mant.length)>10)  // early exit
throw new java.lang.NumberFormatException("Conversion overflow");
useexp=exp;
}
/* convert the mantissa to binary, inline for speed */
result=0;lastresult=result;
{int $9=lodigit+useexp;i=0;i:for(;i<=$9;i++){
result=result*10;
if (i<=lodigit) result=result+((((int)(mant[i]))-((int)('0'))));
if (result<lastresult) {/* also catches the 'impossible' number */
/* however the impossible number is allowed if we are negative */
if (ind==isneg) if (result==java.lang.Integer.MIN_VALUE) 
if (i==(lodigit+useexp))  // used all digits
return result;
throw new java.lang.NumberFormatException("Conversion overflow");
}
lastresult=result;
}}/*i*/
/* Looks good */
if (ind>0) return result;
return (int)-result;}

/** Return the number as a long.
   Raises exception if information loss.
   Decimal part must be 0.
    */
/* Later move this to a function in a helper class to save space? */
/* Identical to toint except for result=long, and exp>=20 test */
public long tolong() throws java.lang.NumberFormatException{int lodigit;int cstart=0;int j=0;int useexp=0;long result;long lastresult;int i=0;
if (ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
/* We know the number is normalized */
if (ind==0) return 0; // easy, and quite common
lodigit=mant.length-1; // last included digit
if (exp<0) {
lodigit=lodigit+exp; // -(-exp)
/* all decimal places must be 0 */
if (lodigit<0) cstart=0;else cstart=lodigit+1;
{int $10=mant.length-1;j=cstart;j:for(;j<=$10;j++){
if (mant[j]!='0') 
throw new java.lang.NumberFormatException(netrexx.lang.Rexx.toString(netrexx.lang.Rexx.toRexx("Decimal part non-zero:").OpCcblank(null,this)));
}}/*j*/
if (lodigit<0) return 0; // -1<this<1
useexp=0;
}
else {/* >=0 */
if ((exp+mant.length)>=20)  // early exit
throw new java.lang.NumberFormatException("Conversion overflow");
useexp=exp;
}
/* convert the mantissa to binary, inline for speed */
/* int(0) should => 0, later */
result=(long)(int)((byte)0);lastresult=result;
{int $11=lodigit+useexp;i=0;i:for(;i<=$11;i++){
result=result*10;
if (i<=lodigit) result=result+((((int)(mant[i]))-((int)('0'))));
if (result<lastresult) {/* also catches the 'impossible' number */
/* however the impossible number is allowed if we are negative */
if (ind<0) if (result==java.lang.Long.MIN_VALUE) 
if (i==(lodigit+useexp))  // used all digits
return result;
throw new java.lang.NumberFormatException("Conversion overflow");
}
lastresult=result;
}}/*i*/
/* so far so good */
if (ind>0) return result;
return (long)-result;}

/** Return the number as a float
   Should raises exception if information loss.
    */
public float tofloat() throws java.lang.NumberFormatException{double dub;
dub=this.todouble();
if ((dub>(3.402823466e+38D))|(dub<(-(3.402823466e+38D)))) 
throw new java.lang.NumberFormatException("Overflow");
return (float)dub;}

/** Return the number as a double.
   Raises exception if information loss.
    */
/* We go via a String for now .. later do a better/faster version */
/* This does not detect underflow [small numbers -> 0] */
public double todouble() throws java.lang.NumberFormatException{double dub;
if (ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
if (chars==null) chars=layout(); // convert to chars
/* next line may raise NumberFormatException */
dub=java.lang.Double.valueOf(new java.lang.String(chars)).doubleValue();
if (java.lang.Double.isInfinite(dub)) throw new java.lang.NumberFormatException("Overflow");
return dub;}

/** Return char array value of a number (conversion from number
     to laid-out canonical char array).
   The mantissa will either already have been rounded (following an
     operation) or will be of length appropriate (in the case of
     construction from an int).
   We should/must not alter the mantissa, here (or format and trunc
     will break).
   Form maybe the internal-use PLAIN setting, to force plain layout.
   Note that 'dig' is just a trigger point, and hence may be 0; it is
     only used if form is not PLAIN.
    */
char[] layoutnum(){ // visible to RexxUtil
return layout();}

private char[] layout(){int mag;java.lang.StringBuffer sb=null;int euse=0;int sig=0;java.lang.String s=null;char res[]=null; // fast linkage version
/* Next are sanity checks, can be removed later */
if (ind==NotaNum) netrexx.lang.RexxIO.Say("Internal error: Rexx missing number"+" "+java.lang.String.valueOf(chars));
if (dig<0) {
netrexx.lang.RexxIO.Say("Internal error: Rexx: bad dig"+" "+dig);
dig=DefaultDigits;
}
/* say 'layout: mant ind exp dig form' mant ind exp dig form */

mag=exp+mant.length;

if ((mag>dig)|(mag<(-5)))  // exponential notation triggered
if (form!=netrexx.lang.RexxSet.PLAIN) { // .. and allowed
sb=new java.lang.StringBuffer(mant.length+15); // -x.xxxE+999999999
if (ind==isneg) sb.append('-');
euse=(exp+mant.length)-1; // exponent to use
/* setup sig=significant digits and copy to result */
if (form==DefaultForm) { // scientific
sb.append(mant[0]); // significant character
if (mant.length>1)  // have decimal part
sb.append('.').append(mant,1,mant.length-1);
}
else {engineering:do{
sig=euse%3; // common
if (sig<0) sig=3+sig; // negative exponent
euse=euse-sig;
sig++;
if (sig>=mant.length) { // zero padding may be needed
sb.append(mant,0,mant.length);
{int $12=sig-mant.length;for(;$12>0;$12--){
sb.append('0');
}}
}
else { // decimal point needed
sb.append(mant,0,sig).append('.').append(mant,sig,mant.length-sig);
}}while(false);
}/*engineering*/

if (euse!=0) {
if (euse<0) {s="-";euse=(int)-euse;}
else s="+";
sb.append('E').append(s).append(euse);
}
res=new char[sb.length()];sb.getChars(0,sb.length(),res,0);
return res;
}

/* Here for a plain number */
if (exp==0) {/* easy */
if (ind>=0) return mant;
res=new char[mant.length+1];res[0]='-';
java.lang.System.arraycopy((java.lang.Object)mant,0,(java.lang.Object)res,1,mant.length);
return res;
}

/* Need a '.', and maybe quite a few zeros */
sb=new java.lang.StringBuffer((((mant.length+dig)+5)+3)+10); // allow for all cases
if (ind==isneg) sb.append('-');
/* MAG is the position of the point in the mantissa (index of the
   character it follows) */
if (mag<1) {/* 0.00xxxx form */
sb.append('0').append('.');
{int $13=(int)-mag;for(;$13>0;$13--){ // maybe 0
sb.append('0');
}}
sb.append(mant);
res=new char[sb.length()];sb.getChars(0,sb.length(),res,0);return res;
}
if (mag>mant.length) {/* xxxx0000 form */
sb.append(mant);
{int $14=mag-mant.length;for(;$14>0;$14--){ // never 0
sb.append('0');
}}
res=new char[sb.length()];sb.getChars(0,sb.length(),res,0);return res;
}
/* decimal point is in the middle of the mantissa */
sb.append(mant,0,mag).append('.').append(mant,mag,mant.length-mag);
res=new char[sb.length()];sb.getChars(0,sb.length(),res,0);
return res;}

/* ----- Argument checker methods ----- */
/** checks and returns a pad character, or raises exception if invalid */
private char padcheck() throws netrexx.lang.NotCharacterException{
if (chars==null) chars=layout();
if (chars.length!=1) throw new netrexx.lang.NotCharacterException(java.lang.String.valueOf(chars));
return chars[0];}

/** checks an argument to ensure it's a true integer in a certain range
   if OK, returns it */
private int intcheck(int min,int max) throws netrexx.lang.BadArgumentException{int lodigit;int i=0;

if (ind==NotaNum) throw new java.lang.NumberFormatException("Not a number");
/* We know the number is normalized */
/* check any trailing decimal part is all 0's */
lodigit=mant.length-1;
if (exp<0) {i=exp+1;i:for(;i<=0;i++){
if (mant[lodigit]=='0') lodigit--;
else throw new java.lang.NumberFormatException(netrexx.lang.Rexx.toString(netrexx.lang.Rexx.toRexx("Non-zero decimal part in").OpCcblank(null,this)));
}}/*i*/
i=this.toint();
if (i<min) throw new netrexx.lang.BadArgumentException("Argument"+" "+i+" "+"<"+" "+min);
if (i>max) throw new netrexx.lang.BadArgumentException("Argument"+" "+i+" "+">"+" "+max);
return i;}

/** checks there is an option character, and that it is in list
   if found, returns it (always upper case)
   Arg1 is string of permitted option characters (all upper case) */
private char optioncheck(java.lang.String oklist) throws netrexx.lang.BadArgumentException{char ochar;char uchar;

if (chars==null) chars=layout();
if (chars.length==0) throw new netrexx.lang.BadArgumentException("Null option string");
// Next line split to work around 1.1.4 JIT problem (flaky optimization)
ochar=chars[0];
uchar=java.lang.Character.toUpperCase(ochar);
if ((oklist.indexOf((int)(uchar),0))<0) 
throw new netrexx.lang.BadArgumentException("Bad Option character"+" "+java.lang.String.valueOf(ochar)+" "+"["+java.lang.String.valueOf(uchar)+"]");
return uchar;}

/* ---------------------------------------------------------------- */
/* Rexx 'built-in' methods                                          */
/* ---------------------------------------------------------------- */
/* Some of these are evaluated directly; others just
   check arguments then call a helper function */

/** Abbrev  */
public netrexx.lang.Rexx abbrev(netrexx.lang.Rexx b){ return abbrev(b,new netrexx.lang.Rexx(b.intlength()));}public netrexx.lang.Rexx abbrev(netrexx.lang.Rexx b,netrexx.lang.Rexx len){int n;
n=len.intcheck(0,MaxArg);
if (chars==null) chars=layout();
if (b.chars==null) b.chars=b.layout();
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.abbrev(chars,b.chars,n));}

/** Abs returns the absolute value of a number.  */
public netrexx.lang.Rexx abs() throws java.lang.NumberFormatException{netrexx.lang.RexxSet set=null;
if (ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
if (mant.length>DefaultDigits) set=new netrexx.lang.RexxSet(mant.length);
else set=(netrexx.lang.RexxSet)null;
if (ind>=0) return this.OpPlus(set); // make canonical
return this.OpMinus(set);}

/** B2x binary to hexadecimal conversion.  */
public netrexx.lang.Rexx b2x(){char res[];int j;int acc;int mask;int i=0;
if (chars==null) chars=layout();
if (chars.length==0) throw new netrexx.lang.BadArgumentException("No digits");
res=new char[((chars.length+3))/4];
j=res.length-1;
acc=0;mask=1;
{i=chars.length-1;i:for(;i>=0;i--){
if (chars[i]=='0') ;
else if (chars[i]=='1') acc=acc+mask;
else throw new netrexx.lang.BadArgumentException(netrexx.lang.Rexx.toString($01.OpCcblank(null,this)));
mask=mask+mask; // shift left 1
if ((mask==16)|(i==0)) {
res[j]=Hexes[acc];j--;
acc=0;mask=1;
}
}}/*i*/
return new netrexx.lang.Rexx(res);}

/** Centre/Center  */
public netrexx.lang.Rexx centre(netrexx.lang.Rexx wid){ return centre(wid,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx centre(netrexx.lang.Rexx wid,netrexx.lang.Rexx pad){int width;char padchar;
width=wid.intcheck(0,MaxArg);
padchar=pad.padcheck();
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxWords.centre(chars,width,padchar));}
public netrexx.lang.Rexx center(netrexx.lang.Rexx wid){ return center(wid,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx center(netrexx.lang.Rexx wid,netrexx.lang.Rexx pad){
return this.centre(wid,pad);}

/** Changestr  */
public netrexx.lang.Rexx changestr(netrexx.lang.Rexx old,netrexx.lang.Rexx $new){
if (chars==null) chars=layout();
if (old.chars==null) old.chars=old.layout();
if ($new.chars==null) $new.chars=$new.layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxWords.changestr(old.chars,chars,$new.chars));}

/** Compare compares with a string  */
public netrexx.lang.Rexx compare(netrexx.lang.Rexx target){ return compare(target,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx compare(netrexx.lang.Rexx target,netrexx.lang.Rexx pad){char padchar;
padchar=pad.padcheck();
if (chars==null) chars=layout();
if (target.chars==null) target.chars=target.layout();
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.compare(chars,target.chars,padchar));}

/** Copies returns n copies of a Rexx object  */
public netrexx.lang.Rexx copies(netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException{int rep;int len;char res[];int start;
rep=n.intcheck(0,MaxArg);
if (chars==null) chars=layout();
len=chars.length;
res=new char[rep*len];
start=0;
{int $15=rep;for(;$15>0;$15--){
java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,start,len);
start=start+len;
}}
return new netrexx.lang.Rexx(res,true);}

/** CopyIndexed merges the collection of indexed strings with
   the collection of (explicitly-set) indexed strings from another
   Rexx object, and returns the current object.  */
public synchronized netrexx.lang.Rexx copyIndexed(netrexx.lang.Rexx r){java.util.Enumeration $enum=null;netrexx.lang.Rexx key=null;netrexx.lang.RexxNode node=null;
// Note that the whole operation is protected (atomic)
if (r.coll==null) return this; // that was easy [probably rare]
synchronized(r.coll){{
$enum=r.coll.keys();
{collcopy:for(;;){if(!($enum.hasMoreElements()))break;
key=(netrexx.lang.Rexx)($enum.nextElement());
node=(netrexx.lang.RexxNode)(r.coll.get((java.lang.Object)key)); // get the node for the key
// ignore if dropped or never set explicitly
if (node.leaf==null) continue collcopy; // a dropped item
if (((java.lang.Object)node.leaf)==((java.lang.Object)node.initleaf)) continue collcopy;
// it's a reference that we want
// use getnode for safety, initialization, and to maintain transparency
this.getnode(key).leaf=node.leaf;
}}/*collcopy*/
}}
return this;}

/** Countstr  */
public netrexx.lang.Rexx countstr(netrexx.lang.Rexx b){
if (chars==null) chars=layout();
if (b.chars==null) b.chars=b.layout();
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.countstr(b.chars,chars));}

/** C2d coded to decimal conversion.  */
public netrexx.lang.Rexx c2d(){return new netrexx.lang.Rexx((int)(padcheck()));}

/** C2x coded to hexadecimal conversion.  */
public netrexx.lang.Rexx c2x(){int enc;char res[];
enc=(int)(padcheck());
res="0000".toCharArray();
res[3]=Hexes[enc%16];enc=enc/16;
if (enc==0) return (new netrexx.lang.Rexx(res)).right(new netrexx.lang.Rexx((byte)1));
res[2]=Hexes[enc%16];enc=enc/16;
if (enc==0) return (new netrexx.lang.Rexx(res)).right(new netrexx.lang.Rexx((byte)2));
res[1]=Hexes[enc%16];enc=enc/16;
if (enc==0) return (new netrexx.lang.Rexx(res)).right(new netrexx.lang.Rexx((byte)3));
res[0]=Hexes[enc%16];enc=enc/16;
return new netrexx.lang.Rexx(res);}

/** Datatype returns 0/1; tests datatypes.
   <p>Differences from classic Rexx:
   <ol>
   <li>Must always be an option.
   <li>'B' and 'X' do not allow whitespace, and return 0 for a null
       string.
   <li>'D' for arabic digits
   <li>'S' tests only NetRexx characters (i.e., as 'A', plus '_'); the
       first letter may not be a digit..
    */
// This uses our methods [verify, etc.]
public netrexx.lang.Rexx datatype(netrexx.lang.Rexx opt) throws netrexx.lang.BadArgumentException{char ochar;int ok=0;netrexx.lang.RexxSet set=null;
ochar=opt.optioncheck("ABDLMNSUWX");
{types:do{/*select*/
if ((intlength())==0)ok=0;
else if (ochar=='A')ok=(int)((verify(netrexx.lang.Rexx.toRexx(Lowers+Uppers+Digits09))).OpEqS(null,$02)?1:0);
else if (ochar=='B')ok=(int)((verify(netrexx.lang.Rexx.toRexx("01"))).OpEqS(null,$02)?1:0);
else if (ochar=='D')ok=(int)((verify(netrexx.lang.Rexx.toRexx(Digits09))).OpEqS(null,$02)?1:0);
else if (ochar=='L')ok=(int)((verify(netrexx.lang.Rexx.toRexx(Lowers))).OpEqS(null,$02)?1:0);
else if (ochar=='M')ok=(int)((verify(netrexx.lang.Rexx.toRexx(Lowers+Uppers))).OpEqS(null,$02)?1:0);
else if (ochar=='N')ok=(int)((!((ind==NotaNum)))?1:0);
else if (ochar=='S')ok=(int)((verify(netrexx.lang.Rexx.toRexx("_"+Lowers+Uppers+Digits09))).OpEqS(null,$02)&(left(new netrexx.lang.Rexx((byte)1)).verify(netrexx.lang.Rexx.toRexx(Digits09))).OpNotEqS(null,$02)?1:0);

else if (ochar=='U')ok=(int)((verify(netrexx.lang.Rexx.toRexx(Uppers))).OpEqS(null,$02)?1:0);
else if (ochar=='W'){
/* If a number, make canonical and divide by 1, then just look for '.' */
if (ind==NotaNum) ok=0;
else {
if (mant.length>DefaultDigits) set=new netrexx.lang.RexxSet(mant.length);
else set=(netrexx.lang.RexxSet)null;
ok=(int)((this.OpDiv(set,new netrexx.lang.Rexx('1')).pos(new netrexx.lang.Rexx('.'))).OpEqS(null,$02)?1:0);
}
}
else if (ochar=='X')ok=(int)((verify(netrexx.lang.Rexx.toRexx(Hexes))).OpEqS(null,$02)?1:0);
else{throw new netrexx.lang.NoOtherwiseException();}}while(false);}/*types*/
return new netrexx.lang.Rexx(ok);}

/** Delstr deletes a substring (index 1) of a Rexx object  */
// Use intlength as length is a forward reference
public netrexx.lang.Rexx delstr(netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException{ return delstr(n,new netrexx.lang.Rexx(this.intlength()));}public netrexx.lang.Rexx delstr(netrexx.lang.Rexx n,netrexx.lang.Rexx length) throws netrexx.lang.BadArgumentException{int start;int len;


start=n.intcheck(1,MaxArg);
len=length.intcheck(0,MaxArg);
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxWords.delstr(chars,start,len));}

/** Delword deletes a substring of words (index 1) of a Rexx
   object  */
public netrexx.lang.Rexx delword(netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException{ return delword(n,new netrexx.lang.Rexx(this.intwords()));}public netrexx.lang.Rexx delword(netrexx.lang.Rexx n,netrexx.lang.Rexx length) throws netrexx.lang.BadArgumentException{int start;int len;


start=n.intcheck(1,MaxArg);
len=length.intcheck(0,MaxArg);
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxWords.delword(chars,start,len));}

/** D2c decimal to coded conversion.  */
public netrexx.lang.Rexx d2c(){int i;
i=this.toint();
if ((i<0)|(i>65535)) throw new java.lang.NumberFormatException(netrexx.lang.Rexx.toString(netrexx.lang.Rexx.toRexx("Encoding bad").OpCcblank(null,this)));
return new netrexx.lang.Rexx((char)(i));}

/** D2x decimal to hexadecimal conversion.  */
public netrexx.lang.Rexx d2x(netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{int req;
req=n.intcheck(0,MaxArg);
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxUtil.d2x(this,req));}
public netrexx.lang.Rexx d2x() throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxUtil.d2x(this,-1));}

/** Exists tests indexed variable  */
public netrexx.lang.Rexx exists(netrexx.lang.Rexx key){
return new netrexx.lang.Rexx(testnode(key));}

/** Format -- number layout.  */
/* Note: use of null to indicate omitted argument is Java-only */
public netrexx.lang.Rexx format() throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{ return format((netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null);}public netrexx.lang.Rexx format(netrexx.lang.Rexx before) throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{ return format(before,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null);}public netrexx.lang.Rexx format(netrexx.lang.Rexx before,netrexx.lang.Rexx after) throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{ return format(before,after,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null);}public netrexx.lang.Rexx format(netrexx.lang.Rexx before,netrexx.lang.Rexx after,netrexx.lang.Rexx explaces) throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{ return format(before,after,explaces,(netrexx.lang.Rexx)null,(netrexx.lang.Rexx)null);}public netrexx.lang.Rexx format(netrexx.lang.Rexx before,netrexx.lang.Rexx after,netrexx.lang.Rexx explaces,netrexx.lang.Rexx exdigits) throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{ return format(before,after,explaces,exdigits,(netrexx.lang.Rexx)null);}public netrexx.lang.Rexx format(netrexx.lang.Rexx before,netrexx.lang.Rexx after,netrexx.lang.Rexx explaces,netrexx.lang.Rexx exdigits,netrexx.lang.Rexx exform) throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{int b=0;int a=0;int p=0;int d=0;java.lang.String f=null;



if (ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
if (before==null) b=-1;else b=before.intcheck(1,MaxArg);
if (after==null) a=-1;else a=after.intcheck(0,MaxArg);
if (explaces==null) p=-1;else p=explaces.intcheck(1,MaxArg);
if (exdigits==null) d=-1;else d=exdigits.intcheck(0,MaxArg);
if (exform==null) f="S";else f=java.lang.String.valueOf(exform.optioncheck("SE"));
return new netrexx.lang.Rexx(netrexx.lang.RexxUtil.format(this,b,a,p,d,netrexx.lang.Rexx.tochar(f)),true);}

/** Insert  */
public netrexx.lang.Rexx insert(netrexx.lang.Rexx $new) throws netrexx.lang.BadArgumentException{ return insert($new,new netrexx.lang.Rexx(0),new netrexx.lang.Rexx($new.intlength()),netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx insert(netrexx.lang.Rexx $new,netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException{ return insert($new,n,new netrexx.lang.Rexx($new.intlength()),netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx insert(netrexx.lang.Rexx $new,netrexx.lang.Rexx n,netrexx.lang.Rexx length) throws netrexx.lang.BadArgumentException{ return insert($new,n,length,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx insert(netrexx.lang.Rexx $new,netrexx.lang.Rexx n,netrexx.lang.Rexx length,netrexx.lang.Rexx pad) throws netrexx.lang.BadArgumentException{int num;int len;char padchar;


num=n.intcheck(0,MaxArg);
len=length.intcheck(0,MaxArg);
padchar=pad.padcheck();
if (chars==null) chars=layout();
if ($new.chars==null) $new.chars=$new.layout();
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.insert(chars,$new.chars,num,len,padchar),true);}

/** Lastpos returns the last position of the needle in a Rexx object  */
public netrexx.lang.Rexx lastpos(netrexx.lang.Rexx needle) throws netrexx.lang.BadArgumentException{int start;
start=this.intlength();
if (start==0) start=1; // Use 1 if 0-length haystack
return lastpos(needle,new netrexx.lang.Rexx(start));}
public netrexx.lang.Rexx lastpos(netrexx.lang.Rexx needle,netrexx.lang.Rexx start) throws netrexx.lang.BadArgumentException{int startoff;int nlength;int i=0;int j=0;

startoff=(start.intcheck(1,MaxArg))-1;
if (chars==null) chars=layout();
if (startoff>=chars.length) startoff=chars.length-1;
if (needle.chars==null) needle.chars=needle.layout();
nlength=needle.chars.length;
if (nlength==0) return new netrexx.lang.Rexx(0);
startoff=(startoff-nlength)+1;
{i=startoff;i:for(;i>=0;i--){
{int $16=nlength-1;j=0;j:for(;j<=$16;j++){
if (needle.chars[j]!=(chars[i+j])) continue i;
}}/*j*/
return new netrexx.lang.Rexx(i+1);
}}/*i*/
return new netrexx.lang.Rexx(0);}

/** Left returns the leftmost substring of a Rexx object  */
public netrexx.lang.Rexx left(netrexx.lang.Rexx length){ return left(length,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx left(netrexx.lang.Rexx length,netrexx.lang.Rexx pad){
return substr(new netrexx.lang.Rexx(1),length,pad);}

/** Length returns the length of the Rexx object, in characters
    */
public netrexx.lang.Rexx length(){return new netrexx.lang.Rexx(intlength());}

/** Lower returns lowercased Rexx object  */
public netrexx.lang.Rexx lower() throws netrexx.lang.BadArgumentException{ return lower(new netrexx.lang.Rexx(1),this.length());}public netrexx.lang.Rexx lower(netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException{ return lower(n,this.length());}public netrexx.lang.Rexx lower(netrexx.lang.Rexx n,netrexx.lang.Rexx length) throws netrexx.lang.BadArgumentException{int startoff;int len;int j;char res[];int i=0;

startoff=(n.intcheck(1,MaxArg))-1;
len=length.intcheck(0,MaxArg);
if (chars==null) chars=layout();
j=chars.length;
if (j==0) return new netrexx.lang.Rexx(""); // null string
res=new char[j]; // result will be full length
if ((len<j)|(startoff>0))  // some unchanged characters
java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,0,j);
{int $17=j-1;int $18=len;i=startoff;i:for(;(i<=$17)&&($18>0);$18--,i++){
res[i]=java.lang.Character.toLowerCase(chars[i]);
}}/*i*/
return new netrexx.lang.Rexx(res,true);}

/** Max returns the larger number, first if equal.  */
public netrexx.lang.Rexx max(netrexx.lang.Rexx rhs) throws java.lang.NumberFormatException{int len;netrexx.lang.Rexx ret=null;
if (ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
if (rhs.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
len=DefaultDigits;
if (this.mant.length>len) len=this.mant.length;
if (rhs.mant.length>len) len=rhs.mant.length;
if ((this.docompare(new netrexx.lang.RexxSet(len),rhs))<0) ret=rhs;else ret=this;
len=DefaultDigits;if (ret.mant.length>len) len=ret.mant.length;
return ret.OpPlus(new netrexx.lang.RexxSet(len));}

/** Min returns the smaller number, first if equal.  */
public netrexx.lang.Rexx min(netrexx.lang.Rexx rhs) throws java.lang.NumberFormatException{int len;netrexx.lang.Rexx ret=null;
if (ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
if (rhs.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
len=DefaultDigits;
if (this.mant.length>len) len=this.mant.length;
if (rhs.mant.length>len) len=rhs.mant.length;
if ((this.docompare(new netrexx.lang.RexxSet(len),rhs))>0) ret=rhs;else ret=this;
len=DefaultDigits;if (ret.mant.length>len) len=ret.mant.length;
return ret.OpPlus(new netrexx.lang.RexxSet(len));}

/** Overlay  */
public netrexx.lang.Rexx overlay(netrexx.lang.Rexx $new) throws netrexx.lang.BadArgumentException{ return overlay($new,new netrexx.lang.Rexx(1),new netrexx.lang.Rexx($new.intlength()),netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx overlay(netrexx.lang.Rexx $new,netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException{ return overlay($new,n,new netrexx.lang.Rexx($new.intlength()),netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx overlay(netrexx.lang.Rexx $new,netrexx.lang.Rexx n,netrexx.lang.Rexx length) throws netrexx.lang.BadArgumentException{ return overlay($new,n,length,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx overlay(netrexx.lang.Rexx $new,netrexx.lang.Rexx n,netrexx.lang.Rexx length,netrexx.lang.Rexx pad) throws netrexx.lang.BadArgumentException{int num;int len;char padchar;


num=n.intcheck(1,MaxArg);
len=length.intcheck(0,MaxArg);
padchar=pad.padcheck();
if (chars==null) chars=layout();
if ($new.chars==null) $new.chars=$new.layout();
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.overlay(chars,$new.chars,num,len,padchar),true);}

/** Pos returns the position of the needle in a Rexx object  */
public netrexx.lang.Rexx pos(netrexx.lang.Rexx needle) throws netrexx.lang.BadArgumentException{ return pos(needle,new netrexx.lang.Rexx(1));}public netrexx.lang.Rexx pos(netrexx.lang.Rexx needle,netrexx.lang.Rexx start) throws netrexx.lang.BadArgumentException{int startoff;int i=0;int j=0;

startoff=(start.intcheck(1,MaxArg))-1;
if (needle.chars==null) needle.chars=needle.layout();
if (needle.chars.length==0) return new netrexx.lang.Rexx(0);
if (chars==null) chars=layout();
{int $19=chars.length-needle.chars.length;i=startoff;i:for(;i<=$19;i++){
{int $20=needle.chars.length-1;j=0;j:for(;j<=$20;j++){
if (needle.chars[j]!=(chars[i+j])) continue i;
}}/*j*/
return new netrexx.lang.Rexx(i+1);
}}/*i*/
return new netrexx.lang.Rexx(0);}

/** Reverse returns reversed Rexx object  */
public netrexx.lang.Rexx reverse(){int j;char res[];int i=0;
if (chars==null) chars=layout();
j=chars.length;
if (j==0) return new netrexx.lang.Rexx(""); // null string
res=new char[j];
{int $21=j-1;i=0;i:for(;i<=$21;i++){j--;res[i]=chars[j];}}/*i*/
return new netrexx.lang.Rexx(res,true);}

/** Right returns the rightmost substring of a Rexx object  */
public netrexx.lang.Rexx right(netrexx.lang.Rexx length) throws netrexx.lang.NotCharacterException,netrexx.lang.BadArgumentException{ return right(length,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx right(netrexx.lang.Rexx length,netrexx.lang.Rexx pad) throws netrexx.lang.NotCharacterException,netrexx.lang.BadArgumentException{int len;int trim;char padchar;char res[];int i=0;

len=length.intcheck(0,MaxArg);
if (chars==null) chars=layout();
trim=chars.length-len;
if (trim>=0) return this.substr(new netrexx.lang.Rexx(trim+1));
/* need padding */
padchar=pad.padcheck();
res=new char[len];
{int $22=((int)-trim)-1;i=0;i:for(;i<=$22;i++){res[i]=padchar;}}/*i*/
java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,i,res.length-i);
return new netrexx.lang.Rexx(res,true);}

/** Sequence (aka xrange).  */
public netrexx.lang.Rexx sequence(netrexx.lang.Rexx $final) throws netrexx.lang.BadArgumentException,netrexx.lang.NotCharacterException{char startchar;char finalchar;int istart;int len;char car[];int i=0;

startchar=this.padcheck();
finalchar=$final.padcheck();
istart=(int)(startchar);
len=(((int)(finalchar))-istart)+1;
if (len<=0) throw new netrexx.lang.BadArgumentException("final<start");
car=new char[len];
{int $23=len-1;i=0;i:for(;i<=$23;i++){
car[i]=(char)(i+istart);
}}/*i*/
return new netrexx.lang.Rexx(car,true);}

/** Sign tests sign  */
public netrexx.lang.Rexx sign() throws java.lang.NumberFormatException{
if (this.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
return new netrexx.lang.Rexx(this.ind);}

/** Space returns evenly-spaced string  */
public netrexx.lang.Rexx space(){ return space(new netrexx.lang.Rexx(1),netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx space(netrexx.lang.Rexx n){ return space(n,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx space(netrexx.lang.Rexx n,netrexx.lang.Rexx pad){int gap;char padchar;
gap=n.intcheck(0,MaxArg);
padchar=pad.padcheck();
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxWords.space(chars,gap,padchar));}

/** Strip returns stripped Rexx object  */
public netrexx.lang.Rexx strip() throws netrexx.lang.BadArgumentException{ return strip(netrexx.lang.Rexx.toRexx("B"),netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx strip(netrexx.lang.Rexx opt) throws netrexx.lang.BadArgumentException{ return strip(opt,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx strip(netrexx.lang.Rexx opt,netrexx.lang.Rexx pad) throws netrexx.lang.BadArgumentException{char ochar;char padchar;int startoff=0;int endoff=0;int len;char subchars[];

ochar=opt.optioncheck("BLT");
padchar=pad.padcheck();
if (chars==null) chars=layout();
if (chars.length==0) return new netrexx.lang.Rexx(""); // null string
if (ochar=='T') startoff=0;
else {/* strip leading padding */
{int $24=chars.length-1;startoff=0;startoff:for(;startoff<=$24;startoff++){
if (this.chars[startoff]!=padchar) break startoff;
}}/*startoff*/
if (startoff==chars.length) return new netrexx.lang.Rexx("");
}
if (ochar=='L') endoff=chars.length-1;
else {/* strip trailing padding */
{endoff=chars.length-1;endoff:for(;endoff>=0;endoff--){
if (this.chars[endoff]!=padchar) break endoff;
}}/*endoff*/
if (endoff<0) return new netrexx.lang.Rexx("");
}
if (startoff==0) if (endoff==(chars.length-1))  // no change
return new netrexx.lang.Rexx(this);
/* Need to return a substring */
len=(endoff-startoff)+1;
subchars=new char[len];
java.lang.System.arraycopy((java.lang.Object)chars,startoff,(java.lang.Object)subchars,0,len);
return new netrexx.lang.Rexx(subchars,true);}

/** Substr returns a substring (index 1) of a Rexx object  */
public netrexx.lang.Rexx substr(netrexx.lang.Rexx n) throws netrexx.lang.NotCharacterException,netrexx.lang.BadArgumentException{ return substr(n,(new netrexx.lang.Rexx(((this.intlength())+1)-(n.toint()))).max(new netrexx.lang.Rexx((byte)0)),netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx substr(netrexx.lang.Rexx n,netrexx.lang.Rexx length) throws netrexx.lang.NotCharacterException,netrexx.lang.BadArgumentException{ return substr(n,length,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx substr(netrexx.lang.Rexx n,netrexx.lang.Rexx length,netrexx.lang.Rexx pad) throws netrexx.lang.NotCharacterException,netrexx.lang.BadArgumentException{int startoff;int len;char padchar;int thislen;char subchars[];int i=0;




startoff=(n.intcheck(1,MaxArg))-1;
len=length.intcheck(0,MaxArg);
padchar=pad.padcheck();
if (chars==null) chars=layout();
thislen=chars.length;
subchars=new char[len];
if ((startoff+len)<=thislen) 
java.lang.System.arraycopy((java.lang.Object)chars,startoff,(java.lang.Object)subchars,0,len);
else {
/* pad needed; note startoff may be to right of string */
if (startoff<thislen)  // have some to re-use
java.lang.System.arraycopy((java.lang.Object)chars,startoff,(java.lang.Object)subchars,0,thislen-startoff);
else startoff=thislen; // pad from 0
{int $25=len-1;i=thislen-startoff;i:for(;i<=$25;i++){subchars[i]=padchar;}}/*i*/
}
return new netrexx.lang.Rexx(subchars,true);}

/** Subword returns a substring of words  */
public netrexx.lang.Rexx subword(netrexx.lang.Rexx n){ return subword(n,this.length());}public netrexx.lang.Rexx subword(netrexx.lang.Rexx n,netrexx.lang.Rexx length){int start;int len;
start=n.intcheck(1,MaxArg);
len=length.intcheck(0,MaxArg);
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxWords.subword(chars,start,len));}

/** Translate  */
public netrexx.lang.Rexx translate(netrexx.lang.Rexx tableo,netrexx.lang.Rexx tablei) throws netrexx.lang.BadArgumentException{ return translate(tableo,tablei,netrexx.lang.Rexx.toRexx(" "));}public netrexx.lang.Rexx translate(netrexx.lang.Rexx tableo,netrexx.lang.Rexx tablei,netrexx.lang.Rexx pad) throws netrexx.lang.BadArgumentException{char padchar;


padchar=pad.padcheck();
if (chars==null) chars=layout();
if (tableo.chars==null) tableo.chars=tableo.layout();
if (tablei.chars==null) tablei.chars=tablei.layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxUtil.translate(chars,tableo.chars,tablei.chars,padchar));}

/** Trunc number truncation.  */
public netrexx.lang.Rexx trunc() throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{ return trunc(new netrexx.lang.Rexx(0));}public netrexx.lang.Rexx trunc(netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException,java.lang.NumberFormatException{int after;

if (ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
after=n.intcheck(0,MaxArg);
return new netrexx.lang.Rexx(netrexx.lang.RexxUtil.trunc(this,after));}

/** Upper returns uppercased Rexx object  */
public netrexx.lang.Rexx upper() throws netrexx.lang.BadArgumentException{ return upper(new netrexx.lang.Rexx(1),this.length());}public netrexx.lang.Rexx upper(netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException{ return upper(n,this.length());}public netrexx.lang.Rexx upper(netrexx.lang.Rexx n,netrexx.lang.Rexx length) throws netrexx.lang.BadArgumentException{int startoff;int len;int j;char res[];int i=0;

startoff=(n.intcheck(1,MaxArg))-1;
len=length.intcheck(0,MaxArg);
if (chars==null) chars=layout();
j=chars.length;
if (j==0) return new netrexx.lang.Rexx(""); // null string
res=new char[j]; // result will be full length
if ((len<j)|(startoff>0))  // some unchanged characters
java.lang.System.arraycopy((java.lang.Object)chars,0,(java.lang.Object)res,0,j);
{int $26=j-1;int $27=len;i=startoff;i:for(;(i<=$26)&&($27>0);$27--,i++){
res[i]=java.lang.Character.toUpperCase(chars[i]);
}}/*i*/
return new netrexx.lang.Rexx(res,true);}

/** Verify returns verification  */
public netrexx.lang.Rexx verify(netrexx.lang.Rexx list){ return verify(list,netrexx.lang.Rexx.toRexx("N"),new netrexx.lang.Rexx(1));}public netrexx.lang.Rexx verify(netrexx.lang.Rexx list,netrexx.lang.Rexx opt){ return verify(list,opt,new netrexx.lang.Rexx(1));}public netrexx.lang.Rexx verify(netrexx.lang.Rexx list,netrexx.lang.Rexx opt,netrexx.lang.Rexx start){char ochar;int from;
ochar=opt.optioncheck("NM");
from=start.intcheck(1,MaxArg);
if (chars==null) chars=layout();
if (list.chars==null) list.chars=list.layout();
if (ochar=='N') return new netrexx.lang.Rexx(netrexx.lang.RexxWords.verifyn(chars,list.chars,from));
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.verifym(chars,list.chars,from));}

/** Word returns a substring of words  */
public netrexx.lang.Rexx word(netrexx.lang.Rexx n){return this.subword(n,new netrexx.lang.Rexx((byte)1));}

/** Wordindex returns index of a word  */
public netrexx.lang.Rexx wordindex(netrexx.lang.Rexx n){int from;
from=n.intcheck(1,MaxArg);
if (chars==null) chars=layout();
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.wordindex(chars,from));}

/** Wordlength returns length of a word  */
public netrexx.lang.Rexx wordlength(netrexx.lang.Rexx n){int from;
from=n.intcheck(1,MaxArg);
if (chars==null) chars=layout();
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.wordlength(chars,from));}

/** Wordpos returns the position of the words needle in a Rexx
   object  */
public netrexx.lang.Rexx wordpos(netrexx.lang.Rexx needle){ return wordpos(needle,new netrexx.lang.Rexx(1));}public netrexx.lang.Rexx wordpos(netrexx.lang.Rexx needle,netrexx.lang.Rexx num){int n;
n=num.intcheck(1,MaxArg);
if (chars==null) chars=layout();
if (needle.chars==null) needle.chars=needle.layout();
return new netrexx.lang.Rexx(netrexx.lang.RexxWords.wordpos(needle.chars,chars,n));}

/** Words returns count of words  */
public netrexx.lang.Rexx words(){
return new netrexx.lang.Rexx(intwords());}

/** X2b hexadecimal to binary conversion.  */
public netrexx.lang.Rexx x2b(){
if (chars==null) chars=layout();
if (chars.length==0) throw new netrexx.lang.BadArgumentException("No digits");
return new netrexx.lang.Rexx(netrexx.lang.RexxUtil.x2b(this));}

/** X2c hexadecimal to coded conversion.  */
public netrexx.lang.Rexx x2c(){
if (chars==null) chars=layout();
if (chars.length==0) throw new netrexx.lang.BadArgumentException("No digits");
return new netrexx.lang.Rexx(netrexx.lang.RexxUtil.x2c(this));}

/** X2d hexadecimal to decimal conversion.  */
public netrexx.lang.Rexx x2d(netrexx.lang.Rexx n) throws netrexx.lang.BadArgumentException{int req;
req=n.intcheck(0,MaxArg);
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxUtil.x2d(this,req));}
public netrexx.lang.Rexx x2d() throws netrexx.lang.BadArgumentException{
if (chars==null) chars=layout();
return netrexx.lang.Rexx.toRexx(netrexx.lang.RexxUtil.x2d(this,-1));}

/* ---------------------------------------------------------------- */
/* General Operator methods                                         */
/* ---------------------------------------------------------------- */
/** The '||' operator.  */
public netrexx.lang.Rexx OpCc(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return concat(set,rhs,0);}
/** The ' ' operator.  */
public netrexx.lang.Rexx OpCcblank(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return concat(set,rhs,1);}

/** The '=' operator.  */
public boolean OpEq(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (this.docompare(set,rhs))==0;}
/** The '\=' operator.  */
public boolean OpNotEq(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (this.docompare(set,rhs))!=0;}
/** The '<' operator.  */
public boolean OpLt(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (this.docompare(set,rhs))<0;}
/** The '>' operator.  */
public boolean OpGt(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (this.docompare(set,rhs))>0;}
/** The '<=' operator.  */
public boolean OpLtEq(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (this.docompare(set,rhs))<=0;}
/** The '>=' operator.  */
public boolean OpGtEq(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (this.docompare(set,rhs))>=0;}

/** The '==' operator.  */
public boolean OpEqS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (docomparestrict(set,rhs))==0;}
/** The '\==' operator.  */
public boolean OpNotEqS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (docomparestrict(set,rhs))!=0;}
/** The '<<' operator.  */
public boolean OpLtS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (docomparestrict(set,rhs))<0;}
/** The '>>' operator.  */
public boolean OpGtS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (docomparestrict(set,rhs))>0;}
/** The '<<=' operator.  */
public boolean OpLtEqS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (docomparestrict(set,rhs))<=0;}
/** The '>>=' operator.  */
public boolean OpGtEqS(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return (docomparestrict(set,rhs))>=0;}

/** The '|' operator.  */
public boolean OpOr(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
set=set;
return (this.toboolean())|(rhs.toboolean());}

/** The '&' operator.  */
public boolean OpAnd(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
set=set;
return (this.toboolean())&(rhs.toboolean());}

/** The '&&' operator.  */
public boolean OpXor(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
set=set;
return (this.toboolean())^(rhs.toboolean());}

/** The '\' prefix operator.  */
public boolean OpNot(netrexx.lang.RexxSet set){
set=set;
return (!(this.toboolean()));}

/* ----- Numeric Operator methods ----- */
/** The minus operator.  */
/* This clones and flips the sign; invalidates chars to ensure
   canonical if used as a string, later */
public netrexx.lang.Rexx OpMinus(netrexx.lang.RexxSet set){netrexx.lang.Rexx res;
if (this.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
res=new netrexx.lang.Rexx(this);res.ind=(byte)-res.ind;res.chars=(char[])null;
if (set==null) {res.dig=DefaultDigits;res.form=DefaultForm;}
else {res.dig=set.digits;res.form=set.form;}
return res.finish(res.dig,false);}

/** The plus operator.  */
/* Checks validity, clones, and invalidates chars */
public netrexx.lang.Rexx OpPlus(netrexx.lang.RexxSet set){netrexx.lang.Rexx res;
if (this.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(chars));
res=new netrexx.lang.Rexx(this);res.chars=(char[])null;
if (set==null) {res.dig=DefaultDigits;res.form=DefaultForm;}
else {res.dig=set.digits;res.form=set.form;}
return res.finish(res.dig,false);}

/** The Subtract operator.  */
public netrexx.lang.Rexx OpSub(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){netrexx.lang.Rexx newrhs;
if (this.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(this.chars));
if (rhs.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(rhs.chars));
/* carry out the subtraction */
newrhs=new netrexx.lang.Rexx(rhs); // clone
newrhs.ind=(byte)-newrhs.ind; // prepare to subtract
return this.OpAdd(set,newrhs);} // arithmetic

/** The Add operator.  */
public netrexx.lang.Rexx OpAdd(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){netrexx.lang.Rexx lhs;int resdig=0;byte resform=0;netrexx.lang.Rexx res=null;char usel[]=null;char user[]=null;int newlen=0;int i=0;int tlen=0;char t[]=null;int ia=0;int ib=0;int ea=0;int eb=0;char ca=0;char cb=0;char $new[]=null;
lhs=this;
if (lhs.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(lhs.chars));
if (rhs.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(rhs.chars));
if (set==null) {resdig=DefaultDigits;resform=DefaultForm;}
else {resdig=set.digits;resform=set.form;}

/* Quick exit for add 0 -- code below assumes non-zero sign */
if (lhs.ind==0) {
res=new netrexx.lang.Rexx(rhs);res.chars=(char[])null;res.dig=resdig;res.form=resform;
return res.finish(resdig,false);}
if (rhs.ind==0) {
res=new netrexx.lang.Rexx(lhs);res.chars=(char[])null;res.dig=resdig;res.form=resform;
return res.finish(resdig,false);}

/* Prepare numbers according to Rexx rules */
if (lhs.mant.length>(resdig+1)) {lhs=new netrexx.lang.Rexx(lhs);lhs.cut(resdig);}
if (rhs.mant.length>(resdig+1)) {rhs=new netrexx.lang.Rexx(rhs);rhs.cut(resdig);}

res=new netrexx.lang.Rexx(); // build result here
res.dig=resdig;res.form=resform; // record digits & form

/* Now see how much we have to pad or truncate lhs or rhs in order
   to align the numbers.  If one number is much larger than the
   other, then the smaller cannot affect the answer [but we may
   still need to pad with up to DIGITS trailing zeros]. */
{/*select*/
if (lhs.exp==rhs.exp){/* no adjustment needed */
// This is the most common, and fastest, path
usel=lhs.mant;user=rhs.mant;res.exp=lhs.exp;
}
else if (lhs.exp>rhs.exp){ // need to pad lhs and/or truncate rhs
newlen=(lhs.mant.length+lhs.exp)-rhs.exp;
/* if, after pad, lhs would be longer than rhs by >digits then
   rhs cannot affect answer, so we only need to pad up to a
   length of DIGITS+1. */
if (newlen>((rhs.mant.length+resdig)+1)) { // LHS is sufficient
res.mant=lhs.mant;
res.exp=lhs.exp;res.ind=lhs.ind;
if (res.mant.length<resdig) { // need 0 padding
res.mant=new char[resdig];
java.lang.System.arraycopy((java.lang.Object)lhs.mant,0,(java.lang.Object)res.mant,0,lhs.mant.length);
{int $28=resdig-1;i=lhs.mant.length;i:for(;i<=$28;i++){res.mant[i]='0';}}/*i*/
res.exp=res.exp-((resdig-lhs.mant.length));
}
return res.finish(resdig,false);
}
// RHS may affect result
res.exp=rhs.exp; // expected final exponent
if (newlen>(resdig+1)) { // LHS will be max; RHS truncated
tlen=(newlen-resdig)-1; // truncation length
user=new char[rhs.mant.length-tlen];
java.lang.System.arraycopy((java.lang.Object)rhs.mant,0,(java.lang.Object)user,0,user.length);
res.exp=res.exp+tlen;
newlen=resdig+1;
}
else user=rhs.mant;
if (newlen>lhs.mant.length) { // need to pad LHS
usel=new char[newlen];
java.lang.System.arraycopy((java.lang.Object)lhs.mant,0,(java.lang.Object)usel,0,lhs.mant.length);
{int $29=newlen-1;i=lhs.mant.length;i:for(;i<=$29;i++){usel[i]='0';}}/*i*/
}
else usel=lhs.mant;
}
else{ // need to pad rhs and/or truncate lhs
newlen=(rhs.mant.length+rhs.exp)-lhs.exp;
if (newlen>((lhs.mant.length+resdig)+1)) { // RHS is sufficient
res.mant=rhs.mant;
res.exp=rhs.exp;res.ind=rhs.ind;
if (res.mant.length<resdig) { // need 0 padding
res.mant=new char[resdig];
java.lang.System.arraycopy((java.lang.Object)rhs.mant,0,(java.lang.Object)res.mant,0,rhs.mant.length);
{int $30=resdig-1;i=rhs.mant.length;i:for(;i<=$30;i++){res.mant[i]='0';}}/*i*/
res.exp=res.exp-((resdig-rhs.mant.length));
}
return res.finish(resdig,false);
}
// LHS may affect result
res.exp=lhs.exp; // expected final exponent
if (newlen>(resdig+1)) { // RHS will be max; LHS truncated
tlen=(newlen-resdig)-1; // truncation length
usel=new char[lhs.mant.length-tlen];
java.lang.System.arraycopy((java.lang.Object)lhs.mant,0,(java.lang.Object)usel,0,usel.length);
res.exp=res.exp+tlen;
newlen=resdig+1;
}
else usel=lhs.mant;
if (newlen>rhs.mant.length) { // need to pad RHS
user=new char[newlen];
java.lang.System.arraycopy((java.lang.Object)rhs.mant,0,(java.lang.Object)user,0,rhs.mant.length);
{int $31=newlen-1;i=rhs.mant.length;i:for(;i<=$31;i++){user[i]='0';}}/*i*/
}
else user=rhs.mant;}
}

/* OK, we have aligned mantissas.  Now add or subtract. */
if (lhs.ind==rhs.ind) {/* same sign [0 not possible] */
res.mant=charaddsub(usel,user,1);
res.ind=lhs.ind; // true in all cases
return res.finish(resdig,false);
}

/* different signs -- subtraction needed */
/* before we can swap we must determine which is the larger, as our
   add/subtract routine can only handle non-negative results */
res.ind=lhs.ind; // likely sign
if (usel.length>user.length) ; // original A bigger
else if (usel.length<user.length) { // original B bigger
t=usel;usel=user;user=t; // swap
res.ind=(byte)-res.ind;
}
else {/* lengths the same */
/* may still need to swap: compare the strings */
ia=0;ib=0;ea=usel.length-1;eb=user.length-1;
{compare:for(;;){
if (ia<=ea) ca=usel[ia];
else {
if (ib>eb) {
/* identical */
res.mant=new char[1];res.mant[0]='0';res.ind=(byte)0;res.exp=0;
return res;
}
ca='0';
}
if (ib<=eb) cb=user[ib];
else cb='0';
if (ca!=cb) {
if (ca<cb) {/* swap needed */
t=usel;usel=user;user=t; // swap
res.ind=(byte)-res.ind;
}
break compare;}
/* mantissas the same, so far */
ia++;ib++;
}}/*compare*/
} // lengths the same
/* here, A>B, result cannot be 0 until rounded afterwards */
res.mant=charaddsub(usel,user,-1); // subtract
/* round, before removing leading zeros */
if (res.mant.length>resdig) res.round(resdig);
/* Now see if we have to strip leading 0's */
if (res.mant[0]=='0') {
{int $32=res.mant.length;i=1;i:for(;i<=$32;i++){
if (i==res.mant.length) {/* all 0 */
res.mant=new char[1];res.mant[0]='0';res.ind=(byte)0;res.exp=0;
return res;
}
if (res.mant[i]!='0') {
$new=new char[res.mant.length-i];
java.lang.System.arraycopy((java.lang.Object)res.mant,i,(java.lang.Object)$new,0,res.mant.length-i);
res.mant=$new;
break i;}
}}/*i*/
}
return res;}

/** The Multiply operator.  */
public netrexx.lang.Rexx OpMult(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){netrexx.lang.Rexx lhs;int resdig=0;byte resform=0;netrexx.lang.Rexx res;char multer[]=null;char mand[]=null;char acc[];int n=0;int mult=0;char newmand[]=null;
lhs=this;
if (lhs.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(lhs.chars));
if (rhs.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(rhs.chars));
if (set==null) {resdig=DefaultDigits;resform=DefaultForm;}
else {resdig=set.digits;resform=set.form;}
/* Prepare numbers according to Rexx rules */
if (lhs.mant.length>(resdig+1)) {lhs=new netrexx.lang.Rexx(lhs);lhs.cut(resdig);}
if (rhs.mant.length>(resdig+1)) {rhs=new netrexx.lang.Rexx(rhs);rhs.cut(resdig);}

res=new netrexx.lang.Rexx(); // where we'll build result
res.dig=resdig;res.form=resform; // for layout
res.ind=(byte)(lhs.ind*rhs.ind); // final sign
res.exp=lhs.exp+rhs.exp; // initial exponent

/* for best speed, as in DMSRCN, we use the shorter number as the
   multiplier and the longer as the multiplicand */
if (lhs.mant.length<rhs.mant.length) {
multer=lhs.mant;mand=rhs.mant;}
else {
multer=rhs.mant;mand=lhs.mant;}
acc=new char[1];acc[0]='0'; // accumulator

/* now the main long multiplication loop */
{n=multer.length-1;n:for(;n>=0;n--){
mult=((int)(multer[n]))-((int)('0'));
if (mult>0)  // [optimization]
acc=charaddsub(acc,mand,mult); // accumulate
if (n==0) break n; // done
/* multiply multiplicand by 10 */
/* later: can avoid create & copy if charaddsub took length(s) */
newmand=new char[mand.length+1];
java.lang.System.arraycopy((java.lang.Object)mand,0,(java.lang.Object)newmand,0,mand.length);
newmand[mand.length]='0'; // the trailing 0
mand=newmand;
}}/*n*/
res.mant=acc; // prepare the result
return res.finish(resdig,false);}

/** The Divide operator.  */
public netrexx.lang.Rexx OpDiv(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return this.dodivide('D',set,rhs);}

/** The '%' operator.  */
public netrexx.lang.Rexx OpDivI(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return this.dodivide('I',set,rhs);}

/** The '//' operator.  */
public netrexx.lang.Rexx OpRem(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){
return this.dodivide('R',set,rhs);}

/** The '**' operator.  */
public netrexx.lang.Rexx OpPow(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){int d=0;int n;netrexx.lang.Rexx lhs;netrexx.lang.Rexx one;int L=0;netrexx.lang.RexxSet workset=null;netrexx.lang.Rexx res;boolean seenbit;int i=0;
if (this.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(this.chars));
/* Round the rhs to DIGITS as per definition */
if (set==null) d=DefaultDigits;
else d=set.digits;
if (rhs.mant.length>d) {
rhs=new netrexx.lang.Rexx(rhs); // clone ..
rhs.round(d); // .. and round
}
if ((rhs.mant.length+rhs.exp)>d) 
throw new netrexx.lang.BadArgumentException("Too many digits:"+" "+rhs.toString());
n=rhs.intcheck(MinArg,MaxArg);
/* Similarly truncate the lhs to DIGITS+1 if need be */
lhs=this;
if (lhs.mant.length>(d+1)) {lhs=new netrexx.lang.Rexx(lhs);lhs.cut(d);}
one=new netrexx.lang.Rexx(1); // several uses
/* L for precision calculation [see ANSI, and TRL p133] */
if (rhs.exp==0) L=rhs.mant.length; // very common
else L=((new netrexx.lang.Rexx(n)).length()).toint(); // without decimal zeros
if (set==null) workset=new netrexx.lang.RexxSet((DefaultDigits+L)+1);
else workset=new netrexx.lang.RexxSet((set.digits+L)+1,set.form);
res=one; // accumulator
if (n==0) return res; // x**0 == 1
if (n<0) n=(int)-n; // [rhs.ind records the sign]
seenbit=false; // set once we've seen a 1-bit
{i=1;i:for(;;i++){ // for each bit [top bit ignored]
n=n+n; // shift left 1 bit
if (n<0) { // top bit is set
seenbit=true; // OK, we're off
res=res.OpMult(workset,lhs); // acc=acc*x
}
if (i==31) break i; // that was the last bit
if ((!seenbit)) continue i; // we don't have to square 1
res=res.OpMult(workset,res); // acc=acc*acc [square]
}}/*i*/ // 32 bits

if (rhs.ind<0)  // was a **-n
res=one.OpDiv(workset,res); // 1/acc [at longer digits]

/* Final calculation rounds and removes trailing zeros */
return res.OpDiv(set,one);} // acc/1

/* ----- Private methods ----- */

/** DODIVIDE -- carry out division operations
   Arg1 is code: D=divide (/), I=integer divide (%), R=remainder (//)
   Arg2 is the rhs.

   Underlying algorithm (complications for Remainder function are
   omitted for clarity):

     Test for x/0 and then 0/x
     Exp =Exp1 - Exp2
     Exp =Exp +len(var1) -len(var2)
     Sign=Sign1 * Sign2
     Pad accumulator (Var1) to double-length with 0's (pad1)
     Pad Var2 to same length as Var1
     C2B=1st two digits of var2, +1 to allow for roundup
     have=0
     Do until (have=digits+1 OR residue=0)
       if exp<0 then if integer divide/residue then leave
       this_digit=0
       Do forever
          compare numbers
          if <0 then leave inner_loop
          if =0 then do -- quick exit without subtract
             this_digit=this_digit+1; output this_digit
             leave outer_loop; end
          Compare lengths of numbers (mantissae):
          If same then CA=first_digit_of_Var1
                  else CA=first_two_digits_of_Var1
          mult=ca*10/c2b   -- Good and safe guess at divisor
          if mult=0 then mult=1
          this_digit=this_digit+mult
          subtract
          end inner_loop
        if have\=0 | this_digit\=0 then do
          output this_digit
          have=have+1; end
        var2=var2/10
        exp=exp-1
        end outer_loop
     exp=exp+1   -- set the proper exponent
     if have=0 then generate answer=0
     Return to FINISHED
     Result defined by MATHV1

     For extended commentary, see DMSRCN(*)
     */
private final netrexx.lang.Rexx dodivide(char code,netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs) throws java.lang.NumberFormatException,netrexx.lang.DivideException{int resdig=0;byte resform=0;netrexx.lang.Rexx lhs;int newexp;netrexx.lang.Rexx res=null;int newlen;char var1[];int i=0;char var2[];int c2b;int have;int thisdigit=0;int ca=0;int mult=0;int d=0;int start=0;char reduced[]=null;char newvar2[]=null;char newmant[]=null;int padding;char newvar1[]=null;

if (this.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(this.chars));
if (rhs.ind==NotaNum) throw new java.lang.NumberFormatException(java.lang.String.valueOf(rhs.chars));
if (rhs.ind==0) throw new netrexx.lang.DivideException("Divide by 0"); // includes 0/0
if (set==null) {resdig=DefaultDigits;resform=DefaultForm;}
else {resdig=set.digits;resform=set.form;}

lhs=this;
if (lhs.ind==0) return new netrexx.lang.Rexx(0); // all three results must be 0

/* Prepare numbers according to Rexx rules */
if (lhs.mant.length>(resdig+1)) {lhs=new netrexx.lang.Rexx(lhs);lhs.cut(resdig);}
if (rhs.mant.length>(resdig+1)) {rhs=new netrexx.lang.Rexx(rhs);rhs.cut(resdig);}
/* precalculate exponent */
newexp=((lhs.exp-rhs.exp)+lhs.mant.length)-rhs.mant.length;
/* If new exponent -ve, then some quick exits are possible */
if (newexp<0) if (code!='D') {
if (code=='I') return new netrexx.lang.Rexx(0); // no integer part
res=new netrexx.lang.Rexx(lhs); // clone - remainder is input value
res.dig=resdig;res.form=resform;
return res.finish(resdig,false); // don't strip remainder
}

/* We need slow division */
res=new netrexx.lang.Rexx(); // where we'll build result
res.ind=(byte)(lhs.ind*rhs.ind); // final sign (for D/I)
res.exp=newexp; // initial exponent (for D/I)
res.dig=resdig;res.form=resform; // for layout
res.mant=new char[resdig+1]; // where build the result

/* Now pad the mantissae with trailing zeros */
newlen=(resdig+resdig)+1;
var1=new char[newlen];
java.lang.System.arraycopy((java.lang.Object)lhs.mant,0,(java.lang.Object)var1,0,lhs.mant.length);
{int $33=newlen-1;i=lhs.mant.length;i:for(;i<=$33;i++){var1[i]='0';}}/*i*/
var2=new char[newlen];
java.lang.System.arraycopy((java.lang.Object)rhs.mant,0,(java.lang.Object)var2,0,rhs.mant.length);
{int $34=newlen-1;i=rhs.mant.length;i:for(;i<=$34;i++){var2[i]='0';}}/*i*/

/* Calculate first two digits of rhs (var2), +1 for later estimations */
c2b=(((((((int)(var2[0]))-((int)('0'))))*10)+((int)(var2[1])))-((int)('0')))+1;

/* start the long-division loops */
have=0;
{outer:for(;;){
thisdigit=0;
/* find the next digit */
{inner:for(;;){
if (var1.length<var2.length) break inner; // V1 too low
if (var1.length==var2.length) { // compare needed
{compare:do{ // comparison
{int $35=var1.length-1;i=0;i:for(;i<=$35;i++){
if (var1[i]<var2[i]) break inner; // V1 too low
if (var1[i]>var2[i]) break compare; // OK to subtract
}}/*i*/
/* reach here if lhs and rhs are identical; the residue will
   be 0 so we are done, and we don't need the final
   subtraction unless doing remainder (in which case '0' may
   be needed) */
if (code!='R') {
thisdigit++;
res.mant[have]=(char)(thisdigit+((int)('0')));
have++;
break outer;
}}while(false);
}/*compare*/
/* prepare for subtraction.  Estimate CA (lengths the same) */
ca=((int)(var1[0]))-((int)('0')); // use only first digit
} // lengths the same
else {/* lhs longer than rhs */
/* use first two digits for estimate */
ca=((((int)(var1[0]))-((int)('0'))))*10;
if (var1.length>1) ca=(ca+((int)(var1[1])))-((int)('0'));
}
/* subtraction needed; V1>=V2 */
mult=(ca*10)/c2b;if (mult==0) mult=1;
thisdigit=thisdigit+mult;
var1=charaddsub(var1,var2,(int)-mult); // subtract
if (var1[0]!='0') continue inner; // maybe another subtract needed
/* V1 now has leading zeros, remove leading 0's and try again */
/* (It could be longer than V2) */
d=var1.length;
{int $36=d-2;start=0;start:for(;start<=$36;start++){
if (var1[start]!='0') break start;
d--;
}}/*start*/
reduced=new char[d]; // make and copy mantissa
java.lang.System.arraycopy((java.lang.Object)var1,start,(java.lang.Object)reduced,0,d);
var1=reduced;
}}/*inner*/

/* We have the next digit */
if ((have!=0)|(thisdigit!=0)) { // put the digit we got
res.mant[have]=(char)(thisdigit+((int)('0')));
have++;
if (have==(resdig+1)) break outer; // we have all we need
if (var1[0]=='0') break outer; // residue now 0
}
/* can leave now if not Divide and no integer part left  */
if (code!='D') if (res.exp<=0) break outer;
/* to get here, V1 is less than V2, so divide V2 by 10 and go for
   the next digit */
res.exp=res.exp-1; // reduce the exponent
/* later: can avoid create & copy if charaddsub took length(s) */
newvar2=new char[var2.length-1];
java.lang.System.arraycopy((java.lang.Object)var2,0,(java.lang.Object)newvar2,0,var2.length-1);
var2=newvar2;
}}/*outer*/

/* here when we have finished dividing, for some reason */
if ((code=='I')|(code=='R')) {/* check for integer overflow */
if ((have+res.exp)>resdig) throw new netrexx.lang.DivideException("Integer overflow");
}
if (code!='R') {/* Divide or Integer Divide */
if (have==0) return new netrexx.lang.Rexx(0); // got no digits
if (have==res.mant.length) { // got digits+1 digits
res.round(resdig);
have=resdig;
}
/* make the mantissa 'have' long */
newmant=new char[have];
java.lang.System.arraycopy((java.lang.Object)res.mant,0,(java.lang.Object)newmant,0,have);
res.mant=newmant;
return res.finish(resdig,true);
}

/* We were doing Remainder -- return the residue */
if (have==0) { // no integer part was found
/* return lhs, canonical */
res=new netrexx.lang.Rexx(lhs);res.chars=(char[])null;res.dig=resdig;res.form=resform;
return res.finish(resdig,false);}
if (var1[0]=='0') return new netrexx.lang.Rexx(0); // simple 0 residue
res.ind=lhs.ind; // sign is always as LHS
/* Calculate the exponent by subtracting the number of padding zeros
   we added and adding the original exponent */
padding=((resdig+resdig)+1)-lhs.mant.length;
res.exp=(res.exp-padding)+lhs.exp;

/* strip insignificant padding zeros from residue, and create/copy
   the resulting mantissa if need be */
d=var1.length;
{i=d-1;i:for(;i>=1;i--){if(!((res.exp<lhs.exp)&(res.exp<rhs.exp)))break;
if (var1[i]!='0') break i;
d--;
res.exp=res.exp+1;
}}/*i*/
if (d<var1.length) {/* need to reduce */
newvar1=new char[d];
java.lang.System.arraycopy((java.lang.Object)var1,0,(java.lang.Object)newvar1,0,d);
var1=newvar1;
}
res.mant=var1;
return res.finish(resdig,false);}

/** CUT -- cut back a Rexx Mantissa to digits+1
   Arg1 is digits (not digits+1)
 */
private void cut(int d){int adjust;char use[];
adjust=(mant.length-d)-1;
if (adjust<=0) return; // [B&B] nowt to do
exp=exp+adjust;
use=new char[d+1];
java.lang.System.arraycopy((java.lang.Object)mant,0,(java.lang.Object)use,0,d+1);
mant=use;chars=(char[])null; // sign will not change
return;}

/* ROUND -- rounds current Rexx to specified digits */
private void round(int d){int adjust;char use[]=null;char add[];char $new[];char res[];
adjust=mant.length-d;
if (adjust<=0) return; // nowt to do
exp=exp+adjust;
if (adjust>1) {/* truncate */
use=new char[d+1];
java.lang.System.arraycopy((java.lang.Object)mant,0,(java.lang.Object)use,0,d+1);
}
else use=mant;
add=new char[1];add[0]='5';
$new=charaddsub(use,add,1);
if ($new.length>(d+1)) exp++; // had a carry
/* [room for improvement here] */
res=new char[d]; // make the final result ...
java.lang.System.arraycopy((java.lang.Object)$new,0,(java.lang.Object)res,0,d);
mant=res;chars=(char[])null;
return;}

/** CHARADDSUB -- add or subtract two >=0 integers in char arrays
   This routine performs the calculation:

     C=A+(B*M)

   Where M is in the range -9 through +9

   If M<0 then A>=B must be true, so the result is always positive.
   Leading zeros are not removed after a subtraction.  The result is
   either the same length as the longer of A and B, or 1 longer than
   that (if a carry occurred).

   A and B are right-aligned.

   1996.02.20 -- enhanced version of DMSRCN algorithm (1981)

   [Performance can be improved by reuse, etc. -- see MFC's BigDecimal]
 */
private static char[] charaddsub(char a[],char b[],int m){int ap;int bp;int maxarr;char res[];int carry;int op=0;int da=0;char $new[];

/* We'll usually be right if we assume no carry */
ap=a.length-1; // -> final (rightmost) digit
bp=b.length-1; // ..
maxarr=ap;
if (bp>maxarr) maxarr=bp;
res=new char[maxarr+1];
carry=0; // carry or borrow
{op=maxarr;op:for(;op>=0;op--){
da=carry;
if (ap>=0) { // have some A digit
da=(da+((int)(a[ap])))-((int)('0'));
ap--;
}
if (bp>=0) { // have some B digit
da=da+(((((int)(b[bp]))-((int)('0'))))*m);
bp--;
}
/* result could be -82 to 91 */
if (da<0) {
da=da+100; // yes, this is right -- consider -50
carry=(da/10)-10;
res[op]=(char)(((da%10))+((int)('0')));
continue op;}
if (da>9) {
carry=(da/10);
res[op]=(char)(((da%10))+((int)('0')));
continue op;}
/* no carry */
carry=0;
res[op]=(char)(da+((int)('0')));
}}/*op*/
/* say '--->' res carry */
if (carry==0) return res;
// if carry<0 then signal DivideException("internal.error ["a b m carry"]")
/* We have carry -- need to make space for the extra digit */
$new=new char[maxarr+2];
$new[0]=(char)(carry+((int)('0')));
java.lang.System.arraycopy((java.lang.Object)res,0,(java.lang.Object)$new,1,maxarr+1);
return $new;}

/** concatenates two Rexx objects, optionally with blank(s)  */
private netrexx.lang.Rexx concat(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs,int blanks){char res[];int i=0;
set=set;
if (chars==null) chars=layout();
if (rhs.chars==null) rhs.chars=rhs.layout();
res=new char[(chars.length+rhs.chars.length)+blanks];
java.lang.System.arraycopy((java.lang.Object)this.chars,0,(java.lang.Object)res,0,chars.length);
if (blanks>0) 
{int $37=(chars.length+blanks)-1;i=chars.length;i:for(;i<=$37;i++){res[i]=' ';}}/*i*/
java.lang.System.arraycopy((java.lang.Object)rhs.chars,0,(java.lang.Object)res,chars.length+blanks,rhs.chars.length);
return new netrexx.lang.Rexx(res,true);}

/** Make a character array from an array of java.lang.String;
   the array elements are concatenated with a single blank between
   each.  */
private static char[] sa2ca(java.lang.String s[]){int blanks;int len;int i=0;char res[];int out;int seglen=0;
blanks=s.length-1; // number of blanks needed
if (blanks<0) return new char[0]; // empty
len=blanks; // length of result (to be)
{int $38=blanks;i=0;i:for(;i<=$38;i++){len=len+(s[i].length());}}/*i*/
res=new char[len];
out=0; // output offset
{int $39=blanks;i=0;i:for(;i<=$39;i++){
seglen=s[i].length();
s[i].getChars(0,seglen,res,out);
if (i==blanks) break i; // no blank after final
out=(out+seglen)+1;
res[out-1]=' ';
}}/*i*/
return res;}

/** General Rexx compare  */
private final int docompare(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){int thislength=0;int i=0;netrexx.lang.Rexx newrhs=null;netrexx.lang.Rexx res=null;char sl[];char sr[];int il;int ir;int el;int er;char cl=0;char cr=0;
if (this.ind!=NotaNum) 
if (rhs.ind!=NotaNum) {numcompare:do{
/* Numeric compare */
if ((this.ind==rhs.ind)&(this.exp==rhs.exp)) {
/* sign & exponent the same [very common] */
thislength=this.mant.length;
if (thislength<rhs.mant.length) return (byte)-this.ind;
if (thislength>rhs.mant.length) return this.ind;
/* lengths are the same; we can do a straight mantissa compare
   unless maybe rounding [rounding is very unusual] */
dig=DefaultDigits;
if (set!=null) dig=set.digits;
if (thislength<=dig) {
{int $40=thislength-1;i=0;i:for(;i<=$40;i++){
if (this.mant[i]<rhs.mant[i]) return (byte)-this.ind;
if (this.mant[i]>rhs.mant[i]) return this.ind;
}}/*i*/
return 0; // identical
}
/* drop through for full comparison */
}
else {
/* More fastpaths possible */
if (this.ind<rhs.ind) return -1;
if (this.ind>rhs.ind) return 1;
}
/* carry out a subtract to make the comparison */
newrhs=new netrexx.lang.Rexx(rhs); // clone
newrhs.ind=(byte)-newrhs.ind; // prepare to subtract
res=this.OpAdd(set,newrhs); // arithmetic
return res.ind;}while(false);
}/*numcompare*/
/* One of them isn't a number */
if (chars==null) chars=layout();sl=this.chars;
if (rhs.chars==null) rhs.chars=rhs.layout();sr=rhs.chars;
il=0;ir=0;el=sl.length-1;er=sr.length-1;
{el:for(;el>=0;el--){if(!(sl[el]==(' ')))break;}}/*el*/ // skip trailers
{er:for(;er>=0;er--){if(!(sr[er]==(' ')))break;}}/*er*/ // skip trailers
{int $41=el;il=0;il:for(;il<=$41;il++){if(!(sl[il]==(' ')))break;}}/*il*/ // skip leaders
{int $42=er;ir=0;ir:for(;ir<=$42;ir++){if(!(sr[ir]==(' ')))break;}}/*ir*/ // ..
/* Now the real comparison */

{$43:for(;;){
if (il<=el) cl=sl[il];else cl=' '; // pad with blanks on right as
if (ir<=er) cr=sr[ir];else cr=' '; // .. per TRL (else matches '\0')
if (cr==cl) {
if (cr==(' ')) if (il>el) if (ir>er) break $43;
il++;ir++;continue $43;
}
/* differ (exactly) but maybe not caseless */
cl=java.lang.Character.toLowerCase(cl);
cr=java.lang.Character.toLowerCase(cr);
if (cr!=cl) {
if (cl<cr) return -1;
return 1;
}
/* cr=cl */
il++;ir++;
}}
return 0;}

/** Strict Rexx compare  */
private final int docomparestrict(netrexx.lang.RexxSet set,netrexx.lang.Rexx rhs){char sl[];char sr[];int il;int ir;int el;int er;char cl=0;char cr=0;
if (this.chars==null) this.chars=this.layout();sl=this.chars;
if (rhs.chars==null) rhs.chars=rhs.layout();sr=rhs.chars;
il=0;ir=0;el=sl.length-1;er=sr.length-1;
{$44:for(;;){
if (il<=el) cl=sl[il];
else {
if (ir>er) break $44;
cl='\000';
}
if (ir<=er) cr=sr[ir];
else cr='\000';
if (cr!=cl) {
if (cl<cr) return -1;
return 1;
}
/* characters the same */
il++;ir++;
}}
set=set;
return 0;}

/** Compare (normalized) mantissas.
          Arg1 is lhs
          Arg2 is rhs
          returns -1, 0, 1
    */
/** not used
method mantcompare(lhs=char[], rhs=char[]) static private returns int
  if lhs.length<rhs.length then return -1
  if lhs.length>rhs.length then return  1
  (- lengths the same -)
  loop i=0 to lhs.length-1
    if lhs[i]<rhs[i] then return -1
    if lhs[i]>rhs[i] then return  1
    end i
  return 0   -- really are =
**/

/** Round (if need be) then check for 0 mantissa
          returns this rounded, unchanged, or new 0 object if needed
          Arg1 is length to round to
          Arg2 is whether trailing insignificant zeros should be
                  removed after round
    */
private final netrexx.lang.Rexx finish(int roundlen,boolean strip){int d=0;int i=0;char newmant[]=null;int mag=0;int sig=0;
if (this.mant.length>roundlen) this.round(roundlen);

/* If strip requested, remove insignificant trailing zeros */
if (strip) {
d=mant.length;
/* see if we need to drop any trailing zeros */
{i=d-1;i:for(;i>=1;i--){
if (mant[i]!='0') break i;
d--;
exp++;
}}/*i*/
if (d<mant.length) {/* need to reduce */
newmant=new char[d];
java.lang.System.arraycopy((java.lang.Object)mant,0,(java.lang.Object)newmant,0,d);
mant=newmant;
}
}
/* now check for all zeros in mantissa */
{int $45=this.mant.length-1;i=0;i:for(;i<=$45;i++){
if (this.mant[i]!='0') {/* non-0 result */
/* check for overflow */
mag=(exp+mant.length)-1;
if ((mag<MinExp)|(mag>MaxExp)) {overflow:do{
// possible reprieve if form is engineering
if (form==netrexx.lang.RexxSet.ENGINEERING) {
sig=mag%3; // leftover
if (sig<0) sig=3+sig; // negative exponent
mag=mag-sig; // exponent to use
if (mag>=MinExp) if (mag<=MinExp) break overflow;
}
throw new netrexx.lang.ExponentOverflowException(java.lang.String.valueOf(mag));}while(false);
}/*overflow*/
return this;
}
}}/*i*/
/* mantissa is all zeros */
if (this.mant.length==1) 
if (this.ind==0) 
if (this.exp==0) return this;
return new netrexx.lang.Rexx(0);}}
