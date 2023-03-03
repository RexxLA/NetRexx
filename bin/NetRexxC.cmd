/* Translate and compile a NetRexx program             */
/*                                                     */
/* use as:  NetRexxC hello [file2]...                  */
/*                                                     */
/*   which will use the NetRexx translator to          */
/*   translate hello.nrx to hello.java                 */
/*   then will use javac to compile hello.java         */
/*                                                     */
/* OPTIONS keywords may be added (with a -) before or  */
/* after the file specification, along with the extra  */
/* flags known to NetRexxC (such as -keep).   For      */
/* example:                                            */
/*                                                     */
/*    NetRexxC -keep -format -comments hello           */
/*                                                     */
/* Invoke with no parameters for a full list of flags. */
/*                                                     */
/* To run the class after compilation, specify -run as */
/* an option word.  Each file will be run in turn.     */
/*                                                     */
/*    NetRexxC -run hello                              */
/*                                                     */
/* Multiple programs may be specified; in this case    */
/* they are all run (if requested) after all compiles. */
/*                                                     */
/* ----------                                          */
/* 1996.09.02 -- handle Warnings from NetRexxC (rc=1)  */
/* 1996.12.14 -- use COM.ibm.netrexx.process           */
/* 1998.05.25 -- pass NETREXX_JAVA setting to java.exe */
/* 2011.09.01 -- use org.netrexx.process               */
/* 2011.09.01 -- remove -xms4M                         */
/* 2023.03.03 -- set CLASSPATH relative to bin, if not set */

parse arg args
w=wordpos('-run', args)
if w>0 then do; run=1;   args=delword(args,w,1); end; else run=0
w=wordpos('-nocompile', args)
if w>0 then do; noc=1; end; else noc=0

/* ----- Translate & Compile ----- */
parse source system . sourcefile
select                             /* system-specific options */
  when system='OS/2' then do
    '@echo off'
    /* Add option -norestart for OS/2s 1.0.2+ java.exe, for better display */
    'java -version 2>&1|rxqueue'; parse pull '1.' subv ' '
    if subv>=0.2 then javaopts='-norestart'; else javaopts='-noasyncgc'

    /* Add any options from NETREXX_JAVA environment variable */
    nrjava=value('NETREXX_JAVA',,'OS2ENVIRONMENT')
    if nrjava\='' then javaopts=javaopts nrjava
    classpath=value('CLASSPATH',,'OS2ENVIRONMENT')
    env = 'OS2ENVIRONMENT'
    end
  otherwise
    /* Add any options from NETREXX_JAVA environment variable */
    javaopts=value('NETREXX_JAVA',,'ENVIRONMENT')
    classpath=value('CLASSPATH',,'ENVIRONMENT')
    env = 'ENVIRONMENT'
    pathsep=':'
    if pos('Windows', system) > 0 then pathsep=';' 
  end

if pos('NetRexxF.jar', javaopts) = 0 & pos('NetRexxC.jar', javaopts) = 0 then do
  if pos('NetRexxF.jar', classpath) = 0 & pos('NetRexxC.jar', classpath) = 0 then do
    newclasspath=substr(sourcefile, 1, length(sourcefile) - 12)'../lib/NetRexxF.jar'pathsep'.'pathsep''classpath
    value('CLASSPATH', newclasspath, env)
  end
end 

'java ' javaopts 'org.netrexx.process.NetRexxC' args

/* ----- Run ----- */
if rc<=1 & run then do
  if noc then say 'Run option ignored as -nocompile specified'
   else do
    /* This section of code is still OS/2-specific */
    do forever /* find the file parameters */
      parse var args file args
      if file='' then leave
      if left(file,1)='-' then iterate
      filename=filespec('n', file); parse var filename fn '.' fe
      /* Now determine the exact case of the class, for java commands */
      file=fn'.class'
      'dir' file '/n /b 2>nul | rxqueue'    /* use DIR to get exact case */
      if queued()<>1 then do
        if queued()=0 then say 'Cannot find file:' file
        if queued()>1 then do; do queued(); parse pull .; end
          say 'File "'file'" is not a unique specification'
          end
        exit 1; end
      parse pull file                         /* is now correct case */
      parse var file fn '.' fe
      say 'Running' fn '...'
      'java' fn
      end
    end
  end

exit rc

