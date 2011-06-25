/* Build the netrexx.lang classes                      */
/*                                                     */
/* To bootstrap, remove the 'implements RexxOperators' */
/* from the Rexx class for first compilation round.    */
/* See also note in RexxNode.                          */
/*                                                     */
/* Special options:                                    */
/*   -new       .. build with the NetRexx translator   */
/*   -os2       .. build with the OS/2 Rexx translator */
/*   -diag      .. build with the -diag flag           */
/*   -test      .. build with the -test flag           */
/*   -prof      .. build with the -prof flag           */
/*   -nocopy    .. don't copy to main classes          */
/*   -debug     .. build using java_g                  */
/*   -trace     .. defeat notrace                      */
/*   -nodecimal .. build with -nodecimal               */
opts='-nodiag -keep -time'
jopts='-ms8M -mx12M' /* -nojit' */      /* for compile */

os2=0          /* set to 1 to use OS/2 translator */
copy=1         /* do copy */
debug=0        /* use java_g */
trace=0        /* use notrace */
nodecimal=0    /* specify NODECIMAL */

namelist='NotCharacterException BadArgumentException',
         'ExponentOverflowException DivideException',
         'NotLogicException NoOtherwiseException',
         'BadColumnException BadNumericException',
         'RexxWords RexxParse RexxIO RexxUtil',  /* helpers */
         'RexxNode',
         'RexxSet RexxTrace RexxOperators Rexx'

parse arg names
w=wordpos('-bigger', names)
if w>0 then do; jopts='-ms8M -mx16M'; names=delword(names,w,1); end
w=wordpos('-os2', names)
if w>0 then do; os2=1; names=delword(names,w,1); end
w=wordpos('-new', names)
if w>0 then do; os2=0; names=delword(names,w,1); end
w=wordpos('-debug', names)
if w>0 then do; debug=1; names=delword(names,w,1); end
w=wordpos('-trace', names)
if w>0 then do; trace=1; names=delword(names,w,1); end
w=wordpos('-nodecimal', names)
if w>0 then do; nodecimal=1; names=delword(names,w,1); end
w=wordpos('-diag', names)
if w>0 then do; opts=opts '-diag'; names=delword(names,w,1); end
w=wordpos('-test', names)
if w>0 then do; opts=opts '-test'; names=delword(names,w,1); end
w=wordpos('-verbose5', names)
if w>0 then do; opts=opts '-verbose5'; names=delword(names,w,1); end
w=wordpos('-comments', names)
if w>0 then do; opts=opts '-comments'; names=delword(names,w,1); end
w=wordpos('-nocrossref', names)
if w>0 then do; opts=opts '-nocrossref'; names=delword(names,w,1); end
w=wordpos('-prof', names)
if w>0 then do; jopts=jopts '-prof'; names=delword(names,w,1); end
w=wordpos('-nocopy', names)
if w>0 then do; copy=0; names=delword(names,w,1); end; else copy=1
if \trace then opts=opts '-notrace'
if nodecimal then opts=opts '-nodecimal'


if names='' then /* everything */ do
  names=namelist
  end
 else /* just one expected */ do
  parse upper var names name rest
  if rest<>'' then do; say 'Just one name expected; extra:' rest; exit; end
  s=lastpos('\', name); name=substr(name, s+1)  /* drop path */
  parse var name name '.'                       /* drop ext  */
  n=wordpos(name, translate(namelist))
  if n=0 then do; say 'Unknown name' name; exit; end
  names=word(namelist, n)
  end

'@echo off'; 'cls'

javahome=javahome()
/**
javahome='c:\jdk1.1.3'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then javahome='c:\java11'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then javahome='f:\java11'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then do
  say 'Cannot find java home'; exit 1; end
**/

javaclasses=javahome'\classes'
say 'Classes:' javaclasses

/* ---- ensure target class directories exist ---- */
here=directory()
parse var here heredisk ':' herepath
parse var javaclasses javadisk ':' javapath

call directory javaclasses
dir='netrexx'
if stream(dir, 'c', 'query datetime')='' then '@md' dir
call directory dir

dir='lang'
if stream(dir, 'c', 'query datetime')='' then '@md' dir

call directory here

/* ---- off we go... ---- */
if debug then javaexe='java_g'; else javaexe='java'
parse source os .
if os='OS/2' then jopts=jopts '-norestart'

startsecs=time('s')
call qerase '*.java'
call qerase '*.class'
do words(names)
  parse var names name names
  say name'...'
  call time 'r'
  if os2 then 'call \nr\netrexxcos2.cmd' opts name '| tee' name'.stdout'
         else javaexe jopts 'COM.ibm.netrexx.process.NetRexxC' opts name '| tee' name'.stdout'
  elap=time('e')
  say '...' elap/1 'elapsed'
  if rc\=0 then exit rc       /* no effect .. TEE loses it */
  target=name'.class'
  if stream(target, 'c', 'query exists')='' then exit 2  /* had error */
  targdir=javaclasses'\netrexx\lang'
  if copy then do
    'copy' target targdir '>nul'
    if rc<>0 then exit rc
    say target 'copied to' targdir
    'erase' target
    end
   else say '(not copied to' targdir')'
  end
stopsecs=time('s'); elap=stopsecs-startsecs
if elap<0 then elap=elap+86400  /* better be done in a day */
mins=format(elap/60,,1)/1

say '--- All OK --- ['mins 'minutes]' '07'x
exit 0

/**
directory: procedure
 tfile='$'random(1000)'.$$$'
 'cd >' tfile
 res=linein(tfile)
 call lineout tfile
 'erase' tfile
 return res
 **/

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
   '@erase' dir||file
   end
 return
