/* Build the NetRexx/Venta processor classes.           */
/*                                                      */
/* Special options:                                     */
/*   -from name .. build starting at named item         */
/*                                                      */
/*   -xxxx flags are passed through to NetRexxC         */
/*   =xxxx flags are passed through to java.exe with =  */
/*         changed to -                                 */

/* There are several categories of classes, here:

  bases:    depend only on netrexx.lang and java. classes

  middles:  depend on bases

  clauses:  classes for individual instruction types

  commands: command shells

  temps: temporary classes
*/
-- 2004.07.04 javahome can now include blanks

from     =0         /* not a 'from' request */
together =0         /* parallel compilation */

bases   ='RxProcessor RxFlag RxToken RxType RxException',
         'RxClauseParser RxChunk RxCode RxTermParser RxExprParser RxExpr',
         'RxClause RxArray RxField RxConvert',
         'RxVariable RxClassInfo RxBabel RxSignal RxSignalPend',
         'RxMessage RxQuit RxError RxWarn RxSource'
makers  ='RxClauser RxFileReader RxStreamer RxClasser RxParser RxTracer',
         'RxVarpool RxConverter RxFixup RxClassImage',
         'RxClassPool RxCursor RxInterpreter RxProxy RxProxyLoader'
makers  =makers 'NrLevel NrBlock'
middles ='RxProgram RxTranslator'
middles =middles 'NrBabel BxBabel'
supercs ='RxClass RxMethod'
                        /* NrClass */
clauses ='NrAssign NrCatch         NrDo NrElse NrEnd NrExit NrFinally',
         'NrIf NrImport NrIterate NrLeave NrLoop',
         'NrMethod NrMethodcall NrNop NrNumeric NrOptions NrOtherwise',
         'NrPackage NrParse NrProperties NrReturn NrSay NrSelect NrSignal',
         'NrThen NrTrace NrWhen'
external='NetRexxA          NetRexxC'  /* NetRexxT removed 1997.04.20 */
                                       /* NetRexxG removed 2004.07.04 */

namelist=bases makers middles supercs clauses external


/* ----------------------------------------------------------------- */
parse arg names

w=wordpos('-from', names)
if w>0 then do
  fromname=word(names, w+1)
  names=delword(names,w,2)
  from=1; end

/* Move flags from the input string to the options string */
opts=''
jopts=''
do w=words(names) to 1 by -1       /* from right */
  try=word(names,w)
  if left(try,1)='-' then do
    opts=try opts
    names=delword(names,w,1)
    end
  if left(try,1)='=' then do
    jopts='-'substr(try,2) jopts
    names=delword(names,w,1)
    end
  end w
opts='-nodiag -keep' opts          /* -time */


if names='' then /* everything */ do
  names=namelist
  if from then do
    cut=wordpos(translate(fromname), translate(names))
    if cut=0 then do; say '-from "'fromname'" not found'; exit 1; end
    names=subword(names, cut)
    end
  opts=opts '-notrace'        /* just in case... */
  end
 else /* just one expected */ do
  if from then do; say 'Unexpected name(s) after -from'; exit 1; end
  ins=names; names=''
  together=1
  added=0
  do while ins\=''
    parse upper var ins name ins
    if left(name, 1)=='-' then do; say 'Unexpected flag:' name; exit 1; end
    s=lastpos('\', name); name=substr(name, s+1)  /* drop path */
    parse var name name '.'                       /* drop ext  */
    n=wordpos(name, translate(namelist))
    if n=0 then do; say 'Unknown name' name; exit 1; end
    name=word(namelist, n)                        /* actual spelling */
    if name='NrBabel' then if \added then do
      add='RxProcessor RxTranslator RxTermParser NetRexxC'
      say 'Adding dependents:' add
      ins=ins add
      /* together=1  -- no, cannot do in parallel to pick up constants */
      together=0
      added=1
      end
    if name='RxProcessor' then if \added then do
      add='NrBabel RxTranslator RxTermParser NetRexxC'
      say 'Adding dependents:' add
      ins=ins add
      /* together=1  -- no, cannot do in parallel to pick up constants */
      together=0
      added=1
      end
    names=names name
    end
  names=space(names)
  end

max='24M'
if words(names)>1 then max='32M'
jopts='-ms16M -mx'max jopts

javahome=javahome()
/**
javahome='c:\jdk1.1.3'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then javahome='f:\java11'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then javahome='h:\java11'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then do
  say 'Cannot find java home'; exit 1; end
**/

javaclasses=javahome'\classes'
javadisk=left(javahome, 2)
'@cls'
say 'Classes:' javaclasses

/* ---- ensure target class directories exist ---- */
here=directory()
parse var here heredisk ':' herepath
parse var javaclasses javadisk ':' javapath

call directory javaclasses
dir='COM'
if stream(dir, 'c', 'query datetime')='' then '@md' dir
call directory dir

dir='ibm'
if stream(dir, 'c', 'query datetime')='' then '@md' dir
call directory dir

dir='netrexx'
if stream(dir, 'c', 'query datetime')='' then '@md' dir
call directory dir

dir='process'
if stream(dir, 'c', 'query datetime')='' then '@md' dir

call directory here

/* ---- off we go... ---- */
parse source os .
if os='OS/2' then jopts=jopts '-norestart'

startsecs=time('s')
call qerase '*.java'
call qerase '*.class'
files=words(names)
if together then count=1
            else count=files
/* if files>1 then say 'Compiling' files 'files...' */
do count
  parse var names name names
  package=getpacksub(name)    /* find the package */
  if together then filelist=name names
              else filelist=name
  say 'Compile:' filelist'...'

  /* if words(filelist)=1 then filelist=filelist'.nrx' */

  call time 'r'
  '@java' jopts 'COM.ibm.netrexx.process.NetRexxC' opts filelist '| tee' name'.stdout'
  elap=time('e')
  say '...' elap/1 'elapsed'
  /* T currently hides the RC */
  if rc>1 then do
    say '+++ Compilation failed (rc='rc') +++' filelist
    exit rc
    end

  target='*.class'
  if stream(target, 'c', 'query exists')='' then exit 2
  parse var package 'netrexx.' subdir
  targdir=javaclasses'\COM\ibm\netrexx\'subdir
  '@copy' target '"'targdir'"' '>nul'
  if rc<>0 then do
    say 'Copy failed ('target targdir')'
    exit rc
    end
  say target 'copied to' targdir
  call qerase target
  end

stopsecs=time('s'); elap=stopsecs-startsecs
if elap<0 then elap=elap+86400  /* better be done in a day */
mins=format(elap/60,,1)/1

/* Copy the error messages file */
targdir=javaclasses'\COM\ibm\netrexx\process\'
'@copy nrc.prp' targdir'NetRexxC.properties >nul'

say '--- All OK --- ['mins 'minutes]'
exit 0


/* GETPACKSUB -- return subdirectory (from package clause) */
getpacksub: procedure
  parse arg name
  in=name'.nrx'

  try=200; pack=''
  do try
    line=linein(in)
    parse var line pre 'package ' pack post
    if pack<>'' then if pre post=' ' then leave
    end
  call lineout in
  /* say '# package' pack */

  if pack<>'' then return pack
  say "No package instruction line found in '"in"', first" try "lines"
  exit 8

/* Quiet erase */
qerase: procedure
 parse source os .
 if pos(95,os)=0 then do
   '@erase' arg(1) '1>nul 2>nul'
   return
   end
 /* Win95 doesn't have stderr redirection, or the /f flag */
 parse arg spec
 p=lastpos('\', spec)
 if p=0 then dir=''
  else do
   p=p+1
   parse var spec dir =(p) spec
   end
 'dir' spec '/b | rxqueue'
 do queued()
   parse pull file
   call sysfiledelete dir||file
   end
 return

/* JAVAHOME -- return the path to the Java home directory */
javahome:
  parse source os .
  if os='OS/2' then env='OS2ENVIRONMENT'
               else env='ENVIRONMENT'
  path=value('PATH',,env)
  javaexe='javac.exe'
  do while path<>''
    parse var path dir';' path
    if right(dir, 1)<>'\' then dir=dir'\'
    exe=dir||javaexe
    if stream(exe, 'c', 'query datetime')<>'' then do
      /* say 'found' exe */
      home=left(dir, lastpos('\', dir, length(dir)-1)-1)
      return  home
      end
    end
  call error 0, 'java.exe.not.found', 'java.exe needs to be on PATH'
