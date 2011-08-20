set where=runall.log
echo Running Diags (-exec -nojava) and appending logs to %where%...


erase %where%

set do=java -ms4M %netrexx_java% COM.ibm.netrexx.process.NetRexxC
set opts=-time -exec -nocompile -nocrossref -nojava
set app=%where%

%do% %opts% DiagSay         >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagConcat      >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagIf          >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagStems       >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagArrays      >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagLoop        >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagSelect      >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagExpression  >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagConstant    >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagNop         >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagParse       >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagContinue    >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagComment     >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagForward     >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagMutual DiagMutual2 DiagMutual3 DiagMutual4 >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagAbstract    >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagUTF8 -utf8  >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagIndir       >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagMinor       >> %app%
echo ---------------------- >> %app%
%do% %opts% DiagRexx        >> %app%

echo Done
