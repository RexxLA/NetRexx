/* Generated from 'RexxTrace.nrx' 11 Jun 2011 03:25:36 [v3.00] */
/* Options: Binary Comments Compact Crossref Format Java Logo Replace Sourcedir Strictargs Trace2 Verbose3 */
package netrexx.lang;

/* IBM Materials Licensed under International Components for Unicode  */
/* Licence version 1.8.1 (ICU Licence) - Property of IBM              */
/* IBM NetRexx                                                        */
/* Copyright (c) 1995-2009 IBM Corp.                                  */
/* Copyright (c) 2011- RexxLA                                         */
/* ------------------------------------------------------------------ */
/* netrexx.lang.RexxTrace -- Trace object and helper functions        */
/* ------------------------------------------------------------------ */
/* Copyright IBM Corporation, 1996, 2000.  All Rights Reserved.       */
/* Author    Mike Cowlishaw                                           */
/*                                                                    */
/* ------------------------------------------------------------------ */
/* 96.07.29 Initial version                                           */
/* 98.05.21 Support Trace VAR [array of active variables]             */
/* 98.05.23 Context switch tracing added                              */
/* 00.05.25 Add reset for command -prompt use                         */
/* 00.08.24 'A' tracetag used to indicate method argument trace       */



/**
   This defines trace settings for NetRexx programs.
   A RexxTrace object is passed to RexxTrace helper functions, and
   is manipulated/modified by TRACE instructions.
    */


public final class RexxTrace{
 private static final netrexx.lang.Rexx $01=netrexx.lang.Rexx.toRexx("*=*");
 private static final netrexx.lang.Rexx $02=new netrexx.lang.Rexx(' ');
 private static final netrexx.lang.Rexx $03=netrexx.lang.Rexx.toRexx("*-*");
 private static final netrexx.lang.Rexx $04=new netrexx.lang.Rexx('>');
 private static final netrexx.lang.Rexx $05=new netrexx.lang.Rexx('\"');
 private static final netrexx.lang.Rexx $06=netrexx.lang.Rexx.toRexx("---");
 private static final java.lang.String $0="RexxTrace.nrx";
 
 /* ----- Properties ----- */
 /* properties public constant */
 /* Tracing levels */
 public static final int OFF=0;
 public static final int VAR=1;
 public static final int METHODS=2;
 public static final int ALL=3;
 public static final int RESULTS=4;
 public static final int TERMS=5;
 public static final int OPS=6;
 
 /* properties private constant */
 private static final netrexx.lang.Rexx levelwords=netrexx.lang.Rexx.toRexx("off var methods all results"); // "terms operations"
 private static final java.lang.String nulltrace="[null]";
 
 /* properties private */
 private int level=OFF; // the current tracing level
 private boolean context=true; // show context changes
 private int width; // width of maximum linenumber in the program
 private int lastline=0; // last linenumber traced using this object
 private java.lang.String programname; // name of program that we belong to
 private java.io.PrintStream tracestream; // the output stream to use
 private java.lang.String varnames[]=new java.lang.String[0]; // variables currently being traced [may
 // be 0 but not null; names in lower case]
 
 /* properties private static */ // global context
 private static java.lang.Thread lastthread=(java.lang.Thread)null; // last thread to trace
 private static java.lang.String lastprogramname=""; // last program to trace
 private static java.lang.Object threadlock=new java.lang.Object();
 // capability token
 
 /* ----- Constructors ----- */
 /** Build a default Trace settings context
    Arg1 is number of lines in source program
    Arg2 is the stream to use for output
    Arg3 is the name of the program constructing the tracer
    */
 
 public RexxTrace(int lines){
  this(lines,java.lang.System.err,"?");return;
  }
 public RexxTrace(int lines,java.io.PrintStream stream){
  this(lines,stream,"?");return;
  }
 public RexxTrace(int lines,java.io.PrintStream stream,java.lang.String name){super();
  width=java.lang.String.valueOf(lines).length();
  tracestream=stream;
  programname=name;
  return;}

 /**  Clone a Trace settings context  */
 // Note the varnames themselves don't have to be copied, as any
 // modification makes a new array
 
 public RexxTrace(netrexx.lang.RexxTrace old){super();
  level=old.level;
  width=old.width;
  lastline=old.lastline;
  tracestream=old.tracestream;
  programname=old.programname;
  varnames=old.varnames;
  context=old.context;
  return;}

 /* ----- Utility methods ----- */
 /** Reset the static information [used during prompt loop] */
 
 public static void reset(){
  lastthread=(java.lang.Thread)null;
  lastprogramname="";
  return;
  }

 /* ----- Value methods ----- */
 /** Return a word for the trace level */
 
 public netrexx.lang.Rexx levelword(){
  return levelwords.word(new netrexx.lang.Rexx(level+1));
  }

 /** Set a new level; OFF resets the 'last line' count */
 
 public void newlevel(int $new){
  level=$new;
  if (level==OFF) 
   lastline=0;
  return;}

 /** Get/Set the Context property */
 
 public boolean getContext(){
  return context;
  }
 
 public void setContext(boolean b){
  context=b;
  return;}

 /** Add and/or remove names to the array of variables being traced
    Arg1 is array of lowercase names to add to the list
    Arg2 is array of lowercase names to remove from the list
    Either array may be null or empty (or both may be)
    A name may not be in both lists
    Not an error if adds duplicate olds, or removes not found.
    Side-effect: this implies TRACE VAR: OFF is bumped to VAR.
    */
 // This always makes a new array
 
 public void modnames(java.lang.String addnames[],java.lang.String subnames[]){
  java.util.Vector work;
  int a=0;
  int v=0;
  java.lang.String name=null;
  int s=0;
  if (level==OFF) 
   level=VAR;
  if (addnames==null) 
   addnames=new java.lang.String[0];
  if (subnames==null) 
   subnames=new java.lang.String[0];
  work=new java.util.Vector(varnames.length+addnames.length); // always enough
  // put the addnames in first, as they are likely to be referenced
  {int $1=addnames.length;a=0;a:for(;$1>0;$1--,a++){
   work.addElement((java.lang.Object)addnames[a]);
   }
  }/*a*/
  // add the old names if not in either list
  {int $2=varnames.length;v=0;v:for(;$2>0;$2--,v++){
   name=varnames[v];
   {int $3=addnames.length;a=0;a:for(;$3>0;$3--,a++){
    if (addnames[a].equals(name)) 
     continue v;
    }
   }/*a*/
   {int $4=subnames.length;s=0;s:for(;$4>0;$4--,s++){
    if (subnames[s].equals(name)) 
     continue v;
    }
   }/*s*/
   work.addElement((java.lang.Object)name);
   }
  }/*v*/
  // and finally make the new array
  varnames=new java.lang.String[work.size()];
  work.copyInto((java.lang.Object[])varnames);
  return;}

 /* ---------------------------------------------------------------- */
 /* Tracer methods (actually produce trace output)                   */
 /* ---------------------------------------------------------------- */
 
 /** traceclause -- Trace a clause
    Arg1 is line number of this clause
    Arg2 is the sourceline segments for this clause, or a single
            clause segment
    Arg3 is the normal trace level for this clause (ALL assumed)
    Arg4 is the list of variables changed (assigned) in this clause
            (lower case names), or null if none.
    The clause is traced if the current level is tracelevel or higher,
    unless the tracelevel is VAR and level is <ALL.  In this case, one
    or more variables are assigned and unless none is in the varnames
    list the clause is traced.
    */
 
 public void traceclause(int lineno,java.lang.String line){
  traceclause(lineno,line,ALL,(java.lang.String[])null);return;
  }
 public void traceclause(int lineno,java.lang.String line,int tracelevel){
  traceclause(lineno,line,tracelevel,(java.lang.String[])null);return;
  }
 public void traceclause(int lineno,java.lang.String line,int tracelevel,java.lang.String names[]){
  java.lang.String lines[];
  
  // this entrypoint accepts a single string for the lines array
  lines=new java.lang.String[1];
  lines[0]=line;
  traceclause(lineno,lines,tracelevel,names);
  return;
  }

 
 public void traceclause(int lineno,java.lang.String lines[]){
  traceclause(lineno,lines,ALL,(java.lang.String[])null);return;
  }
 public void traceclause(int lineno,java.lang.String lines[],int tracelevel){
  traceclause(lineno,lines,tracelevel,(java.lang.String[])null);return;
  }
 public void traceclause(int lineno,java.lang.String lines[],int tracelevel,java.lang.String names[]){
  int n=0;
  java.lang.String name=null;
  int v=0;
  netrexx.lang.Rexx prefix=null;
  int i=0;
  
  if (level<tracelevel) 
   return; // just too low
  if (tracelevel==VAR) 
   if (level<ALL) 
    {mayleave:do{
     if (names!=null) 
      {int $5=names.length;n=0;n:for(;$5>0;$5--,n++){
       name=names[n];
       {int $6=varnames.length;v=0;v:for(;$6>0;$6--,v++){
        if (varnames[v].equals(name)) 
         break mayleave;
        }
       }/*v*/
       }
      }/*n*/
     return; // var but we didn't match
    }while(false);}/*mayleave*/
  
  /* Clause will be traced.  Lock while we do it so context correct */
  synchronized(threadlock){{
   if (context) 
    showContext();
   if (lastline==lineno) 
    prefix=new netrexx.lang.Rexx(' ').copies(new netrexx.lang.Rexx(width));
   else 
    {
     prefix=(new netrexx.lang.Rexx(lineno)).right(new netrexx.lang.Rexx(width));
     lastline=(lineno+lines.length)-1;
    }
   prefix=prefix.OpCcblank(null,$01);
   {int $7=lines.length-1;i=0;i:for(;i<=$7;i++){
    tracestream.println((java.lang.Object)(($02.OpCc(null,prefix)).OpCcblank(null,netrexx.lang.Rexx.toRexx(lines[i]))));
    prefix=((new netrexx.lang.Rexx((lineno+i)+1)).right(new netrexx.lang.Rexx(width))).OpCcblank(null,$03);
    }
   }/*i*/
  }}
  return;}

 /** traceresult -- data string trace (Shared by type-dependent methods)
    Arg1 is line number of this clause
    Arg2 is the data (string) to be traced
    Arg3 is the tag character for the trace
    Arg4 is the name (actual case) of the variable this result will be
            assigned to, or '' (not null) if none
 
    The data are traced if:
     -- the current level is RESULTS or higher, or
     -- the current level is METHODS or higher and the tracetag is the
        method argument indicator, 'm', or
     -- the current level is VAR or higher, and one or more variables
        are assigned and one is in the varnames list
 
    */
 
 private void traceresult(int lineno,netrexx.lang.Rexx data,char tag,java.lang.String name){
  java.lang.String lowname=null;
  int v=0;
  netrexx.lang.Rexx prefix=null;
  char cdata[]=null;
  int i=0;
  if (level<RESULTS) 
   {mayquit:do{
    if (level>=METHODS) 
     if (tag=='m') 
      break mayquit;
    if (level<VAR) 
     return; // lower than VAR
    // must be VAR or higher
    lowname=name.toLowerCase();
    {int $8=varnames.length;v=0;v:for(;$8>0;$8--,v++){
     if (varnames[v].equals(lowname)) 
      break mayquit;
     }
    }/*v*/
    return; // no reason to trace
   }while(false);}/*mayquit*/
  
  if (tag=='m') 
   tag='a'; // as per NRL
  
  /* String will be traced.  Lock while we do it so context correct */
  synchronized(threadlock){{
   if (context) 
    showContext();
   if (lastline==lineno) 
    prefix=new netrexx.lang.Rexx(' ').copies(new netrexx.lang.Rexx(width));
   else 
    {
     prefix=(new netrexx.lang.Rexx(lineno)).right(new netrexx.lang.Rexx(width));
     lastline=lineno;
    }
   prefix=((prefix.OpCcblank(null,$04)).OpCc(null,new netrexx.lang.Rexx(tag))).OpCc(null,$04); // add tag
   if (!name.equals("")) 
    prefix=prefix.OpCcblank(null,netrexx.lang.Rexx.toRexx(name)); // add name (if any)
   /* Sanitize the data */
   if (data==null) 
    tracestream.println((java.lang.Object)(($02.OpCc(null,prefix)).OpCcblank(null,netrexx.lang.Rexx.toRexx(nulltrace))));
   else 
    {nonnull:do{
     cdata=netrexx.lang.Rexx.tochararray(data);
     {int $9=cdata.length-1;i=0;i:for(;i<=$9;i++){
      if (cdata[i]<(' ')) 
       {
        if (cdata[i]==('\t')) 
         cdata[i]=' ';
        else 
         cdata[i]='?';
       }
      }
     }/*i*/
     tracestream.println((java.lang.Object)(((($02.OpCc(null,prefix)).OpCcblank(null,$05)).OpCc(null,netrexx.lang.Rexx.toRexx(cdata))).OpCc(null,$05)));
    }while(false);}/*nonnull*/
  }}
  return;}

 /** Produce a separator line if the context has changed */
 // this should only be called if the capability token is protected
 
 public void showContext(){
  java.lang.Thread thisthread;
  netrexx.lang.Rexx prefix;
  java.lang.String tname;
  java.lang.String gname;
  thisthread=java.lang.Thread.currentThread();
  if (lastthread==thisthread) 
   if (lastprogramname.equals(programname)) 
    return; // context unchanged
  // build and display separator
  prefix=new netrexx.lang.Rexx(' ').copies(new netrexx.lang.Rexx(width)).OpCcblank(null,$06);
  tname=thisthread.getName();
  gname=thisthread.getThreadGroup().getName();
  if (!gname.equals("main")) 
   tname=tname+","+gname;
  if (tname.equals("main")) 
   tname="";
  else 
   tname=" ["+tname+"]";
  tracestream.println((java.lang.Object)((($02.OpCc(null,prefix)).OpCcblank(null,netrexx.lang.Rexx.toRexx(programname))).OpCc(null,netrexx.lang.Rexx.toRexx(tname))));
  lastthread=thisthread; // new context
  lastprogramname=programname; // ..
  lastline=0; // force line number
  return;}

 /* ---------------------------------------------------------------- */
 /* Trace various types of data                                      */
 /* Arguments, etc., as for traceresult.                             */
 /* ---------------------------------------------------------------- */
 
 /*** Trace a char, and return it  */
 
 public char tracechar(int lineno,char data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,new netrexx.lang.Rexx(data),tag,name);
  return data;
  }

 /**  Trace a boolean, and return it  */
 
 public boolean traceboolean(int lineno,boolean data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,new netrexx.lang.Rexx(data),tag,name);
  return data;
  }

 /**  Trace a byte, and return it  */
 
 public byte tracebyte(int lineno,byte data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,new netrexx.lang.Rexx(data),tag,name);
  return data;
  }

 /**  Trace a short, and return it  */
 
 public short traceshort(int lineno,short data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,new netrexx.lang.Rexx(data),tag,name);
  return data;
  }

 /**  Trace a int, and return it  */
 
 public int traceint(int lineno,int data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,new netrexx.lang.Rexx(data),tag,name);
  return data;
  }

 /**  Trace a long, and return it  */
 
 public long tracelong(int lineno,long data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,new netrexx.lang.Rexx(data),tag,name);
  return data;
  }

 /**  Trace a float, and return it  */
 
 public float tracefloat(int lineno,float data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,new netrexx.lang.Rexx(data),tag,name);
  return data;
  }

 /**  Trace a double, and return it  */
 
 public double tracedouble(int lineno,double data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,new netrexx.lang.Rexx(data),tag,name);
  return data;
  }

 /**  Trace a Rexx string, and return it  */
 
 public netrexx.lang.Rexx traceRexx(int lineno,netrexx.lang.Rexx data,char tag,int tracelevel,java.lang.String name){
  
  if (level<tracelevel) 
   return data; // fastpath
  traceresult(lineno,data,tag,name);
  return data;
  }

 /**  Trace a String, and return it  */
 
 public java.lang.String traceString(int lineno,java.lang.String data,char tag,int tracelevel,java.lang.String name){
  netrexx.lang.Rexx tdata=null;
  
  if (level<tracelevel) 
   return data; // fastpath
  if (data==null) 
   tdata=(netrexx.lang.Rexx)null;
  else 
   tdata=new netrexx.lang.Rexx(data);
  traceresult(lineno,tdata,tag,name);
  return data;
  }

 /**  Trace a char[], and return it  */
 
 public char[] tracechararray(int lineno,char data[],char tag,int tracelevel,java.lang.String name){
  netrexx.lang.Rexx tdata=null;
  
  if (level<tracelevel) 
   return data; // fastpath
  if (data==null) 
   tdata=(netrexx.lang.Rexx)null;
  else 
   tdata=new netrexx.lang.Rexx(data);
  traceresult(lineno,tdata,tag,name);
  return data;
  }

 /**  Trace an arbitrary Object, and return it  */
 
 public java.lang.Object traceObject(int lineno,java.lang.Object data,char tag,int tracelevel,java.lang.String name){
  netrexx.lang.Rexx tdata=null;
  
  if (level<tracelevel) 
   return data; // fastpath
  if (data==null) 
   tdata=(netrexx.lang.Rexx)null;
  else 
   tdata=new netrexx.lang.Rexx(data.toString());
  traceresult(lineno,tdata,tag,name);
  return data;
  }
 }
